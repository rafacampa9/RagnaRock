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
public class Entrada extends Thread{
    private int dormir;
    private Buffer buffer;
    private Sala sala;
    private DrawView paint;
    private boolean wait;
    private int cont;
            


    public Entrada(int dormir, Buffer buffer, Sala sala, DrawView paint) {
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
    
    public boolean getWait(){
        return wait;
    }
    
    public void setWait(boolean wait){
        this.wait = wait;
    }
    

    @Override
    public void run() {
        wait = false;
        cont = 0;
        
        while (true){
            
            if (wait){
               buffer.pausar(); 
               cont = 1;
            } else{
                if (cont == 1){
                    buffer.reanudar();
                }
                buffer.put(1);
                try{
                    sleep(dormir);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sala.txtAforo.setText(String.valueOf(buffer.get()));
                paint.setAforo(buffer.get());
                
                sala.txtArea.setText("Ha entrado un cliente por la Entrada 1.\n" + sala.txtArea.getText());
                paint.txtArea.setText("Ha entrado un cliente por la Entrada 1.\n" + paint.txtArea.getText());
            }
           
        }

    }
}

