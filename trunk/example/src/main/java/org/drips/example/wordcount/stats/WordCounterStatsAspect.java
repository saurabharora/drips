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
package org.drips.example.wordcount.stats;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.aspectwerkz.joinpoint.JoinPoint;
import org.codehaus.aspectwerkz.joinpoint.MethodRtti;
import org.drips.framework.apt.DripsBeanInfo;
import org.drips.framework.apt.DripsBeanMethodInfo;
import org.drips.framework.aspectwerkx.AbstractDripsAspect;
import org.drips.utils.Utils;


@DripsBeanInfo(value = "This Mbean allows you to obtain Wordcounter statitics", mbeanInterface = "com.bea.drips.example.wordcounter.stats.WorkCounterStats")
public class WordCounterStatsAspect extends AbstractDripsAspect {

    private static Map<String, FileInfo> fileInfosMap = new HashMap<String, FileInfo>();

    private static int totalFileSize, NoOfFiles;

    public Object doWCDataExtraction(final JoinPoint joinPoint)
            throws Throwable {
        int wordcount = -1;
        long starttime = 0, endtime = 0;
        MethodRtti method = (MethodRtti) joinPoint.getRtti();
        String filename = (String) method.getParameterValues()[0];
        try {
            starttime = System.currentTimeMillis();
            wordcount = ((Integer) joinPoint.proceed()).intValue();
            endtime = System.currentTimeMillis();
            totalFileSize += wordcount;
            NoOfFiles++;
        } finally {
            fileInfosMap.put(filename, new FileInfo(filename, wordcount,
                    endtime - starttime));
            Utils.debug("File Processed=" + filename);
        }
        return wordcount;
    }

    @DripsBeanMethodInfo(value = "Gives you File Related Information for the specified File.")
    public static FileInfo getFileInfo(final String filename) {
        Utils.debug("Looking for FileStats=" + filename);
        if (fileInfosMap.containsKey(filename)) {
            return fileInfosMap.get(filename);
        }
        return null;
    }

    @DripsBeanMethodInfo(value = "Gives you Average File Size.")
    public static int getAverageFileSize() {
        if (NoOfFiles == 0) {
            return 0;
        } else {
            return totalFileSize / NoOfFiles;
        }
    }

}
