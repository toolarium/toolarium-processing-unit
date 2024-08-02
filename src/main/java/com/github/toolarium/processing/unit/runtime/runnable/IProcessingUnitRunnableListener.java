/*
 * IProcessingUnitRunnableListener.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.runtime.runnable;

import com.github.toolarium.common.bandwidth.IBandwidthThrottling;
import com.github.toolarium.processing.unit.IProcessingUnitContext;
import com.github.toolarium.processing.unit.IProcessingUnitProgress;
import com.github.toolarium.processing.unit.dto.Parameter;
import com.github.toolarium.processing.unit.dto.ProcessingActionStatus;
import com.github.toolarium.processing.unit.dto.ProcessingRuntimeStatus;
import com.github.toolarium.processing.unit.runtime.IProcessingUnitRuntimeTimeMeasurement;
import java.util.List;


/**
 * Defines the processing unit runnable listener
 * 
 * @author patrick
 */
public interface IProcessingUnitRunnableListener {
    

    /**
     * Notify processing unit progress. It is called if at least 1 percent progress has been made. 
     * The processingProgress.getProgress() contains the progress, the last progress in percentage 
     * is past as well. 
     *
     * @param id the id
     * @param name the name
     * @param processingUnitClass the processing unit class
     * @param parameters the parameters
     * @param processingUnitContext the processing unit context
     * @param processingProgress the progressing unit progress
     * @param processingActionStatus the action status
     * @param processingRuntimeStatus the runtime status
     * @param messages the messages
     * @param timeMeasurement the time measurement
     * @param processingUnitThrottling the processing unit throttling
     * @param lastProgressInPercentage the last progress in percentage
     */
    void notifyProcessingUnitProgress(String id, // CHECKSTYLE IGNORE THIS LINE
                                      String name, 
                                      String processingUnitClass,
                                      List<Parameter> parameters,
                                      IProcessingUnitContext processingUnitContext,
                                      IProcessingUnitProgress processingProgress, 
                                      ProcessingActionStatus processingActionStatus,
                                      ProcessingRuntimeStatus processingRuntimeStatus,
                                      List<String> messages,
                                      IProcessingUnitRuntimeTimeMeasurement timeMeasurement, 
                                      IBandwidthThrottling processingUnitThrottling,
                                      int lastProgressInPercentage);
    
    
    /**
     * Notification processing unit an action status has changed.
     *
     * @param id the unique id of this processing 
     * @param name the name of this processing unit runnable
     * @param processingUnitClass the processing unit class
     * @param previousProcessingActionStatus the previous processing action status or null in case of processingActionStatus is STARTING or RESUMED 
     * @param processingActionStatus the processing action status
     * @param processingUnitProgress the processing unit progress 
     * @param runtimeTimeMeasurment the runtime time measurement
     * @param processingUnitContext the processing unit context
     */
    void notifyProcessingUnitState(String id, 
                                   String name, 
                                   String processingUnitClass,
                                   ProcessingActionStatus previousProcessingActionStatus,
                                   ProcessingActionStatus processingActionStatus,
                                   IProcessingUnitProgress processingUnitProgress,
                                   IProcessingUnitRuntimeTimeMeasurement runtimeTimeMeasurment,
                                   IProcessingUnitContext processingUnitContext);
}
