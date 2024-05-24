/*
 * MyDataProcessingUnit.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.mydata;

import com.github.toolarium.processing.unit.IProcessStatus;
import com.github.toolarium.processing.unit.IProcessingPersistence;
import com.github.toolarium.processing.unit.IProcessingProgress;
import com.github.toolarium.processing.unit.IProcessingUnitContext;
import com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl;
import com.github.toolarium.processing.unit.dto.Parameter;
import com.github.toolarium.processing.unit.dto.ProcessingStatusType;
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
    private MyDataProducer dataProducer;
    private boolean onStop;
    private boolean onSuccess;


    /**
     * Constructor
     */
    public MyDataProcessingUnit() {
        super();
        onStop = false;
        onSuccess = false;
    }


    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#initializeParameterDefinition()
     */
    @Override
    protected void initializeParameterDefinition() {
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
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#initialize(java.util.List, com.github.toolarium.processing.unit.IProcessingUnitContext)
     */
    @Override
    public void initialize(java.util.List<com.github.toolarium.processing.unit.dto.Parameter> parameterList, IProcessingUnitContext processingUnitContext) {
        super.initialize(parameterList, processingUnitContext);

        dataProducer = new MyDataProducer();
        dataProducer.init();
        getProcessingProgress().setTotalUnits(dataProducer.getSize());
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
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#onStop()
     */
    @Override
    public void onStop() {
        onStop = true;
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
    public void resumeProcessing(List<Parameter> parameterList, IProcessingProgress resumeProcessingProgress, IProcessingPersistence processingPersistence, IProcessingUnitContext processingUnitContext) 
            throws ProcessingException {
        super.initialize(parameterList, processingUnitContext);
        dataProducer = (MyDataProducer)processingPersistence;

        // initialize previous state
        getProcessingProgress().init(resumeProcessingProgress);
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
            getProcessingProgress().setProcessingStatusType(ProcessingStatusType.WARN);
            getProcessingProgress().increaseTotalFailedUnits();
        }

        getProcessingProgress().addStatistic("PROCEEDING", 5d);

        // the next lines are written to test the default value behavior. Please don't do this in your code!
        if (!status.hasNext()) {
            getProcessingProgress().addStatistic(COUNTER_PARAMETER.getKey(), getParameterRuntime().getParameterValueList(COUNTER_PARAMETER).getValueAsDouble());
            getProcessingProgress().addStatistic(DEFAULTVALUE_TEST_PARAMETER.getKey(), getParameterRuntime().getParameterValueList(DEFAULTVALUE_TEST_PARAMETER).getValueAsDouble());
        }

        getProcessingProgress().increaseTotalProcessedUnits();
        return status;
    }
}
