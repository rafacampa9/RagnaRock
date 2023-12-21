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
public class DateNow extends Thread{
    private Buffer buffer;
    private Sala sala;
    private DrawView paint;
    
    
    public DateNow (Buffer buffer, Sala sala, DrawView paint){
      this.buffer = buffer;
      this.sala = sala;
      this.paint = paint;
        
    }
    
    @Override
    public void run(){
        while(true){           
            sala.txtDateTime.setText(buffer.getDateNow());
            paint.txtDateTime.setText(buffer.getDateNow());
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
