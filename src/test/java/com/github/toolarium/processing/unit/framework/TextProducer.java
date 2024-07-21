/*
 * TextProducer.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.framework;

import com.github.toolarium.common.util.RandomGenerator;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Text producer
 *  
 * @author patrick
 */
public final class TextProducer {
    private long wordCounter;

    
    /**
     * Private class, the only instance of the singelton which will be created by accessing the holder class.
     *
     * @author patrick
     */
    private static class HOLDER {
        static final TextProducer INSTANCE = new TextProducer();
    }

    
    /**
     * Constructor
     */
    private TextProducer() {
        wordCounter = 0;
    }

    
    /**
     * Get the instance
     *
     * @return the instance
     */
    public static TextProducer getInstance() {
        return HOLDER.INSTANCE;
    }


    /**
     * Get a text
     *
     * @param numberOfWords the number of words
     * @return the size of the text
     */
    public Text getText(long numberOfWords) {
        return new Text(getRandomText(numberOfWords));
    }


    /**
     * Get random text
     *
     * @param numberOfWords the number of words
     * @return the text as list
     */
    private List<String> getRandomText(long numberOfWords) {
        List<String> result = new ArrayList<String>();
        
        for (int i = 0; i < numberOfWords; i++) {
            StringBuilder text = new StringBuilder();
            text.append(++wordCounter).append("{");

            int wordLen = 0;
            while (wordLen < 2) {
                wordLen = Long.valueOf(RandomGenerator.getInstance().getRandomNumber(8, false)).intValue();
            }
            
            text.append(RandomGenerator.getInstance().getRandomString(wordLen, RandomGenerator.validNumberLetterCharacters));
            text.append("}");
            
            result.add(text.toString());            
        }
        
        return result;
    }

    
    /**
     * Represents a text
     * 
     * @author patrick
     */
    public class Text implements Serializable {
        private static final long serialVersionUID = -3156747127218754770L;
        private Map<Long, String> text;
        private volatile long currentPosition;
        
        
        /**
         * Constructor for SamplePersistence
         * 
         * @param text the text
         */
        Text(List<String> text) {
            this.text = new ConcurrentHashMap<Long, String>();
            this.currentPosition = 0;
            
            long counter = 0;
            for (String t : text) {
                this.text.put(Long.valueOf(counter++), t);
            }
        }

        
        /**
         * Check if there are more text elements
         *
         * @return true if there are more words
         */
        public synchronized boolean hasMoreWords() {
            return currentPosition < this.text.size();
        }

        
        /**
         * Get the next word
         * 
         * @return the word
         */
        public String getWord() {
            List<String> result = getWords(1);
            if (result.size() > 0) {
                return result.get(0);
            }
            
            return null;
        }

        
        /**
         * Get the next words
         * 
         * @param blockSize the block size
         * @return the words
         */
        public synchronized List<String> getWords(long blockSize) {
            
            List<String> textBlock = new ArrayList<String>();
            if (!hasMoreWords()) {
                return textBlock;
            }
            
            long max = currentPosition + blockSize;
            if (max > text.size()) {
                max = text.size();
            }
            
            for (long i = currentPosition; i < max; i++) {
                textBlock.add(text.get(i));
            }
            
            return textBlock;
        }

        
        /**
         * Increment the position
         */
        public void incrementPosition() {
            incrementPosition(1);
        }


        /**
         * Increment the position
         *
         * @param increment the position
         */
        public synchronized void incrementPosition(long increment) {
            long size = size();
            if (this.currentPosition < size) {
                if ((this.currentPosition + increment) < size) {
                    this.currentPosition += increment;
                } else {
                    this.currentPosition = size();
                }
            }
        }
        
        
        /**
         * Close the data producer
         */
        public void close() {
            this.text = null;
        }

        
        /**
         * Get the size
         *
         * @return the size
         */
        public long size() {
            return text.size();
        }
        

        @Override
        public String toString() {
            return "Text [text=" + getWords(size() - currentPosition) + "]";
        }
    }
}
