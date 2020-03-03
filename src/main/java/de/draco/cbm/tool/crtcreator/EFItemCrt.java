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
public class EFItemCrt
extends EFItem {
    Cart m_cart = new Cart();

    protected EFItemCrt(byte[] bytes, int offset) {
        super(bytes, offset);
        Header header = this.m_cart.getHeader();
        header.setCartName(this.m_name);
        header.setExromLine(this.m_type.getExrom() ? (byte)1 : 0);
        header.setGameLine(this.m_type.getGame() ? (byte)1 : 0);
    }

    public EFItemCrt(File file) {
        super(file);
        this.m_cart = new Cart(file);
        this.m_type = this.m_cart.m_type;
        this.m_name = this.m_cart.getName();
        this.m_offset = 0;
        this.m_size = this.m_cart.m_size;
    }

    public EFItemCrt(Cart cart, String filename) {
        super(new File(filename));
        this.m_cart = cart;
        this.m_type = this.m_cart.m_type;
        this.m_name = this.m_cart.getName();
        this.m_offset = 0;
        this.m_size = this.m_cart.m_size;
    }

    @Override
    public void setName(String name) {
        super.setName(name);
        this.m_cart.getHeader().setCartName(name);
    }

    @Override
    public String getName() {
        return this.m_cart.getName();
    }

    @Override
    public byte[] getBytes() {
        return this.m_cart.m_bytes;
    }

    public Vector<Chip> getChips() {
        return this.m_cart.getChips();
    }

    public void setChips(Vector<Chip> chips) {
        this.m_cart.setChips(chips);
    }

    public Cart getCart() {
        return this.m_cart;
    }

    public Header getHeader() {
        return this.m_cart.getHeader();
    }

    public void setHeader(Header header) {
        this.m_cart.setHeader(header);
    }

    @Override
    public boolean write(FileOutputStream fos) {
        return this.m_cart.write(fos);
    }

    @Override
    public String getTypeDescription() {
        return String.valueOf(this.m_cart.getHeader().getHwTypeDescription()) + " " + this.m_type.toString();
    }

    @Override
    public boolean updateDataFromChips(Vector<Chip> chips) {
        boolean ret = super.updateDataFromChips(chips);
        int chipIdx = this.m_bank * 2;
        if (chipIdx < 0) {
            return false;
        }
        Vector<Chip> crtChips = new Vector<Chip>();
        if (this.m_type == EFType.mode_16k || this.m_type == EFType.mode_m16k) {
            if (this.m_size > 16384) {
                int pagesLeft = (int)Math.ceil((double)this.m_size / 8192.0);
                int oceanBorder = 15;
                int sp = 0;
                int ep = 1;
                boolean ocean = false;
                if (chips.get(chipIdx).getBankIdx() == 0) {
                    ocean = true;
                    this.m_cart.getHeader().setHwType((short)5);
                    sp = 0;
                    ep = 0;
                } else {
                    this.m_cart.getHeader().setHwType((short)33);
                }
                int idx = 0;
                int pagesUsed = 0;
                while (pagesLeft > 0) {
                    if (ocean && pagesUsed > oceanBorder) {
                        sp = 1;
                        ep = 1;
                    }
                    for (int i = sp; i <= ep; ++i) {
                        Chip chip1 = chips.get(chipIdx + idx + i);
                        chip1.setPos(8192);
                        chip1.addItemReference(this);
                        Chip chip2 = chip1.clone();
                        chip2.setIndex(chip2.getPageIndex() - chipIdx);
                        crtChips.add(chip2);
                        idx += 2;
                        --pagesLeft;
                        ++pagesUsed;
                    }
                }
            } else {
                Chip chip1 = chips.get(chipIdx);
                chips.get(chipIdx).addItemReference(this);
                chip1.setIndex(0);
                Chip chip2 = chips.get(chipIdx + 1);
                chips.get(chipIdx + 1).addItemReference(this);
                chip2.setIndex(1);
                Chip c16k = new Chip(0, 16384);
                c16k.setBytes(chip1.getBytes(), 0);
                c16k.setBytes(chip2.getBytes(), 8192);
                crtChips.add(c16k);
            }
        } else if (this.m_type == EFType.mode_8k) {
            Chip chip1 = chips.get(chipIdx);
            chip1.setPos(8192);
            chips.get(chipIdx).addItemReference(this);
            chip1.setIndex(0);
            crtChips.add(chip1.clone());
        } else if (this.m_type == EFType.mode_m8k) {
            if (this.m_size > 8192) {
                for (int i = 0; i < this.m_size / 8192; ++i) {
                    Chip chip1 = chips.get(chipIdx + 1 + i * 2);
                    chip1.setPos(8192);
                    chip1.addItemReference(this);
                    Chip chip2 = chip1.clone();
                    chip2.setIndex(chip2.getPageIndex() - chipIdx);
                    crtChips.add(chip2);
                }
            } else {
                Chip chip2 = chips.get(chipIdx + 1).clone();
                chip2.setIndex(1);
                crtChips.add(chip2);
                chip2.addItemReference(this);
            }
        }
        this.m_cart.setChips(crtChips);
        return ret;
    }
}

