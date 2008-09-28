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

import java.util.HashMap;
import java.util.Map;

import javax.management.NotCompliantMBeanException;
import javax.management.StandardMBean;

import org.codehaus.aspectwerkz.transform.inlining.deployer.Deployer;
import org.codehaus.aspectwerkz.transform.inlining.deployer.DeploymentHandle;
import org.drips.framework.apt.DripsBeanInfo;
import org.drips.framework.apt.DripsBeanMethodInfo;
import org.drips.utils.UniqueIdentityRepository;


/**
 * This Base Interface for the Aspect defined in the Drips Framework. Any
 * Drips aware aspect must implement this interface
 * 
 * @author Prasen Mukherjee
 * @author Saurabh Arora
 */
public class AbstractDripsAspect implements DripsAspect {

    // Should not be extended by user aspects,But as of now
    // DripsDynamicAspectMbean modifies it. So we may need to
    // rearchitect a little. ;-)
    public InternalDripsAspectInfo getInfo() {
        if (!getClass().isAnnotationPresent(DripsBeanInfo.class)) {
            StandardMBean mbean = getStandardMBean();
            return new InternalDripsAspectInfo(mbean.getMBeanInfo(),
                    "com.bea.drips.framework:type="
                            + mbean.getImplementationClass(), mbean
                            .getMBeanInterface().getName());
        } else {
            DripsDynamicAspectMBean mbean = new DripsDynamicAspectMBean(
                    (Class<? extends DripsAspect>) getClass());
            return mbean.getInfo();
        }
    }

    protected StandardMBean getStandardMBean() {
        try {
            StandardMBean mbean = new StandardMBean(getMBeanImplementation(),
                    getMBeanInterface());
            return mbean;
        } catch (NotCompliantMBeanException e) {
            e.printStackTrace(); // To change body of catch statement use
            // File | Settings | File Templates.
        }

        return null;
    }

    protected Object getMBeanImplementation() {
        return this;
    }

    protected Class getMBeanInterface() {
        String mbeanInterfaceName = getClass().getName() + "MBean";
        try {
            return Class.forName(mbeanInterfaceName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace(); // To change body of catch statement use
            // File | Settings | File Templates.
        }
        return null;
    }

    private Map<String, DeploymentHandle> deploymenHandleMap = new HashMap<String, DeploymentHandle>();

    @DripsBeanMethodInfo(value = "Method to Deploy this Aspect")
    public String deploy(final String aspectExpression,
            final String adviceName, final String adviceType) {
        String pointCutName = getUniquePointcutName();
        String xmlDef = "<aspect class=\"" + getDeployableAspect().getCanonicalName()
                + "\" >" + "<pointcut name=\"" + pointCutName
                + "\" expression=\"" + aspectExpression + "\"/>"
                + "<advice name=\"" + adviceName + "\" type=\"" + adviceType
                + "\" bind-to=\"" + pointCutName + "\"/>" + "</aspect>";

        DeploymentHandle handle = Deployer.deploy(getDeployableAspect(), xmlDef);
        String handleKey = handle.toString();
        deploymenHandleMap.put(handleKey, handle);
        return handleKey;
    }

    protected Class getDeployableAspect() {
        return getClass();
    }

    @DripsBeanMethodInfo(value = "Method to Deploy this Aspect")
    public void deploy() {
        Deployer.deploy(getDeployableAspect());
    }

    @DripsBeanMethodInfo(value = "Method to UnDeploy this Aspect,using the Deployment Handle")
    public boolean undeploy(final String deploymentHandleString) {
        DeploymentHandle handle = deploymenHandleMap
                .get(deploymentHandleString);
        if (handle == null) {
            return false;
        }
        Deployer.undeploy(handle);
        deploymenHandleMap.remove(deploymentHandleString);
        return true;
    }

    @DripsBeanMethodInfo(value = "Method to UnDeploy this Aspect")
    public boolean undeploy() {
        Deployer.undeploy(getDeployableAspect());
        return true;
    }

    // The following is a hack to overcome an AOP bug.
    private static UniqueIdentityRepository uniquePointcutNamerepository = new UniqueIdentityRepository();

    private static String pointCutPrefix = "dripsPointcut";

    private static String getUniquePointcutName() {
        return uniquePointcutNamerepository.createUniqueId(pointCutPrefix);
    }

}
