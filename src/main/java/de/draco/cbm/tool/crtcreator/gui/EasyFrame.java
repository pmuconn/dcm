/*
 * Decompiled with CFR 0.137.
 */
package de.draco.cbm.tool.crtcreator.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.draco.cbm.tool.crtcreator.Constants;

public class EasyFrame
extends JFrame
implements ActionListener,
ChangeListener,
Constants {
    Vector<ActionListener> m_actionListeners = new Vector();
    JPanel contentPane;
    BorderLayout borderLayout1 = new BorderLayout();
    boolean m_eventsEnabled = true;
    EasyModel m_model;
    EasyGui m_controller;

    public EasyFrame() {
        this.enableEvents(64L);
        try {
            this.initialize();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        this.m_controller = EasyGui.getInstance();
        this.m_model = this.m_controller.getModel();
        this.m_model.addChangeListener(this);
        this.addActionListener(this.m_controller);
    }

    private void initialize() throws Exception {
        this.contentPane = (JPanel)this.getContentPane();
        this.contentPane.setLayout(this.borderLayout1);
        this.setSize(new Dimension(800, 600));
        this.setMinimumSize(new Dimension(600, 400));
        this.setTitle("DCM 1.0.1");
        Image image = new ImageIcon(EFMainFrame.class.getResource("EasyFlashIcon.gif")).getImage();
        this.setIconImage(image);
    }

    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        e.getID();
    }

    public void actionPerformed(ActionEvent e) {
        for (ActionListener l : this.m_actionListeners) {
            l.actionPerformed(e);
        }
    }

    public void addActionListener(ActionListener l) {
        if (!this.m_actionListeners.contains(l)) {
            this.m_actionListeners.add(l);
        }
    }

    public void removeActionListener(ActionListener l) {
        this.m_actionListeners.remove(l);
    }

    public final void stateChanged(ChangeEvent e) {
        this.m_eventsEnabled = false;
        this.update(e);
        this.m_eventsEnabled = true;
    }

    public void update(ChangeEvent e) {
        boolean enabled = this.m_model.isChangeable();
    }
}

