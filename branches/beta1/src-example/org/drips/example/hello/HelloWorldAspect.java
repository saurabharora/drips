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
package org.drips.example.hello;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.aspectwerkz.annotation.Around;
import org.codehaus.aspectwerkz.annotation.Before;
import org.codehaus.aspectwerkz.joinpoint.JoinPoint;
import org.drips.framework.apt.DripsBeanInfo;
import org.drips.framework.apt.DripsBeanMethodInfo;
import org.drips.framework.aspectwerkx.AbstractDripsAspect;
import org.drips.utils.Utils;


/**
 * This is a simple DripsAspect to instrument and obtain data from HelloWorld
 * Java Class.
 * 
 * @author sarora
 */
@DripsBeanInfo("Mbean to access Information for Hello World Program")
public class HelloWorldAspect extends AbstractDripsAspect {
    static Map<String, Integer> nameCount = new HashMap<String, Integer>();

    /**
     * The default instrumentation method to be invoked around call to
     * HelloWorld.prompt()
     * 
     * @throws Throwable
     */
    public Object doAroundPromptMessage(JoinPoint jp) throws Throwable {
        String name = (String) jp.proceed();
        Utils.debug("Called doAroundPromptMessage =" + name);
        Integer counter = null;
        synchronized (nameCount) {
            if (nameCount.containsKey(name)) {
                counter = nameCount.get(name);
                counter = new Integer(counter.intValue() + 1);
            } else {
                counter = new Integer(1);
            }
            nameCount.put(name, counter);
        }

        return name;
    }

    /**
     * Returns the No of times a particular Name is entered. This method is
     * exposed as the Mbean Method.
     * 
     * @param name
     *            The Name for which count is required.
     * @return int, counter value for the specified name.
     */
    @DripsBeanMethodInfo("Access the Number of Times a Name is entered.")
    public static int getNameCount(String name) {
        if (nameCount.containsKey(name)) {
            return nameCount.get(name).intValue();
        } else {
            return 0;
        }
    }
}
