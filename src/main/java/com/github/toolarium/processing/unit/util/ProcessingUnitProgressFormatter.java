/*
 * ProcessUnitProgressFormatter.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.util;

import com.github.toolarium.common.bandwidth.IBandwidthThrottling;
import com.github.toolarium.common.formatter.TimeDifferenceFormatter;
import com.github.toolarium.common.util.RoundUtil;
import com.github.toolarium.common.util.TextUtil;
import com.github.toolarium.processing.unit.IProcessingUnitContext;
import com.github.toolarium.processing.unit.IProcessingUnitPersistence;
import com.github.toolarium.processing.unit.IProcessingUnitProgress;
import com.github.toolarium.processing.unit.IProcessingUnitStatistic;
import com.github.toolarium.processing.unit.dto.Parameter;
import com.github.toolarium.processing.unit.dto.ProcessingActionStatus;
import com.github.toolarium.processing.unit.dto.ProcessingRuntimeStatus;
import com.github.toolarium.processing.unit.runtime.IProcessingUnitRuntimeTimeMeasurement;
import java.util.List;


/**
 * Defines a processing unit progress formatter.
 * 
 * @author patrick
 */
public class ProcessingUnitProgressFormatter {
    private static ThreadLocal<TimeDifferenceFormatter> timeDifferenceFormatter = ThreadLocal.withInitial(() -> new TimeDifferenceFormatter());    
    private final String startTag; 
    

    /**
     * Constructor for ProcessUnitProgressFormatter
     * 
     * @param startTag the start tag
     *
     */
    public ProcessingUnitProgressFormatter(String startTag) {
        this.startTag = TextUtil.NL + startTag;
    }

    
    /**
     * Format process unit progress
     *
     * @param id the id
     * @param name the name
     * @param processingUnitClass the processing unit class
     * @param parameters the parameters
     * @param processingUnitContext the processing unit context
     * @param processingProgress the processing unit progress
     * @param processingActionStatus the action status
     * @param processingRuntimeStatus the runtime status
     * @param messages the messages
     * @param timeMeasurement the time measurement
     * @param processingUnitThrottling the processing unit throttling
     * @param processingPersistence the processing persistence
     * @return the formatted message
     */
    public String toString(String id, // CHECKSTYLE IGNORE THIS LINE
                           String name, 
                           String processingUnitClass,
                           List<Parameter> parameters,
                           IProcessingUnitContext processingUnitContext,
                           IProcessingUnitProgress processingProgress, 
                           ProcessingActionStatus processingActionStatus,
                           ProcessingRuntimeStatus processingRuntimeStatus,
                           List<String> messages,
                           IProcessingUnitRuntimeTimeMeasurement timeMeasurement, 
                           IBandwidthThrottling processingUnitThrottling,
                           IProcessingUnitPersistence processingPersistence) {
        StringBuilder builder = new StringBuilder();
        builder.append(ProcessingUnitUtil.getInstance().toString(id, name, processingUnitClass));
        if (processingActionStatus != null) {
            builder.append(" Status ").append(processingActionStatus);
        }

        if (processingProgress != null) {
            builder.append(": ").append(processingProgress.getProgress()).append("%");
        }
        
        builder.append(prepareProgressNumbers(startTag, processingProgress, true));
        if (processingProgress != null && processingRuntimeStatus != null) {
            builder.append(" -> ").append(processingRuntimeStatus);
        }
        
        builder.append(prepareParameters(startTag, parameters));
        builder.append(prepareProcessingContext(startTag, processingUnitContext));
        builder.append(prepareTimeMeasurement(startTag, timeMeasurement));
        builder.append(prepareMessages(startTag, messages));
        
        if (processingProgress != null && processingProgress.getProcessingUnitStatistic() != null) {
            builder.append(prepareStatistic(startTag, processingProgress.getProcessingUnitStatistic()));
        }
        
        builder.append(prepareBandwidthThrottling(startTag, processingUnitThrottling));
        builder.append(prepareProcesingPersistenceContainer(startTag, processingPersistence));
        return builder.toString();
    }    
    
    
    /**
     * Prepare process unit runtime time measurement
     *
     * @param header the message header
     * @param processingProgress the processing progress
     * @param includeUnprocessed true to include unprocessed units
     * @return the prepared string
     */
    public StringBuilder prepareProgressNumbers(String header, IProcessingUnitProgress processingProgress, boolean includeUnprocessed) {
        StringBuilder builder = new StringBuilder();
        if (processingProgress == null) {
            return builder;
        }

        builder.append(header);
        builder.append("Processed units: ").append(processingProgress.getNumberOfProcessedUnits())
               .append(" (successful: ").append(processingProgress.getNumberOfSuccessfulUnits())
               .append(", failed: ").append(processingProgress.getNumberOfFailedUnits());
 
        if (includeUnprocessed) {
            builder.append(", unprocessed: ").append(processingProgress.getNumberOfUnprocessedUnits());
        }
        
        builder.append(")");
        return builder;
    }

    
    /**
     * Prepare parameters
     *
     * @param header the message header
     * @param parameters the parameters
     * @return the prepared string
     */
    public StringBuilder prepareParameters(String header, List<Parameter> parameters) {
        StringBuilder builder = new StringBuilder();
        if (parameters == null || parameters.isEmpty()) {
            return builder;
        }

        builder.append(header);
        builder.append("Parameters: ");
        
        builder.append("[");
        boolean addSeparator = false;
        for (Parameter parameter : parameters) {
            if (addSeparator) {
                builder.append(", ");
            } else {
                addSeparator = true;
            }

            builder.append(parameter.getKey());
            builder.append("=");
            if (parameter.getParameterValue() != null) {
                builder.append(parameter.getParameterValue().getValueAsStringList());
            }
        }
        
        builder.append("]");
        return builder;
    }

    
    /**
     * Prepare process context
     *
     * @param header the message header
     * @param processingUnitContext the processing context
     * @return the prepared string
     */
    public StringBuilder prepareProcessingContext(String header, IProcessingUnitContext processingUnitContext) {
        StringBuilder builder = new StringBuilder();
        if (processingUnitContext == null || processingUnitContext.isEmpty()) {
            return builder;
        }

        builder.append(header);
        builder.append("Context: ");
        builder.append("[");
        boolean addSeparator = false;
        for (String key : processingUnitContext.keySet()) {
            if (addSeparator) {
                builder.append(", ");
            } else {
                addSeparator = true;
            }

            builder.append(key);
            builder.append("=");
            builder.append(processingUnitContext.get(key));
        }
        
        builder.append("]");
        return builder;
    }

    
    /**
     * Prepare process unit runtime time measurement
     *
     * @param header the message header
     * @param timeMeasurement the runtime time measurement
     * @return the prepared string
     */
    public StringBuilder prepareTimeMeasurement(String header, IProcessingUnitRuntimeTimeMeasurement timeMeasurement) {
        StringBuilder builder = new StringBuilder();
        if (timeMeasurement == null) {
            return builder;
        }
        
        boolean hasEnded = timeMeasurement.getStopTimestamp() != null;
        builder.append(header);
        if (hasEnded) {
            builder.append("Total duration ");
        } else {
            builder.append("Current duration ");
        }
        
        builder.append(timeMeasurement.getDurationAsString());
        if (timeMeasurement.getStartTimestamp() != null) {
            builder.append(" (started: ").append(timeMeasurement.getStartTimestamp());
        } else {
            builder.append(" (not yet started)");
        }
        
        if (hasEnded) {
            builder.append(", ended: ").append(timeMeasurement.getStopTimestamp());
        }
        
        builder.append(")");
        return builder;
    }

    
    /**
     * Prepare process unit messages
     *
     * @param header the message header
     * @param messages the processing unit messages
     * @return the prepared string
     */
    public StringBuilder prepareMessages(String header, List<String> messages) {
        //boolean hasNotEnded = timeMeasurement.getStopTimestamp() == null;
        StringBuilder builder = new StringBuilder();
        if (messages == null || messages.isEmpty()) {
            return builder;
        }
        
        builder.append(header);
        builder.append("Messages: ").append(messages);
        return builder;
    }

    
    /**
     * Prepare process unit statistic
     *
     * @param header the message header
     * @param processingStatistic the processing unit statistic
     * @return the prepared string
     */
    public StringBuilder prepareStatistic(String header, IProcessingUnitStatistic processingStatistic) {
        StringBuilder builder = new StringBuilder();
        if (processingStatistic == null || processingStatistic.isEmpty()) {
            return builder;
        }

        builder.append(header);
        builder.append("Statistic: ");
        
        builder.append("[");
        boolean addSeparator = false;
        for (String key : processingStatistic.keySet()) {
            if (addSeparator) {
                builder.append(", ");
            } else {
                addSeparator = true;
            }

            builder.append(key);
            builder.append(" = ");
            builder.append("avg:");
            builder.append(RoundUtil.getInstance().round(processingStatistic.get(key).getAverage(), 2));
            builder.append(", num:");
            builder.append(RoundUtil.getInstance().round(processingStatistic.get(key).getCounter(), 2));
            builder.append(", sum:");
            builder.append(RoundUtil.getInstance().round(processingStatistic.get(key).getSum(), 2));
        }
        
        builder.append("]");
        
        return builder;
    }

    
    /**
     * Prepare process unit bandwidth throttling
     *
     * @param header the message header
     * @param bandwidthThrottling the bandwidth throttling
     * @return the prepared string
     */
    public StringBuilder prepareBandwidthThrottling(String header, IBandwidthThrottling bandwidthThrottling) {
        StringBuilder builder = new StringBuilder();
        if (bandwidthThrottling == null) {
            return builder;
        }

        builder.append(header)
            .append("Throttling: av. ")
            .append(timeDifferenceFormatter.get().formatAsString(bandwidthThrottling.getBandwidthStatisticCounter().getAverage()))
            .append(", sd. ")
            .append(RoundUtil.getInstance().round(bandwidthThrottling.getBandwidthStatisticCounter().getStandardDeviation(), 2))
            .append(", no. ")
            .append(bandwidthThrottling.getBandwidthStatisticCounter().getCounter());
        return builder;
    }

    
    /**
     * Prepare process unit persistence
     *
     * @param header the message header
     * @param processingPersistence the processing persistence
     * @return the prepared string
     */
    public StringBuilder prepareProcesingPersistenceContainer(String header, IProcessingUnitPersistence processingPersistence) {
        StringBuilder builder = new StringBuilder();
        if (processingPersistence == null) {
            return builder;
        }

        builder.append(header)
            .append("Persistence: ")
            .append(processingPersistence);
        return builder;
    }


    /**
     * Get the start tag
     *
     * @return the start tag
     */
    public String getStartTag() {
        return startTag;
    }
}
