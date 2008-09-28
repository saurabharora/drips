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

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.codehaus.aspectwerkz.joinpoint.JoinPoint;
import org.codehaus.aspectwerkz.joinpoint.MethodRtti;
import org.codehaus.aspectwerkz.joinpoint.Rtti;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * @author Prasen Mukherjee
 */
public class HttpServletMethodInfo extends AspectwerkzMethodInfo {

    /**
     * Default Serialization UID.
     */
    private static final long serialVersionUID = 3257562927752820025L;

    public static final String HTTP_SESSION_ID = "HTTP_SESSION_ID";

    public static final String SERVLET_INFO_ID = "SERVLET_INFO_ID";

    public static final String SERVLET_NAME_ID = "SERVLET_NAME_ID";

    public HttpServletMethodInfo(final JoinPoint jp) {
        super(jp);

        HttpServlet servlet = (HttpServlet) jp.getTarget();
        putMetaData(SERVLET_INFO_ID, servlet.getServletInfo());
        putMetaData(SERVLET_NAME_ID, servlet.getServletName());
        Rtti rtti = jp.getRtti();
        if (rtti instanceof MethodRtti) {
            MethodRtti methodRtti = (MethodRtti) rtti;
            Object[] params = methodRtti.getParameterValues();
            for (int i = 0; i < params.length; i++) {
                Object param = params[i];
                if (param instanceof HttpServletRequest) {
                    HttpServletRequest req = (HttpServletRequest) param;
                    HttpSession session = req.getSession(false);
                    putMetaData(HTTP_SESSION_ID, new SerializableHttpSession(session));
                }
            }
        }
    }
}

class SerializableHttpSession implements Serializable {
    private final Map<String,Serializable> attributeMap = new HashMap<String,Serializable>();
    private final String sessionId;
    public SerializableHttpSession(HttpSession session) {
        Enumeration enum_ = session.getAttributeNames();
        while (enum_.hasMoreElements()) {
            String attrib_name = (String) enum_.nextElement();
            Object attrib_value = session.getAttribute(attrib_name);
            if (attrib_value instanceof Serializable) attributeMap.put(attrib_name,(Serializable)attrib_value);
            else attributeMap.put(attrib_name,attrib_value.toString());
        }
        sessionId = session.getId();
    }

    public Map getAttibuteMap() {
        return Collections.unmodifiableMap(attributeMap);
    }

    public String toString() {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append("\nSessionAttributeMap for session: "+sessionId+"\n");
        for (Iterator<Entry<String,Serializable>> iterator = attributeMap.entrySet().iterator(); iterator
                .hasNext();)
        {
            Entry entry = iterator.next();
            sbuf.append("" + entry.getKey() + ": " + entry.getValue()
                    + ", ");
        }
        sbuf.append("\n");
        return sbuf.toString();
    }
}
