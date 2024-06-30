/*
 * MyDataProcessingUnitConstants.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.mydata;

import com.github.toolarium.processing.unit.ParameterDefinitionBuilder;
import com.github.toolarium.processing.unit.dto.ParameterDefinition;
import com.github.toolarium.processing.unit.dto.ParameterValueType;


/**
 * Defines the parameters for the {@link MyDataProcessingUnit}.
 *
 * @author patrick
 */
public interface MyDataProcessingUnitConstants {
    /** INPUT_FILENAME: input filename parameter. It is not optional. */
    ParameterDefinition INPUT_FILENAME_PARAMETER = new ParameterDefinitionBuilder().name("inputFilename").isMandatory().emptyValueIsNotAllowed().description("The filename incl. path to read in a file.").build();

    /** OUTPUT_FILENAME: output filename parameter. It is not optional. */
    ParameterDefinition OUTPUT_FILENAME_PARAMETER = new ParameterDefinitionBuilder().name("outputFilename").isMandatory().emptyValueIsNotAllowed().description("The filename incl. path to write out a file.").build();

    /** JDBC_BATCHSIZE: jdbc batch size parameter. It is not optional. */
    ParameterDefinition JDBC_BATCHSIZE_PARAMETER = new ParameterDefinitionBuilder().name("jdbcBatchSize").defaultValue(25).type(ParameterValueType.INTEGER).emptyValueIsNotAllowed().description("The JDBC batch size.").build();

    /** FILENAME_PARAMETER */
    ParameterDefinition FILENAME_PARAMETER = new ParameterDefinitionBuilder().name("filename").defaultValue("my-name").description("The file name.").build();

    /** DEFAULTVALUE_TEST_PARAMETER */
    ParameterDefinition DEFAULTVALUE_TEST_PARAMETER = new ParameterDefinitionBuilder().name("defaultValueTest").description("The default test value parameter.").build();

    /** COUNTER_PARAMETER */
    ParameterDefinition COUNTER_PARAMETER = new ParameterDefinitionBuilder().name("counter").defaultValue(3).type(ParameterValueType.INTEGER).description("The counter.").build();
    
    /** HASH_NAMES */
    ParameterDefinition HASH_NAMES = new ParameterDefinitionBuilder().name("hashNames").defaultValue(new String[] {"SHA-256", "SHA-1"}).maxOccurs(100).emptyValueIsNotAllowed().hasValueToProtect()
                                                                     .description("The hash names, default parameter test.").build();
    
    /** KEY_NAMES */
    ParameterDefinition KEY_NAMES = new ParameterDefinitionBuilder().name("keyNames").maxOccurs(100).emptyValueIsNotAllowed().description("The hash names, default parameter test.").build();

    /** NUMBER_OF_TESTDATA_RECORDS: The number of test data records. */
    ParameterDefinition NUMBER_OF_TESTDATA_RECORDS = new ParameterDefinitionBuilder().name("numberOfTestDataRecords").type(ParameterValueType.INTEGER).defaultValue(26).description("The number of testdata records.").build();
}
