/*
 * ValidationException.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.exception;


import java.io.Serializable;


/**
 * Defines a validation exception.
 *
 * @author patrick
 */
public class ValidationException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = -8714419321468495854L;
    private boolean abortProcessing;


    /**
     * Default constructor for <code>ValidationException</code>.
     */
    protected ValidationException() {
        super();
        abortProcessing = true;
    }


    /**
     * Constructor for <code>ValidationException</code>.
     *
     * @param message the message
     */
    public ValidationException(String message) {
        super(message);
    }


    /**
     * Constructor for <code>ValidationException</code>.
     *
     * @param message the message
     * @param throwable the throwable
     */
    public ValidationException(String message, Throwable throwable) {
        super(message, throwable);
    }

    
    /**
     * Set abort the processing
     *
     * @param abortProcessing true to abort the processing; false to continue the processing
     */
    public void setAbortProcessing(boolean abortProcessing) {
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
}
