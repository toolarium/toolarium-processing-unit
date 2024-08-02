[![License](https://img.shields.io/github/license/toolarium/toolarium-processing-unit)](https://github.com/toolarium/toolarium-processing-unit/blob/master/LICENSE)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.toolarium/toolarium-processing-unit/1.3.1)](https://search.maven.org/artifact/com.github.toolarium/toolarium-processing-unit/1.3.1/jar)
[![javadoc](https://javadoc.io/badge2/com.github.toolarium/toolarium-processing-unit/javadoc.svg)](https://javadoc.io/doc/com.github.toolarium/toolarium-processing-unit)

# toolarium-processing-unit

Defines the processing unit interface.

A ProcessingUnit is a simple java class that implements the main part of a processing. The framework is designed so that only the main part, the real processing, has to be implemented. 
This means that you do not have to write any loops.

The processing unit has the following features
- You only need to write your "clue code", instead of loops, just conditions with only java dependencies (lightweight)
- You can pass in [parameters](https://github.com/toolarium/toolarium-processing-unit/blob/master/src/main/java/com/github/toolarium/processing/unit/dto/ParameterDefinition.java) (the parameter values can reference to environment or system properties, e.g. `${MYVALUE}`)
- A transparent [Progress](https://github.com/toolarium/toolarium-processing-unit/blob/master/src/main/java/com/github/toolarium/processing/unit/IProcessingProgress.java) 
- [Status](https://github.com/toolarium/toolarium-processing-unit/blob/master/src/main/java/com/github/toolarium/processing/unit/dto/ProcessingRuntimeStatus.java) defines whether the processing was successful or ended with warnings / errors included with messages.
- Support of additional [statistic counters](https://github.com/toolarium/toolarium-processing-unit/blob/master/src/main/java/com/github/toolarium/processing/unit/IProcessingStatistic.java) which is available by the progress
- [Context](https://github.com/toolarium/toolarium-processing-unit/blob/master/src/main/java/com/github/toolarium/processing/unit/IProcessingUnitContext.java) which can be used to pass in or out any context information
- A processing can have it's own additional [Persistence](https://github.com/toolarium/toolarium-processing-unit/blob/master/src/main/java/com/github/toolarium/processing/unit/IProcessingPersistence.java) object
- Support of [unit testing](https://github.com/toolarium/toolarium-processing-unit/blob/master/src/main/java/com/github/toolarium/processing/unit/runtime/test/TestProcessingUnitRunnerFactory.java)

## Built With

* [cb](https://github.com/toolarium/common-build) - The toolarium common build

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/toolarium/toolarium-processing-unit/tags). 


### Gradle:

```groovy
dependencies {
    implementation "com.github.toolarium:toolarium-processing-unit:1.3.1"
}
```

### Maven:

```xml
<dependency>
    <groupId>com.github.toolarium</groupId>
    <artifactId>toolarium-processing-unit</artifactId>
    <version>1.3.1</version>
</dependency>
```

## How to implement a processing
Either you implement simple the interface `com.github.toolarium.processing.unit.IProcessingUnit` or the implementation class inherit from the abstract class `com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl`.
The implementation is simpler to use the abstract class (see the sample below).


### ProcessingUnit Sample
```java
public class ProcessingUnitSample extends AbstractProcessingUnitImpl {
    /** INPUT_FILENAME: input filename parameter. It is not optional. */
    public static final  ParameterDefinition INPUT_FILENAME_PARAMETER = new ParameterDefinitionBuilder().name("inputFilename").isMandatory().description("The filename incl. path to read in a file.").build();

    
    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#initializeParameterDefinition()
     */
    public void initializeParameterDefinition() {
        getParameterRuntime().addParameterDefinition(INPUT_FILENAME_PARAMETER); // register parameters
    }
    

    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#estimateNumberOfUnitsToProcess()
     */
    @Override
    public long estimateNumberOfUnitsToProcess() {
        return getProcessingUnitProgress().setNumberOfUnitsToProcess(10);
    }
    

    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#processUnit(com.github.toolarium.processing.unit.ProcessingUnitStatusBuilder)
     */
    @Override
    public IProcessingUnitStatus processUnit(ProcessingUnitStatusBuilder processingUnitStatusBuilder) throws ProcessingException {
        // This is the main part where the processing takes place

        // In case of successful processing
        processingUnitStatusBuilder.increaseNumberOfSuccessfulUnits();
        
        // other wise if it was failed
        //processingUnitStatusBuilder.increaseNumberOfFailedUnits();

        // During a processing step status message can be returned, a status SUCCESSFUL, WARN or ERROR. Additional a message can be set
        //processingUnitStatusBuilder.warn("Warning sample");
        //processingUnitStatusBuilder.error("Error sample");
        //processingUnitStatusBuilder.message("Error sample");

        // Support of statistic:
        //processingUnitStatusBuilder.statistic("counter", 1);
        
        return processingUnitStatusBuilder.hasNextIfHasUnprocessedUnits().build();
    }

    
    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#releaseResource()
     */
    /* In case we have to release any resources
    @Override
    public void releaseResource() throws ProcessingException {
    }
    */
}
```

### ProcessingUnit Sample with own persistence
```java
public class ProcessingUnitSampleWithOwnPersistence extends AbstractProcessingUnitPersistenceImpl<ProcessingUnitSampleWithOwnPersistence.SamplePersistence> {
    /** INPUT_FILENAME: input filename parameter. It is not optional. */
    public static final ParameterDefinition INPUT_FILENAME_PARAMETER = new ParameterDefinitionBuilder().name("inputFilename").isMandatory().description("The filename incl. path to read in a file.").build();
    
    
    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#initializeParameterDefinition()
     */
    public void initializeParameterDefinition() {
        getParameterRuntime().addParameterDefinition(INPUT_FILENAME_PARAMETER); // register parameters
    }
    

    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#estimateNumberOfUnitsToProcess()
     */
    @Override
    public long estimateNumberOfUnitsToProcess() {
        return getProcessingUnitProgress().setNumberOfUnitsToProcess(10);
    }
    

    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#processUnit(com.github.toolarium.processing.unit.ProcessingUnitStatusBuilder)
     */
    @Override
    public IProcessingUnitStatus processUnit(ProcessingUnitStatusBuilder processingUnitStatusBuilder) throws ProcessingException {

        // This is the main part where the processing takes place

        // In case of successful processing
        processingUnitStatusBuilder.increaseNumberOfSuccessfulUnits();
        
        // other wise if it was failed
        //processingUnitStatusBuilder.increaseNumberOfFailedUnits();

        // During a processing step status message can be returned, a status SUCCESSFUL, WARN or ERROR. Additional a message can be set
        //processingUnitStatusBuilder.warn("Warning sample");
        //processingUnitStatusBuilder.error("Error sample");
        //processingUnitStatusBuilder.message("Error sample");

        // Support of statistic:
        //processingUnitStatusBuilder.statistic("counter", 1);
        
        getProcessingPersistence().setCounter(getProcessingPersistence().getCounter() + 1);
        getProcessingPersistence().setText("Counter" + getProcessingPersistence().getCounter());
        
        return processingUnitStatusBuilder.hasNextIfHasUnprocessedUnits().build();
    }

    
    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#releaseResource()
     */
    /* In case we have to release any resources
    @Override
    public void releaseResource() throws ProcessingException {
    }
    */
    
    
    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitPersistenceImpl#newPersistenceInstance()
     */
    @Override
    protected SamplePersistence newPersistenceInstance() {
        return new SamplePersistence();
    }
    
    
    /**
     * Define sample an own persistence 
     * 
     * @author patrick
     */
    static class SamplePersistence implements IProcessingUnitPersistence {
        private static final long serialVersionUID = -178680376384580300L;
        private String text;
        private int counter;
        
        
        /**
         * Constructor for SamplePersistence
         */
        SamplePersistence() {
        }
        
        
        /**
         * Get text
         *
         * @return the text
         */
        public String getText() {
            return text;
        }
        
        
        /**
         * Set text
         *
         * @param text the text
         */
        public void setText(String text) {
            this.text = text;
        }
        
        
        /**
         * Get the counter
         *
         * @return the counter
         */
        public int getCounter() {
            return counter;
        }
        
        
        /**
         * Set the counter
         *
         * @param counter the counter
         */
        public void setCounter(int counter) {
            this.counter = counter;
        }

        
        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "SamplePersistence [text=" + text + ", counter=" + counter + "]";
        }
    }
}
```

### ProcessingUnit Sample with own persistence
A processing unit which supports parallel / multithreaded execution you have simply to add the marker interface <code>IParallelProcessingUnit</code>.
The interface <code>IProcessingUnitObjectLockManagerSupport</code> defines there is an object manager which can be used for locking between the threads.

```java
public class ParallelProcessingUnitSample extends AbstractProcessingUnitPersistenceImpl<ParallelProcessingUnitSample.ParallelProcessingPersistence> implements IParallelProcessingUnit, IProcessingUnitObjectLockManagerSupport {
    
    /** NUMBER_OF_WORDS: the number of words. */
    public static final ParameterDefinition NUMBER_OF_WORDS = new ParameterDefinitionBuilder().name("numberOfWords").defaultValue(10000).description("The number of words.").build();
    
    private List<String> wordResultList;

    
    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#initializeParameterDefinition()
     */
    public void initializeParameterDefinition() {
        getParameterRuntime().addParameterDefinition(NUMBER_OF_WORDS); // register parameters
    }

    
    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#initialize(java.util.List, com.github.toolarium.processing.unit.IProcessingUnitContext)
     */
    @Override
    public void initialize(List<Parameter> parameterList, IProcessingUnitContext processingUnitContext) throws ValidationException, ProcessingException {
        super.initialize(parameterList, processingUnitContext);
        wordResultList = new ArrayList<String>();
    }
    
    
    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#estimateNumberOfUnitsToProcess()
     */
    @Override
    public long estimateNumberOfUnitsToProcess() {
        // simulate data source: it's in a static way so multiple threads see the same -> this method is only called ones fron the first thread
        TextSource.getInstance().createText(getParameterRuntime().getParameterValueList(NUMBER_OF_WORDS).getValueAsLong());
        return getProcessingUnitProgress().setNumberOfUnitsToProcess(getParameterRuntime().getParameterValueList(NUMBER_OF_WORDS).getValueAsLong());
    }
    

    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#processUnit(com.github.toolarium.processing.unit.ProcessingUnitStatusBuilder)
     */
    @Override
    public IProcessingUnitStatus processUnit(ProcessingUnitStatusBuilder processingUnitStatusBuilder) throws ProcessingException {
        int blockSize = 10;
        
        List<String> lockList = getObjectLockManager().lock(TextSource.getInstance().getWords(blockSize));
        try {
            final Queue<String> wordBuffer = new LinkedBlockingQueue<String>(lockList);
            if (wordBuffer != null && !wordBuffer.isEmpty()) {
                while (wordBuffer != null && !wordBuffer.isEmpty()) {
                    final String text = wordBuffer.poll();
                    if (text != null && !text.isBlank()) {
                        wordResultList.add(text);
           
                        // mark one as processed
                        TextSource.getInstance().incrementPosition();
                        processingUnitStatusBuilder.increaseNumberOfSuccessfulUnits();
                        processingUnitStatusBuilder.statistic("processedWord", 1L);
                    } else {
                        processingUnitStatusBuilder.warn("Empty text dound!");
                        processingUnitStatusBuilder.increaseNumberOfFailedUnits();
                    }                    
                }
            }
        } finally {
            if (lockList != null && !lockList.isEmpty()) {
                getObjectLockManager().unlock(lockList);
            }
        }
       
        return processingUnitStatusBuilder.hasNextIfHasUnprocessedUnits().build();
    }
    
    
    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitPersistenceImpl#suspendProcessing()
     */
    @Override
    public IProcessingUnitPersistence suspendProcessing() throws ProcessingException {
        getProcessingPersistence().setWordResultList(wordResultList);
        return super.suspendProcessing();
    }
    
    
    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitPersistenceImpl#resumeProcessing(com.github.toolarium.processing.unit.IProcessingUnitProgress, com.github.toolarium.processing.unit.IProcessingUnitPersistence)
     */
    @Override
    public void resumeProcessing(IProcessingUnitProgress processingUnitProgress, IProcessingUnitPersistence processingPersistence) throws ProcessingException {
        super.resumeProcessing(processingUnitProgress, processingPersistence);
        wordResultList = getProcessingPersistence().getWordResultList();
        removePersistenceInstance();
    }

    
    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#releaseResource()
     */
    @Override
    public void releaseResource() throws ProcessingException {
    }

    
    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitPersistenceImpl#newPersistenceInstance()
     */
    @Override
    protected ParallelProcessingPersistence newPersistenceInstance() {
        return new ParallelProcessingPersistence();
    }


    /**
     * Defines the parallel processing persistence 
     */
    static class ParallelProcessingPersistence implements IProcessingUnitPersistence {
        private static final long serialVersionUID = -178680376384580300L;
        private List<String> wordResultList; 

        
        /**
         * Get the result word list
         * 
         * @return the result word list
         */
        public List<String> getWordResultList() {
            return wordResultList;
        }

        
        /**
         * Set the result word list
         * 
         * @param wordResultList the word list
         */
        public void setWordResultList(List<String> wordResultList) {
            this.wordResultList = wordResultList;
        }
    }
}
```

In case of a parallel execution the number of threads can be set by the parameter <code>NUMBER_OF_WORDS</code>.
