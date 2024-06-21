/*
 * ValidateSeverityType.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.dto;


/**
 * Defines the processing action status.
 *
 * @author patrick
 */
public enum ProcessingActionStatus {

    /** STARTING -> RUNNING | ABORTING */
    STARTING,

    /** RUNNING  -> ENDING | ABORTING | SUSPENDING */
    RUNNING,

    /** ENDING -> ENDED */
    ENDING,

    /** ENDED */
    ENDED,

    /** SUSPENDING -> SUSPENDED | ABORTING */
    SUSPENDING,

    /** SUSPENDED -> RESUMING | ABORTING */
    SUSPENDED,

    /** RESUMING -> RUNNING | ABORTING */
    RESUMING,

    /** ABORTING -> ABORTED */
    ABORTING,

    /** ABORTED */
    ABORTED
}
