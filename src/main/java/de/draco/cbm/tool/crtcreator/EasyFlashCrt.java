/*
 * Decompiled with CFR 0.137.
 */
package de.draco.cbm.tool.crtcreator;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;

import de.draco.cbm.tool.crtcreator.gui.ExtensionFileFilter;
import de.draco.cbm.tool.crtcreator.gui.PrgFileFilter;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class EasyFlashCrt
extends Cart
implements Constants {
    int m_currentBank = 0;
    int[] m_sizeOptions = new int[]{917504, 1048576, 2080768};
    int m_sizeOpt = 1;
    int m_flashsize = this.m_sizeOptions[this.m_sizeOpt];
    int m_sorted = 1;
    boolean m_oceanMode = false;
    boolean m_useBootScreen = true;
    boolean m_useScreenSaver = true;
    EFItemCrt m_bootscreen = null;
    EFItemCrt m_screensaver = null;
    EFSDirChip m_dir;
    LoaderChip m_loaderPage;
    Vector<EFItem> m_items = new Vector();

    public EasyFlashCrt() {
        super("EasyFlash (DCM 1.0.1)", (short)256, (short)32, (byte)0, (byte)1);
        this.prepareSystemChips(false);
    }

    public EasyFlashCrt(InputStream is) {
        this.fromStream(is);
    }

    private void prepareSystemChips(boolean oceanMode) {
        int idx = 0;
        this.m_chips.clear();
        this.m_currentBank = 0;
        if (oceanMode) {
            this.m_chips.add(new Chip(idx++));
            this.m_dir = new EFSDirChip(idx++, oceanMode);
            this.m_chips.add(this.m_dir);
            this.m_chips.add(new Chip(idx++));
            this.m_loaderPage = new LoaderChip(idx++, oceanMode, this.m_sorted);
            this.m_loaderPage.init();
            this.m_chips.add(this.m_loaderPage);
        } else {
            this.m_loaderPage = new LoaderChip(idx++, oceanMode, this.m_sorted);
            this.m_loaderPage.init();
            this.m_chips.add(this.m_loaderPage);
            this.m_dir = new EFSDirChip(idx++, oceanMode);
            this.m_chips.add(this.m_dir);
            ++this.m_currentBank;
        }
        this.m_usedBanks = this.m_currentBank;
    }

    public boolean scanDir(File dir) {
        if (dir == null || !dir.isDirectory()) {
            return false;
        }
        this.m_items.addAll(this.getPrgs(dir));
        this.m_items.addAll(this.getCarts(dir));
        this.organize();
        return true;
    }

    public boolean addItem(EFItem item) {
        boolean ret = true;
        this.m_items.add(item);
        return ret;
    }

    public boolean importFiles(File[] files) {
        boolean ret = true;
        for (int i = 0; i < files.length; ++i) {
            if (files[i].isDirectory()) {
                this.scanDir(files[i]);
                continue;
            }
            if (files[i].getName().toLowerCase().endsWith(".prg")) {
                this.m_items.add(new EFItemPrg(files[i]));
                continue;
            }
            if (files[i].getName().toLowerCase().endsWith(".crt")) {
                EFItemCrt ci = new EFItemCrt(files[i]);
                if (!this.canImport(ci.getCart())) continue;
                this.m_items.add(ci);
                continue;
            }
            ret = false;
        }
        this.organize();
        return ret;
    }

    @Override
    public boolean fromStream(InputStream is) {
        boolean ret = super.fromStream(is);
        if (ret) {
            boolean bl = ret = this.m_header.getHwType() == 32;
        }
        if (ret) {
            int idx = 0;
            Vector<Chip> paddedChips = new Vector<Chip>();
            for (Chip chip : this.m_chips) {
                while (chip.getPageIndex() > idx) {
                    paddedChips.add(new Chip(idx++));
                }
                chip.setIndex(idx);
                paddedChips.add(chip);
                ++idx;
            }
            this.m_chips = paddedChips;
            Logger.info(this.getClass(), "EasyFlash cart name is " + this.m_header.getCartName());
            for (int i = this.m_chips.size(); i < this.getMaxNumChips(); ++i) {
                Chip page = new Chip(i);
                this.m_chips.add(page);
            }
            this.m_dir = new EFSDirChip(this.m_chips);
            EFItem boot = null;
            EFItem screensaver = null;
            Vector<EFItem> delItems = new Vector<EFItem>();
            for (EFItem item : this.m_dir.getItems()) {
                if (item.m_name.toLowerCase().contains("!el_boot-once")) {
                    boot = item;
                    delItems.add(item);
                }
                if (!item.m_name.toLowerCase().contains("!el_screen-saver")) continue;
                screensaver = item;
                delItems.add(item);
            }
            this.setUseScreenSaver(screensaver != null);
            this.setUseBootScreen(boot != null);
            this.m_dir.getItems().removeAll(delItems);
            this.m_chips.remove(1);
            this.m_chips.add(1, this.m_dir);
            this.setItems(this.m_dir.getItems());
            this.organize();
        }
        return ret;
    }

    public boolean readCart(File cart) {
        boolean ret = false;
        try {
            FileInputStream is = new FileInputStream(cart);
            ret = this.fromStream(is);
            is.close();
        }
        catch (FileNotFoundException e) {
            Logger.info(this.getClass(), "Can't read cart file.");
            e.printStackTrace();
        }
        catch (IOException e) {
            Logger.info(this.getClass(), "Can't access cart file.");
            e.printStackTrace();
        }
        return ret;
    }

    private Vector<EFItemCrt> getCarts(File dir) {
        Vector<EFItemCrt> crts = new Vector<EFItemCrt>();
        File[] crtfiles = dir.listFiles(new ExtensionFileFilter(new String[]{".crt"}, "Carts"));
        for (int i = 0; i < crtfiles.length; ++i) {
            Logger.info(this.getClass(), "--------------------------");
            Logger.info(this.getClass(), "checking cart " + crtfiles[i]);
            EFItemCrt cf = new EFItemCrt(crtfiles[i]);
            if (this.canImport(cf.getCart())) {
                Logger.info(this.getClass(), "adding cart " + crtfiles[i]);
                crts.add(cf);
                continue;
            }
            Logger.info(this.getClass(), "skipping cart " + crtfiles[i] + " (unsupported)");
        }
        return crts;
    }

    private Vector<EFItemPrg> getPrgs(File dir) {
        Vector<EFItemPrg> files = new Vector<EFItemPrg>();
        if (dir.isDirectory()) {
            File[] prgfiles = dir.listFiles(new PrgFileFilter());
            for (int i = 0; i < prgfiles.length; ++i) {
                Logger.info(this.getClass(), "--------------------------");
                Logger.info(this.getClass(), "checking file " + prgfiles[i]);
                files.add(new EFItemPrg(prgfiles[i]));
            }
        }
        return files;
    }

    public boolean exportItems(File dir) {
        boolean ret = false;
        if (!dir.isDirectory()) {
            return false;
        }
        for (EFItem item : this.m_items) {
            String ext = ".crt";
            if (item.m_type == EFType.mode_prg) {
                ext = ".prg";
            }
            File f = new File(String.valueOf(dir.getAbsolutePath()) + File.separator + EasyFlashCrt.secureFileName(new StringBuilder(String.valueOf(item.getName())).append(ext).toString()));
            try {
                FileOutputStream fos = new FileOutputStream(f);
                item.write(fos);
                fos.close();
            }
            catch (FileNotFoundException e) {
                Logger.error(this.getClass(), "File not found " + f);
                Logger.logStackTrace(e);
            }
            catch (IOException e) {
                Logger.error(this.getClass(), "Can't write file " + f);
                Logger.logStackTrace(e);
            }
        }
        return ret;
    }

    public static String secureFileName(String in) {
        return in.replaceAll("[^a-zA-Z0-9!. ]", "-");
    }

    public void setItems(Vector<EFItem> items) {
        this.m_items = items;
    }

    public Vector<EFItem> getItems() {
        return this.m_items;
    }

    public void remove(Vector<EFItem> items) {
        this.m_items.removeAll(items);
        this.organize();
    }

    public boolean organize() {
        boolean ret = true;
        Vector<EFItem> items_xbank = new Vector<EFItem>();
        Vector<EFItemCrt> items_ocean = new Vector<EFItemCrt>();
        Vector<EFItemCrt> items_16k = new Vector<EFItemCrt>();
        Vector<EFItemCrt> items_8k = new Vector<EFItemCrt>();
        Vector<EFItemCrt> items_m8k = new Vector<EFItemCrt>();
        Vector<EFItemPrg> items_prg = new Vector<EFItemPrg>();
        Vector<EFItem> items = new Vector<EFItem>(this.m_items.size());
        items.addAll(this.m_items);
        if (this.m_sorted != 0) {
            Collections.sort(items, new ItemComparator(this.m_sorted));
        }
        for (EFItem item : items) {
            if (item instanceof EFItemCrt) {
                EFItemCrt ci = (EFItemCrt)item;
                if (ci.getHeader().isXBank()) continue;
                if (((EFItemCrt)item).getHeader().isOcean()) {
                    item.setType(EFType.mode_16k);
                    items_ocean.add(ci);
                    continue;
                }
                switch (item.getType()) {
                    case mode_16k: 
                    case mode_m16k: {
                        items_16k.add(ci);
                        break;
                    }
                    case mode_8k: {
                        items_8k.add(ci);
                        break;
                    }
                    case mode_m8k: {
                        items_m8k.add(ci);
                    }
                    default: {
                        break;
                    }
                }
                continue;
            }
            if (!(item instanceof EFItemPrg)) continue;
            items_prg.add((EFItemPrg)item);
        }
        int topIdx = 0;
        if (this.m_useBootScreen) {
            items_m8k.add(this.getBootScreenCartItem());
            items.add(topIdx++, this.getBootScreenCartItem());
        }
        if (this.m_useScreenSaver) {
            items_m8k.add(this.getScreenSaverCartItem());
            items.add(topIdx++, this.getScreenSaverCartItem());
        }
        boolean oceanMode = items_ocean.size() > 0;
        this.prepareSystemChips(oceanMode);
        this.m_dir.clear();
        for (EFItem item : items) {
            this.m_dir.addEntry(item);
        }
        int mixedBank = this.m_currentBank;
        Iterator iterator = items_ocean.iterator();
        if (iterator.hasNext()) {
            EFItem item = (EFItem)iterator.next();
            EFItemCrt ic = (EFItemCrt)item;
            int bank = this.m_currentBank;
            item.setBank((byte)bank);
            for (Chip chip : ic.getChips()) {
                byte[] bytes = chip.getBytes();
                int idx = bank * 2 + (chip.usesHigh() ? 1 : 0);
                this.getChip(idx).addData(item, bytes, 0, 0);
                ++bank;
            }
            this.m_usedBanks = Math.max(this.m_usedBanks, bank);
        }
        for (EFItemCrt item : items_m8k) {
            this.placeCartItem(item, this.m_currentBank);
        }
        this.m_usedBanks = Math.max(this.m_usedBanks, this.m_currentBank);
        for (EFItemCrt item : items_8k) {
            this.placeCartItem(item, this.m_currentBank);
        }
        for (EFItem item : items_xbank) {
            Object xbtype = EFType.mode_16k;
            EFItemCrt ic = (EFItemCrt)item;
            xbtype = ic.getCart().usesLow() && ic.getCart().usesHigh() ? EFType.mode_16k : (ic.getCart().usesLow() ? EFType.mode_8k : EFType.mode_m8k);
            item.setType((EFType)((Object)xbtype));
            this.placeCartItem(ic, this.m_currentBank);
        }
        for (EFItemCrt item : items_16k) {
            this.placeCartItem(item, this.m_currentBank);
        }
        Collections.sort(items_prg);
        int pageIdx = mixedBank * 2;
        for (EFItem item : items_prg) {
            byte[] bytes = item.getBytes();
            int prgBytesPos = 0;
            block13 : while (prgBytesPos < bytes.length - 1) {
                if (this.prgFitsIn((int)pageIdx, bytes.length)) {
                    do {
                        Chip page = this.getChip((int)pageIdx);
                        if (prgBytesPos == 0) {
                            item.setOffset((short)page.getPosOffsetInBank());
                            item.setBank((byte)page.getBankIdx());
                        }
                        if ((prgBytesPos += page.addData(item, bytes, prgBytesPos, page.getPos())) >= bytes.length - 1) continue block13;
                        ++pageIdx;
                    } while (true);
                }
                ++pageIdx;
            }
            this.m_usedBanks = (int)Math.ceil((double)(pageIdx /* + true */) / 2.0);
        }
        Logger.info(this.getClass(), "-----------------------------------------------------------");
        Logger.info(this.getClass(), "Number of pages: " + this.m_chips.size());
        if (!ret) {
            Logger.error(this.getClass(), "************************");
            Logger.error(this.getClass(), ">> organize() failed1 <<");
            Logger.error(this.getClass(), "************************");
        }
        return ret;
    }

    private boolean placeCartItem(EFItemCrt item, int startBank) {
        boolean ret = true;
        int currentBank = this.locateBank(startBank, item);
        item.setBank((byte)currentBank);
        for (Chip chip : item.getChips()) {
            byte[] bytes = chip.getBytes();
            int sp = chip.usesLow() ? 0 : 1;
            int ep = chip.usesHigh() ? 1 : 0;
            for (int i = sp; i <= ep; ++i) {
                if (!this.getChip(currentBank * 2 + i).isEmpty()) {
                    ++currentBank;
                }
                this.getChip(currentBank * 2 + i).addData(item, bytes, (i - sp) * 8192, 0);
                this.m_usedBanks = Math.max(this.m_usedBanks, currentBank + 1);
            }
        }
        return ret;
    }

    private Chip getChip(int index) {
        if (index >= this.m_chips.size()) {
            int startIdx = this.m_chips.size();
            int num = index - startIdx + 1;
            for (int i = 0; i < num; ++i) {
                this.m_chips.add(new Chip(startIdx + i));
            }
        }
        return (Chip)this.m_chips.get(index);
    }

    public Chip getLowPage(int bankIdx) {
        return this.getChip(bankIdx * 2 + 0);
    }

    public Chip getHighPage(int bankIdx) {
        return this.getChip(bankIdx * 2 + 1);
    }

    public void printDir() {
        Logger.info(this.getClass(), "Directory:");
        Logger.info(this.getClass(), this.m_dir.toString());
    }

    public void printBanks() {
        Logger.info(this.getClass(), "Banks:");
        for (int bankIndex = 0; bankIndex < this.m_usedBanks; ++bankIndex) {
            String line = "[" + bankIndex + "]";
            for (int pageBankIndex = 0; pageBankIndex < 2; ++pageBankIndex) {
                line = String.valueOf(line) + pageBankIndex + ":";
                Chip page = this.getChip(bankIndex * 2 + pageBankIndex);
                line = String.valueOf(line) + String.format("%1$-40s", page.getDescription()) + " | ";
            }
            Logger.info(this.getClass(), line);
        }
    }

    private boolean prgFitsIn(int pageidx, int len) {
        Chip curpage;
        boolean ret = false;
        if (!(curpage = this.getChip(pageidx++)).isFull()) {
            int contPages;
            int offset = curpage.getPos();
            int reminpage = 8192 - offset;
            int remprg = Math.max(0, len - reminpage);
            int rempages = (int)Math.ceil((double)remprg / 8192.0);
            for (contPages = 0; contPages < rempages; ++contPages) {
                if (!this.getChip(pageidx++).isEmpty()) break;
            }
            ret = contPages >= rempages;
        }
        return ret;
    }

    private int locateBank(int bankidx, EFItemCrt item) {
        int endPageOffs;
        int numPages = (int)Math.ceil((double)item.getBytes().length / 8192.0);
        int startPageOffs = item.getCart().usesLow() ? 0 : 1;
        int n = endPageOffs = item.getCart().usesHigh() ? 1 : 0;
        if (endPageOffs < startPageOffs) {
            endPageOffs = startPageOffs;
        }
        int loc = -1;
        int curbank = bankidx;
        int remPages = numPages;
        block0 : while (remPages > 0) {
            for (int pageoffs = startPageOffs; pageoffs <= endPageOffs; ++pageoffs) {
                Chip curpage = this.getChip(curbank * 2 + pageoffs);
                if (curpage.isEmpty()) {
                    if (loc == -1) {
                        loc = curbank;
                    }
                    --remPages;
                    continue;
                }
                loc = -1;
                remPages = numPages;
                ++curbank;
                continue block0;
            }
        }
        return loc;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean writeCart(File cartfile) {
        boolean ret = true;
        FileOutputStream fos = null;
        this.organize();
        try {
            try {
                fos = new FileOutputStream(cartfile);
                DataOutputStream dos = new DataOutputStream(fos);
                byte[] filler = new byte[6];
                dos.write("C64 CARTRIDGE   ".getBytes(), 0, 16);
                dos.writeInt(64);
                dos.writeShort(256);
                dos.writeShort(32);
                dos.write(1);
                dos.write(0);
                dos.write(filler);
                dos.write("Easyflash Multicart (DCM 1.0.1)                             ".getBytes(), 0, 32);
                for (Chip chip : this.m_chips) {
                    if (chip.isEmpty()) continue;
                    ret &= chip.writeChip(dos);
                }
                return ret;
            }
            catch (FileNotFoundException e) {
                Logger.error(this.getClass(), "Can't access output file " + cartfile);
                Logger.logStackTrace(e);
                ret = false;
                if (fos == null) return ret;
                try {
                    fos.close();
                    return ret;
                }
                catch (Exception exception) {}
                return ret;
            }
            catch (IOException e) {
                Logger.error(this.getClass(), "Can't write output file " + cartfile);
                Logger.logStackTrace(e);
                ret = false;
                if (fos == null) return ret;
                try {
                    fos.close();
                    return ret;
                }
                catch (Exception exception) {}
                return ret;
            }
        }
        finally {
            if (fos != null) {
                try {
                    fos.close();
                }
                catch (Exception exception) {}
            }
        }
    }

    public int getSizeOption() {
        return this.m_sizeOpt;
    }

    public void setSizeOption(int sizeOpt) {
        this.m_sizeOpt = sizeOpt;
        this.m_flashsize = this.m_sizeOptions[sizeOpt];
        this.organize();
    }

    public int getMaxNumChips() {
        return this.m_flashsize / 8192;
    }

    public int getBytesUsed() {
        int bytes = 0;
        for (Chip chip : this.m_chips) {
            bytes += chip.getPos();
        }
        return bytes;
    }

    public int getBytesMax() {
        return this.m_flashsize;
    }

    public boolean up(EFItem item) {
        boolean ret = false;
        int idx = this.m_items.indexOf(item);
        if (idx > 0) {
            EFItem predecessor = this.m_items.get(idx - 1);
            this.m_items.set(idx - 1, item);
            this.m_items.set(idx, predecessor);
            ret = true;
        }
        return ret;
    }

    public boolean down(EFItem item) {
        boolean ret = false;
        int idx = this.m_items.indexOf(item);
        if (idx + 1 < this.m_items.size()) {
            EFItem successor = this.m_items.get(idx + 1);
            this.m_items.set(idx + 1, item);
            this.m_items.set(idx, successor);
            ret = true;
        }
        return ret;
    }

    public int getUsedBanks() {
        return this.m_usedBanks;
    }

    public int getMaxBanks() {
        return this.m_flashsize / 16384;
    }

    public void setSortStatus(int status) {
        this.m_sorted = status;
    }

    public int getSortStatus() {
        return this.m_sorted;
    }

    private EFItemCrt getBootScreenCartItem() {
        if (this.m_bootscreen == null) {
            this.m_bootscreen = this.getCartItemFromResources("boot-screen.crt", "!el_boot-once");
            this.m_bootscreen.setType(EFType.mode_m8k);
        }
        return this.m_bootscreen;
    }

    private EFItemCrt getScreenSaverCartItem() {
        if (this.m_screensaver == null) {
            this.m_screensaver = this.getCartItemFromResources("screen-saver.crt", "!el_screen-saver");
            this.m_screensaver.setType(EFType.mode_m8k);
        }
        return this.m_screensaver;
    }

    private EFItemCrt getCartItemFromResources(String filename, String cartAlias) {
        EFItemCrt ret = null;
        Cart cart = new Cart();
        cart.fromStream(this.getClass().getClassLoader().getResourceAsStream(filename));
        if (cartAlias != null) {
            cart.setAlias(cartAlias);
        }
        ret = new EFItemCrt(cart, filename);
        ret.setHidden(true);
        return ret;
    }

    public boolean getUseBootScreen() {
        return this.m_useBootScreen;
    }

    public void setUseBootScreen(boolean useBootScreen) {
        this.m_useBootScreen = useBootScreen;
    }

    public boolean getUseScreenSaver() {
        return this.m_useScreenSaver;
    }

    public void setUseScreenSaver(boolean useScreenSaver) {
        this.m_useScreenSaver = useScreenSaver;
    }

    public boolean canImport(Cart cf) {
        short hw = cf.getHeader().getHwType();
        return hw == 0 || hw == 5;
    }
}

