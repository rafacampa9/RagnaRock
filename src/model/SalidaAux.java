/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import com.mongodb.MongoException;
import static java.lang.Thread.sleep;
import org.bson.Document;
import view.DrawView;
import view.Sala;

/**
 *
 * @author rafacampa9
 */
public class SalidaAux extends Thread implements Movimiento{
    
    private int dormir;
    private Buffer buffer;
    private Sala sala;
    private boolean wait, block, free, changedBlock, mov;
    private int cont, contBlock, contFree;
    private DrawView paint;
    private final int  limiteAforo = 6;
    private Conexion conn;

    public SalidaAux(int dormir, Buffer buffer, Sala sala, DrawView paint, Conexion conn) {
        this.dormir = dormir;
        this.buffer = buffer;
        this.sala = sala;
        this.paint = paint;
        this.conn = conn;
    }
    
    public Document addDocument(boolean b){
        if (b)
            return new Document ("Estado Salida 2", buffer.stopExitAux());
        else
            return null;
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
    
    
    public void setWait(boolean wait){
        this.wait=wait;
    }
    
    public boolean getWait(){
        return wait;
    }

    @Override
    public void run() {

        cont = 0; 
        contBlock = 0; 
        contFree = 1;
        free = false;
        block = false;
        
        while (true){

            buffer.stopExitAux();
            //System.out.println("\n\n"+ buffer.get() + buffer.isSalidaBloqueada()+"\n\n");
            while (buffer.get() < limiteAforo && buffer.isSalidaBloqueada()){

                wait = true;
                mov = false;

                cont++;
                buffer.setSalidaBloqueada(false);

                
            } 
            
             if(buffer.get() < limiteAforo){
                contFree = 0;
                free = false;
                setChangedBlock(false);
                if (!block && contBlock == 0){
                    sala.txtArea.setText(bloqueo(buffer.stopExitAux()));
                    paint.txtArea.setText(bloqueo(buffer.stopExitAux()));
                    setChangedBlock(true);
                    realizarBloqueo();

                    try{
                        sleep(dormir);
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                } 
                contBlock++;
                
                wait = true;
                block = true;
                try{
                    sleep(getDormir());
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
            
            
            } else{

                if (cont>0){

                    cont = 0;
                }

                block = false;
                setChangedBlock(false);
                contBlock = 0;  
                if (!free && contFree == 0){
                        /*
                        AQUÍ SE REALIZA LA INSERCIÓN
                        */
                    sala.txtArea.setText(String.valueOf(bloqueo(buffer.stopExitAux())));
                    paint.txtArea.setText(String.valueOf(bloqueo(buffer.stopExitAux())));
                    setChangedBlock(true);
                    realizarBloqueo();

                    
                } 
                free = true;
                
                contFree++;
                wait = false;    
                    
                buffer.quit(1);
                mov = true;
                realizarMovimiento();

                sala.txtAforo.setText(String.valueOf(buffer.get()));
                paint.setAforo(buffer.get());

                sala.txtArea.setText(movimiento());
                paint.txtArea.setText(movimiento());

                try{
                
                    sleep(dormir);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }   
            }
            
            
        }
    }
    
    public String movimiento(){
        return buffer.getDateNow() + ". Un cliente ha abandonado la sala por la Puerta 2.\n" + sala.txtArea.getText();
    }
    
    public String bloqueo(String stopExitResult){
        return buffer.getDateNow() + ". " + stopExitResult + sala.txtArea.getText();
    }
    
    public boolean isChangedBlock(){
        return changedBlock;
    }
    
    public void setChangedBlock(boolean changedBlock){
        this.changedBlock = changedBlock;
    }
    
    @Override
    public void realizarMovimiento() {
        if (this.isMov()) {
            Document documento = new Document("Fecha", buffer.getDateNow().substring(0, 10)).
                    append("Hora", buffer.getDateNow().substring(12)).
                    append("Aforo", buffer.get());

            documento.append("Movimiento", "Un cliente ha abandonado la sala por la Puerta 2");
            //System.out.println("\n\nINSERTADO DOC MOVIMIENTO SALIDA 2\n\n");

            conn.insertarDatos(documento);
        }
    }
    
    public void realizarBloqueo(){
        if (!this.mov){
            Document documento = new Document("Fecha", buffer.getDateNow().substring(0, 10)).
                    append("Hora", buffer.getDateNow().substring(12)).
                    append("Aforo", buffer.get());

            documento.append("Estado Salida 2", buffer.stopExitAux());
            //System.out.println("\n\nINSERTADO DOC MOVIMIENTO SALIDA 2\n\n");

            conn.insertarDatos(documento);
        }
    }
}
