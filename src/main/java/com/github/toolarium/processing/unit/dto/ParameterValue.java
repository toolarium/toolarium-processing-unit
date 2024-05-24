/*
 * ParameterValue.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.dto;

import com.github.toolarium.common.util.DateUtil;
import com.github.toolarium.common.util.PropertyExpander;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;


/**
 * Defines the value list. It acts as input parameter value
 *
 * @author patrick
 */
public final class ParameterValue implements Serializable {
    private static final long serialVersionUID = -3825765070859460949L;
    private List<String> valueList;


    /**
     * Constructor
     */
    public ParameterValue() {
        valueList = new ArrayList<String>();
    }


    /**
     * Constructor
     *
     * @param valueList the value list
     */
    public ParameterValue(List<String> valueList) {
        this.valueList = valueList;
    }


    /**
     * Gets the first value
     *
     * @return the value
     */
    public String getValueAsString() {
        return getValueAsString(0);
    }


    /**
     * Gets the value as string.
     *
     * @param idx the index
     * @return the value
     * @throws ArrayIndexOutOfBoundsException In case out of index
     */
    public String getValueAsString(int idx) {
        if (valueList == null) {
            return null;
        }

        if (idx > valueList.size()) {
            throw new ArrayIndexOutOfBoundsException(idx);
        }

        if (isEmpty()) {
            return null;
        }

        String result = valueList.get(idx);
        return PropertyExpander.getInstance().expand(result);
    }


    /**
     * Gets the value as a string list
     *
     * @return the string list
     */
    public List<String> getValueAsStringList() {
        List<String> result = new ArrayList<String>();
        for (int i = 0; i < size(); i++) {
            result.add(getValueAsString(i));
        }

        return result;
    }

    
    /**
     * Gets the first value as character.
     *
     * @return the value
     */
    public Character getValueAsCharacter() {
        return getValueAsCharacter(0);
    }


    /**
     * Gets the value as character.
     *
     * @param idx the index
     * @return the value
     */
    public Character getValueAsCharacter(int idx) {
        String v = getValueAsString(idx);
        if (v == null) {
            return null;
        }

        try {
            return Character.valueOf(v.trim().charAt(0)).charValue();
        } catch (Exception e) {
            // NOP
        }

        return null;
    }


    /**
     * Gets the value as a character list
     *
     * @return the character list
     */
    public List<Character> getValueAsCharacterList() {
        List<Character> result = new ArrayList<Character>();
        for (int i = 0; i < size(); i++) {
            result.add(getValueAsCharacter(i));
        }

        return result;
    }

    
    /**
     * Gets the first value as boolean.
     *
     * @return the value
     */
    public Boolean getValueAsBoolean() {
        return getValueAsBoolean(0);
    }

    
    /**
     * Gets the value as boolean.
     *
     * @param idx the index
     * @return the value
     */
    public Boolean getValueAsBoolean(int idx) {
        String v = getValueAsString(idx);
        if (v == null) {
            return null;
        }

        try {
            return Boolean.valueOf(v.trim()).booleanValue();
        } catch (Exception e) {
            // NOP
        }

        return null;
    }

    
    /**
     * Gets the value as a boolean list
     *
     * @return the boolean list
     */
    public List<Boolean> getValueAsBooleanList() {
        List<Boolean> result = new ArrayList<Boolean>();
        for (int i = 0; i < size(); i++) {
            result.add(getValueAsBoolean(i));
        }

        return result;
    }

    
    /**
     * Gets the first value as short.
     *
     * @return the value
     */
    public Short getValueAsShort() {
        return getValueAsShort(0);
    }

    
    /**
     * Gets the value as short.
     *
     * @param idx the index
     * @return the value
     */
    public Short getValueAsShort(int idx) {
        String v = getValueAsString(idx);
        if (v == null) {
            return null;
        }

        try {
            return Short.valueOf(v.trim()).shortValue();
        } catch (Exception e) {
            // NOP
        }

        return null;
    }

    
    /**
     * Gets the value as a short list
     *
     * @return the short list
     */
    public List<Short> getValueAsShortList() {
        List<Short> result = new ArrayList<Short>();
        for (int i = 0; i < size(); i++) {
            result.add(getValueAsShort(i));
        }

        return result;
    }

    
    /**
     * Gets the first value as integer.
     *
     * @return the value
     */
    public Integer getValueAsInteger() {
        return getValueAsInteger(0);
    }

    
    /**
     * Gets the value as integer.
     *
     * @param idx the index
     * @return the value
     */
    public Integer getValueAsInteger(int idx) {
        String v = getValueAsString(idx);
        if (v == null) {
            return null;
        }

        try {
            return Integer.valueOf(v.trim()).intValue();
        } catch (Exception e) {
            // NOP
        }

        return null;
    }

    
    /**
     * Gets the value as a integer list
     *
     * @return the integer list
     */
    public List<Integer> getValueAsIntegerList() {
        List<Integer> result = new ArrayList<Integer>();
        for (int i = 0; i < size(); i++) {
            result.add(getValueAsInteger(i));
        }

        return result;
    }

    
    /**
     * Gets the first value as long.
     *
     * @return the value
     */
    public Long getValueAsLong() {
        return getValueAsLong(0);
    }

    
    /**
     * Gets the value as long.
     *
     * @param idx the index
     * @return the value
     */
    public Long getValueAsLong(int idx) {
        String v = getValueAsString(idx);
        if (v == null) {
            return null;
        }

        try {
            return Long.valueOf(v.trim()).longValue();
        } catch (Exception e) {
            // NOP
        }

        return null;
    }

    
    /**
     * Gets the value as a long list
     *
     * @return the long list
     */
    public List<Long> getValueAsLongList() {
        List<Long> result = new ArrayList<Long>();
        for (int i = 0; i < size(); i++) {
            result.add(getValueAsLong(i));
        }

        return result;
    }

    
    /**
     * Gets the first value as float.
     *
     * @return the value
     */
    public Float getValueAsFloat() {
        return getValueAsFloat(0);
    }

    
    /**
     * Gets the value as float.
     *
     * @param idx the index
     * @return the value
     */
    public Float getValueAsFloat(int idx) {
        String v = getValueAsString(idx);
        if (v == null) {
            return null;
        }

        try {
            return Float.valueOf(v.trim()).floatValue();
        } catch (Exception e) {
            // NOP
        }

        return null;
    }

    
    /**
     * Gets the value as a float list
     *
     * @return the float list
     */
    public List<Float> getValueAsFloatList() {
        List<Float> result = new ArrayList<Float>();
        for (int i = 0; i < size(); i++) {
            result.add(getValueAsFloat(i));
        }

        return result;
    }

    
    /**
     * Gets the first value as double.
     *
     * @return the value
     */
    public Double getValueAsDouble() {
        return getValueAsDouble(0);
    }


    /**
     * Gets the value as double.
     *
     * @param idx the index
     * @return the value
     */
    public Double getValueAsDouble(int idx) {
        String v = getValueAsString(idx);
        if (v == null) {
            return null;
        }

        try {
            return Double.valueOf(v.trim()).doubleValue();
        } catch (Exception e) {
            // NOP
        }

        return null;
    }

    
    /**
     * Gets the value as a double list
     *
     * @return the double list
     */
    public List<Double> getValueAsDoubleList() {
        List<Double> result = new ArrayList<Double>();
        for (int i = 0; i < size(); i++) {
            result.add(getValueAsDouble(i));
        }

        return result;
    }

    
    /**
     * Gets the first value as date.
     *
     * @return the value
     */
    public Date getValueAsDate() {
        return getValueAsDate(0);
    }

    
    /**
     * Gets the value as date.
     *
     * @param idx the index
     * @return the value
     */
    public Date getValueAsDate(int idx) {
        String v = getValueAsString(idx);
        if (v == null) {
            return null;
        }

        try {
            return DateUtil.getInstance().parseDate(v, " ");
        } catch (Exception e) {
            // NOP
        }

        return null;
    }

    
    /**
     * Gets the value as a date list
     *
     * @return the date list
     */
    public List<Date> getValueAsDateList() {
        List<Date> result = new ArrayList<Date>();
        for (int i = 0; i < size(); i++) {
            result.add(getValueAsDate(i));
        }

        return result;
    }

    
    /**
     * Gets the first value as regular expression
     *
     * @return the value
     */
    public Pattern getValueAsRegularExpression() {
        return getValueAsRegularExpression(0);
    }

    
    /**
     * Gets the value as regular expression.
     *
     * @param idx the index
     * @return the value
     */
    public Pattern getValueAsRegularExpression(int idx) {
        String v = getValueAsString(idx);
        if (v == null) {
            return null;
        }

        return Pattern.compile(v);
    }

    
    /**
     * Gets the value as a regular expression list
     *
     * @return the regular expression list
     */
    public List<Pattern> getValueAsRegularExpressionList() {
        List<Pattern> result = new ArrayList<Pattern>();
        for (int i = 0; i < size(); i++) {
            result.add(getValueAsRegularExpression(i));
        }

        return result;
    }

    
    /**
     * Check if the value list ist empty
     *
     * @return true if it is empty
     */
    public boolean isEmpty() {
        if (valueList == null) {
            return true;
        }

        return valueList.isEmpty();
    }

    
    /**
     * Gets the size of the value list
     *
     * @return the size
     */
    public int size() {
        if (valueList == null) {
            return 0;
        }

        return valueList.size();
    }

    
    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hash(valueList);
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
        
        ParameterValue other = (ParameterValue) obj;
        return Objects.equals(valueList, other.valueList);
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ParameterValueList [valueList=" + valueList + "]";
    }
}
