/*
 * ProcessingUnitThrottling.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.runtime.runnable.impl;

import com.github.toolarium.common.bandwidth.BandwidthThrottling;
import com.github.toolarium.common.bandwidth.IBandwidthThrottling;
import com.github.toolarium.common.formatter.TimeDifferenceFormatter;
import com.github.toolarium.processing.unit.IProcessingUnit;
import com.github.toolarium.processing.unit.runtime.runnable.IProcessingUnitThrottling;
import com.github.toolarium.processing.unit.util.ProcessingUnitUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Implements the {@link IProcessingUnitThrottling}.
 *  
 * @author patrick
 */
public class ProcessingUnitThrottling implements IProcessingUnitThrottling {
    private static final Logger LOG = LoggerFactory.getLogger(ProcessingUnitThrottling.class);
    private final String processInfo;
    private volatile IBandwidthThrottling bandwidthThrottling;
    private volatile boolean processingUnitThrottlingInitLogged;
    private final TimeDifferenceFormatter timeDifferenceFormatter; 

    
    /**
     * Constructor for ProcessingUnitThrottling
     *
     * @param id the id
     * @param name the name
     * @param processingUnitClass the processing class
     * @param maxNumberOfProcessingUnitCallsPerSecond the max number of processing units per second
     */
    public ProcessingUnitThrottling(String id, String name, Class<? extends IProcessingUnit> processingUnitClass, Long maxNumberOfProcessingUnitCallsPerSecond) {
        processInfo = ProcessingUnitUtil.getInstance().toString(id, name, processingUnitClass);
        processingUnitThrottlingInitLogged = false;
        bandwidthThrottling = new BandwidthThrottling(maxNumberOfProcessingUnitCallsPerSecond, 10 /* update interval*/);
        timeDifferenceFormatter = new TimeDifferenceFormatter();
    }


    /**
     * @see com.github.toolarium.processing.unit.runtime.runnable.IProcessingUnitThrottling#getBandwidth()
     */
    @Override
    public synchronized IBandwidthThrottling getBandwidth() {
        return bandwidthThrottling;
    }


    /**
     * @see com.github.toolarium.processing.unit.runtime.runnable.IProcessingUnitThrottling#throttlingProcessing()
     */
    @Override
    public synchronized void throttlingProcessing() {
        try {
            if (bandwidthThrottling == null) {
                if (!processingUnitThrottlingInitLogged) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug(processInfo + " Has no throttling delay.");
                    }
                }
                return;
            }
    
            if (!processingUnitThrottlingInitLogged) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug(processInfo + " Has throttling update interval: " + bandwidthThrottling.getUpdateInterval() + ".");
                }
            }
    
            long start = System.currentTimeMillis();
            bandwidthThrottling.bandwidthCheck();
    
            long time = System.currentTimeMillis() - start;
            if (time > bandwidthThrottling.getUpdateInterval()) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug(processInfo + " Waited for " + timeDifferenceFormatter.formatAsString(time));
                }
            }
        } finally {
            if (!processingUnitThrottlingInitLogged) {
                processingUnitThrottlingInitLogged = true;
            }
        }
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ProcessingUnitThrottling [processInfo=" + processInfo + ", bandwidthThrottling=" + bandwidthThrottling + "]";
    }
}
