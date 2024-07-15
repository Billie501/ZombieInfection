
import javax.swing.SwingUtilities;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author billi
 */


public class VirusSimulation {
    
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new ZombieInfectionFrame();
        });
        
    }
    
}
