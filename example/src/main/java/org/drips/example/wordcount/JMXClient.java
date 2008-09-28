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
package org.drips.example.wordcount;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;


public class JMXClient {
    public static void main(String[] args) {
        try {
            MBeanServerConnection mbc = getMBeanServerConnection();
            ObjectName name = new ObjectName(
                    "com.bea.drips.framework:type=DripsMain");
            mbc.invoke(name, "disable", new Object[] {}, null);
            System.out.println("state = " + mbc.getAttribute(name, "State"));
            mbc.invoke(name, "enable", new Object[] {}, null);
            System.out.println("state = " + mbc.getAttribute(name, "State"));
        } catch (IOException e) {
            e.printStackTrace(); // To change body of catch statement use
                                    // File | Settings | File Templates.
        } catch (MalformedObjectNameException e) {
            e.printStackTrace(); // To change body of catch statement use
                                    // File | Settings | File Templates.
        } catch (ReflectionException e) {
            e.printStackTrace(); // To change body of catch statement use
                                    // File | Settings | File Templates.
        } catch (InstanceNotFoundException e) {
            e.printStackTrace(); // To change body of catch statement use
                                    // File | Settings | File Templates.
        } catch (MBeanException e) {
            e.printStackTrace(); // To change body of catch statement use
                                    // File | Settings | File Templates.
        } catch (AttributeNotFoundException e) {
            e.printStackTrace(); // To change body of catch statement use
                                    // File | Settings | File Templates.
        }

    }

    private static MBeanServerConnection getMBeanServerConnection()
            throws IOException {
        String rmi_url = System.getProperty("drips.rmi.url",
                "rmi://localhost:9999/server");
        String serviceURL = "service:jmx:rmi:///jndi/" + rmi_url;

        JMXServiceURL jmxURL = new JMXServiceURL(serviceURL);
        JMXConnector _jmxConnector = JMXConnectorFactory.connect(jmxURL);
        return _jmxConnector.getMBeanServerConnection();
    }

}
