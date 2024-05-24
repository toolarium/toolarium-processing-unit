/*
 * MyDataProcessingUnitConstants.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.processing.unit.mydata;

import com.github.toolarium.processing.unit.dto.ParameterDefinition;
import com.github.toolarium.processing.unit.dto.ParameterValueType;


/**
 * Defines the parameters for the {@link MyDataProcessingUnit}.
 *
 * @author patrick
 */
public interface MyDataProcessingUnitConstants {
    /** INPUT_FILENAME: input filename parameter. It is not optional. */
    ParameterDefinition INPUT_FILENAME_PARAMETER =
            new ParameterDefinition("inputFilename",
                                    ParameterValueType.STRING,
                                    ParameterDefinition.NO_DEFAULT_PARAMETER,
                                    ParameterDefinition.NOT_OPTIONAL,
                                    1,
                                    ParameterDefinition.EMPTY_VALUE_NOT_ALLOWED,
                                    "The filename incl. path to read in a file.");

    /** OUTPUT_FILENAME: output filename parameter. It is not optional. */
    ParameterDefinition OUTPUT_FILENAME_PARAMETER =
            new ParameterDefinition("outputFilename",
                                    ParameterValueType.STRING,
                                    ParameterDefinition.NO_DEFAULT_PARAMETER,
                                    ParameterDefinition.NOT_OPTIONAL,
                                    1,
                                    ParameterDefinition.EMPTY_VALUE_NOT_ALLOWED,
                                    "The filename incl. path to write out a file.");

    /** JDBC_BATCHSIZE: jdbc batch size parameter. It is not optional. */
    ParameterDefinition JDBC_BATCHSIZE_PARAMETER =
            new ParameterDefinition("jdbcBatchSize",
                                    ParameterValueType.INTEGER,
                                    25,
                                    ParameterDefinition.OPTIONAL,
                                    1,
                                    ParameterDefinition.EMPTY_VALUE_NOT_ALLOWED,
                                    "The JDBC batch size.");

    /** FILENAME_PARAMETER */
    ParameterDefinition FILENAME_PARAMETER =
            new ParameterDefinition("filename",
                                    "my-name",
                                    ParameterDefinition.OPTIONAL,
                                    "The file name.");

    /** DEFAULTVALUE_TEST_PARAMETER */
    ParameterDefinition DEFAULTVALUE_TEST_PARAMETER =
            new ParameterDefinition("defaultValueTest",
                                    ParameterDefinition.OPTIONAL,
                                    "The default test value parameter.");

    /** COUNTER_PARAMETER */
    ParameterDefinition COUNTER_PARAMETER =
            new ParameterDefinition("counter",
                                    3,
                                    ParameterDefinition.OPTIONAL,
                                    "The counter.");

    /** HASH_NAMES */
    ParameterDefinition HASH_NAMES =
            new ParameterDefinition("hashNames",
                                    ParameterValueType.STRING,
                                    new String[] {"SHA-256", "SHA-1"},
                                    ParameterDefinition.OPTIONAL,
                                    100,
                                    false,
                                    true,
                                    "The hash names, default parameter test.");

    /** KEY_NAMES */
    ParameterDefinition KEY_NAMES =
            new ParameterDefinition("keyNames",
                                    ParameterValueType.STRING,
                                    null,
                                    ParameterDefinition.OPTIONAL,
                                    100,
                                    false,
                                    "The key names, default parameter test.");
}
