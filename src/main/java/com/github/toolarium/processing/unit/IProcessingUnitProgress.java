/*
 * IProcessProgress.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit;

import com.github.toolarium.processing.unit.dto.ProcessingRuntimeStatus;


/**
 * Defines the processing unit progress.
 *
 * @author patrick
 */
public interface IProcessingUnitProgress {

    /**
     * Get the number of units to process in total.
     *
     * @return the number of units to process in total.
     */
    long getNumberOfUnitsToProcess();

    
    /**
     * Get the number of unprocessed units.
     *
     * @return the number of unprocessed units.
     */
    long getNumberOfUnprocessedUnits();


    /**
     * Gets the number of processed units (failed units included).
     *
     * @return the number of processed units (failed units included).
     */
    long getNumberOfProcessedUnits();

    
    /**
     * Gets the number of successful units.
     *
     * @return the number of successful units.
     */
    long getNumberOfSuccessfulUnits();

    
    /**
     * Gets the number of failed units.
     *
     * @return the number of failed units.
     */
    long getNumberOfFailedUnits();


    /**
     * The overall processing runtime status.
     *
     * @return the processing status type.
     */
    ProcessingRuntimeStatus getProcessingRuntimeStatus();


    /**
     * Get the processing unit statistic
     * 
     * @return the processing unit statistic
     */
    IProcessingUnitStatistic getProcesingUnitStatistic();
}
