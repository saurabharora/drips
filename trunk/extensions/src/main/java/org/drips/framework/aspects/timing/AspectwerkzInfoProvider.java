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
package org.drips.framework.aspects.timing;

import javax.servlet.http.HttpServlet;

import org.codehaus.aspectwerkz.joinpoint.JoinPoint;
import org.drips.framework.aspects.timing.methodinfo.AspectwerkzMethodInfo;
import org.drips.framework.aspects.timing.methodinfo.HttpServletMethodInfo;
import org.drips.framework.profiler.MethodInfo;
import org.drips.framework.profiler.ProfilerInfoProvider;


/**
 * This is the default implementation of ProfilerInfoProvider for a
 * Aspectwerkz kind of source.
 * 
 * @author Saurabh Arora
 */
public class AspectwerkzInfoProvider implements ProfilerInfoProvider {


    public String getMethodSignature(final Object type) {
        JoinPoint point = (JoinPoint) type;
        return point.getSignature().toString();
    }

    public MethodInfo createMethodInfo(final Object datainfo) {
        JoinPoint jp = (JoinPoint) datainfo;
        MethodInfo methodInfo = null;

        if (jp.getTarget() instanceof HttpServlet) {
            methodInfo = new HttpServletMethodInfo(jp);
        } else {
            methodInfo = new AspectwerkzMethodInfo(jp);
        }

        return methodInfo;

    }
}
