/*
 * ParameterRuntime.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.runtime;

import com.github.toolarium.processing.unit.IProcessingUnitContext;
import com.github.toolarium.processing.unit.base.IParameterRuntime;
import com.github.toolarium.processing.unit.dto.Parameter;
import com.github.toolarium.processing.unit.dto.ParameterDefinition;
import com.github.toolarium.processing.unit.dto.ParameterValue;
import com.github.toolarium.processing.unit.dto.ParameterValueType;
import com.github.toolarium.processing.unit.exception.ProcessingException;
import com.github.toolarium.processing.unit.exception.ValidationException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;


/**
 * Implements the {@link IParameterRuntime}.
 *  
 * @author patrick
 */
public class ParameterRuntime implements IParameterRuntime, Serializable {
    private static final long serialVersionUID = -1601481726023356519L;
    private Map<String, ParameterDefinition> parameterDefinitionMap;
    private Map<String, ParameterDefinition> mandatoryParameterDefinitionMap;
    private Map<String, Parameter> parameterMapping;

    
    /**
     * Constructor for ParameterRuntime
     */
    public ParameterRuntime() {
        parameterDefinitionMap = new LinkedHashMap<String, ParameterDefinition>();
        mandatoryParameterDefinitionMap = new LinkedHashMap<String, ParameterDefinition>();
        parameterMapping = new LinkedHashMap<String, Parameter>();
    }

    
    /**
     * @see com.github.toolarium.processing.unit.base.IParameterRuntime#getParameterDefinition()
     */
    @Override
    public List<ParameterDefinition> getParameterDefinition() {
        return new ArrayList<ParameterDefinition>(parameterDefinitionMap.values());
    }

    
    /**
     * Gets the parameter definition of a specific key
     *
     * @param key the key
     * @return the parameter definition
     */
    protected ParameterDefinition getParameterDefinition(String key) {
        return parameterDefinitionMap.get(key);
    }

    
    /**
     * @see com.github.toolarium.processing.unit.base.IParameterRuntime#addParameterDefinition(com.github.toolarium.processing.unit.dto.ParameterDefinition)
     */
    @Override
    public void addParameterDefinition(ParameterDefinition parameterDefinition) {
        if (parameterDefinition != null) {
            parameterDefinitionMap.put(parameterDefinition.getKey(), parameterDefinition);

            if (!parameterDefinition.isOptional()) {
                mandatoryParameterDefinitionMap.put(parameterDefinition.getKey(), parameterDefinition);
            }
        }
    }


    /**
     * @see com.github.toolarium.processing.unit.base.IParameterRuntime#existParameter(com.github.toolarium.processing.unit.dto.ParameterDefinition)
     */
    @Override
    public boolean existParameter(ParameterDefinition parameterDefinition) {
        if (parameterDefinition == null) {
            return false;
        }

        return existParameter(parameterDefinition.getKey());
    }

    
    /**
     * Check if the parameter exists or not
     *
     * @param key the key
     * @return true or false
     */
    protected boolean existParameter(String key) {
        if (key == null) {
            return false;
        }

        return parameterMapping.containsKey(key);
    }
 

    /**
     * @see com.github.toolarium.processing.unit.base.IParameterRuntime#setParameterList(java.util.List, com.github.toolarium.processing.unit.IProcessingUnitContext)
     */
    @Override
    public void setParameterList(List<Parameter> parameterList, IProcessingUnitContext processingContext) throws ValidationException, ProcessingException {
        // set mandatory fields which has a default value
        List<ParameterDefinition> parameterDefinitionList = getParameterDefinition();
        if (parameterDefinitionList != null && !parameterDefinitionList.isEmpty()) {
            // check all parameters with its definition
            for (ParameterDefinition paramDefinition : parameterDefinitionList) {
                if (paramDefinition != null && !paramDefinition.isOptional() && paramDefinition.getDefaultValue() != ParameterDefinition.NO_DEFAULT_PARAMETER) {
                    Parameter p = createParameter(paramDefinition);
                    parameterMapping.put(p.getKey(), p);
                }
            }
        }

        // update parameter mapping
        if (parameterList != null) {
            for (Parameter p : parameterList) {
                parameterMapping.put(p.getKey(), p);
            }
        }

        // verify consistency
        validateParameterList(parameterList);
    }

    
    /**
     * @see com.github.toolarium.processing.unit.base.IParameterRuntime#validateParameterList(java.util.List)
     */
    @Override
    public void validateParameterList(List<Parameter> parameterList) throws ValidationException {
        String message = "";

        if (parameterList != null && !parameterList.isEmpty()) {
            Set<String> mandatoryParameters = new TreeSet<String>(mandatoryParameterDefinitionMap.keySet());
            int parameterCounterIdx = 0;

            // check all parameters with its definition
            for (Parameter param : parameterList) {
                parameterCounterIdx++;
                message += verifyParameter(mandatoryParameters, param, parameterCounterIdx);
            }

            if (!mandatoryParameters.isEmpty()) {
                message += "\n-missing mandatory parameter(s): " + mandatoryParameters + "! ";
            }
        } else if (!mandatoryParameterDefinitionMap.isEmpty()) {
            message += "\n-missing mandatory parameter(s): " + mandatoryParameterDefinitionMap.keySet() + "! ";
        }

        if (!message.trim().isEmpty()) {
            throw new ValidationException("The parameters are inconsistent: " + message);
        }
    }


    /**
     * @see com.github.toolarium.processing.unit.base.IParameterRuntime#getParameterValueList(com.github.toolarium.processing.unit.dto.ParameterDefinition)
     */
    @Override
    public ParameterValue getParameterValueList(ParameterDefinition parameterDefinition) {
        Parameter parameter = getParameter(parameterDefinition);
        if (parameter != null) {
            return parameter.getParameterValue();
        }

        if (parameterDefinition.getDefaultValue() == null) {
            return new ParameterValue();
        }

        return createParameter(parameterDefinition).getParameterValue();
    }


    /**
     * @see com.github.toolarium.processing.unit.base.IParameterRuntime#getParameterValueList(java.util.List, com.github.toolarium.processing.unit.dto.ParameterDefinition)
     */
    @Override
    public ParameterValue getParameterValueList(List<Parameter> parameterList, ParameterDefinition parameterDefinition) {
        if (parameterList != null) {
            for (Parameter p : parameterList) {
                if (parameterDefinition.getKey().equals(p.getKey())) {
                    ParameterValue parameterValue = p.getParameterValue();
                    if (parameterValue != null) {
                        return parameterValue;
                    }
                }
            }
        }

        if (parameterDefinition.getDefaultValue() == null) {
            return new ParameterValue();
        }

        return createParameter(parameterDefinition).getParameterValue();
    }

    
    /**
     * Gets the parameter
     *
     * @param parameterDefinition the parameter definition
     * @return the parameter or null
     */
    protected Parameter getParameter(ParameterDefinition parameterDefinition) {
        if (parameterDefinition == null) {
            return null;
        }

        return getParameter(parameterDefinition.getKey());
    }

    
    /**
     * Gets the parameter
     *
     * @param key the key
     * @return the parameter or null
     */
    protected Parameter getParameter(String key) {
        if (key == null) {
            return null;
        }

        return parameterMapping.get(key);
    }
    
    
    
    /**
     * Verify parameter
     *
     * @param mandatoryParameters the mandatory parameters
     * @param parameter the parameter to check
     * @param parameterCounterIdx the parameter counter index
     * @return the message
     */
    protected String verifyParameter(Set<String> mandatoryParameters, Parameter parameter, int parameterCounterIdx) {
        if (parameter == null) {
            return "\n-null parameter is not allowed (parameters position " + parameterCounterIdx + ")!";
        }

        String key = parameter.getKey();
        if (key == null || key.trim().isEmpty()) {
            return "\n-empty parameter is not allowed (parameters position " + parameterCounterIdx + ")!";
        }

        ParameterDefinition def = getParameterDefinition(key);
        if (def == null) {
            return "\n-no parameter definition found for key: " + key + "!";
        }

        // non optional parameter found
        if (!def.isOptional()) {
            mandatoryParameters.remove(def.getKey());
        }

        if (!def.getKey().equals(key)) {
            return "\n-incompatible key: " + key + " != " + def.getKey() + "!";
        }

        String message = "";
        ParameterValue parameterValue = parameter.getParameterValue();
        if (parameterValue == null || parameterValue.isEmpty()) {
            if (def.getMinOccurs() > 0) {
                message = "\n-min occurs of " + key + " expected " + def.getMinOccurs() + "(not empty)!";
            }
        } else {
            if (parameterValue.size() < def.getMinOccurs()) {
                message = "\n-min occurs of " + key + " expected " + def.getMinOccurs() + "!";
            }

            if (parameterValue.size() > def.getMaxOccurs()) {
                message = "\n-max occurs of " + key + " expected " + def.getMaxOccurs() + ", current size: " + parameterValue.size() + "!";
            }
        }

        try {
            if (ParameterValueType.BOOLEAN.equals(def.getValueDataType())) {
                parameter.getParameterValue().getValueAsBooleanList();
            }
            
            if (ParameterValueType.CHAR.equals(def.getValueDataType())) {
                parameter.getParameterValue().getValueAsCharacterList();
            }
            
            if (ParameterValueType.STRING.equals(def.getValueDataType())) {
                parameter.getParameterValue().getValueAsStringList();
            }
            
            if (ParameterValueType.SHORT.equals(def.getValueDataType())) {
                parameter.getParameterValue().getValueAsShortList();
            }
            
            if (ParameterValueType.INTEGER.equals(def.getValueDataType())) {
                parameter.getParameterValue().getValueAsIntegerList();
            }
            
            if (ParameterValueType.LONG.equals(def.getValueDataType())) {
                parameter.getParameterValue().getValueAsLongList();
            }
            
            if (ParameterValueType.FLOAT.equals(def.getValueDataType())) {
                parameter.getParameterValue().getValueAsFloatList();
            }
            
            if (ParameterValueType.DOUBLE.equals(def.getValueDataType())) {
                parameter.getParameterValue().getValueAsDoubleList();
            }
            
            if (ParameterValueType.DATE.equals(def.getValueDataType()) || ParameterValueType.TIME.equals(def.getValueDataType()) || ParameterValueType.DATETIME.equals(def.getValueDataType())) {
                parameter.getParameterValue().getValueAsDateList();
            }
            
            if (ParameterValueType.REGEXP.equals(def.getValueDataType())) {
                parameter.getParameterValue().getValueAsRegularExpressionList();
            }
        } catch (Exception e) {
            message += "\n-invalid data type in value list of key " + key + ": " + parameter + "!";
        }

        return message;
    }

        
    /**
     * Create parameter
     *
     * @param paramDefinition the parameter definition
     * @return the parameter
     */
    protected Parameter createParameter(ParameterDefinition paramDefinition) {
        assert (paramDefinition != null) : "ParameterDefinition must not be null";

        Parameter p;
        if (paramDefinition.getDefaultValue() != ParameterDefinition.NO_DEFAULT_PARAMETER) {
            if (paramDefinition.getDefaultValue().getClass().isArray()) {
                // default parameter has multiple values
                Object[] defaultArrayValues = (Object[])paramDefinition.getDefaultValue();

                String[] parameterValueList = new String[defaultArrayValues.length];
                for (int i = 0; i < defaultArrayValues.length; i++) {
                    parameterValueList[i] = convertToString(defaultArrayValues[i]);
                }

                p = new Parameter(paramDefinition.getKey(), parameterValueList);
            } else {
                String value = convertToString(paramDefinition.getDefaultValue());
                p = new Parameter(paramDefinition.getKey(), value);
            }
        } else {
            p = new Parameter(paramDefinition.getKey());
        }

        return p;
    }


    /**
     * Convert the default value into a string value
     *
     * @param defaultValue the default value
     * @return the default value as string
     */
    protected String convertToString(Object defaultValue) {
        String value = null;
        if (defaultValue instanceof Boolean) {
            value = String.valueOf(((Boolean) defaultValue).booleanValue());
        } else if (defaultValue instanceof Character) {
            value = String.valueOf(((Character) defaultValue).charValue());
        } else if (defaultValue instanceof Short) {
            value = String.valueOf(((Short) defaultValue).shortValue());
        } else if (defaultValue instanceof Integer) {
            value = String.valueOf(((Integer) defaultValue).intValue());
        } else if (defaultValue instanceof Long) {
            value = String.valueOf(((Long) defaultValue).longValue());
        } else if (defaultValue instanceof Float) {
            value = String.valueOf(((Float) defaultValue).floatValue());
        } else if (defaultValue instanceof Double) {
            value = String.valueOf(((Double) defaultValue).doubleValue());
        } else {
            value = String.valueOf(defaultValue);
        }

        return value;
    }
}
