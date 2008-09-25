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
package org.drips.framework.aspects.broadcastingaspect;


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
import org.drips.framework.test.AbstractDripsBaseTest;
import org.drips.utils.Utils;

import java.io.IOException;

/**
 * @author Prasen Mukherjee
 */
public class TestBroadcastingAspect extends AbstractDripsBaseTest{
    protected boolean isLocalJVM() {
        return false;
    }

    public void testDoDeployPointcut()
            throws 
            ReflectionException, InstanceNotFoundException, IOException,
            ClassNotFoundException,MalformedObjectNameException, IntrospectionException,
            InterruptedException {
        MyBroadcastingAspectMBean broadcastingAspectMBean = (MyBroadcastingAspectMBean) enableAspect(MyBroadcastingAspect.class);
        broadcastingAspectMBean.enableMyAspect();
/*

        DripsAspectInfo apectInfo = getDripsMainMBean().getAspectInfo(MyBroadcastingAspect.class.getName());
        assertNotNull(apectInfo);
        ObjectName aspectObjectName = new ObjectName(apectInfo
                .getObjectName());

*/
        broadcastingAspectMBean.addNotificationListener(new Listener(), null,"MYHANDBACK");

        System.out.println("Waiting for notification...");
        Thread.sleep(50000);
    }

}

class Listener implements NotificationListener {

    public void handleNotification(Notification notification, Object handback) {
        System.err.println("RECEIVED notification source class"
                + notification.getSource().getClass());
        System.out.println("RECEIVED notification = " + notification.getSource());
        System.err.println("handback = " + handback);
        // System.out.println("handback = " + handback);
    }

}

