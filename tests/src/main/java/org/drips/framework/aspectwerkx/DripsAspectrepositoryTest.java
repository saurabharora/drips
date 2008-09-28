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

import java.io.IOException;

import org.codehaus.aspectwerkz.transform.inlining.deployer.Deployer;
import org.codehaus.aspectwerkz.transform.inlining.deployer.DeploymentHandle;
import org.drips.example.wordcount.WordCounter;
import org.drips.framework.mbeans.TestAspect;
import org.drips.framework.test.AbstractDripsBaseTest;


// This test can only run in aspectwerkz started env
public class DripsAspectrepositoryTest extends AbstractDripsBaseTest {

    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testAspect() throws IOException{

        String xmlDef = "<aspect name=\""
                + TestAspect.class.getCanonicalName() + "\" >"
                + "<pointcut name=\"profilingcut\" expression=\"execution(public void ..WordCounter.runcounter(String))\"/>"
                + "<advice name=\"doGenericMethodProfiling\" type=\"around\" bind-to=\"profilingcut\"/>"
                + "</aspect>";
        System.out.println(xmlDef);

        DeploymentHandle handle = Deployer.deploy(TestAspect.class,xmlDef);
        WordCounter counter = new WordCounter();        
        counter.runcounter(getTempDataFile().getAbsolutePath());
        // undeploy using handle
        Deployer.undeploy(handle);
    }
}
