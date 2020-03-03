/*
 * Decompiled with CFR 0.137.
 */
package de.draco.cbm.tool.crtcreator.gui;

import java.awt.GridBagLayout;

import javax.swing.JPanel;

public class BottomPanel
extends JPanel {
    private static final long serialVersionUID = 1L;

    public BottomPanel() {
        this.initialize();
    }

    private void initialize() {
        this.setSize(300, 200);
        this.setLayout(new GridBagLayout());
        this.setOpaque(false);
    }
}

