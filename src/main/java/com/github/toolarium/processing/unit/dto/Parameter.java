/*
 * Parameter.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.dto;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


/**
 * Defines the processing parameter.
 *
 * @author patrick
 */
public final class Parameter implements Serializable {
    private static final long serialVersionUID = 2951075291489078812L;
    private String key;
    private ParameterValue value;


    /**
     * Constructor
     *
     * @param key the key
     */
    public Parameter(String key) {
        this.key = key;
        this.value = new ParameterValue();
    }


    /**
     * Constructor
     *
     * @param key the key
     * @param string the value as an array of strings
     */
    public Parameter(String key, String... string) {
        this.key = key;
        this.value = new ParameterValue(Arrays.asList(string));
    }


    /**
     * Constructor
     *
     * @param key the key
     * @param stringList the value list as string
     */
    public Parameter(String key, List<String> stringList) {
        this.key = key;
        this.value = new ParameterValue(stringList);
    }


    /**
     * Constructor
     *
     * @param key the key
     * @param parameterValue the parameter value
     */
    public Parameter(String key, ParameterValue parameterValue) {
        this.key = key;
        this.value = parameterValue;
    }


    /**
     * Gets the key
     *
     * @return the key
     */
    public String getKey() {
        return key;
    }


    /**
     * Gets the parameter value
     *
     * @return the parameter value
     */
    public ParameterValue getParameterValue() {
        return value;
    }

    
    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        
        if (obj == null) {
            return false;
        }
        
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        Parameter other = (Parameter) obj;
        return Objects.equals(key, other.key) && Objects.equals(value, other.value);
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Parameter [key=" + key + ", value=" + value + "]";
    }
}
