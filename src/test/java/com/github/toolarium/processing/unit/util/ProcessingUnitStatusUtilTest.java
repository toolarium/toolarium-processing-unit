/*
 * ProcessingUnitStatusUtilTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.github.toolarium.processing.unit.IProcessingUnitProgress;
import com.github.toolarium.processing.unit.IProcessingUnitStatistic;
import com.github.toolarium.processing.unit.IProcessingUnitStatus;
import com.github.toolarium.processing.unit.ProcessingUnitStatusBuilder;
import com.github.toolarium.processing.unit.dto.ProcessingRuntimeStatus;
import com.github.toolarium.processing.unit.runtime.ProcessingUnitProgress;
import com.github.toolarium.processing.unit.runtime.ProcessingUnitStatus;
import java.util.List;
import org.junit.jupiter.api.Test;


/**
 * Test the {@link ProcessingUnitStatusUtil}.
 * 
 * @author patrick
 */
public class ProcessingUnitStatusUtilTest {

    /**
     * Test successful
     */
    @Test
    public void testSuccessfulFailedUnprocessed() {
        assertProcessingUnitStatus(ProcessingUnitStatusUtil.getInstance().aggregateProcessingUnitStatus(new ProcessingUnitStatusBuilder(), 
                                       new ProcessingUnitStatus().increaseNumberOfSuccessfulUnits())
                                       .build(), 
                                   ProcessingRuntimeStatus.SUCCESSFUL, 1L, null, null, null, null);
        assertProcessingUnitStatus(ProcessingUnitStatusUtil.getInstance().aggregateProcessingUnitStatus(new ProcessingUnitStatusBuilder(), 
                                       new ProcessingUnitStatus().increaseNumberOfSuccessfulUnits().increaseNumberOfSuccessfulUnits())
                                       .build(), 
                ProcessingRuntimeStatus.SUCCESSFUL, 2L, null, null, null, null);
        assertProcessingUnitStatus(ProcessingUnitStatusUtil.getInstance().aggregateProcessingUnitStatus(new ProcessingUnitStatusBuilder(), 
                new ProcessingUnitStatus().increaseNumberOfSuccessfulUnits().increaseNumberOfFailedUnits().increaseNumberOfSuccessfulUnits())
                .build(), 
                ProcessingRuntimeStatus.SUCCESSFUL, 2L, 1L, null, null, null);
        
        assertProcessingUnitStatus(ProcessingUnitStatusUtil.getInstance().aggregateProcessingUnitStatus(new ProcessingUnitStatusBuilder(), 
                new ProcessingUnitStatus().increaseNumberOfSuccessfulUnits().increaseNumberOfFailedUnits().increaseNumberOfSuccessfulUnits().setNumberOfUnprocessedUnits(20L))
                .build(), 
                ProcessingRuntimeStatus.SUCCESSFUL, 2L, 1L, 20L, null, null);
    }

    
    /**
     * Test successful
     */
    @Test
    public void testSuccessfulFailedUnprocessedWithProgress() {
        ProcessingUnitProgress processingUnitProgress = new ProcessingUnitProgress();
        assertProcessingUnitStatus(ProcessingUnitStatusUtil.getInstance().aggregateProcessingUnitStatus(new ProcessingUnitStatusBuilder(processingUnitProgress), 
                                       new ProcessingUnitStatus().increaseNumberOfSuccessfulUnits())
                                       .build(), 
                                   ProcessingRuntimeStatus.SUCCESSFUL, 1L, null, null, null, null);
        assertProcessingUnitProgress(processingUnitProgress, ProcessingRuntimeStatus.SUCCESSFUL, 100 /* progress */, 1 /* total */, 0 /* unprocessed */, 1 /* processed */, 1 /* successful */, 0 /* failed */, List.of());
        
        assertProcessingUnitStatus(ProcessingUnitStatusUtil.getInstance().aggregateProcessingUnitStatus(new ProcessingUnitStatusBuilder(processingUnitProgress), 
                                       new ProcessingUnitStatus().increaseNumberOfSuccessfulUnits().increaseNumberOfSuccessfulUnits()).statistic("a", 6.0).statistic("b", 9.0)
                                       .build(), 
                                   ProcessingRuntimeStatus.SUCCESSFUL, 2L, null, null, null, List.of("a=6.0", "b=9.0"));
        assertProcessingUnitProgress(processingUnitProgress, ProcessingRuntimeStatus.SUCCESSFUL, 100 /* progress */, 3 /* total */, 0 /* unprocessed */, 3 /* processed */, 3 /* successful */, 0 /* failed */, List.of("a=6.0", "b=9.0"));
        
        assertProcessingUnitStatus(ProcessingUnitStatusUtil.getInstance().aggregateProcessingUnitStatus(new ProcessingUnitStatusBuilder(processingUnitProgress), 
                                       new ProcessingUnitStatus().increaseNumberOfSuccessfulUnits().increaseNumberOfFailedUnits().increaseNumberOfSuccessfulUnits()).statistic("a", 10.0).statistic("b", 11.0)
                                       .build(), 
                                   ProcessingRuntimeStatus.SUCCESSFUL, 2L, 1L, null, null, List.of("a=10.0", "b=11.0"));
        assertProcessingUnitProgress(processingUnitProgress, ProcessingRuntimeStatus.SUCCESSFUL, 100 /* progress */, 4 /* total */, 0 /* unprocessed */, 4 /* processed */, 3 /* successful */, 1 /* failed */, List.of("a=8.0", "b=10.0")); 
        
        assertProcessingUnitStatus(ProcessingUnitStatusUtil.getInstance().aggregateProcessingUnitStatus(new ProcessingUnitStatusBuilder(processingUnitProgress), 
                                      new ProcessingUnitStatus().increaseNumberOfSuccessfulUnits().increaseNumberOfFailedUnits().increaseNumberOfSuccessfulUnits().setNumberOfUnprocessedUnits(20L))
                                      .build(), 
                                   ProcessingRuntimeStatus.SUCCESSFUL, 2L, 1L, 20L, null, null);
        assertProcessingUnitProgress(processingUnitProgress, ProcessingRuntimeStatus.SUCCESSFUL, 20 /* progress */, 25 /* total */, 20 /* unprocessed */, 5 /* processed */, 3 /* successful */, 2 /* failed */, List.of("a=8.0", "b=10.0")); 
    }

    
    /**
     * Assert processing unit statis
     *
     * @param processingUnitProgress the processing unit progress
     * @param status the status
     * @param progress the progress
     * @param total the total
     * @param unprocessed number of unprocessed units
     * @param processed number of processed units
     * @param successful number of successful processed units
     * @param failed number of failed processed units
     * @param statisticData the statistic data: <code>keY=value</code>
     */
    void assertProcessingUnitProgress(IProcessingUnitProgress processingUnitProgress, ProcessingRuntimeStatus status, int progress, long total, long unprocessed, long processed, long successful, long failed, List<String> statisticData) {
        assertNotNull(processingUnitProgress);
        assertEquals(progress, processingUnitProgress.getProgress());
        assertEquals(total, processingUnitProgress.getNumberOfUnitsToProcess());
        assertEquals(unprocessed, processingUnitProgress.getNumberOfUnprocessedUnits());
        assertEquals(processed, processingUnitProgress.getNumberOfProcessedUnits());
        assertEquals(successful, processingUnitProgress.getNumberOfSuccessfulUnits());
        assertEquals(failed, processingUnitProgress.getNumberOfFailedUnits());
        assertEquals(status, processingUnitProgress.getProcessingRuntimeStatus());
        assertProcessingUnitStatistic(processingUnitProgress.getProcessingUnitStatistic(), statisticData);
    }

    
    /**
     * Assert processing unit statis
     *
     * @param processingUnitStatus the status
     * @param status the status
     * @param successful number of successful processed units
     * @param failed number of failed processed units
     * @param unprocessed number of unprocessed units
     * @param msg the message
     * @param statisticData the statistic data: <code>keY=value</code>
     */
    void assertProcessingUnitStatus(IProcessingUnitStatus processingUnitStatus, ProcessingRuntimeStatus status, Long successful, Long failed, Long unprocessed, String msg, List<String> statisticData) {
        assertNotNull(processingUnitStatus);
        assertEquals(successful, processingUnitStatus.getNumberOfSuccessfulUnits());
        assertEquals(failed, processingUnitStatus.getNumberOfFailedUnits());
        assertEquals(unprocessed, processingUnitStatus.getNumberOfUnprocessedUnits());
        assertEquals(status, processingUnitStatus.getProcessingRuntimeStatus());
        
        if (msg == null) {
            assertNull(processingUnitStatus.getStatusMessageList());
        } else {
            assertNotNull(processingUnitStatus.getStatusMessageList());
            assertEquals(msg, processingUnitStatus.getStatusMessageList());
        }
        
        assertProcessingUnitStatistic(processingUnitStatus.getProcessingUnitStatistic(), statisticData);
    }


    /**
     * Assert processing unit statistic
     *
     * @param processingUnitStatistic the processing unit statistic
     * @param statisticData the statistic data: <code>keY=value</code>
     */
    void assertProcessingUnitStatistic(IProcessingUnitStatistic processingUnitStatistic, List<String> statisticData) {
        if (processingUnitStatistic == null) {
            assertNull(statisticData);
            return;
        }
        
        assertNotNull(processingUnitStatistic);
        assertNotNull(statisticData);
        assertEquals(processingUnitStatistic.size(), statisticData.size());

        for (String s : statisticData) {
            int idx = s.indexOf('=');
            String key = s.substring(0, idx);
            assertEquals(processingUnitStatistic.get(key).getAverage(), Double.valueOf(s.substring(idx + 1)));
        }
    }
}
