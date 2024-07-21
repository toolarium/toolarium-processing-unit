/*
 * TextProducerTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.framework;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;


/**
 * Test the text producer
 * 
 * @author patrick
 */
public class TextProducerTest {
    
    /**
     * Test text producer
     */
    @Test
    public void test() {
        TextProducer.Text text = TextProducer.getInstance().getText(128);

        assertEquals(1, text.getWords(1).size());
        assertEquals(text.getWords(1).get(0), text.getWord());

        assertEquals(2, text.getWords(2).size());
        assertEquals(text.getWords(2), text.getWords(2));
        
        assertEquals(20, text.getWords(20).size());
        assertEquals(text.getWords(20), text.getWords(20));
        
        assertEquals(128, text.getWords(128).size());
        assertEquals(text.getWords(128), text.getWords(128));
        
        assertEquals(128, text.getWords(130).size());
        assertEquals(text.getWords(130), text.getWords(130));
        
        List<String> text40 = text.getWords(40);
        final List<String> text128 = text.getWords(128);
        text.incrementPosition(20);
        assertEquals(text40.subList(20, 40),text.getWords(20));

        text.incrementPosition(107);
        assertEquals(text128.subList(127, 128), text.getWords(10));

        text.incrementPosition(128);
        assertEquals(new ArrayList<String>(),  text.getWords(10));
    }
}
