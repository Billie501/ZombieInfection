

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author billi
 */
import java.awt.BorderLayout;
import javax.swing.JFrame;
//import zombie.infection.ZombieInfectionPanel;

public class ZombieInfectionFrame extends JFrame {
    
    public ZombieInfectionFrame() {
        
        ZombieInfectionPanel panel = new ZombieInfectionPanel();
        
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        //getContentPane().add(panel);
        getContentPane().add(panel, BorderLayout.CENTER);
        setVisible(true);
        
        
    }
    
       
    
}
