/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import org.bson.Document;

/**
 *
 * @author rafacampa9
 */
public class Registrar extends Thread{
    private Document documento;
    private Buffer buffer;
    private Conexion conn;
    
    public Registrar(Buffer buffer, Conexion conn){
        this.buffer = buffer;
        this.conn = conn;
    }
    
    @Override
    public void run(){
        while(true){
            
            /**
             * Si el aforo llega a 25, registramos
             * que se ha llenado el aforo
             */
            if (buffer.get() == 25){
                registrar(true);   
            }
            
            /**
             * Si el aforo baja a 0, registramos
             * que se ha vaciado el aforo
             */
            if (buffer.get() == 0){
                registrar(false);   
            }
            
            /**
             * Para no repetir registros,
             * ya que nos interesa la fecha
             * y la hora, y cada segundo,
             * el valor de la hora cambia
             */
            try{
                sleep(1000);
            } catch (InterruptedException e){
               e.printStackTrace();
            }
            
            /**
             * Si usamos las líneas de abajo, y 
             * comentamos las que hay justo encima
             * de este comentario y por debajo de
             * la sentencia
             * while(true)
             * eliminaremos todos los registros
             * de la colección de nuestra base de datos
             */
            //conn.eliminarPicos("Mínimo aforo", true);
            //conn.eliminarPicos("Máximo aforo", true);
        }
    }
    /**
     * 
     * @param minMax 
     * 
     * Si minMax = true, nos registrará un pico máximo
     * Si minMax = false, nos registrará un pico mínimo
     */
    public void registrar(Boolean minMax){
        if (minMax){
            documento = new Document("Fecha", buffer.getDateNow().substring(0, 10)).
                append("Hora", buffer.getDateNow().substring(12)).
                append("Máximo aforo", true);
            conn.insertarDatos(documento);
           
        }       
        else{
            documento = new Document("Fecha", buffer.getDateNow().substring(0, 10)).
                append("Hora", buffer.getDateNow().substring(12)).
                append("Mínimo aforo", true);
            conn.insertarDatos(documento);
        }
    }
    
    
}
