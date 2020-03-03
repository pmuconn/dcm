/*
 * Decompiled with CFR 0.137.
 */
package de.draco.cbm.tool.crtcreator;

import java.io.IOException;
import java.io.InputStream;

public class LoaderChip
extends Chip
implements Constants {
    boolean m_oceanMode = false;
    int m_sortStatus = 1;

    public LoaderChip(int index, boolean oceanMode, int sortStatus) {
        super(index);
        this.m_oceanMode = oceanMode;
        this.m_sortStatus = sortStatus;
    }

    public boolean init() {
        String loaderFileName;
        boolean ret = false;
        String string = loaderFileName = this.m_oceanMode ? "easyloader_ocm.prg" : "easyloader_nrm.prg";
        if (this.m_sortStatus != 1) {
            loaderFileName = this.m_oceanMode ? "easyloader_ocm_nosort.prg" : "easyloader_nrm_nosort.prg";
        }
        InputStream is = LoaderChip.class.getClassLoader().getResourceAsStream(loaderFileName);
        try {
            is.read();
            is.read();
            ret = this.readRaw(is);
        }
        catch (IOException e) {
            Logger.error(this.getClass(), "Can't load page from " + loaderFileName);
            Logger.logStackTrace(e);
        }
        return ret;
    }

    public String getDescription() {
        return "EasyLoader";
    }
}

