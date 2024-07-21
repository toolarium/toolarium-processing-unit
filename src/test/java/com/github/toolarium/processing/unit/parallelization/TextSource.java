/*
 * TextSource.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.parallelization;

import com.github.toolarium.processing.unit.framework.TextProducer;
import java.util.List;


/**
 * The text source
 * 
 * @author patrick
 */
public final class TextSource {
    private TextProducer.Text text;

    /**
     * Private class, the only instance of the singelton which will be created by accessing the holder class.
     *
     * @author patrick
     */
    private static class HOLDER {
        static final TextSource INSTANCE = new TextSource();
    }

    /**
     * Constructor
     */
    private TextSource() {
        // NOP
    }

    /**
     * Get the instance
     *
     * @return the instance
     */
    public static TextSource getInstance() {
        return HOLDER.INSTANCE;
    }

    
    /**
     * Create the text
     *
     * @param size the size
     */
    public void createText(long size) {
        text = TextProducer.getInstance().getText(size);
    }
    
    
    /**
     * Get the next word block
     *
     * @param blockSize the block size
     * @return the word block
     */
    public List<String> getWords(long blockSize) {
        return text.getWords(blockSize);
    }
    
    
    /**
     * Increment the position
     */
    public void incrementPosition() {
        text.incrementPosition();
    }

    
    /**
     * Increment position
     *
     * @param size the size
     */
    public void incrementPosition(long size) {
        text.incrementPosition(size);
    }
    
    
    /**
     * Check if there are more text elements
     *
     * @return true if there are more words
     */
    public boolean hasMoreWords() {
        return text.hasMoreWords();
    }
}
