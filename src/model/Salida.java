/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import view.DrawView;
import view.Sala;

/**
 *
 * @author rafacampa9
 */

public class Salida extends Thread{
    private int dormir;
    private Buffer buffer;
    private Sala sala;
    private boolean wait;
    private int cont;
    private DrawView paint;

    public Salida(int dormir, Buffer buffer, Sala sala, DrawView paint) {
        this.dormir = dormir;
        this.buffer = buffer;
        this.sala = sala;
        this.paint = paint;
    }
    
    
    
    /**
     * 
     * @return
     * 
     * Los métodos get y set de a continuación
     * serán utilizados para modificar los parámetros
     * de frecuencia de salida
     */
    
    
    public int getDormir() {
        return dormir;
    }

    public void setDormir(int dormir) {
        this.dormir = dormir;
    }
    
    
    public void setWait(boolean wait){
        this.wait=wait;
    }
    
    public boolean getWait(){
        return wait;
    }

    @Override
    public void run() {
        wait = false;
        cont = 0;
        
        while (true){
            if (wait){
                buffer.pausar();
                cont=1;
                
            } else{
                if (cont == 1){
                    buffer.reanudar();
                }
                buffer.quit(1);
                try{
                    sleep(dormir);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }

                sala.txtAforo.setText(String.valueOf(buffer.get()));
                paint.setAforo(buffer.get());
                
                sala.txtArea.setText(buffer.getDateNow() + ". Un cliente ha abandonado la sala por la Salida 1.\n" + sala.txtArea.getText());
                paint.txtArea.setText(buffer.getDateNow() + ". Un cliente ha abandonado la sala por la Salida 1.\n" + paint.txtArea.getText());
            }
            

        }
    }
}

