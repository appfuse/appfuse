package org.appfuse.mojo.exporter;

/*
* Copyright 2005 Johann Reyes.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

import java.io.Serializable;

/**
 * Bean in charge to hold the default values needed to run a goal.
 *
 * @author <a href="mailto:jreyes@hiberforum.org">Johann Reyes</a>
 * @version $Id$
 */
public final class Component implements Serializable {
    /**
     * Configuration alias for this goal implementation.
     */
    private String implementation;

    /**
     * Goal's name.
     */
    private String name;

    /**
     * Output directory for this goal implementation.
     */
    private String outputDirectory;

    /**
     * Returns the alias of the Configuration class that this goal uses.
     *
     * @return String
     */
    public String getImplementation() {
        return implementation;
    }

    /**
     * Sets the alias of the Configuration class that this goal uses.
     *
     * @param implementation String
     */
    public void setImplementation(String implementation) {
        this.implementation = implementation;
    }

    /**
     * Returns this goal's name.
     *
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Sets this goal's name.
     *
     * @param name String
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the output directory for this goal.
     *
     * @return String
     */
    public String getOutputDirectory() {
        return outputDirectory;
    }

    /**
     * Sets the output directory for this goal.
     *
     * @param outputDirectory String
     */
    public void setOutputDirectory(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }
}
