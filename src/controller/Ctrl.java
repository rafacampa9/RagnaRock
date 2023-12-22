/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;


import com.mongodb.client.MongoCollection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import model.Buffer;
import model.Conexion;
import model.DateNow;
import model.Entrada;
import model.EntradaAux;
import model.Registrar;
import model.Registro;
import model.Salida;
import model.SalidaAux;
import org.bson.Document;
import view.DrawView;
import view.InicioSala;
import view.PicosAforo;
import view.Sala;

/**
 *
 * @author rafacampa9
 */
public class Ctrl extends WindowAdapter implements ActionListener{
    private Buffer buffer;
    private Entrada entrada1;
    private EntradaAux entrada2;
    private Salida salida1;
    private SalidaAux salida2;
    private final InicioSala init;
    private Sala sala;
    private final DrawView paint;
    private DateNow dateNow;
    private int cont;
    private final Timer timer;
    private Conexion conn;
    private Registrar reg;
    private MongoCollection <Document> collection;
    private final PicosAforo picos;

    public Ctrl(Buffer buffer, Entrada entrada1, EntradaAux entrada2, 
            Salida salida1, SalidaAux salida2, InicioSala init, Sala sala, 
            DrawView paint, PicosAforo picos, Conexion conn) {
        this.conn = conn;
        this.sala = sala;
        this.entrada1 = entrada1;
        this.entrada2 = entrada2;
        this.salida1 = salida1;
        this.salida2 = salida2;
        this.init = init;
        this.sala = sala;
        this.picos = picos;
        this.paint = paint;
        
        this.sala.btnQuery.addActionListener(this);
        this.paint.btnQuery.addActionListener(this);
        
        this.init.btnSend.addActionListener(this);
        this.init.txtEntrada1.addActionListener(this);
        this.init.txtEntrada2.addActionListener(this);
        this.init.txtSalida.addActionListener(this);
        
        this.sala.btnUpdate.addActionListener(this);
        this.sala.btnChange.addActionListener(this);
        
        this.paint.btnChange.addActionListener(this);
        this.paint.btnUpdate.addActionListener(this);
        
        this.sala.addWindowListener(this);
        this.paint.addWindowListener(this);
        
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paint.repaint();
            }
        });
        timer.setRepeats(true);
    }
    
    /**
     * 
     * Al cerrar la ventana sala o paint,
     * cerrará la app y la conexión con la
     * base de datos
    */
    @Override
    public void windowClosing(WindowEvent e){
        conn.cerrarConexion();
    }

    
    /**
     * Método que posteriormente utilizaremos
     * en la clase RagnaRock para iniciar la app
     */
    public void iniciar(){
        init.setTitle("Controller");
        init.setSize(404, 339);
        init.setResizable(false);
    }
    
   
    /**
     * 
     * @param dormir_entrada1
     * @param dormir_entrada2
     * @param dormir_salida
     * @param cont
     * @param wait 
     * 
     * Método para indicar o actualizar
     * la frecuencia de entrada y salida 
     * del aforo
     */
    
    public void controlAforo(int dormir_entrada1, int dormir_entrada2, int dormir_salida1, int dormir_salida2, int cont, boolean wait){
        /**
         * Si se abre la ventana init
         * por primera vez
         */
        if (cont == 0){
            conn = new Conexion(collection);
            conn.conectar();
            buffer = new Buffer();
            entrada1 = new Entrada(dormir_entrada1, buffer, sala, paint);
            entrada2 = new EntradaAux(dormir_entrada2, buffer, sala, paint);
            salida1 = new Salida(dormir_salida1, buffer, sala, paint);
            salida2 = new SalidaAux (dormir_salida2, buffer, sala, paint);
            dateNow = new DateNow(buffer, sala, paint);
            reg = new Registrar(buffer, conn);
               
            entrada1.start();
            entrada2.start();
            salida1.start();
            salida2.start();
            dateNow.start();
            reg.start();
            cont = 1;
            
            
         
        /**
         * En este caso, ya se ha abierto
         * previamente, por lo que no debemos
         * instanciar de nuevo los objetos.
         * Esto haría que comenzara un nuevo conteo
         * de aforo desde 0, mezclandose los dos
         * conteos.
         */
        } else {
            entrada1.setWait(wait);
            entrada2.setWait(wait);
            salida1.setWait(wait);
            salida2.setWait(wait);
                
            entrada1.setDormir(dormir_entrada1);
            entrada2.setDormir(dormir_entrada2);
            salida1.setDormir(dormir_salida1);
            salida2.setDormir(dormir_salida2);
  
        }    
    }

    
    /**
     * 
     * @param e
     * 
     * producidos con el escuchador
     * ActionListener, asociados al
     * evento ActionEvent, para que 
     * al pulsar un botón, dependiendo
     * cual sea, haga una cosa u otra
     */
    @Override
    public void actionPerformed(ActionEvent e) {    
        
        int dormirEntrada1, dormirEntrada2, dormirSalida, dormirSalida2;
        /**
         * Si el botón pulsado es ENVIAR de 
         * la ventana init
         */
        if (e.getSource() == init.btnSend){
            if (cont != 0){
                controlAforo(0, 0, 0,0, cont, true);
            }
            if (!init.txtEntrada1.getText().isEmpty() && init.txtEntrada1.getText()!= null
                    && !init.txtEntrada2.getText().isEmpty() && init.txtEntrada2.getText()!= null 
                    && !init.txtSalida.getText().isEmpty() && init.txtSalida.getText()!= null
                    && !init.txtSalida2.getText().isEmpty() && init.txtSalida2.getText()!=null){
                
                try {
                    dormirEntrada1 = (int) Math.round(Double.parseDouble(init.txtEntrada1.getText()));
                    dormirEntrada2 = (int) Math.round(Double.parseDouble(init.txtEntrada2.getText()));
                    dormirSalida = (int) Math.round(Double.parseDouble(init.txtSalida.getText()));
                    dormirSalida2 = (int) Math.round(Double.parseDouble(init.txtSalida2.getText()));
                    
                    
                } catch (NumberFormatException ex) {
                    // Si los valores introducidos no son numéricos
                    Random rand = new Random();
                    dormirEntrada1 = rand.nextInt(9001) + 1000;
                    dormirEntrada2 = rand.nextInt(9001) + 1000;
                    dormirSalida = rand.nextInt(9001) + 1000;
                    dormirSalida2 = rand.nextInt(9001) + 1000;
                }   
                
            } else {
                Random rand = new Random();               
                dormirEntrada1 = rand.nextInt(9001) + 1000;
                dormirEntrada2 = rand.nextInt(9001) + 1000;
                dormirSalida = rand.nextInt(9001) + 1000; 
                dormirSalida2 = rand.nextInt(9001) + 1000;
            }
            
            controlAforo(
                dormirEntrada1,
                dormirEntrada2,
                dormirSalida,
                dormirSalida2,
                cont,
                false
            );

            
            if (cont == 0){               
                initSalaPrincipal();
                init.setVisible(false);
                cont++;
            } 
            
        } 
        
        /**
         * Si el botón pulsado es MODIFICAR
         * de las ventanas sala o paint
         */
        if (e.getSource()==sala.btnUpdate || e.getSource()==paint.btnUpdate){
            init.setLocation(20,50);
            init.setResizable(false);
            init.setVisible(true);
            init.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            
        }
        
        /**
         * Si el botón pulsado es
         * CAMBIAR VISTA de la ventana sala
         */
        if (e.getSource()==sala.btnChange){
            paint.setTitle("RagnaRock");
            paint.setLocationRelativeTo(null);
            paint.setSize(500, 700);
            paint.setResizable(false);
            paint.setVisible(true);
            sala.setVisible(false);
            
            startTimer();
        }
        
        
        /**
         * Si el botón pulsado es
         * CAMBIAR VISTA de la ventana paint
         */
        if (e.getSource()==paint.btnChange){
            stopTimer();
            initSalaPrincipal();
            paint.setVisible(false);
        }
        
        
        /**
         * Si el botón pulsado es 
         * CONSULTAS AFORO de las ventanas
         * sala o paint
         */
        if (e.getSource() == sala.btnQuery || e.getSource()==paint.btnQuery){
            DefaultTableModel tableMax = (DefaultTableModel) picos.tableMax.getModel();
            ArrayList<Registro> picosMax = conn.picosMax();
            
            for (Registro picoMax: picosMax){
                Object [] rowMax = {
                    picoMax.getId(),
                    picoMax.getFecha(),
                    picoMax.getHora()
                };
                
                tableMax.addRow(rowMax);
            }
            
            
            DefaultTableModel tableMin = (DefaultTableModel) picos.tableMin.getModel();
            ArrayList<Registro> picosMin = conn.picosMin();
            
            for (Registro picoMin: picosMin){
                Object [] rowMin = {
                    picoMin.getId(),
                    picoMin.getFecha(),
                    picoMin.getHora()
                };
                tableMin.addRow(rowMin);
            }
            
            picos.setVisible(true);
            picos.setResizable(false);
            picos.setLocationRelativeTo(null);
            picos.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
          
        }
        
    }
    
    /**
     * Arrancar el timer asociado a
     * la ventana paint.
     * Se utiliza para repintar la ventana
     * paint cada segundo, actualizándose 
     * la vista del aforo.
     */
    public void startTimer() {
        if (timer != null && !timer.isRunning())
            timer.start();
    }

    
    /**
     * Pausar el timer asociado
     * a la ventana paint
     */
    public void stopTimer() {
        if (timer != null && timer.isRunning())
            timer.stop();
    }
    
    
    /**
     *  Hacer visible la ventana sala
     */
    public void initSalaPrincipal(){
        sala.setTitle("RagnaRock");
        sala.setLocationRelativeTo(null);
        sala.setResizable(false);
        sala.setVisible(true);
        sala.setSize(480, 435);
    }
    
    
}

