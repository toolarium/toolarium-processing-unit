/*
 * ParallelProcessingUnit.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.runtime.runnable.parallelization;

import com.github.toolarium.common.object.IObjectLockManager;
import com.github.toolarium.common.object.ObjectLockManager;
import com.github.toolarium.common.statistic.StatisticCounter;
import com.github.toolarium.common.util.ThreadUtil;
import com.github.toolarium.processing.unit.IProcessingUnit;
import com.github.toolarium.processing.unit.IProcessingUnitContext;
import com.github.toolarium.processing.unit.IProcessingUnitPersistence;
import com.github.toolarium.processing.unit.IProcessingUnitProgress;
import com.github.toolarium.processing.unit.IProcessingUnitStatus;
import com.github.toolarium.processing.unit.ProcessingUnitStatusBuilder;
import com.github.toolarium.processing.unit.base.AbstractProcessingUnitPersistenceImpl;
import com.github.toolarium.processing.unit.dto.Parameter;
import com.github.toolarium.processing.unit.dto.ParameterDefinition;
import com.github.toolarium.processing.unit.exception.ProcessingException;
import com.github.toolarium.processing.unit.exception.ValidationException;
import com.github.toolarium.processing.unit.parallelization.IParallelProcessingUnit;
import com.github.toolarium.processing.unit.parallelization.IProcessingUnitObjectLockManagerSupport;
import com.github.toolarium.processing.unit.runtime.runnable.EmptyProcessingUnitHandler;
import com.github.toolarium.processing.unit.runtime.runnable.IEmptyProcessingUnitHandler;
import com.github.toolarium.processing.unit.util.ProcessingUnitStatusUtil;
import com.github.toolarium.processing.unit.util.ProcessingUnitUtil;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Implements a parallel processing unit which let run a processing unit with multiple threads. A processing unit has just to implement 
 * the interface {@link IParallelProcessingUnit} which acts as marker interface. In case the processing unit needs 
 * an {@link IObjectLockManager} it can additional implement the interface {@link IProcessingUnitObjectLockManagerSupport}.
 * 
 * @author patrick
 */
public class ParallelProcessingUnit extends AbstractProcessingUnitPersistenceImpl<ParallelProcessingUnitPersistenceContainer> implements ParallelProcessingUnitParameters, IParallelProcessingUnit, UncaughtExceptionHandler {
    
    private static final Logger LOG = LoggerFactory.getLogger(ParallelProcessingUnit.class);
    private String id;
    private String name;
    private String processInfo;
    private Class<? extends IProcessingUnit> processingUnitClass;
    private List<IProcessingUnit> processingUnitList;
    private List<Parameter> processingUnitParameterList;
    private ExecutorService executorService;
    private volatile boolean isInterrupted;
    private DecimalFormat decimalFormatter;
    private int lastPercentage;
    private BlockingQueue<Throwable> runnerThreadExceptionQueue;
    private List<RunnerThreadProcessStatusQueue> runnerThreadStatusQueueList;
    private IObjectLockManager objectKockManager;
    private EmptyProcessingUnitHandler emptyProcessingUnitHandler;
    private IProcessingUnitStatus suspendProcessingUnitStatus;
    

    /**
     * Constructor for ParallelProcessingUnit
     * 
     * @param id the unique id of the processing
     * @param name the name of the processing
     * @param processingUnitClass the processing unit class
     */
    public ParallelProcessingUnit(String id, String name, Class<? extends IProcessingUnit> processingUnitClass) {
        this.id = id;
        this.name = name;
        this.processInfo = ProcessingUnitUtil.getInstance().toString(id, name, processingUnitClass); 
        this.processingUnitClass = processingUnitClass;
        this.processingUnitList = null;
        this.processingUnitParameterList = null;
        this.executorService = null;
        this.isInterrupted = false;
        this.decimalFormatter = new DecimalFormat("#########0.00");
        this.decimalFormatter.setMinimumFractionDigits(0);
        this.decimalFormatter.setGroupingUsed(false);
        this.lastPercentage = 0;
        this.runnerThreadExceptionQueue = new LinkedBlockingQueue<Throwable>();
        this.runnerThreadStatusQueueList = null;
        this.objectKockManager = null;      
        this.emptyProcessingUnitHandler = new EmptyProcessingUnitHandler();
        this.suspendProcessingUnitStatus = null;
    }

    
    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#getParameterDefinition()
     */
    @Override
    public List<ParameterDefinition> getParameterDefinition() {
        // get parameters from the processing class
        final String defName = createInstanceName("parameterDefinition");
        IProcessingUnit processingUnit = ProcessingUnitUtil.getInstance().createSingleProcessingUnitInstance(id, defName, processingUnitClass);
        if (processingUnit != null && processingUnit.getParameterDefinition() != null) {
            for (ParameterDefinition parameterDefinition : processingUnit.getParameterDefinition()) {
                getParameterRuntime().addParameterDefinition(parameterDefinition);
                ThreadUtil.getInstance().sleep(1000L);
            }
        }
        ProcessingUnitUtil.getInstance().releaseResource(id, defName, processingUnit);
        processingUnit = null;
        
        // add parallel processing parameters
        getParameterRuntime().addParameterDefinition(NUMBER_OF_THREAD_PARAMETER);
        getParameterRuntime().addParameterDefinition(LOCK_SIZE);
        getParameterRuntime().addParameterDefinition(UNLOCK_TIMEOUT);
        getParameterRuntime().addParameterDefinition(STARTUP_PHASED_SLEEP_TIME);
        getParameterRuntime().addParameterDefinition(AGGREGATE_STATUS_PAUSE_TIME);
        getParameterRuntime().addParameterDefinition(NO_PROGRESS_PAUSE_TIME);
        getParameterRuntime().addParameterDefinition(MAX_NUMBER_OF_NO_PROGRESS_BEFORE_ABORT);
        
        return super.getParameterDefinition();
    }


    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#initialize(java.util.List, com.github.toolarium.processing.unit.IProcessingUnitContext)
     */
    @Override
    public void initialize(List<Parameter> parameterList, IProcessingUnitContext processingUnitContext) throws ValidationException {
        super.initialize(parameterList, processingUnitContext);
        
        // prepare processing unit parameter list
        this.processingUnitParameterList = new ArrayList<Parameter>();
        for (Parameter p : parameterList) {
            if (NUMBER_OF_THREAD_PARAMETER.getKey().equals(p.getKey())
                || LOCK_SIZE.getKey().equals(p.getKey())
                || UNLOCK_TIMEOUT.getKey().equals(p.getKey())
                || STARTUP_PHASED_SLEEP_TIME.getKey().equals(p.getKey())
                || AGGREGATE_STATUS_PAUSE_TIME.getKey().equals(p.getKey())
                || NO_PROGRESS_PAUSE_TIME.getKey().equals(p.getKey())
                || MAX_NUMBER_OF_NO_PROGRESS_BEFORE_ABORT.getKey().equals(p.getKey())) {
                // NOP
            } else {
                processingUnitParameterList.add(p);
            }
        }

        this.emptyProcessingUnitHandler.setSleepTimeAfterEmptyProcessingUnit(getParameterRuntime().getParameterValueList(NO_PROGRESS_PAUSE_TIME).getValueAsLong());
        this.emptyProcessingUnitHandler.setMaxNumberOfEmptyProcessingUnits(getParameterRuntime().getParameterValueList(MAX_NUMBER_OF_NO_PROGRESS_BEFORE_ABORT).getValueAsLong());
        
        // create instances
        this.processingUnitList = createProcessingUnitInstances(processingUnitClass, getParameterRuntime().getParameterValueList(NUMBER_OF_THREAD_PARAMETER).getValueAsInteger());

        // initialize all parallel processing unit's
        if (processingUnitList != null) {
            for (IProcessingUnit processingUnit : processingUnitList) {
                processingUnit.initialize(processingUnitParameterList, processingUnitContext);
            }
        }
    }

    
    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#estimateNumberOfUnitsToProcess()
     */
    @Override
    public long estimateNumberOfUnitsToProcess() throws ProcessingException {
        return getProcessingUnitProgress().setNumberOfUnitsToProcess(getProcessingUnit().estimateNumberOfUnitsToProcess());
    }


    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#processUnit(com.github.toolarium.processing.unit.ProcessingUnitStatusBuilder)
     */
    @Override
    public IProcessingUnitStatus processUnit(ProcessingUnitStatusBuilder processingUnitStatusBuilder) throws ProcessingException {
        if (!isThreadPoolStarted()) {
            startThreadPool();
        }

        if (suspendProcessingUnitStatus != null) {
            // in case suspending processing unit status exist take it up
            ProcessingUnitStatusUtil.getInstance().aggregateProcessingUnitStatus(processingUnitStatusBuilder, suspendProcessingUnitStatus);
            suspendProcessingUnitStatus = null;
        }

        // sleep
        ThreadUtil.getInstance().sleep(getParameterRuntime().getParameterValueList(AGGREGATE_STATUS_PAUSE_TIME).getValueAsLong());
        
        // get status from runner threads and aggregate it
        aggregateProcessingUnitStatus(processingUnitStatusBuilder);
        
        final int percentage = getProcessingUnitProgress().getProgress();
        if (percentage > 0 && percentage <= 100 && (percentage % 10 == 0)) {
            logObjectLockStatistic();
        }

        // check if exception occured in runner threads
        checkExceptionInThreads();

        return processingUnitStatusBuilder.build();
    }


    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#onEnding()
     */
    @Override
    public void onEnding() {
        if (processingUnitList != null) {
            for (IProcessingUnit processingUnit : processingUnitList) {
                try {
                    processingUnit.onEnding();
                } catch (RuntimeException e) {
                    LOG.warn(processInfo + " Could not call onEnding: " + e.getMessage(), e);
                }
            }
        }

        logObjectLockStatistic();
    }

    
    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#onAborting()
     */
    @Override
    public void onAborting() {
        waitForThreadPoolTerminated();

        if (processingUnitList != null) {
            for (IProcessingUnit processingUnit : processingUnitList) {
                try {
                    processingUnit.onAborting();
                } catch (RuntimeException e) {
                    LOG.warn(processInfo + " Could not call onAborting: " + e.getMessage(), e);
                }
            }
        }

        logObjectLockStatistic();
    }

    
    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#releaseResource()
     */
    @Override
    public void releaseResource() throws ProcessingException {
        super.releaseResource();

        if (processingUnitList != null) {
            for (int i = 0; i < processingUnitList.size(); i++) {
                try {
                    ProcessingUnitUtil.getInstance().releaseResource(id, createInstanceName("" + (i + 1)), processingUnitList.get(i));
                } catch (Exception t) {
                    throw new ValidationException("Could not release " + processingUnitClass.getName() + ": " + t.getMessage(), t);
                }
            }
        }

        if (objectKockManager != null) {
            try {
                objectKockManager.releaseResource();
            } catch (RuntimeException e) {
                LOG.warn(processInfo + " Could not release resource object lock manager: " + e.getMessage(), e);
            }
        }
    }

    
    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#suspendProcessing()
     */
    @Override
    public IProcessingUnitPersistence suspendProcessing() throws ProcessingException {
        waitForThreadPoolTerminated();

        LOG.info(processInfo + " Start suspend processing units...");
        for (IProcessingUnit processingUnit : processingUnitList) {
            getProcessingPersistence().addProcessingUnitPersistence(processingUnit.suspendProcessing());
        }

        // get status from runner threads and aggregate it
        getProcessingPersistence().setSuspendProcessingUnitStatus(aggregateProcessingUnitStatus(new ProcessingUnitStatusBuilder()).build());
        
        getProcessingPersistence().setObjectLockManager(objectKockManager);
        return getProcessingPersistence();
    }

    
    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#resumeProcessing(com.github.toolarium.processing.unit.IProcessingUnitProgress, com.github.toolarium.processing.unit.IProcessingUnitPersistence)
     */
    @Override
    public void resumeProcessing(IProcessingUnitProgress processingUnitProgress, IProcessingUnitPersistence processingPersistence) throws ProcessingException {
        super.resumeProcessing(processingUnitProgress, processingPersistence);

        LOG.info(processInfo + " Resume all procesing units...");
        
        if (getProcessingPersistence().getProcessingUnitPersistenceList().size() != processingUnitList.size()) {
            throw new ProcessingException("Can not resume processing unit because of different size: " 
                                            + processingUnitList.size() 
                                            + ", persisted state size: " 
                                            + getProcessingPersistence().getProcessingUnitPersistenceList().size(), true);
        }

        // restore the lock manager
        this.objectKockManager = getProcessingPersistence().getObjectLockManager();

        // set the suspend processing status
        suspendProcessingUnitStatus = getProcessingPersistence().getSuspendProcessingUnitStatus();

        int i = 0;
        for (IProcessingUnitPersistence processingUnitPersistence : getProcessingPersistence().getProcessingUnitPersistenceList()) {
            LOG.info(processInfo + " Resume processing unit parallelization thread #" + i + "...");
            processingUnitList.get(i++).resumeProcessing(processingUnitProgress, processingUnitPersistence);
        }
        
        // remove persistence instance
        super.removePersistenceInstance();
    }

    
    /**
     * Get the empty processing unit handler 
     *
     * @return the empty processing unit handler
     */
    public IEmptyProcessingUnitHandler getEmptyProcessingUnitHandler() {
        return emptyProcessingUnitHandler;
    }

    
    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#setObjectLockManager(com.github.toolarium.common.object.IObjectLockManager)
     */
    @Override
    public void setObjectLockManager(IObjectLockManager objectLockManager) {
        this.objectKockManager = objectLockManager;
        this.objectKockManager.setObjectLockSize(getParameterRuntime().getParameterValueList(LOCK_SIZE).getValueAsInteger());
        this.objectKockManager.setUnlockTimeout(getParameterRuntime().getParameterValueList(UNLOCK_TIMEOUT).getValueAsLong());

        // initialize all parallel processing unit's
        if (processingUnitList != null) {
            for (IProcessingUnit processingUnit : processingUnitList) {
                if (IProcessingUnitObjectLockManagerSupport.class.isAssignableFrom(processingUnit.getClass())) {
                    ((IProcessingUnitObjectLockManagerSupport) processingUnit).setObjectLockManager(objectKockManager);
                }
            }
        }
    }

    
    /**
     * @see java.lang.Thread.UncaughtExceptionHandler#uncaughtException(java.lang.Thread, java.lang.Throwable)
     */
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        LOG.error(processInfo + " Uncaught exception in thread (id=" + t.getId() + ", name=" + t.getName() + "): " + e.getMessage(), e);
        runnerThreadExceptionQueue.offer(e);
    }
    
       
    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitPersistenceImpl#newPersistenceInstance()
     */
    @Override
    protected ParallelProcessingUnitPersistenceContainer newPersistenceInstance() {
        return new ParallelProcessingUnitPersistenceContainer();
    }

    
    /**
     * Aggregate all processing unit status 
     * 
     * @param processingUnitStatusBuilder the processing status builder
     * @return the processing status builder
     */
    protected ProcessingUnitStatusBuilder aggregateProcessingUnitStatus(ProcessingUnitStatusBuilder processingUnitStatusBuilder) {
        for (RunnerThreadProcessStatusQueue statusQueue : runnerThreadStatusQueueList) {
            IProcessingUnitStatus processingUnitStatus = statusQueue.getProcessingUnitStatus();
            while (processingUnitStatus != null && !isThreadPoolTerminated()) {
                ProcessingUnitStatusUtil.getInstance().aggregateProcessingUnitStatus(processingUnitStatusBuilder, processingUnitStatus);
                processingUnitStatus = statusQueue.getProcessingUnitStatus();
            }
        }

        processingUnitStatusBuilder.hasNextIfHasUnprocessedUnits();
        processingUnitStatusBuilder.hasNext(processingUnitStatusBuilder.hasNext() || !isThreadPoolTerminated());
        return processingUnitStatusBuilder;
    }

    
    /**
     * Check for exceptions in runner threads
     * 
     * @throws ProcessingException in case of an error
     */
    protected void checkExceptionInThreads() throws ProcessingException {
        if (runnerThreadExceptionQueue.size() > 0) {
            waitForThreadPoolTerminated();

            ArrayList<Throwable> exceptionList = new ArrayList<Throwable>();
            runnerThreadExceptionQueue.drainTo(exceptionList);
            String message = "";
            for (Throwable throwable : exceptionList) {
                message += throwable.getMessage() + "\n";
            }

            throw new ProcessingException("Processing exception: " + message, exceptionList.get(0), true);
        }
    }

    
    /**
     * Start thread pool
     */
    protected void startThreadPool() {
        if (isThreadPoolStarted()) {
            return;
        }

        // init thread pool
        String threadName = ProcessingUnitRunnerThread.class.getName() + ": Parallel Processing Unit Runner (Parent: " + Thread.currentThread().getName() + ")";
        executorService = Executors.newFixedThreadPool(processingUnitList.size(), new ProcessingUnintRunnerThreadFactory(Executors.defaultThreadFactory(), threadName, this));
        runnerThreadStatusQueueList = new ArrayList<RunnerThreadProcessStatusQueue>();

        if (this.objectKockManager == null) {
            this.objectKockManager = new ObjectLockManager();
        }
        this.objectKockManager.setObjectLockSize(getParameterRuntime().getParameterValueList(LOCK_SIZE).getValueAsInteger());
        this.objectKockManager.setUnlockTimeout(getParameterRuntime().getParameterValueList(UNLOCK_TIMEOUT).getValueAsLong());

        int number = 0;
        long noProgressPauseTime = getParameterRuntime().getParameterValueList(NO_PROGRESS_PAUSE_TIME).getValueAsLong();
        for (IProcessingUnit processingUnit : processingUnitList) {
            if (IProcessingUnitObjectLockManagerSupport.class.isAssignableFrom(processingUnit.getClass())) {
                ((IProcessingUnitObjectLockManagerSupport) processingUnit).setObjectLockManager(objectKockManager);
            }
            
            BlockingQueue<IProcessingUnitStatus> processStatusQueue = new LinkedBlockingQueue<IProcessingUnitStatus>();
            executorService.execute(new ProcessingUnitRunnerThread(processingUnit, ++number, processStatusQueue, noProgressPauseTime));
            runnerThreadStatusQueueList.add(new RunnerThreadProcessStatusQueue(number, processStatusQueue));

            ThreadUtil.getInstance().sleep(getParameterRuntime().getParameterValueList(STARTUP_PHASED_SLEEP_TIME).getValueAsLong());
        }

        // execute previously submitted tasks but accept no new ones
        executorService.shutdown();
    }

    
    /**
     * Wait for thread pool terminated.
     */
    protected void waitForThreadPoolTerminated() {
        LOG.info(processInfo + " Interrupt all procesing unit threads...");
        isInterrupted = true;

        while (!isThreadPoolTerminated()) {
            LOG.debug(processInfo + " Wait on threads...");
            ThreadUtil.getInstance().sleep(500L);
        }
        LOG.info(processInfo + " All threads stopped.");
    }

    
    /**
     * Log the object lock statistic
     */
    protected void logObjectLockStatistic() {
        final int percentage = getProcessingUnitProgress().getProgress();
        if (lastPercentage == percentage) {
            return;
        }

        if (objectKockManager != null) {
            lastPercentage = percentage;
            LOG.info(processInfo + " Object lock statistic (progress " + percentage + "%):\n"
                     + "   lock size average                    : " + prepareAverage(objectKockManager.getLockStatistic()) + "\n"
                     + "   already locked hit size average      : " + prepareAverage(objectKockManager.getIgnoreLockStatistic()) + "\n"
                     + "   blocked to unlocked hit size average : " + prepareAverage(objectKockManager.getUnlockStatistic()) + "\n"
                     + "   count of object lock size reached    : " + objectKockManager.getNumberOfLockSizeReached());
        }
    }

    
    /**
     * Gets the isInterrupted
     *
     * @return Returns the isInterrupted
     */
    boolean isInterrupted() {
        return isInterrupted;
    }

    
    /**
     * Check if the thread pool is started
     *
     * @return the thread pool
     */
    private boolean isThreadPoolStarted() {
        return executorService != null;
    }

    
    /**
     * Check if the thread pool is terminated
     *
     * @return true if it is terminated
     */
    private boolean isThreadPoolTerminated() {
        return (executorService == null || executorService.isTerminated());
    }

    
    /**
     * Prepare the average value as string of a statistic counter
     *
     * @param statisticCounter the statistic counter
     * @return the string representation
     */
    private String prepareAverage(StatisticCounter statisticCounter) {
        if (statisticCounter == null) {
            return "(n/a)";
        }

        return decimalFormatter.format(statisticCounter.getAverage());
    }    

    
    /**
     * Create the processing unit implementation
     *
     * @param processingUnitClass the class
     * @param inputNumberOfInstances the number of instances
     * @return the instance
     * @throws ValidationException If the instance of the processing unit cannot be initialized correctly 
     */
    private List<IProcessingUnit> createProcessingUnitInstances(Class<? extends IProcessingUnit> processingUnitClass, Integer inputNumberOfInstances) throws ValidationException {
        
        int numberOfInstances = 1;
        if (inputNumberOfInstances != null && inputNumberOfInstances > 0) {
            numberOfInstances = inputNumberOfInstances;
        }
        
        List<IProcessingUnit> processingUnitList = new ArrayList<IProcessingUnit>(numberOfInstances);
        for (int i = 0; i < numberOfInstances; i++) {
            try {
                processingUnitList.add(ProcessingUnitUtil.getInstance().createSingleProcessingUnitInstance(id, createInstanceName("" + (i + 1)), processingUnitClass));
            } catch (Exception t) {
                throw new ValidationException("Could not initialize " + processingUnitClass.getName() + ": " + t.getMessage(), t);
            }
        }
        
        return processingUnitList;
    }


    /**
     * Create instance name
     *
     * @param instanceId the instance id
     * @return the prepared instance name
     */
    private String createInstanceName(String instanceId) {
        StringBuilder builder = new StringBuilder();
        if (name != null && !name.isBlank()) {
            builder.append(name).append("-");
        }
        
        builder.append("#").append(instanceId);
        return builder.toString();
    }
    

    /**
     * Get the first processing unit
     * 
     * @return the processing unit
     */
    private IProcessingUnit getProcessingUnit() {
        return processingUnitList.get(0);
    }  
   
    
    /**
     * Defines the processing unit runner
     */
    class ProcessingUnitRunnerThread implements Runnable {
        private IProcessingUnit processingUnit;
        private BlockingQueue<IProcessingUnitStatus> processStatusQueue;
        private int number;
        private long noProgressPauseTime;


        /**
         * Constructor
         *
         * @param processingUnit the processing unit
         * @param number the thread number
         * @param processStatusQueue the process status queue
         * @param noProgressPauseTime the pause time on no progress
         */
        ProcessingUnitRunnerThread(IProcessingUnit processingUnit, int number, BlockingQueue<IProcessingUnitStatus> processStatusQueue, long noProgressPauseTime) {
            this.processingUnit = processingUnit;
            this.number = number;
            this.noProgressPauseTime = noProgressPauseTime;
            this.processStatusQueue = processStatusQueue;
        }


        /**
         * @see java.lang.Runnable#run()
         */
        @Override
        public void run() {
            LOG.info(processInfo + " Start processing unit parallelization thread #" + number + "...");

            long lastProgress = getProcessingUnitProgress().getProgress();
            boolean hasNext = false;
            do {

                IProcessingUnitStatus processStatus = processingUnit.processUnit();
                try {
                    processStatusQueue.put(processStatus);
                    lastProgress = getProcessingUnitProgress().getProgress();
                    hasNext = processStatus.hasNext();

                    if (getProcessingUnitProgress().getProgress() == lastProgress) {
                        ThreadUtil.getInstance().sleep(noProgressPauseTime);
                    }
                } catch (InterruptedException e) {
                    LOG.debug("Interrupt: " + e.getMessage(), e);
                }
            } while (!isThreadInterrupted() && hasNext);

            if (isInterrupted()) {
                LOG.info(processInfo + " Processing unit parallelization thread #" + number + "  interrupted!");
            } else {
                LOG.info(processInfo + " Processing unit parallelization thread #" + number + " ended.");
            }
        }


        /**
         * Check if it is interrupted
         *
         * @return true if it is interrupted
         */
        private boolean isThreadInterrupted() {
            return Thread.currentThread().isInterrupted() || isInterrupted();
        }
    }

    
    /**
     * Defines the processing unit runner thread factory
     */
    class ProcessingUnintRunnerThreadFactory implements ThreadFactory {
        private ThreadFactory threadFactory;
        private String threadName;
        private UncaughtExceptionHandler uncaughtExceptionHandler;
        private int threadCounter = 1;

       
        /**
         * Constructor
         *
         * @param threadFactory the thread factory
         * @param threadName the thread name
         * @param uncaughtExceptionHandler the uncaught exception handler
         */
        ProcessingUnintRunnerThreadFactory(ThreadFactory threadFactory, String threadName, UncaughtExceptionHandler uncaughtExceptionHandler) {
            this.threadFactory = threadFactory;
            this.threadName = threadName;
            this.uncaughtExceptionHandler = uncaughtExceptionHandler;
        }

        /**
         * @see java.util.concurrent.ThreadFactory#newThread(java.lang.Runnable)
         */
        @Override
        public Thread newThread(Runnable r) {
            Thread newThread = threadFactory.newThread(r);
            // make threadname unique (required for jptools profiling)
            newThread.setName(threadName + " " + threadCounter++);
            newThread.setUncaughtExceptionHandler(uncaughtExceptionHandler);
            return newThread;
        }
    }


    /**
     * Defines the runner thread
     */
    class RunnerThreadProcessStatusQueue {
        private final int threadNumber;
        private BlockingQueue<IProcessingUnitStatus> processingUnitStatusQueue;


        /**
         * Constructor
         *
         * @param threadNumber the thread number
         * @param processingUnitStatusQueue the queue
         */
        RunnerThreadProcessStatusQueue(int threadNumber, BlockingQueue<IProcessingUnitStatus> processingUnitStatusQueue) {
            this.threadNumber = threadNumber;
            this.processingUnitStatusQueue = processingUnitStatusQueue;
        }

        
        /**
         * Get the thread number
         *
         * @return the thread number
         */
        public int getThreadNumber() {
            return threadNumber;
        }

        
        /**
         * Get the data process status
         *
         * @return the data process status
         */
        public IProcessingUnitStatus getProcessingUnitStatus() {
            try {
                return processingUnitStatusQueue.poll(100L, TimeUnit.MILLISECONDS);
                //return processingUnitStatusQueue.take();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            return null;
        }
    }
}
