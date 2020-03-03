/*
 * Decompiled with CFR 0.137.
 */
package de.draco.cbm.tool.crtcreator;

public class Util {
    public static String flipCase(String in) {
        String out = "";
        if (in != null) {
            for (int i = 0; i < in.length(); ++i) {
                char c = in.charAt(i);
                out = c >= 'A' && c <= 'Z' || c >= '\u00c1' && c <= '\u00da' ? String.valueOf(out) + (char)(c + 32) : (c >= 'a' && c <= 'z' || c >= '\u00e1' && c <= '\u00fa' ? String.valueOf(out) + (char)(c - 32) : String.valueOf(out) + c);
            }
        }
        return out;
    }
}

