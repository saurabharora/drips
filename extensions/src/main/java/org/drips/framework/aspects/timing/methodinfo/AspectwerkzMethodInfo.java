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
package org.drips.framework.aspects.timing.methodinfo;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.codehaus.aspectwerkz.joinpoint.JoinPoint;
import org.drips.framework.profiler.MethodInfo;
import org.drips.utils.Utils;


/**
 * This is the extension of the default MethodInfo in an Aspectwerkz System.
 * It currently only stores methodSignature, we need to store other data like
 * methodRtti etc.
 * 
 * @author Saurabh Arora, Prasen Mukherjee
 */
public class AspectwerkzMethodInfo extends MethodInfo {
    public static final String METHOD_SIGNATURE_ID = "METHOD_SIGNATURE_ID";

    private static final long serialVersionUID = 1L;

    private final String methodSignature;
    public AspectwerkzMethodInfo(final JoinPoint datapoint) {
        methodSignature = Utils.getSignatureString(datapoint);
        putMetaData(METHOD_SIGNATURE_ID, methodSignature);
    }

    public String getMethodSignature() {
        return methodSignature;
    }

    public String toString() {
        StringBuffer sbuf = new StringBuffer();

        sbuf.append("< Method=" + getMethodSignature()+" > ");
        Map metaDataMap = getMetaDataMap();
        for (Iterator<Entry> iterator = metaDataMap.entrySet().iterator(); iterator
                .hasNext();)
        {
            Entry entry = iterator.next();
            sbuf.append("< " + entry.getKey() + ": " + entry.getValue()
                    + " > ");
        }
        /*
         * sbuf.append("Method: "+getMetaData(METHOD_SIGNATURE_ID));
         * sbuf.append(", Thread: "+getMetaData(THREAD_ID));
         */
        sbuf.append(",  ElapsedTime: " + getElapsedTime() + "<"
                + getEndTime() + "--" + getStartTime() + ">");
        return sbuf.toString();
    }

}
