/*
 * Decompiled with CFR 0.137.
 */
package de.draco.cbm.tool.crtcreator.gui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;

import de.draco.cbm.tool.crtcreator.EFItem;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class ItemTablePanel
extends EasyPanel {
    private static final long serialVersionUID = 1L;
    private JScrollPane jScrollPaneTable = null;
    private JTable table = null;

    public ItemTablePanel() {
        this.initialize();
        new de.draco.cbm.tool.crtcreator.gui.FileDrop(System.out, this, (FileDropListener)this.m_controller);
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
            this.table = new JTable();
            this.table.setModel(new TableSorter(this.m_model, new ItemTableModelAdapter(this.m_model), this.table));
            this.table.getColumnModel().getColumn(0).setPreferredWidth(this.getWidth() * 30 / 100);
            this.table.getColumnModel().getColumn(1).setPreferredWidth(this.getWidth() * 10 / 100);
            this.table.getColumnModel().getColumn(2).setPreferredWidth(this.getWidth() * 15 / 100);
            this.table.getColumnModel().getColumn(3).setPreferredWidth(this.getWidth() * 10 / 100);
            this.table.getColumnModel().getColumn(4).setPreferredWidth(this.getWidth() * 35 / 100);
            this.table.addKeyListener(new ViewKeyListener());
        }
        return this.table;
    }

    @Override
    public void update(ChangeEvent e) {
    }

    public void setSelectedItems(Vector<EFItem> items) {
        Vector<EFItem> allItems = this.m_model.getItems();
        if (items != null && allItems != null) {
            this.getTable().clearSelection();
            for (EFItem current : items) {
                int row = allItems.indexOf(current);
                if (row <= -1) continue;
                if (this.table.getModel() instanceof TableSorter) {
                    this.addSelected(((TableSorter)this.table.getModel()).viewIndex(row), 0);
                    continue;
                }
                this.addSelected(row, 0);
            }
        }
    }

    public void addSelected(int row, int col) {
        final Rectangle rec = this.getTable().getCellRect(row, col, true);
        this.getTable().addRowSelectionInterval(row, row);
        SwingUtilities.invokeLater(new Runnable(){

            public void run() {
                try {
                    ItemTablePanel.this.scrollRectToVisible(rec);
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
        });
    }

    public Vector<EFItem> getSelectedItems() {
        int[] rows = new int[]{};
        int[] viewrows = this.table.getSelectedRows();
        if (this.table.getModel() instanceof TableSorter) {
            rows = new int[viewrows.length];
            for (int i = 0; i < viewrows.length; ++i) {
                rows[i] = ((TableSorter)this.table.getModel()).modelIndex(viewrows[i]);
            }
        } else {
            rows = viewrows;
        }
        return this.m_model.getItems(rows);
    }

    public class ViewKeyListener
    extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == 127) {
                ItemTablePanel.this.actionPerformed(new ActionEvent(e.getSource(), e.getID(), "RemoveSelected"));
                e.consume();
            }
        }
    }

}

