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
package org.drips.framework.aspects.testtomcataspect;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MalformedObjectNameException;
import javax.management.ReflectionException;
import javax.swing.tree.DefaultMutableTreeNode;

import org.drips.framework.aspects.timing.TimingAspect;
import org.drips.framework.aspects.timing.TimingAspectMBean;
import org.drips.framework.mbeans.DripsMainMBean;
import org.drips.framework.test.AbstractDripsBaseTest;
import org.drips.utils.Utils;


/**
 * @author Prasen Mukherjee
 */
public class TestTimingTomcatAspect extends AbstractDripsBaseTest {
    private DripsMainMBean dripsMainMBean;

    protected void setUp() throws Exception {
        dripsMainMBean = getDripsMainMBean();
    }

    protected boolean isLocalJVM() {
        return false;
    }

    public void testDoDeployPointcut() throws ReflectionException,
            InstanceNotFoundException, IOException, ClassNotFoundException,
            MalformedObjectNameException, IntrospectionException {
        TimingAspectMBean timingAspectMBean = (TimingAspectMBean) enableAspect(TimingAspect.class);
        String aspectExpression = "execution(public void HelloWorldExample.*(..))";
        String adviceName = "doGenericMethodProfiling";
        String adviceType = "around";
        String handle = timingAspectMBean.deploy(aspectExpression,
                adviceName, adviceType);
        System.out.println("handle = " + handle);
    }

    public void testDoUnDeployPointcut() throws ReflectionException,
            InstanceNotFoundException, IOException, ClassNotFoundException,
            MalformedObjectNameException, IntrospectionException {
        TimingAspectMBean timingAspectMBean = (TimingAspectMBean) enableAspect(TimingAspect.class);
        timingAspectMBean.undeploy();
        dripsMainMBean.disableAspect(TimingAspect.class.getName());
        timingAspectMBean = (TimingAspectMBean) getAspectMBean(TimingAspect.class);
        assertTrue(timingAspectMBean == null);

    }

    public void testGetAllExecutionProfileTrees() throws ReflectionException,
            InstanceNotFoundException, IOException, ClassNotFoundException,
            MalformedObjectNameException, IntrospectionException {
        TimingAspectMBean timingAspectMBean = (TimingAspectMBean) enableAspect(TimingAspect.class);
        Collection<DefaultMutableTreeNode> executionProfileTrees = timingAspectMBean
                .getExecutionProfileTrees();
        assertTrue(executionProfileTrees != null);
        System.out.println("executionProfileTrees.size = "
                + executionProfileTrees.size());
        for (Iterator<DefaultMutableTreeNode> iterator = executionProfileTrees
                .iterator(); iterator.hasNext();)
        {
            DefaultMutableTreeNode defaultMutableTreeNode = iterator.next();
            System.out.println("defaultMutableTreeNode = "
                    + defaultMutableTreeNode);
            Utils.printTree(defaultMutableTreeNode, "");
        }
    }

}
