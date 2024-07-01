/*
 * TestProcessingUnit.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.runtime.test;

import com.github.toolarium.common.util.RandomGenerator;
import com.github.toolarium.common.util.RoundUtil;
import com.github.toolarium.common.util.ThreadUtil;
import com.github.toolarium.processing.unit.IProcessingUnitContext;
import com.github.toolarium.processing.unit.IProcessingUnitPersistence;
import com.github.toolarium.processing.unit.IProcessingUnitProgress;
import com.github.toolarium.processing.unit.IProcessingUnitStatus;
import com.github.toolarium.processing.unit.ParameterDefinitionBuilder;
import com.github.toolarium.processing.unit.ProcessingUnitStatusBuilder;
import com.github.toolarium.processing.unit.base.AbstractProcessingUnitPersistenceImpl;
import com.github.toolarium.processing.unit.dto.Parameter;
import com.github.toolarium.processing.unit.dto.ParameterDefinition;
import com.github.toolarium.processing.unit.dto.ParameterValueType;
import com.github.toolarium.processing.unit.exception.ProcessingException;
import com.github.toolarium.processing.unit.exception.ValidationException;
import java.util.List;
import java.util.UUID;


/**
 * Test processing unit.
 * 
 * @author patrick
 */
public class TestProcessingUnit extends AbstractProcessingUnitPersistenceImpl<TestProcessingUnit.TestPersistence> {
    public static final  ParameterDefinition NUMBER_OF_UNITS_TO_PROCESS_PARAMETER = 
            new ParameterDefinitionBuilder().name("numberOfUnitsToProcess").defaultValue(100).isMandatory().description("Defines the number of units to process.").build();
    public static final  ParameterDefinition PERCENTAGE_NUMBER_OF_UNITS_TO_FAIL_PARAMTER =
            new ParameterDefinitionBuilder().name("percentageNumberOfUnitsToFail").defaultValue(2).description("Defines the percentage of units to fail.").build();
    public static final  ParameterDefinition SLEEP_TIME_BY_A_PROCESSING_PARAMTER = 
            new ParameterDefinitionBuilder().name("sleepTime").type(ParameterValueType.INTEGER).defaultValue(100).description("Defines the sleep time which simulates the processing.").build();
    public static final  ParameterDefinition END_AS_WARNING_PARAMTER = 
            new ParameterDefinitionBuilder().name("endAsWarning").defaultValue(false).description("End the processing as warning.").build();
    public static final  ParameterDefinition END_AS_ERROR_PARAMTER = 
            new ParameterDefinitionBuilder().name("endAsError").defaultValue(false).description("End the processing as error.").build();
    public static final  ParameterDefinition THROW_RUNTIME_EXCEPTION_PARAMTER = 
            new ParameterDefinitionBuilder().name("throwRuntimeException").defaultValue(false).description("Throws a runtime exception during processing randomly.").build();
    public static final  ParameterDefinition THROW_PROCESSING_EXCEPTION_PARAMTER = 
            new ParameterDefinitionBuilder().name("throwProcessingException").defaultValue(false).description("Throws a processing exception during processing randomly.").build();
    public static final  ParameterDefinition THROW_PROCESSING_EXCEPTION_AND_ABORT_PARAMTER = 
            new ParameterDefinitionBuilder().name("throwProcessingExceptionAndAbort").defaultValue(false).description("Throws a processing exception during processing and abort.").build();
    public static final  ParameterDefinition THROW_RUNTIME_EXCEPTION_IN_VALIDATION_PARAMTER = 
            new ParameterDefinitionBuilder().name("throwRuntimeExceptionInValidation").defaultValue(false).description("Throws a runtime exception during validation.").build();
    public static final  ParameterDefinition THROW_VALIDATION_EXCEPTION_IN_VALIDATION_PARAMTER = 
            new ParameterDefinitionBuilder().name("throwValidationExceptionInValidation").defaultValue(false).description("Throws a validation exception during validation.").build();
    public static final  ParameterDefinition STATISIIC_KEYS_PARAMTER = 
            new ParameterDefinitionBuilder().name("statisticKeys").defaultValue(new String[] {"VALUES", "GUESSES"}).isOptional().description("Defines the statistic keys.").build();
    public static final  ParameterDefinition STATISIIC_MAX_NUMBER_PARAMTER = 
            new ParameterDefinitionBuilder().name("statisticMaxNumber").defaultValue(5).description("Defines the statistic max number.").build();

    
    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitPersistenceImpl#newPersistenceInstance()
     */
    @Override
    protected TestPersistence newPersistenceInstance() {
        return new TestPersistence();
    }
    
    
    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#initializeParameterDefinition()
     */
    public void initializeParameterDefinition() {
        getParameterRuntime().addParameterDefinition(NUMBER_OF_UNITS_TO_PROCESS_PARAMETER); 
        getParameterRuntime().addParameterDefinition(PERCENTAGE_NUMBER_OF_UNITS_TO_FAIL_PARAMTER); 
        getParameterRuntime().addParameterDefinition(SLEEP_TIME_BY_A_PROCESSING_PARAMTER); 
        getParameterRuntime().addParameterDefinition(END_AS_WARNING_PARAMTER); 
        getParameterRuntime().addParameterDefinition(END_AS_ERROR_PARAMTER); 
        getParameterRuntime().addParameterDefinition(THROW_RUNTIME_EXCEPTION_PARAMTER); 
        getParameterRuntime().addParameterDefinition(THROW_PROCESSING_EXCEPTION_PARAMTER); 
        getParameterRuntime().addParameterDefinition(THROW_PROCESSING_EXCEPTION_AND_ABORT_PARAMTER); 
        getParameterRuntime().addParameterDefinition(THROW_RUNTIME_EXCEPTION_IN_VALIDATION_PARAMTER); 
        getParameterRuntime().addParameterDefinition(THROW_VALIDATION_EXCEPTION_IN_VALIDATION_PARAMTER); 
        getParameterRuntime().addParameterDefinition(STATISIIC_KEYS_PARAMTER); 
        getParameterRuntime().addParameterDefinition(STATISIIC_MAX_NUMBER_PARAMTER); 
    }
    
    
    /**
     * @see com.github.toolarium.processing.unit.IProcessingUnit#initialize(java.util.List, com.github.toolarium.processing.unit.IProcessingUnitContext)
     */
    @Override
    public void initialize(List<Parameter> parameterList, IProcessingUnitContext processingUnitContext) throws ValidationException, ProcessingException {
        super.initialize(parameterList, processingUnitContext);
        
        if (getParameterRuntime().getParameterValueList(THROW_RUNTIME_EXCEPTION_IN_VALIDATION_PARAMTER).getValueAsBoolean()) {
            throw new RuntimeException("Test runtime exception in validation.");
        }
        if (getParameterRuntime().getParameterValueList(THROW_VALIDATION_EXCEPTION_IN_VALIDATION_PARAMTER).getValueAsBoolean()) {
            throw new ValidationException("Test validation exception in validation.");
        }
        
    }


    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#estimateNumberOfUnitsToProcess(com.github.toolarium.processing.unit.IProcessingUnitContext)
     */
    @Override
    public long estimateNumberOfUnitsToProcess(IProcessingUnitContext processingUnitContext) {
        return getParameterRuntime().getParameterValueList(NUMBER_OF_UNITS_TO_PROCESS_PARAMETER).getValueAsLong();
    }
    

    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#processUnit(com.github.toolarium.processing.unit.IProcessingUnitProgress, com.github.toolarium.processing.unit.IProcessingUnitContext)
     */
    @Override
    public IProcessingUnitStatus processUnit(IProcessingUnitProgress processingProgress, IProcessingUnitContext processingUnitContext) throws ProcessingException {

        // simulate some doing
        ThreadUtil.getInstance().sleep(getParameterRuntime().getParameterValueList(SLEEP_TIME_BY_A_PROCESSING_PARAMTER).getValueAsLong());
        
        // store in persistence
        if (getProcessingPersistence().getRecordId() == null) {
            getProcessingPersistence().setRecordId(UUID.randomUUID().toString());
            processingUnitContext.set("recordId", getProcessingPersistence().getRecordId());
        }
        getProcessingPersistence().setNumber(RandomGenerator.getInstance().getLongRandom());
        
        // In case of successful processing
        ProcessingUnitStatusBuilder processingUnitStatusBuilder = new ProcessingUnitStatusBuilder(); 
        processingUnitStatusBuilder.processedSuccessful();
        
        // randomly fail units
        final int percentageToFail = getParameterRuntime().getParameterValueList(PERCENTAGE_NUMBER_OF_UNITS_TO_FAIL_PARAMTER).getValueAsInteger();
        if (processingProgress.getNumberOfProcessedUnits() > 0) {
            final long num = RoundUtil.getInstance().roundToInt(processingProgress.getNumberOfProcessedUnits() / 100.0 * percentageToFail);
            if (num < processingProgress.getNumberOfFailedUnits() && RandomGenerator.getInstance().getBooleanRandom()) {
                processingUnitStatusBuilder.processingUnitFailed();
            }
        }
        
        // add statistic
        List<String> keys = getParameterRuntime().getParameterValueList(SLEEP_TIME_BY_A_PROCESSING_PARAMTER).getValueAsStringList();
        if (keys != null && !keys.isEmpty()) { 
            for (String key : getParameterRuntime().getParameterValueList(SLEEP_TIME_BY_A_PROCESSING_PARAMTER).getValueAsStringList()) {
                processingUnitStatusBuilder.statistic(key, 
                        Double.valueOf(RandomGenerator.getInstance().getRandomNumber(getParameterRuntime().getParameterValueList(STATISIIC_MAX_NUMBER_PARAMTER).getValueAsInteger(), false)));
            }
        }

        long toProcess = processingProgress.getNumberOfUnitsToProcess();
        long processed = processingProgress.getNumberOfProcessedUnits();
        long percentageNumbers = RoundUtil.getInstance().roundToLong(toProcess * 0.1);
        boolean allTenPercentageProcessed = (percentageNumbers == 0) || toProcess <= 0 || (percentageNumbers > 0 && processed > 0 && toProcess > 0 && (processed % percentageNumbers) == 0);
        if (allTenPercentageProcessed)  {
            if (getParameterRuntime().getParameterValueList(THROW_RUNTIME_EXCEPTION_PARAMTER).getValueAsBoolean()) {
                throw new RuntimeException("Test runtime exception in processing.");
            } else if (getParameterRuntime().getParameterValueList(THROW_PROCESSING_EXCEPTION_PARAMTER).getValueAsBoolean()) {
                throw new ProcessingException("Test process exception in processing.", false);
            } else if (getParameterRuntime().getParameterValueList(THROW_PROCESSING_EXCEPTION_AND_ABORT_PARAMTER).getValueAsBoolean()) {
                throw new ProcessingException("Test process exception in processing.", true);
            } else if (getParameterRuntime().getParameterValueList(END_AS_WARNING_PARAMTER).getValueAsBoolean()) {
                processingUnitStatusBuilder.warn("Test warn message.");
            } else if (getParameterRuntime().getParameterValueList(END_AS_ERROR_PARAMTER).getValueAsBoolean()) {
                processingUnitStatusBuilder.error("Test error message.");
            } else if (RandomGenerator.getInstance().getBooleanRandom()) {
                processingUnitStatusBuilder.message("Test processing message.");
            }
        }
        
        return processingUnitStatusBuilder.hasNext(processingProgress).build();
    }


    /**
     * Define sample an own persistence 
     * 
     * @author patrick
     */
    static class TestPersistence implements IProcessingUnitPersistence {
        private static final long serialVersionUID = -178680376384580300L;
        private String recordId;
        private long number;
        
        
        /**
         * Get the record id
         *
         * @return the record id
         */
        public String getRecordId() {
            return recordId; 
        }
        
        
        /**
         * Set the record id
         *
         * @param recordId the record id
         */
        public void setRecordId(String recordId) {
            this.recordId = recordId;
        }
        
        
        /**
         * Get the number
         *
         * @return the number
         */
        public long getNumber() {
            return number;
        }
        
        
        /**
         * Set the number
         *
         * @param number the number
         */
        public void setNumber(long number) {
            this.number = number;
        }

        
        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "TestPersistence [recordId=" + recordId + ", number=" + number + "]";
        }
    }
}
