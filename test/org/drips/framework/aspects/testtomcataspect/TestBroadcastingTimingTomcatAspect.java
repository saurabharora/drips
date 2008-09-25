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
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.swing.tree.DefaultMutableTreeNode;

import org.drips.framework.aspects.timing.BroadcastingTimingAspect;
import org.drips.framework.aspects.timing.BroadcastingTimingAspectMBean;
import org.drips.framework.aspectwerkx.DripsAspectInfo;
import org.drips.framework.mbeans.DripsMainMBean;
import org.drips.framework.test.AbstractDripsBaseTest;
import org.drips.utils.Utils;


/**
 * @author Prasen Mukherjee
 */
public class TestBroadcastingTimingTomcatAspect extends AbstractDripsBaseTest {
    DripsMainMBean dripsMainMBean;

    protected void setUp() throws Exception {
        dripsMainMBean = getDripsMainMBean();
    }

    protected boolean isLocalJVM() {
        return false;
    }

    public void testDoDeployPointcut() throws ReflectionException,
            InstanceNotFoundException, IOException, ClassNotFoundException,
            MalformedObjectNameException, IntrospectionException,
            InterruptedException {
        BroadcastingTimingAspectMBean timingAspectMBean = (BroadcastingTimingAspectMBean) enableAspect(BroadcastingTimingAspect.class);
        String aspectExpression = "execution(public void ..WordCounter.*(..))";// "execution(public
                                                                                // void
                                                                                // HelloWorldExample.*(..))";
        String adviceName = "doGenericMethodProfiling";
        String adviceType = "around";
        timingAspectMBean.deploy(aspectExpression,
                adviceName, adviceType);

        MBeanServerConnection mBeanServerConnection = getMBeanServerConnection();
        DripsAspectInfo apectInfo = getDripsMainMBean().getAspectInfo(
                BroadcastingTimingAspect.class.getName());
        assertNotNull(apectInfo);
        ObjectName aspectObjectName = new ObjectName(apectInfo
                .getObjectName());

        timingAspectMBean.addNotificationListener(new Listener(), null,
                "MYHANDBACK");

        printMBeanInfo(mBeanServerConnection, aspectObjectName,
                BroadcastingTimingAspect.class.getName());

        System.out.println("Waiting for notification...");
        Thread.sleep(50000);
    }

    public void testDoUnDeployPointcut() throws ReflectionException,
            InstanceNotFoundException, IOException, ClassNotFoundException,
            MalformedObjectNameException, IntrospectionException {
        BroadcastingTimingAspectMBean timingAspectMBean = (BroadcastingTimingAspectMBean) enableAspect(BroadcastingTimingAspect.class);
        timingAspectMBean.undeploy();
        dripsMainMBean
                .disableAspect(BroadcastingTimingAspect.class.getName());
        timingAspectMBean = (BroadcastingTimingAspectMBean) getAspectMBean(BroadcastingTimingAspect.class);
        assertTrue(timingAspectMBean == null);

    }

    public void testGetAllExecutionProfileTrees() throws ReflectionException,
            InstanceNotFoundException, IOException, ClassNotFoundException,
            MalformedObjectNameException, IntrospectionException {
        BroadcastingTimingAspectMBean timingAspectMBean = (BroadcastingTimingAspectMBean) enableAspect(BroadcastingTimingAspect.class);
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

    private static void printMBeanInfo(MBeanServerConnection mbs,
            ObjectName mbeanObjectName, String mbeanClassName) {
        echo("\n>>> Retrieve the management information for the "
                + mbeanClassName);
        echo("    MBean using the getMBeanInfo() method of the MBeanServer");
        MBeanInfo info = null;
        try {
            info = mbs.getMBeanInfo(mbeanObjectName);
        } catch (Exception e) {
            echo("\t!!! Could not get MBeanInfo object for " + mbeanClassName
                    + " !!!");
            e.printStackTrace();
            return;
        }
        echo("\nCLASSNAME: \t" + info.getClassName());
        echo("\nDESCRIPTION: \t" + info.getDescription());
        echo("\nATTRIBUTES");
        MBeanAttributeInfo[] attrInfo = info.getAttributes();
        if (attrInfo.length > 0) {
            for (int i = 0; i < attrInfo.length; i++) {
                echo(" ** NAME: \t" + attrInfo[i].getName());
                echo("    DESCR: \t" + attrInfo[i].getDescription());
                echo("    TYPE: \t" + attrInfo[i].getType() + "\tREAD: "
                        + attrInfo[i].isReadable() + "\tWRITE: "
                        + attrInfo[i].isWritable());
            }
        } else
            echo(" ** No attributes **");
        echo("\nCONSTRUCTORS");
        MBeanConstructorInfo[] constrInfo = info.getConstructors();
        for (int i = 0; i < constrInfo.length; i++) {
            echo(" ** NAME: \t" + constrInfo[i].getName());
            echo("    DESCR: \t" + constrInfo[i].getDescription());
            echo("    PARAM: \t" + constrInfo[i].getSignature().length
                    + " parameter(s)");
        }
        echo("\nOPERATIONS");
        MBeanOperationInfo[] opInfo = info.getOperations();
        if (opInfo.length > 0) {
            for (int i = 0; i < opInfo.length; i++) {
                echo(" ** NAME: \t" + opInfo[i].getName());
                echo("    DESCR: \t" + opInfo[i].getDescription());
                echo("    PARAM: \t" + opInfo[i].getSignature().length
                        + " parameter(s)");
            }
        } else
            echo(" ** No operations ** ");
        echo("\nNOTIFICATIONS");
        MBeanNotificationInfo[] notifInfo = info.getNotifications();
        if (notifInfo.length > 0) {
            for (int i = 0; i < notifInfo.length; i++) {
                echo(" ** NAME: \t" + notifInfo[i].getName());
                echo("    DESCR: \t" + notifInfo[i].getDescription());
                String notifTypes[] = notifInfo[i].getNotifTypes();
                for (int j = 0; j < notifTypes.length; j++) {
                    echo("    TYPE: \t" + notifTypes[j]);
                }
            }
        } else
            echo(" ** No notifications **");
    }

    private static void echo(String msg) {
        System.out.println(msg);
    }

}

class Listener implements NotificationListener {

    public void handleNotification(Notification notification, Object handback) {
        System.err.println("RECEIVED notification source "
                + notification.getSource().getClass());
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) notification
                .getSource();
        Utils.printTree(node, " ");
        System.err.println("handback = " + handback);
        // System.out.println("RECEIVED notification = " + notification);
        // System.out.println("handback = " + handback);
    }

}
