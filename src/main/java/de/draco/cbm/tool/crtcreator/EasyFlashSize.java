/*
 * Decompiled with CFR 0.137.
 */
package de.draco.cbm.tool.crtcreator;

public class EasyFlashSize {
    int m_banks = 0;
    String m_description;

    public EasyFlashSize(int banks, String description) {
        this.m_banks = banks;
        this.m_description = description;
    }

    public String toString() {
        return String.valueOf(this.m_description) + " [" + this.m_banks + " banks]";
    }
}

