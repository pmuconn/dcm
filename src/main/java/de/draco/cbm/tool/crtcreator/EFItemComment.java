/*
 * Decompiled with CFR 0.137.
 */
package de.draco.cbm.tool.crtcreator;

import java.io.FileOutputStream;
import java.io.IOException;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class EFItemComment
extends EFItem
implements Comparable<EFItemComment> {
    String m_text = "";

    public EFItemComment(String text) {
        super(null);
        this.m_text = text;
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
    public String getName() {
        return this.m_text;
    }

    @Override
    public String toString() {
        return String.valueOf(this.m_text) + " (Comment)";
    }

    @Override
    public int compareTo(EFItemComment other) {
        return 0;
    }
}

