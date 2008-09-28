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
package org.drips.framework.main;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.drips.utils.Utils;


/**
 * This Class defines one of the Mechanism to bootstrap the Drips Framework.
 * This class contains the main method which can be used to boot an
 * application.
 * <p> Usage: </p> <b> java
 * com.bea.drips.framework.main.DripsMain Program name &lt;argument for the
 * program&gt; </b>
 * 
 * @author Saurabh Arora
 */
public class DripsMain extends DripsBaseBootstrap {
    /**
     * The DripsMain method to boot the Drips Framework.
     * 
     * @param args
     *            Array of Arguments.
     */
    public static void main(final String[] args) {
        if (args.length == 0) {
            usage();
            return;
        }
        DripsMain m = new DripsMain();
        m.run(args);
        return;
    }

    /**
     * This is the primary method for execution in DripsMain,it wraps over all
     * the other method and provide the need support for startup,shutdown of
     * framework and running of user application.
     * 
     * @param args
     *            String array of arguments.
     */
    public void run(final String[] args) {
        try {
            start();
            startMainProgram(args);
        } finally {
            shutdown();
        }

    }

    /**
     * This method is used to call the user program.
     * 
     * @param args
     *            arguments passed to main, which contains program name as the
     *            first argument.
     */
    public void startMainProgram(final String[] args) {
        try {
            String programname = args[0];
            String[] progarg = null;
            if (args.length > 1) {
                progarg = new String[args.length - 1];
                for (int i = 1; i < args.length; i++) {
                    progarg[i - 1] = args[i];
                }
            } else {
                progarg = new String[0];
            }

            Class progclass = Class.forName(programname);
            Class[] c = new Class[1];
            c[0] = Array.newInstance(String.class, 1).getClass();
            Method mainmethod = progclass.getMethod("main", c);
            if (mainmethod == null) {
                Utils.debug("Cannot Find DripsMain method for "
                        + programname);
                return;
            }
            // This main should be a static method
            if ((mainmethod.getModifiers() & Modifier.STATIC) == 0) {
                System.out.println("Cannot Find DripsMain method for "
                        + programname);
            }

            // This main should have return type as void
            if (mainmethod.getReturnType() != void.class) {
                System.out.println("Cannot Find DripsMain method for "
                        + programname);
            }

            // Main has a single parameter as String[]
            Object[] params = new Object[1];
            params[0] = (Object) progarg;
            mainmethod.invoke(null, params);

        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Method to shutdown the system.
     */
    public void shutdown() {
        shutdownDrips();
    }

    /**
     * This method is used to intialize the Drips system.
     */
    public void start() {
        boolean intialize = intializeDrips();
        if (!intialize) {
            System.out.println("Cannot Intialize Drips");
            return;
        }
    }

    private static void usage() {
        System.out
                .println("Usage: DripsMain <Program name> <argument for the program> ");
    }
}
