/*
 * ProcessingUnitStatisticTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.runtime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.github.toolarium.common.statistic.StatisticCounter;
import org.junit.jupiter.api.Test;


/**
 * Tes the processing unit statistic
 *  
 * @author patrick
 */
public class ProcessingUnitStatisticTest {
    
   
    /**
     * Test testProcessingUnitStatistic
     */
    @Test
    public void testProcessingUnitStatistic() {
        ProcessingUnitStatistic processingUnitStatistic = new ProcessingUnitStatistic();
        processingUnitStatistic.add("a", new StatisticCounter());
        processingUnitStatistic.add("c", new StatisticCounter());
        processingUnitStatistic.get("a").add(2);
        processingUnitStatistic.get("a").add(4);
        processingUnitStatistic.get("c").add(3);
        processingUnitStatistic.get("c").add(8);
        assertNull(processingUnitStatistic.get("d"));
        assertEquals(6.0, processingUnitStatistic.get("a").getSum());
        assertEquals(11.0, processingUnitStatistic.get("c").getSum());
    }
    
    
    
    /**
     * Test ProcessingUnitStatus
     */
    @Test
    public void testProcessingUnitStatus() {
        ProcessingUnitStatus status = new ProcessingUnitStatus();
        status.addStatistic("e", 4L);
        assertEquals(4.0, status.getStatisticCounter("e").getSum());
        status.addStatistic("e", 2L);
        assertEquals(6L, status.getStatisticCounter("e").getSum());
    }
}
