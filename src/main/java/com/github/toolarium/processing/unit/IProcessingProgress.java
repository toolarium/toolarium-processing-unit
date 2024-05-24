/*
 * IProcessProgress.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit;

import com.github.toolarium.processing.unit.dto.ProcessingStatusType;

/**
 * Defines the processing progress.
 *
 * @author patrick
 */
public interface IProcessingProgress {

    /**
     * Gets the number of total processed unit.
     *
     * @return the total units.
     */
    long getTotalUnits();


    /**
     * Gets the number of processed units.
     *
     * @return the number of processed units.
     */
    long getProcessedUnits();


    /**
     * Gets the number of failed units.
     *
     * @return the number of failed units.
     */
    long getTotalFailedUnits();


    /**
     * The processing status type which covers the overall status
     *
     * @return the processing status type.
     */
    ProcessingStatusType getProcessingStatusType();


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
