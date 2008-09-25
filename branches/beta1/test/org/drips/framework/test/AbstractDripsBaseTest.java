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
package org.drips.framework.test;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.Iterator;
import java.util.Set;

import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.ReflectionException;

import org.drips.framework.aspectwerkx.DripsAspect;
import org.drips.framework.aspectwerkx.DripsAspectInfo;
import org.drips.framework.mbeans.DripsMainMBean;
import org.drips.utils.Utils;

import junit.framework.TestCase;


/**
 * @author saurabh TODO To change the template for this generated type comment
 *         go to Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class AbstractDripsBaseTest extends TestCase {

    private boolean isLocalJVM = true;

    /**
     * 
     */
    public AbstractDripsBaseTest() {
        super();
        initLocalJVM();
    }

    /**
     * @param arg0
     */
    public AbstractDripsBaseTest(final String arg0) {
        super(arg0);
        initLocalJVM();
    }

    protected void setUp() throws Exception {
        super.setUp();
        if (!isLocalJVM())
            getDripsMainMBean().enable();
    }

    protected void tearDown() throws Exception {
        super.tearDown();

    }

    /**
     * @return Returns the isLocalJVM.
     */
    protected boolean isLocalJVM() {
        return isLocalJVM;
    }

    protected MBeanServerConnection getMBeanServerConnection()
            throws IOException {
        if (isLocalJVM()) {
            return ManagementFactory.getPlatformMBeanServer();
        } else {
            return Utils.getMBeanServerConnectionForJMXClient();
        }
    }

    private void initLocalJVM() {
        isLocalJVM = !(Boolean.parseBoolean(System.getProperty(
                "drips.remotejvmtest", "false")));
        if (!isLocalJVM()) {

            System.out.println("This is a Remote JVM Tests");
        } else {
            System.out.println("This is a Local JVM Tests");
            System.setProperty("drips.rmiregistry.notrequired", "true");
        }
    }

    protected DripsMainMBean getDripsMainMBean() throws IOException,
            MalformedObjectNameException {
        ObjectName name = new ObjectName(
                DripsMainMBean.DRIPS_MBEAN_OBJECTNAME);
        assertTrue(getMBeanServerConnection().isRegistered(name));
        DripsMainMBean mbean = (DripsMainMBean) MBeanServerInvocationHandler
                .newProxyInstance(getMBeanServerConnection(), name,
                        DripsMainMBean.class, false);
        return mbean;
    }

    protected DripsAspect enableAspect(Class aspectClass)
            throws MalformedObjectNameException, IOException,
            InstanceNotFoundException, ClassNotFoundException,
            ReflectionException, IntrospectionException {
        DripsMainMBean dripsMainMBean = getDripsMainMBean();
        String aspectClassName = aspectClass.getName();

        if (!dripsMainMBean.isAspectEnabled(aspectClassName))
            dripsMainMBean.enableAspect(aspectClassName);
        DripsAspectInfo apectInfo = dripsMainMBean.getAspectInfo(aspectClass
                .getName());
        assertNotNull(apectInfo);

        //assertTrue(dripsMainMBean.isAspectEnabled(aspectClassName));

        return getAspectMBean(aspectClass);

    }

    protected void disableAspect(Class aspectClass) throws IOException,
            MalformedObjectNameException {
        getDripsMainMBean().disableAspect(aspectClass.getName());
    }

    protected DripsAspect getAspectMBean(Class aspectClass)
            throws IOException, MalformedObjectNameException,
            InstanceNotFoundException, ReflectionException,
            IntrospectionException, ClassNotFoundException {
        MBeanServerConnection mBeanServerConnection = getMBeanServerConnection();

        DripsAspectInfo apectInfo = getDripsMainMBean().getAspectInfo(
                aspectClass.getName());
        assertNotNull(apectInfo);

        DripsAspect aspectMBean = null;
        ObjectName aspectObjectName = new ObjectName(apectInfo
                .getObjectName());
        if (mBeanServerConnection.isRegistered(aspectObjectName)) {
            Class mbeanClass = Class
                    .forName(apectInfo.getMBeanInerfaceName());
            aspectMBean = (DripsAspect) MBeanServerInvocationHandler
                    .newProxyInstance(mBeanServerConnection,
                            aspectObjectName, mbeanClass, false);
        }
        return aspectMBean;

    }

    // Utility method
    protected static void printAllMbeans(MBeanServerConnection mbs)
            throws MalformedObjectNameException, IOException {
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
        Set data = mbs.queryMBeans(null, null);
        if (!data.isEmpty()) {
            System.out.println("Get all mbeans");
            Iterator iter = data.iterator();
            while (iter.hasNext()) {
                ObjectInstance st = (ObjectInstance) iter.next();
                System.out.println("Mbean =" + st.getObjectName()
                        + "  ImplClass: " + st.getClassName());
            }
        }

    }

}
