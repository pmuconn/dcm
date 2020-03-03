/*
 * Decompiled with CFR 0.137.
 */
package de.draco.cbm.tool.crtcreator.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JLabel;

public class HyperLabel
extends JLabel {
    private Color underlineColor = null;

    public HyperLabel() {
        this.setForeground(Color.BLUE.darker());
        this.setCursor(new Cursor(12));
        this.addMouseListener(new HyperlinkLabelMouseAdapter());
    }

    public HyperLabel(String label) {
        super(label);
        this.setForeground(Color.BLUE.darker());
        this.setCursor(new Cursor(12));
        this.addMouseListener(new HyperlinkLabelMouseAdapter());
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(this.underlineColor == null ? this.getForeground() : this.underlineColor);
        Insets insets = this.getInsets();
        int left = insets.left;
        if (this.getIcon() != null) {
            left += this.getIcon().getIconWidth() + this.getIconTextGap();
        }
        g.drawLine(left, this.getHeight() - 1 - insets.bottom, (int)this.getPreferredSize().getWidth() - insets.right, this.getHeight() - 1 - insets.bottom);
    }

    private static void open(URI uri) {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(uri);
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
    }

    public Color getUnderlineColor() {
        return this.underlineColor;
    }

    public void setUnderlineColor(Color underlineColor) {
        this.underlineColor = underlineColor;
    }

    public class HyperlinkLabelMouseAdapter
    extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            try {
                HyperLabel.open(new URI(HyperLabel.this.getText()));
            }
            catch (URISyntaxException e1) {
                e1.printStackTrace();
            }
        }
    }

}

