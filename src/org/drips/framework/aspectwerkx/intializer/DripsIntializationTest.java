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


/**
 * This class contains the testcase required to assert whether aspectwerkz is
 * intialized or not.
 * 
 * @author Saurabh Arora
 */
public class DripsIntializationTest {
    /**
     * Default constructor, It's access level is package so that only
     * DripsIntializationTest can recreate it.
     */
    DripsIntializationTest() {
        super();
    }

   

    void method1() {
        DripsIntializationUtils.methodcalled("method1");
    }

    void method2() {
        DripsIntializationUtils.methodcalled("method2");
    }

    void callallmethods() {
        method1();
        method2();
    }

    

}
