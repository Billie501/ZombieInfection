/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author billi
 */

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class ThreadCount extends JPanel {
    
    private final int width = 160;
    private final int height = 100;
    
    ZombieInfectionPanel panel;
    JLabel healthy;
    JLabel infected;
    JLabel hospitalised;
    JLabel line;
    JLabel total;
    
    public ThreadCount (ZombieInfectionPanel panel) {
        
        this.panel = panel;
        setLayout(null);
        setBorder(BorderFactory.createLineBorder(Color.black, 1));
        setOpaque(false);
        healthy = new JLabel();
        infected = new JLabel();
        hospitalised = new JLabel();
        line = new JLabel();
        total = new JLabel();

        healthy.setBounds(10, 10, 150, 20);
        infected.setBounds(10, 30, 150, 20);
        hospitalised.setBounds(10, 50, 150, 20);
        line.setBounds(10, 60, 150, 20);
        total.setBounds(10, 70, 150, 20);

        healthy.setForeground(Color.GREEN);
        infected.setForeground(Color.RED);
        hospitalised.setForeground(Color.YELLOW);

        this.add(healthy);
        this.add(infected);
        this.add(hospitalised);
        this.add(line);
        this.add(total);

        healthy.setText("Healthy Child:  0");
        infected.setText("Infected by Zombies:  0");
        hospitalised.setText("In the Hospital:  0");
        line.setText("--------------------------------");
        total.setText("Total Active Thread: 0");

        setVisible(true);
    }
    
    public void updateThreadCount(int a,int b, int c) {
        healthy.setText("Healthy Child: " + a);
        infected.setText("Infected Children: " + b);
        hospitalised.setText("In the Hospital: " + c);
        total.setText("Total Active Thread: " + panel.getHumanThreadSize());
    }
    
    @Override
    public int getWidth() {
        return width;
    }

    /**
     * @return the height
     */
    @Override
    public int getHeight() {
        return height;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(211, 211, 211, 100));
        g.fillRect(0, 0, getWidth(), getHeight());
    }
    
    
}
