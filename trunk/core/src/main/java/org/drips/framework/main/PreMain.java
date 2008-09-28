/*
 * Copyright 2008 Drips project Owners http://code.google.com/p/drips/
 * Saurabh Arora <saurabh.arora@gmail.com> And Prasen Mukherjee <prasen.bea@gmail.com>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.drips.framework.main;

import java.lang.instrument.Instrumentation;

import org.drips.utils.Utils;


/**
 * Java 1.5 preMain agent Can be used with -javaagent:dripsagent-xxx.jar
 * 
 * @author <a href="mailto:prasen.bea@gmail.com">Prasen Mukherjee</a>
 * @see java.lang.instrument.Instrumentation
 */

public class PreMain extends DripsBaseBootstrap {

    /**
     * JSR-163 preMain Agent entry method
     * 
     * @param options
     *            options passed from JVM.
     * @param instrumentation
     *            Handle to Instrumentation interface for changing byte code.
     */
    public static void premain(final String options,
            final Instrumentation instrumentation) {
        Utils.debug("Initializing drips from premain");
        boolean initialized = new DripsBaseBootstrap().intializeDrips();
        if (!initialized) {
            throw new RuntimeException(
                    "Cannot initialize Drips from Premain. Aborting...");
        }
        // DripsAspectrepository.registerAspect("com.bea.drips.framework.aspects.testextendedaspect.ExtendedAspectTest");
        Utils.debug("Drips Initialized from premain");
    }

}
