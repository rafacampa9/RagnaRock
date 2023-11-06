/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ragnarock;

import controller.Ctrl;

import model.Buffer;


import view.InicioSala;
import view.Sala;

/**
 *
 * @author rafacampa9
 */
public class RagnaRock {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        InicioSala init = new InicioSala();
        Sala sala = new Sala();
        Buffer buff = new Buffer ();
        
        Ctrl ctrl = new Ctrl(buff, null, null, null, init, sala);
        ctrl.iniciar();
        init.setVisible(true);
        
        
        
        /*int dormir1, dormir2, dormir3;
        InicioSala init = new InicioSala();
        Scanner teclado = new Scanner(System.in);

        System.out.println("Introduzca el valor del tiempo transcurrido en el que la gente quiere entrar en la entrada 1: ");
        dormir1 = teclado.nextInt();

        System.out.println("Introduzca el valor del tiempo transcurrido en el que la gente quiere entrar en la entrada 2: ");
        dormir2 = teclado.nextInt();

        System.out.println("Introduzca el valor del tiempo transcurrido en el que la gente quiere salir: ");
        dormir3 = teclado.nextInt();

        ctrl.controlAforo(dormir1, dormir2, dormir3);

       */
    }
    
}
