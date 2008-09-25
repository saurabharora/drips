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
package org.drips.example.wordcount;

import java.io.FileReader;
import java.io.IOException;

/**
 * This is a crude implementation of wc utility. It counts no of words as of
 * now.
 * 
 * @author Saurabh Arora
 */
public class WordCounter {

    /**
     * The Main invocation method of this class.
     * 
     * @param args
     *            Arguments to the program.
     */
    public static void main(final String[] args) {
        if (args.length == 0) {
            usage();
            return;
        }
        WordCounter counter = new WordCounter();
        for (int i = 0; i < args.length; i++) {
            try {
                System.err.println("Word counter for file " + args[i]
                        + " is counter =" + counter.runcounter(args[i]));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * This method is used to find the wordsof charaters in the specified
     * file.
     * 
     * @param filename
     *            Name of the file to search.
     */
    public int runcounter(String filename) throws IOException {
        foo();
        int wordcount = 0;
        boolean isword = false;
        FileReader readerstream = null;
        try {
            readerstream = new FileReader(filename);
            char[] cbuf = new char[10];
            int count = 0;
            while ((count = readerstream.read(cbuf)) != -1) {
                for (int i = 0; i < count; i++) {
                    if ((cbuf[i] == '\n') || (cbuf[i] == ' ')
                            || (cbuf[i] == '\t'))
                    {
                        if (isword) {
                            isword = false;
                            wordcount++;
                        }
                    } else {
                        isword = true;
                    }
                }
            }
            readerstream.close();
            return wordcount;
        } finally {
            if (readerstream != null) {
                readerstream.close();
            }
        }
    }

    public void foo() {
        bar();
        System.out.println("WordCounter.foo");
    }

    public void bar() {
        System.out.println("WordCounter.bar");
    }

    /**
     * Usage Message for this Class.
     */
    private static void usage() {
        System.out.println("Usage: WordCounter <file 1> <file 2>");
    }
}
