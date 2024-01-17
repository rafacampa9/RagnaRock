/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import com.mongodb.MongoException;
import org.bson.Document;
import view.DrawView;
import view.Sala;

/**
 *
 * @author rafacampa9
 */
public class Entrada extends Thread implements Movimiento{
    private int dormir;
    private Buffer buffer;
    private Sala sala;
    private DrawView paint;
    private boolean wait, mov;
    private Conexion conn;     
    private int cont;
    
            


    public Entrada(int dormir, Buffer buffer, Sala sala, DrawView paint, Conexion conn) {
        this.dormir = dormir;
        this.buffer = buffer;
        this.sala = sala;
        this.paint = paint;
        this.conn = conn;
       
    }

    
    /**
     * 
     * @return
     * 
     * Los métodos get y set de a continuación
     * serán utilizados para modificar los parámetros
     * de frecuencia de salida
     */
    public boolean isMov(){
        return mov;
    }
    
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
        mov = false;
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
                mov = true;
                try{
                    realizarMovimiento();
                }catch(MongoException e){
                    e.printStackTrace();
                }
                try{
                    sleep(dormir);
                    mov = false;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sala.txtAforo.setText(String.valueOf(buffer.get()));
                paint.setAforo(buffer.get());
                
                sala.txtArea.setText(movimiento());
                paint.txtArea.setText(movimiento());
            }
            
    }
    
            } 
            
    public String movimiento(){
        return buffer.getDateNow() + ". Ha entrado un cliente por la Puerta 1.\n" + sala.txtArea.getText();
    }
    
    @Override
    public void realizarMovimiento() {
        if (this.isMov()) {
            Document documento = new Document("Fecha", buffer.getDateNow().substring(0, 10)).
                    append("Hora", buffer.getDateNow().substring(12)).
                    append("Aforo", buffer.get());

            documento.append("Movimiento", "Ha entrado un cliente por la Puerta 1");
            //System.out.println("\n\nINSERTADO DOC MOVIMIENTO ENTRADA 1\n\n");

            conn.insertarDatos(documento);
        }
    }
    
            
        }
    
