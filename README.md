[![License](https://img.shields.io/github/license/toolarium/toolarium-processing-unit)](https://github.com/toolarium/toolarium-processing-unit/blob/master/LICENSE)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.toolarium/toolarium-processing-unit/1.2.0)](https://search.maven.org/artifact/com.github.toolarium/toolarium-processing-unit/1.2.0/jar)
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
    implementation "com.github.toolarium:toolarium-processing-unit:1.2.0"
}
```

### Maven:

```xml
<dependency>
    <groupId>com.github.toolarium</groupId>
    <artifactId>toolarium-processing-unit</artifactId>
    <version>1.2.0</version>
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
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#estimateNumberOfUnitsToProcess(com.github.toolarium.processing.unit.IProcessingUnitContext)
     */
    @Override
    public long estimateNumberOfUnitsToProcess(IProcessingUnitContext processingUnitContext) {
        // check how many entries we have to process, e.g. counting database records to process
        // it will be called just once, the first time before start processing
        // this number will be set in getProcessingProgress().setNumberOfUnitsToProcess(...) 
        return 10;
    }
    
    
    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#processUnit(com.github.toolarium.processing.unit.IProcessingUnitContext)
     */
    @Override
    public IProcessingUnitStatus processUnit(IProcessingUnitProgress processingProgress, IProcessingUnitContext processingUnitContext) throws ProcessingException {
        ProcessingUnitStatusBuilder processingUnitStatusBuilder = new ProcessingUnitStatusBuilder(); 

        // This is the main part where the processing takes place

        // In case of successful processing
        processingUnitStatusBuilder.processedSuccessful();
        
        // other wise if it was failed
        //processingUnitStatusBuilder.processingUnitFailed();

        // During a processing step status message can be returned, a status SUCCESSFUL, WARN or ERROR. Additional a message can be set
        //processingUnitStatusBuilder.warn("Warning sample");
        //processingUnitStatusBuilder.error("Error sample");
        //processingUnitStatusBuilder.message("Error sample");

        // Support of statistic:
        //processingUnitStatusBuilder.statistic("counter", 1);
        
        return processingUnitStatusBuilder.hasNext(processingProgress).build();
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
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitPersistenceImpl#newPersistenceInstance()
     */
    @Override
    protected SamplePersistence newPersistenceInstance() {
        return new SamplePersistence();
    }
    
    
    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#initializeParameterDefinition()
     */
    public void initializeParameterDefinition() {
        getParameterRuntime().addParameterDefinition(INPUT_FILENAME_PARAMETER); // register parameters
    }
    

    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#estimateNumberOfUnitsToProcess(com.github.toolarium.processing.unit.IProcessingUnitContext)
     */
    @Override
    public long estimateNumberOfUnitsToProcess(IProcessingUnitContext processingUnitContext) {
        // check how many entries we have to process, e.g. counting database records to process
        // it will be called just once, the first time before start processing
        // this number will be set in getProcessingProgress().setNumberOfUnitsToProcess(...) 
        return 10;
    }
    
    
    /**
     * @see com.github.toolarium.processing.unit.base.AbstractProcessingUnitImpl#processUnit(com.github.toolarium.processing.unit.IProcessingUnitContext)
     */
    @Override
    public IProcessingUnitStatus processUnit(IProcessingUnitProgress processingProgress, IProcessingUnitContext processingUnitContext) throws ProcessingException {
        ProcessingUnitStatusBuilder processingUnitStatusBuilder = new ProcessingUnitStatusBuilder(); 

        // This is the main part where the processing takes place

        // In case of successful processing
        processingUnitStatusBuilder.processedSuccessful();
        
        // other wise if it was failed
        //processingUnitStatusBuilder.processingUnitFailed();

        // During a processing step status message can be returned, a status SUCCESSFUL, WARN or ERROR. Additional a message can be set
        //processingUnitStatusBuilder.warn("Warning sample");
        //processingUnitStatusBuilder.error("Error sample");
        //processingUnitStatusBuilder.message("Error sample");

        // Support of statistic:
        //processingUnitStatusBuilder.statistic("counter", 1);
        
        getProcessingPersistence().setCounter(getProcessingPersistence().getCounter() + 1);
        getProcessingPersistence().setText("Counter" + getProcessingPersistence().getCounter());
        
        return processingUnitStatusBuilder.hasNext(processingProgress).build();
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
     * Define sample an own persistence 
     * 
     * @author patrick
     */
    static class SamplePersistence implements IProcessingUnitPersistence {
        private static final long serialVersionUID = -178680376384580300L;
        private String text;
        private int counter;
        
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

## How to test a processing (unit testing)
The class `TestProcessingUnitRunner` supports different methods to simulate all kind real behaviours: `run()`, `runAndAbort()`, `runWithThrottling()`, `runWithSuspendAndResume()`
To test the processing unit, the simple method `run()` fulfills the test purpose. The other run methods are just for specific tests.

```java
    @Test
    public void testProcessingUnitSample() {
        // set the input parameter
        List<Parameter> parameterList = new ArrayList<Parameter>();
        parameterList.add(new Parameter(ProcessingUnitSample.INPUT_FILENAME_PARAMETER.getKey(), "myFilename"));
        
        // Get a process runner and run the unit test
        TestProcessingUnitRunner<ProcessingUnitSample> processRunner = TestProcessingUnitRunnerFactory.getInstance().getProcessingUnitRunner();
        assertEquals(processRunner.run(ProcessingUnitSample.class, parameterList), 10);
    }
```

