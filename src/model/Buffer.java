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

    public Buffer() {
    }

    
    public synchronized void put(int value)  {
        while (aforo == 10){
            try {
                wait();
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        notify();
        aforo += value;
        System.out.println("Aforo actual: " + aforo);
        
    }

    public synchronized void quit(int value){
        while (aforo == 0){
            try{
                wait();
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        notify();
        aforo -= value;
        System.out.println("Aforo actual: " + aforo);
    }

    public synchronized int get(){

        return aforo;
    }
    
    public synchronized String getDateNow(){
        while(true){
            Date date = new Date(); 
            SimpleDateFormat dateTime = new SimpleDateFormat("dd/MM/yyyy  HH:mm:ss");
            dateNow = dateTime.format(date);
            
            return dateNow;
            
           
        }
  
    }
    
    public synchronized void pausar(){
        while(true){
            try{
                wait();
            }catch (InterruptedException ex){
                ex.printStackTrace();
            }
        }
    }
    
    public synchronized void reanudar(){
        while(true){
            notifyAll();
        }
    }


}