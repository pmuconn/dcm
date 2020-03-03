/*
 * Decompiled with CFR 0.137.
 */
package de.draco.cbm.tool.crtcreator.gui;

import java.io.File;

import javax.swing.UIManager;

import de.draco.cbm.tool.crtcreator.EasyFlashCrt;
import de.draco.cbm.tool.crtcreator.Logger;

public class CartMaker {
    boolean packFrame = false;

    public void run(String[] args) {
        if (args.length == 0) {
            EasyGui.getInstance().start();
        } else {
            String dirName = ".";
            String cartName = "dcm_multicart.crt";
            if (args.length >= 1) {
                dirName = args[0];
            }
            if (args.length >= 2) {
                cartName = args[1];
            }
            this.batch(dirName, cartName);
        }
    }

    private void batch(String dirName, String cartName) {
        File dir = new File(dirName);
        if (!dir.exists()) {
            Logger.error(this.getClass(), "not a directory <" + dirName + ">");
            this.usage();
        } else {
            EasyFlashCrt ef = new EasyFlashCrt();
            ef.scanDir(dir);
            ef.printDir();
            ef.printBanks();
            if (!ef.writeCart(new File(cartName))) {
                Logger.info(this.getClass(), "Failed to write " + cartName);
            }
        }
    }

    private void usage() {
        Logger.info(this.getClass(), "Start with GUI: java -jar <jarfile>");
        Logger.info(this.getClass(), "Start in batch mode: java -jar <jarfile> <dir to import> [destination.crt]");
    }

    public static void main(String[] args) {
        try {
            String os = System.getProperty("os.name");
            if (os.indexOf("Vista") >= 0 || os.indexOf("Windows 7") >= 0) {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        new CartMaker().run(args);
    }
}

