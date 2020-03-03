/*
 * Decompiled with CFR 0.137.
 */
package de.draco.cbm.tool.crtcreator.gui;

import java.io.File;
import java.io.FileFilter;

public class PrgFileFilter
extends javax.swing.filechooser.FileFilter
implements FileFilter {
    public boolean accept(File pathname) {
        return pathname.getName().toLowerCase().endsWith(".prg");
    }

    public String getDescription() {
        return "Programs";
    }
}

