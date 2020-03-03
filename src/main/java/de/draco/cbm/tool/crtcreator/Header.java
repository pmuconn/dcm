/*
 * Decompiled with CFR 0.137.
 */
package de.draco.cbm.tool.crtcreator;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Header {
    static final String[] HWTYPES = new String[]{"Cart", "Action Replay", "KCS Power Cartridge", "Final Cartridge III", "Simons Basic", "Ocean Type 1", "Expert Cartridge", "Fun Play", "Super Games", "Atomic Power", "Epyx Fastload", "Westermann", "Rex", "Final Cartridge I", "Magic Formel", "C64 Game System", "Warpspeed", "Dinamic", "Zaxxon", "Magic Desk", "Super Snapshot 5", "COMAL 80", "Unknown 22", "Unknown 23", "Unknown 24", "Unknown 25", "Unknown 26", "Unknown 27", "Unknown 28", "Unknown 29", "Unknown 30", "Unknown 31", "EasyFlash", "XBank", "Unknown 34", "Unknown 35"};
    static final String CARTMAGIC = "C64 CARTRIDGE   ";
    static final int HWTYPE_NORMAL = 0;
    static final int HWTYPE_OCEAN = 5;
    static final int HWTYPE_EASYFLASH = 32;
    static final int HWTYPE_XBANK = 33;
    private String m_cartmagic;
    private int m_fhlen;
    private short m_version;
    private short m_hwtype;
    private byte m_exromline;
    private byte m_gameline;
    private String m_name;

    public Header() {
        this.m_version = (short)256;
    }

    public Header(String name, short version, short hwtype, byte exromline, byte gameline) {
        this.m_name = name;
        this.m_version = version;
        this.m_hwtype = hwtype;
        this.m_exromline = exromline;
        this.m_gameline = gameline;
    }

    public boolean read(InputStream fs) {
        boolean ret;
        DataInputStream dis;
        String cartmagic;
        block4 : {
            ret = false;
            dis = new DataInputStream(fs);
            byte[] cartmagic_bytes = new byte[16];
            int lenread = 0;
			try {
				lenread = dis.read(cartmagic_bytes);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            cartmagic = new String(cartmagic_bytes);
            if (lenread == 16 && cartmagic.equals(CARTMAGIC)) break block4;
            return false;
        }
        try {
            this.m_cartmagic = new String(cartmagic);
            this.m_fhlen = dis.readInt();
            this.m_version = dis.readShort();
            this.m_hwtype = dis.readShort();
            this.m_exromline = dis.readByte();
            this.m_gameline = dis.readByte();
            byte[] fubuff = new byte[6];
            dis.read(fubuff);
            byte[] namebuff = new byte[20];
            dis.read(namebuff);
            this.m_name = new String(namebuff).trim();
            Logger.info(this.getClass(), "---- Cart ----");
            Logger.info(this.getClass(), "m_cartmagic = " + this.m_cartmagic);
            Logger.info(this.getClass(), "m_fhlen = " + this.m_fhlen);
            Logger.info(this.getClass(), "m_version = " + this.m_version);
            Logger.info(this.getClass(), "m_exromline = " + this.m_exromline);
            if (this.m_hwtype >= 0 && this.m_hwtype < HWTYPES.length) {
                Logger.info(this.getClass(), "m_hwtype = " + HWTYPES[this.m_hwtype] + " (" + this.m_hwtype + ")");
            }
            Logger.info(this.getClass(), "m_gameline = " + this.m_gameline);
            Logger.info(this.getClass(), "m_name = " + this.m_name);
        }
        catch (IOException e) {
            Logger.error(this.getClass(), "Can't read file. e=" + e);
            Logger.logStackTrace(e);
        }
        return ret;
    }

    public boolean write(OutputStream fos) {
        boolean ret = false;
        DataOutputStream dos = new DataOutputStream(fos);
        try {
            byte[] filler = new byte[6];
            dos.write(CARTMAGIC.getBytes(), 0, 16);
            dos.writeInt(64);
            dos.writeShort(this.m_version);
            dos.writeShort(this.m_hwtype);
            dos.write(this.m_exromline);
            dos.write(this.m_gameline);
            dos.write(filler);
            Logger.info(this.getClass(), "Writing cart " + this.m_name);
            dos.write(this.m_name.getBytes());
            filler = new byte[32 - this.m_name.getBytes().length];
            dos.write(filler);
            ret = true;
        }
        catch (IOException e) {
            Logger.error(this.getClass(), "Can't write header to stream.");
            Logger.logStackTrace(e);
        }
        return ret;
    }

    public String getCartName() {
        return this.m_name;
    }

    public void setCartName(String name) {
        this.m_name = name;
    }

    public int getHeaderLen() {
        return this.m_fhlen;
    }

    public short getVersion() {
        return this.m_version;
    }

    public void setVersion(short version) {
        this.m_version = version;
    }

    public short getHwType() {
        return this.m_hwtype;
    }

    public void setHwType(short hwType) {
        this.m_hwtype = hwType;
    }

    public String getHwTypeDescription() {
        String ret = "Unknown" + this.m_hwtype;
        if (this.m_hwtype < HWTYPES.length) {
            ret = HWTYPES[this.m_hwtype];
        }
        return ret;
    }

    public byte getExromLine() {
        return this.m_exromline;
    }

    public void setExromLine(byte exromLine) {
        this.m_exromline = exromLine;
    }

    public byte getGameLine() {
        return this.m_gameline;
    }

    public void setGameLine(byte gameLine) {
        this.m_gameline = gameLine;
    }

    public boolean isUltimax() {
        return this.getExromLine() == 1 && this.getGameLine() == 0;
    }

    public boolean isXBank() {
        return this.getHwType() == 33;
    }

    public boolean isOcean() {
        return this.getHwType() == 5;
    }
}

