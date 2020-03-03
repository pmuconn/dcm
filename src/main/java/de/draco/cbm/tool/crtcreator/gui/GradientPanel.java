/*
 * Decompiled with CFR 0.137.
 */
package de.draco.cbm.tool.crtcreator.gui;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Point;

public class GradientPanel
extends EasyPanel {
    private static final long serialVersionUID = 1L;

    public GradientPanel() {
        this.initialize();
    }

    private void initialize() {
        this.setSize(300, 200);
        this.setLayout(new GridBagLayout());
    }

    public void paint(Graphics g) {
        this.paintGradient(g);
        super.paint(g);
    }

    public void paintGradient(Graphics i_g) {
        int w = this.getSize().width;
        int h = this.getSize().height;
        Point bottomRight = new Point(w, h);
        Point upperLeft = new Point(0, 0);
        GradientPaint gradientPaint = new GradientPaint(upperLeft, Color.darkGray, bottomRight, Color.lightGray);
        Graphics2D g2d = (Graphics2D)i_g;
        g2d.setPaint(gradientPaint);
        g2d.fillRect(0, 0, w - 1, h - 1);
    }
}

