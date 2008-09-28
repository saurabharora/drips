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
package org.drips.test.example.wordcount;

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;

import org.drips.example.wordcount.stats.FileInfo;
import org.drips.example.wordcount.stats.WordCounterStatsAspect;
import org.drips.example.wordcount.stats.WordCounterStatsInf;
import org.drips.framework.aspectwerkx.DripsAspectInfo;
import org.drips.framework.main.DripsMain;
import org.drips.framework.test.AbstractDripsBaseTest;


public class WordCounterStatsTest extends AbstractDripsBaseTest {

    private DripsMain m = new DripsMain();

    private String[] argument = null;

    protected void setUp() throws Exception {
    	String[] argument1 =
        { "com.bea.drips.example.wordcount.WordCounter", getTempDataFile().getAbsolutePath(),
        	getTempDataFile().getAbsolutePath() };
    	argument = argument1;
    	
        System.out.println("DripsMainTest Called");
        super.setUp();

        if (isLocalJVM()) {
            m.start();
        }
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        m.shutdown();
    }

    public void testDeploy() throws Exception {
        MBeanServerConnection mbs = getMBeanServerConnection();
        getDripsMainMBean().enableAspect(
                WordCounterStatsAspect.class.getName());
        DripsAspectInfo aspectinfo = getDripsMainMBean().getAspectInfo(
                WordCounterStatsAspect.class.getName());

        ObjectName name = new ObjectName(aspectinfo.getObjectName());
        MBeanServerConnection mbsc = getMBeanServerConnection();
        WordCounterStatsInf aptimpl = (WordCounterStatsInf) MBeanServerInvocationHandler
                .newProxyInstance(mbs, new ObjectName(aspectinfo
                        .getObjectName()), WordCounterStatsInf.class, false);
        aptimpl.deploy("execution(public int ..WordCounter.runcounter(..))",
                "doWCDataExtraction(JoinPoint jp)", "around");
        m.startMainProgram(argument);
        FileInfo finfo = aptimpl.getFileInfo("hello.txt");
        assertNotNull(finfo);
        assertEquals(finfo.getWordCount(), 146);
        assertTrue(finfo.getProcessingTime() > 0);
        assertEquals(aptimpl.getAverageFileSize(), 146);
        aptimpl.undeploy();
    }

}
