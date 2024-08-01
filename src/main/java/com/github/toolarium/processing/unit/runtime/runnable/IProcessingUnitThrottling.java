/*
 * IProcessingUnitThrottling.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.runtime.runnable;

import com.github.toolarium.common.bandwidth.IBandwidthThrottling;


/**
 * Defines the processing unit throttling
 * 
 * @author patrick
 */
public interface IProcessingUnitThrottling {
    
    /**
     * Get the bandwidth
     *
     * @return the bandwidth
     */
    IBandwidthThrottling getBandwidth();

        
    /**
     * Throttling the processing if its defined and needed
     */
    void throttlingProcessing();
}
