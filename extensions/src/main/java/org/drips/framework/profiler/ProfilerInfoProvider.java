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
package org.drips.framework.profiler;

/**
 * This interface is used by ProfilerProcessor to access information about the
 * specific providers. </br> Few example of providers <p> aspectwerkx Provider
 * like TimingAspect </p> <p>Log providers </p> Note that
 * ProfilerInfoProviders are to be states less providers, wherein the actually
 * information is contained in the Object Passed into the system
 */
public interface ProfilerInfoProvider {

    /**
     * Provide a String Method Signature for this method.
     * 
     * @param type
     *            Object data provided by calling provider.
     * @return String representation of the calling method.
     */
    String getMethodSignature(Object type);

    /**
     * This returns Provider specific implementation of MethodInfo.
     * 
     * @param datainfo
     *            Object data provided by the calling provider.
     * @return MethodInfo specific for this providers.
     */
    MethodInfo createMethodInfo(Object datainfo);
}
