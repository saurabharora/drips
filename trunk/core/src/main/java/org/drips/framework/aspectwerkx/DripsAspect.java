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

/**
 * This Base Interface for the Aspect defined in the Drips Framework. Any
 * Drips aware aspect must implement this interface
 * 
 * @author Saurabh Arora, Prasen Mukherjee
 */
public interface DripsAspect {
    /**
     * This method is used to obtain the Meta Data (called
     * InternalDripsAspectInfo) which can be used to start this Aspect.
     * 
     * @return InternalDripsAspectInfo, the metadata required to intialize
     *         this DripsAspect.
     */
    InternalDripsAspectInfo getInfo();

    /**
     * This method is used to deploy aspects dynamically
     * 
     * @param aspectExpression
     *            String to define pointcut expression
     * @param adviceName
     *            MethodName of aspect-class
     * @param adviceType :
     *            aspectType, one of around,execution,before etc.
     * @return deploymentHandleString, which is passed to undeploy for
     *         disabling the aspect
     */
    String deploy(String aspectExpression, String adviceName,
            String adviceType);

    /**
     * This method is used to deploy the whole aspectclass dynamically
     */
    void deploy();

    /**
     * This method is used to undeploy aspects dynamically
     * 
     * @param deploymentHandleString
     *            Handle for the deployer.
     * @return boolean, true, if successfully undeployed, false otherwise
     * @see DripsAspect.deploy(String)
     */
    boolean undeploy(String deploymentHandleString);

    /**
     * This method is used to undeploy the whole aspectclass dynamically
     * 
     * @return boolean, true, if successfully undeployed, false otherwise
     * @see DripsAspect.deploy()
     */
    boolean undeploy();

}
