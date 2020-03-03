/*
 * Decompiled with CFR 0.137.
 */
package de.draco.cbm.tool.crtcreator.gui;

import java.util.Vector;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;

import de.draco.cbm.tool.crtcreator.Constants;
import de.draco.cbm.tool.crtcreator.EFItem;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class ItemTableModelAdapter
extends AbstractTableModel
implements ChangeListener,
Constants {
    EasyModel m_model;
    Vector<ChangeListener> m_changeListeners = new Vector();
    String[] m_colNames = new String[]{"Name", "Size", "Type", "Hidden", "File"};
    private static final long serialVersionUID = 1L;

    public ItemTableModelAdapter(EasyModel model) {
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

    @Override
    public String getColumnName(int column) {
        return this.m_colNames[column];
    }

    @Override
    public int getColumnCount() {
        return this.m_colNames.length;
    }

    @Override
    public int getRowCount() {
        return this.m_model.getItems().size();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        Class<?> ret = Object.class;
        switch (columnIndex) {
            case 3: {
                ret = Boolean.class;
            }
        }
        return ret;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object ret = "";
        EFItem item = this.m_model.getItems().get(rowIndex);
        switch (columnIndex) {
            case 0: {
                ret = item.getName();
                break;
            }
            case 1: {
                ret = item.getSizeData();
                break;
            }
            case 2: {
                ret = item.getTypeDescription();
                break;
            }
            case 3: {
                ret = item.isHidden();
                break;
            }
            case 4: {
                ret = item.getFileName();
            }
        }
        return ret;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        EFItem item = this.m_model.getItems().get(rowIndex);
        switch (columnIndex) {
            case 0: {
                String v = aValue.toString();
                if (v.length() > 16) {
                    v = v.substring(0, 15);
                }
                item.setName(v);
                break;
            }
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        boolean ret = false;
        switch (columnIndex) {
            case 0: {
                ret = true;
                break;
            }
            case 3: {
                ret = true;
            }
        }
        return ret;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        this.changed();
    }
}

