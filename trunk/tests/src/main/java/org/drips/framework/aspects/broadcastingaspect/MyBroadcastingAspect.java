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


import javax.management.NotificationBroadcasterSupport;
import javax.management.MBeanNotificationInfo;
import javax.management.Notification;

import org.codehaus.aspectwerkz.joinpoint.JoinPoint;
import org.codehaus.aspectwerkz.joinpoint.Rtti;
import org.codehaus.aspectwerkz.joinpoint.MethodRtti;
import org.drips.framework.apt.DripsBeanInfo;
import org.drips.framework.aspects.AbstractBroadcastingAspect;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Prasen Mukherjee
 */

public class MyBroadcastingAspect extends AbstractBroadcastingAspect implements MyBroadcastingAspectMBean {
    private final static NotificationBroadcasterSupport NOTIFICATION_BROADCASTER_SUPPORT = new NotificationBroadcasterSupport();
    protected NotificationBroadcasterSupport getSingleton() {
       return NOTIFICATION_BROADCASTER_SUPPORT;
    }

    public MBeanNotificationInfo[] getNotificationInfo() {
        return new MBeanNotificationInfo[]
            { new MBeanNotificationInfo(new String[]
                { "CALENDAR_NOTIF_TYPE" }, Notification.class.getName(),
                    "CALENDAR Notification.") };
    }


    private final List alertWordList = Arrays.asList(new String[]{"terrorist"});
    public Object checkForAlertingWords(JoinPoint jp) throws Throwable {

        Object result = jp.proceed();

        Rtti rtti = jp.getRtti();
        if (rtti instanceof MethodRtti) {
            MethodRtti methodrtti = (MethodRtti) rtti;
            Method method = methodrtti.getMethod();
            if (method.getName().equals("setDescription") && method.getDeclaringClass().getName().equals("cal.Entry")) {
                String descriptionString = (String) methodrtti.getParameterValues()[1];
                if (alertWordList.contains(descriptionString)) {
                    Notification notif = new Notification("ALERT_NOTIF_TYPE", descriptionString,1);
                    sendNotification(notif);
                }
            }
        }

        return result;
    }

    public String enableMyAspect() {
        String aspectPointcutExpression = "execution(public void cal..*.*(..)) OR execution(public * org.apache.jsp.cal..*.*(..))";
        String adviseName = "checkForAlertingWords";
        String adviseType = "around";
        return deploy(aspectPointcutExpression,adviseName,adviseType);
    }
}
