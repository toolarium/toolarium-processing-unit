/*
 * MyDataProcessingUnit.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.mydata;

import com.github.toolarium.common.util.RandomGenerator;
import com.github.toolarium.processing.unit.IProcessStatus;
import com.github.toolarium.processing.unit.IProcessingPersistence;
import com.github.toolarium.processing.unit.IProcessingProgress;
import com.github.toolarium.processing.unit.IProcessingUnitContext;
import com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl;
import com.github.toolarium.processing.unit.dto.Parameter;
import com.github.toolarium.processing.unit.dto.ProcessingRuntimeStatus;
import com.github.toolarium.processing.unit.exception.ProcessingException;
import com.github.toolarium.processing.unit.exception.ValidationException;
import com.github.toolarium.processing.unit.runtime.ProcessStatus;
import java.util.List;
import java.util.logging.Logger;


/**
 * Sample of a {@link IProcessingProgress}
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
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#countNumberOfUnitsToProcess(com.github.toolarium.processing.unit.IProcessingUnitContext)
     */
    @Override
    protected long countNumberOfUnitsToProcess(IProcessingUnitContext processingUnitContext) throws ProcessingException {
        dataProducer = new MyDataProducer();
        dataProducer.init(getParameterRuntime().getParameterValueList(NUMBER_OF_TESTDATA_RECORDS).getValueAsInteger());
        return dataProducer.getSize();
    }


    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#processUnit(com.github.toolarium.processing.unit.IProcessingUnitContext)
     */
    @Override
    public IProcessStatus processUnit(IProcessingUnitContext processingUnitContext) {
        String fileName = "fileName";
        ProcessStatus status = super.getProcessStatus();

        if (getParameterRuntime().existParameter(FILENAME_PARAMETER)) {
            fileName = getParameterRuntime().getParameterValueList(FILENAME_PARAMETER).getValueAsString();
            Logger.getLogger(MyDataProcessingUnit.class.getName()).fine("Filename: " + fileName);
        }

        // don't do this in your code, this is only for test purpose of the default values
        for (String hashName : getParameterRuntime().getParameterValueList(HASH_NAMES).getValueAsStringList()) {
            getProcessingProgress().addStatistic(hashName, 1d);
        }

        String result = dataProducer.getData();
        status.setHasNext(true);
        if (result == null || !dataProducer.hasMoreData()) {
            status.setHasNext(false);
        } 
        
        if (result != null && result.isEmpty()) {
            getProcessingProgress().setProcessingRuntimeStatus(ProcessingRuntimeStatus.WARN);
            getProcessingProgress().increaseNumberOfFailedUnits();
        }

        if (!processingUnitContext.isEmpty()) {
            processingUnitContext.set("myId", RandomGenerator.getInstance().createUUID());
        }
        
        getProcessingProgress().addStatistic(PROCEEDING_KEY, PROCEEDING_BASE_VALUE);

        // the next lines are written to test the default value behavior. Please don't do this in your code!
        if (!status.hasNext()) {
            getProcessingProgress().addStatistic(COUNTER_PARAMETER.getKey(), getParameterRuntime().getParameterValueList(COUNTER_PARAMETER).getValueAsDouble());
            getProcessingProgress().addStatistic(DEFAULTVALUE_TEST_PARAMETER.getKey(), getParameterRuntime().getParameterValueList(DEFAULTVALUE_TEST_PARAMETER).getValueAsDouble());
        }

        getProcessingProgress().increaseNumberOfProcessedUnits();
        return status;
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
    public IProcessingPersistence suspendProcessing() throws ProcessingException {
        return dataProducer;
    }


    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#resumeProcessing(java.util.List, com.github.toolarium.processing.unit.IProcessingProgress, 
     * com.github.toolarium.processing.unit.IProcessingPersistence, com.github.toolarium.processing.unit.IProcessingUnitContext)
     */
    @Override
    public void resumeProcessing(List<Parameter> parameterList, IProcessingProgress resumeProcessingProgress, IProcessingPersistence processingPersistence, IProcessingUnitContext processingUnitContext) throws ProcessingException {
        // initialize the parameters
        super.initialize(parameterList, processingUnitContext); 
        
        // set the processing persistence
        dataProducer = (MyDataProducer)processingPersistence;

        // initialize previous state
        getProcessingProgress().init(resumeProcessingProgress);
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
