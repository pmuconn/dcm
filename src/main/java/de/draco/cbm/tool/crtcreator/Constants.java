/*
 * Decompiled with CFR 0.137.
 */
package de.draco.cbm.tool.crtcreator;

public interface Constants {
    public static final String VERSION = "DCM 1.0.1";
    public static final int CHIPSIZE = 8192;
    public static final int EF1REDIDX = 0;
    public static final int EF1STDIDX = 1;
    public static final int EF2STDIDX = 2;
    public static final int FLASHSIZE_0 = 917504;
    public static final int FLASHSIZE_1 = 1048576;
    public static final int FLASHSIZE_2 = 2080768;
    public static final int CHIPSPERBANK = 2;
    public static final int ITEM_NAME_LEN = 16;
    public static final String LOADER_NORMAL_NOSORT = "easyloader_nrm_nosort.prg";
    public static final String LOADER_OCEAN_NOSORT = "easyloader_ocm_nosort.prg";
    public static final String LOADER_NORMAL = "easyloader_nrm.prg";
    public static final String LOADER_OCEAN = "easyloader_ocm.prg";
    public static final String LOADER_NRM_LAUNCHCODE = "easyloader_launcher_nrm.bin";
    public static final String LOADER_OCM_LAUNCHCODE = "easyloader_launcher_ocm.bin";
    public static final String BOOTSCREEN = "boot-screen.crt";
    public static final String BOOTSCREEN_NAME = "!el_boot-once";
    public static final String SCREENSAVER = "screen-saver.crt";
    public static final String SCREENSAVER_NAME = "!el_screen-saver";
    public static final String EAPI_FILENAME = "eapi-am29f040-03";
    public static final int EAPI_OFFSET = 6144;
    public static final int DESCENDING = -1;
    public static final int NOT_SORTED = 0;
    public static final int ASCENDING = 1;
}

