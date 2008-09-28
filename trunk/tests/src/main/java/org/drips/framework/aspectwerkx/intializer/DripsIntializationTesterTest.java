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
package org.drips.framework.aspectwerkx.intializer;

import junit.framework.TestCase;

import org.codehaus.aspectwerkz.transform.inlining.deployer.Deployer;
import org.codehaus.aspectwerkz.transform.inlining.deployer.DeploymentHandle;
import org.drips.framework.aspectwerkx.intializer.DripsIntializationTest;
import org.drips.framework.aspectwerkx.intializer.DripsIntializationTestAspect;
import org.drips.framework.aspectwerkx.intializer.DripsIntializationUtils;

public class DripsIntializationTesterTest extends TestCase {

    public void setUp() {
        DripsIntializationUtils.resetCallStack();
    }

    public void testXMLDeployment() throws Throwable {
        // dynamicpointcut
        String xmlDef = "<aspect name=\""
                + DripsIntializationTestAspect.class.getCanonicalName()
                + "\" >"
                + "<pointcut name=\"dynamicpointcut\" expression=\"execution"
                + "(void ..DripsIntializationTest.method2())\"/>"
                + "</aspect>";
        System.out.println(xmlDef);
        String[] calltrace =
            { "callPointcut", "method1", "dynamicpointcut", "method2" };
        DeploymentHandle handle = Deployer.deploy(
                DripsIntializationTestAspect.class, xmlDef);
        DripsIntializationTest test = new DripsIntializationTest();

        test.callallmethods();
        // undeploy using handle
        Deployer.undeploy(handle);
        String[] calls = DripsIntializationUtils.getCallStack();
        printcalls(calls);
        assertEqual(calltrace, calls);
    }

    public void ltestSimpleDeployment() throws Throwable {
        String[] calltrace =
            { "callPointcut", "method1", "method2" };
        DeploymentHandle handle = Deployer
                .deploy(DripsIntializationTestAspect.class);
        DripsIntializationTest test = new DripsIntializationTest();
        test.callallmethods();
        // undeploy using handle
        Deployer.undeploy(handle);
        String[] calls = DripsIntializationUtils.getCallStack();
        printcalls(calls);
        assertEqual(calltrace, calls);
    }

    private void assertEqual(final String[] calltrace, final String[] calls)
            throws Throwable {
        assertEquals(calltrace.length, calls.length);
        for (int i = 0; i < calltrace.length; i++) {
            assertEquals(calltrace[i], calls[i]);
        }
    }

    private void printcalls(final String[] calls) {
        System.out.print("Call Trace={");
        for (int i = 0; i < calls.length; i++) {
            System.out.print(calls[i] + ",");
        }
        System.out.println("}");
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        DripsIntializationUtils.resetCallStack();
    }
}
