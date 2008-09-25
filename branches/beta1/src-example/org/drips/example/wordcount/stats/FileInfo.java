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

import java.io.Serializable;

/**
 * File Related Information that can we extracted using
 * WordCounterStatsAspect.
 * 
 * @author Saurabh Arora.
 */
public class FileInfo implements Serializable {

    /**
     * Default UID for this Class.
     */
    private static final long serialVersionUID = 3906646392972916021L;

    private final int wordCount;

    private final String fileName;

    private final long timeElapsed;

    public FileInfo(final String name, final int aWordCount,
            final long elapsedtime) {
        wordCount = aWordCount;
        fileName = name;
        timeElapsed = elapsedtime;
    }

    /**
     * Returns the Word Count of this File.
     * 
     * @return
     */
    public long getWordCount() {
        return wordCount;
    }

    /**
     * Gets the Time take to process this message.
     * 
     * @return time elapsed as long.
     */
    public long getProcessingTime() {
        return timeElapsed;
    }

}
