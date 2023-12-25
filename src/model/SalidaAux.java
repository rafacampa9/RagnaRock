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
public class SalidaAux extends Thread{
    
    private int dormir;
    private Buffer buffer;
    private Sala sala;
    private boolean wait, block, free, changedBlock;
    private int cont, contBlock, contFree;
    private DrawView paint;

    public SalidaAux(int dormir, Buffer buffer, Sala sala, DrawView paint) {
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
        contBlock = 0; 
        contFree = 1;
        free = false;
        block = false;
        
        while (true){
            sala.txtArea.setText(buffer.getDateNow() + ". " + buffer.stopExitAux() + sala.txtArea.getText());
            paint.txtArea.setText(buffer.getDateNow() + ". " + buffer.stopExitAux() + paint.txtArea.getText());
            
            
            if (wait){
                buffer.pausar();
                cont=1;
                
            } else{
                
                if (cont == 1){
                    buffer.reanudar();
                }
                
                // Si la salida no está bloqueada
                if (!buffer.isSalidaBloqueada()){
                    block = false;
                    setChangedBlock(false);
                    
                    if (!free && contFree == 0){
                        sala.txtArea.setText(String.valueOf(bloqueo()));
                        paint.txtArea.setText(String.valueOf(bloqueo()));
                        setChangedBlock(true);
                    } 
                    free = true;
                    contBlock = 0;
                    contFree++;
                    
                    
                    buffer.quit(1);
                    sala.txtAforo.setText(String.valueOf(buffer.get()));
                    paint.setAforo(buffer.get());

                    sala.txtArea.setText(movimiento());
                    paint.txtArea.setText(movimiento());
                    try{
                        sleep(dormir);
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    
                // Si la salida está bloqueada, esperaremos un segundo para volver a comprobarlo    
                } else {
                    free = false;
                    setChangedBlock(false);
                    if (!block && contBlock == 0){
                        sala.txtArea.setText(bloqueo());
                        paint.txtArea.setText(bloqueo());
                        setChangedBlock(true);
                    } 
                    contBlock++;
                    contFree = 0;
                    block = true;
                    
                    
                }   
                

                
            }
            

        }
    }
    
    public String movimiento(){
        return buffer.getDateNow() + ". Un cliente ha abandonado la sala por la Salida 2.\n" + sala.txtArea.getText();
    }
    
    public String bloqueo(){
        return buffer.getDateNow() + ". " + buffer.stopExitAux() + sala.txtArea.getText();
    }
    
    public boolean isChangedBlock(){
        return changedBlock;
    }
    
    public void setChangedBlock(boolean changedBlock){
        this.changedBlock = changedBlock;
    }
}
