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

import java.net.MalformedURLException;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.drips.framework.aspectwerkx.DripsAspect;
import org.drips.framework.aspectwerkx.DripsAspectInfo;
import org.drips.framework.aspectwerkx.DripsAspectrepository;
import org.drips.framework.aspectwerkx.intializer.DripsIntializationTester;
import org.drips.utils.Utils;


/**
 * This is the base MBean for the Drips Framework. The user must use this
 * mbean to carry out operation on the Drips Framework.
 */
public class DripsMain implements DripsMainMBean {

    private boolean state = false;

    private boolean dripsintialized = false;

    /**
     * This method is to Intialize Drips
     */
    private void intializeDrips() {
        // We know that AspectWerkz is setup properly.
        if (DripsIntializationTester.getInstance().isintialized()) {
            dripsintialized = true;
        } else {
            dripsintialized = false;
        }
    }

    private void setState(final boolean astate) {
        // System.out.println("Set State called");
        if (astate) {
            intializeDrips();
            // if we are trying to start Drips, first
            // Check that aspect is intialized or throw
            // exception.
            if (!dripsintialized) {
                throw new IllegalStateException(
                        "Aspect subsystem is not intialized");
            } else {
                // Try to do some thing.
                System.out.println("Aspect System");
            }
        } else {
            // Try to Shutdown the system
            DripsAspectrepository.removeAllAspect();
        }
        state = astate;
        // FIXME if state is start , then start the processor or shutdown

    }

    /**
     * Gets the state of the Drips Framework.
     * 
     * @return true when drips is running, or else false.
     */
    public boolean getState() {
        // System.out.println("Get State called");
        return state;
    }

    /**
     * This method is used to return the DripsAspectInfos(Meta data) for all
     * the DripsAspects running in the system.
     * 
     * @return Array of DripsAspectInfo
     */
    public DripsAspectInfo[] getAspectInfos() {
        String[] aspect = getAspects();
        DripsAspectInfo[] infos = new DripsAspectInfo[aspect.length];
        for (int i = 0; i < aspect.length; i++) {
            infos[i] = getAspectInfo(aspect[i]);
        }
        return infos;
    }

    /**
     * This method returns the DripsAspectInfo(Meta Information) associated
     * with the specified classname(DripsAspect).
     * 
     * @param classname
     *            Name of the DripsAspect Class, for which DripsAspectinfo is
     *            required.
     * @return DripsAspectInfo of the specified DripsAspect classname.
     */
    public DripsAspectInfo getAspectInfo(final String classname) {
        try {
            Class c = DripsAspectrepository.getAspectClass(classname);
            DripsAspect base = (DripsAspect) c.newInstance();
            return base.getInfo().getDripsInfo();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Return the Array of all the ClassName of all the DripsAspects.
     * 
     * @return ClassName of all the drips Aspects.
     */
    public String[] getAspects() {
        String[] aspect = DripsAspectrepository.getAspects();
        return aspect;
    }

    /**
     * Used to enable(start) the drips Framework.
     */
    public void enable() {
        setState(true);

    }

    /**
     * Use this method to disable the Drips Framework.
     */
    public void disable() {
        setState(false);

    }

    /**
     * This method is used to enable a DripsAspect,using the specified
     * DripsAspectInfo.
     * 
     * @param info
     *            DripsAspectInfo for the DripsAspect you want to enable.
     */
    @Deprecated
    public void enableAspect(final DripsAspectInfo info) {
        DripsAspectrepository.startAspect(info);
    }

    /**
     * This method is used to disable the specified DripsAspect,using the
     * specified DripsAspectInfo.
     * 
     * @param info
     *            DripsAspectInfo for the DripsAspect you want to disable.
     */
    @Deprecated
    public void disableAspect(final DripsAspectInfo info) {
        DripsAspectrepository.stopAspect(info);
    }

    /**
     * This method is used to register a AspectClass to the Registry.
     * 
     * @param aspectclass
     *            The name of the Class to be registered.
     * @return It returns the DripsAspectInfo.
     */

    @Deprecated
    public DripsAspectInfo registerAspect(final String aspectclass) {
        try {
            DripsAspectrepository.registerAspect(aspectclass, null);
        } catch (MalformedURLException e) {
            e.printStackTrace(); // To change body of catch statement use
            // File | Settings | File Templates.
        }
        return getAspectInfo(aspectclass);
    }

    /**
     * This method is used to determine whether an Aspect is registered or
     * not.
     * 
     * @param aspectclass
     *            The name of the Class to be registered.
     * @return true if it is registered, false otherwise
     */
    public boolean isAspectRegistered(final String aspectclass) {
        // REVIEW prasen Assumption is that aspectInfo will not exist if it
        // has not been registered
        return null != getAspectInfo(aspectclass);
    }

    /**
     * This method is used to enable a DripsAspect,using the specified
     * aspectClass.
     * 
     * @param aspectclass
     *            The name of the Class to be enabled.
     */
    public void enableAspect(final String aspectclass) {
        try {
            enableAspect(aspectclass, null);
        } catch (MalformedURLException e) {
            e.printStackTrace(); // To change body of catch statement use
            // File | Settings | File Templates.
        }
    }

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
    public void enableAspect(final String aspectclass,
            final String aspectclasspath) throws MalformedURLException {
        // if (isAspectRegistered(aspectclass) &&
        // isAspectEnabled(aspectclass)) throw new RuntimeException("Aspect:
        // "+aspectclass+" already enabled");
        // if (aspectclasspath!=null)
        // aspectClassToClassPathMap.put(aspectclass,aspectclasspath);
        DripsAspectrepository.registerAspect(aspectclass, aspectclasspath);
        enableAspect(getAspectInfo(aspectclass));
    }

    /**
     * This method is used to determine whether an Aspect is enabled or not.
     * 
     * @param aspectclass
     *            The name of the Class to be registered.
     * @return true if it is enabled, false otherwise
     */
    public boolean isAspectEnabled(final String aspectclass) {
        DripsAspectInfo dripsAspectInfo = getAspectInfo(aspectclass);
        try {
            return Utils.getLocalMBeanServer().isRegistered(
                    new ObjectName(dripsAspectInfo.getObjectName()));
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * This method is used to disable the specified DripsAspect,using the
     * specified aspectclass.
     * 
     * @param aspectclass
     *            The name of the Class to be registered.
     */
    public void disableAspect(final String aspectclass) {
        disableAspect(getAspectInfo(aspectclass));
    }

}
