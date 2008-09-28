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

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class WordCounterGUI {
    public static void main(String[] args) {
        final JFrame frame = new JFrame("WRODCOUNTER GUI");
        final WordCounter wc = new WordCounter();
        JButton button = new JButton("Select File to run wordcount on");
        button.addActionListener(new ActionListener() {
            // WordCounter wc = new WordCounter();
            public void actionPerformed(ActionEvent e) {
                FileDialog fd = new FileDialog(frame);
                fd.setVisible(true);
                File file = new File(fd.getDirectory(), fd.getFile());
                System.out.println("file = " + file.getAbsolutePath());
                try {

                    int wordcount = wc.runcounter(file.getAbsolutePath());
                    System.err.println("Word counter for file "
                            + file.getAbsolutePath() + " is counter ="
                            + wordcount);

                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        frame.getContentPane().add(button);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }
}
