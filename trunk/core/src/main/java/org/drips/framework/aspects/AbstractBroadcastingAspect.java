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
package org.drips.framework.aspects;


import javax.management.ListenerNotFoundException;
import javax.management.MBeanNotificationInfo;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;

import org.drips.framework.aspectwerkx.AbstractDripsAspect;

/**
 * @author Prasen Mukherjee
 */
public abstract class AbstractBroadcastingAspect extends AbstractDripsAspect implements AbstractBroadcastingAspectMBean {

    protected abstract NotificationBroadcasterSupport getSingleton();

    public void removeNotificationListener(NotificationListener listener,
                                           NotificationFilter filter, Object handback)
            throws ListenerNotFoundException {
        getSingleton().removeNotificationListener(listener, filter, handback);
    }

    public void addNotificationListener(NotificationListener listener,
                                        NotificationFilter filter, Object handback)
            throws IllegalArgumentException {
        getSingleton().addNotificationListener(listener, filter, handback);
    }

    public void removeNotificationListener(NotificationListener listener)
            throws ListenerNotFoundException {
        getSingleton().removeNotificationListener(listener);
    }

    public abstract MBeanNotificationInfo[] getNotificationInfo();
/*
    public MBeanNotificationInfo[] getNotificationInfo() {
        return new MBeanNotificationInfo[]
            { new MBeanNotificationInfo(new String[]
                { "BASE_NOTIF_TYPE" }, Notification.class.getName(),
                    "Base Notification.") };
    }
*/

    protected void sendNotification(Notification notif) {
        getSingleton().sendNotification(notif);
    }

}
