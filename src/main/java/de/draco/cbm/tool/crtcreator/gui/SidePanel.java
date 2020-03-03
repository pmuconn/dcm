/*
 * Decompiled with CFR 0.137.
 */
package de.draco.cbm.tool.crtcreator.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

import de.draco.cbm.tool.crtcreator.Constants;

public class SidePanel
extends EasyPanel
implements Constants {
    private static final long serialVersionUID = 1L;
    private ItemButtonsPanel itemButtonsPanel = null;
    private LogoPanel logoPanel = null;
    private StorageButtonsPanel storageButtonsPanel = null;
    private JPanel jPanelFillerTop = null;
    private JPanel jPanelBottom = null;
    private TopOptionsPanel optionsPanel = null;
    private HelpButtonsPanel optionsPanel2 = null;
    private JLabel jLabelDCM = null;

    public SidePanel() {
        this.initialize();
    }

    private void initialize() {
        GridBagConstraints gridBagConstraintsLabel = new GridBagConstraints();
        gridBagConstraintsLabel.gridx = 0;
        gridBagConstraintsLabel.anchor = 13;
        gridBagConstraintsLabel.insets = new Insets(55, 0, 0, 20);
        gridBagConstraintsLabel.weightx = 0.01;
        gridBagConstraintsLabel.weighty = 0.01;
        gridBagConstraintsLabel.gridy = 0;
        this.jLabelDCM = new JLabel();
        this.jLabelDCM.setText("DCM 1.0.1");
        this.jLabelDCM.setForeground(Color.white);
        this.jLabelDCM.setFont(new Font("Dialog", 0, 10));
        GridBagConstraints gridBagConstraintsOptions2 = new GridBagConstraints();
        gridBagConstraintsOptions2.gridx = 0;
        gridBagConstraintsOptions2.weighty = 0.1;
        gridBagConstraintsOptions2.fill = 1;
        gridBagConstraintsOptions2.anchor = 18;
        gridBagConstraintsOptions2.insets = new Insets(2, 2, 2, 2);
        gridBagConstraintsOptions2.gridy = 5;
        GridBagConstraints gridBagConstraintsOptions1 = new GridBagConstraints();
        gridBagConstraintsOptions1.gridx = 0;
        gridBagConstraintsOptions1.gridy = 2;
        GridBagConstraints gridBagConstraintsStorageButtons = new GridBagConstraints();
        gridBagConstraintsStorageButtons.anchor = 16;
        gridBagConstraintsStorageButtons.insets = new Insets(2, 2, 2, 2);
        gridBagConstraintsStorageButtons.gridx = 0;
        gridBagConstraintsStorageButtons.gridy = 6;
        gridBagConstraintsStorageButtons.weightx = 0.1;
        gridBagConstraintsStorageButtons.weighty = 2.0;
        gridBagConstraintsStorageButtons.fill = 1;
        GridBagConstraints gridBagConstraintsLogo = new GridBagConstraints();
        gridBagConstraintsLogo.gridx = 0;
        gridBagConstraintsLogo.fill = 2;
        gridBagConstraintsLogo.weightx = 0.1;
        gridBagConstraintsLogo.weighty = 0.1;
        gridBagConstraintsLogo.insets = new Insets(0, 10, 0, 0);
        gridBagConstraintsLogo.anchor = 11;
        gridBagConstraintsLogo.gridy = 0;
        GridBagConstraints gridBagConstraintsItemButtons = new GridBagConstraints();
        gridBagConstraintsItemButtons.gridx = 0;
        gridBagConstraintsItemButtons.weightx = 1.0;
        gridBagConstraintsItemButtons.weighty = 3.0;
        gridBagConstraintsItemButtons.fill = 1;
        gridBagConstraintsItemButtons.anchor = 18;
        gridBagConstraintsItemButtons.gridheight = 1;
        gridBagConstraintsItemButtons.insets = new Insets(0, 2, 2, 2);
        gridBagConstraintsItemButtons.gridy = 3;
        this.setSize(250, 500);
        this.setMinimumSize(new Dimension(250, 500));
        this.setLayout(new GridBagLayout());
        this.setOpaque(false);
        this.add((Component)this.jLabelDCM, gridBagConstraintsLabel);
        this.add((Component)this.getLogoPanel(), gridBagConstraintsLogo);
        this.add((Component)this.getItemButtonsPanel(), gridBagConstraintsItemButtons);
        this.add((Component)this.getStorageButtonsPanel(), gridBagConstraintsStorageButtons);
        this.add((Component)this.getOptionsPanel(), gridBagConstraintsOptions1);
        this.add((Component)this.getOptionsPanel2(), gridBagConstraintsOptions2);
    }

    private ItemButtonsPanel getItemButtonsPanel() {
        if (this.itemButtonsPanel == null) {
            this.itemButtonsPanel = new ItemButtonsPanel();
        }
        return this.itemButtonsPanel;
    }

    private StorageButtonsPanel getStorageButtonsPanel() {
        if (this.storageButtonsPanel == null) {
            this.storageButtonsPanel = new StorageButtonsPanel();
        }
        return this.storageButtonsPanel;
    }

    private LogoPanel getLogoPanel() {
        if (this.logoPanel == null) {
            this.logoPanel = new LogoPanel();
            this.logoPanel.setPreferredSize(new Dimension(180, 60));
        }
        return this.logoPanel;
    }

    private JPanel getJPanelFillerTop() {
        if (this.jPanelFillerTop == null) {
            this.jPanelFillerTop = new JPanel();
            this.jPanelFillerTop.setLayout(new GridBagLayout());
        }
        return this.jPanelFillerTop;
    }

    private JPanel getJPanelBottom() {
        if (this.jPanelBottom == null) {
            this.jPanelBottom = new JPanel();
            this.jPanelBottom.setLayout(new GridBagLayout());
        }
        return this.jPanelBottom;
    }

    private TopOptionsPanel getOptionsPanel() {
        if (this.optionsPanel == null) {
            this.optionsPanel = new TopOptionsPanel();
        }
        return this.optionsPanel;
    }

    private HelpButtonsPanel getOptionsPanel2() {
        if (this.optionsPanel2 == null) {
            this.optionsPanel2 = new HelpButtonsPanel();
        }
        return this.optionsPanel2;
    }
}

