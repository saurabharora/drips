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
package org.drips.example.hello;

import java.io.IOException;

/**
 * This is a Simple HelloWorld Example for using Drips.
 * 
 * @author Saurabh Arora
 */
public class HelloWorld {

    /**
     * The Main method for this program
     * 
     * @param args
     */
    public static void main(String[] args) {
        (new HelloWorld()).run();
    }

    void run() {
        try {
            //Read Name From Command Line
            String name = prompt();
            while (!name.equals("")) {
                outputGreetings(name);
                name = prompt();
            }
            System.out.println("Thanks, Bye");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void outputGreetings(String name) {
        System.out.println("Hello " + name + " Welcome to Drips World");
    }

    private String prompt() throws IOException {
        System.out.print("\nEnter your Name:");
        System.out.flush();
        StringBuffer buf = new StringBuffer();
        char ch;
        while ((ch = (char) System.in.read()) != '\n') {
            buf.append(ch);
        }
        buf.append("\n");
        return buf.toString().trim();
    }

}
