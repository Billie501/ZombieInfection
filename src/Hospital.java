/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author billi
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
//import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;


public class Hospital extends JLayeredPane {
    
    private final int width = 450;
    private final int height = 450;
    private ImageIcon hospitalImage;
    
    public Hospital() {
        // Set layout to null to manually manage component placement
        setLayout(null);
        setBounds(0, 0, width, height);
        setOpaque(false);

        // Load hospital image
        hospitalImage = loadImage("Images/hospital.png");
        
        JLabel backgroundImageLabel = new JLabel(hospitalImage);
        backgroundImageLabel.setBounds(0, 0, width, height);
        add(backgroundImageLabel, JLayeredPane.DEFAULT_LAYER); // Add image to the center of JLayeredPane
        
        JLabel label = new JLabel("Hospital");
        label.setForeground(Color.black);
        label.setBackground(Color.lightGray);
        label.setOpaque(false);
        label.setPreferredSize(new Dimension(this.getWidth(), 20));
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setBounds(0, 0, this.getWidth(), 20); // Manually set the position and size

        add(label, JLayeredPane.PALETTE_LAYER); // Add label to a different layer
        setVisible(true);
    }
    
    private ImageIcon loadImage(String path) {
        try {
            return new ImageIcon(getClass().getClassLoader().getResource(path));
        } catch (Exception e) {
            System.err.println("Error loading image: " + path);
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        hospitalImage.paintIcon(this, g, 0, 0); // Paint hospital image on top of the label
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }
    
    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
}