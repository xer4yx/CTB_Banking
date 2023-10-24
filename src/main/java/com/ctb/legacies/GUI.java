package com.ctb.legacies;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

public class GUI {
    GUI() {
        JFrame mainFrame = new JFrame();
        JLabel mainLabel = new JLabel();
        ImageIcon iconLogo = new ImageIcon("res/ctb_logo.png");

        mainLabel.setText("Central Trust Bank");
        mainLabel.setHorizontalTextPosition(JLabel.CENTER);
        mainLabel.setVerticalTextPosition(JLabel.TOP);
        mainLabel.setForeground(new Color(0xFFFFFF));
        mainLabel.setFont(new Font("Young Serif", Font.PLAIN, 24));

        mainLabel.setIcon(iconLogo);
        mainLabel.setHorizontalAlignment(JLabel.CENTER);
//        mainLabel.setVerticalAlignment(JLabel.TOP);

        mainFrame.setTitle("CTB Banking");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1280, 720);
        mainFrame.setIconImage(iconLogo.getImage());
        mainFrame.getContentPane().setBackground(new Color(0xFFDAB8));

        mainFrame.setVisible(true);
        mainFrame.add(mainLabel);
    }
}
