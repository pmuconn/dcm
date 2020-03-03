/*
 * Decompiled with CFR 0.137.
 */
package de.draco.cbm.tool.crtcreator;

public class Logger {
    static final int NON = 0;
    static final int ERR = 1;
    static final int WNG = 2;
    static final int INF = 3;
    static final int DBG1 = 10;
    static final int DBG2 = 11;
    static final int DBG3 = 12;
    static final int ALL = 99;
    static int m_debuglevel = 3;

    static {
        m_debuglevel = Integer.getInteger("dcm.debuglevel", 3);
    }

    public static void error(Class cl, String msg) {
        Logger.printerrorln(cl, msg, 1);
    }

    public static void info(Class cl, String msg) {
        Logger.println(cl, msg, 1);
    }

    public static void debug(Class cl, String msg) {
        Logger.println(cl, msg, 10);
    }

    public static void debug2(Class cl, String msg) {
        Logger.println(cl, msg, 11);
    }

    public static void debug3(Class cl, String msg) {
        Logger.println(cl, msg, 12);
    }

    public static void warning(Class cl, String msg) {
        Logger.printerrorln(cl, msg, 2);
    }

    private static void println(Class cl, String msg, int level) {
        if (m_debuglevel >= level) {
            System.out.println(String.valueOf(System.currentTimeMillis()) + "-" + Thread.currentThread().getName() + " " + cl.getName() + " (" + level + "): " + msg);
        }
    }

    private static void printerrorln(Class cl, String msg, int level) {
        System.err.println(String.valueOf(System.currentTimeMillis()) + "-" + Thread.currentThread().getName() + " " + cl.getName() + " (" + level + "): " + msg);
    }

    public static void logStackTrace(Exception x) {
        x.printStackTrace(System.err);
    }
}

