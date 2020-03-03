/*
 * Decompiled with CFR 0.137.
 */
package de.draco.cbm.tool.crtcreator.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.draco.cbm.tool.crtcreator.Constants;

public class AboutBox
extends JDialog
implements ActionListener,
Constants {
    JPanel panel1 = new JPanel();
    JPanel insetsPanel1 = new JPanel();
    JPanel insetsPanel2 = new JPanel();
    JButton button1 = new JButton();
    JLabel imageLabel = new JLabel();
    JLabel label1 = new JLabel();
    JLabel label2 = new JLabel();
    JLabel label3 = new JLabel();
    HyperLabel label4 = new HyperLabel();
    ImageIcon image1 = new ImageIcon();
    BorderLayout borderLayout1 = new BorderLayout();
    BorderLayout borderLayout2 = new BorderLayout();
    FlowLayout flowLayout1 = new FlowLayout();
    GridLayout gridLayout1 = new GridLayout();
    String product = "";
    String version = "1.0";
    String copyright = "Copyright (c) 2010";
    String comments = "";
    private HyperLabel label41 = null;
    private JPanel jPanelLogo = null;
    private HyperLabel labelWebsite = null;

    public AboutBox(Frame parent) {
        super(parent);
        this.enableEvents(64L);
        try {
            this.initialize();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    AboutBox() {
        this(null);
    }

    private JPanel getJPanelLogo() {
        if (this.jPanelLogo == null) {
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.gridwidth = 2;
            gridBagConstraints1.gridy = 0;
            gridBagConstraints1.gridx = 0;
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridheight = 1;
            gridBagConstraints.gridy = 1;
            gridBagConstraints.gridx = 0;
            this.jPanelLogo = new JPanel();
            this.jPanelLogo.setLayout(new FlowLayout());
            this.jPanelLogo.setOpaque(false);
            this.jPanelLogo.add((Component)this.imageLabel, null);
        }
        return this.jPanelLogo;
    }

    public static void main(String[] args) {
        AboutBox dlg = new AboutBox();
        Dimension dlgSize = dlg.getPreferredSize();
        dlg.setModal(true);
        dlg.pack();
        dlg.show();
    }

    private void initialize() throws Exception {
        GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
        gridBagConstraints8.gridx = 2;
        gridBagConstraints8.gridy = 1;
        GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
        gridBagConstraints7.gridx = 2;
        gridBagConstraints7.gridy = 13;
        this.labelWebsite = new HyperLabel();
        this.labelWebsite.setText("http://www.sascha-bader.de/html/dcm.html");
        GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
        gridBagConstraints6.gridx = 2;
        gridBagConstraints6.gridy = 0;
        GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
        gridBagConstraints5.gridx = 2;
        gridBagConstraints5.gridy = 11;
        gridBagConstraints5.anchor = 10;
        gridBagConstraints5.gridwidth = 4;
        GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
        gridBagConstraints4.gridx = 2;
        gridBagConstraints4.gridy = 14;
        gridBagConstraints4.gridwidth = 4;
        GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 1;
        gridBagConstraints3.gridy = 2;
        gridBagConstraints3.weightx = 0.1;
        gridBagConstraints3.weighty = 0.1;
        gridBagConstraints3.gridwidth = 2;
        GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        gridBagConstraints2.gridy = 10;
        gridBagConstraints2.gridx = 2;
        this.label1.setText("DCM 1.0.1");
        this.setSize(new Dimension(450, 200));
        this.image1 = new ImageIcon(EFMainFrame.class.getResource("EasyFlashLogoSmallTransparent.gif"));
        this.imageLabel.setIcon(this.image1);
        this.setTitle("Info");
        this.panel1.setLayout(this.borderLayout1);
        this.insetsPanel1.setLayout(this.flowLayout1);
        this.insetsPanel1.setOpaque(false);
        this.insetsPanel2.setLayout(new GridBagLayout());
        this.insetsPanel2.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.panel1.setBackground(Color.lightGray);
        this.insetsPanel2.setOpaque(false);
        this.gridLayout1.setRows(4);
        this.gridLayout1.setColumns(1);
        this.label2.setText("");
        this.label2.setPreferredSize(new Dimension(0, 20));
        this.label3.setText("(c) 2010 by Sascha Bader");
        this.label4.setText("http://www.skoe.de/easyflash");
        this.label41 = new HyperLabel();
        this.label41.setText("http://www.youtube.com/watch?v=wNf9rEPoc8Q");
        this.button1.setText("OK");
        this.insetsPanel2.add((Component)this.label2, gridBagConstraints2);
        this.insetsPanel2.add((Component)this.label3, gridBagConstraints3);
        this.insetsPanel2.add((Component)this.label4, gridBagConstraints4);
        this.insetsPanel2.add((Component)this.label41, gridBagConstraints5);
        this.insetsPanel2.add((Component)this.getJPanelLogo(), gridBagConstraints6);
        this.insetsPanel2.add((Component)this.labelWebsite, gridBagConstraints7);
        this.insetsPanel2.add((Component)this.label1, gridBagConstraints8);
        this.button1.addActionListener(this);
        this.getContentPane().add((Component)this.panel1, null);
        this.insetsPanel1.add((Component)this.button1, null);
        this.panel1.add((Component)this.insetsPanel1, "South");
        this.panel1.add((Component)this.insetsPanel2, "Center");
        this.setResizable(false);
    }

    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == 201) {
            this.cancel();
        }
        super.processWindowEvent(e);
    }

    void cancel() {
        this.dispose();
    }

    public void paint(Graphics g) {
        this.paintGradient(g);
        super.paint(g);
    }

    public void paintGradient(Graphics i_g) {
        int w = this.getSize().width;
        int h = this.getSize().height;
        Point bottomRight = new Point(w, h);
        Point upperLeft = new Point(0, 0);
        GradientPaint gradientPaint = new GradientPaint(upperLeft, Color.darkGray, bottomRight, Color.lightGray);
        Graphics2D g2d = (Graphics2D)i_g;
        g2d.setPaint(gradientPaint);
        g2d.fillRect(0, 0, w - 1, h - 1);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.button1) {
            this.cancel();
        }
    }
}

