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
package org.drips.framework.profiler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Stack;

import javax.swing.tree.DefaultMutableTreeNode;

import org.codehaus.aspectwerkz.joinpoint.JoinPoint;
import org.drips.utils.Utils;


/**
 * This class is the core processor for the Profiling Subsytem.The Processor
 * would receive events of the form startmethod,endmethod from various
 * sources. This sources would also send the ProfilerInfoprovider with the
 * events, which is then used to obtain the information of the current
 * profiling source.This information (MethodInfo) with the methodname
 * (obtained from ProfileInfoProvider) is used in the Tree,based on the
 * calling trace. It is a singleton Class.
 * 
 * @author Saurabh Arora, Prasen Mukherjee
 * @see org.drips.framework.profiler.MethodInfo
 * @see org.drips.framework.profiler.ProfilerInfoProvider
 */
public final class ProfilerProcessor {

    private static ProfilerProcessor instance = new ProfilerProcessor();

    private Collection<DefaultMutableTreeNode> methodTreeCollection = new ArrayList<DefaultMutableTreeNode>();

    private ProfilerProcessor() {

    }

    public Collection<DefaultMutableTreeNode> getExecutionProfileTrees() {
        return Collections.unmodifiableCollection(methodTreeCollection);
    }

    public DefaultMutableTreeNode getLastExecutionProfileTree() {
        ArrayList<DefaultMutableTreeNode> al = (ArrayList<DefaultMutableTreeNode>) methodTreeCollection;
        if (al.size() > 0) {
            return al.get(al.size() - 1);
        } else {
            return null;
        }
    }

    /**
     * Method to access the instance of this Singleton Class.
     * 
     * @return instance of ProfilerProcessor
     */
    public static ProfilerProcessor getInstance() {
        return instance;
    }

    /**
     * This method starts the profiling for a specific method.
     * 
     * @param startTime
     *            Time at which this method is start.
     * @param datainfo
     *            Data Object which contains the data information to be
     *            extracted using the provider.
     * @param infoprovider
     *            Information Provider, which operates on the datainfo and
     *            returns the required internal information
     */
    public DefaultMutableTreeNode startMethod(final long startTime,
            final JoinPoint datainfo, final ProfilerInfoProvider infoprovider) {

        MethodInfo info = infoprovider.createMethodInfo(datainfo);
        info.setStartTime(startTime);
        return push(info, getThreadLocalData());
    }

    /**
     * This method ends the profiling of a specific method
     * 
     * @param endTime
     *            Time at which this method ends processing.
     * @param information
     *            Data Object which contains the data information to be
     *            extracted using the provider.
     * @param infoprovider
     *            Information Provider, which operates on the datainfo and
     *            returns the required internal information
     */
    public DefaultMutableTreeNode endMethod(final long endTime,
            final Object information, final ProfilerInfoProvider infoprovider) {
        return pop(endTime, getThreadLocalData());
    }

    private DefaultMutableTreeNode push(final MethodInfo info,
            final ThreadLocalData threadLocalData) {
        MyDefaultMutableTreeNode treeNode = new MyDefaultMutableTreeNode(info);
        threadLocalData.methodStack.push(treeNode);
        return treeNode;
    }

    private DefaultMutableTreeNode pop(final long endTime,
            final ThreadLocalData threadLocalData) {
        Stack<DefaultMutableTreeNode> methodStack = threadLocalData.methodStack;
        DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) methodStack
                .pop();
        MethodInfo info = (MethodInfo) treeNode.getUserObject();
        long startTime = info.getStartTime();
        info.setEndTime(endTime);
        long timeelapsed = (endTime - startTime);
        info.setElapsedTime(timeelapsed);

        if (methodStack.isEmpty()) {
            // Now add it to our global set
            methodTreeCollection.add(treeNode);
            ((MyDefaultMutableTreeNode) treeNode).stillExecuting = false;

            Utils.printTree(treeNode, "  ");
        } else {
            DefaultMutableTreeNode callerTreeNode = (DefaultMutableTreeNode) methodStack
                    .peek();
            if (callerTreeNode != null) {
                callerTreeNode.add(treeNode);
            }
        }

        return treeNode;

    }

    private ThreadLocal<ThreadLocalData> threadLocalDataHolder = new ThreadLocal<ThreadLocalData>();

    private ThreadLocalData getThreadLocalData() {
        ThreadLocalData threadLocalData = (ThreadLocalData) threadLocalDataHolder
                .get();
        if (threadLocalData == null) {
            threadLocalData = new ThreadLocalData();
            threadLocalDataHolder.set(threadLocalData);
        }
        return threadLocalData;
    }

    class ThreadLocalData {
        private Stack<DefaultMutableTreeNode> methodStack = new Stack<DefaultMutableTreeNode>();

        Stack<DefaultMutableTreeNode> getStack() {
            return methodStack;
        }
    }

    public static class MyDefaultMutableTreeNode extends
            DefaultMutableTreeNode {

        private static final long serialVersionUID = 3258132457646665783L;

        private MethodInfo userObject;

        private boolean stillExecuting = true;

        public boolean isStillExecuting() {
            return stillExecuting;
        }

        public MyDefaultMutableTreeNode(final MethodInfo auserObject) {
            super(auserObject);
            this.userObject = auserObject;
        }

        @Override
        public MethodInfo getUserObject() {
            return userObject;
        }
    }
}
