/*
 * Decompiled with CFR 0.137.
 */
package de.draco.cbm.tool.crtcreator.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JProgressBar;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;

public class GaugePanel
extends EasyPanel {
    private static final long serialVersionUID = 1L;
    private JProgressBar jProgressBarGauge = null;

    public GaugePanel() {
        this.initialize();
    }

    private void initialize() {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(2, 5, 2, 5);
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = 2;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.gridx = 0;
        this.setSize(294, 30);
        this.setLayout(new GridBagLayout());
        this.setOpaque(false);
        this.add((Component)this.getJProgressBarGauge(), gridBagConstraints);
    }

    private JProgressBar getJProgressBarGauge() {
        if (this.jProgressBarGauge == null) {
            UIManager.put("ProgressBar.selectionForeground", Color.darkGray);
            UIManager.put("ProgressBar.selectionBackground", Color.darkGray);
            this.jProgressBarGauge = new JProgressBar();
            this.jProgressBarGauge.setBorderPainted(true);
            this.jProgressBarGauge.setStringPainted(true);
        }
        return this.jProgressBarGauge;
    }

    public void update(ChangeEvent e) {
        int val = this.m_model.getPercentUsed();
        String text = val + "% (" + this.m_model.getUsedBanks() + "/" + this.m_model.getMax() + ")";
        this.jProgressBarGauge.setValue(val);
        if (val > 100) {
            this.jProgressBarGauge.setForeground(Color.red);
            text = String.valueOf(text) + " WARNING - TOO MUCH DATA!";
        } else if (val >= 80) {
            this.jProgressBarGauge.setForeground(Color.orange);
        } else {
            this.jProgressBarGauge.setForeground(Color.green);
        }
        this.jProgressBarGauge.setString(text);
    }
}

