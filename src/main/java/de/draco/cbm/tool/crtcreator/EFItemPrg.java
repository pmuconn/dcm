/*
 * Decompiled with CFR 0.137.
 */
package de.draco.cbm.tool.crtcreator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class EFItemPrg
extends EFItem
implements Comparable<EFItemPrg> {
    protected EFItemPrg(byte[] bytes, int offset) {
        super(bytes, offset);
    }

    public EFItemPrg(File file) {
        super(file);
        this.m_type = EFType.mode_prg;
        String fname = this.m_file.getName();
        int pos = fname.toLowerCase().lastIndexOf(".prg");
        this.m_name = fname.substring(0, pos != -1 ? pos : fname.length());
        try {
            FileInputStream is = new FileInputStream(this.m_file);
            this.m_size = is.available();
            this.m_bytes = new byte[this.m_size];
            is.read(this.m_bytes);
            is.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean write(FileOutputStream fos) {
        boolean ret = false;
        try {
            fos.write(this.getBytes());
            ret = true;
        }
        catch (IOException e) {
            Logger.error(this.getClass(), "Can't write program to stream.");
            Logger.logStackTrace(e);
        }
        return ret;
    }

    @Override
    public int compareTo(EFItemPrg other) {
        return this.getSizeData() - other.getSizeData();
    }

    @Override
    public boolean updateDataFromChips(Vector<Chip> chips) {
        boolean ret = super.updateDataFromChips(chips);
        int chipIdx = this.m_bank * 2;
        if (chipIdx < 0) {
            return false;
        }
        int offsetInPage = this.getOffset() % 8192;
        int pageIdx = (this.m_bank & 255) * 2 + this.m_offset / 8192;
        int nPages = (int)Math.ceil((double)(this.getSizeData() + offsetInPage) / 8192.0);
        if (nPages >= chips.size()) {
            Logger.error(this.getClass(), "Error in directory structure. Number of Pages not supported. Item " + this);
            System.exit(-1);
        }
        for (int i = pageIdx; i < pageIdx + nPages; ++i) {
            if (i < chips.size()) {
                Logger.info(this.getClass(), "adding " + this + " to " + i);
                chips.get(i).addItemReference(this);
                continue;
            }
            Logger.error(this.getClass(), "Error in directory structure. Can't add " + this + " to " + i);
            System.exit(-1);
        }
        return ret;
    }
}

