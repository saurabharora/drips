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
package org.drips.main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.drips.framework.main.DripsMain;

import junit.framework.TestCase;


/**
 * @author sarora TODO To change the template for this generated type comment
 *         go to Window - Preferences - Java - Code Style - Code Templates
 */
public class MainTest extends TestCase {

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(MainTest.class);
    }

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        String[] argument =
            { "com.bea.drips.example.wordcount.WordCounter", getTempDataFile().getAbsolutePath() };
        DripsMain.main(argument);
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testMain() {

    }
    protected File getTempDataFile() throws IOException {
		File data = File.createTempFile("data", "txt");
		data.deleteOnExit();
		FileOutputStream dataout = new FileOutputStream(data);
		StringBuffer buf = new StringBuffer();
		buf.append("Hi here hwo are you.");
		buf
				.append("This is a dummy file used in the source to process some example.");
		buf.append("\n");
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				buf.append("Dummy ");
			}
			buf.append("\n");
		}
		
		dataout.write(buf.toString().getBytes());
		dataout.close();
		return data;
	}
}
