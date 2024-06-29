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
import com.github.toolarium.processing.unit.IProcessingProgress;
import com.github.toolarium.processing.unit.IProcessingStatistic;
import com.github.toolarium.processing.unit.IProcessingUnit;
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
    private String processUnitIdentification;
    private TimeDifferenceFormatter formatter = new TimeDifferenceFormatter();
    private String startTag; 
    
    
    /**
     * Constructor for ProcessUnitProgressFormatter
     *
     * @param id the id
     * @param name the name
     * @param processingUnitClass the processing unit class
     */
    public ProcessingUnitProgressFormatter(String id, String name, Class<? extends IProcessingUnit> processingUnitClass) {
        this.processUnitIdentification = ProcessingUnitUtil.getInstance().prepare(id, name, processingUnitClass);
        setStartTag(" - ");
    }
    

    /**
     * Format process unit progress
     *
     * @param startTag the start tag
     */
    public void setStartTag(String startTag) {
        this.startTag = TextUtil.NL + startTag;
    }

    
    /**
     * Format process unit progress
     *
     * @param processingProgress the progress
     * @param processingActionStatus the action status
     * @param processingRuntimeStatus the runtime status
     * @param messages the messages
     * @param timeMeasurement the time measurement
     * @param processingUnitThrottling the processing unit throttling
     * @return the formatted message
     */
    public String formatProgress(IProcessingProgress processingProgress, 
                                 ProcessingActionStatus processingActionStatus,
                                 ProcessingRuntimeStatus processingRuntimeStatus,
                                 List<String> messages,
                                 IProcessingUnitRuntimeTimeMeasurement timeMeasurement, 
                                 IBandwidthThrottling processingUnitThrottling) {
        StringBuilder builder = new StringBuilder();
        builder.append(processUnitIdentification).append(": ").append(processingActionStatus);
        builder.append(prepareProgressNumbers(startTag, processingProgress, true));
        if (processingProgress != null) {
            builder.append(" -> ").append(processingRuntimeStatus);
        }

        builder.append(prepareTimeMeasurement(startTag, timeMeasurement));
        builder.append(prepareMessages(startTag, messages));
        builder.append(prepareStatistic(startTag, processingProgress.getProcesingStatistic()));
        builder.append(prepareBandwidthThrottling(startTag, processingUnitThrottling));
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
    public StringBuilder prepareProgressNumbers(String header, IProcessingProgress processingProgress, boolean includeUnprocessed) {
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
        
        builder.append(timeMeasurement.getDurationAsString()).append(" (started: ").append(timeMeasurement.getStartTimestamp());
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
    public StringBuilder prepareStatistic(String header, IProcessingStatistic processingStatistic) {
        StringBuilder builder = new StringBuilder();
        if (processingStatistic == null || processingStatistic.isEmpty()) {
            return builder;
        }

        builder.append(header);
        builder.append("Statistic: ").append(processingStatistic);
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
            .append(formatter.formatAsString(bandwidthThrottling.getBandwidthStatisticCounter().getAverage()))
            .append(", sd. ")
            .append(RoundUtil.getInstance().round(bandwidthThrottling.getBandwidthStatisticCounter().getStandardDeviation(), 2))
            .append(", no. ")
            .append(bandwidthThrottling.getBandwidthStatisticCounter().getCounter());
        return builder;
    }
}
