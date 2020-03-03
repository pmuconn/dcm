/*
 * Decompiled with CFR 0.137.
 */
package de.draco.cbm.tool.crtcreator.gui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.event.ChangeEvent;

public class HelpButtonsPanel
extends EasyPanel {
    private static final long serialVersionUID = 1L;
    private ButtonGroup buttonGroup = new ButtonGroup();
    private JButton jButtonShowBanks = null;
    private JButton jButtonHelp = null;
    private JButton jButtonAbout = null;

    public HelpButtonsPanel() {
        this.initialize();
    }

    private void initialize() {
        GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 4;
        gridBagConstraints3.insets = new Insets(0, 0, 0, 0);
        gridBagConstraints3.gridy = 0;
        GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        gridBagConstraints2.gridx = 3;
        gridBagConstraints2.insets = new Insets(0, 0, 0, 0);
        gridBagConstraints2.gridy = 0;
        GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
        gridBagConstraints11.gridx = 2;
        gridBagConstraints11.insets = new Insets(0, 0, 0, 0);
        gridBagConstraints11.gridy = 0;
        this.setOpaque(false);
        this.setSize(300, 200);
        this.setLayout(new GridBagLayout());
        this.add((Component)this.getJButtonShowBanks(), gridBagConstraints11);
        this.add((Component)this.getJButtonHelp(), gridBagConstraints2);
        this.add((Component)this.getJButtonAbout(), gridBagConstraints3);
    }

    public void update(ChangeEvent e) {
    }

    private JButton getJButtonShowBanks() {
        if (this.jButtonShowBanks == null) {
            this.jButtonShowBanks = new JButton();
            this.jButtonShowBanks.setText("Banks");
            this.jButtonShowBanks.setActionCommand("ShowBanks");
            this.jButtonShowBanks.addActionListener(this);
        }
        return this.jButtonShowBanks;
    }

    private JButton getJButtonHelp() {
        if (this.jButtonHelp == null) {
            this.jButtonHelp = new JButton();
            this.jButtonHelp.setText("Help");
            this.jButtonHelp.addActionListener(this);
        }
        return this.jButtonHelp;
    }

    private JButton getJButtonAbout() {
        if (this.jButtonAbout == null) {
            this.jButtonAbout = new JButton();
            this.jButtonAbout.setText("About");
            this.jButtonAbout.addActionListener(this);
        }
        return this.jButtonAbout;
    }
}

