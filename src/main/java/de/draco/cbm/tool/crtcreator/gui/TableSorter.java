/*
 * Decompiled with CFR 0.137.
 */
package de.draco.cbm.tool.crtcreator.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

public class TableSorter
extends AbstractTableModel {
    protected TableModel tableModel;
    public static final int DESCENDING = -1;
    public static final int NOT_SORTED = 0;
    public static final int ASCENDING = 1;
    private boolean[] numericCols = null;
    private static Directive EMPTY_DIRECTIVE = new Directive(-1, 0);
    public static final Comparator COMPARABLE_COMAPRATOR = new Comparator(){

        public int compare(Object o1, Object o2) {
            return ((Comparable)o1).compareTo(o2);
        }
    };
    public static final Comparator DATE_COMPARATOR = new Comparator(){

        public int compare(Object o1, Object o2) {
            int x = 0;
            try {
                x = new SimpleDateFormat("dd-MM-yyyy").parse(o1.toString()).compareTo(new SimpleDateFormat("dd-MM-yyyy").parse(o2.toString()));
            }
            catch (ParseException ex) {
                ex.printStackTrace();
            }
            return x;
        }
    };
    public static final Comparator LEXICAL_COMPARATOR = new Comparator(){

        public int compare(Object o1, Object o2) {
            return o1.toString().toLowerCase().compareTo(o2.toString().toLowerCase());
        }
    };
    public static final Comparator STRINGLONG_COMPARATOR = new Comparator(){

        public int compare(Object o1, Object o2) {
            long c = Long.parseLong(o1.toString()) - Long.parseLong(o2.toString());
            return c < 0L ? -1 : (c > 0L ? 1 : 0);
        }
    };
    private Row[] viewToModel;
    private int[] modelToView;
    private JTableHeader tableHeader;
    private MouseListener mouseListener;
    private TableModelListener tableModelListener;
    private Map columnComparators = new HashMap();
    private List<Directive> sortingColumns = new ArrayList<Directive>();
    private EasyModel m_model;

    public TableSorter(EasyModel model, TableModel tableModel, JTable table) {
        this.m_model = model;
        this.mouseListener = new MouseHandler();
        this.tableModelListener = new TableModelHandler();
        this.setTableHeader(table.getTableHeader());
        this.setTableModel(tableModel);
    }

    private void clearSortingState() {
        this.viewToModel = null;
        this.modelToView = null;
    }

    public TableModel getTableModel() {
        return this.tableModel;
    }

    public void setTableModel(TableModel tableModel) {
        if (this.tableModel != null) {
            this.tableModel.removeTableModelListener(this.tableModelListener);
        }
        this.tableModel = tableModel;
        if (this.tableModel != null) {
            this.tableModel.addTableModelListener(this.tableModelListener);
        }
        this.numericCols = new boolean[tableModel.getColumnCount()];
        for (int i = 0; i < tableModel.getColumnCount(); ++i) {
            try {
                Long.parseLong(tableModel.getValueAt(0, i).toString());
                this.numericCols[i] = true;
                continue;
            }
            catch (Exception e) {
                this.numericCols[i] = false;
            }
        }
        this.clearSortingState();
        this.fireTableStructureChanged();
    }

    public boolean isNumeric(int spalte) {
        return this.numericCols[spalte];
    }

    public JTableHeader getTableHeader() {
        return this.tableHeader;
    }

    public void setTableHeader(JTableHeader tableHeader) {
        if (this.tableHeader != null) {
            this.tableHeader.removeMouseListener(this.mouseListener);
            TableCellRenderer defaultRenderer = this.tableHeader.getDefaultRenderer();
            if (defaultRenderer instanceof SortableHeaderRenderer) {
                this.tableHeader.setDefaultRenderer(((SortableHeaderRenderer)defaultRenderer).tableCellRenderer);
            }
        }
        this.tableHeader = tableHeader;
        if (this.tableHeader != null) {
            this.tableHeader.addMouseListener(this.mouseListener);
            this.tableHeader.setDefaultRenderer(new SortableHeaderRenderer(this.tableHeader.getDefaultRenderer()));
        }
    }

    public boolean isSorting() {
        return this.sortingColumns.size() != 0;
    }

    private Directive getDirective(int column) {
        for (int i = 0; i < this.sortingColumns.size(); ++i) {
            Directive directive = (Directive)this.sortingColumns.get(i);
            if (directive.column != column) continue;
            return directive;
        }
        return EMPTY_DIRECTIVE;
    }

    public int getSortingStatus(int column) {
        return this.getDirective(column).direction;
    }

    private void sortingStatusChanged() {
        this.clearSortingState();
        this.fireTableDataChanged();
        if (this.tableHeader != null) {
            this.tableHeader.repaint();
        }
    }

    public void setSortingStatus(int column, int status) {
        Directive directive = this.getDirective(column);
        if (directive != EMPTY_DIRECTIVE) {
            this.sortingColumns.remove(directive);
        }
        if (status != 0) {
            this.sortingColumns.add(new Directive(column, status));
        }
    }

    protected Icon getHeaderRendererIcon(int column, int size) {
        Directive directive = this.getDirective(column);
        if (directive == EMPTY_DIRECTIVE) {
            return null;
        }
        return new Arrow(directive.direction == -1, size, this.sortingColumns.indexOf(directive));
    }

    private void cancelSorting() {
        this.sortingColumns.clear();
        this.sortingStatusChanged();
    }

    public void setColumnComparator(Class type, Comparator comparator) {
        if (comparator == null) {
            this.columnComparators.remove(type);
        } else {
            this.columnComparators.put(type, comparator);
        }
    }

    protected Comparator getComparator(int column) {
        if (this.isNumeric(column)) {
            return STRINGLONG_COMPARATOR;
        }
        Class<?> columnType = this.tableModel.getColumnClass(column);
        Comparator comparator = (Comparator)this.columnComparators.get(columnType);
        if (comparator != null) {
            return comparator;
        }
        if (columnType.getName().compareTo("DATE") == 0) {
            return COMPARABLE_COMAPRATOR;
        }
        if (Comparable.class.isAssignableFrom(columnType)) {
            return COMPARABLE_COMAPRATOR;
        }
        return LEXICAL_COMPARATOR;
    }

    private Row[] getViewToModel() {
        if (this.viewToModel == null) {
            int tableModelRowCount = this.tableModel.getRowCount();
            this.viewToModel = new Row[tableModelRowCount];
            for (int row = 0; row < tableModelRowCount; ++row) {
                this.viewToModel[row] = new Row(row);
            }
            if (this.isSorting()) {
                Arrays.sort(this.viewToModel);
            }
        }
        return this.viewToModel;
    }

    public int modelIndex(int viewIndex) {
        return this.getViewToModel()[viewIndex].modelIndex;
    }

    public int viewIndex(int modelIndex) {
        return this.getModelToView()[modelIndex];
    }

    private int[] getModelToView() {
        if (this.modelToView == null) {
            int n = this.getViewToModel().length;
            this.modelToView = new int[n];
            int i = 0;
            while (i < n) {
                this.modelToView[this.modelIndex((int)i)] = i++;
            }
        }
        return this.modelToView;
    }

    public int getRowCount() {
        return this.tableModel == null ? 0 : this.tableModel.getRowCount();
    }

    public int getColumnCount() {
        return this.tableModel == null ? 0 : this.tableModel.getColumnCount();
    }

    public String getColumnName(int column) {
        return this.tableModel.getColumnName(column);
    }

    public Class getColumnClass(int column) {
        return this.tableModel.getColumnClass(column);
    }

    public boolean isCellEditable(int row, int column) {
        return this.tableModel.isCellEditable(this.modelIndex(row), column);
    }

    public Object getValueAt(int row, int column) {
        return this.tableModel.getValueAt(this.modelIndex(row), column);
    }

    public void setValueAt(Object aValue, int row, int column) {
        this.tableModel.setValueAt(aValue, this.modelIndex(row), column);
    }

    private static class Arrow
    implements Icon {
        private boolean descending;
        private int size;
        private int priority;

        public Arrow(boolean descending, int size, int priority) {
            this.descending = descending;
            this.size = size;
            this.priority = priority;
        }

        public void paintIcon(Component c, Graphics g, int x, int y) {
            Color color = c == null ? Color.GRAY : c.getBackground();
            int dx = (int)((double)(this.size / 2) * Math.pow(0.8, this.priority));
            int dy = this.descending ? dx : - dx;
            y = y + 5 * this.size / 6 + (this.descending ? - dy : 0);
            int shift = this.descending ? 1 : -1;
            g.translate(x, y);
            g.setColor(color.darker());
            g.drawLine(dx / 2, dy, 0, 0);
            g.drawLine(dx / 2, dy + shift, 0, shift);
            g.setColor(color.brighter());
            g.drawLine(dx / 2, dy, dx, 0);
            g.drawLine(dx / 2, dy + shift, dx, shift);
            if (this.descending) {
                g.setColor(color.darker().darker());
            } else {
                g.setColor(color.brighter().brighter());
            }
            g.drawLine(dx, 0, 0, 0);
            g.setColor(color);
            g.translate(- x, - y);
        }

        public int getIconWidth() {
            return this.size;
        }

        public int getIconHeight() {
            return this.size;
        }
    }

    private static class Directive {
        private int column;
        private int direction;

        public Directive(int column, int direction) {
            this.column = column;
            this.direction = direction;
        }
    }

    private class MouseHandler
    extends MouseAdapter {
        private MouseHandler() {
        }

        public void mouseClicked(MouseEvent e) {
            int viewColumn;
            JTableHeader h = (JTableHeader)e.getSource();
            TableColumnModel columnModel = h.getColumnModel();
            int column = columnModel.getColumn(viewColumn = columnModel.getColumnIndexAtX(e.getX())).getModelIndex();
            if (column == 0) {
                int status = TableSorter.this.getSortingStatus(column);
                e.isControlDown();
                if ((status += e.isShiftDown() ? -1 : 1) > 1) {
                    status = 0;
                }
                if (status < 0) {
                    status = 0;
                }
                TableSorter.this.m_model.setSortStatus(status);
            }
        }
    }

    private class Row
    implements Comparable {
        private int modelIndex;

        public Row(int index) {
            this.modelIndex = index;
        }

        public int compareTo(Object o) {
            int row1 = this.modelIndex;
            int row2 = ((Row)o).modelIndex;
            for (Directive directive : TableSorter.this.sortingColumns) {
                int column = directive.column;
                Object o1 = TableSorter.this.tableModel.getValueAt(row1, column);
                Object o2 = TableSorter.this.tableModel.getValueAt(row2, column);
                int comparison = 0;
                comparison = o1 == null && o2 == null ? 0 : (o1 == null ? -1 : (o2 == null ? 1 : TableSorter.this.getComparator(column).compare(o1, o2)));
                if (comparison == 0) continue;
                return directive.direction == -1 ? - comparison : comparison;
            }
            return 0;
        }
    }

    private class SortableHeaderRenderer
    implements TableCellRenderer {
        private TableCellRenderer tableCellRenderer;

        public SortableHeaderRenderer(TableCellRenderer tableCellRenderer) {
            this.tableCellRenderer = tableCellRenderer;
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = this.tableCellRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (c instanceof JLabel) {
                JLabel l = (JLabel)c;
                l.setHorizontalTextPosition(2);
                int modelColumn = table.convertColumnIndexToModel(column);
                l.setIcon(TableSorter.this.getHeaderRendererIcon(modelColumn, l.getFont().getSize()));
            }
            return c;
        }
    }

    private class TableModelHandler
    implements TableModelListener {
        private TableModelHandler() {
        }

        public void tableChanged(TableModelEvent e) {
            int status = TableSorter.this.m_model.getSortStatus();
            if (status != TableSorter.this.getSortingStatus(0)) {
                TableSorter.this.setSortingStatus(0, status);
                TableSorter.this.sortingStatusChanged();
            }
            if (!TableSorter.this.isSorting()) {
                TableSorter.this.clearSortingState();
                TableSorter.this.fireTableChanged(e);
                return;
            }
            if (e.getFirstRow() == -1) {
                TableSorter.this.cancelSorting();
                TableSorter.this.fireTableChanged(e);
                return;
            }
            int column = e.getColumn();
            if (e.getFirstRow() == e.getLastRow() && column != -1 && TableSorter.this.getSortingStatus(column) == 0 && TableSorter.this.modelToView != null) {
                int viewIndex = TableSorter.this.getModelToView()[e.getFirstRow()];
                TableSorter.this.fireTableChanged(new TableModelEvent(TableSorter.this, viewIndex, viewIndex, column, e.getType()));
                return;
            }
            TableSorter.this.clearSortingState();
            TableSorter.this.fireTableDataChanged();
        }
    }

}

