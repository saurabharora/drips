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

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;

import org.drips.framework.aspectwerkx.DripsAspectInfo;
import org.drips.framework.main.DripsMain;
import org.drips.framework.mbeans.DripsMainMBean;
import org.drips.framework.test.AbstractDripsBaseTest;


/**
 * @author saurabh TODO To change the template for this generated type comment
 *         go to Window - Preferences - Java - Code Style - Code Templates
 */
public class DripsMainTest extends AbstractDripsBaseTest {

    private DripsMain m = new DripsMain();

    private MBeanServerConnection mbs;

    private ObjectName name = null;

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(DripsMainTest.class);
    }

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        System.out.println("DripsMainTest Called");
        super.setUp();
        super.setUpWordCounter(m);

        mbs = getMBeanServerConnection();
        name = new ObjectName("com.bea.drips.framework:type=DripsMain");
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        m.shutdown();
    }

    public void testGetState() throws Exception {
        setUpMbean();
        // printAllMbeans();
        assertTrue(mbs.isRegistered(name));

        Boolean state = (Boolean) mbs.getAttribute(name, "State");
        assertTrue("State should be false intially", !state.booleanValue());

    }

    public void testEnable() {
        // In this test, we create a proxy instance and invoke
        // methods on that proxy.
        DripsMainMBean mbean = (DripsMainMBean) MBeanServerInvocationHandler
                .newProxyInstance(mbs, name, DripsMainMBean.class, false);
        assertTrue("State should be false at startup", !mbean.getState());

        mbean.enable();
        System.err.println("Drips enabled");
        mbean.disable();

    }

public void testRegister() {
        // In this test, we create a proxy instance and invoke
        // methods on that proxy.
        DripsMainMBean mbean = (DripsMainMBean) MBeanServerInvocationHandler
                .newProxyInstance(mbs, name, DripsMainMBean.class, false);
        assertTrue("State should be false at startup", !mbean.getState());

        mbean.enable();

        mbean.enableAspect(TestAspect.class
                .getName());
        mbean.getAspectInfo(TestAspect.class
                .getName());

        String[] args = mbean.getAspects();
        // We have only one Aspect in the System.
        assertEquals(args.length, 1);
        for (int i = 0; i < args.length; i++) {
            System.out.println("Data=" + args[i]);
            assertEquals(TestAspect.class.getName(), args[i]);
        }
        System.err.println("Drips enabled");
        mbean.disable();
    }    public void testAspectInfo() {
        // In this test, we create a proxy instance and invoke
        // methods on that proxy.
        DripsMainMBean mbean = (DripsMainMBean) MBeanServerInvocationHandler
                .newProxyInstance(mbs, name, DripsMainMBean.class, false);
        assertTrue("State should be false at startup", !mbean.getState());

        mbean.enable();
        mbean.enableAspect(TestAspect.class.getName());

        String[] args = mbean.getAspects();
        if (args.length > 0) {
            DripsAspectInfo info = mbean.getAspectInfo(TestAspect.class
                    .getName());
            assertNotNull(info);
            assertNotNull(info.getDescription());
            assertNotNull(info.getName());
            System.out.println("Description=" + info.getDescription());
            System.out.println("Name=" + info.getName());
        }
        mbean.disable();
    }

    public void testAspectInfos() {
        // In this test, we create a proxy instance and invoke
        // methods on that proxy.
        DripsMainMBean mbean = (DripsMainMBean) MBeanServerInvocationHandler
                .newProxyInstance(mbs, name, DripsMainMBean.class, false);
        assertTrue("State should be false at startup", !mbean.getState());

        mbean.enable();
        mbean.enableAspect(TestAspect.class.getName());
        String[] args = mbean.getAspects();
        if (args.length > 0) {
            DripsAspectInfo[] infos = mbean.getAspectInfos();
            assertEquals(infos.length, 1);
            assertNotNull(infos[0]);
            assertNotNull(infos[0].getDescription());
            assertNotNull(infos[0].getName());
        }
        mbean.disable();
    }

    public void testEnableAspect() throws Throwable {

        // In this test, we create a proxy instance and invoke
        // methods on that proxy.
        DripsMainMBean mbean = (DripsMainMBean) MBeanServerInvocationHandler
                .newProxyInstance(mbs, name, DripsMainMBean.class, false);
        assertTrue("State should be false at startup", !mbean.getState());

        mbean.enable();
        mbean.enableAspect(TestAspect.class.getName());
        DripsAspectInfo info1 = mbean.getAspectInfo(TestAspect.class
                .getName());
        assertNotNull(info1);
        assertNotNull(info1.getDescription());
        assertNotNull(info1.getName());
        ObjectName name1 = new ObjectName(info1.getObjectName());
        assertTrue(mbs.isRegistered(name1));
        TestMainMBean mbean1 = (TestMainMBean) MBeanServerInvocationHandler
                .newProxyInstance(mbs, name1, TestMainMBean.class, false);
        printAllMbeans();
        assertEquals(mbean1.testmethod(), 0);
        mbean.disableAspect(TestAspect.class.getName());
        mbean.disable();
    }

    public void testDisableAspect() throws Throwable {
        // In this test, we create a proxy instance and invoke
        // methods on that proxy.
        DripsMainMBean mbean = (DripsMainMBean) MBeanServerInvocationHandler
                .newProxyInstance(mbs, name, DripsMainMBean.class, false);
        assertTrue("State should be false at startup", !mbean.getState());

        mbean.enable();
        mbean.enableAspect(TestAspect.class.getName());
        DripsAspectInfo info1 = mbean.getAspectInfo(TestAspect.class
                .getName());
        assertNotNull(info1);
        assertNotNull(info1.getDescription());
        assertNotNull(info1.getName());
        ObjectName name1 = new ObjectName(info1.getObjectName());
        assertTrue(mbs.isRegistered(name1));
        mbean.disableAspect(TestAspect.class.getName());
        // I have disable the mbean, not it should not lookup.
        assertTrue(!mbs.isRegistered(name1));
        mbean.disable();
    }

    public void testDisableDrips() throws Throwable {

        // In this test, we create a proxy instance and invoke
        // methods on that proxy.
        DripsMainMBean mbean = (DripsMainMBean) MBeanServerInvocationHandler
                .newProxyInstance(mbs, name, DripsMainMBean.class, false);
        assertTrue("State should be false at startup", !mbean.getState());

        mbean.enable();
        mbean.enableAspect(TestAspect.class.getName());
        DripsAspectInfo info1 = mbean.getAspectInfo(TestAspect.class
                .getName());
        assertNotNull(info1);
        assertNotNull(info1.getDescription());
        assertNotNull(info1.getName());
        ObjectName name1 = new ObjectName(info1.getObjectName());
//        TestMainMBean mbean1 = (TestMainMBean) MBeanServerInvocationHandler
//                .newProxyInstance(mbs, name1, TestMainMBean.class, false);
        // printAllMbeans();
        // System.out.flush();
        // System.out.println("====================");

        mbean.disable();
        // printAllMbeans();
        // System.out.flush();

        // Now we have disable Drips , try to lookup mbeam again,
        // It should fail. disable() should remove all mbeans from the
        // system.
        assertTrue(!mbs.isRegistered(name1));

    }

    private void setUpMbean() {
        // Nothing to do in case of in same JVM case
        // in case of separate VM case, we need to create a
        // the mbean and register the same.

    }

    private void printAllMbeans() throws MalformedObjectNameException,
            IOException {
        System.out.println("Get all mbeans count=" + mbs.getMBeanCount());
        System.out.println("Get Default mbeans Domain="
                + mbs.getDefaultDomain());
        String domains[] = mbs.getDomains();
        if (domains.length > 0) {
            System.out.println("Get All Domain");
            for (int i = 0; i < domains.length; i++) {
                System.out.println("Domain[" + i + "]=" + domains[i]);
            }
        }
        // Unfortunately mbeans is still using pre generics code.
        Set<?> data = mbs.queryMBeans(null, null);
        if (!data.isEmpty()) {
            System.out.println("Get all mbeans");
            Iterator<?> iter = data.iterator();
            while (iter.hasNext()) {
                ObjectInstance st = (ObjectInstance) iter.next();
                System.out.println("Mbean =" + st.getClassName());
            }
        }

    }
}
