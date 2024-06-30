/*
 * ParameterDefinitionBuilder.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit;

import com.github.toolarium.processing.unit.dto.ParameterDefinition;
import com.github.toolarium.processing.unit.dto.ParameterValueType;


/**
 * Defines a parameter definition builder: At least the parameter must define a key and a description.
 * 
 * @author patrick
 */
public class ParameterDefinitionBuilder {
    private String name;
    private String description;
    private ParameterValueType valueDataType;
    private Object defaultValue;
    private int minOccurs;
    private int maxOccurs;
    private boolean isEmptyValueAllowed;
    private boolean hasValueToProtect;
    
    
    /**
     * Constructor for ParameterDefinitionBuilder
     */
    public ParameterDefinitionBuilder() {
        name = null;
        description = null;
        valueDataType = ParameterValueType.STRING;
        defaultValue = ParameterDefinition.NO_DEFAULT_PARAMETER;
        minOccurs = ParameterDefinition.OPTIONAL;
        maxOccurs = 1;
        isEmptyValueAllowed = ParameterDefinition.EMPTY_VALUE_ALLOWED;
        hasValueToProtect = false;
    }

    
    /**
     * Set the parameter name.
     *
     * @param name the name
     * @return this instance
     */
    public ParameterDefinitionBuilder name(String name) {
        this.name = name;
        return this;
    }

    
    /**
     * Set the description
     *
     * @param description the description
     * @return this instance
     */
    public ParameterDefinitionBuilder description(String description) {
        this.description = description;
        return this;
    }

    
    /**
     * Set the value data type
     *
     * @param valueDataType the value data type
     * @return this instance
     */
    public ParameterDefinitionBuilder type(ParameterValueType valueDataType) {
        this.valueDataType = valueDataType;
        return this;
    }

    
    /**
     * Set the default value
     *
     * @param defaultValue the default value
     * @return this instance
     */
    public ParameterDefinitionBuilder defaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    
    /**
     * Defines the parameter is optional (sets implicit the min occurs).
     *
     * @return this instance
     */
    public ParameterDefinitionBuilder isOptional() {
        this.minOccurs = ParameterDefinition.OPTIONAL;
        return this;
    }

    
    /**
     * Defines the parameter is mandatory (sets implicit the min occurs).
     *
     * @return this instance
     */
    public ParameterDefinitionBuilder isMandatory() {
        this.minOccurs = ParameterDefinition.NOT_OPTIONAL;
        return this;
    }

    
    /**
     * Set the min occurs: The min occurs (zero minOccurs has the meaning of the value is optional)
     *
     * @param minOccurs the min occurs
     * @return this instance
     */
    public ParameterDefinitionBuilder minOccurs(int minOccurs) {
        this.minOccurs = minOccurs;
        return this;
    }

    
    /**
     * Set the max occurs
     *
     * @param maxOccurs the max occurs
     * @return this instance
     */
    public ParameterDefinitionBuilder maxOccurs(int maxOccurs) {
        this.maxOccurs = maxOccurs;
        return this;
    }

    
    /**
     * Defines an empty value is allowed
     *
     * @return this instance
     */
    public ParameterDefinitionBuilder emptyValueIsAllowed() {
        this.isEmptyValueAllowed = true;
        return this;
    }

    
    /**
     * Defines an empty value is not allowed
     *
     * @return this instance
     */
    public ParameterDefinitionBuilder emptyValueIsNotAllowed() {
        this.isEmptyValueAllowed = false;
        return this;
    }


    /**
     * Define if the value must be protected, e.g. password / credentials
     *
     * @return this instance
     */
    public ParameterDefinitionBuilder hasValueToProtect() {
        this.hasValueToProtect = true;
        return this;
    }


    /**
     * Build the parameter definition
     *
     * @return the parameter definition
     */
    public ParameterDefinition build() {
        return new ParameterDefinition(name, valueDataType, defaultValue, minOccurs, maxOccurs, isEmptyValueAllowed, hasValueToProtect, description);
    }
}
