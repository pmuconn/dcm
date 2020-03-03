/*
 * Decompiled with CFR 0.137.
 */
package de.draco.cbm.tool.crtcreator.gui;

import java.io.File;
import java.util.Vector;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.draco.cbm.tool.crtcreator.Chip;
import de.draco.cbm.tool.crtcreator.Constants;
import de.draco.cbm.tool.crtcreator.EFItem;
import de.draco.cbm.tool.crtcreator.EFItemComment;
import de.draco.cbm.tool.crtcreator.EasyFlashCrt;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class EasyModel
implements Constants {
    EasyFlashCrt m_cart = new EasyFlashCrt();
    Vector<ChangeListener> m_changeListeners = new Vector();
    boolean m_changeable = true;
    File m_workDir = null;
    private static final long serialVersionUID = 1L;

    public EasyModel() {
        this.m_cart = new EasyFlashCrt();
    }

    public EasyModel(EasyFlashCrt cart) {
        this.m_cart = cart;
        this.changed();
    }

    public void start() {
        this.update();
    }

    public void setCart(EasyFlashCrt cart) {
        this.m_cart = cart;
        this.changed();
    }

    public void setSizeOption(int sizeOpt) {
        this.m_cart.setSizeOption(sizeOpt);
        this.changed();
    }

    public int getSizeOption() {
        return this.m_cart.getSizeOption();
    }

    public void clear() {
        this.m_cart = new EasyFlashCrt();
        this.m_changeable = true;
        this.update();
    }

    public boolean scanDir(File dir) {
        boolean ret = this.m_cart.scanDir(dir);
        this.changed();
        return ret;
    }

    public boolean importFiles(File[] files) {
        boolean ret = this.m_cart.importFiles(files);
        this.changed();
        return ret;
    }

    public boolean readCart(File cart) {
        boolean ret = this.m_cart.readCart(cart);
        this.changed();
        return ret;
    }

    public boolean exportItems(File dir) {
        return this.m_cart.exportItems(dir);
    }

    public static String secureFileName(String in) {
        return in.replaceAll("[^a-zA-Z0-9!. ]", "-");
    }

    public void setItems(Vector<EFItem> items) {
        this.m_cart.setItems(items);
    }

    public Vector<EFItem> getItems() {
        return this.m_cart.getItems();
    }

    public Vector<EFItem> getItems(int[] rows) {
        Vector<EFItem> ret = new Vector<EFItem>();
        Vector<EFItem> allItems = this.m_cart.getItems();
        for (int i = 0; i < rows.length; ++i) {
            ret.add(allItems.get(rows[i]));
        }
        return ret;
    }

    public void printDir() {
        this.m_cart.printDir();
    }

    public boolean writeCart(File cartfile) {
        return this.m_cart.writeCart(cartfile);
    }

    public void update() {
        this.m_cart.organize();
        this.changed();
    }

    public void remove(Vector<EFItem> items) {
        this.m_cart.remove(items);
        this.changed();
    }

    public int getPercentUsed() {
        return (int)((double)this.getUsedBanks() * 100.0 / (double)this.getMax());
    }

    public int getUsedBanks() {
        return this.m_cart.getUsedBanks();
    }

    public int getMax() {
        return this.m_cart.getMaxBanks();
    }

    public Vector<Chip> getChips() {
        return this.m_cart.getChips();
    }

    private void changed() {
        for (ChangeListener l : this.m_changeListeners) {
            l.stateChanged(new ChangeEvent("Update"));
        }
    }

    public void addChangeListener(ChangeListener l) {
        if (!this.m_changeListeners.contains(l)) {
            this.m_changeListeners.add(l);
        }
    }

    public void removeChangeListener(ChangeListener l) {
        this.m_changeListeners.remove(l);
    }

    public void actionUp(Vector<EFItem> items) {
        if (items != null) {
            for (EFItem item : items) {
                if (!this.m_cart.up(item)) break;
            }
        }
        this.changed();
    }

    public void actionDown(Vector<EFItem> items) {
        if (items != null) {
            for (int index = items.size(); index > 0; --index) {
                EFItem item = items.get(index - 1);
                if (!this.m_cart.down(item)) break;
            }
        }
        this.changed();
    }

    public void actionRemove(Vector<EFItem> items) {
        if (items != null) {
            this.m_cart.remove(items);
        }
        this.changed();
    }

    public void spacer() {
        EFItemComment item = new EFItemComment("----------------");
        this.m_cart.addItem(item);
        this.changed();
    }

    public boolean isChangeable() {
        return this.m_changeable;
    }

    public void toggleSortStatus() {
        int status = this.m_cart.getSortStatus() + 1;
        if (status > 1) {
            status = 0;
        }
        this.m_cart.setSortStatus(status);
        this.changed();
    }

    public void setSortStatus(int status) {
        this.m_cart.setSortStatus(status);
        this.changed();
    }

    public int getSortStatus() {
        return this.m_cart.getSortStatus();
    }

    public boolean getUseBootScreen() {
        return this.m_cart.getUseBootScreen();
    }

    public void setUseBootScreen(boolean useBootScreen) {
        this.m_cart.setUseBootScreen(useBootScreen);
        this.update();
    }

    public boolean getUseScreenSaver() {
        return this.m_cart.getUseScreenSaver();
    }

    public void setUseScreenSaver(boolean useScreenSaver) {
        this.m_cart.setUseScreenSaver(useScreenSaver);
        this.update();
    }

    public File getWorkDir() {
        if (this.m_workDir == null) {
            this.m_workDir = new File(System.getProperty("user.dir"));
        }
        return this.m_workDir;
    }

    public void setWorkDir(File dir) {
        this.m_workDir = dir;
    }
}

