/*
 * IProcessingUnitThrottlingSupport.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.base;

import com.github.toolarium.common.bandwidth.IBandwidthThrottling;
import com.github.toolarium.processing.unit.IProcessingUnit;


/**
 * Defines the processing unit throttling support. This interface should only implemented in case
 * a {@link IProcessingUnit} has specialized requirements regarding the throttling, 
 * e.g. ParallelProcesingUnit otherwise the framework already covers this properly.
 * 
 * @author patrick
 */
public interface IProcessingUnitThrottlingSupport {
    
    /**
     * Set the max number of processing unit calls per seconds
     *
     * @param id the unique id of the processing
     * @param name the name of the processing
     * @param maxNumberOfProcessingUnitCallsPerSecond The max number of processing unit calls per seconds
     */
    void setMaxNumberOfProcessingUnitCallsPerSecond(String id, String name, Long maxNumberOfProcessingUnitCallsPerSecond);

    
    /**
     * Get the bandwidth processing unit throttling
     *
     * @return the bandwidth processing unit throttling
     */
    IBandwidthThrottling getBandwidthProcessingUnitThrottling();
}
