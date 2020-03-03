/*
 * Decompiled with CFR 0.137.
 */
package de.draco.cbm.tool.crtcreator;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Vector;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class Cart
implements Constants {
    Header m_header;
    Vector<Chip> m_chips;
    int m_usedBanks = 0;
    int m_maxChipSize = 0;
    int m_size;
    byte[] m_bytes;
    EFType m_type;
    File m_file;
    String m_alias = null;
    boolean m_usesHigh = false;
    boolean m_usesLow = false;

    public Cart() {
        this.m_header = new Header();
        this.m_chips = new Vector();
    }

    public Cart(String name, short version, short hwtype, byte exromline, byte gameline) {
        this.m_header = new Header(name, version, hwtype, exromline, gameline);
        this.m_chips = new Vector();
    }

    public Cart(File file) {
        this();
        this.m_file = file;
        try {
            FileInputStream fs = new FileInputStream(file);
            this.fromStream(fs);
            fs.close();
        }
        catch (FileNotFoundException e) {
            Logger.error(this.getClass(), "File not found " + file);
            Logger.logStackTrace(e);
        }
        catch (IOException e) {
            Logger.error(this.getClass(), "Can't access file " + file);
            Logger.logStackTrace(e);
        }
    }

    public boolean fromStream(InputStream fs) {
        boolean ret = false;
        this.m_chips.clear();
        this.m_header = new Header();
        try {
            this.m_header.read(fs);
            int pageIndex = 0;
            while (fs.available() > 16) {
                Chip c = new Chip(pageIndex);
                c.readChip(fs);
                this.m_chips.add(c);
            }
            Collections.sort(this.m_chips);
            this.m_usedBanks = this.m_chips.lastElement().getBankIdx() + 1;
            ret = true;
        }
        catch (IOException e) {
            Logger.error(this.getClass(), "Can't read input stream.");
            Logger.logStackTrace(e);
        }
        this.m_size = 0;
        if (this.m_chips != null && this.m_chips.size() > 0) {
            for (Chip chip : this.m_chips) {
                this.m_size += chip.getLength();
                this.m_maxChipSize = Math.max(this.m_maxChipSize, chip.getLength());
                this.m_usesHigh |= chip.usesHigh();
                this.m_usesLow |= chip.usesLow();
            }
        }
        if (this.m_maxChipSize <= 8192) {
            this.m_type = this.m_header.isUltimax() ? EFType.mode_m8k : EFType.mode_8k;
        } else if (this.m_maxChipSize > 8192 && this.m_maxChipSize <= 16384) {
            this.m_type = this.m_header.isUltimax() ? EFType.mode_m16k : EFType.mode_16k;
        } else if (this.m_header.isOcean()) {
            this.m_type = EFType.mode_16k;
        }
        if (this.m_header.isXBank()) {
            this.m_type = EFType.mode_16k;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream(this.m_chips.size() * 8192);
        for (Chip chip : this.m_chips) {
            try {
                bos.write(chip.getBytes());
            }
            catch (Exception x) {
                Logger.error(this.getClass(), "Can't write bytestream!");
                Logger.logStackTrace(x);
            }
        }
        this.m_bytes = bos.toByteArray();
        return ret;
    }

    public boolean usesHigh() {
        return this.m_usesHigh;
    }

    public boolean usesLow() {
        return this.m_usesLow;
    }

    public boolean write(OutputStream os) {
        boolean ret = true;
        ret &= this.m_header.write(os);
        DataOutputStream dos = new DataOutputStream(os);
        for (Chip chip : this.m_chips) {
            if (chip.isEmpty()) continue;
            ret &= chip.writeChip(dos);
        }
        return ret;
    }

    public String getName() {
        String ret = "N/A";
        if (this.m_alias != null) {
            ret = this.m_alias;
        } else {
            int pos;
            ret = this.m_header.getCartName();
            if ((ret == null || ret.length() == 0) && this.m_file != null && (ret = ret.substring(0, (pos = (ret = this.m_file.getName()).toLowerCase().lastIndexOf(".")) != -1 ? pos : ret.length())).length() > 19) {
                ret = ret.substring(0, 18);
            }
            if (ret == null) {
                ret = "N/A";
            }
        }
        return ret;
    }

    public Vector<Chip> getChips() {
        return this.m_chips;
    }

    public void setChips(Vector<Chip> chips) {
        this.m_chips = chips;
        int size = 0;
        for (Chip chip : this.m_chips) {
            size += chip.m_bytes.length;
            this.m_usesHigh |= chip.usesHigh();
            this.m_usesLow |= chip.usesLow();
        }
        this.m_bytes = new byte[size];
        int offset = 0;
        for (Chip chip : this.m_chips) {
            System.arraycopy(chip.m_bytes, 0, this.m_bytes, offset, chip.m_bytes.length);
        }
    }

    public Header getHeader() {
        return this.m_header;
    }

    public void setHeader(Header header) {
        this.m_header = header;
    }

    public void setAlias(String alias) {
        this.m_alias = alias;
    }
}

