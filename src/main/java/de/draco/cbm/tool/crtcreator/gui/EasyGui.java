/*
 * Decompiled with CFR 0.137.
 */
package de.draco.cbm.tool.crtcreator.gui;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;

import de.draco.cbm.tool.crtcreator.Constants;
import de.draco.cbm.tool.crtcreator.EFItem;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class EasyGui
implements ActionListener,
FileDropListener,
Constants {
    private static EasyGui m_instance = new EasyGui();
    private EFMainFrame m_mainFrame;
    private ItemTablePanel m_tableView;
    private EasyModel m_model = new EasyModel();
    private Vector<EasyPanel> m_views = new Vector();
    private BanksFrame m_banksFrame;
    private HelpFrame m_helpFrame;
    private File m_currentDir;

    private EasyGui() {
    }

    public static EasyGui getInstance() {
        return m_instance;
    }

    public void start() {
        this.m_mainFrame = new EFMainFrame();
        this.m_mainFrame.validate();
        this.m_mainFrame.setVisible(true);
        for (EasyPanel view : this.m_views) {
            if (!(view instanceof ItemTablePanel)) continue;
            this.m_tableView = (ItemTablePanel)view;
        }
        this.m_model.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Exit")) {
            this.actionExit(e);
        } else if (e.getActionCommand().equals("Load")) {
            this.actionLoad(e);
        } else if (e.getActionCommand().equals("New")) {
            this.actionNew(e);
        } else if (e.getActionCommand().equals("Save")) {
            this.actionSave(e);
        } else if (e.getActionCommand().equals("SaveAs")) {
            this.actionSaveAs(e);
        } else if (e.getActionCommand().equals("Import")) {
            this.actionImport(e);
        } else if (e.getActionCommand().equals("Export")) {
            this.actionExport(e);
        } else if (e.getActionCommand().equals("Up")) {
            this.actionUp(e);
        } else if (e.getActionCommand().equals("Down")) {
            this.actionDown(e);
        } else if (e.getActionCommand().equals("Add")) {
            this.actionAdd(e);
        } else if (e.getActionCommand().equals("Remove")) {
            this.actionRemove(e);
        } else if (e.getActionCommand().equals("1MB")) {
            this.actionSize(e, 1);
        } else if (e.getActionCommand().equals("2MB")) {
            this.actionSize(e, 2);
        } else if (e.getActionCommand().equals("About")) {
            this.actionAbout(e);
        } else if (e.getActionCommand().equals("ShowBanks")) {
            this.actionShowBanks(e);
        } else if (e.getActionCommand().equals("Help")) {
            this.actionHelp(e);
        } else if (e.getActionCommand().equals("CloseHelp")) {
            this.actionCloseHelp(e);
        } else if (e.getActionCommand().equals("Spacer")) {
            this.actionSpacer(e);
        } else if (e.getActionCommand().equals("BootScreen")) {
            this.actionBootScreen(e);
        } else if (e.getActionCommand().equals("ScreenSaver")) {
            this.actionScreenSaver(e);
        } else if (e.getActionCommand().equals("RemoveSelected")) {
            this.actionRemove(e);
        } else if (e.getActionCommand().equals("Sort")) {
            this.actionSort(e);
        }
    }

    public void actionShowBanks(ActionEvent e) {
        if (this.m_banksFrame == null) {
            this.m_banksFrame = new BanksFrame();
        }
        if (!this.m_banksFrame.isShowing()) {
            this.m_banksFrame.show();
        }
        this.m_banksFrame.toFront();
    }

    public void actionHelp(ActionEvent e) {
        if (this.m_helpFrame == null) {
            this.m_helpFrame = new HelpFrame();
        }
        if (!this.m_helpFrame.isShowing()) {
            this.m_helpFrame.show();
        }
        this.m_helpFrame.toFront();
    }

    public void actionCloseHelp(ActionEvent e) {
        if (this.m_helpFrame != null) {
            this.m_helpFrame.setVisible(false);
        }
    }

    public void actionAbout(ActionEvent e) {
        AboutBox dlg = new AboutBox(this.m_mainFrame);
        Dimension dlgSize = dlg.getPreferredSize();
        Dimension frmSize = this.m_mainFrame.getSize();
        Point loc = this.m_mainFrame.getLocation();
        dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
        dlg.setModal(true);
        dlg.pack();
        dlg.show();
    }

    public void actionExit(ActionEvent e) {
        System.exit(0);
    }

    public EasyModel getModel() {
        return this.m_model;
    }

    public void actionLoad(ActionEvent e) {
        JFileChooser fc = new JFileChooser(this.m_currentDir);
        fc.addChoosableFileFilter(new ExtensionFileFilter(new String[]{".crt"}, "Carts"));
        fc.setFileSelectionMode(2);
        int ret = fc.showDialog(null, "OK");
        if (ret == 0) {
            File file = fc.getSelectedFile();
            if (file != null && file.isDirectory()) {
                this.m_currentDir = file;
            } else if (file.getParent() != null) {
                this.m_currentDir = file.getParentFile();
            }
            if (!this.m_model.readCart(file)) {
                JOptionPane.showMessageDialog(this.m_mainFrame, "Can't load cart. It's probably not an easyflash cart.");
            }
        }
    }

    public void actionNew(ActionEvent e) {
        this.m_model.clear();
    }

    public void actionSave(ActionEvent e) {
        JFileChooser fc = new JFileChooser(this.m_currentDir);
        fc.addChoosableFileFilter(new ExtensionFileFilter(new String[]{".crt"}, "Carts"));
        fc.setFileSelectionMode(0);
        int ret = fc.showDialog(null, "OK");
        if (ret == 0) {
            File file = fc.getSelectedFile();
            if (file.getParent() != null) {
                this.m_currentDir = file.getParentFile();
            }
            if (!file.getName().endsWith(".crt")) {
                file = new File(String.valueOf(file.getAbsolutePath()) + ".crt");
            }
            if (!this.m_model.writeCart(file)) {
                JOptionPane.showMessageDialog(this.m_mainFrame, "Failed to write cart.");
            }
        }
    }

    public void actionSaveAs(ActionEvent e) {
        JFileChooser fc = new JFileChooser(this.m_currentDir);
        fc.addChoosableFileFilter(new ExtensionFileFilter(new String[]{".crt"}, "Carts"));
        fc.setFileSelectionMode(0);
        int ret = fc.showDialog(null, "OK");
        if (ret == 0) {
            File file = fc.getSelectedFile();
            if (file.getParent() != null) {
                this.m_currentDir = file.getParentFile();
            }
            if (!file.getName().endsWith(".crt")) {
                file = new File(String.valueOf(file.getAbsolutePath()) + ".crt");
            }
            if (!this.m_model.writeCart(file)) {
                JOptionPane.showMessageDialog(this.m_mainFrame, "Failed to write cart.");
            }
        }
    }

    public void actionImport(ActionEvent e) {
        JFileChooser fc = new JFileChooser(this.m_currentDir);
        fc.setFileSelectionMode(1);
        int ret = fc.showDialog(null, "OK");
        if (ret == 0) {
            File file;
            this.m_currentDir = file = fc.getSelectedFile();
            this.m_model.scanDir(file);
        }
    }

    public void actionExport(ActionEvent e) {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(1);
        int ret = fc.showDialog(null, "OK");
        if (ret == 0) {
            File file;
            this.m_currentDir = file = fc.getSelectedFile();
            this.m_model.exportItems(file);
        }
    }

    public void actionUp(ActionEvent e) {
        Vector<EFItem> holder = this.getSelectedItems();
        if (holder.size() > 0) {
            this.m_model.actionUp(holder);
            this.setSelectedItems(holder);
        }
    }

    public void actionDown(ActionEvent e) {
        Vector<EFItem> holder = this.getSelectedItems();
        if (holder.size() > 0) {
            this.m_model.actionDown(holder);
            this.setSelectedItems(holder);
        }
    }

    public void actionSize(ActionEvent e, int sizeOpt) {
        this.m_model.setSizeOption(sizeOpt);
    }

    public void actionAdd(ActionEvent e) {
        JFileChooser fc = new JFileChooser(this.m_currentDir);
        fc.setFileSelectionMode(2);
        fc.setMultiSelectionEnabled(true);
        int ret = fc.showDialog(null, "Select files");
        if (ret == 0) {
            File file = fc.getSelectedFile();
            if (file != null && file.isDirectory()) {
                this.m_currentDir = file;
            } else if (file.getParent() != null) {
                this.m_currentDir = file.getParentFile();
            }
            File[] files = fc.getSelectedFiles();
            this.m_model.importFiles(files);
        }
    }

    public void actionRemove(ActionEvent e) {
        Vector<EFItem> holder = this.getSelectedItems();
        if (holder.size() > 0) {
            this.m_model.actionRemove(holder);
        }
    }

    public void actionSpacer(ActionEvent e) {
        this.m_model.spacer();
    }

    public void actionSort(ActionEvent e) {
        if (e.getSource() instanceof JToggleButton) {
            JToggleButton jtb = (JToggleButton)e.getSource();
            this.m_model.setSortStatus(jtb.isSelected() ? 1 : 0);
        } else {
            this.m_model.toggleSortStatus();
        }
    }

    public void actionBootScreen(ActionEvent e) {
        Object src = e.getSource();
        if (src instanceof JCheckBox) {
            this.m_model.setUseBootScreen(((JCheckBox)src).isSelected());
        }
    }

    public void actionScreenSaver(ActionEvent e) {
        Object src = e.getSource();
        if (src instanceof JCheckBox) {
            this.m_model.setUseScreenSaver(((JCheckBox)src).isSelected());
        }
    }

    private Vector<EFItem> getSelectedItems() {
        return this.m_tableView.getSelectedItems();
    }

    private void setSelectedItems(Vector<EFItem> items) {
        this.m_tableView.setSelectedItems(items);
    }

    public void setModel(EasyModel model) {
        this.m_model = model;
    }

    public void register(EasyPanel view) {
        view.addActionListener(this);
        this.m_views.add(view);
    }

    @Override
    public void filesDropped(File[] files) {
        this.m_model.importFiles(files);
    }
}

