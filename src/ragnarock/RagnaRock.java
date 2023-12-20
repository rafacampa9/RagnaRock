/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ragnarock;

import controller.Ctrl;

import model.Buffer;
import view.DrawView;


import view.InicioSala;
import view.Sala;

/**
 *
 * @author rafacampa9
 */
public class RagnaRock {

    public static void main(String[] args) {
        InicioSala init = new InicioSala();
        Sala sala = new Sala();
        Buffer buff = new Buffer ();
        DrawView paint = new DrawView();
        
        Ctrl ctrl = new Ctrl(buff, null, null, null, init, sala, paint);
        ctrl.iniciar();
        init.setVisible(true);
        
    }
    
}
