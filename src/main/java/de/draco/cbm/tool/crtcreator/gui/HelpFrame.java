/*
 * Decompiled with CFR 0.137.
 */
package de.draco.cbm.tool.crtcreator.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class HelpFrame
extends EasyFrame
implements ActionListener {
    JPanel contentPane;
    BorderLayout borderLayout1 = new BorderLayout();
    private JTextPane jTextPane = null;
    private JButton jButtonClose = null;
    private JScrollPane jScrollPane = null;

    public HelpFrame() {
        this.enableEvents(64L);
        try {
            this.initialize();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initialize() throws Exception {
        this.contentPane = (JPanel)this.getContentPane();
        this.contentPane.setLayout(this.borderLayout1);
        this.contentPane.add((Component)this.getJButtonClose(), "South");
        this.contentPane.add((Component)this.getJScrollPane(), "Center");
        this.setSize(new Dimension(400, 600));
        this.setContentPane(this.contentPane);
        this.setMinimumSize(new Dimension(600, 400));
        this.setTitle("DCM Help");
    }

    public void show() {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int w = this.getSize().width;
        int h = this.getSize().height;
        int x = (dim.width - w) / 2;
        int y = (dim.height - h) / 2;
        this.setLocation(x, y);
        super.show();
    }

    private JTextPane getJTextPane() {
        if (this.jTextPane == null) {
            this.jTextPane = new JTextPane();
            this.jTextPane.setBackground(Color.lightGray);
            this.jTextPane.setEditable(false);
            URL helpURL = this.getClass().getResource("help.html");
            if (helpURL != null) {
                try {
                    this.jTextPane.setPage(helpURL);
                }
                catch (IOException e) {
                    System.err.println("Attempted to read a bad URL: " + helpURL);
                }
            } else {
                System.err.println("Couldn't find file: help.html");
            }
        }
        return this.jTextPane;
    }

    private JButton getJButtonClose() {
        if (this.jButtonClose == null) {
            this.jButtonClose = new JButton();
            this.jButtonClose.setText("Close");
            this.jButtonClose.setActionCommand("CloseHelp");
            this.jButtonClose.addActionListener(this);
        }
        return this.jButtonClose;
    }

    private JScrollPane getJScrollPane() {
        if (this.jScrollPane == null) {
            this.jScrollPane = new JScrollPane();
            this.jScrollPane.setViewportView(this.getJTextPane());
        }
        return this.jScrollPane;
    }
}

