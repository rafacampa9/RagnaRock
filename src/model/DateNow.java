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
public class DateNow extends Thread{
    private Buffer buffer;
    private Sala sala;
    
    
    public DateNow (Buffer buffer, Sala sala){
      this.buffer = buffer;
      this.sala = sala;
        
    }
    
    @Override
    public void run(){
        while(true){
            
            sala.txtDateTime.setText(buffer.getDateNow());
            try
            {
                sleep(1000);
            } catch (InterruptedException ex)
            {
               ex.printStackTrace();
            }
        }
    }
}
