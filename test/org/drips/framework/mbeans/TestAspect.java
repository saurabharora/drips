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
package org.drips.framework.mbeans;

import org.codehaus.aspectwerkz.definition.Pointcut;
import org.codehaus.aspectwerkz.joinpoint.JoinPoint;
import org.drips.framework.aspectwerkx.AbstractDripsAspect;
import org.drips.utils.Utils;


/**
 * This class contains the Sample Aspect used to test the core.
 * 
 * @author Saurabh Arora
 */
public class TestAspect extends AbstractDripsAspect {
    boolean DEBUG = true;

    Pointcut profilingcut;

    public Object doGenericMethodProfiling(final JoinPoint joinPoint)
            throws Throwable {
        if (DEBUG) {
            Utils.debug("doGenericMethodProfiling");
        }
        return joinPoint.proceed();
    }

    public static long testmethod() {
        return 0;
    }

    // TODO prasen, This method is good for testing, but should not be the
    // standard approach
    // as this class itself will suffice for TestMain
    protected Object getMBeanImplementation() {
        return new TestMain();
    }

    // TODO prasen. Good for testing. Should follow standard naming convention
    protected Class getMBeanInterface() {
        return TestMainMBean.class;
    }

    /*
     * //TODO need to remove this method, as the above 2 methods will do the
     * job public InternalDripsAspectInfo getInfo() { StandardMBean mbean; try {
     * mbean = new StandardMBean(new TestMain(), TestMainMBean.class); } catch
     * (NotCompliantMBeanException e) { e.printStackTrace(); throw new
     * IllegalStateException(e); } return new
     * InternalDripsAspectInfo(mbean.getMBeanInfo(),
     * "com.bea.drips.framework:type=" + TestMain.class.getName()); }
     */

}
