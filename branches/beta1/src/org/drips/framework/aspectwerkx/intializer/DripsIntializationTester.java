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

import org.codehaus.aspectwerkz.transform.inlining.deployer.Deployer;
import org.codehaus.aspectwerkz.transform.inlining.deployer.DeploymentHandle;
import org.drips.utils.Utils;


/**
 * This Class is used to find out whether the aspectwerkz platform is
 * correctly intialized or not.It carries out a series of test to assert the
 * same. This class is a <b>singleton</b> which uses DripsIntializationTest
 * (Testcases) and DripsIntializationTestAspect (Associated Aspect) to Test
 * the same.
 * 
 * @author Saurabh Arora
 * @see DripsIntializationTest
 * @see DripsIntializationTestAspect
 */
public final class DripsIntializationTester {
    private static DripsIntializationTester instance = new DripsIntializationTester();

    /**
     * This method is used to get a instance of this singleton.
     * 
     * @return single unique instance of this class.
     */
    public static DripsIntializationTester getInstance() {
        if (instance == null) {
            synchronized (DripsIntializationTester.class) {
                instance = new DripsIntializationTester();
            }
        }
        return instance;
    }

    /**
     * Private contructor for this class, since it is a singleton.
     */
    private DripsIntializationTester() {
      super();
    }

    private void testXMLDeployment() throws Throwable {
        // dynamicpointcut
        String xmlDef = "<aspect name=\""
                + DripsIntializationTestAspect.class.getCanonicalName()
                + "\" >" + "<pointcut name=\"dynamicpointcut\" expression="
                + "\"execution(void "
                + "..DripsIntializationTest.method2())\"/>" + "</aspect>";
        Utils.debug(xmlDef);
        String[] calltrace =
            { "callPointcut", "method1", "dynamicpointcut", "method2" };
        DeploymentHandle handle = Deployer.deploy(
                DripsIntializationTestAspect.class, xmlDef);
        DripsIntializationTest test= new DripsIntializationTest(); 
        test.callallmethods();
        // undeploy using handle
        Deployer.undeploy(handle);
        String[] calls = DripsIntializationUtils.getCallStack();
        printcalls(calls);
        try {
            assertEqual(calltrace, calls);
        } catch (Exception e) {
            Utils.debug("Aspect test fails " + e.getMessage());
            throw e;
        }
    }

    private void assertEqual(final String[] calltrace, final String[] calls)
            throws Throwable {
        if (calltrace.length != calls.length) {
            throw new Exception("length mismatch");
        }
        for (int i = 0; i < calltrace.length; i++) {
            if (!calltrace[i].equals(calls[i])) {
                throw new Exception("data mismatch");
            }
        }
    }

    private void printcalls(final String[] calls) {
        Utils.debug("Call Trace={");
        for (int i = 0; i < calls.length; i++) {
            Utils.debug(calls[i] + ",");
        }
        Utils.debug("}");
    }

    private boolean tested = false;

    private boolean intialized = false;

    /**
     * This method is used to assert whether the drips system is intialized
     * properly or not.
     * 
     * @return true if aspectwerkz is intialized properly or else false.
     */
    public boolean isintialized() {
        if (!tested) {
            tested = true;
            testaspect();
        }
        return intialized;
    }

    private void testaspect() {
        try {

            testXMLDeployment();
            intialized = true;
        } catch (Throwable th) {
            th.printStackTrace();
        }

    }

}
