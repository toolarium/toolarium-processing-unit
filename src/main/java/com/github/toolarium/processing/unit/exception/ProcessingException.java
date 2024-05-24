/*
 * ProcessingException.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.exception;

import java.io.Serializable;


/**
 * Defines the processing exception.
 *
 * @author patrick
 */
public class ProcessingException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 3088800428812242742L;
    private boolean abortProcessing;


    /**
     * <p>Default constructor for <code>ProcessingException</code>.</p>
     * 
     * @param abortProcessing true to abort the processing; false to continue the processing
     */
    protected ProcessingException(boolean abortProcessing) {
        super();
        this.abortProcessing = abortProcessing;
    }


    /**
     * <p>Constructor for <code>ProcessingException</code>.</p>
     *
     * @param message the message
     * @param abortProcessing true to abort the processing; false to continue the processing
     */
    public ProcessingException(String message, boolean abortProcessing) {
        super(message);
        this.abortProcessing = abortProcessing;
    }


    /**
     * <p>Constructor for <code>ProcessingException</code>.</p>
     *
     * @param message the message
     * @param throwable the throwable
     * @param abortProcessing true to abort the processing; false to continue the processing
     */
    public ProcessingException(String message, Throwable throwable, boolean abortProcessing) {
        super(message, throwable);
        this.abortProcessing = abortProcessing;
    }

    
    /**
     * Abort the processing
     *
     * @return true to abort the processing; false to continue the processing
     */
    public boolean abortProcessing() {
        return abortProcessing;
    }


    /**
     * Set abort the processing
     *
     * @param abortProcessing true to abort the processing; false to continue the processing
     */
    public void setAbortProcessing(boolean abortProcessing) {
        this.abortProcessing = abortProcessing;
    }
}
