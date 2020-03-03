/*
 * Decompiled with CFR 0.137.
 */
package de.draco.cbm.tool.crtcreator;

import java.util.Comparator;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class ItemComparator
implements Comparator<EFItem>,
Constants {
    int m_sortStatus = 0;

    public ItemComparator(int sortStatus) {
        this.m_sortStatus = sortStatus;
    }

    @Override
    public int compare(EFItem o1, EFItem o2) {
        int ret = 0;
        if (o1.getName() != null && o2.getName() != null) {
            ret = o1.getName().compareTo(o2.getName());
            if (this.m_sortStatus == -1) {
                ret *= -1;
            }
        } else {
            Logger.error(this.getClass(), "Found item with no name");
        }
        return ret;
    }
}

