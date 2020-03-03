/*
 * Decompiled with CFR 0.137.
 */
package de.draco.cbm.tool.crtcreator.gui;

import java.io.File;
import java.io.FileFilter;

public class ExtensionFileFilter
extends javax.swing.filechooser.FileFilter
implements FileFilter {
    String[] m_extensions;
    String m_description;

    public ExtensionFileFilter(String[] extensions, String description) {
        this.m_extensions = extensions;
        this.m_description = description;
    }

    public boolean accept(File pathname) {
        boolean ret = false;
        for (int i = 0; i < this.m_extensions.length; ++i) {
            if (!pathname.getName().toLowerCase().endsWith(this.m_extensions[i])) continue;
            ret = true;
            break;
        }
        return ret;
    }

    public String getDescription() {
        return this.m_description;
    }
}

