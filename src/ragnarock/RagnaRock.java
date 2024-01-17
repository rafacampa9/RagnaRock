/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ragnarock;

import controller.Ctrl;

import model.Buffer;
import model.Conexion;
import view.Bloqueos;
import view.DrawView;


import view.InicioSala;
import view.Movimientos;
import view.PanelConsultas;
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
        Conexion conn = new Conexion();      
        Sala sala = new Sala(conn);
        PanelConsultas panel = new PanelConsultas();
        PicosAforo picos = new PicosAforo();
        Bloqueos block = new Bloqueos();
        Movimientos mov = new Movimientos();        
        Buffer buff = new Buffer ();
        DrawView paint = new DrawView(conn);
        Ctrl ctrl = new Ctrl(buff, null, null, null,
                null, init, sala,
                paint, panel, picos, 
                block, mov, conn);
        ctrl.iniciar();       
    }  
}
