/*
 * Decompiled with CFR 0.137.
 */
package de.draco.cbm.tool.crtcreator.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class LogoPanel
extends EasyPanel {
    private static final long serialVersionUID = 1L;
    private ImageIcon imageIcon = null;

    private ImageIcon getImageIcon() {
        if (this.imageIcon == null) {
            this.imageIcon = new ImageIcon();
            InputStream is = this.getClass().getResourceAsStream("EasyFlashLogoHiresTransparent.gif");
            try {
                byte[] data = new byte[is.available()];
                is.read(data);
                this.imageIcon = new ImageIcon(data);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return this.imageIcon;
    }

    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setSize(140, 60);
        f.getContentPane().add(new LogoPanel());
        f.show();
    }

    public LogoPanel() {
        this.getImageIcon();
        this.initialize();
    }

    public void paint(Graphics g) {
        this.imageIcon.paintIcon(this, g, 0, 0);
    }

    private void initialize() {
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(180, 65));
        this.setMinimumSize(new Dimension(180, 65));
        this.setMaximumSize(new Dimension(180, 65));
        this.setBackground(Color.black);
        this.addMouseListener(new MouseAdapter(){

            public void mouseClicked(MouseEvent e) {
                LogoPanel.this.actionPerformed(new ActionEvent(this, 0, "About"));
            }
        });
    }

}

