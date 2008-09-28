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
package org.drips.framework.aspects.timing;

import javax.management.ListenerNotFoundException;
import javax.management.MBeanNotificationInfo;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.swing.tree.DefaultMutableTreeNode;

import org.codehaus.aspectwerkz.joinpoint.JoinPoint;
import org.drips.framework.profiler.ProfilerProcessor.MyDefaultMutableTreeNode;


/**
 * @author Prasen Mukherjee
 */
public class BroadcastingTimingAspect extends TimingAspect implements
        BroadcastingTimingAspectMBean {

    // This has to be static, otherwise notifications are called on a
    // different object than the one where they got added
    private static final NotificationBroadcasterSupport singleton = new NotificationBroadcasterSupport();

    public void removeNotificationListener(NotificationListener listener,
            NotificationFilter filter, Object handback)
            throws ListenerNotFoundException {
        singleton.removeNotificationListener(listener, filter, handback);
    }

    public void addNotificationListener(NotificationListener listener,
            NotificationFilter filter, Object handback)
            throws IllegalArgumentException {
        singleton.addNotificationListener(listener, filter, handback);
    }

    public void removeNotificationListener(NotificationListener listener)
            throws ListenerNotFoundException {
        singleton.removeNotificationListener(listener);
    }

    private final static String EXEC_TREE_ADDED_NOTIF_TYPE = "ExecutionTreeAddedNotification";

    public MBeanNotificationInfo[] getNotificationInfo() {
        return new MBeanNotificationInfo[]
            { new MBeanNotificationInfo(new String[]
                { EXEC_TREE_ADDED_NOTIF_TYPE }, Notification.class.getName(),
                    "This notification is emitted whenever an executionTree is added.") };
    }

    protected DefaultMutableTreeNode endMethod(long endTime,
            JoinPoint joinPoint) {
        DefaultMutableTreeNode node = super.endMethod(endTime, joinPoint);
        MyDefaultMutableTreeNode mydefaultNode = (MyDefaultMutableTreeNode) node;

        if (!mydefaultNode.isStillExecuting())
            sendNotification(node);
        return node;
    }

    private void sendNotification(DefaultMutableTreeNode node) {
        Notification acn = new Notification(EXEC_TREE_ADDED_NOTIF_TYPE, node,
                1);
        singleton.sendNotification(acn);
    }

}
