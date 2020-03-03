/*
 * Decompiled with CFR 0.137.
 */
package de.draco.cbm.tool.crtcreator.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;

import de.draco.cbm.tool.crtcreator.Constants;

public class ItemButtonsPanel
extends EasyPanel
implements Constants {
    private static final long serialVersionUID = 1L;
    private JButton jButtonUp = null;
    private JButton jButtonAdd = null;
    private JButton jButtonDelete = null;
    private JButton jButtonDown = null;
    private JToggleButton jToggleButtonSort = null;

    public ItemButtonsPanel() {
        this.initialize();
    }

    private void initialize() {
        GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
        gridBagConstraints11.gridx = 5;
        gridBagConstraints11.insets = new Insets(2, 2, 2, 2);
        gridBagConstraints11.fill = 1;
        gridBagConstraints11.gridy = 2;
        GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 5;
        gridBagConstraints3.gridy = 4;
        GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        gridBagConstraints2.gridx = 7;
        gridBagConstraints2.gridwidth = 2;
        gridBagConstraints2.gridy = 2;
        GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridwidth = 2;
        gridBagConstraints1.gridy = 2;
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        this.setSize(300, 200);
        this.setMinimumSize(new Dimension(200, 150));
        this.setLayout(new GridBagLayout());
        this.setOpaque(false);
        this.add((Component)this.getJButtonAdd(), gridBagConstraints1);
        this.add((Component)this.getJButtonUp(), gridBagConstraints);
        this.add((Component)this.getJButtonDown(), gridBagConstraints3);
        this.add((Component)this.getJButtonDelete(), gridBagConstraints2);
        this.add((Component)this.getJButtonSort(), gridBagConstraints11);
    }

    private JButton getJButtonUp() {
        if (this.jButtonUp == null) {
            this.jButtonUp = new JButton();
            this.jButtonUp.setText("up");
            this.jButtonUp.setActionCommand("Up");
            this.jButtonUp.setPreferredSize(new Dimension(80, 50));
            this.jButtonUp.addActionListener(this);
        }
        return this.jButtonUp;
    }

    private JButton getJButtonAdd() {
        if (this.jButtonAdd == null) {
            this.jButtonAdd = new JButton();
            this.jButtonAdd.setPreferredSize(new Dimension(80, 50));
            this.jButtonAdd.setText("+");
            this.jButtonAdd.setActionCommand("Add");
            this.jButtonAdd.addActionListener(this);
        }
        return this.jButtonAdd;
    }

    private JButton getJButtonDelete() {
        if (this.jButtonDelete == null) {
            this.jButtonDelete = new JButton();
            this.jButtonDelete.setText("-");
            this.jButtonDelete.setPreferredSize(new Dimension(80, 50));
            this.jButtonDelete.setActionCommand("Remove");
            this.jButtonDelete.addActionListener(this);
        }
        return this.jButtonDelete;
    }

    private JButton getJButtonDown() {
        if (this.jButtonDown == null) {
            this.jButtonDown = new JButton();
            this.jButtonDown.setText("down");
            this.jButtonDown.setPreferredSize(new Dimension(80, 50));
            this.jButtonDown.setActionCommand("Down");
            this.jButtonDown.addActionListener(this);
        }
        return this.jButtonDown;
    }

    public void update(ChangeEvent e) {
        boolean enabled = this.m_model.isChangeable();
        this.jButtonAdd.setEnabled(enabled);
        this.jButtonDelete.setEnabled(enabled);
        int sortStatus = this.m_model.getSortStatus();
        this.jButtonUp.setEnabled(enabled && sortStatus == 0);
        this.jButtonDown.setEnabled(enabled && sortStatus == 0);
        this.jToggleButtonSort.setSelected(sortStatus != 0);
    }

    private JToggleButton getJButtonSort() {
        if (this.jToggleButtonSort == null) {
            this.jToggleButtonSort = new JToggleButton();
            this.jToggleButtonSort.setIcon(new ImageIcon(ItemButtonsPanel.class.getResource("sort.png")));
            this.jToggleButtonSort.setToolTipText("Sorted or unsorted list (search will be disabled if not sorted)");
            this.jToggleButtonSort.setActionCommand("Sort");
            this.jToggleButtonSort.addActionListener(this);
        }
        return this.jToggleButtonSort;
    }
}

