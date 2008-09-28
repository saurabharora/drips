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
package org.drips.framework.aspectwerkx;

import javax.management.MBeanInfo;

/**
 * This Class defines the internal MetaData required to create a class
 * Instance. The DripsAspectInfo provided by this class is also made available
 * to the end user.
 * 
 * @author Saurabh Arora
 * @see org.drips.framework.aspectwerkx.DripsAspect
 */
public class InternalDripsAspectInfo {

    private final MBeanInfo info;

    private DripsAspectInfo aspectinfo = null;

    private final String objectName;

    private boolean dynamic = false;

    private final String mbeanInterfaceName;

    /**
     * The default constructor for the Drips required to set the internal
     * MBeanInfo.
     * 
     * @param ainfo
     *            MBeanInfo of the associated MBean for this Drips Aspect.
     * @param aobjectname
     *            ObjectName which should be used to register the associated
     * @param ambeanInterfaceName
     *            The interface which is used to describe this mbean.
     */
    public InternalDripsAspectInfo(final MBeanInfo ainfo,
            final String aobjectname, final String ambeanInterfaceName) {

        info = ainfo;
        objectName = aobjectname;
        this.mbeanInterfaceName = ambeanInterfaceName;
    }

    public void setDynamic(final boolean value) {
        dynamic = value;
    }

    /**
     * The mbean Information for the MBean associated with this DripsAspect.
     * This information is internal to the Drips Framwork and not exposed to
     * the end user.
     * 
     * @return MBeanInfo for the associated MBean.
     */
    public MBeanInfo getInfo() {
        return info;
    }

    /**
     * It defines the DripsAspectInfo which is based to the enduser.
     * 
     * @return DripsAspectInfo to be based to the end user.
     * @see DripsAspectInfo
     */
    public DripsAspectInfo getDripsInfo() {
        if (aspectinfo == null) {
            aspectinfo = new DripsAspectInfo(info.getClassName(), info
                    .getDescription(), objectName, mbeanInterfaceName,
                    dynamic);
        }

        return aspectinfo;
    }

}
