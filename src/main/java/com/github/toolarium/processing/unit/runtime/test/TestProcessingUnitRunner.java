/*
 * TestProcessingUnitRunner.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.runtime.test;

import com.github.toolarium.common.bandwidth.IBandwidthThrottling;
import com.github.toolarium.common.util.TextUtil;
import com.github.toolarium.common.util.ThreadUtil;
import com.github.toolarium.processing.unit.IProcessingUnit;
import com.github.toolarium.processing.unit.IProcessingUnitContext;
import com.github.toolarium.processing.unit.IProcessingUnitProgress;
import com.github.toolarium.processing.unit.dto.Parameter;
import com.github.toolarium.processing.unit.dto.ProcessingActionStatus;
import com.github.toolarium.processing.unit.dto.ProcessingRuntimeStatus;
import com.github.toolarium.processing.unit.exception.ProcessingException;
import com.github.toolarium.processing.unit.exception.ValidationException;
import com.github.toolarium.processing.unit.runtime.IProcessingUnitRuntimeTimeMeasurement;
import com.github.toolarium.processing.unit.runtime.ProcessingUnitContext;
import com.github.toolarium.processing.unit.runtime.runnable.IEmptyProcessingUnitHandler;
import com.github.toolarium.processing.unit.util.ProcessingUnitUtil;
import java.io.Serializable;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Implements a simple processing unit runner. It's made to use in unit tests.
 *
 * @author patrick
 */
public class TestProcessingUnitRunner implements Serializable {
    private static final long serialVersionUID = 7297801281265114031L;
    private static final Logger LOG = LoggerFactory.getLogger(TestProcessingUnitRunner.class);
    private TestProcessingUnitRunnable processingUnitRunnable;
    private int suspendCounter = 0;
    private IProcessingUnitContext processingContext;

    
    /**
     * Constructor
     */
    protected TestProcessingUnitRunner() {
        processingUnitRunnable = null;
        processingContext = new ProcessingUnitContext();
    }


    /**
     * Run processing unit
     *
     * @param processingUnitClass the processing unit class
     * @param parameterList the parameter list
     * @return the number of cycles
     * @throws ValidationException In case the processing can not be initilized 
     * @throws ProcessingException In case of an error in a processing
     */
    public long run(Class<? extends IProcessingUnit> processingUnitClass, List<Parameter> parameterList) throws ValidationException, ProcessingException {
        processingUnitRunnable = new TestProcessingUnitRunnable(processingUnitClass, parameterList, processingContext);
        processingUnitRunnable.run();
        return processingUnitRunnable.getProcessingUnitProgress().getNumberOfProcessedUnits();
    }

    
    /**
     * Run processing unit and abort after some cycles
     *
     * @param processingUnitClass the processing unit class
     * @param parameterList the parameter list
     * @param numberOfCyclesBeforeStop the number of cycles before stop
     * @return the number of cycles
     * @throws ValidationException In case the processing can not be initilized 
     * @throws ProcessingException In case of an error in a processing
     */
    public long runAndAbort(Class<? extends IProcessingUnit> processingUnitClass, List<Parameter> parameterList, Integer numberOfCyclesBeforeStop) throws ValidationException, ProcessingException {
        processingUnitRunnable = new TestProcessingUnitRunnable(processingUnitClass, parameterList, processingContext);
        processingUnitRunnable.setNumberOfCyclesBeforeStop(numberOfCyclesBeforeStop);
        processingUnitRunnable.run();
        return processingUnitRunnable.getProcessingUnitProgress().getNumberOfProcessedUnits();
    }

    
    /**
     * Run processing unit and abort after some cycles
     *
     * @param processingUnitClass the processing unit class
     * @param parameterList the parameter list
     * @param maxNumberOfProcessingUnitCallsPerSecond the max number of calls per second of the processing unit
     * @return the number of cycles
     * @throws ValidationException In case the processing can not be initilized 
     * @throws ProcessingException In case of an error in a processing
     */
    public long runWithThrottling(Class<? extends IProcessingUnit> processingUnitClass, List<Parameter> parameterList, Long maxNumberOfProcessingUnitCallsPerSecond) throws ValidationException, ProcessingException {
        processingUnitRunnable = new TestProcessingUnitRunnable(processingUnitClass, parameterList, processingContext);
        processingUnitRunnable.setProcessingUnitThrottling(maxNumberOfProcessingUnitCallsPerSecond);
        processingUnitRunnable.run();
        return processingUnitRunnable.getProcessingUnitProgress().getNumberOfProcessedUnits();
    }

    
    /**
     * Run processing unit
     *
     * @param processingUnitClass the processing unit class
     * @param parameterList the parameter list
     * @param suspendAfterCycles after the number of cycles it will suspended
     * @param suspendSleepTime the sleep time after suspending or null
     * @param maxNumberOfSuspends the max number of suspends
     * @return the number of cycles
     * @throws ValidationException In case the processing can not be initilized 
     * @throws ProcessingException In case of an error in a processing
     */
    public long runWithSuspendAndResume(Class<? extends IProcessingUnit> processingUnitClass,
                                        List<Parameter> parameterList,
                                        long suspendAfterCycles,
                                        long suspendSleepTime,
                                        int maxNumberOfSuspends) throws ValidationException, ProcessingException {
        return runWithSuspendAndResume(processingUnitClass, parameterList, suspendAfterCycles, suspendSleepTime, maxNumberOfSuspends, null);
    }

    /**
     * Run processing unit
     *
     * @param processingUnitClass the processing unit class
     * @param parameterList the parameter list
     * @param suspendAfterCycles after the number of cycles it will suspended
     * @param suspendSleepTime the sleep time after suspending or null
     * @param maxNumberOfSuspends the max number of suspends
     * @param maxNumberOfProcessingUnitCallsPerSecond the max number of calls per second of the processing unit
     * @return the number of cycles
     * @throws ValidationException In case the processing can not be initilized 
     * @throws ProcessingException In case of an error in a processing
     */
    public long runWithSuspendAndResume(Class<? extends IProcessingUnit> processingUnitClass,
                                        List<Parameter> parameterList,
                                        long suspendAfterCycles,
                                        long suspendSleepTime,
                                        int maxNumberOfSuspends,
                                        Long maxNumberOfProcessingUnitCallsPerSecond) throws ValidationException, ProcessingException {

        processingUnitRunnable = new TestProcessingUnitRunnable(processingUnitClass, parameterList, processingContext);
        processingUnitRunnable.setProcessingUnitThrottling(maxNumberOfProcessingUnitCallsPerSecond);
        processingUnitRunnable.setSuspendAfterCycles(suspendAfterCycles);

        byte[] suspendedState = null;
        while (suspendCounter < maxNumberOfSuspends) {
            processingUnitRunnable.run();
            
            if (ProcessingActionStatus.ENDED.equals(processingUnitRunnable.getProcessingActionStatus()) || ProcessingActionStatus.ABORTED.equals(processingUnitRunnable.getProcessingActionStatus())) {
                break;
            } else {
                // suspend & resume
                suspendedState = processingUnitRunnable.getSuspendedState();
                
                LOG.info("Suspended state: " + TextUtil.NL + ProcessingUnitUtil.getInstance().toString(suspendedState));
                suspendCounter++;
                processingUnitRunnable = null;

                if (suspendedState != null && suspendCounter < maxNumberOfSuspends) {
                    // sleep..
                    ThreadUtil.getInstance().sleep(suspendSleepTime);
                    
                    processingUnitRunnable = new TestProcessingUnitRunnable(suspendedState);
                    processingUnitRunnable.setSuspendAfterCycles(suspendAfterCycles);
                } else {
                    processingUnitRunnable = new TestProcessingUnitRunnable(suspendedState);
                    processingUnitRunnable.run();
                }
            }
        }
        
        return processingUnitRunnable.getProcessingUnitProgress().getNumberOfProcessedUnits();
    }


    /**
     * Set the processing context
     *
     * @param processingContext the processing context
     * @return this instance
     */
    public TestProcessingUnitRunner processingUnitContext(IProcessingUnitContext processingContext) {
        this.processingContext = processingContext;
        return this;
    }

    
    /**
     * Gets the suspend counter
     *
     * @return the suspend counter
     */
    public int getSuspendCounter() {
        if (processingUnitRunnable != null) {
            return suspendCounter; //processingUnitRunnable.getSuspendCounter();
        }
        
        return 0;
    }

        
    /**
     * Get the runnable id
     *
     * @return the runnable id
     */
    public String getId() {
        return processingUnitRunnable.getId();
    }

    
    /**
     * Get the runnable name or null
     *
     * @return the name or null
     */
    public String getName() {
        return processingUnitRunnable.getName();
    }

    
    /**
     * Get the processing action status
     * 
     * @return the processing action status
     */
    public ProcessingActionStatus getProcessingActionStatus() {
        return processingUnitRunnable.getProcessingActionStatus();
    }
    

    /**
     * Gets the processing unit progress
     *
     * @return the processing unit progress
     */
    public IProcessingUnitProgress getProcessingUnitProgress() {
        return processingUnitRunnable.getProcessingUnitProgress();
    }

    
    /**
     * Get the processing runtime status
     * 
     * @return the processing runtime status
     */
    public ProcessingRuntimeStatus getProcessingRuntimeStatus() {
        return processingUnitRunnable.getProcessingRuntimeStatus();
    }

    
    /**
     * Get the status message list
     *
     * @return the status message list
     */
    public List<String> getStatusMessageList() {
        return processingUnitRunnable.getStatusMessageList();
    }

    
    /**
     * Get the time measurement
     *
     * @return the time measurement
     */
    public IProcessingUnitRuntimeTimeMeasurement getTimeMeasurement() {
        return processingUnitRunnable.getTimeMeasurement(); 
    }


    /**
     * Get the processing unit.
     * IMPORTANT: Only to verify some results
     * 
     * @param <T> the processing unit type
     * @return the processing unit
     */
    @SuppressWarnings("unchecked")
    public <T extends IProcessingUnit> T getProcesingUnit() {
        if (processingUnitRunnable != null) {
            return (T)processingUnitRunnable.getProcessingUnit();
        }
        
        return null;
    }
    
    
    /**
     * Get the processing parameter list.
     *
     * @return the processing parameter list
     */
    public List<Parameter> getParameterList() {
        return processingUnitRunnable.getParameterList();
    }

    
    /**
     * Get the processing unit throttling
     *
     * @return the processing unit throttling
     */
    public IBandwidthThrottling getProcessingUnitThrottling() {
        return processingUnitRunnable.getProcessingUnitThrottling();
    }

    
    /** 
     * Get the empty processing unit handler
     *
     * @return the empty processing unit handler
     */
    public IEmptyProcessingUnitHandler getEmptyProcessingUnitHandler() {
        return processingUnitRunnable.getEmptyProcessingUnitHandler();
    }

    
    /**
     * Set the empty processing unit handler
     *
     * @param emptyProcessingUnitHandler the empty processing unit handler
     */
    public void setEmptyProcessingUnitHandler(IEmptyProcessingUnitHandler emptyProcessingUnitHandler) {
        this.processingUnitRunnable.setEmptyProcessingUnitHandler(emptyProcessingUnitHandler);
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return processingUnitRunnable.toString();
    }
}
