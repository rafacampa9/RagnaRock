/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.text.SimpleDateFormat;
import java.util.Date;





/**
 *
 * @author rafacampa9
 */

public class Buffer {
    private int aforo;
    private String dateNow;
    private boolean entradaBloqueada = false;
    private boolean salidaBloqueada = false;
    private boolean pausa = false;
    
    
    public Buffer() {
    }


    /**
     * 
     * @return
     * 
     * 
     * notificarán a la entrada y salida auxiliar
     * para que sepan si deben permanecer abiertas 
     * o por el contrario, bloquearse
     */
    
    public boolean isPausa(){
        return pausa;
    }
    public void setPausa(boolean pausa) {
        this.pausa = pausa;
    }
    
    
    public synchronized void setEntradaBloqueada(boolean entradaBloqueada){
        this.entradaBloqueada = entradaBloqueada;
    }
    public synchronized boolean isEntradaBloqueada() {
        return entradaBloqueada;
    }
    
    public synchronized void setSalidaBloqueada(boolean salidaBloqueada){
        this.salidaBloqueada = salidaBloqueada;
    }
    public synchronized boolean isSalidaBloqueada(){
        return salidaBloqueada;
    }

    public synchronized void setAforo(int aforo){
        this.aforo = aforo;
    }
    
    
    
    /**
     * 
     * @param value
     * 
     * Nos incrementa el valor de aforo
     * con el valor entero que le pasemos 
     * (En el caso de esta app, le pasaremos
     * como valor 1, es decir, incrementaremos
     * el valor de aforo de 1 en 1). Sin embargo,
     * si el valor de aforo llega a 25, bloquearemos
     * este método hasta que el valor de aforo sea 
     * menor
     */
    public synchronized void put(int value)  {
        while (aforo == 25 || pausa){
            try{
                wait();
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        notify();
        aforo += value;
        //System.out.println("Aforo actual: " + aforo);
        
    }

    
    /**
     * 
     * @param value
     * 
     * Igual que put, pero en este caso restando
     * el valor pasado por parámetro. Si el valor
     * de aforo llega a 0, bloquearemos este 
     * método hasta que el valor de aforo sea mayor
     */
    public synchronized void quit(int value){
        while (aforo == 0 || pausa){
            try{
                wait();
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        notify();
        aforo -= value;
        //System.out.println("Aforo actual: " + aforo);
    }
    
    
    public synchronized String stopExitAux(){
        if(aforo <6){
            try{
                salidaBloqueada = true;
                wait();
                return "Salida 2 cerrada\n";

            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        notify();
        salidaBloqueada = false;
        return "Salida 2 abierta\n";

    }
    
            
    public synchronized String stopEntryAux(){
        if(aforo > 19){
            try{
                entradaBloqueada = true;
                wait();
                return "Entrada 2 cerrada\n";

            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        notify();
        entradaBloqueada = false;
        return "Entrada 2 abierta\n";
       
    }
    /**
     * 
     * @return 
     * 
     * Nos devuelve el valor de aforo
     */
    public synchronized int get(){

        return aforo;
    }
    
    /**
     * 
     * @return 
     * 
     * Nos devuelve la fecha
     * y hora actual
     */
    public synchronized String getDateNow(){
        while(true){
            Date date = new Date(); 
            SimpleDateFormat dateTime = new SimpleDateFormat("dd/MM/yyyy  HH:mm:ss");
            dateNow = dateTime.format(date);
            
            return dateNow;   
        } 
    }
    
    
    /**
     * Pausa la frecuencia de entrada y 
     * salida al modificar los parámetros 
     * y de nuevo los reanuda (los métodos
     * pausar() y reanudar())
     */
    public synchronized void pausar(){
        //while(true){
        try{
                pausa = true;
                wait();
                
            }catch (InterruptedException ex){
                ex.printStackTrace();
            }
        //}
        
        
    }
    
    public synchronized void reanudar(){

        pausa = false;
        notifyAll();
        
    }
   
}