/*
 * Decompiled with CFR 0.137.
 */
package de.draco.cbm.tool.crtcreator.gui;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class EasyPanel
extends JPanel
implements ChangeListener,
ActionListener {
    private static final long serialVersionUID = 1L;
    Vector<ActionListener> m_actionListeners = new Vector();
    EasyModel m_model;
    EasyGui m_controller;
    boolean m_eventsEnabled = true;

    public EasyPanel() {
        this.initialize();
        this.m_controller = EasyGui.getInstance();
        this.m_model = this.m_controller.getModel();
        this.m_model.addChangeListener(this);
        this.m_controller.register(this);
    }

    private void initialize() {
        this.setSize(300, 200);
        this.setLayout(new GridBagLayout());
    }

    public final void stateChanged(ChangeEvent e) {
        this.m_eventsEnabled = false;
        this.update(e);
        this.m_eventsEnabled = true;
    }

    public void update(ChangeEvent e) {
    }

    public void addActionListener(ActionListener l) {
        if (!this.m_actionListeners.contains(l)) {
            this.m_actionListeners.add(l);
        }
    }

    public void removeActionListener(ActionListener l) {
        this.m_actionListeners.remove(l);
    }

    public void actionPerformed(ActionEvent e) {
        if (this.m_eventsEnabled) {
            for (ActionListener l : this.m_actionListeners) {
                l.actionPerformed(e);
            }
        }
    }
}

