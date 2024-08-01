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
import java.util.Objects;
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
     * Get a text
     *
     * @param text the text to add to the {@link StringList}.
     * @return the size of the text
     */
    public StringList toStringList(List<String> text) {
        return new StringList(text);
    }

    
    /**
     * Parse string to string list
     *
     * @param inputContent the string list as string
     * @return the string list
     */
    public StringList toStringList(String inputContent) {
        List<String> stringList = new ArrayList<String>();
        String content = inputContent;
        if (content != null && content.length() > 2) {
            content = content.substring(1, content.length() - 2);
            
            String[] parseResult = content.split("\\}, ");
            for (int i = 0; i < parseResult.length; i++) {
                String[] contentSplit = parseResult[i].split("\\{");
                
                if (contentSplit != null) {
                    stringList.add(contentSplit[1]);
                }
            }
        }
        
        return new StringList(stringList);
    }


    /**
     * Get random text
     *
     * @param numberOfWords the number of words
     * @return the text as list
     */
    private StringList getRandomText(long numberOfWords) {
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
        
        return new StringList(result);
    }

    
    /**
     * The string list
     */
    public class StringList implements Serializable {
        private static final long serialVersionUID = 3660407008816423978L;
        private List<String> stringList;
        
        
        /**
         * Constructor for StringList
         *
         * @param stringList the string list
         */
        public StringList(List<String> stringList) {
            this.stringList = stringList; 
        }

        
        /**
         * List
         *
         * @return the list
         */
        public List<String> list() {
            return stringList;
        }

        
        /**
         * Get the size
         *
         * @return the size
         */
        public int size() {
            return stringList.size();
        }

        
        /**
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + Objects.hash(stringList);
            return result;
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
            
            StringList other = (StringList) obj;
            return Objects.equals(stringList, other.stringList);
        }


        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            if (stringList == null) {
                return null;
            }
            
            return stringList.toString();
        }
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
         * @param stringList the string list
         */
        Text(StringList stringList) {
            this.text = new ConcurrentHashMap<Long, String>();
            this.currentPosition = 0;
            
            long counter = 0;
            for (String t : stringList.list()) {
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
        public synchronized String getWord() {
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
        public synchronized void close() {
            this.text = null;
        }

        
        /**
         * Get the size
         *
         * @return the size
         */
        public synchronized long size() {
            return text.size();
        }
        
        
        /**
         * Get the number of unprocessed words
         *
         * @return the number of unprocessed words
         */
        public synchronized long getNumberOfUnprocessedWords() {
            return size() - currentPosition;
        }

        
        @Override
        public String toString() {
            return "Text [text=" + getWords(getNumberOfUnprocessedWords()) + "]";
        }
    }
}
