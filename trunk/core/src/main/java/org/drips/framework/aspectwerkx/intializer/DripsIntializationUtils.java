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

import java.util.ArrayList;
import java.util.List;

import org.drips.utils.Utils;


final class DripsIntializationUtils {
    /* Since this is a utility class, hence a private constructor */
    private DripsIntializationUtils() {

    }

    private static List<String> methods = new ArrayList<String>();

    static void methodcalled(final String name) {
        Utils.debug(name);
        methods.add(name);
    }

    static String[] getCallStack() {
        int length = methods.size();
        if (length == 0) {
            return new String[0];
        } else {
            String[] calls = new String[length];
            methods.toArray(calls);
            return calls;
        }
    }

    static void resetCallStack() {
        methods.clear();
    }
}
