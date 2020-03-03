/*
 * Decompiled with CFR 0.137.
 */
package de.draco.cbm.tool.crtcreator.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.draco.cbm.tool.crtcreator.Constants;

public class EFMainFrame
extends EasyFrame
implements ActionListener,
ChangeListener,
Constants {
    Vector<ActionListener> m_actionListeners = new Vector();
    JPanel contentPane;
    JMenuBar jMenuBar1 = new JMenuBar();
    JMenu jMenuFile = new JMenu();
    JMenuItem jMenuFileExit = new JMenuItem();
    JMenuItem jMenuFileLoad = new JMenuItem();
    JMenuItem jMenuFileAdd = new JMenuItem();
    JMenuItem jMenuFileSave = new JMenuItem();
    JMenuItem jMenuFileNew = new JMenuItem();
    JMenuItem jMenuFileExport = new JMenuItem();
    JMenu jMenuHelp = new JMenu();
    JMenuItem jMenuHelpAbout = new JMenuItem();
    JToolBar jToolBar = new JToolBar();
    JButton jButton1 = new JButton();
    JButton jButton2 = new JButton();
    JButton jButton3 = new JButton();
    ImageIcon image1;
    ImageIcon image2;
    ImageIcon image3;
    JLabel statusBar = new JLabel();
    BorderLayout borderLayout1 = new BorderLayout();
    MainPanel mainPanel = null;

    public EFMainFrame() {
        this.enableEvents(64L);
        try {
            this.initialize();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initialize() throws Exception {
        this.image1 = new ImageIcon(EFMainFrame.class.getResource("openFile.png"));
        this.image2 = new ImageIcon(EFMainFrame.class.getResource("closeFile.png"));
        this.image3 = new ImageIcon(EFMainFrame.class.getResource("help.png"));
        this.contentPane = (JPanel)this.getContentPane();
        this.contentPane.setLayout(this.borderLayout1);
        this.setSize(new Dimension(800, 600));
        this.setMinimumSize(new Dimension(600, 400));
        this.setContentPane(this.getMainPanel());
        this.statusBar.setText(" ");
        this.jMenuFile.setText("File");
        this.jMenuFileExit.setText("Exit");
        this.jMenuFileExit.addActionListener(this);
        this.jMenuFileLoad.setText("Load");
        this.jMenuFileLoad.addActionListener(this);
        this.jMenuFileNew.setText("New");
        this.jMenuFileNew.addActionListener(this);
        this.jMenuFileSave.setText("Save");
        this.jMenuFileSave.addActionListener(this);
        this.jMenuFileAdd.setText("Add");
        this.jMenuFileAdd.addActionListener(this);
        this.jMenuFileExport.setText("Export");
        this.jMenuFileExport.addActionListener(this);
        this.jMenuHelp.setText("About");
        this.jMenuHelpAbout.setText("About");
        this.jMenuHelpAbout.addActionListener(this);
        this.jButton1.setIcon(this.image1);
        this.jButton1.setToolTipText("Load file");
        this.jButton2.setIcon(this.image2);
        this.jButton2.setToolTipText("Save file");
        this.jButton3.setIcon(this.image3);
        this.jButton3.setToolTipText("Help");
        this.jToolBar.add(this.jButton1);
        this.jToolBar.add(this.jButton2);
        this.jToolBar.add(this.jButton3);
        this.jMenuFile.add(this.jMenuFileLoad);
        this.jMenuFile.add(this.jMenuFileAdd);
        this.jMenuFile.add(this.jMenuFileExport);
        this.jMenuFile.add(this.jMenuFileSave);
        this.jMenuFile.add(this.jMenuFileNew);
        this.jMenuFile.add(this.jMenuFileExit);
        this.jMenuHelp.add(this.jMenuHelpAbout);
        this.jMenuBar1.add(this.jMenuFile);
        this.jMenuBar1.add(this.jMenuHelp);
        this.setJMenuBar(this.jMenuBar1);
        this.contentPane.add((Component)this.jToolBar, "North");
        this.contentPane.add((Component)this.statusBar, "South");
    }

    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == 201) {
            this.actionPerformed(new ActionEvent(this, e.getID(), "Exit"));
        }
    }

    private MainPanel getMainPanel() {
        if (this.mainPanel == null) {
            this.mainPanel = new MainPanel();
            this.mainPanel.addActionListener(this);
        }
        return this.mainPanel;
    }

    public void update(ChangeEvent e) {
        boolean enabled = this.m_model.isChangeable();
        this.jMenuFileAdd.setEnabled(enabled);
        this.jMenuFileSave.setEnabled(enabled);
    }
}

