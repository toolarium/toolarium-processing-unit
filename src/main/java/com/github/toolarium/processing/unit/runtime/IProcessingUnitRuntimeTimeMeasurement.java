/*
 * IProcessingUnitTimeMeasurement.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.runtime;

import java.time.Instant;


/**
 * Defines the processing unit runtime time measurement.
 * 
 * @author patrick
 */
public interface IProcessingUnitRuntimeTimeMeasurement {

    /**
     * Get the start time stamp
     *
     * @return the start time stamp
     */
    Instant getStartTimestamp();

    
    /**
     * Get the stop time stamp
     *
     * @return the stop time stamp
     */
    Instant getStopTimestamp();

    
    /**
     * Get the duration in milliseconds
     *
     * @return the duration
     */
    long getDuration();


    /**
     * Get the formated duration
     *
     * @return the formated duration
     */
    String getDurationAsString();
}
