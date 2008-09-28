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
package org.drips.test.example.hello;

import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import org.codehaus.aspectwerkz.joinpoint.JoinPoint;
import org.drips.example.hello.HelloWorld;
import org.drips.example.hello.HelloWorldAspect;
import org.drips.framework.aspectwerkx.DripsAspectInfo;
import org.drips.framework.main.DripsMain;
import org.drips.framework.test.AbstractDripsBaseTest;


public class HelloWorldTest extends AbstractDripsBaseTest {

    private DripsMain m = new DripsMain();

    private String[] argument =
        { "com.bea.drips.example.hello.HelloWorld" };

    protected void setUp() throws Exception {
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
        getDripsMainMBean().enableAspect(HelloWorldAspect.class.getName());
        DripsAspectInfo aspectinfo = getDripsMainMBean().getAspectInfo(
                HelloWorldAspect.class.getName());

        ObjectName name = new ObjectName(aspectinfo.getObjectName());
        MBeanServerConnection mbsc = getMBeanServerConnection();

        PipedOutputStream out = new PipedOutputStream();
        PipedInputStream in = new PipedInputStream(out);
        String name1 = new String("testuser1\n");
        String name2 = new String("testuser2\n");
        String name3 = new String("testuser3\n");

        RunHello hello = new RunHello(in);
        hello.start();
        // This call should not be recorded, till be deploy
        out.write(name1.getBytes());
        out.flush();
        // // Sleep for the HelloWorld program to read the Input.
        Thread.sleep(1000);
        Object[] parameter = new Object[3];
        String[] signature = new String[3];
        signature[0] = String.class.getName();
        signature[1] = String.class.getName();
        signature[2] = String.class.getName();

        parameter[0] = "execution( private String com.bea.drips.example.hello.HelloWorld.prompt())";
        parameter[1] = "doAroundPromptMessage(JoinPoint jp)";
        parameter[2] = "around";
        // Object[] parameter = new Object[0];
        // String[] signature = new String[0];
        mbsc.invoke(name, "deploy", parameter, signature);
        Thread.sleep(2000);
        // RunHello hello = new RunHello(in);
        // hello.start();
        out.write(name1.getBytes());
        out.write(name1.getBytes());
        out.write(name2.getBytes());
        out.write(name1.getBytes());
        out.flush();
        // Sleep for the HelloWorld program to read the Input.
        Thread.sleep(1000);
        parameter = new Object[1];
        signature = new String[1];
        signature[0] = String.class.getName();
        parameter[0] = "testuser1";
        Integer count = (Integer) mbsc.invoke(name, "getNameCount",
                parameter, signature);

        assertEquals(count.intValue(), 2);

        parameter[0] = "testuser2";
        count = (Integer) mbsc.invoke(name, "getNameCount", parameter,
                signature);
        assertEquals(count.intValue(), 1);

        parameter[0] = "testuser3";
        count = (Integer) mbsc.invoke(name, "getNameCount", parameter,
                signature);
        assertEquals(count.intValue(), 0);

        mbsc.invoke(name, "undeploy", parameter, signature);
        getDripsMainMBean().disable();
        out.write("\r\n".getBytes());
        out.flush();
        out.close();
    }

    class RunHello extends Thread {
        RunHello(InputStream stream) {
            System.setIn(stream);
        }

        public void run() {
            HelloWorld.main(argument);
        }
    }

}
