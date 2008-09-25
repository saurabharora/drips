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
package org.drips.framework.aspectwerkx;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.IntrospectionException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.NotCompliantMBeanException;
import javax.management.ReflectionException;
import javax.management.StandardMBean;

import org.drips.framework.apt.DripsBeanInfo;
import org.drips.framework.apt.DripsBeanMethodInfo;
import org.drips.utils.Utils;


/**
 * This DynamicMBean provides the support for creation of Mbean form
 * Anontations defined in the code.
 * 
 * @author Saurabh Arora
 */
public class DripsDynamicAspectMBean extends AbstractDripsAspect implements
        DynamicMBean {

    private MBeanInfo beaninfo;

    private InternalDripsAspectInfo info;

    private Map<MethodKey, Method> methodMap = new HashMap<MethodKey, Method>();

    private Map<String, AttributeInfo> attributeMap = new HashMap<String, AttributeInfo>();

    public DripsDynamicAspectMBean(final Class<? extends DripsAspect> abase) {
        generateInfo(abase);
    }

    public Object getAttribute(final String attribute)
            throws AttributeNotFoundException, MBeanException,
            ReflectionException {
        try {
            AttributeInfo info = attributeMap.get(attribute);
            if (info != null) {
                return info.getter.invoke(null);
            }

        } catch (IllegalArgumentException e) {
            throw new MBeanException(e,
                    "Exception in execution of mbean method.");
        } catch (IllegalAccessException e) {
            throw new MBeanException(e,
                    "Exception in execution of mbean method.");
        } catch (InvocationTargetException e) {
            throw new MBeanException(e,
                    "Exception in execution of mbean method.");
        }
        return null;
    }

    public void setAttribute(final Attribute attribute)
            throws AttributeNotFoundException,
            InvalidAttributeValueException, MBeanException,
            ReflectionException {
        try {
            AttributeInfo info = attributeMap.get(attribute.getName());
            if (info != null) {
                Object value = attribute.getValue();
                Object[] ob = new Object[1];
                ob[0] = value;
                info.setter.invoke(null, ob);
            }

        } catch (IllegalArgumentException e) {
            throw new MBeanException(e,
                    "Exception in execution of mbean method.");
        } catch (IllegalAccessException e) {
            throw new MBeanException(e,
                    "Exception in execution of mbean method.");
        } catch (InvocationTargetException e) {
            throw new MBeanException(e,
                    "Exception in execution of mbean method.");
        }
    }

    public AttributeList getAttributes(final String[] attributes) {
        AttributeList list = new AttributeList();
        for (int i = 0; i < attributes.length; i++) {
            Object value = null;
            try {
                value = getAttribute(attributes[i]);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (value != null) {
                    list.add(new Attribute(attributes[i], value));
                }
            }
        }
        return list;
    }

    public AttributeList setAttributes(final AttributeList attributes) {
        Iterator<Object> attrIter = attributes.iterator();

        while (attrIter.hasNext()) {
            Attribute attr = (Attribute) attrIter.next();
            try {
                setAttribute(attr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    protected Class getDeployableAspect() {
        try {
            return Class.forName(beaninfo.getClassName());
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    
    public Object invoke(final String actionName, final Object[] params,
            final String[] signature) throws MBeanException,
            ReflectionException {
        MethodKey key = new MethodKey(actionName, signature);
        if (methodMap.containsKey(key)) {
            Method method = methodMap.get(key);
            try {
                Utils.debug("Method Invoke"+actionName);
                if (Modifier.isStatic(method.getModifiers())) {
                    // TODO: we are invoking only static method, on custom
                    // implemetation.
                    return method.invoke(null, params);
                } else {
                    // This is a method on this instance, deployment methods.
                    return method.invoke(this, params);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                throw new MBeanException(e,
                        "Exception in execution of mbean method.");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                throw new MBeanException(e,
                        "Exception in execution of mbean method.");
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                throw new MBeanException(e,
                        "Exception in execution of mbean method.");
            }

        } else {
            System.out.println("Unknown Action=" + actionName);
            throw new MBeanException(null, "Unknown Action=" + actionName);
        }
    }

    public MBeanInfo getMBeanInfo() {
        return beaninfo;
    }

    private void generateInfo(final Class<? extends DripsAspect> aclass) {
        // TODO: This class is the implementation of DripsAspect Info.
        if (aclass.isAnnotationPresent(DripsBeanInfo.class)) {
            DripsBeanInfo dbeaninfo = (DripsBeanInfo) aclass
                    .getAnnotation(DripsBeanInfo.class);
            String mbeanDescription = dbeaninfo.value();
            String mbeanClassName = dbeaninfo.mbeanImplClass();
            String objectname = dbeaninfo.objectName();

            String beaninterface = dbeaninfo.mbeanInterface();
            if (mbeanClassName.equals("[unassigned]")) {
                mbeanClassName = aclass.getName();
            } else {

                // we have a custom mbean implementations
                if (objectname.equals("[unassigned]")) {
                    objectname = "com.bea.drips.framework:type="
                            + mbeanClassName;
                }

                // Create instance of mbeanClass.
                try {
                    Class mbeanClass = Class.forName(mbeanClassName);
                    Object mbeaninstance = mbeanClass.newInstance();
                    Class mbeanInfClass = Class.forName(beaninterface);
                    StandardMBean mbean = new StandardMBean(mbeaninstance,
                            mbeanInfClass);
                    MBeanInfo info1 = mbean.getMBeanInfo();
                    beaninfo = new MBeanInfo(info1.getClassName(),
                            mbeanDescription, info1.getAttributes(), info1
                                    .getConstructors(),
                            info1.getOperations(), info1.getNotifications());
                    info = new InternalDripsAspectInfo(beaninfo, objectname,
                            beaninterface);
                    return;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NotCompliantMBeanException e) {
                    e.printStackTrace();
                }
                return;
            }
            if (beaninterface.equals("[unassigned]")) {
                beaninterface = DripsAspect.class.getName();
            }
            if (objectname.equals("[unassigned]")) {
                objectname = "com.bea.drips.framework:type=" + mbeanClassName;
            }
            methodMap.clear();
            Method[] methods = aclass.getMethods();
            DripsBeanMethodInfo methodinfo;
            List<MBeanOperationInfo> mbeanOperationInfos = new ArrayList<MBeanOperationInfo>();
            Map<String, MBeanAttributeInfo> mbeanAttributemap = new HashMap<String, MBeanAttributeInfo>();
            for (int i = 0; i < methods.length; i++) {
                if ((methodinfo = (DripsBeanMethodInfo) methods[i]
                        .getAnnotation(DripsBeanMethodInfo.class)) != null)
                {
                    // This method is anontated.
                    // Let me create MbeanMethodInfo
                    // public MBeanOperationInfo(String description,
                    // Method method)
                    String description = methodinfo.value();
                    String methodname = methods[i].getName();
                    Class[] Parametertypes = methods[i].getParameterTypes();
                    Class returnClassType = methods[i].getReturnType();
                    if (description == null) {
                        description = methods[i].getName();
                    }
                    if (Parametertypes.length == 0) {
                        // Okay we may be looking at a getter.
                        if (methodname.startsWith("get")
                                && methodname.length() > 3
                                && !(returnClassType == Void.TYPE))
                        {
                            // This is a getter for a attribute.
                            createAttributeInfo(methodname.substring(3),
                                    returnClassType.getName(), description,
                                    mbeanAttributemap, methods[i], null);
                            continue;
                        } else if (methodname.startsWith("is")
                                && methodname.length() > 2
                                && ((returnClassType == Boolean.TYPE) || (returnClassType == Boolean.class)))
                        {
                            // Is Boolean type of setter.
                            createAttributeInfo(methodname.substring(2),
                                    returnClassType.getName(), description,
                                    mbeanAttributemap, methods[i], null);
                            continue;
                        }
                    } else if (methodname.startsWith("set")
                            && methodname.length() > 3
                            && Parametertypes.length == 1
                            && (returnClassType == Void.TYPE))
                    {
                        // This is a case of setter.
                        createAttributeInfo(methodname.substring(3),
                                returnClassType.getName(), description,
                                mbeanAttributemap, null, methods[i]);
                        continue;
                    }
                    MBeanOperationInfo opinfo = new MBeanOperationInfo(
                            description, methods[i]);
                    mbeanOperationInfos.add(opinfo);
                    MethodKey key = new MethodKey(methodname,
                            getsignature(methods[i]));
                    methodMap.put(key, methods[i]);
                }
            }
            if (!mbeanOperationInfos.isEmpty()
                    || !mbeanAttributemap.isEmpty())
            {

                MBeanOperationInfo[] operationInfos = new MBeanOperationInfo[mbeanOperationInfos
                        .size()];
                mbeanOperationInfos.toArray(operationInfos);
                MBeanAttributeInfo[] attributeInfos = new MBeanAttributeInfo[mbeanAttributemap
                        .size()];
                mbeanAttributemap.values().toArray(attributeInfos);
                beaninfo = new MBeanInfo(mbeanClassName, mbeanDescription,
                        attributeInfos, null, operationInfos, null);
                info = new InternalDripsAspectInfo(beaninfo, objectname,
                        beaninterface);
                info.setDynamic(true);
                return;
            }
            // TODO: throw a detailed error.
        }
        // TODO: Provide a more detailed exception.
        throw new IllegalArgumentException("DripsClass " + aclass
                + " is not anontated");

    }

    private void createAttributeInfo(String attributeName, String type,
            String description,
            Map<String, MBeanAttributeInfo> mbeanAttributemap,
            Method gettermethod, Method settermethod) {
        try {

            MBeanAttributeInfo attrInfo = null;
            AttributeInfo prevAttrInfo = attributeMap.get(attributeName);
            if (prevAttrInfo == null) {
                prevAttrInfo = new AttributeInfo();
                prevAttrInfo.getter = gettermethod;
                prevAttrInfo.setter = settermethod;
            } else {
                if ((gettermethod != null) && (prevAttrInfo.getter == null)) {
                    prevAttrInfo.getter = gettermethod;
                }
                if ((settermethod != null) && (prevAttrInfo.setter == null)) {
                    prevAttrInfo.setter = settermethod;
                }
            }
            attrInfo = new MBeanAttributeInfo(attributeName, description,
                    prevAttrInfo.getter, prevAttrInfo.setter);

            // replace the original AttributeInfos.
            mbeanAttributemap.put(attributeName, attrInfo);
            attributeMap.put(attributeName, prevAttrInfo);
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param method
     * @return
     */
    private String[] getsignature(final Method method) {
        Class<?>[] classes = method.getParameterTypes();
        String[] classnames = new String[classes.length];
        for (int i = 0; i < classes.length; i++) {
            classnames[i] = classes[i].getName();
        }
        return classnames;
    }

    public InternalDripsAspectInfo getInfo() {
        return info;
    }

    static class MethodKey {
        private final String methodname;

        private final String[] signature;

        private int hashcode;

        MethodKey(final String amethodname, final String[] asignature) {
            if (amethodname == null) {
                throw new IllegalArgumentException(
                        "methodName cannot be null");

            }
            if (asignature == null) {
                throw new IllegalArgumentException(
                        "Method Signature cannot be null");
            }
            methodname = amethodname;
            signature = asignature;
            hashcode = methodname.hashCode()
                    + getSignatureHashCode(signature);
        }

        private int getSignatureHashCode(final String[] signatures) {
            int hash = 0;
            for (int i = 0; i < signatures.length; i++) {
                hash += signatures[i].hashCode();
            }
            return hash;
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj instanceof MethodKey) {
                MethodKey key = (MethodKey) obj;
                if (hashcode == key.hashcode) {
                    if ((methodname.equals(key.methodname))
                            && (signature.length == key.signature.length))
                    {
                        // equality of arguments.
                        for (int i = 0; i < signature.length; i++) {
                            if (!signature[i].equals(key.signature[i])) {
                                return false;
                            }
                        }
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        public int hashCode() {
            return hashcode;
        }

        @Override
        public String toString() {
            StringBuffer buf = new StringBuffer();
            buf.append(methodname + "[");
            for (int i = 0; i < signature.length; i++) {
                buf.append(signature[i]);
            }
            buf.append("]");
            return buf.toString();
        }
    }

    static class AttributeInfo {
        Method getter;

        Method setter;
    }
}
