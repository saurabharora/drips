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

import java.lang.reflect.Method;
import java.util.Collection;

import javax.swing.tree.DefaultMutableTreeNode;

import org.drips.framework.aspectwerkx.DripsAspect;


/**
 * This is a interface which exposes the Operation provided by TimingAspect.
 * 
 * @author Prasen Mukherjee, Saurabh Arora
 */
public interface TimingAspectMBean extends DripsAspect{

    /**
     * This is a dummy method, need to remove it
     */
    long getTime(Method methodname);

    /**
     * This method is used to deploy aspects dynamically
     *
     * @param aspectPointcutExpression String to define pointcut expression
     * @return deploymentHandleString, which is passed to undeploy for disabling the aspect
     * @see DripsAspect#undeploy(String)
     */
    String doProfiling(String aspectPointcutExpression);


    /**
     * This method is used to retrieve profiling data in a tree format
     *
     * @return TreeNodeCollection this includes the profileTreeSet
     */
    Collection<DefaultMutableTreeNode> getExecutionProfileTrees();

    DefaultMutableTreeNode getLastExecutionProfileTree();

}
