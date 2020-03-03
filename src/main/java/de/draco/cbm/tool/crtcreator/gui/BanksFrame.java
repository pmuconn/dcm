/*
 * Decompiled with CFR 0.137.
 */
package de.draco.cbm.tool.crtcreator.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JPanel;

public class BanksFrame
extends EasyFrame {
    ChipTablePanel chipTablePanel;
    JPanel contentPane;
    BorderLayout borderLayout1 = new BorderLayout();

    public BanksFrame() {
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
        this.setSize(new Dimension(400, 600));
        this.setMinimumSize(new Dimension(600, 400));
        this.setContentPane(this.getChipTablePanel());
        this.setTitle("DCM Bank View");
    }

    private ChipTablePanel getChipTablePanel() {
        if (this.chipTablePanel == null) {
            this.chipTablePanel = new ChipTablePanel();
        }
        return this.chipTablePanel;
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
}

