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

import java.io.IOException;
import java.lang.management.ManagementFactory;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

import org.drips.framework.mbeans.DripsMain;
import org.drips.utils.Utils;


/**
 * This Base Class for Drips BootStrap classes DripsMain and
 * DripsBootAspect.It contains all the functionality required to boot the
 * Drips Framework.
 * 
 * @author Saurabh Arora, Prasen Mukherjee
 */
class DripsBaseBootstrap {

    private static boolean DEBUG = true;

    private boolean intialized = false;

    private DripsMain mbean = null;

    private Process rmiregistryProcess;
    private JMXConnectorServer jmxConnectorServer;

    protected boolean intializeDrips() {

        addShutDownHook();
        try {
            intializeMBeans();
        } catch (MalformedObjectNameException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } catch (InstanceAlreadyExistsException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } catch (MBeanRegistrationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } catch (NotCompliantMBeanException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return false;
        }
        intialized = true;
        return true;
    }

    private void intializeMBeans() throws MalformedObjectNameException,
                                          InstanceAlreadyExistsException, MBeanRegistrationException,
                                          NotCompliantMBeanException, IOException {
        // Get the Platform MBean Server
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

        // Construct the ObjectName for the MBean we will register
        ObjectName name;

        name = new ObjectName("com.bea.drips.framework:type=DripsMain");

        // Create the Drips DripsMain MBean
        mbean = new DripsMain();

        // Register the Drips DripsMain MBean
        mbs.registerMBean(getMbean(), name);
        if (DEBUG) Utils.debug("Register Mbean successfull");

        // Start the RMI connector server
        if (!Boolean.getBoolean("drips.rmiregistry.notrequired")) startJMXConnectorServer(mbs);

    }

    private void startJMXConnectorServer(MBeanServer mbs) throws IOException {

        if (DEBUG) Utils.debug("Create an RMI connector server");

        JMXServiceURL url = new JMXServiceURL(Utils.getJMXRMIServiceURL());
        jmxConnectorServer = JMXConnectorServerFactory.newJMXConnectorServer(url, null, mbs);



        // Start the RMI connector server
        //
        if (DEBUG) Utils.debug("Start the RMI connector server");

        try {
            jmxConnectorServer.start();
        } catch (IOException e) {
            rmiregistryProcess = Utils.startRmiRegistry(Utils.getRMIRegistryPort());
            jmxConnectorServer.start();
        }

        if (DEBUG) Utils.debug("The RMI connector server successfully started");
        if (DEBUG) Utils.debug("and is ready to handle incoming connections at " +Utils.getJMXRMIServiceURL());

    }

    private void addShutDownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {

                if (rmiregistryProcess!=null) {
                    Utils.stopRmiRegistry(rmiregistryProcess);
                } else {
                    try {
                        if (jmxConnectorServer!=null) jmxConnectorServer.stop();
                    } catch (IOException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
            }
        });
        if (DEBUG) Utils.debug("Adding shutdown hook");
    }

    protected void shutdownDrips() {
        if (!intialized)
            return;
        getMbean().disable();
        shutdownMBeans();
    }

    private void shutdownMBeans() {
        // TODO Auto-generated method stub
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

        // Construct the ObjectName for the MBean we will register
        ObjectName name;

        try {
            name = new ObjectName("com.bea.drips.framework:type=DripsMain");
            mbs.unregisterMBean(name);
            if (DEBUG) Utils.debug("UnRegister Mbean successfull");

        } catch (MalformedObjectNameException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NullPointerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InstanceNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MBeanRegistrationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Register the Drips DripsMain MBean

    }

    /**
     * @return Returns the mbean.
     */
    protected DripsMain getMbean() {
        return mbean;
    }

}
