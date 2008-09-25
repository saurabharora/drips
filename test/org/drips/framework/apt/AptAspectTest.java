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

import java.io.IOException;

import javax.management.Attribute;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;

import org.drips.framework.aspectwerkx.DripsAspect;
import org.drips.framework.aspectwerkx.DripsAspectInfo;
import org.drips.framework.main.DripsMain;
import org.drips.framework.mbeans.DripsMainMBean;
import org.drips.framework.test.AbstractDripsBaseTest;


public class AptAspectTest extends AbstractDripsBaseTest {

    private DripsMain m = new DripsMain();

    protected void setUp() throws Exception {
        System.out.println("DripsMainTest Called");
        super.setUp();
        String[] argument =
            { "com.bea.drips.example.wordcount.WordCounter", "hello.txt" };

        if (isLocalJVM()) {
            m.start();
            m.startMainProgram(argument);
        }
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        m.shutdown();
    }

    public void testAptAspect1() throws Exception {
        Class aptclass = AptAspect1.class;
        String aptdescription = "AptAspect1";
        MBeanInfo info = enableAspectandGetInfo(aptclass);
        assertNotNull(info);
        assertEquals(info.getClassName(), aptclass.getName());
        assertEquals(info.getDescription(), aptdescription);

        // Assert the Operations
        assertNotNull(info.getOperations());
        assertEquals(info.getOperations().length, 5);
        assertEquals(info.getOperations()[0].getName(), "counterValue");
        assertEquals(info.getOperations()[0].getDescription(), "Counter");
        assertEquals(info.getOperations()[0].getSignature().length, 0);
        assertEquals(info.getOperations()[0].getReturnType(), "int");

        // Assert the attributes.
        assertNotNull(info.getAttributes());
        assertEquals(info.getAttributes().length, 1);
        assertEquals(info.getAttributes()[0].getName(), "Counter");
        assertEquals(info.getAttributes()[0].getDescription(),
                "Setting Counter");
        assertEquals(info.getAttributes()[0].getType(), "int");

        MBeanServerConnection mbs = getMBeanServerConnection();
        DripsAspectInfo apectInfo = getDripsMainMBean().getAspectInfo(
                aptclass.getName());
        assertEquals(apectInfo.getDescription(), aptdescription);
        assertEquals(apectInfo.getMBeanInerfaceName(), DripsAspect.class
                .getName());
        assertEquals(apectInfo.getName(), aptclass.getName());
        assertEquals(apectInfo.getObjectName(),
                "com.bea.drips.framework:type=" + aptclass.getName());
        ObjectName aspectObjectName = new ObjectName(apectInfo
                .getObjectName());
        String[] signature = new String[0];

        // Calls to Operations.
        Object ob = mbs.invoke(aspectObjectName, info.getOperations()[0]
                .getName(), new Object[0], signature);
        assertNotNull(ob);
        assertNotNull(ob);
        assertEquals(((Integer) ob).intValue(), 1);
        // Second call
        ob = mbs.invoke(aspectObjectName, info.getOperations()[0].getName(),
                new Object[0], signature);
        assertNotNull(ob);
        assertEquals(((Integer) ob).intValue(), 2);

        // Calls to the Attributes.
        ob = mbs.getAttribute(aspectObjectName, info.getAttributes()[0]
                .getName());
        assertNotNull(ob);
        assertEquals(((Integer) ob).intValue(), 3);

        // Again call Attribute, same value should be returned.
        ob = mbs.getAttribute(aspectObjectName, info.getAttributes()[0]
                .getName());
        assertNotNull(ob);
        assertEquals(((Integer) ob).intValue(), 3);

        // Call setter on the Attribute.
        // Attribute attr = new Attribute("Counter", new Integer(10));
        Attribute attr = new Attribute("Counter", 10);
        mbs.setAttribute(aspectObjectName, attr);

        // Again call Attribute, we should get 10 as value
        ob = mbs.getAttribute(aspectObjectName, info.getAttributes()[0]
                .getName());
        assertNotNull(ob);
        assertEquals(((Integer) ob).intValue(), 10);

    }

    protected MBeanInfo enableAspectandGetInfo(final Class aspectClass)
            throws MalformedObjectNameException, IOException,
            InstanceNotFoundException, ClassNotFoundException,
            ReflectionException, IntrospectionException {
        DripsMainMBean dripsMainMBean = getDripsMainMBean();
        String aspectClassName = aspectClass.getName();

        if (!dripsMainMBean.isAspectEnabled(aspectClassName)) {
            dripsMainMBean.enableAspect(aspectClassName);
        }
        DripsAspectInfo apectInfo = dripsMainMBean.getAspectInfo(aspectClass
                .getName());
        assertNotNull(apectInfo);
        assertTrue(dripsMainMBean.isAspectEnabled(aspectClassName));

        MBeanServerConnection mBeanServerConnection = null;

        mBeanServerConnection = getMBeanServerConnection();

        DripsAspect aspectMBean = null;
        ObjectName aspectObjectName = new ObjectName(apectInfo
                .getObjectName());
        if (mBeanServerConnection.isRegistered(aspectObjectName)) {
            return mBeanServerConnection.getMBeanInfo(aspectObjectName);
        }
        fail("Cannot Find Mbean " + aspectObjectName + "in the Mbean Server");
        return null;
    }

    public void testAptAspect2() throws Exception {
        Class aptclass = AptAspect2.class;
        String aptdescription = "AptAspect2";
        MBeanInfo info = enableAspectandGetInfo(aptclass);
        assertNotNull(info);
        assertEquals(info.getClassName(), aptclass.getName());
        assertEquals(info.getDescription(), aptdescription);

        // Assert the Operations
        assertNotNull(info.getOperations());
        assertEquals(info.getOperations().length, 5);
        assertEquals(info.getOperations()[0].getName(), "counterValue");
        assertEquals(info.getOperations()[0].getDescription(), "Counter");
        assertEquals(info.getOperations()[0].getSignature().length, 0);
        assertEquals(info.getOperations()[0].getReturnType(), "int");

        MBeanServerConnection mbs = getMBeanServerConnection();
        DripsAspectInfo apectInfo = getDripsMainMBean().getAspectInfo(
                aptclass.getName());
        assertEquals(apectInfo.getDescription(), aptdescription);
        assertEquals(apectInfo.getMBeanInerfaceName(), DripsAspect.class
                .getName());
        assertEquals(apectInfo.getName(), aptclass.getName());
        assertEquals(apectInfo.getObjectName(), "com.bea:type=AptAspect2");
        ObjectName aspectObjectName = new ObjectName(apectInfo
                .getObjectName());
        String[] signature = new String[0];

        Object ob = mbs.invoke(aspectObjectName, info.getOperations()[0]
                .getName(), new Object[0], signature);
        assertNotNull(ob);
        assertNotNull(ob);
        assertEquals(((Integer) ob).intValue(), 1);
        // Second call
        ob = mbs.invoke(aspectObjectName, info.getOperations()[0].getName(),
                new Object[0], signature);
        assertNotNull(ob);
        assertEquals(((Integer) ob).intValue(), 2);
    }

    public void testAptAspect3() throws Exception {
        Class aptclass = AptAspect3.class;
        String aptdescription = "AptAspect3";
        MBeanInfo info = enableAspectandGetInfo(aptclass);
        assertNotNull(info);
        assertEquals(info.getClassName(), aptclass.getName());
        assertEquals(info.getDescription(), aptdescription);

        assertNotNull(info.getOperations());
        assertEquals(info.getOperations().length, 5);
        assertEquals(info.getOperations()[0].getName(), "currentCounter");
        assertEquals(info.getOperations()[0].getDescription(),
                "currentCounter");
        assertEquals(info.getOperations()[0].getSignature().length, 0);
        assertEquals(info.getOperations()[0].getReturnType(), "int");

        // Assert the attributes.
        assertNotNull(info.getAttributes());
        // we also have a Even as a Attribute
        assertEquals(info.getAttributes().length, 2);

        // Asserts for Counter.
        assertEquals(info.getAttributes()[0].getName(), "Counter");
        assertEquals(info.getAttributes()[0].getDescription(), "Counter");
        assertEquals(info.getAttributes()[0].getType(), "int");
        // Asserts for Even.
        assertEquals(info.getAttributes()[1].getName(), "Even");
        assertEquals(info.getAttributes()[1].getDescription(), "Even");
        assertEquals(info.getAttributes()[1].getType(), "boolean");

        MBeanServerConnection mbs = getMBeanServerConnection();
        DripsAspectInfo apectInfo = getDripsMainMBean().getAspectInfo(
                aptclass.getName());
        assertEquals(apectInfo.getDescription(), aptdescription);
        assertEquals(apectInfo.getMBeanInerfaceName(), AptAspect3Inf.class
                .getName());
        assertEquals(apectInfo.getName(), aptclass.getName());
        assertEquals(apectInfo.getObjectName(),
                "com.bea.drips.framework:type=" + aptclass.getName());
        ObjectName aspectObjectName = new ObjectName(apectInfo
                .getObjectName());
        String[] signature = new String[0];

        Object ob = mbs.invoke(aspectObjectName, info.getOperations()[0]
                .getName(), new Object[0], signature);
        assertNotNull(ob);
        assertNotNull(ob);
        assertEquals(((Integer) ob).intValue(), 1);
        // Second call
        ob = mbs.invoke(aspectObjectName, info.getOperations()[0].getName(),
                new Object[0], signature);
        assertNotNull(ob);
        assertEquals(((Integer) ob).intValue(), 2);

        // Use proxy invocation.
        AptAspect3Inf aptimpl = (AptAspect3Inf) MBeanServerInvocationHandler
                .newProxyInstance(mbs, aspectObjectName, AptAspect3Inf.class,
                        false);
        assertEquals(aptimpl.currentCounter(), 3);

        // Calls to the Attributes.
        ob = mbs.getAttribute(aspectObjectName, info.getAttributes()[0]
                .getName());
        assertNotNull(ob);
        assertEquals(((Integer) ob).intValue(), 4);

        // Again call Attribute, same value should be returned,
        // using proxy invocation.
        assertEquals(aptimpl.getCounter(), 4);

        // Call setter on the Attribute.
        Attribute attr = new Attribute("Counter", 10);
        mbs.setAttribute(aspectObjectName, attr);

        // Again call Attribute, we should get 10 as value
        assertEquals(aptimpl.getCounter(), 10);

        // Call setter using Proxy
        aptimpl.setCounter(14);
        // Check value
        assertEquals(aptimpl.getCounter(), 14);

        // Now let us test the isEven method.
        assertFalse(aptimpl.isEven());

        // Set to True
        aptimpl.setEven(true);

        // Now test for true
        assertTrue(aptimpl.isEven());
    }

    public void testAptAspect4() throws Exception {
        Class aptclass = AptAspect4.class;
        Class aptmbeanclass = org.drips.framework.apt.AptAspect4Inf.class;
        String aptdescription = "AptAspect4";
        MBeanInfo info = enableAspectandGetInfo(aptclass);
        assertNotNull(info);
        assertEquals(info.getClassName(), aptmbeanclass.getName());
        // FIXME: Description is not preserved.
        // assertEquals(info.getDescription(), aptdescription);
        assertNotNull(info.getOperations());
        assertEquals(info.getOperations().length, 1);
        assertEquals(info.getOperations()[0].getName(), "currentCounter");
        // assertEquals(info.getOperations()[0].getDescription(), "Counter");
        assertEquals(info.getOperations()[0].getSignature().length, 0);
        assertEquals(info.getOperations()[0].getReturnType(), "int");
        MBeanServerConnection mbs = getMBeanServerConnection();
        DripsAspectInfo apectInfo = getDripsMainMBean().getAspectInfo(
                aptclass.getName());
        assertEquals(apectInfo.getDescription(), aptdescription);
        assertEquals(apectInfo.getMBeanInerfaceName(),
                AptAspect4InfMBean.class.getName());
        assertEquals(apectInfo.getName(), aptmbeanclass.getName());
        assertEquals(apectInfo.getObjectName(),
                "com.bea.drips.framework:type=" + aptmbeanclass.getName());
        ObjectName aspectObjectName = new ObjectName(apectInfo
                .getObjectName());
        String[] signature = new String[0];

        Object ob = mbs.invoke(aspectObjectName, info.getOperations()[0]
                .getName(), new Object[0], signature);
        assertNotNull(ob);
        assertNotNull(ob);
        assertEquals(((Integer) ob).intValue(), 1);
        // Second call
        ob = mbs.invoke(aspectObjectName, info.getOperations()[0].getName(),
                new Object[0], signature);
        assertNotNull(ob);
        assertEquals(((Integer) ob).intValue(), 2);

        // Use proxy invocation.
        AptAspect4InfMBean aptimpl = (AptAspect4InfMBean) MBeanServerInvocationHandler
                .newProxyInstance(mbs, aspectObjectName,
                        AptAspect4InfMBean.class, false);
        assertEquals(aptimpl.currentCounter(), 3);
    }

    public void testAptAspect5() throws Exception {
        Class aptclass = AptAspect5.class;
        MBeanInfo info = enableAspectandGetInfo(aptclass);
        MBeanServerConnection mbs = getMBeanServerConnection();
        DripsAspectInfo aspectinfo = getDripsMainMBean().getAspectInfo(
                aptclass.getName());

        AptAspect5Inf aptimpl = (AptAspect5Inf) MBeanServerInvocationHandler
                .newProxyInstance(mbs, new ObjectName(aspectinfo
                        .getObjectName()), AptAspect5Inf.class, false);
        
        aptimpl.deploy();
        aptimpl.undeploy();
    }
}
