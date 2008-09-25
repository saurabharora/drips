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

import java.io.Serializable;

/**
 * This class defines the DripsAspect related information exposed to the end
 * user. The end user uses this information to create a mbean.
 * 
 * <pre>
 *        DripsMainMBean mbean;
 *        Intialize DripsMainMBean, be MBean lookup to the server jvm.
 *        DripsAspectInfo info1 = mbean.getAspectInfo(TestAspect.class.getName());
 *        mbean.enableAspect(info1);
 * </pre>
 * 
 * @author Saurabh Arora
 * @see org.drips.framework.aspectwerkx.InternalDripsAspectInfo
 */
public class DripsAspectInfo implements Serializable {

    /**
     * Default Serialization UID.
     */
    private static final long serialVersionUID = 3257288011191628080L;

    private final String name;

    private final String description;

    private final String objectName;

    private boolean dynamic = false;

    private final String mbeanInterfaceName;

    /**
     * Default constructor for creating DripsAspectInfo, with all the
     * Information, which can be asked by the client.
     * 
     * @param aname
     *            Name of the DripsAspect
     * @param adescription
     *            Detailed Description of the behaviour of Aspect.
     * @param aObjectName
     *            The ObjectName used to register,the mbean associated with
     *            this aspect.
     * @param ambeanInterfaceName
     *            The interface which is used to describe this mbean.
     * @param adynamic
     *            Whether the mbean, associated with this DripsAspect is
     *            Dynamic,or contains statndard mbean.
     */
    public DripsAspectInfo(final String aname, final String adescription,
            final String aObjectName, final String ambeanInterfaceName,
            final boolean adynamic) {
        name = aname;
        description = adescription;
        objectName = aObjectName;
        dynamic = adynamic;
        this.mbeanInterfaceName = ambeanInterfaceName;
    }

    /**
     * Construtor for DefaultAspectInfo which assumes that dynamic mbean is
     * gals
     * 
     * @param aname
     *            Name of the DripsAspect
     * @param adescription
     *            Detailed Description of the behaviour of Aspect.
     * @param aObjectName
     *            The ObjectName used to register,the mbean associated with
     *            this aspect.
     * @param ambeanInterfaceName
     *            The interface which is used to describe this mbean.
     * @see #DripsAspectInfo(String, String, String)
     */
    public DripsAspectInfo(final String aname, final String adescription,
            final String aObjectName, final String ambeanInterfaceName) {

        name = aname;
        description = adescription;
        objectName = aObjectName;
        this.mbeanInterfaceName = ambeanInterfaceName;
    }

    /**
     * Get the Name of the Mbean defining this DripsAspect.
     * 
     * @return Name as String.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the Description.
     * 
     * @return Description as String.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the ObjectName used to register the mbean associated with this
     * DripsAspect.
     * 
     * @return ObjectName as String.
     */
    public String getObjectName() {
        return objectName;
    }

    public String getMBeanInerfaceName() {
        return mbeanInterfaceName;
    }

    /**
     * returns whether the associated MBean is Dyanmic.
     * 
     * @return boolean
     */
    public boolean isDynamic() {
        return dynamic;
    }
}
