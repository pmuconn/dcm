/*
 * Decompiled with CFR 0.137.
 */
package de.draco.cbm.tool.crtcreator.gui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.event.ChangeEvent;

public class TopOptionsPanel
extends EasyPanel {
    private static final long serialVersionUID = 1L;
    private JRadioButton jRadioButton1M = null;
    private JRadioButton jRadioButton2M = null;
    private ButtonGroup buttonGroup = new ButtonGroup();
    private JCheckBox jCheckBoxScreenSaver = null;
    private JCheckBox jCheckBoxBoot = null;

    public TopOptionsPanel() {
        this.initialize();
        this.update(new ChangeEvent(""));
    }

    private void initialize() {
        GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        gridBagConstraints2.gridx = 2;
        gridBagConstraints2.gridy = 0;
        GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
        gridBagConstraints11.gridx = 3;
        gridBagConstraints11.gridy = 0;
        GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 0;
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        this.setOpaque(false);
        this.setSize(300, 200);
        this.setLayout(new GridBagLayout());
        this.add((Component)this.getJRadioButton1M(), gridBagConstraints1);
        this.add((Component)this.getJRadioButton2M(), gridBagConstraints);
        this.add((Component)this.getJCheckBoxBoot(), gridBagConstraints2);
        this.add((Component)this.getJCheckBoxScreenSaver(), gridBagConstraints11);
    }

    public void update(ChangeEvent e) {
        if (this.m_model.getSizeOption() == 1) {
            this.jRadioButton1M.setSelected(true);
        } else if (this.m_model.getSizeOption() == 2) {
            this.jRadioButton2M.setSelected(true);
        }
        this.jCheckBoxBoot.setSelected(this.m_model.getUseBootScreen());
        this.jCheckBoxScreenSaver.setSelected(this.m_model.getUseScreenSaver());
    }

    private JRadioButton getJRadioButton1M() {
        if (this.jRadioButton1M == null) {
            this.jRadioButton1M = new JRadioButton();
            this.jRadioButton1M.setActionCommand("1MB");
            this.jRadioButton1M.addActionListener(this);
            this.jRadioButton1M.setText("1MB");
            this.jRadioButton1M.setToolTipText("Create a cart with 1MB (64 banks)");
            this.jRadioButton1M.setOpaque(false);
            this.buttonGroup.add(this.jRadioButton1M);
        }
        return this.jRadioButton1M;
    }

    private JRadioButton getJRadioButton2M() {
        if (this.jRadioButton2M == null) {
            this.jRadioButton2M = new JRadioButton();
            this.jRadioButton2M.setActionCommand("2MB");
            this.jRadioButton2M.addActionListener(this);
            this.jRadioButton2M.setText("2MB");
            this.jRadioButton2M.setToolTipText("Create a cart with 2MB (127 banks)");
            this.jRadioButton2M.setOpaque(false);
            this.buttonGroup.add(this.jRadioButton2M);
        }
        return this.jRadioButton2M;
    }

    private JCheckBox getJCheckBoxScreenSaver() {
        if (this.jCheckBoxScreenSaver == null) {
            this.jCheckBoxScreenSaver = new JCheckBox();
            this.jCheckBoxScreenSaver.setText("Screens.");
            this.jCheckBoxScreenSaver.setToolTipText("Use easyflash screen saver");
            this.jCheckBoxScreenSaver.setActionCommand("ScreenSaver");
            this.jCheckBoxScreenSaver.addActionListener(this);
            this.jCheckBoxScreenSaver.setOpaque(false);
        }
        return this.jCheckBoxScreenSaver;
    }

    private JCheckBox getJCheckBoxBoot() {
        if (this.jCheckBoxBoot == null) {
            this.jCheckBoxBoot = new JCheckBox();
            this.jCheckBoxBoot.setText("Boot");
            this.jCheckBoxBoot.setActionCommand("BootScreen");
            this.jCheckBoxBoot.setToolTipText("Use easyflash bootscreen");
            this.jCheckBoxBoot.addActionListener(this);
            this.jCheckBoxBoot.setOpaque(false);
        }
        return this.jCheckBoxBoot;
    }
}

