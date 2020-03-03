/*
 * Decompiled with CFR 0.137.
 */
package de.draco.cbm.tool.crtcreator.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.event.ChangeEvent;

public class StorageButtonsPanel
extends EasyPanel {
    private static final long serialVersionUID = 1L;
    private JButton jButtonLoad = null;
    private JButton jButtonSave = null;
    private JButton jButtonExport = null;
    private JButton jButtonNew = null;

    public StorageButtonsPanel() {
        this.initialize();
    }

    private void initialize() {
        GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
        gridBagConstraints11.gridx = 1;
        gridBagConstraints11.fill = 2;
        gridBagConstraints11.weightx = 0.1;
        gridBagConstraints11.insets = new Insets(0, 0, 0, 0);
        gridBagConstraints11.gridy = 8;
        GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.gridwidth = 2;
        gridBagConstraints2.insets = new Insets(0, 0, 5, 0);
        gridBagConstraints2.fill = 2;
        gridBagConstraints2.weightx = 0.1;
        gridBagConstraints2.gridy = 7;
        GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridwidth = 2;
        gridBagConstraints1.insets = new Insets(0, 0, 5, 0);
        gridBagConstraints1.fill = 2;
        gridBagConstraints1.weightx = 0.1;
        gridBagConstraints1.gridy = 4;
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.insets = new Insets(0, 0, 5, 0);
        gridBagConstraints.fill = 2;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.0;
        gridBagConstraints.gridy = 1;
        this.setSize(200, 500);
        this.setPreferredSize(new Dimension(200, 500));
        this.setMinimumSize(new Dimension(100, 200));
        this.setMaximumSize(new Dimension(200, 500));
        this.setLayout(new GridBagLayout());
        this.setOpaque(false);
        this.add((Component)this.getJButtonLoad(), gridBagConstraints);
        this.add((Component)this.getJButtonSave(), gridBagConstraints1);
        this.add((Component)this.getJButtonExport(), gridBagConstraints2);
        this.add((Component)this.getJButtonNew(), gridBagConstraints11);
    }

    private JButton getJButtonLoad() {
        if (this.jButtonLoad == null) {
            this.jButtonLoad = new JButton();
            this.jButtonLoad.setText("Load");
            this.jButtonLoad.setPreferredSize(new Dimension(80, 50));
            this.jButtonLoad.setActionCommand("Load");
            this.jButtonLoad.addActionListener(this);
        }
        return this.jButtonLoad;
    }

    private JButton getJButtonSave() {
        if (this.jButtonSave == null) {
            this.jButtonSave = new JButton();
            this.jButtonSave.setPreferredSize(new Dimension(80, 50));
            this.jButtonSave.setText("Save");
            this.jButtonSave.setActionCommand("Save");
            this.jButtonSave.addActionListener(this);
        }
        return this.jButtonSave;
    }

    private JButton getJButtonExport() {
        if (this.jButtonExport == null) {
            this.jButtonExport = new JButton();
            this.jButtonExport.setText("Export");
            this.jButtonExport.setPreferredSize(new Dimension(80, 50));
            this.jButtonExport.setActionCommand("Export");
            this.jButtonExport.addActionListener(this);
        }
        return this.jButtonExport;
    }

    private JButton getJButtonNew() {
        if (this.jButtonNew == null) {
            this.jButtonNew = new JButton();
            this.jButtonNew.setPreferredSize(new Dimension(80, 50));
            this.jButtonNew.setText("New");
            this.jButtonNew.setActionCommand("New");
            this.jButtonNew.addActionListener(this);
        }
        return this.jButtonNew;
    }

    public void update(ChangeEvent e) {
        boolean enabled = this.m_model.isChangeable();
        this.jButtonSave.setEnabled(enabled);
    }
}

