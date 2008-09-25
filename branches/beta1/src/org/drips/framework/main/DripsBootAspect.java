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
package org.drips.framework.main;

import org.codehaus.aspectwerkz.annotation.After;
import org.codehaus.aspectwerkz.annotation.Before;

/**
 * This Aspect can be used to boot an Drips Framework.The end user can define
 * an aop.xml to execute this aspect. It would inturn intialized the Drips
 * Framework. The end user should define a aop.xml such that initDrips is
 * called before the start of custom code and destroyDrips() at the shutdown
 * time.
 * 
 * @author Saurabh Arora
 */
public class DripsBootAspect extends DripsBaseBootstrap {

    private static DripsBootAspect instance = null;

    private static DripsBootAspect getInstance() {
        if (instance == null) {
            synchronized (DripsBootAspect.class) {
                instance = new DripsBootAspect();
            }
        }
        return instance;
    }

    /**
     * This method should be called before starting of the custom code.
     */
    @Before("call(public static void *.main) && !within(com.bea.drips.framework.*)")
    public static void initDrips() {
        // Intialize the aspect system.
        getInstance().intializeDrips();
    }

    /**
     * This method should be called at the end of the custom code execution.
     */
    @After("call(public static void *.main) && !within(com.bea.drips.framework.*)    ")
    public static void destroyDrips() {
        // Intialize the aspect system.
        getInstance().shutdownDrips();
    }
}
