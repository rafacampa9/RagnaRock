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
    private Entrada entrada;
    private Salida salida;
    private EntradaAux entradaAux;
    private SalidaAux salidaAux;
    
    public Registrar(Buffer buffer, Conexion conn, Entrada entrada, 
            Salida salida, EntradaAux entradaAux, SalidaAux salidaAux){
        this.buffer = buffer;
        this.conn = conn;
        this.entrada = entrada;
        this.salida = salida;
        this.entradaAux = entradaAux;
        this.salidaAux = salidaAux;
    }
    
    @Override
    public void run(){
        while(true){
            
            /**
             * Para registrar los movimientos
             */
            registrar(entrada);
            registrar(entradaAux);
            registrar(salida);
            registrar(salidaAux);
            
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
    public void registrar(Object objeto){
        documento = new Document("Fecha", buffer.getDateNow().substring(0, 10)).
                append("Hora", buffer.getDateNow().substring(12)).
                append("Aforo",buffer.get());
        if (objeto instanceof Entrada){
            System.out.println("Entrada1");
            documento.append("Movimiento", entrada.movimiento().substring(22, 60));
        }
        else if (objeto instanceof Salida ){
            System.out.println("Salida1");
            documento.append("Movimiento", salida.movimiento().substring(22, 70));
            
        }else if (objeto instanceof EntradaAux){
            System.out.println("EntradaAux");
            EntradaAux enter = (EntradaAux) objeto;
            if (enter.isChangedBlock()){
                documento.append("Estado Entrada 2", entradaAux.bloqueo().substring(22, 39));
            } else {
                documento.append("Movimiento", entradaAux.movimiento().substring(22,60));
            }
        }
        else if (objeto instanceof SalidaAux){
            System.out.println("SalidaAux");
            SalidaAux exit = (SalidaAux) objeto;
            if (exit.isChangedBlock())
                documento.append("Estado Salida 2", salidaAux.bloqueo().substring(22,38));
            else
                documento.append("Movimiento", salidaAux.movimiento().substring(22, 70));
        }      
        conn.insertarDatos(documento);
     
    }
    
    
}
