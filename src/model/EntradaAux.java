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
public class EntradaAux extends Thread implements Movimiento{
    private int dormir;
    private Buffer buffer;
    private Sala sala;
    private DrawView paint;
    private boolean wait, block, free, changedBlock, mov;
    private int cont, contBlock, contFree;
    private final int limiteAforo = 19;
    private Conexion conn;
            


    public EntradaAux(int dormir, Buffer buffer, Sala sala, DrawView paint, Conexion conn) {
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
   
        cont = 0; 
        mov = false;
        contBlock = 1; 
        contFree = 0;
        free = false;
        block = false;
        
        while (true){
            //wait = false;
            buffer.stopEntryAux();

            //System.out.println("\n\n"+ buffer.get() + buffer.isEntradaBloqueada()+"\n\n");
            while (buffer.get() > limiteAforo && buffer.isEntradaBloqueada()){

                //wait = true;
                mov = false; 
                //System.out.println("\n\nENTRADA BLOQUEADA\n\n");
                cont++;
                buffer.setEntradaBloqueada(false);
                //System.out.println("\nDESBLOQUEAMOS ENTRADA Y SUMAMOS CONTADOR\n\n");
                
            } 
            
             if(buffer.get() > limiteAforo){
                //System.out.println("\n\nAFORO MAYOR QUE 19\n\n");
                
                
                free = false;
                setChangedBlock(false);
                if (!block && contBlock == 0){
                    sala.txtArea.setText(bloqueo(buffer.stopEntryAux()));
                    paint.txtArea.setText(bloqueo(buffer.stopEntryAux()));
                    setChangedBlock(true);
                    try{
                        realizarBloqueo();
                    }catch(MongoException e){
                        e.printStackTrace();
                    }
                    //System.out.println("\n\nMAYOR QUE 19 Y CONTBLOCK = 0\n\n");
                    try{
                        sleep(dormir);
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                } 
                contBlock++;
                 //System.out.println("\n\ncontBlock:"+contBlock+"\n\n");
                contFree = 0;
                block = true;
                try{
                    //System.out.println("\n\nSE DUERME\n\n");
                    
                    sleep(getDormir());
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
            
            
            } else{

                //System.out.println("\n\nSALIDA ABIERTA\n\n" + cont);
                
                if (cont>0){

                    //System.out.println("\nCONTADOR MAYOR QUE 0. IGUALAMOS A 0\n");
                    cont = 0;
                }
                //System.out.println("\n\nREANUDA\n\n");
                block = false;
                setChangedBlock(false);
                    
                if (!free && contFree == 0){
                        /*
                        AQUÍ SE REALIZA LA INSERCIÓN
                        */
                    sala.txtArea.setText(String.valueOf(bloqueo(buffer.stopEntryAux())));
                    paint.txtArea.setText(String.valueOf(bloqueo(buffer.stopEntryAux())));
                    setChangedBlock(true);
                    try{
                        realizarBloqueo();
                    }catch (MongoException e){
                        e.printStackTrace();
                    }
                    
                    //System.out.println("\n\nINSERTAMOS CAMBIO BLOQUEO\n\n");
                    
                } 
                free = true;
                contBlock = 0;
                contFree++;
                    
                    
                buffer.put(1);
                
                realizarMovimiento();
                
                mov = true;
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
        return buffer.getDateNow() + ". Ha entrado un cliente por la Puerta 2.\n" + sala.txtArea.getText();
    }
    
    public String bloqueo(String stopEntryResult){
        return buffer.getDateNow() + ". " + stopEntryResult + sala.txtArea.getText();
    }

    public boolean isChangedBlock() {
        return changedBlock;
    }

    public void setChangedBlock(boolean changedBlock) {
        this.changedBlock = changedBlock;
    }

    @Override
    public void realizarMovimiento() {
        if (this.isMov()) {
            Document documento = new Document("Fecha", buffer.getDateNow().substring(0, 10)).
                    append("Hora", buffer.getDateNow().substring(12)).
                    append("Aforo", buffer.get());

            documento.append("Movimiento", "Ha entrado un cliente por la Puerta 2");
            //System.out.println("\n\nINSERTADO DOC MOVIMIENTO ENTRADA 2\n\n");

            conn.insertarDatos(documento);
        }
    }
    
    public void realizarBloqueo(){
        if (!this.mov){
            Document documento = new Document("Fecha", buffer.getDateNow().substring(0, 10)).
                    append("Hora", buffer.getDateNow().substring(12)).
                    append("Aforo", buffer.get());

            documento.append("Estado Entrada 2", buffer.stopEntryAux());
            //System.out.println("\n\nINSERTADO DOC MOVIMIENTO ENTRADA 2\n\n");

            conn.insertarDatos(documento);
        }
    }
    
    
    
    
    
    
}
