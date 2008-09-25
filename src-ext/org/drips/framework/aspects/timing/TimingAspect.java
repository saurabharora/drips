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

import org.codehaus.aspectwerkz.joinpoint.JoinPoint;
import org.drips.framework.aspectwerkx.AbstractDripsAspect;
import org.drips.framework.profiler.ProfilerProcessor;
import org.drips.utils.Utils;


/**
 * This is TimingAspect,which can be deployed to obtain Method Timings.
 * 
 * @author Prasen Mukherjee,Saurabh Arora
 */

public class TimingAspect extends AbstractDripsAspect implements
        TimingAspectMBean {

    static final boolean DEBUG = true;

    private static AspectwerkzInfoProvider provider = new AspectwerkzInfoProvider();

    private static final String profilingAdviceName = "doGenericMethodProfiling";

    private static final String profilingAdviceType = "around";

    public TimingAspect() {
        // Thread.dumpStack();
        // System.out.println("Inside TimingAspect.TimingAspect: "+this);
    }

    public Object doGenericMethodProfiling(final JoinPoint joinPoint)
            throws Throwable {

        long startTime = 0;
        final Object result;
        long endTime = 0;

        startTime = System.currentTimeMillis();
        startMethod(startTime, joinPoint);

        try {
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            if (DEBUG) {
                throwable.printStackTrace();
            }
            throw throwable;
        } finally {
            endTime = System.currentTimeMillis();
            DefaultMutableTreeNode node = endMethod(endTime, joinPoint);

            if (DEBUG)
                Utils.printTree(node, " ");
        }

        return result;
    }

    protected DefaultMutableTreeNode startMethod(long startTime,
            JoinPoint joinPoint) {
        return ProfilerProcessor.getInstance().startMethod(startTime,
                joinPoint, provider);
    }

    protected DefaultMutableTreeNode endMethod(long endTime,
            JoinPoint joinPoint) {
        return ProfilerProcessor.getInstance().endMethod(endTime, joinPoint,
                provider);
    }

    public Collection<DefaultMutableTreeNode> getExecutionProfileTrees() {
        Collection<DefaultMutableTreeNode> treeColl = ProfilerProcessor
                .getInstance().getExecutionProfileTrees();
        return treeColl;
    }

    public DefaultMutableTreeNode getLastExecutionProfileTree() {
        return ProfilerProcessor.getInstance().getLastExecutionProfileTree();
    }

    public long getTime(final Method methodname) {
        if (methodname == null) {
            throw new IllegalArgumentException("Null methodname");
        }
        // TODO: we need to process the ThreadStack as in ProfilerProcessor
        // and find the time taken for this method.
        return 0;
    }

    public String doProfiling(String aspectExpression) {
        return deploy(aspectExpression, profilingAdviceName,
                profilingAdviceType);
    }

}
