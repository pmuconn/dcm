/*
 * Decompiled with CFR 0.137.
 */
package de.draco.cbm.tool.crtcreator;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class Chip
implements Comparable<Chip> {
    public static final int PAGESIZE = 8192;
    public static final int PAGESPERBANK = 2;
    private static final byte[] CHIPMAGIC = new byte[]{67, 72, 73, 80};
    private static final String[] CHIPTYPES = new String[]{"ROM", "RAM", "FLASH", "Unknown", "Unknown", "Unknown"};
    protected byte[] m_bytes;
    protected Vector<EFItem> m_items = new Vector();
    private int m_packetlength;
    private short m_chiptype;
    private int m_address;
    private int m_size = 8192;
    private int m_pos = 0;
    private int m_pageIndex = -1;
    protected boolean m_ultimax = false;

    private Chip() {
    }

    public Chip(int index) {
        this.m_pageIndex = index;
        this.m_bytes = new byte[this.m_size];
        this.clear();
    }

    public Chip(int index, int size) {
        this.m_pageIndex = index;
        this.m_size = size;
        this.m_bytes = new byte[this.m_size];
        this.clear();
    }

    public Chip(EFItem item, byte[] data) {
        this.m_bytes = new byte[data.length];
        this.addData(item, data, 0, 0);
    }

    public boolean usesLow() {
        return this.m_address == 32768 | this.m_size > 8192;
    }

    public boolean usesHigh() {
        return this.m_size > 8192 || this.m_address > 32768;
    }

    public boolean setBytes(byte[] bytes, int offset) {
        boolean ret = false;
        if (offset + bytes.length <= this.m_bytes.length) {
            System.arraycopy(bytes, 0, this.m_bytes, offset, bytes.length);
            ret = true;
            this.m_pos = offset + bytes.length;
        }
        return ret;
    }

    public void clear() {
        for (int i = 0; i < this.m_bytes.length; ++i) {
            this.m_bytes[i] = -1;
        }
        this.m_items.clear();
    }

    public void addItemReference(EFItem item) {
        this.m_items.add(item);
    }

    public int addData(EFItem item, byte[] data, int dataOffset, int pageOffset) {
        int numBytesAdded = 0;
        if (dataOffset >= 0 && dataOffset < data.length && pageOffset >= 0 && pageOffset < 8192) {
            int srclen = data.length - dataOffset;
            int dstlen = 8192 - pageOffset;
            numBytesAdded = Math.min(srclen, dstlen);
            System.arraycopy(data, dataOffset, this.m_bytes, pageOffset, numBytesAdded);
            this.m_pos += numBytesAdded;
            this.m_items.add(item);
        }
        return numBytesAdded;
    }

    public byte[] getBytes() {
        return this.m_bytes;
    }

    public int getPos() {
        return this.m_pos;
    }

    public void setPos(int pos) {
        this.m_pos = pos;
    }

    public int getPageIndex() {
        return this.m_pageIndex;
    }

    boolean isEmpty() {
        return this.m_pos == 0;
    }

    boolean isFull() {
        return this.m_pos >= 8191;
    }

    Vector<EFItem> getItems() {
        return this.m_items;
    }

    public int getPacketlength() {
        return this.m_packetlength;
    }

    public short getChipType() {
        return this.m_chiptype;
    }

    public int getPosOffsetInBank() {
        return this.m_pageIndex % 2 * 8192 + this.m_pos;
    }

    public int getBankIdx() {
        return this.m_pageIndex / 2;
    }

    public int getAddress() {
        return this.m_address;
    }

    public int getLength() {
        return this.m_size;
    }

    public String getDescription() {
        String ret = "";
        int i = 0;
        for (EFItem item : this.m_items) {
            short x = (short)item.getOffset();
            ret = String.valueOf(ret) + item.getName();
            if (++i >= this.m_items.size()) continue;
            ret = String.valueOf(ret) + ", ";
        }
        return ret;
    }

    public boolean readRaw(InputStream is) {
        boolean ret = false;
        this.m_pos = 0;
        this.m_items.clear();
        if (is != null) {
            try {
                int len = is.read(this.m_bytes);
                if (len != 0) {
                    ret = true;
                    this.m_pos = this.m_bytes.length;
                    this.m_size = this.m_bytes.length;
                }
            }
            catch (IOException e) {
                Logger.error(this.getClass(), "Can't read page from stream.");
                Logger.logStackTrace(e);
            }
        }
        return ret;
    }

    public boolean readChip(InputStream fs) {
        boolean ret = false;
        try {
            DataInputStream dis = new DataInputStream(fs);
            int c = 0;
            int magicpos = 0;
            boolean found = false;
            while (c != -1 && !found) {
                c = dis.read();
                if (c == CHIPMAGIC[magicpos]) {
                    if (++magicpos < CHIPMAGIC.length) continue;
                    found = true;
                    continue;
                }
                magicpos = 0;
            }
            if (found) {
                Logger.info(this.getClass(), "---- Chip ----");
                this.m_packetlength = dis.readInt();
                Logger.info(this.getClass(), "m_packetlength = " + this.m_packetlength);
                this.m_chiptype = dis.readShort();
                if (this.m_chiptype >= 0 && this.m_chiptype < CHIPTYPES.length) {
                    Logger.info(this.getClass(), "m_chiptype = " + CHIPTYPES[this.m_chiptype] + " (" + this.m_chiptype + ")");
                }
                short bankIndex = dis.readShort();
                this.m_address = dis.readShort() & 65535;
                Logger.info(this.getClass(), "m_address = " + Integer.toHexString(this.m_address));
                Logger.info(this.getClass(), "bankIndex = " + bankIndex);
                boolean highpage = this.m_address > 32768;
                this.m_pageIndex = bankIndex * 2 + (highpage ? 1 : 0);
                Logger.info(this.getClass(), "m_pageIndex = " + this.m_pageIndex);
                this.m_size = dis.readShort();
                Logger.info(this.getClass(), "m_size = " + this.m_size);
                int offset = 0;
                if (this.m_size == 4096 && this.m_address == 61440) {
                    this.m_size = 8192;
                    offset = 4096;
                }
                this.m_bytes = new byte[this.m_size];
                int lenread = dis.read(this.m_bytes, offset, this.m_bytes.length - offset);
                this.m_pos = this.m_bytes.length;
                byte[] skiprest = new byte[this.m_packetlength - lenread - 16];
                dis.read(skiprest);
            }
        }
        catch (IOException e) {
            Logger.error(this.getClass(), "Can't read file. e=" + e);
            Logger.logStackTrace(e);
        }
        return ret;
    }

    public boolean writeChip(DataOutputStream dos) {
        boolean ret = false;
        try {
            if (!this.isEmpty()) {
                byte[] bytes = this.getBytes();
                dos.write("CHIP".getBytes());
                dos.writeInt(bytes.length + 16);
                dos.writeShort(0);
                dos.writeShort(this.m_pageIndex / 2);
                if (this.m_items.size() >= 1 && this.m_items.get(0).getType().isUltimax()) {
                    dos.writeShort((this.m_pageIndex & 1) > 0 ? 57344 : 40960);
                } else {
                    dos.writeShort((this.m_pageIndex & 1) > 0 ? 40960 : 32768);
                }
                dos.writeShort(bytes.length);
                dos.write(bytes);
            }
            ret = true;
        }
        catch (IOException e) {
            Logger.error(this.getClass(), "Can't write page to output stream.");
            Logger.logStackTrace(e);
        }
        return ret;
    }

    @Override
    public int compareTo(Chip other) {
        return this.getPageIndex() - other.getPageIndex();
    }

    public void setIndex(int index) {
        this.m_pageIndex = index;
    }

    public String toString() {
        String ret = Integer.toHexString(this.m_address) + ":";
        for (EFItem item : this.m_items) {
            ret = String.valueOf(ret) + item.toString() + " ";
        }
        return ret;
    }

    public Chip clone() {
        Chip ret = new Chip();
        ret.m_address = this.m_address;
        ret.m_bytes = this.m_bytes;
        ret.m_items = new Vector(this.m_items.size());
        ret.m_items.addAll(this.m_items);
        ret.m_packetlength = this.m_packetlength;
        ret.m_chiptype = this.m_chiptype;
        ret.m_size = this.m_size;
        ret.m_pos = this.m_pos;
        ret.m_pageIndex = this.m_pageIndex;
        ret.m_ultimax = this.m_ultimax;
        return ret;
    }
}

