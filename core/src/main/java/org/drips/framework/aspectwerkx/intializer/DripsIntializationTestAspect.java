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
package org.drips.framework.aspectwerkx.intializer;

import org.codehaus.aspectwerkz.annotation.Around;
import org.codehaus.aspectwerkz.annotation.Expression;
import org.codehaus.aspectwerkz.definition.Pointcut;
import org.codehaus.aspectwerkz.joinpoint.JoinPoint;

/**
 * This is the Test Aspect Class, used by DripsIntializationTest.
 * 
 * @author Saurabh Arora
 */
public class DripsIntializationTestAspect {
    @Expression("execution(void ..DripsIntializationTest.method1())")
    Pointcut profilingcut;
    @Around("profilingcut")
    //@Around ("execution(void ..DripsIntializationTest.method1())")
    Object callPointcut(final JoinPoint joinPoint) throws Throwable {
        DripsIntializationUtils.methodcalled(
                "callPointcut");
        return joinPoint.proceed();

    }

    // This pointcut is defined at runtime
    Pointcut dynamicpointcut;

    @Around("dynamicpointcut")
    Object callDynamicPointcut(final JoinPoint joinPoint)
            throws Throwable {
        DripsIntializationUtils.methodcalled(
                "dynamicpointcut");
        return joinPoint.proceed();

    }

}
