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

import java.io.File;
import java.lang.management.ManagementFactory;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

/**
 * This Class acts as a Registry of all the DripsAspects available in the
 * current classpath.So that they can in access by using the DripsMBean API.
 * 
 * @author Saurabh Arora
 */
public final class DripsAspectrepository {

    private static DripsAspectrepository instance = new DripsAspectrepository();

    // static {
    // intializeAspect(TimingAspect.class.getName());
    // }

    private Map<String, ClassLoader> aspectMap = new HashMap<String, ClassLoader>();

    private List<ObjectName> objectnamemap = new ArrayList<ObjectName>();

    private DripsAspectrepository() {
        super();
    }

    /**
     * This method is used to register a AspectClass to the Registry.
     * 
     * @param aspectclass
     *            The name of the Class to be registred.
     * @param aspectclasspath
     *            ClassPath for loading the aspect.
     * @see #unregisterAspect(String)
     * @throws MalformedURLException
     *             When a proper URL cannot be created from aspectclasspath
     */
    public static void registerAspect(final String aspectclass,
            final String aspectclasspath) throws MalformedURLException {
        if ((aspectclass == null)) {
            throw new IllegalArgumentException();
        }

        URLClassLoader cl = null;
        if (aspectclasspath != null) {
            URL classpathUrl = new File(aspectclasspath).toURL();
            cl = new URLClassLoader(new URL[]
                { classpathUrl }, DripsAspectrepository.class
                    .getClassLoader());
        }

        instance.aspectMap.put(aspectclass, cl);
    }

    public static Class getAspectClass(final String aspectClass)
            throws ClassNotFoundException {
        ClassLoader cl = instance.aspectMap.get(aspectClass);
        if (cl != null) {
            return Class.forName(aspectClass, false, cl);
        } else {
            return Class.forName(aspectClass);
        }
    }

    /**
     * This method brings up a particular aspect in aspect system. It
     * intializes the mbean for the specified type.
     * 
     * @param aspectinfo
     *            DripsAspectInfo for the DripsAspect that we need to start
     *            up.
     * @see #stopAspect(DripsAspectInfo)
     */
    public static void startAspect(final DripsAspectInfo aspectinfo) {
        if (aspectinfo == null) {
            throw new IllegalArgumentException();
        }
        // Returns the class that it defines.
        String classname = aspectinfo.getName();

        // TODO: we cannot handle Dynamic MBeans as of now.
        try {
            if (!aspectinfo.isDynamic()) {

                // InternalDripsAspectInfo info= getAspectInfo(classname);
                Class<?> c = getAspectClass(classname);
                Object main = c.newInstance();

                MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

                // Construct the ObjectName for the MBean we will register
                ObjectName name;

                name = new ObjectName(aspectinfo.getObjectName());
                // Register the Drips DripsMain MBean
                mbs.registerMBean(main, name);
                instance.objectnamemap.add(name);
            } else {
                Class<DripsAspect> c = (Class<DripsAspect>) getAspectClass(classname); // Class.forName(classname);
                DripsDynamicAspectMBean main = new DripsDynamicAspectMBean(c);
                MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

                // Construct the ObjectName for the MBean we will register
                ObjectName name;

                name = new ObjectName(aspectinfo.getObjectName());
                // Register the Drips DripsMain MBean
                mbs.registerMBean(main, name);
                instance.objectnamemap.add(name);
            }
        } catch (MalformedObjectNameException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new IllegalStateException(e);
        } catch (NullPointerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new IllegalStateException(e);
        } catch (InstanceAlreadyExistsException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new IllegalStateException(e);
        } catch (MBeanRegistrationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new IllegalStateException(e);
        } catch (NotCompliantMBeanException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new IllegalStateException(e);
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

    }

    /**
     * This method is used to unregister a AspectClass from the
     * DripsAspectrepository.
     * 
     * @param dripsaspect
     *            Fully Qualified Name of class to unregister.
     * @see #registerAspect(String)
     */
    // TODO: call destroAspect in a J2EE app, when class is unloaded.
    public static void unregisterAspect(final String dripsaspect) {
        if (dripsaspect == null) {
            throw new IllegalArgumentException();
        }
        instance.aspectMap.remove(dripsaspect);
    }

    /**
     * Returns all the aspects registered in the repository.
     * 
     * @return Array of String, containing classnames of registered
     *         DripsAspectClassses.
     */
    public static String[] getAspects() {
        Set<String> m = instance.aspectMap.keySet();
        int len = m.size();
        String[] ob = new String[len];
        return (String[]) m.toArray(ob);
    }

    /**
     * Stops a running DripsAspect, and unregisters it from the Mbean Server.
     * 
     * @param info
     *            DripsAspectInfo of the DripsAspect to stop.
     */
    public static void stopAspect(final DripsAspectInfo info) {
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name;
        try {
            name = new ObjectName(info.getObjectName());
            mbs.unregisterMBean(name);
            instance.objectnamemap.remove(name);
        } catch (MalformedObjectNameException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new IllegalStateException(e);
        } catch (NullPointerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new IllegalStateException(e);
        } catch (InstanceNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new IllegalStateException(e);
        } catch (MBeanRegistrationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new IllegalStateException(e);
        }

    }

    /**
     * This method is used to stop all the running Drips Aspects. It is
     * primarily a cleanup method.
     */
    public static void removeAllAspect() {
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        for (ObjectName name : instance.objectnamemap) {
            try {
                mbs.unregisterMBean(name);
            } catch (NullPointerException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                throw new IllegalStateException(e);
            } catch (InstanceNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                throw new IllegalStateException(e);
            } catch (MBeanRegistrationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                throw new IllegalStateException(e);
            }
        }
        instance.objectnamemap.clear();

    }
}
