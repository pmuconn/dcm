/*
 * Decompiled with CFR 0.137.
 */
package de.draco.cbm.tool.crtcreator;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public enum EFType {
    mode_invalid("inv", 0, false, false),
    mode_prg("Program", 1, false, false),
    mode_8k("8K", 16, false, true),
    mode_16k("16K", 17, false, false),
    mode_m16k("U16K", 18, true, false),
    mode_m8k("U8K", 19, true, false),
    mode_endmark("end", 31, false, false),
    mode_unknown("unknown", -1, false, false);
    
    private String m_name;
    private byte m_id;
    private boolean m_exrom;
    private boolean m_game;

    private EFType(String id, int type, boolean exrom, boolean game) {
        this.m_name = id;
        this.m_id = (byte)type;
        this.m_exrom = exrom;
        this.m_game = game;
    }

    public static EFType getTypeById(byte typeId) {
        EFType ret = null;
        switch (typeId) {
            case 0: {
                ret = mode_invalid;
                break;
            }
            case 1: {
                ret = mode_prg;
                break;
            }
            case 16: {
                ret = mode_8k;
                break;
            }
            case 17: {
                ret = mode_16k;
                break;
            }
            case 18: {
                ret = mode_m16k;
                break;
            }
            case 19: {
                ret = mode_m8k;
                break;
            }
            case 31: {
                ret = mode_endmark;
                break;
            }
            default: {
                ret = mode_unknown;
            }
        }
        return ret;
    }

    boolean isUltimax() {
        return this.m_exrom && !this.m_game;
    }

    public boolean getExrom() {
        return this.m_exrom;
    }

    public boolean getGame() {
        return this.m_game;
    }

    public String getName() {
        return this.m_name;
    }

    public byte getId() {
        return this.m_id;
    }

    public String toString() {
        return this.m_name;
    }
}

