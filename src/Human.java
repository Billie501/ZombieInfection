/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author billi
 */
//import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.Timer;


public class Human extends JLabel implements Runnable {
    
    int x;
    int y;
    int vx;
    int vy;
    int delay = 10;
    int width;
    int height;
    boolean infected = false;
    boolean hospitalised = false;
    
    
    
    boolean healthy = true;
    int life = 500;
    int time = 4;
    Timer hospitalTimer;
    Timer lifeCount;
    ImageIcon icon;
    ImageIcon schoolGirl;
    ImageIcon healthyGirl;
    ImageIcon zombie;
    ImageIcon sickGirl;
    
    private Hospital hospital;
    private ZombieInfectionPanel panel;
    private Thread thread;
    private volatile boolean running = true;
    private final Lock lock = new ReentrantLock();
    
    public Human(Hospital hospital, ZombieInfectionPanel panel) {
        this.panel = panel;
        this.hospital = hospital;
        assignVXandVYrandomValues();

        Timer velocityTimer = new Timer(3000, (ActionEvent e) -> {
            assignVXandVYrandomValues();
        });
        velocityTimer.start();

        // Load images as resources
        schoolGirl = loadImage("Images/schoolgirl.png");
        healthyGirl = loadImage("Images/healthygirl.png");
        zombie = loadImage("Images/zombie.png");
        sickGirl = loadImage("Images/sickGirl.png");
        icon = healthyGirl;

        setIcon(icon);
        setSize(icon.getIconWidth(), icon.getIconHeight());

        // Initialize and start the thread
        thread = new Thread(this);
        thread.start();
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
    
     public void assignVXandVYrandomValues() {
        do {
            vx = (int) (Math.random() * 5) - 2;
            vy = (int) (Math.random() * 5) - 2;
        } while (vx == 0 || vy == 0);
    }

    public void setRange(int width, int height) {
        this.width = width;
        this.height = height;
    }
    
        
    @Override
    public void run() {
        while (running) {

            if (!this.hospitalised) {

                x += vx;
                y += vy;

                // Check if the human hits the panel boundaries
                if (x < 0 || x + getWidth() > panel.getWidth()) {
                    vx = -vx; // Reverse horizontal velocity
                }
                if (y < 0 || y + getHeight() > panel.getHeight()) {
                   vy = -vy; // Reverse vertical velocity
                }

                //-----------------------------------------------
                if (x + hospital.getWidth() >= hospital.getX() && x <= hospital.getX() + hospital.getWidth()
                      && y + hospital.getHeight() >= hospital.getY() && y <= hospital.getY() + hospital.getHeight()) {
                   if (x + hospital.getWidth() >= hospital.getX() || x <= hospital.getX() + hospital.getWidth()) {
                        vx = -vx;
                    }
                   if (y + 100 >= hospital.getY() || y <= hospital.getY() + hospital.getHeight()) {
                        vy = -vy;
                    }
                }
              setLocation(x, y);
                //----------------------------------------------
                try {
                 Thread.sleep(delay);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Human.class.getName()).log(Level.SEVERE, null, ex);
                    Thread.currentThread().interrupt();
               }
            }
        }
   }
    
    public void randomizeStartPosition(int Panelwidth, int Panelheight) {
        // randomize the initial position of the phone
        x = (int) (Math.random() * (Panelwidth - this.getWidth())); //new x;
        y = (int) (Math.random() * (Panelheight - this.getHeight()));//new y;
        if (x < hospital.getWidth()) {
            x = (int) (x / 4 + hospital.getWidth());
        }

    }
    
    public int distanceBetweenHumans(Human humanThread) {
        // Calculate distance from each corner of 'this' human thread to 'humanThread'
        int[] cornerDeltasX = {0, getWidth(), 0, getWidth()};
        int[] cornerDeltasY = {0, 0, getHeight(), getHeight()};

        int minDistance = Integer.MAX_VALUE;

        for (int i = 0; i < 4; i++) {
            int deltaX = this.getX() + cornerDeltasX[i] - humanThread.getX();
            int deltaY = this.getY() + cornerDeltasY[i] - humanThread.getY();
            int distance = (int) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
            minDistance = Math.min(minDistance, distance);
        }

        return minDistance;

    }
    
    public void setIconImg(ImageIcon color) {
        icon = color;
        setIcon(icon);
        repaint();
    }

    public void setInfected(Boolean infected) {
        this.healthy = false;
        this.hospitalised = false;
        this.infected = infected;
        setIcon(zombie);
        startLifeTimer();
        repaint();
    }
    
    public void setHospitalise(Boolean hospitalise) {
        lock.lock();
        try {
            this.infected = false;
            this.healthy = false;
            this.hospitalised = true;
            panel.hospitalisedProcessActive = true;
            
             // Create a JLabel for the sickGirl image
            JLabel label1 = new JLabel();
            label1.setOpaque(true);
            label1.setIcon(sickGirl);

            // Calculate the center position within the hospital panel
           int xCenter = (hospital.getWidth() - sickGirl.getIconWidth()) / 2;
            int yCenter = (hospital.getHeight() - sickGirl.getIconHeight()) / 2;
            label1.setBounds(xCenter, yCenter, sickGirl.getIconWidth(), sickGirl.getIconHeight());

            // Add the label1 to the hospital JLayeredPane
            hospital.add(label1, JLayeredPane.PALETTE_LAYER);
                                              
            if (lifeCount != null) {
                lifeCount.stop();
            }
            this.life = 500;
            hospital.add(this);
            startRepairTimer();
            repaint();
            
                        
        } finally {
            lock.unlock();
        }
    }
    
   

    
    private void startDelayBeforeRepair() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.schedule(() -> {
            if(panel.infected>0){
            panel.goToHospital(); // Start the repair timer after the delay
            }else {
                panel.hospitalisedProcessActive = false;
            }
        }, 2, TimeUnit.SECONDS); // 2-second delay
        executor.shutdown(); // Shutdown the executor after the task is completed
    }

    public void setHealthy(Boolean healthy) {
        this.hospitalised = false;
        this.infected = false;
        this.healthy = healthy;
        setIcon(schoolGirl);
        repaint();
        
        // Remove the image of the sick girl from the hospital's JLayeredPane
        for (Component component : hospital.getComponents()) {
            if (component instanceof JLabel) {
                JLabel label = (JLabel) component;
                if (label.getIcon() == sickGirl) {
                    hospital.remove(label);
                    break;
                }
            }
        }
        hospital.revalidate();
        hospital.repaint();

        repaint();
    
    }

    public Boolean isInfected() {
        return this.infected;
    }
    
    public Boolean isHospitalised() {
        return this.hospitalised;
    }
    
    public Boolean isHealthy() {
        return this.healthy;
    }
    
    public void startRepairTimer() {
        hospitalTimer = new Timer(1000, (ActionEvent e) -> {
            repairTimeDown();
        });
        hospitalTimer.start();
    }

    public void repairTimeDown() {
        if (time != 0) {
            time--;
        } else {
            this.setHealthy(true);
            lock.lock();
            try {
                panel.hospitalised--;
            } finally {
                lock.unlock();
            }
            randomizeStartPosition(panel.getWidth(), panel.getHeight());
            panel.add(this);
            hospitalTimer.stop();
            startDelayBeforeRepair();
        }
    }

    public void startLifeTimer() {
        lifeCount = new Timer(20, (ActionEvent e) -> {
            lifeDown();
        });
        lifeCount.start();
    }
    
    

    public void lifeDown() {
        if (this.infected) {
            life--;
            if (life <= 0) {
                Container parent = this.getParent();
                stopThread();
                if (parent != null) {
                    parent.remove(this);
                    parent.revalidate();
                    parent.repaint();
                    panel.removeHuman(this);
                }
            }
        }
    }

    private void stopThread() {
        running = false;
        try {
            thread.join(); // Wait for the thread to finish
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    

    private void goToHospital() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
