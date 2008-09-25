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
package org.drips.test.j2eeaspect;


import javax.management.NotificationListener;
import javax.management.Notification;
import javax.management.ReflectionException;
import javax.management.InstanceNotFoundException;
import javax.management.MalformedObjectNameException;
import javax.management.IntrospectionException;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.swing.tree.DefaultMutableTreeNode;

import org.drips.framework.aspects.testtomcataspect.*;
import org.drips.framework.aspects.timing.BroadcastingTimingAspect;
import org.drips.framework.aspects.timing.BroadcastingTimingAspectMBean;
import org.drips.framework.aspectwerkx.DripsAspectInfo;
import org.drips.framework.mbeans.DripsMainMBean;
import org.drips.framework.test.AbstractDripsBaseTest;
import org.drips.utils.Utils;

import java.io.IOException;

/**
 * @author Prasen Mukherjee
 */
public class TestJ2EEAspect extends AbstractDripsBaseTest {
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
/*
        String aspectExpression = "execution(public void ..WordCounter.*(..))";
        String adviceName = "doGenericMethodProfiling";
        String adviceType = "around";
        timingAspectMBean.deploy(aspectExpression,adviceName, adviceType);
*/
        String aspectExpression = "execution(public void cal..*.*(..)) OR execution(public * org.apache.jsp.cal..*.*(..))";
        timingAspectMBean.doProfiling(aspectExpression);

        MBeanServerConnection mBeanServerConnection = getMBeanServerConnection();
        DripsAspectInfo apectInfo = getDripsMainMBean().getAspectInfo(BroadcastingTimingAspect.class.getName());
        assertNotNull(apectInfo);
        ObjectName aspectObjectName = new ObjectName(apectInfo.getObjectName());

        timingAspectMBean.addNotificationListener(new Listener(), null,"MYHANDBACK");

        //printMBeanInfo(mBeanServerConnection, aspectObjectName,BroadcastingTimingAspect.class.getName());

        System.out.println("Waiting for notification...");
        Thread.sleep(50000);
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
