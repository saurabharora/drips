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

import org.drips.framework.aspectwerkx.AbstractDripsAspect;

/**
 * @author Saurabh Arora
 */
@DripsBeanInfo(value = "AptAspect2", objectName = "com.bea:type=AptAspect2")
public class AptAspect2 extends AbstractDripsAspect {

    private static int count = 1;

    @DripsBeanMethodInfo("Counter")
    public static int counterValue() {
        return count++;
    }
}
