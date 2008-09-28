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
package org.drips.framework.mbeans;

import org.drips.framework.aspectwerkx.DripsAspectInfo;

/**
 * This is the fundamental interface of a DripsFramework which is used to
 * interact with the system. It represents a MBean for the interaction with
 * the system.
 * 
 * @author Saurabh Arora
 */
public interface DripsMainMBean {

    String DRIPS_MBEAN_OBJECTNAME = "com.bea.drips.framework:type=DripsMain";

    /**
     * Used to enable(start) the drips Framework.
     */
    void enable();

    /**
     * Use this method to disable the Drips Framework.
     */
    void disable();

    /**
     * Gets the state of the Drips Framework.
     * 
     * @return true when drips is running, or else false.
     */
    boolean getState();

    /**
     * Return the Array of all the ClassName of all the DripsAspects.
     * 
     * @return ClassName of all the drips Aspects.
     */
    String[] getAspects();

    /**
     * This method returns the DripsAspectInfo(Meta Information) associated
     * with the specified classname(DripsAspect).
     * 
     * @param classname
     *            Name of the DripsAspect Class, for which DripsAspectinfo is
     *            required.
     * @return DripsAspectInfo of the specified DripsAspect classname.
     */
    DripsAspectInfo getAspectInfo(String classname);

    /**
     * This method is used to return the DripsAspectInfos(Meta data) for all
     * the DripsAspects running in the system.
     * 
     * @return Array of DripsAspectInfo
     */
    DripsAspectInfo[] getAspectInfos();

    /**
     * This method is used to enable a DripsAspect,using the specified
     * DripsAspectInfo.
     * 
     * @param info
     *            DripsAspectInfo for the DripsAspect you want to enable.
     * @deprecated in favor of enableAspect(String aspectClassName)
     * @see DripsMainMBean#enableAspect(String)
     */
    @Deprecated
    void enableAspect(DripsAspectInfo info);

    /**
     * This method is used to disable the specified DripsAspect,using the
     * specified DripsAspectInfo.
     * 
     * @param info
     *            DripsAspectInfo for the DripsAspect you want to disable.
     * @deprecated in favor of enableAspect(String aspectClassName)
     * @see DripsMainMBean#disableAspect(String)
     */
    @Deprecated
    void disableAspect(DripsAspectInfo info);

    /**
     * This method is used to register a AspectClass to the Registry.
     * 
     * @param aspectclass
     *            The name of the Class to be registered.
     * @see DripsMainMBean
     * @deprecated in favour of #enableAspect(String,String)
     * @return It returns the DripsAspectInfo.
     */
    @Deprecated
    DripsAspectInfo registerAspect(String aspectclass);

    /**
     * This method is used to determine whether an Aspect is registered or
     * not.
     * 
     * @param aspectclass
     *            The name of the Class to be registered.
     * @return true if it is registered, false otherwise
     */
    boolean isAspectRegistered(String aspectclass);

    /**
     * This method is used to enable a DripsAspect,using the specified
     * aspectClass.
     * 
     * @param aspectclass
     *            The name of the Class to be enabled.
     */
    void enableAspect(String aspectclass);

    /**
     * This method is used to enable a DripsAspect,using the specified
     * aspectClass.
     * 
     * @param aspectclass
     *            The name of the Class to be enabled.
     * @param aspectclasspath
     *            ClassPath to be used to load aspectclass
     * @throws MalformedURLException
     *             It throws the classpath, is not a valid URL.
     */
/*
    //TODO Need to revisit after beta release - prasen 
    void enableAspect(String aspectclass, String aspectclasspath)
            throws MalformedURLException;
*/

    /**
     * This method is used to determine whether an Aspect is enabled or not.
     * 
     * @param aspectclass
     *            The name of the Class to be registered.
     * @return true if it is enabled, false otherwise
     */
    boolean isAspectEnabled(String aspectclass);

    /**
     * This method is used to disable the specified DripsAspect,using the
     * specified aspectclass.
     * 
     * @param aspectclass
     *            The name of the Class to be registered.
     */
    void disableAspect(String aspectclass);

}
