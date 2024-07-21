/*
 * TestProcessingUnitRunnerFactory.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.runtime.test;

/**
 * Factory to get a {@link TestProcessingUnitRunner}.
 *
 * @author patrick
 */
public final class TestProcessingUnitRunnerFactory {
    
    /**
     * Private class, the only instance of the singelton which will be created by accessing the holder class.
     *
     * @author patrick
     */
    private static class HOLDER {
        static final TestProcessingUnitRunnerFactory INSTANCE = new TestProcessingUnitRunnerFactory();
    }

    
    /**
     * Constructor
     */
    private TestProcessingUnitRunnerFactory() {
        // NOP
    }

    
    /**
     * Get the instance
     *
     * @return the instance
     */
    public static TestProcessingUnitRunnerFactory getInstance() {
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
