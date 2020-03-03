/*
 * Decompiled with CFR 0.137.
 */
package de.draco.cbm.tool.crtcreator;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class EFSDirChip
extends Chip
implements Constants {
    public static int MAX_ITEMS = 254;
    boolean m_oceanMode = false;

    public EFSDirChip(int index, boolean oceanMode) {
        super(index);
        this.m_oceanMode = oceanMode;
        this.setPos(8192);
    }

    public EFSDirChip(Vector<Chip> chips) {
        super(1);
        this.reconstructItems(chips);
    }

    @Override
    public boolean readChip(InputStream is) {
        boolean ret = super.readChip(is);
        int offset = 0;
        while (offset + EFItem.DIRENTRYSIZE <= 8192) {
            EFItem item = EFItem.fromBytes(this.getBytes(), offset);
            if (item.isEndMark()) break;
            this.m_items.add(item);
            offset += EFItem.DIRENTRYSIZE;
        }
        return ret;
    }

    private boolean reconstructItems(Vector<Chip> chips) {
        boolean ret = true;
        Chip p = chips.get(1);
        this.m_bytes = p.getBytes();
        int offset = 0;
        while (offset + EFItem.DIRENTRYSIZE <= 8192) {
            EFItem item = EFItem.fromBytes(this.m_bytes, offset);
            if (item.isEndMark()) break;
            item.updateDataFromChips(chips);
            this.m_items.add(item);
            offset += EFItem.DIRENTRYSIZE;
        }
        this.m_items = (Vector)this.getItems().clone();
        for (EFItem item : this.m_items) {
            int offsetInPage = item.getOffset() % 8192;
            int nPages = (int)Math.ceil((double)(item.getSizeData() + offsetInPage) / 8192.0);
            if (nPages < chips.size()) continue;
            Logger.error(this.getClass(), "Error in directory structure. Number of Pages not supported. Item " + item);
            System.exit(-1);
        }
        return ret;
    }

    public boolean addEntry(EFItem entry) {
        boolean ret = false;
        if (!this.m_items.contains(entry) && this.m_items.size() <= MAX_ITEMS) {
            ret = true;
            this.m_items.add(entry);
        }
        return ret;
    }

    public void removeEntry(EFItem entry) {
        this.m_items.remove(entry);
    }

    @Override
    public byte[] getBytes() {
        try {
            int destPos = 0;
            for (EFItem entry : this.m_items) {
                byte[] entryBytes = entry.getDirEntryBytes();
                System.arraycopy(entryBytes, 0, this.m_bytes, destPos, entryBytes.length);
                destPos += entryBytes.length;
            }
            String startupCodeFileName = this.m_oceanMode ? "easyloader_launcher_ocm.bin" : "easyloader_launcher_nrm.bin";
            Logger.info(this.getClass(), "Using " + startupCodeFileName + " launchcode.");
            InputStream is = this.getClass().getClassLoader().getResourceAsStream(startupCodeFileName);
            int launcherLen = is.available();
            is.read(this.m_bytes, 8192 - launcherLen, launcherLen);
        }
        catch (IOException e) {
            Logger.error(this.getClass(), "Can't read easyloader_launcher_nrm.bin");
            Logger.logStackTrace(e);
        }
        try {
            Logger.info(this.getClass(), "Using eapi-am29f040-03 api.");
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("eapi-am29f040-03");
            is.read();
            is.read();
            is.read(this.m_bytes, 6144, is.available());
        }
        catch (IOException e) {
            Logger.error(this.getClass(), "Can't read easyloader_launcher_nrm.bin");
            Logger.logStackTrace(e);
        }
        return this.m_bytes;
    }

    @Override
    public String getDescription() {
        return "Easyflash Directory";
    }

    @Override
    public String toString() {
        String ret = "";
        for (EFItem entry : this.m_items) {
            ret = String.valueOf(ret) + entry.toString() + "\n";
        }
        return ret;
    }
}

