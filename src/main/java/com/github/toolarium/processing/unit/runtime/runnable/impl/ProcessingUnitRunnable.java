/*
 * ProcessingUnitRunnable.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.runtime.runnable.impl;

import com.github.toolarium.common.bandwidth.IBandwidthThrottling;
import com.github.toolarium.processing.unit.IProcessingUnit;
import com.github.toolarium.processing.unit.IProcessingUnitContext;
import com.github.toolarium.processing.unit.dto.Parameter;
import com.github.toolarium.processing.unit.dto.ProcessingActionStatus;
import com.github.toolarium.processing.unit.exception.ProcessingException;
import com.github.toolarium.processing.unit.exception.ValidationException;
import com.github.toolarium.processing.unit.runtime.runnable.IProcessingUnitProxy;
import com.github.toolarium.processing.unit.runtime.runnable.IProcessingUnitRunnable;
import com.github.toolarium.processing.unit.runtime.runnable.IProcessingUnitRunnableListener;
import com.github.toolarium.processing.unit.runtime.runnable.IProcessingUnitThrottling;
import com.github.toolarium.processing.unit.runtime.runnable.ProcessingUnitProxy;
import com.github.toolarium.processing.unit.util.ProcessingUnitUtil;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Implements a {@link IProcessingUnitRunnable}. 
 * 
 * @author patrick
 */
public class ProcessingUnitRunnable extends AbstractProcessingUnitRunnable implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(ProcessingUnitRunnable.class);
    private volatile boolean suspend = false;
    private byte[] suspendedState = null;
    private IProcessingUnitThrottling processingUnitThrottling;
    private volatile boolean isInterrupted;

    
    /**
     * Constructor
     *
     * @param id the unique id of this processing 
     * @param name the name of this processing unit runnable
     * @param processingUnitClass the processing unit class
     * @param parameterList the parameter list
     * @param processingUnitContext the processing context.
     * @param processingUnitRunnableListener the processing unit runnable listener
     * @throws ValidationException This will be throw in case the consistency check failures.
     * @throws ProcessingException Throws this exception in case of initialization failures.
     */
    public ProcessingUnitRunnable(String id, 
                                  String name, 
                                  Class<? extends IProcessingUnit> processingUnitClass, 
                                  List<Parameter> parameterList, 
                                  IProcessingUnitContext processingUnitContext,
                                  IProcessingUnitRunnableListener processingUnitRunnableListener) {
        super(id, name, processingUnitClass, parameterList, processingUnitContext);
        this.processingUnitThrottling = null;
        this.isInterrupted = false;
        
        setProcessingUnitRunnableListener(processingUnitRunnableListener);
        setProcessingActionStatus(ProcessingActionStatus.STARTING);
    }

    
    /**
     * Constructor
     *
     * @param suspendedState the suspended state
     * @param processingUnitRunnableListener the processing unit runnable listener
     * @throws ValidationException This will be throw in case the consistency check failures.
     * @throws ProcessingException Throws this exception in case of initialization failures.
     */
    public ProcessingUnitRunnable(byte[] suspendedState, IProcessingUnitRunnableListener processingUnitRunnableListener) {
        super(suspendedState, processingUnitRunnableListener);

        setMaxNumberOfProcessingUnitCallsPerSecond(getProcessingUnitProxy().getMaxNumberOfProcessingUnitCallsPerSecond());
    }

    
    /**
     * Defines the max calls per second to throttle the processing unit
     *
     * @param maxNumberOfProcessingUnitCallsPerSecond the max number of processing units per second
     */
    public void setMaxNumberOfProcessingUnitCallsPerSecond(Long maxNumberOfProcessingUnitCallsPerSecond) {
        super.setMaxNumberOfProcessingUnitCallsPerSecond(maxNumberOfProcessingUnitCallsPerSecond);
        
        if (maxNumberOfProcessingUnitCallsPerSecond == null || maxNumberOfProcessingUnitCallsPerSecond.longValue() <= 0) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(ProcessingUnitUtil.getInstance().toString(getId(), getName(), getProcessingUnitClass()) + " Disable throttling (max number of processing unit calls per second).");
            }
            processingUnitThrottling = null;
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug(ProcessingUnitUtil.getInstance().toString(getId(), getName(), getProcessingUnitClass()) + " Enable throttling (max number of processing unit calls per second): " + maxNumberOfProcessingUnitCallsPerSecond);
            }
            processingUnitThrottling = new ProcessingUnitThrottling(getId(), getName(), getProcessingUnitClass(), maxNumberOfProcessingUnitCallsPerSecond);
        }
        
        if (getProcessingUnitProxy() != null) {
            if ((maxNumberOfProcessingUnitCallsPerSecond == null && getProcessingUnitProxy().getMaxNumberOfProcessingUnitCallsPerSecond() == null)
                    || (maxNumberOfProcessingUnitCallsPerSecond != null && maxNumberOfProcessingUnitCallsPerSecond.equals(getProcessingUnitProxy().getMaxNumberOfProcessingUnitCallsPerSecond()))) {
                // they are equal, no change
            } else {
                // in case the set value are different set max number of calls per second
                boolean processingHasOwnThrottlingImplementation = getProcessingUnitProxy().setMaxNumberOfProcessingUnitCallsPerSecond(maxNumberOfProcessingUnitCallsPerSecond);
                if (processingHasOwnThrottlingImplementation) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug(ProcessingUnitUtil.getInstance().toString(getId(), getName(), getProcessingUnitClass())
                                  + " Processing unit has own throttling implementation, propagted value of max number of processing unit calls per second: "
                                  + maxNumberOfProcessingUnitCallsPerSecond);
                    }

                    processingUnitThrottling = null; 
                }
            }
        }
    }

    
    /**
     * Suspend processing
     */
    public void suspendProcessing() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Suspending processing unit " + getProcessingUnitProxy());
        }
        
        suspend = true;
    }


    /**
     * Get suspended state
     *
     * @return the suspended state
     */
    public byte[] getSuspendedState() {
        return suspendedState;
    }

    
    /**
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        final String processingInfo;
        
        try {
            if (ProcessingActionStatus.RESUMING.equals(getProcessingActionStatus())) {
                if (getProcessingUnitProxy() == null) {
                    throw new ProcessingException("Could not initialize processing!", true);
                }
                processingInfo = getProcessingUnitProxy().toString(); 
                LOG.info(processingInfo + " Resumed processing unit");                
            } else {
                IProcessingUnitProxy processingUnitProxy = createProcessingUnitProxy();
                if (processingUnitProxy == null) {
                    throw new ProcessingException("Could not initialize processing!", true);
                }
                processingInfo = processingUnitProxy.toString();
                LOG.info(processingInfo + " Started processing unit");
            }

            boolean continueProcessing = !isThreadInterrupted();
            boolean exceptionOccured = false;
            while (continueProcessing && !suspend) {
                if (!ProcessingActionStatus.RUNNING.equals(getProcessingActionStatus())) {
                    setProcessingActionStatus(ProcessingActionStatus.RUNNING);
                }
                
                try { 
                    continueProcessing = getProcessingUnitProxy().processUnit();
                    continueProcessing = continueProcessing && !isThreadInterrupted();
                } catch (RuntimeException e) {
                    continueProcessing = false;
                    exceptionOccured = true;
                }
            
                if (LOG.isDebugEnabled()) {
                    LOG.debug(toString());
                }

                if (continueProcessing && !suspend) {
                    continueProcessing = afterProcessUnit(continueProcessing);
                    throttlingProcessing();
                }
            } 

            if (suspend) {
                // we must suspend and end
                LOG.info("Suspended processing unit " + processingInfo);
                setProcessingActionStatus(ProcessingActionStatus.SUSPENDING);
                suspendedState = getProcessingUnitProxy().suspendProcessing();
                setProcessingActionStatus(ProcessingActionStatus.SUSPENDED);
            } else {                
                // in case continue processing is marked as false but there are still unit to process open, there we have to stop
                if (exceptionOccured || (!continueProcessing && (getProcessingUnitProgress().getNumberOfUnprocessedUnits() > 0))) {
                    setProcessingActionStatus(ProcessingActionStatus.ABORTING);
                    if (LOG.isDebugEnabled()) {
                        LOG.debug(processingInfo + " Aborting processing unit");
                    }
                    getProcessingUnitProxy().onAborting();
                    setProcessingActionStatus(ProcessingActionStatus.ABORTED);
                    
                    if (LOG.isDebugEnabled()) {
                        LOG.debug(toString());
                    }
                    LOG.info(processingInfo + " Aborted processing unit");
                } else {
                    setProcessingActionStatus(ProcessingActionStatus.ENDING);                
                    if (LOG.isDebugEnabled()) {
                        LOG.debug(processingInfo + " Ending processing unit");
                    }
                    getProcessingUnitProxy().onEnding();
                    setProcessingActionStatus(ProcessingActionStatus.ENDED);
                    
                    if (LOG.isDebugEnabled()) {
                        LOG.debug(toString());
                    }
                    LOG.info(processingInfo + " Ended processing unit");
                }
            }
        } finally {
            // release
            if (getProcessingUnitProxy() != null) {
                if (isInterrupted) {
                    LOG.info(getProcessingUnitProxy().toString() + " Processing unit thread #" + Thread.currentThread().getId() + " interrupted!");
                } else {
                    LOG.debug(getProcessingUnitProxy().toString() + " Processing unit thread #" + Thread.currentThread().getId() + " ended.");
                }
                
                if (LOG.isDebugEnabled()) {
                    LOG.debug(getProcessingUnitProxy().toString() + " Release resource of processing unit: " + getProcessingActionStatus());
                }
                
                try {
                    getProcessingUnitProxy().releaseResource();
                } catch (ProcessingException e) {
                    LOG.debug(getProcessingUnitProxy().toString() + " Could not release resource of processing unit: " + e.getMessage(), e);
                    LOG.warn(getProcessingUnitProxy().toString() + " Could not release resource of processing unit: " + e.getMessage());
                }
            }
        }
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ProcessingUnitUtil.getInstance().toString(getId(), 
                                                         getName(), 
                                                         getProcessingUnitProxy().getProcessingUnitClass().getName(),
                                                         getParameterList(),
                                                         getProcessingUnitContext(),
                                                         getProcessingUnitProgress(), 
                                                         getProcessingActionStatus(), 
                                                         getProcessingRuntimeStatus(), 
                                                         getStatusMessageList(), 
                                                         getTimeMeasurement(), 
                                                         getProcessingUnitThrottling());
    }

    
    /**
     * Create processing unit proxy
     * 
     * @return the processing unit proxy
     */
    protected ProcessingUnitProxy createProcessingUnitProxy() {
        ProcessingUnitProxy processingUnitProxy = super.createProcessingUnitProxy();
        
        // set the throttling
        setMaxNumberOfProcessingUnitCallsPerSecond(getMaxNumberOfProcessingUnitCallsPerSecond());
        return processingUnitProxy;
    }

    
    /**
     * Throttling the processing if its defined and needed
     */
    protected void throttlingProcessing() {
        if (processingUnitThrottling != null) {
            processingUnitThrottling.throttlingProcessing();
        }
    }

    
    /**
     * Get the processing unit throttling
     *
     * @return the processing unit throttling
     */
    protected IBandwidthThrottling getProcessingUnitThrottling() {
        if (getMaxNumberOfProcessingUnitCallsPerSecond() == null) {
            return null;
        }

        if (processingUnitThrottling != null) {
            return processingUnitThrottling.getBandwidth();
        }

        if (getProcessingUnitProxy() != null) {
            return ProcessingUnitUtil.getInstance().getBandwidthProcessingUnitThrottling(getProcessingUnitProxy().getProcessingUnit());
        }
        
        return null;
    }

    
    /**
     * Check if it is interrupted
     *
     * @return true if it is interrupted
     */
    protected boolean isThreadInterrupted() {
        return Thread.currentThread().isInterrupted() || isInterrupted;
    }
}
