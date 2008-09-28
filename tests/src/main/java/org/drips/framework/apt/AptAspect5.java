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
package org.drips.framework.apt;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.aspectwerkz.joinpoint.JoinPoint;
import org.drips.framework.aspectwerkx.AbstractDripsAspect;
import org.drips.utils.Utils;


/**
 * @author Saurabh Arora
 */
@DripsBeanInfo(value="AptAspect5",
    mbeanInterface = "com.bea.drips.framework.apt.AptAspect5Inf")
public class AptAspect5 extends AbstractDripsAspect {
    boolean DEBUG = true;

    static List<String> sequence = new ArrayList<String>();

    @DripsBeanMethodInfo("Get Sequence")
    public static List getSequence() {
        return sequence;
    }

    @DripsBeanMethodInfo("Clear the Sequence")
    public static void clear() {
        sequence.clear();
    }

    public Object doAround(final JoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        sequence.add(methodName);
        if (DEBUG) {
            Utils.debug("doAround " + methodName);
        }
        return joinPoint.proceed();
    }

}
