/*
 * IProcessingUnitStatus.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit;

import com.github.toolarium.processing.unit.dto.ProcessingRuntimeStatus;
import com.github.toolarium.processing.unit.runtime.runnable.IEmptyProcessingUnitHandler;
import java.util.List;


/**
 * Defines the processing unit status: Either the {@link #getNumberOfSuccessfulUnits()} or the {@link #getNumberOfFailedUnits()}
 * returns a positive number of processed units. 
 * 
 * <p>In general the expectation is to have one record, either successful or failed. If both are null or zero then it will be
 * interpreted as {@link #hasNext()} equals false!   
 * This behavior can be defined by the {@link IEmptyProcessingUnitHandler}.
 * 
 * @author patrick
 */
public interface IProcessingUnitStatus {

    /**
     * Defines if there is more to process or not.
     *
     * @return true if it has more element; otherwise it has ended.
     */
    boolean hasNext();

    
    /**
     * Gets the number of successful units that were processed in the last run.
     * 
     * @return the number of successful units or null.
     */
    Long getNumberOfSuccessfulUnits();


    /**
     * Gets the number of failed units processed that were failed in the last run.
     *
     * @return the number of failed units or null.
     */
    Long getNumberOfFailedUnits();
    
    
    /**
     * Optionally, the number of all unprocessed units can be returned.
     *
     * @return the number of unprocessed units.
     */
    Long getNumberOfUnprocessedUnits();


    /**
     * The processing runtime status for this run. If it's null then it will be interpreted as 
     * successful in the last run.
     *
     * @return the processing runtime status or null
     */
    ProcessingRuntimeStatus getProcessingRuntimeStatus();


    /**
     * A return message can be passed back. In case it's null then no status message is available 
     * in the last run.
     *
     * @return the status message list or null
     */
    List<String> getStatusMessageList();
    
    
    /**
     * Get the processing unit statistic of the last call.
     *
     * @return the processing unit statistic of last call or null.
     */
    IProcessingUnitStatistic getProcessingUnitStatistic();    
}
