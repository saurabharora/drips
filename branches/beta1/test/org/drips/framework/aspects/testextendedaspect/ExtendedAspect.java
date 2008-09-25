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
package org.drips.framework.aspects.testextendedaspect;

import org.codehaus.aspectwerkz.joinpoint.JoinPoint;
import org.drips.example.wordcount.WordCounter;
import org.drips.framework.aspectwerkx.AbstractDripsAspect;


/**
 * Test aspect which extends AbstractDripsAspect
 * 
 * @author Prasen Mukherjee
 */

public class ExtendedAspect extends AbstractDripsAspect implements
        ExtendedAspectMBean {

    public Object testAroundAdvise(JoinPoint joinPoint) {
        System.err.println("Aspect: ExtendedAspect.testMethod");
        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace(); // To change body of catch
                                            // statement use File | Settings |
                                            // File Templates.
        }
        return result;
    }

    public void doIt() {
        try {
            new WordCounter().runcounter("c:\\hello.txt");
        } catch (Exception e) {
            e.printStackTrace(); // To change body of catch statement use
                                    // File | Settings | File Templates.
        }
    }

    public void exit() {
        System.exit(0);
    }

}
