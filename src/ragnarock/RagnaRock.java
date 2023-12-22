/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ragnarock;

import controller.Ctrl;

import model.Buffer;
import model.Conexion;
import view.DrawView;


import view.InicioSala;
import view.PicosAforo;
import view.Sala;

/**
 *
 * @author rafacampa9
 */
public class RagnaRock {

    public static void main(String[] args) {
        InicioSala init = new InicioSala();
        init.setLocationRelativeTo(null);
        Sala sala = new Sala();
        PicosAforo picos = new PicosAforo();
        Buffer buff = new Buffer ();
        DrawView paint = new DrawView();
        Conexion conn = new Conexion();
        
        Ctrl ctrl = new Ctrl(buff, null, null, null,
                null, init, sala,
                paint, picos, conn);
        ctrl.iniciar();
        init.setVisible(true);
        
    }
    
}
