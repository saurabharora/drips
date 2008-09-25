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

import java.io.IOException;

import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MalformedObjectNameException;
import javax.management.ReflectionException;

import org.drips.framework.test.AbstractDripsBaseTest;


/**
 * @author Prasen Mukherjee
 */
public class ExtendedAspectTest extends AbstractDripsBaseTest {

    protected boolean isLocalJVM() {
        return false;
    }

    public void testUndeployExtendedAspect() throws IOException,
            MalformedObjectNameException, InstanceNotFoundException,
            ClassNotFoundException, ReflectionException,
            IntrospectionException {

        ExtendedAspectMBean aspectMBean = (ExtendedAspectMBean) enableAspect(ExtendedAspect.class);
        String aspectExpression = "execution(public void ..WordCounter.runcounter(..))";
        String adviceName = "testAroundAdvise";
        String adviceType = "around";
        aspectMBean.doIt();
        String handle = aspectMBean.deploy(aspectExpression, adviceName,
                adviceType);
        aspectMBean.doIt();
        aspectMBean.undeploy(handle);
        aspectMBean.doIt();
        // System.out.println("aspectMBean = " +
        // aspectMBean.getInfo().getDripsInfo().getObjectName());
        // if (handle!=null) aspectMBean.undeploy(handle);handle = null;
        // aspectMBean.undeploy();

    }

}
