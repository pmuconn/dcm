/*
 * Decompiled with CFR 0.137.
 */
package de.draco.cbm.tool.crtcreator.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.net.URI;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;
import java.util.TooManyListenersException;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.Border;

import de.draco.cbm.tool.crtcreator.Logger;

public class FileDrop {
    private transient Border normalBorder;
    private transient DropTargetListener dropListener;
    private static Boolean supportsDnD;
    private static Color defaultBorderColor;
    private static String ZERO_CHAR_STRING;

    static {
        defaultBorderColor = new Color(0.0f, 0.0f, 1.0f, 0.25f);
        ZERO_CHAR_STRING = "\u0000";
    }

    public FileDrop(Component c, FileDropListener listener) {
        this(null, c, BorderFactory.createMatteBorder(2, 2, 2, 2, defaultBorderColor), true, listener);
    }

    public FileDrop(Component c, boolean recursive, FileDropListener listener) {
        this(null, c, BorderFactory.createMatteBorder(2, 2, 2, 2, defaultBorderColor), recursive, listener);
    }

    public FileDrop(PrintStream out, Component c, FileDropListener listener) {
        this(out, c, BorderFactory.createMatteBorder(2, 2, 2, 2, defaultBorderColor), false, listener);
    }

    public FileDrop(PrintStream out, Component c, boolean recursive, FileDropListener listener) {
        this(out, c, BorderFactory.createMatteBorder(2, 2, 2, 2, defaultBorderColor), recursive, listener);
    }

    public FileDrop(Component c, Border dragBorder, FileDropListener listener) {
        this(null, c, dragBorder, false, listener);
    }

    public FileDrop(Component c, Border dragBorder, boolean recursive, FileDropListener listener) {
        this(null, c, dragBorder, recursive, listener);
    }

    public FileDrop(PrintStream out, Component c, Border dragBorder, FileDropListener listener) {
        this(out, c, dragBorder, false, listener);
    }

    public FileDrop(final PrintStream out, final Component c, final Border dragBorder, boolean recursive, final FileDropListener listener) {
        if (FileDrop.supportsDnD()) {
            this.dropListener = new DropTargetListener(){

                public void dragEnter(DropTargetDragEvent evt) {
                    Logger.debug(this.getClass(), "FileDrop: dragEnter event.");
                    if (FileDrop.this.isDragOk(out, evt)) {
                        if (c instanceof JComponent) {
                            JComponent jc = (JComponent)c;
                            FileDrop.access$1(FileDrop.this, jc.getBorder());
                            Logger.debug(this.getClass(), "FileDrop: normal border saved.");
                            jc.setBorder(dragBorder);
                            Logger.debug(this.getClass(), "FileDrop: drag border set.");
                        }
                        evt.acceptDrag(1);
                        Logger.debug(this.getClass(), "FileDrop: event accepted.");
                    } else {
                        evt.rejectDrag();
                        Logger.debug(this.getClass(), "FileDrop: event rejected.");
                    }
                }

                public void dragOver(DropTargetDragEvent evt) {
                }

                public void drop(DropTargetDropEvent evt) {
                    Logger.debug(this.getClass(), "FileDrop: drop event.");
                    try {
                        try {
                            Transferable tr = evt.getTransferable();
                            if (tr.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                                evt.acceptDrop(1);
                                Logger.debug(this.getClass(), "FileDrop: file list accepted.");
                                List fileList = (List)tr.getTransferData(DataFlavor.javaFileListFlavor);
                                Iterator iterator = fileList.iterator();
                                File[] filesTemp = new File[fileList.size()];
                                fileList.toArray(filesTemp);
                                File[] files = filesTemp;
                                if (listener != null) {
                                    listener.filesDropped(files);
                                }
                                evt.getDropTargetContext().dropComplete(true);
                                Logger.debug(this.getClass(), "FileDrop: drop complete.");
                            } else {
                                DataFlavor[] flavors = tr.getTransferDataFlavors();
                                boolean handled = false;
                                for (int zz = 0; zz < flavors.length; ++zz) {
                                    if (!flavors[zz].isRepresentationClassReader()) continue;
                                    evt.acceptDrop(1);
                                    Logger.debug(this.getClass(), "FileDrop: reader accepted.");
                                    Reader reader = flavors[zz].getReaderForText(tr);
                                    BufferedReader br = new BufferedReader(reader);
                                    if (listener != null) {
                                        listener.filesDropped(FileDrop.createFileArray(br, out));
                                    }
                                    evt.getDropTargetContext().dropComplete(true);
                                    Logger.debug(this.getClass(), "FileDrop: drop complete.");
                                    handled = true;
                                    break;
                                }
                                if (!handled) {
                                    Logger.debug(this.getClass(), "FileDrop: not a file list or reader - abort.");
                                    evt.rejectDrop();
                                }
                            }
                        }
                        catch (IOException io) {
                            Logger.debug(this.getClass(), "FileDrop: IOException - abort:");
                            io.printStackTrace(out);
                            evt.rejectDrop();
                            if (c instanceof JComponent) {
                                JComponent jc = (JComponent)c;
                                jc.setBorder(FileDrop.this.normalBorder);
                                Logger.debug(this.getClass(), "FileDrop: normal border restored.");
                            }
                        }
                        catch (UnsupportedFlavorException ufe) {
                            Logger.debug(this.getClass(), "FileDrop: UnsupportedFlavorException - abort:");
                            ufe.printStackTrace(out);
                            evt.rejectDrop();
                            if (c instanceof JComponent) {
                                JComponent jc = (JComponent)c;
                                jc.setBorder(FileDrop.this.normalBorder);
                                Logger.debug(this.getClass(), "FileDrop: normal border restored.");
                            }
                        }
                    }
                    finally {
                        if (c instanceof JComponent) {
                            JComponent jc = (JComponent)c;
                            jc.setBorder(FileDrop.this.normalBorder);
                            Logger.debug(this.getClass(), "FileDrop: normal border restored.");
                        }
                    }
                }

                public void dragExit(DropTargetEvent evt) {
                    Logger.debug(this.getClass(), "FileDrop: dragExit event.");
                    if (c instanceof JComponent) {
                        JComponent jc = (JComponent)c;
                        jc.setBorder(FileDrop.this.normalBorder);
                        Logger.debug(this.getClass(), "FileDrop: normal border restored.");
                    }
                }

                public void dropActionChanged(DropTargetDragEvent evt) {
                    Logger.debug(this.getClass(), "FileDrop: dropActionChanged event.");
                    if (FileDrop.this.isDragOk(out, evt)) {
                        evt.acceptDrag(1);
                        Logger.debug(this.getClass(), "FileDrop: event accepted.");
                    } else {
                        evt.rejectDrag();
                        Logger.debug(this.getClass(), "FileDrop: event rejected.");
                    }
                }
            };
            this.makeDropTarget(out, c, recursive);
        } else {
            Logger.debug(this.getClass(), "FileDrop: Drag and drop is not supported with this JVM");
        }
    }

    private static boolean supportsDnD() {
        if (supportsDnD == null) {
            boolean support = false;
            try {
                Class<?> arbitraryDndClass = Class.forName("java.awt.dnd.DnDConstants");
                support = true;
            }
            catch (Exception e) {
                support = false;
            }
            supportsDnD = new Boolean(support);
        }
        return supportsDnD;
    }

    private static File[] createFileArray(BufferedReader bReader, PrintStream out) {
        try {
            ArrayList<File> list = new ArrayList<File>();
            String line = null;
            while ((line = bReader.readLine()) != null) {
                try {
                    if (ZERO_CHAR_STRING.equals(line)) continue;
                    File file = new File(new URI(line));
                    list.add(file);
                }
                catch (Exception ex) {
                    Logger.debug(FileDrop.class, "Error with " + line + ": " + ex.getMessage());
                }
            }
            return list.toArray(new File[list.size()]);
        }
        catch (IOException ex) {
            Logger.debug(FileDrop.class, "FileDrop: IOException");
            return new File[0];
        }
    }

    private void makeDropTarget(PrintStream out, final Component c, boolean recursive) {
        DropTarget dt = new DropTarget();
        try {
            dt.addDropTargetListener(this.dropListener);
        }
        catch (TooManyListenersException e) {
            e.printStackTrace();
            FileDrop.log(out, "FileDrop: Drop will not work due to previous error. Do you have another listener attached?");
        }
        c.addHierarchyListener(new HierarchyListener(){

            public void hierarchyChanged(HierarchyEvent evt) {
                Logger.debug(this.getClass(), "FileDrop: Hierarchy changed.");
                Container parent = c.getParent();
                if (parent == null) {
                    c.setDropTarget(null);
                    Logger.debug(this.getClass(), "FileDrop: Drop target cleared from component.");
                } else {
                    new java.awt.dnd.DropTarget(c, FileDrop.this.dropListener);
                    Logger.debug(this.getClass(), "FileDrop: Drop target added to component.");
                }
            }
        });
        if (c.getParent() != null) {
            new java.awt.dnd.DropTarget(c, this.dropListener);
        }
        if (recursive && c instanceof Container) {
            Container cont = (Container)c;
            Component[] comps = cont.getComponents();
            for (int i = 0; i < comps.length; ++i) {
                this.makeDropTarget(out, comps[i], recursive);
            }
        }
    }

    private boolean isDragOk(PrintStream out, DropTargetDragEvent evt) {
        int i;
        boolean ok = false;
        DataFlavor[] flavors = evt.getCurrentDataFlavors();
        for (i = 0; !ok && i < flavors.length; ++i) {
            DataFlavor curFlavor = flavors[i];
            if (!curFlavor.equals(DataFlavor.javaFileListFlavor) && !curFlavor.isRepresentationClassReader()) continue;
            ok = true;
        }
        if (out != null) {
            if (flavors.length == 0) {
                Logger.debug(this.getClass(), "FileDrop: no data flavors.");
            }
            for (i = 0; i < flavors.length; ++i) {
                Logger.debug(this.getClass(), flavors[i].toString());
            }
        }
        return ok;
    }

    private static void log(PrintStream out, String message) {
        if (out != null) {
            out.println(message);
        }
    }

    public static boolean remove(Component c) {
        return FileDrop.remove(null, c, true);
    }

    public static boolean remove(PrintStream out, Component c, boolean recursive) {
        if (FileDrop.supportsDnD()) {
            Logger.debug(FileDrop.class, "FileDrop: Removing drag-and-drop hooks.");
            c.setDropTarget(null);
            if (recursive && c instanceof Container) {
                Component[] comps = ((Container)c).getComponents();
                for (int i = 0; i < comps.length; ++i) {
                    FileDrop.remove(out, comps[i], recursive);
                }
                return true;
            }
            return false;
        }
        return false;
    }

    static /* synthetic */ void access$1(FileDrop fileDrop, Border border) {
        fileDrop.normalBorder = border;
    }

    public static class Event
    extends EventObject {
        private File[] files;

        public Event(File[] files, Object source) {
            super(source);
            this.files = files;
        }

        public File[] getFiles() {
            return this.files;
        }
    }

    public static class TransferableObject
    implements Transferable {
        public static final String MIME_TYPE = "application/x-net.iharder.dnd.TransferableObject";
        public static final DataFlavor DATA_FLAVOR = new DataFlavor(TransferableObject.class, "application/x-net.iharder.dnd.TransferableObject");
        private Fetcher fetcher;
        private Object data;
        private DataFlavor customFlavor;

        public TransferableObject(Object data) {
            this.data = data;
            this.customFlavor = new DataFlavor(data.getClass(), MIME_TYPE);
        }

        public TransferableObject(Fetcher fetcher) {
            this.fetcher = fetcher;
        }

        public TransferableObject(Class dataClass, Fetcher fetcher) {
            this.fetcher = fetcher;
            this.customFlavor = new DataFlavor(dataClass, MIME_TYPE);
        }

        public DataFlavor getCustomDataFlavor() {
            return this.customFlavor;
        }

        public DataFlavor[] getTransferDataFlavors() {
            if (this.customFlavor != null) {
                return new DataFlavor[]{this.customFlavor, DATA_FLAVOR, DataFlavor.stringFlavor};
            }
            return new DataFlavor[]{DATA_FLAVOR, DataFlavor.stringFlavor};
        }

        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
            if (flavor.equals(DATA_FLAVOR)) {
                return this.fetcher == null ? this.data : this.fetcher.getObject();
            }
            if (flavor.equals(DataFlavor.stringFlavor)) {
                return this.fetcher == null ? this.data.toString() : this.fetcher.getObject().toString();
            }
            throw new UnsupportedFlavorException(flavor);
        }

        public boolean isDataFlavorSupported(DataFlavor flavor) {
            if (flavor.equals(DATA_FLAVOR)) {
                return true;
            }
            return flavor.equals(DataFlavor.stringFlavor);
        }

        public static interface Fetcher {
            public Object getObject();
        }

    }

}

