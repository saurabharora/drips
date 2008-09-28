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

package org.drips.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintStream;
import java.lang.management.ManagementFactory;
import java.util.Enumeration;

import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.swing.tree.DefaultMutableTreeNode;

import org.codehaus.aspectwerkz.joinpoint.JoinPoint;
import org.codehaus.aspectwerkz.joinpoint.MemberSignature;

import sun.rmi.registry.RegistryImpl;

/**
 * Utils Class
 * 
 * @author Prasen Mukherjee
 */
public class Utils {

    private static PrintStream printStream;

    static {
        String logFileName = System.getProperty("LOG_FILE");
        if (logFileName != null) {
            try {
                printStream = new PrintStream(new FileOutputStream(
                        logFileName));
            } catch (FileNotFoundException e) {
                e.printStackTrace(); // To change body of catch statement
                // use File | Settings | File
                // Templates.
            }
        }
    }

    public static void debug(final String msg) {
        debug("LOG: ", msg);
    }

    public static void debug(final String prefix, final String msg) {
        String logmsg = prefix + msg;
        if (printStream != null) {
            printStream.println(logmsg);
            printStream.flush();
        }
        System.out.println(logmsg);
    }

    public static String getRMIRegistryPort() {
        return System.getProperty("drips.rmiregistry.port", "9999");
    }

    // URL : service:jmx:rmi:///jndi/rmi://localhost:9999/dripsserver
    public static String getJMXRMIServiceURL() {
        String rmiregistryPort = getRMIRegistryPort();
        String rmiregistryHost = System.getProperty("drips.rmiregistry.host",
                "localhost");
        String rmiUrl = "rmi://" + rmiregistryHost + ":" + rmiregistryPort
                + "/dripsserver";
        String serviceURL = "service:jmx:rmi:///jndi/" + rmiUrl;
        return serviceURL;
    }

    public static MBeanServerConnection getMBeanServerConnectionForJMXClient()
            throws IOException {
        JMXServiceURL jmxURL = new JMXServiceURL(getJMXRMIServiceURL());
        JMXConnector _jmxConnector = JMXConnectorFactory.connect(jmxURL);
        return _jmxConnector.getMBeanServerConnection();
    }

    public static MBeanServer getLocalMBeanServer() {
        return ManagementFactory.getPlatformMBeanServer();
    }

    public static Process startRmiRegistry(final String rmiregistry_port)
            throws IOException {
        /*
         * String jre_bin_directory =
         * System.getProperty("sun.boot.library.path"); String
         * rmiregistry_path = jre_bin_directory+File.separator+"rmiregistry";
         * String exec_command = rmiregistry_path + " "+rmiregistry_port;
         * Process rmiregistryProcess =
         * Runtime.getRuntime().exec(exec_command); debug("Rmi Registry
         * started with command "+exec_command); return rmiregistryProcess;
         */
        new RegistryImpl(Integer.parseInt(rmiregistry_port));
        return null;
    }

    public static void stopRmiRegistry(final Process rmiregistryProcess) {
        Utils.debug("Going to kill local rmiregistry ");
        rmiregistryProcess.destroy();
        try {
            rmiregistryProcess.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace(); // To change body of catch statement use
            // File | Settings | File Templates.
        }
        Utils.debug("Rmiregsitry process exited with value : "
                + rmiregistryProcess.exitValue());
    }

    public static String getStringFromCommandLine() {
        LineNumberReader reader = new LineNumberReader(new InputStreamReader(
                System.in));
        try {
            System.out.print("waiting for input:  ");
            return reader.readLine();
        } catch (IOException e) {
            e.printStackTrace(); // To change body of catch statement use
            // File | Settings | File Templates.
        }
        return null;
    }

    public static String getSignatureString(final JoinPoint joinPoint) {
        MemberSignature signature = (MemberSignature) joinPoint
                .getSignature();
        return signature.getDeclaringType().getName() + "::"
                + signature.getName();
    }

    public static void printTree(final DefaultMutableTreeNode tree,
            final String indent) {
        debug("LOG: \n", treetoString(tree, indent));
    }

    public static String treetoString(final DefaultMutableTreeNode tree,
            final String indent) {
        StringBuffer sbuf = new StringBuffer(indent + tree.getUserObject()
                + "\n");
        // System.out.println(indent + tree.getUserObject());
        Enumeration enumerator = tree.children();
        while (enumerator.hasMoreElements()) {
            DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) enumerator
                    .nextElement();
            sbuf.append(treetoString(treeNode, indent + "  "));
        }
        return sbuf.toString();
    }

}
