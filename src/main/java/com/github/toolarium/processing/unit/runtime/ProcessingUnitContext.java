/*
 * ProcessingUnitContext.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.runtime;

import com.github.toolarium.processing.unit.IProcessingUnitContext;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Implements the {@link IProcessingUnitContext}.
 * 
 * @author patrick
 */
public class ProcessingUnitContext implements IProcessingUnitContext, Serializable {
    private static final long serialVersionUID = 2939141852396108266L;
    private Map<String, String> context;

    
    /**
     * Constructor for ProcessingUnitContext
     */
    public ProcessingUnitContext() {
        this.context = new ConcurrentHashMap<String, String>();
    }

    
    /**
     * Constructor for ProcessingUnitContext
     * 
     * @param context the context to initialize
     */
    public ProcessingUnitContext(Map<String, String> context) {
        this.context = new ConcurrentHashMap<String, String>(context);
    }


    /**
     * Constructor for ProcessingUnitContext
     * 
     * @param context the context to initialize
     */
    public ProcessingUnitContext(IProcessingUnitContext context) {
        this();
        
        for (String key : context.keySet()) {
            set(key, context.get(key));
        }
    }

    
    /**
     * @see com.github.toolarium.processing.unit.IProcessingUnitContext#get(java.lang.String)
     */
    @Override
    public String get(String key) {
        return context.get(key);
    }

    
    /**
     * @see com.github.toolarium.processing.unit.IProcessingUnitContext#set(java.lang.String, java.lang.String)
     */
    @Override
    public String set(String key, String value) {
        return context.put(key, value);
    }
    

    /**
     * @see com.github.toolarium.processing.unit.IProcessingUnitContext#remove(java.lang.String)
     */
    @Override
    public String remove(String key) {
        return context.remove(key);
    }

    
    /**
     * @see com.github.toolarium.processing.unit.IProcessingUnitContext#keySet()
     */
    @Override
    public Set<String> keySet() {
        return context.keySet();
    }

    
    /**
     * @see com.github.toolarium.processing.unit.IProcessingUnitContext#clear()
     */
    @Override
    public void clear() {
        context.clear();
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hash(context);
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
        
        ProcessingUnitContext other = (ProcessingUnitContext) obj;
        return Objects.equals(context, other.context);
    }

    
    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ProcessingUnitContext [context=" + context + "]";
    }
}
