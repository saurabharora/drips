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

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * The basic Method Information class, which stores only the start , end and
 * elapsed time.This is the foundation class to be inherited by other classes,
 * which has more specific MethodInformation to Store.
 * <p>
 * <b>Note:</b>This class implements Serializable and should be able to
 * serializable to disk, since it used as a UserObject in the Profiling Tree.
 * </p>
 * 
 * @author Saurabh Arora, Prasen Mukherjee
 */
public abstract class MethodInfo implements Serializable {

    private long startTime, endTime, elapsedTime;

    private Map metadata = new HashMap();

    public static final String THREAD_ID = "THREAD_ID";

    protected MethodInfo() {
        Thread curr_thr = Thread.currentThread();
        String threadID = curr_thr.getName() + "__" + curr_thr.getId();
        putMetaData(THREAD_ID, threadID);
    }

    public String getThreadIdenitifier() {
        return (String) getMetaData(THREAD_ID);
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(final long starttime) {
        startTime = starttime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(final long endtime) {
        endTime = endtime;
    }

    public void setElapsedTime(final long elapsed) {
        elapsedTime = elapsed;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public Object getMetaData(Object key) {
        return metadata.get(key);
    }

    public Map getMetaDataMap() {
        return Collections.unmodifiableMap(metadata);
    }

    protected Object putMetaData(Object key, Object value) {
        return metadata.put(key, value);
    }

}
