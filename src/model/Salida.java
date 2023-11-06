/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import view.Sala;

/**
 *
 * @author rafacampa9
 */

public class Salida extends Thread{
    private int dormir;
    private Buffer sala;
    private Sala salaView;

    public Salida(int dormir, Buffer sala, Sala salaView) {
        this.dormir = dormir;
        this.sala = sala;
        this.salaView = salaView;
    }

    @Override
    public void run() {
        while (true){
            sala.quit(1);
            try{
                sleep(dormir);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
            
            salaView.txtAforo.setText(String.valueOf(sala.get()));
            //sala.get();
        }
    }
}

