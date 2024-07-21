/*
 * MyDataProcessingUnit.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.mydata;

import com.github.toolarium.common.util.RandomGenerator;
import com.github.toolarium.processing.unit.IProcessingUnitPersistence;
import com.github.toolarium.processing.unit.IProcessingUnitProgress;
import com.github.toolarium.processing.unit.IProcessingUnitStatus;
import com.github.toolarium.processing.unit.ProcessingUnitStatusBuilder;
import com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl;
import com.github.toolarium.processing.unit.dto.Parameter;
import com.github.toolarium.processing.unit.exception.ProcessingException;
import com.github.toolarium.processing.unit.exception.ValidationException;
import java.util.List;
import java.util.logging.Logger;


/**
 * Sample of a {@link IProcessingUnitProgress}
 *
 * @author patrick
 */
public final class MyDataProcessingUnit extends AbstractProcessingUnitImpl implements MyDataProcessingUnitConstants {
    public static final String PROCEEDING_KEY = "PROCEEDING";
    public static final double PROCEEDING_BASE_VALUE = 5d;
    private MyDataProducer dataProducer;
    private boolean onEnding;
    private boolean onAborting;


    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#initializeParameterDefinition()
     */
    @Override
    protected void initializeParameterDefinition() {
        onEnding = false;
        onAborting = false;
        getParameterRuntime().addParameterDefinition(NUMBER_OF_TESTDATA_RECORDS);
        getParameterRuntime().addParameterDefinition(FILENAME_PARAMETER);
        getParameterRuntime().addParameterDefinition(DEFAULTVALUE_TEST_PARAMETER);
        getParameterRuntime().addParameterDefinition(COUNTER_PARAMETER);
        getParameterRuntime().addParameterDefinition(HASH_NAMES);
        getParameterRuntime().addParameterDefinition(KEY_NAMES);

        super.initializeParameterDefinition();
    }


    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#validateParameterList(java.util.List)
     */
    @Override
    public void validateParameterList(List<Parameter> parameterList) throws ValidationException {
        super.validateParameterList(parameterList);

        if (getParameterRuntime().getParameterValueList(parameterList, COUNTER_PARAMETER).getValueAsInteger() <= 0) {
            throw new ValidationException("Invalid parameter value of parameter [" + COUNTER_PARAMETER.getKey() + "]: " + getParameterRuntime().getParameterValueList(parameterList, COUNTER_PARAMETER).getValueAsInteger());
        }
    }


    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#estimateNumberOfUnitsToProcess()
     */
    @Override
    public long estimateNumberOfUnitsToProcess() throws ProcessingException {
        dataProducer = new MyDataProducer();
        dataProducer.init(getParameterRuntime().getParameterValueList(NUMBER_OF_TESTDATA_RECORDS).getValueAsInteger());
        return getProcessingUnitProgress().setNumberOfUnitsToProcess(dataProducer.getSize());
    }


    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#processUnit(com.github.toolarium.processing.unit.ProcessingUnitStatusBuilder)
     */
    @Override
    public IProcessingUnitStatus processUnit(ProcessingUnitStatusBuilder processingUnitStatusBuilder) {
        
        String fileName = "fileName";
        if (getParameterRuntime().existParameter(FILENAME_PARAMETER)) {
            fileName = getParameterRuntime().getParameterValueList(FILENAME_PARAMETER).getValueAsString();
            Logger.getLogger(MyDataProcessingUnit.class.getName()).fine("Filename: " + fileName);
        }

        // don't do this in your code, this is only for test purpose of the default values
        for (String hashName : getParameterRuntime().getParameterValueList(HASH_NAMES).getValueAsStringList()) {
            processingUnitStatusBuilder.statistic(hashName, 1d);
        }

        String result = dataProducer.getData();
        if (result == null || !dataProducer.hasMoreData()) {
            processingUnitStatusBuilder.hasEnded();
            
            // the next lines are written to test the default value behavior. Please don't do this in your code!
            processingUnitStatusBuilder.statistic(COUNTER_PARAMETER.getKey(), getParameterRuntime().getParameterValueList(COUNTER_PARAMETER).getValueAsDouble());
            processingUnitStatusBuilder.statistic(DEFAULTVALUE_TEST_PARAMETER.getKey(), getParameterRuntime().getParameterValueList(DEFAULTVALUE_TEST_PARAMETER).getValueAsDouble());
        } else {
            processingUnitStatusBuilder.hasNextIfHasUnprocessedUnits();
        }
        
        if (result != null && result.isEmpty()) {
            processingUnitStatusBuilder.warn("Warning sample").processingUnitFailed();
        } else {
            processingUnitStatusBuilder.processedSuccessful();
        }

        if (!getProcessingUnitContext().isEmpty()) {
            getProcessingUnitContext().set("myId", RandomGenerator.getInstance().createUUID());
        }
        
        processingUnitStatusBuilder.statistic(PROCEEDING_KEY, PROCEEDING_BASE_VALUE);

        //return processingUnitStatusBuilder.hasNext(processingProgress).build();
        return processingUnitStatusBuilder.build();
    }

    
    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#releaseResource()
     */
    @Override
    public void releaseResource() throws ProcessingException {
        try {
            super.releaseResource();
        } finally {
            if (dataProducer != null) {
                dataProducer.close();
            }
        }
    }


    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#suspendProcessing()
     */
    @Override
    public IProcessingUnitPersistence suspendProcessing() throws ProcessingException {
        return dataProducer;
    }


    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#resumeProcessing(com.github.toolarium.processing.unit.IProcessingUnitProgress, com.github.toolarium.processing.unit.IProcessingUnitPersistence)
     */
    @Override
    public void resumeProcessing(IProcessingUnitProgress processingUnitProgress, IProcessingUnitPersistence processingPersistence) throws ProcessingException {
        super.resumeProcessing(processingUnitProgress, processingPersistence);
        
        // set the processing persistence
        dataProducer = (MyDataProducer)processingPersistence;
    }

    
    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#onEnding()
     */
    @Override
    public void onEnding() {
        onEnding = true;
    }

    
    /**
     * Gets the on ending status
     *
     * @return the status
     */
    public boolean getOnEndingStatus() {
        return onEnding;
    }


    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#onAborting()
     */
    @Override
    public void onAborting() {
        onAborting = true;
    }


    /**
     * Gets the on aborting status
     *
     * @return the status
     */
    public boolean getOnAbortingStatus() {
        return onAborting;
    }
}
