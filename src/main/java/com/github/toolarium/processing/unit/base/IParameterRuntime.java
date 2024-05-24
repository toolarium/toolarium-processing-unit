/*
 * IParameterRuntime.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.base;

import com.github.toolarium.processing.unit.IProcessingUnitContext;
import com.github.toolarium.processing.unit.dto.Parameter;
import com.github.toolarium.processing.unit.dto.ParameterDefinition;
import com.github.toolarium.processing.unit.dto.ParameterValue;
import com.github.toolarium.processing.unit.exception.ProcessingException;
import com.github.toolarium.processing.unit.exception.ValidationException;
import java.util.List;


/**
 * Defines the runtime parameter information.
 * 
 * @author patrick
 */
public interface IParameterRuntime {
    
    /**
     * Gets the parameter definition list of the processing.
     *
     * @return the parameter definition list or null in case of no parameters
     */
    List<ParameterDefinition> getParameterDefinition();

    
    /**
     * Gets the parameter definition of a specific key
     *
     * @param parameterDefinition the parameter definition
     */
    void addParameterDefinition(ParameterDefinition parameterDefinition);

    
    /**
     * Check if the parameter exists or not
     *
     * @param parameterDefinition the parameter definition
     * @return true or false
     */
    boolean existParameter(ParameterDefinition parameterDefinition);

    
    /**
     * Set the processing unit parameters. 
     *
     * @param parameterList the parameter list to run the processing.
     * @param processingUnitContext the processing unit context.
     * @throws ValidationException This will be throw in case the consistency check failures.
     * @throws ProcessingException Throws this exception in case of initialisation failures.
     */
    void setParameterList(List<Parameter> parameterList, IProcessingUnitContext processingUnitContext) throws ValidationException, ProcessingException;

    
    /**
     * Validate the parameter list consistency. It can be used to verify if the parameter of this processing are well defined.
     *
     * @param parameterList the parameter list.
     * @throws ValidationException This will be throw in case the consistency check failures.
     */
    void validateParameterList(List<Parameter> parameterList) throws ValidationException;

    
    /**
     * Get the parameter value list.
     *
     * @param parameterDefinition The parameter definition
     * @return The parameter value. In case the parameter is not defined and the parameter has a default value the default value will be returned.
     */
    ParameterValue getParameterValueList(ParameterDefinition parameterDefinition);
    
    
    /**
     * Get the parameter value list.
     *
     * @param parameterList the parameter list
     * @param parameterDefinition The parameter definition
     * @return The parameter value. In case the parameter is not defined and the parameter has a default value the default value will be returned.
     */
    ParameterValue getParameterValueList(List<Parameter> parameterList, ParameterDefinition parameterDefinition);
}
