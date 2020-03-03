/*
 * Decompiled with CFR 0.137.
 */
package de.draco.cbm.tool.crtcreator;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Vector;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public abstract class EFItem
implements Constants {
    public static int DIRENTRYSIZE = 24;
    protected String m_name;
    protected byte m_flags;
    protected byte m_bank_high = 0;
    protected int m_bank;
    protected int m_offset;
    protected boolean m_hidden;
    protected int m_size;
    protected File m_file;
    protected EFType m_type = EFType.mode_unknown;
    byte[] m_bytes = new byte[0];

    public static EFItem fromBytes(byte[] bytes, int offset) {
        EFItem ret = null;
        byte type = (byte)(bytes[offset + 16] & 31);
        ret = type == 1 ? new EFItemPrg(bytes, offset) : new EFItemCrt(bytes, offset);
        ret.setHidden((bytes[offset + 16] & 128) != 0);
        return ret;
    }

    protected EFItem(byte[] bytes, int offset) {
        byte[] nameBytes = new byte[16];
        System.arraycopy(bytes, offset, nameBytes, 0, 16);
        this.m_name = Util.flipCase(new String(nameBytes).trim());
        this.m_flags = bytes[offset + 16];
        this.m_type = EFType.getTypeById((byte)(this.m_flags & 31));
        this.m_bank = bytes[offset + 17] & 255;
        this.m_bank_high = bytes[offset + 18];
        this.m_offset = (bytes[offset + 20] & 255) << 8 | bytes[offset + 19] & 255;
        this.m_size = (bytes[offset + 23] & 255) << 16 | (bytes[offset + 22] & 255) << 8 | bytes[offset + 21] & 255;
    }

    public EFItem(File file) {
        this.m_file = file;
        if (this.m_file != null && file.getName().startsWith("!")) {
            this.m_hidden = true;
        }
    }

    public byte[] getBytes() {
        return this.m_bytes;
    }

    public byte[] getDirEntryBytes() {
        byte[] ret = new byte[DIRENTRYSIZE];
        if (this.m_name != null) {
            System.arraycopy(Util.flipCase(this.m_name).getBytes(), 0, ret, 0, this.m_name.getBytes().length);
        } else {
            Logger.info(this.getClass(), "Name = null");
        }
        ret[16] = this.getFlags();
        ret[17] = (byte)this.m_bank;
        this.m_bank_high = 0;
        ret[18] = 0;
        ret[19] = (byte)(this.m_offset & 255);
        ret[20] = (byte)(this.m_offset >> 8);
        ret[21] = (byte)(this.m_size & 255);
        ret[22] = (byte)(this.m_size >> 8);
        ret[23] = (byte)(this.m_size >> 16);
        return ret;
    }

    private byte getFlags() {
        return (byte)((this.m_hidden ? 128 : 0) | this.m_type.getId() & 31);
    }

    public String getName() {
        return this.m_name;
    }

    public String toString() {
        return String.valueOf(this.m_name) + " (" + (Object)((Object)this.m_type) + ") s=" + this.m_size + ",o=" + this.m_offset + ",b=" + this.m_bank + ",f=" + Integer.toHexString(this.getFlags());
    }

    public byte[] getNameBytes() {
        byte[] ret = new byte[3];
        for (int i = 0; i < this.m_name.length() && i < 16; ++i) {
            ret[i] = (byte)this.m_name.charAt(i);
        }
        return ret;
    }

    public String getFileName() {
        String ret = "-";
        if (this.m_file != null) {
            ret = this.m_file.getName();
        }
        return ret;
    }

    public EFType getType() {
        return this.m_type;
    }

    public void setType(EFType type) {
        this.m_type = type;
    }

    public String getTypeDescription() {
        return this.m_type.toString();
    }

    public void setName(String name) {
        this.m_name = name;
    }

    public int getBank() {
        return this.m_bank;
    }

    public void setBank(byte bank) {
        this.m_bank = bank;
    }

    public byte getBankHigh() {
        return this.m_bank_high;
    }

    public void setBankHigh(byte bankHigh) {
        this.m_bank_high = bankHigh;
    }

    public int getOffset() {
        return this.m_offset;
    }

    public void setOffset(short offset) {
        this.m_offset = offset;
    }

    public int getSizeData() {
        return this.m_size;
    }

    public void setSizeData(int size) {
        this.m_size = size;
    }

    public boolean updateDataFromChips(Vector<Chip> chips) {
        boolean ret = true;
        int pageIdx = (this.m_bank & 255) * 2 + this.m_offset / 8192;
        int offsetInPage = this.m_offset % 8192;
        this.m_bytes = new byte[this.getSizeData()];
        int destPos = 0;
        int remLength = this.getSizeData();
        while (remLength > 0) {
            int chunkSize = Math.min(8192 - offsetInPage, remLength);
            if (pageIdx < chips.size()) {
                Chip p = chips.get(pageIdx);
                System.arraycopy(p.getBytes(), offsetInPage, this.m_bytes, destPos, chunkSize);
            } else {
                Logger.error(this.getClass(), ">>>>>>>>>>>>> Page index overflow error! <<<<<<<<<<<<<<<<<");
                ret = false;
            }
            remLength -= chunkSize;
            offsetInPage = 0;
            ++pageIdx;
            destPos += chunkSize;
        }
        return ret;
    }

    public boolean isEndMark() {
        return this.m_type == EFType.mode_endmark;
    }

    public String getAttributesString() {
        return this.m_hidden ? "!" : "-";
    }

    public abstract boolean write(FileOutputStream var1);

    public boolean isHidden() {
        return this.m_hidden;
    }

    public void setHidden(boolean hidden) {
        this.m_hidden = hidden;
    }

    public int compareTo(EFItem other) {
        return this.getName().compareTo(other.getName());
    }
}

