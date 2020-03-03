/*
 * Decompiled with CFR 0.137.
 */
package de.draco.cbm.tool.crtcreator.gui;

import java.util.Vector;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;

import de.draco.cbm.tool.crtcreator.Constants;

public class ChipTableModelAdapter
extends AbstractTableModel
implements ChangeListener,
Constants {
    EasyModel m_model;
    Vector<ChangeListener> m_changeListeners = new Vector();
    String[] m_colNames = new String[]{"Bank", "Low", "High"};
    private static final long serialVersionUID = 1L;

    public ChipTableModelAdapter(EasyModel model) {
        this.m_model = model;
        model.addChangeListener(this);
        this.changed();
    }

    private void changed() {
        for (ChangeListener l : this.m_changeListeners) {
            l.stateChanged(new ChangeEvent("Update"));
        }
        this.fireTableDataChanged();
    }

    public void addChangeListener(ChangeListener l) {
        if (!this.m_changeListeners.contains(l)) {
            this.m_changeListeners.add(l);
        }
    }

    public void removeChangeListener(ChangeListener l) {
        this.m_changeListeners.remove(l);
    }

    public String getColumnName(int column) {
        return this.m_colNames[column];
    }

    public int getColumnCount() {
        return this.m_colNames.length;
    }

    public int getRowCount() {
        return this.m_model.getUsedBanks();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        String ret = "";
        switch (columnIndex) {
            case 0: {
                ret = "" + rowIndex;
                break;
            }
            default: {
                int idx = rowIndex * 2 + columnIndex - 1;
                if (idx >= this.m_model.getChips().size()) break;
                ret = this.m_model.getChips().get(idx).getDescription();
            }
        }
        return ret;
    }

    public void stateChanged(ChangeEvent e) {
        this.changed();
    }
}

