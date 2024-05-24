/*
 * ProcessingUnitRunnerFactory.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.runtime.test;


/**
 * Factory to get a {@link TestProcessingUnitRunner}.
 *
 * @author patrick
 */
public final class ProcessingUnitRunnerFactory {
    
    /**
     * Private class, the only instance of the singelton which will be created by accessing the holder class.
     *
     * @author patrick
     */
    private static class HOLDER {
        static final ProcessingUnitRunnerFactory INSTANCE = new ProcessingUnitRunnerFactory();
    }

    
    /**
     * Constructor
     */
    private ProcessingUnitRunnerFactory() {
        // NOP
    }

    
    /**
     * Get the instance
     *
     * @return the instance
     */
    public static ProcessingUnitRunnerFactory getInstance() {
        return HOLDER.INSTANCE;
    }


    /**
     * The processing unit runner.
     *
     * @return the processing unit runner
     */
    public TestProcessingUnitRunner getProcessingUnitRunner() {
        return new TestProcessingUnitRunner();
    }
}
