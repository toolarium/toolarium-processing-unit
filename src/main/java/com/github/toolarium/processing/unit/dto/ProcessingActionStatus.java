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

    /** STARTING -&gt; RUNNING | ABORTING */
    STARTING,

    /** RUNNING  -&gt; ENDING | ABORTING | SUSPENDING */
    RUNNING,

    /** ENDING -&gt; ENDED */
    ENDING,

    /** ENDED */
    ENDED,

    /** SUSPENDING -&gt; SUSPENDED | ABORTING */
    SUSPENDING,

    /** SUSPENDED -&gt; RESUMING | ABORTING */
    SUSPENDED,

    /** RESUMING -&gt; RUNNING | ABORTING */
    RESUMING,

    /** ABORTING -&gt; ABORTED */
    ABORTING,

    /** ABORTED */
    ABORTED
}
