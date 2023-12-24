/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import static java.lang.Thread.sleep;
import view.DrawView;
import view.Sala;

/**
 *
 * @author rafacampa9
 */
public class EntradaAux extends Thread{
    private int dormir;
    private Buffer buffer;
    private Sala sala;
    private DrawView paint;
    private boolean wait, block, free, changedBlock;
    private int cont;
            


    public EntradaAux(int dormir, Buffer buffer, Sala sala, DrawView paint) {
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
        block = false;
        free = false;
        changedBlock = false;
        while (true){
            sala.txtArea.setText(buffer.getDateNow() + ". " + buffer.stopEntryAux() + sala.txtArea.getText());
            paint.txtArea.setText(buffer.getDateNow() + ". " + buffer.stopEntryAux() + paint.txtArea.getText());
             
            if (wait){
               buffer.pausar();
               
               cont = 1;
            } else{
                if (cont == 1){
                    buffer.reanudar();
                }
                
                /**
                 * Si la entrada no está bloqueada
                 */
                if (!buffer.isEntradaBloqueada()){
                    block = false;
                    if (!free){
                        sala.txtArea.setText(String.valueOf(bloqueo()));
                        paint.txtArea.setText(String.valueOf(bloqueo()));
                        setChangedBlock(true);
                               
                    } else{
                        setChangedBlock(false);
                    }
                    free = true;
                    
                    buffer.put(1);
                    sala.txtAforo.setText(String.valueOf(buffer.get()));
                    paint.setAforo(buffer.get());

                    sala.txtArea.setText(movimiento());
                    paint.txtArea.setText(movimiento());
                    try{
                        sleep(dormir);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    
                /**
                 * Si la entrada está bloqueada, 
                 * esperaremos un segundo para
                 * volver a comprobarlo
                 */
                } else {
                    free = false;
                    if (!block){
                        sala.txtArea.setText(bloqueo());   
                        paint.txtArea.setText(bloqueo());
                        setChangedBlock(true);
                    } else{
                        setChangedBlock(false);
                    }
                    block=true;
                    
                    /*
                    try{   
                        sleep(1000);
                    } catch (InterruptedException e){
                        e.printStackTrace();*/
                }
                
            }
           
        }

    }
    
    public String movimiento(){
        return buffer.getDateNow() + ". Ha entrado un cliente por la Entrada 2.\n" + sala.txtArea.getText();
    }
    
    public String bloqueo(){
        return buffer.getDateNow() + ". " + buffer.stopEntryAux() + sala.txtArea.getText();
    }

    public boolean isChangedBlock() {
        return changedBlock;
    }

    public void setChangedBlock(boolean changedBlock) {
        this.changedBlock = changedBlock;
    }
    
    
    
    
    
    
}
