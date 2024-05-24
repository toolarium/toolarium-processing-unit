/*
 * ParameterDefinition.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.dto;


import java.io.Serializable;
import java.util.Date;
import java.util.Objects;


/**
 * Defines the parameter definition.
 *
 * @author patrick
 */
public final class ParameterDefinition implements Serializable {

    /** NO_DEFAULT_PARAMETER: Constant for no default parameter **/
    public static final Object NO_DEFAULT_PARAMETER = null;

    /** OPTIONAL: Constant for minOccurs = 0 **/
    public static final int OPTIONAL = 0;

    /** NOT_OPTIONAL: Constant for minOccurs = 1 **/
    public static final int NOT_OPTIONAL = 1;

    /** EMPTY_VALUE_ALLOWED: Constant for empty value allowed **/
    public static final boolean EMPTY_VALUE_ALLOWED = true;

    /** EMPTY_VALUE_NOT_ALLOWED: Constant for empty value not allowed **/
    public static final boolean EMPTY_VALUE_NOT_ALLOWED = false;

    private static final long serialVersionUID = 6438515276120152690L;

    /** The parameter key */
    private String key;

    /** The data type of the value */
    private ParameterValueType valueDataType;

    /** Defines the default value */
    private Object defaultValue;

    /** The min occurs (zero minOccurs has the meaning of the value is optional) */
    private int minOccurs;

    /** The max occurs */
    private int maxOccurs;

    /** Defines if an empty string value is also valid or not */
    private boolean isEmptyValueAllowed;

    /** Defines if the value of this parameter should be protected */
    private boolean hasValueToProtect;

    /** The parameter description */
    private String description;


    /**
     * Constructor: only key parameter without value.
     *
     * @param key the key
     * @param minOccurs The min occurs (zero minOccurs has the meaning of the value is optional)
     * @param description The parameter description
     * @throws IllegalArgumentException In case of illegal arguments
     */
    public ParameterDefinition(String key, int minOccurs, String description) throws IllegalArgumentException {
        this(key, null, NO_DEFAULT_PARAMETER, minOccurs, 1, EMPTY_VALUE_ALLOWED, description);
    }


    /**
     * Constructor
     *
     * @param key the key
     * @param defaultValue the default value
     * @param minOccurs The min occurs (zero minOccurs has the meaning of the value is optional)
     * @param description The parameter description
     * @throws IllegalArgumentException In case of illegal arguments
     */
    public ParameterDefinition(String key, Object defaultValue, int minOccurs, String description) throws IllegalArgumentException {
        this(key, null, defaultValue, minOccurs, 1, EMPTY_VALUE_ALLOWED, description);
    }


    /**
     * Constructor
     *
     * @param key the key
     * @param valueDataType the value data type
     * @param minOccurs The min occurs (zero minOccurs has the meaning of the value is optional)
     * @param description The parameter description
     * @throws IllegalArgumentException In case of illegal arguments
     */
    public ParameterDefinition(String key, ParameterValueType valueDataType, int minOccurs, String description) throws IllegalArgumentException {
        this(key, valueDataType, NO_DEFAULT_PARAMETER, minOccurs, 1, EMPTY_VALUE_ALLOWED, description);
    }


    /**
     * Constructor
     *
     * @param key the key
     * @param valueDataType the value data type
     * @param defaultValue the default value
     * @param minOccurs The min occurs (zero minOccurs has the meaning of the value is optional)
     * @param description The parameter description
     * @throws IllegalArgumentException In case of illegal arguments
     */
    public ParameterDefinition(String key, ParameterValueType valueDataType, Object defaultValue, int minOccurs, String description) throws IllegalArgumentException {
        this(key, valueDataType, defaultValue, minOccurs, 1, EMPTY_VALUE_ALLOWED, description);
    }


    /**
     * Constructor
     *
     * @param key the key
     * @param valueDataType the value data type
     * @param defaultValue the default value
     * @param minOccurs The min occurs (zero minOccurs has the meaning of the value is optional)
     * @param maxOccurs the max occurs
     * @param isEmptyValueAllowed true if an empty value is allowed
     * @param description The parameter description
     * @throws IllegalArgumentException In case of illegal arguments
     */
    public ParameterDefinition(String key,
                               ParameterValueType valueDataType,
                               Object defaultValue,
                               int minOccurs,
                               int maxOccurs,
                               boolean isEmptyValueAllowed,
                               String description)
        throws IllegalArgumentException {
        this(key, valueDataType, defaultValue, minOccurs, maxOccurs, isEmptyValueAllowed, false, description);
    }


    /**
     * Constructor
     *
     * @param key the key
     * @param valueDataType the value data type
     * @param defaultValue the default value
     * @param minOccurs The min occurs (zero minOccurs has the meaning of the value is optional)
     * @param maxOccurs the max occurs
     * @param isEmptyValueAllowed true if an empty value is allowed
     * @param hasValueToProtect true if the value of this parameter should be protected; otherwise false.
     * @param description The parameter description
     * @throws IllegalArgumentException In case of illegal arguments
     */
    public ParameterDefinition(String key,
                               ParameterValueType valueDataType,
                               Object defaultValue,
                               int minOccurs,
                               int maxOccurs,
                               boolean isEmptyValueAllowed,
                               boolean hasValueToProtect,
                               String description)
        throws IllegalArgumentException {
        this.key = key;
        this.valueDataType = valueDataType;
        this.defaultValue = defaultValue;
        this.minOccurs = minOccurs;
        this.maxOccurs = maxOccurs;
        this.isEmptyValueAllowed = isEmptyValueAllowed;
        this.hasValueToProtect = hasValueToProtect;
        this.description = description;

        if (valueDataType == null && defaultValue != null) {
            if (defaultValue instanceof String) {
                this.valueDataType = ParameterValueType.STRING;
            } else if (defaultValue instanceof Boolean) {
                this.valueDataType = ParameterValueType.BOOLEAN;
            } else if (defaultValue instanceof Character) {
                this.valueDataType = ParameterValueType.CHAR;
            } else if (defaultValue instanceof Short) {
                this.valueDataType = ParameterValueType.SHORT;
            } else if (defaultValue instanceof Integer) {
                this.valueDataType = ParameterValueType.INTEGER;
            } else if (defaultValue instanceof Long) {
                this.valueDataType = ParameterValueType.LONG;
            } else if (defaultValue instanceof Float) {
                this.valueDataType = ParameterValueType.FLOAT;
            } else if (defaultValue instanceof Double) {
                this.valueDataType = ParameterValueType.DOUBLE;
            } else if (defaultValue instanceof Date) {
                this.valueDataType = ParameterValueType.DATETIME;
            }
        }

        if (this.valueDataType == null) {
            this.valueDataType = ParameterValueType.STRING;
        }

        validateDefaultValue(this.valueDataType, defaultValue);
    }


    /**
     * Gets the key.
     *
     * @return the key
     */
    public String getKey() {
        return key;
    }

    
    /**
     * Gets the value data type
     *
     * @return the data type
     */
    public ParameterValueType getValueDataType() {
        return valueDataType;
    }

    
    /**
     * Gets the default value
     *
     * @return the default value
     */
    public Object getDefaultValue() {
        return defaultValue;
    }

    
    /**
     * Check if the key is optional or not.
     *
     * @return true if it is optional
     */
    public boolean isOptional() {
        return (minOccurs == 0);
    }

    
    /**
     * Get the min occurs.
     *
     * @return the min occurs.
     */
    public int getMinOccurs() {
        return minOccurs;
    }


    /**
     * Get the max occurs.
     *
     * @return the max occurs.
     */
    public int getMaxOccurs() {
        return maxOccurs;
    }

    
    /**
     * Check if an empty value is allowed or not.
     *
     * @return true if an empty value is allowed
     */
    public boolean isEmptyValueAllowed() {
        return isEmptyValueAllowed;
    }


    /**
     * Check if the value of this parameter should be protected.
     *
     * @return true if the value of this parameter should be protected; otherwise false.
     */
    public boolean hasValueToProtect() {
        return hasValueToProtect;
    }

    
    /**
     * Gets the parameter description.
     *
     * @return The parameter description
     */
    public String getDescription() {
        return description;
    }



    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hash(defaultValue, description, hasValueToProtect, isEmptyValueAllowed, key, maxOccurs, minOccurs, valueDataType);
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
        
        ParameterDefinition other = (ParameterDefinition) obj;
        return Objects.equals(defaultValue, other.defaultValue) && Objects.equals(description, other.description)
                && hasValueToProtect == other.hasValueToProtect && isEmptyValueAllowed == other.isEmptyValueAllowed
                && Objects.equals(key, other.key) && maxOccurs == other.maxOccurs && minOccurs == other.minOccurs
                && valueDataType == other.valueDataType;
    }
    

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ParameterDefinition [key=" + key + ", valueDataType=" + valueDataType + ", defaultValue=" + defaultValue
                + ", minOccurs=" + minOccurs + ", maxOccurs=" + maxOccurs + ", isEmptyValueAllowed="
                + isEmptyValueAllowed + ", hasValueToProtect=" + hasValueToProtect + ", description=" + description
                + "]";
    }


    /**
     * @see java.lang.Object#clone()
     */
    @Override
    public ParameterDefinition clone() throws CloneNotSupportedException {
        ParameterDefinition result = (ParameterDefinition)super.clone();
        result.key = key;
        result.valueDataType = valueDataType;
        result.defaultValue = defaultValue;
        result.minOccurs = minOccurs;
        result.maxOccurs = maxOccurs;
        result.isEmptyValueAllowed = isEmptyValueAllowed;
        result.hasValueToProtect = hasValueToProtect;
        result.description = description;
        return result;
    }


    /**
     * Validate the default value
     *
     * @param valueDataTypeDef the value data type definition
     * @param defaultDataValue the default value
     * @throws IllegalArgumentException In case of illegal arguments
     */
    private void validateDefaultValue(ParameterValueType valueDataTypeDef, Object defaultDataValue) throws IllegalArgumentException {
        if (defaultValue == null) {
            return;
        }

        if (valueDataTypeDef == null) {
            throw new IllegalArgumentException("Invalid empty ValueDataType!");
        }

        if (defaultDataValue.getClass().isArray()) {
            Object[] defaultDataValueArray = (Object[]) defaultDataValue;
            for (int i = 0; i < defaultDataValueArray.length; i++) {
                validateDefaultValueAssignment(valueDataTypeDef, defaultDataValueArray[i]);
            }
        } else {
            validateDefaultValueAssignment(valueDataTypeDef, defaultDataValue);
        }
    }

    
    /**
     * Validate the default value assignment
     *
     * @param valueDataTypeDef the value data type definition
     * @param defaultDataValue the default value
     * @throws IllegalArgumentException In case of illegal arguments
     */
    private void validateDefaultValueAssignment(ParameterValueType valueDataTypeDef, Object defaultDataValue) throws IllegalArgumentException {
        if ((ParameterValueType.STRING.equals(valueDataTypeDef) && (defaultDataValue instanceof CharSequence))
                || (ParameterValueType.BOOLEAN.equals(valueDataTypeDef) && (defaultDataValue instanceof Boolean))
                || (ParameterValueType.CHAR.equals(valueDataTypeDef) && (defaultDataValue instanceof Character))
                || (ParameterValueType.SHORT.equals(valueDataTypeDef) && (defaultDataValue instanceof Short))
                || (ParameterValueType.INTEGER.equals(valueDataTypeDef) && (defaultDataValue instanceof Integer))
                || (ParameterValueType.LONG.equals(valueDataTypeDef) && (defaultDataValue instanceof Long))
                || (ParameterValueType.FLOAT.equals(valueDataTypeDef) && (defaultDataValue instanceof Character))
                || (ParameterValueType.DOUBLE.equals(valueDataTypeDef) && (defaultDataValue instanceof Character))
                || (ParameterValueType.INTEGER.equals(valueDataTypeDef) && (defaultDataValue instanceof Character))
                || (ParameterValueType.DATE.equals(valueDataTypeDef) && (defaultDataValue instanceof Date))
                || (ParameterValueType.TIME.equals(valueDataTypeDef) && (defaultDataValue instanceof Date))
                || (ParameterValueType.DATETIME.equals(valueDataTypeDef) && (defaultDataValue instanceof Date))
                || (ParameterValueType.REGEXP.equals(valueDataTypeDef) && (defaultDataValue instanceof CharSequence))) {
            // valid
        } else {
            throw new IllegalArgumentException("Invalid default value, don't match to the definied ValueDataType: '" + valueDataTypeDef + "' != " + defaultValue.getClass());
        }
    }
}
