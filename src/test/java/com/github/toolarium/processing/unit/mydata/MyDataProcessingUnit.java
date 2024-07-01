/*
 * MyDataProcessingUnit.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.mydata;

import com.github.toolarium.common.util.RandomGenerator;
import com.github.toolarium.processing.unit.IProcessingUnitContext;
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
    private boolean onStop;
    private boolean onSuccess;


    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#initializeParameterDefinition()
     */
    @Override
    protected void initializeParameterDefinition() {
        onStop = false;
        onSuccess = false;
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
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#estimateNumberOfUnitsToProcess(com.github.toolarium.processing.unit.IProcessingUnitContext)
     */
    @Override
    public long estimateNumberOfUnitsToProcess(IProcessingUnitContext processingUnitContext) throws ProcessingException {
        dataProducer = new MyDataProducer();
        dataProducer.init(getParameterRuntime().getParameterValueList(NUMBER_OF_TESTDATA_RECORDS).getValueAsInteger());
        return dataProducer.getSize();
    }


    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#processUnit(com.github.toolarium.processing.unit.IProcessingUnitContext)
     */
    @Override
    public IProcessingUnitStatus processUnit(IProcessingUnitProgress processingProgress, IProcessingUnitContext processingUnitContext) {
        ProcessingUnitStatusBuilder processingUnitStatusBuilder = new ProcessingUnitStatusBuilder(); 
        
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
            processingUnitStatusBuilder.hasNext();
        }
        
        if (result != null && result.isEmpty()) {
            processingUnitStatusBuilder.warn("Warning sample").processingUnitFailed();
        } else {
            processingUnitStatusBuilder.processedSuccessful();
        }

        if (!processingUnitContext.isEmpty()) {
            processingUnitContext.set("myId", RandomGenerator.getInstance().createUUID());
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
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#resumeProcessing(com.github.toolarium.processing.unit.IProcessingUnitPersistence, com.github.toolarium.processing.unit.IProcessingUnitContext)
     */
    @Override
    public void resumeProcessing(IProcessingUnitPersistence processingPersistence, IProcessingUnitContext processingUnitContext) throws ProcessingException {
        // set the processing persistence
        dataProducer = (MyDataProducer)processingPersistence;
    }

    
    /**
     * Gets the on stop status
     *
     * @return the status
     */
    public boolean getOnStopStatus() {
        return onStop;
    }

    
    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#onStop()
     */
    @Override
    public void onStop() {
        onStop = true;
    }




    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#onSuccess()
     */
    @Override
    public void onSuccess() {
        onSuccess = true;
    }


    /**
     * Gets the on success status
     *
     * @return the status
     */
    public boolean getOnSuccessStatus() {
        return onSuccess;
    }
}
