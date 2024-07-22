/*
 * ParallelProcessingParameter.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.runtime.runnable.parallelization;


import com.github.toolarium.processing.unit.ParameterDefinitionBuilder;
import com.github.toolarium.processing.unit.dto.ParameterDefinition;


/**
 * Defines the parallel processing unit parameters 
 * 
 * @author patrick
 */
public interface ParallelProcessingUnitParameters {
    /** NUMBER_OF_THREAD_PARAMETER */
    ParameterDefinition NUMBER_OF_THREAD_PARAMETER = 
            new ParameterDefinitionBuilder().name("numberOfThreads").defaultValue(1).description("Defines the number of threads to process this processing unit (parallizable).").build();

    /** LOCK_SIZE */
    ParameterDefinition LOCK_SIZE = new ParameterDefinitionBuilder().name("lockSize").defaultValue(10).description("Defines the object lock size.").build();
    
    /** UNLOCK_TIMEOUT */
    ParameterDefinition UNLOCK_TIMEOUT = 
            new ParameterDefinitionBuilder().name("unlockTimeout").emptyValueIsAllowed().defaultValue(500L).description("Defines the timeout in milliseconds after an unlock an object can be locked again.").build();

    /** STARTUP_PHASED_SLEEP_TIME */
    ParameterDefinition STARTUP_PHASED_SLEEP_TIME = 
            new ParameterDefinitionBuilder().name("startupPhasedSleepTime").defaultValue(100L).description("Defines the startup phased sleep time between threads (parallizable).").build();

    /** AGGREGATE_STATUS_PAUSE_TIME */
    ParameterDefinition AGGREGATE_STATUS_PAUSE_TIME = 
            new ParameterDefinitionBuilder().name("aggregateStatusPauseTime").defaultValue(100L).emptyValueIsNotAllowed().description("Defines the aggregate status pause time.").build();

    /** NO_PROGRESS_PAUSE_TIME */
    ParameterDefinition NO_PROGRESS_PAUSE_TIME = 
            new ParameterDefinitionBuilder().name("noProgressPauseTime").defaultValue(500L).emptyValueIsNotAllowed().description("Defines pause time in case a parallel processing unit could nothing process.").build();

    /** MAX_NUMBER_OF_NO_PROGRESS_BEFORE_ABORT */
    ParameterDefinition MAX_NUMBER_OF_NO_PROGRESS_BEFORE_ABORT = 
            new ParameterDefinitionBuilder().name("maxNumberOfNoProgressBeforeAbort").emptyValueIsAllowed().description("Defines pause time in case a parallel processing unit could nothing process.").build();
}
