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

    /** The type of the value */
    private ParameterValueType valueType;

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
     * @param description The parameter description
     * @throws IllegalArgumentException In case of illegal arguments
     */
    public ParameterDefinition(String key, String description) throws IllegalArgumentException {
        this(key, null, NO_DEFAULT_PARAMETER, OPTIONAL, 1, EMPTY_VALUE_ALLOWED, description);
    }


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
     * @param valueType the value type
     * @param minOccurs The min occurs (zero minOccurs has the meaning of the value is optional)
     * @param description The parameter description
     * @throws IllegalArgumentException In case of illegal arguments
     */
    public ParameterDefinition(String key, ParameterValueType valueType, int minOccurs, String description) throws IllegalArgumentException {
        this(key, valueType, NO_DEFAULT_PARAMETER, minOccurs, 1, EMPTY_VALUE_ALLOWED, description);
    }


    /**
     * Constructor
     *
     * @param key the key
     * @param valueType the value type
     * @param defaultValue the default value
     * @param minOccurs The min occurs (zero minOccurs has the meaning of the value is optional)
     * @param description The parameter description
     * @throws IllegalArgumentException In case of illegal arguments
     */
    public ParameterDefinition(String key, ParameterValueType valueType, Object defaultValue, int minOccurs, String description) throws IllegalArgumentException {
        this(key, valueType, defaultValue, minOccurs, 1, EMPTY_VALUE_ALLOWED, description);
    }


    /**
     * Constructor
     *
     * @param key the key
     * @param valueType the value type
     * @param defaultValue the default value
     * @param minOccurs The min occurs (zero minOccurs has the meaning of the value is optional)
     * @param maxOccurs the max occurs
     * @param isEmptyValueAllowed true if an empty value is allowed
     * @param description The parameter description
     * @throws IllegalArgumentException In case of illegal arguments
     */
    private ParameterDefinition(String key,
                                ParameterValueType valueType,
                                Object defaultValue,
                                int minOccurs,
                                int maxOccurs,
                                boolean isEmptyValueAllowed,
                                String description) throws IllegalArgumentException {
        this(key, valueType, defaultValue, minOccurs, maxOccurs, isEmptyValueAllowed, false, description);
    }


    /**
     * Constructor
     *
     * @param key the key
     * @param valueType the value type
     * @param defaultValue the default value
     * @param minOccurs The min occurs (zero minOccurs has the meaning of the value is optional)
     * @param maxOccurs the max occurs
     * @param isEmptyValueAllowed true if an empty value is allowed
     * @param hasValueToProtect true if the value of this parameter should be protected; otherwise false.
     * @param description The parameter description
     * @throws IllegalArgumentException In case of illegal arguments
     */
    public ParameterDefinition(String key,
                               ParameterValueType valueType,
                               Object defaultValue,
                               int minOccurs,
                               int maxOccurs,
                               boolean isEmptyValueAllowed,
                               boolean hasValueToProtect,
                               String description)
        throws IllegalArgumentException {
        this.key = key;
        this.valueType = valueType;
        this.defaultValue = defaultValue;
        this.minOccurs = minOccurs;
        this.maxOccurs = maxOccurs;
        this.isEmptyValueAllowed = isEmptyValueAllowed;
        this.hasValueToProtect = hasValueToProtect;
        this.description = description;

        if (valueType == null && defaultValue != null) {
            if (defaultValue instanceof String) {
                this.valueType = ParameterValueType.STRING;
            } else if (defaultValue instanceof Boolean) {
                this.valueType = ParameterValueType.BOOLEAN;
            } else if (defaultValue instanceof Character) {
                this.valueType = ParameterValueType.CHAR;
            } else if (defaultValue instanceof Short) {
                this.valueType = ParameterValueType.SHORT;
            } else if (defaultValue instanceof Integer) {
                this.valueType = ParameterValueType.INTEGER;
            } else if (defaultValue instanceof Long) {
                this.valueType = ParameterValueType.LONG;
            } else if (defaultValue instanceof Float) {
                this.valueType = ParameterValueType.FLOAT;
            } else if (defaultValue instanceof Double) {
                this.valueType = ParameterValueType.DOUBLE;
            } else if (defaultValue instanceof Date) {
                this.valueType = ParameterValueType.DATETIME;
            }
        }

        if (this.valueType == null) {
            this.valueType = ParameterValueType.STRING;
        }

        validateDefaultValue(this.valueType, defaultValue);
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
     * Gets the value type
     *
     * @return the type
     */
    public ParameterValueType getValueType() {
        return valueType;
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
        return Objects.hash(defaultValue, description, hasValueToProtect, isEmptyValueAllowed, key, maxOccurs, minOccurs, valueType);
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
                && valueType == other.valueType;
    }
    

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ParameterDefinition [key=" + key + ", valueType=" + valueType + ", defaultValue=" + defaultValue
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
        result.valueType = valueType;
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
     * @param valueTypeDef the value type definition
     * @param inputdefaultValue the default value
     * @throws IllegalArgumentException In case of illegal arguments
     */
    private void validateDefaultValue(ParameterValueType valueTypeDef, Object inputdefaultValue) throws IllegalArgumentException {
        if (defaultValue == null) {
            return;
        }

        if (valueTypeDef == null) {
            throw new IllegalArgumentException("Invalid empty ValueType!");
        }

        if (inputdefaultValue.getClass().isArray()) {
            Object[] defaultValueArray = (Object[]) inputdefaultValue;
            for (int i = 0; i < defaultValueArray.length; i++) {
                validateDefaultValueAssignment(valueTypeDef, defaultValueArray[i]);
            }
        } else {
            validateDefaultValueAssignment(valueTypeDef, inputdefaultValue);
        }
    }

    
    /**
     * Validate the default value assignment
     *
     * @param valueTypeDef the value type definition
     * @param inputDefaultValue the default value
     * @throws IllegalArgumentException In case of illegal arguments
     */
    private void validateDefaultValueAssignment(ParameterValueType valueTypeDef, Object inputDefaultValue) throws IllegalArgumentException {
        if ((ParameterValueType.STRING.equals(valueTypeDef) && (inputDefaultValue instanceof CharSequence))
                || (ParameterValueType.BOOLEAN.equals(valueTypeDef) && (inputDefaultValue instanceof Boolean))
                || (ParameterValueType.CHAR.equals(valueTypeDef) && (inputDefaultValue instanceof Character))
                || (ParameterValueType.SHORT.equals(valueTypeDef) && (inputDefaultValue instanceof Short))
                || (ParameterValueType.INTEGER.equals(valueTypeDef) && (inputDefaultValue instanceof Integer))
                || (ParameterValueType.LONG.equals(valueTypeDef) && (inputDefaultValue instanceof Long))
                || (ParameterValueType.FLOAT.equals(valueTypeDef) && (inputDefaultValue instanceof Character))
                || (ParameterValueType.DOUBLE.equals(valueTypeDef) && (inputDefaultValue instanceof Character))
                || (ParameterValueType.INTEGER.equals(valueTypeDef) && (inputDefaultValue instanceof Character))
                || (ParameterValueType.DATE.equals(valueTypeDef) && (inputDefaultValue instanceof Date))
                || (ParameterValueType.TIME.equals(valueTypeDef) && (inputDefaultValue instanceof Date))
                || (ParameterValueType.DATETIME.equals(valueTypeDef) && (inputDefaultValue instanceof Date))
                || (ParameterValueType.REGEXP.equals(valueTypeDef) && (inputDefaultValue instanceof CharSequence))) {
            // valid
        } else {
            throw new IllegalArgumentException("Invalid default value, don't match to the definied ValueType: '" + valueTypeDef + "' != " + defaultValue.getClass());
        }
    }
}
