/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */


/**
 *
 * @author billi
 */

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import javax.swing.JPanel;
import javax.swing.Timer;

public class ZombieInfectionPanel extends JPanel implements KeyListener, ComponentListener {
    private LinkedList<Human> humanThreadWrapper; 
    Hospital hospital;
    ThreadCount count;
    private final int infectionRange = 30;
    int infected = 0;
    int healthy = 0;
    int hospitalised = 0;
    boolean hospitalisedProcessActive = false;
    
    public ZombieInfectionPanel() {
        count = new ThreadCount(this);
        hospital = new Hospital();
        humanThreadWrapper = new LinkedList<>(); // will store the HumanThread
        setLayout(null);
        this.setFocusable(true);
        this.addKeyListener(this);
        this.addComponentListener(this);
        this.add(hospital); // No need to specify constraints for null layout
        
        Timer timer = new Timer(10, (ActionEvent e) -> {
            human_virus_transmission();
            humanStateCount();
            repaint();
        });
        timer.start();
    }
    
    public int getHumanThreadSize() {
        return humanThreadWrapper.size();
    }

    private void add_human() {
        // create new human object
        Human human = new Human(hospital, this);
        human.setRange(this.getWidth(), this.getHeight());
        human.randomizeStartPosition(this.getWidth(), this.getHeight());

        humanThreadWrapper.add(human);
        this.add(human);
        System.out.println("Human added: "+humanThreadWrapper.size());
    }
    
    public void removeHuman(Human human) {
        humanThreadWrapper.remove(human);
        System.out.println("Human removed: " + humanThreadWrapper.size());
    }

    //this is for the initial virus
    public void virus_human() {
        if (this.infected == 0) {
            int index = (int) (Math.random() * humanThreadWrapper.size());
            Human A = humanThreadWrapper.get(index);
            A.setInfected(true);
        }
    }
    
    public void goToHospital() {
    synchronized (hospital) {
        // If the hospital is already occupied, release the current patient
        if (this.hospitalised > 0) {
            for (Human human : humanThreadWrapper) {
                if (human.isHospitalised()) {
                    human.setHealthy(true);
                    human.randomizeStartPosition(this.getWidth(), this.getHeight());
                    this.add(human);
                    break;
                }
            }
        }

        // Find the first infected human and hospitalize them
        for (Human human : humanThreadWrapper) {
            if (human.isInfected() && !human.isHospitalised()) {
                human.setHospitalise(true);
                break;
            }
        }
    }
}
        
    
    public void humanStateCount() {
        this.healthy = 0;
        this.infected = 0;
        this.hospitalised = 0;

        for (Human p : humanThreadWrapper) {
            if (p.healthy) {
                this.healthy++;
            }
            if (p.infected) {
                this.infected++;
            }
            if (p.hospitalised) {
                this.hospitalised++;
            }
        }
        if (this.infected > 2 && !this.hospitalisedProcessActive) {
            goToHospital();
        }
        count.updateThreadCount(this.healthy, this.infected, this.hospitalised);
    }
    
      
    public void human_virus_transmission() {
    for (Human A : humanThreadWrapper) {
        if (A.isInfected()) {
            for (Human B : humanThreadWrapper) {
                if (A != B && !B.isInfected()) { // Skip if already infected
                    int distance = A.distanceBetweenHumans(B);
                    if (distance <= infectionRange && B.isHealthy()) {
                        B.setInfected(true);
                    }
                }
            }
        }
    }
}
    
    

    @Override
    public void keyTyped(KeyEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            add_human();
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            
            virus_human();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (count != null) {
            Dimension panelSize = this.getSize();
            count.setBounds(panelSize.width - count.getWidth(),
                    0, count.getWidth(), count.getHeight());
            this.add(count);
        }
    }

    @Override
    public void componentResized(ComponentEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void componentShown(ComponentEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void componentHidden(ComponentEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
