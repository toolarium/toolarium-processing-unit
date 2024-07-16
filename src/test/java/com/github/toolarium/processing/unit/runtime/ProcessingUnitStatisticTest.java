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
        assertNull(processingUnitStatistic.get("a"));
        assertNull(processingUnitStatistic.get("c"));
        assertNull(processingUnitStatistic.get("d"));
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
    public void testProcessingUnitStatisticAdd() {
        ProcessingUnitStatistic processingUnitStatistic = new ProcessingUnitStatistic();
        assertNull(processingUnitStatistic.get("e"));
        processingUnitStatistic.add("e", 4L);
        assertEquals(4.0, processingUnitStatistic.get("e").getSum());
        processingUnitStatistic.add("e", 2L);
        assertEquals(6, processingUnitStatistic.get("e").getSum());
    }


    /**
     * Test ProcessingUnitStatus
     */
    @Test
    public void testProcessingUnitStatus() {
        ProcessingUnitStatistic processingUnitStatistic = new ProcessingUnitStatistic();
        assertNull(processingUnitStatistic.get("f"));
        processingUnitStatistic.add("f", 4L);
        assertEquals(4.0, processingUnitStatistic.get("f").getSum());
        processingUnitStatistic.add("f", 2L);
        assertEquals(6, processingUnitStatistic.get("f").getSum());
    }
}
