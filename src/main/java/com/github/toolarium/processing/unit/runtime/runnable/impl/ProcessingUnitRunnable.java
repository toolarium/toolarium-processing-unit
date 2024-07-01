/*
 * ProcessingUnitRunnable.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.runtime.runnable.impl;

import com.github.toolarium.common.bandwidth.BandwidthThrottling;
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
    private IBandwidthThrottling processingUnitThrottling;
    private volatile boolean processingUnitThrottlingInitLogged;

    
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
        processingUnitThrottling = null;
        processingUnitThrottlingInitLogged = false;
        
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
        super(suspendedState);
        processingUnitThrottlingInitLogged = false;
        
        // resume
        setProcessingUnitRunnableListener(processingUnitRunnableListener);
        setProcessingActionStatus(ProcessingActionStatus.RESUMING);
        ProcessingUnitProxy processingUnitProxy = ProcessingUnitProxy.resume(suspendedState);
        setProcessingUnitProxy(processingUnitProxy);        
        setProcessingUnitThrottling(processingUnitProxy.getMaxNumberOfProcessingUnitCallsPerSecond());
    }

    /**
     * Create processing unit proxy
     * 
     * @return the processing unit proxy
     */
    protected ProcessingUnitProxy createProcessingUnitProxy() {
        ProcessingUnitProxy processingUnitProxy = super.createProcessingUnitProxy();
        if (processingUnitThrottling != null) {
            processingUnitProxy.setMaxNumberOfProcessingUnitCallsPerSecond(processingUnitThrottling.getBandwidth());
        }
        
        return processingUnitProxy;
    }

    
    /**
     * Defines the max calls per second to throttle the processing unit
     *
     * @param maxNumberOfProcessingUnitCallsPerSecond the max number of processing units per second
     */
    public void setProcessingUnitThrottling(Long maxNumberOfProcessingUnitCallsPerSecond) {
        if (getProcessingUnitProxy() != null) {
            getProcessingUnitProxy().setMaxNumberOfProcessingUnitCallsPerSecond(maxNumberOfProcessingUnitCallsPerSecond);
        }
        
        if (maxNumberOfProcessingUnitCallsPerSecond == null || maxNumberOfProcessingUnitCallsPerSecond.longValue() == BandwidthThrottling.NO_BANDWIDTH) {
            processingUnitThrottling = null;
        } else {
            processingUnitThrottling = new BandwidthThrottling(maxNumberOfProcessingUnitCallsPerSecond, 10 /* update interval*/);
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
        // && !Thread.currentThread().isInterrupted()
        final String processingInfo;
        
        try {
            if (ProcessingActionStatus.RESUMING.equals(getProcessingActionStatus())) {
                if (getProcessingUnitProxy() == null) {
                    throw new ProcessingException("Could not initialize processing!", true);
                }
                processingInfo = getProcessingUnitProxy().toString(); 
                LOG.info("Resumed processing unit " + processingInfo);                
            } else {
                IProcessingUnitProxy processingUnitProxy = createProcessingUnitProxy();
                if (processingUnitProxy == null) {
                    throw new ProcessingException("Could not initialize processing!", true);
                }
                processingInfo = processingUnitProxy.toString();
                LOG.info("Started processing unit " + processingInfo);
            }

            boolean continueProcessing = true;
            boolean exceptionOccured = false;
            while (continueProcessing && !suspend) {
                if (!ProcessingActionStatus.RUNNING.equals(getProcessingActionStatus())) {
                    setProcessingActionStatus(ProcessingActionStatus.RUNNING);
                }
                
                try { 
                    continueProcessing = getProcessingUnitProxy().processUnit();
                } catch (RuntimeException e) {
                    continueProcessing = false;
                    exceptionOccured = true;
                }
            
                if (LOG.isDebugEnabled()) {
                    /*
                    long processedUnits = 1;
                    if (getProcessingProgress() != null) {
                        processedUnits = getProcessingProgress().getNumberOfProcessedUnits() + 1;
                    }
                    LOG.debug(ProcessingUnitUtil.getInstance().preapre(getId(), getName(), getProcessingUnitProxy().getProcessingUnitClass()) + " call #" + processedUnits + ": " + getProcessingActionStatus());
                    */
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
                        LOG.debug("Aborting processing unit " + processingInfo);
                    }
                    getProcessingUnitProxy().getProcessingUnit().onStop();
                    setProcessingActionStatus(ProcessingActionStatus.ABORTED);
                    
                    if (LOG.isDebugEnabled()) {
                        LOG.debug(toString());
                    }
                    LOG.info("Aborted processing unit " + processingInfo);
                } else {
                    setProcessingActionStatus(ProcessingActionStatus.ENDING);                
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Ending processing unit " + processingInfo);
                    }
                    getProcessingUnitProxy().getProcessingUnit().onSuccess();
                    setProcessingActionStatus(ProcessingActionStatus.ENDED);
                    
                    if (LOG.isDebugEnabled()) {
                        LOG.debug(toString());
                    }
                    LOG.info("Ended processing unit " + processingInfo);
                }
            }
        } finally {
            // release
            if (getProcessingUnitProxy() != null) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Release resource of processing unit " + getProcessingUnitProxy().toString() + ": " + getProcessingActionStatus());
                }
                
                try {
                    getProcessingUnitProxy().releaseResource();
                } catch (ProcessingException e) {
                    LOG.debug("Could not release resource of processing unit " + getProcessingUnitProxy().toString() + ": " + e.getMessage(), e);
                    LOG.warn("Could not release resource of processing unit " + getProcessingUnitProxy().toString() + ": " + e.getMessage());
                }
            }
        }
    }

    
    /**
     * Throttling the processing if its defined and needed
     */
    protected void throttlingProcessing() {
        try {
            if (processingUnitThrottling == null) {
                if (!processingUnitThrottlingInitLogged) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug(ProcessingUnitUtil.getInstance().toString(getId(), getName(), getProcessingUnitProxy().getProcessingUnitClass()) + " has no throttling delay.");
                    }
                }
                return;
            }
    
            if (!processingUnitThrottlingInitLogged) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug(ProcessingUnitUtil.getInstance().toString(getId(), getName(), getProcessingUnitProxy().getProcessingUnitClass()) + " has throttling update interval: " + processingUnitThrottling.getUpdateInterval() + ".");
                }
            }
    
            long start = System.currentTimeMillis();
            processingUnitThrottling.bandwidthCheck();
    
            long time = System.currentTimeMillis() - start;
            if (time > processingUnitThrottling.getUpdateInterval()) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug(ProcessingUnitUtil.getInstance().toString(getId(), getName(), getProcessingUnitProxy().getProcessingUnitClass()) + " waited for " + getTimeDifferenceFormatter().formatAsString(time));
                }
            }
        } finally {
            if (!processingUnitThrottlingInitLogged) {
                processingUnitThrottlingInitLogged = true;
            }
        }
    }

    
    /**
     * Get the processing unit throttling
     *
     * @return the processing unit throttling
     */
    protected IBandwidthThrottling getProcessingUnitThrottling() {
        return processingUnitThrottling;
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
}
