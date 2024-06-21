/*
 * IProcessProgress.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit;

import com.github.toolarium.processing.unit.dto.ProcessingRuntimeStatus;


/**
 * Defines the processing progress.
 *
 * @author patrick
 */
public interface IProcessingProgress {

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
     * The processing runtime status which covers the overall status
     *
     * @return the processing status type.
     */
    ProcessingRuntimeStatus getProcessingRuntimeStatus();


    /**
     * Returns the processing status message
     *
     * @return the processing status message
     */
    String getProcessingStatusMessage();


    /**
     * The processing statistic.
     *
     * @return the processing statistic.
     */
    IProcessingStatistic getProcesingStatistic();
}
