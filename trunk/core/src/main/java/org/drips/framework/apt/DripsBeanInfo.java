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
package org.drips.framework.apt;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author sarora TODO To change the template for this generated type comment
 *         go to Window - Preferences - Java - Code Style - Code Templates
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DripsBeanInfo {
    /**
     * For DripsBeanInfo value means the Description for this mbean
     */
    String value();

    /**
     * Name of the Mbean assciated with this class, this is not mandatory. If
     * not specfied, classname+"Mbean" is the class Name.
     */
    String mbeanImplClass() default "[unassigned]";

    /**
     * ObjectName of the Mbean used to register the Mbean with the
     * MbeanServer.The default value is "com.bea.drips.framework:type=" +
     * <mbeanName>.
     */
    String objectName() default "[unassigned]";

    /**
     * Mbean Interface associated with this class.
     */
    String mbeanInterface() default "[unassigned]";
}
