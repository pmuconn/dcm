/*
 * Decompiled with CFR 0.137.
 */
package de.draco.cbm.tool.crtcreator.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class MainPanel
extends GradientPanel {
    private static final long serialVersionUID = 1L;
    private ItemTablePanel tablePanel = null;
    private SidePanel jPanelSide = null;
    private GaugePanel gaugePanel = null;

    public MainPanel() {
        try {
            this.initialize();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initialize() throws Exception {
        GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
        gridBagConstraints11.gridx = 0;
        gridBagConstraints11.gridwidth = 2;
        gridBagConstraints11.fill = 2;
        gridBagConstraints11.weightx = 0.1;
        gridBagConstraints11.weighty = 0.001;
        gridBagConstraints11.gridy = 1;
        GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.anchor = 16;
        gridBagConstraints1.fill = 1;
        gridBagConstraints1.weightx = 0.01;
        gridBagConstraints1.weighty = 0.1;
        gridBagConstraints1.insets = new Insets(0, 0, 0, 0);
        gridBagConstraints1.gridy = 0;
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = 18;
        gridBagConstraints.fill = 1;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.insets = new Insets(5, 5, 0, 0);
        gridBagConstraints.gridy = 0;
        this.setOpaque(false);
        this.setLayout(new GridBagLayout());
        this.setMinimumSize(new Dimension(600, 400));
        this.add((Component)this.getTablePanel(), gridBagConstraints);
        this.add((Component)this.getJPanelSide(), gridBagConstraints1);
        this.add((Component)this.getGaugePanel(), gridBagConstraints11);
    }

    private ItemTablePanel getTablePanel() {
        if (this.tablePanel == null) {
            this.tablePanel = new ItemTablePanel();
        }
        return this.tablePanel;
    }

    private SidePanel getJPanelSide() {
        if (this.jPanelSide == null) {
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.gridx = 0;
            gridBagConstraints3.anchor = 18;
            gridBagConstraints3.fill = 1;
            gridBagConstraints3.weightx = 0.2;
            gridBagConstraints3.weighty = 0.2;
            gridBagConstraints3.gridy = 1;
            this.jPanelSide = new SidePanel();
            this.jPanelSide.setOpaque(false);
        }
        return this.jPanelSide;
    }

    private GaugePanel getGaugePanel() {
        if (this.gaugePanel == null) {
            this.gaugePanel = new GaugePanel();
        }
        return this.gaugePanel;
    }
}

