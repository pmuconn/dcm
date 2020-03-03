/*
 * Decompiled with CFR 0.137.
 */
package de.draco.cbm.tool.crtcreator.gui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;

public class ChipTablePanel
extends EasyPanel {
    private static final long serialVersionUID = 1L;
    private JScrollPane jScrollPaneTable = null;
    private JTable table = null;

    public ChipTablePanel() {
        this.initialize();
    }

    private void initialize() {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.gridx = 0;
        this.setSize(416, 344);
        this.setLayout(new GridBagLayout());
        this.add((Component)this.getJScrollPaneTable(), gridBagConstraints);
    }

    private JScrollPane getJScrollPaneTable() {
        if (this.jScrollPaneTable == null) {
            this.jScrollPaneTable = new JScrollPane();
            this.jScrollPaneTable.setViewportView(this.getTable());
            this.jScrollPaneTable.setOpaque(false);
        }
        return this.jScrollPaneTable;
    }

    public JTable getTable() {
        if (this.table == null) {
            this.table = new JTable(new ChipTableModelAdapter(this.m_model));
            this.table.getColumnModel().getColumn(0).setPreferredWidth(this.getWidth() * 2 / 100);
            this.table.getColumnModel().getColumn(1).setPreferredWidth(this.getWidth() * 49 / 100);
            this.table.getColumnModel().getColumn(2).setPreferredWidth(this.getWidth() * 49 / 100);
        }
        return this.table;
    }

    public void update(ChangeEvent e) {
    }
}

