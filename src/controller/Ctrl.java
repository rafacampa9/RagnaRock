/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;


import com.mongodb.client.MongoCollection;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import model.Buffer;
import model.Conexion;
import model.DateNow;
import model.Entrada;
import model.EntradaAux;

import model.Registro;
import model.Salida;
import model.SalidaAux;
import org.bson.Document;
import view.Bloqueos;
import view.DrawView;
import view.InicioSala;
import view.Movimientos;
import view.PanelConsultas;
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
//    private Registrar reg;
    private MongoCollection <Document> collection;
    private final PicosAforo picos;
    private final Bloqueos block;
    private final Movimientos mov;
    private final PanelConsultas panel;
    private final ButtonGroup grupo;
    
    
    
    public Ctrl(Buffer buffer, Entrada entrada1, EntradaAux entrada2, 
            Salida salida1, SalidaAux salida2, InicioSala init, Sala sala, 
            DrawView paint,  PanelConsultas panel,PicosAforo picos, 
            Bloqueos block, Movimientos mov, Conexion conn) {
        this.conn = conn;
        this.sala = sala;
        this.entrada1 = entrada1;
        this.entrada2 = entrada2;
        this.salida1 = salida1;
        this.salida2 = salida2;
        this.init = init;
        this.sala = sala;
        this.picos = picos;
        this.panel = panel;
        this.paint = paint;
        this.block = block;
        this.mov = mov;
        
        this.sala.btnQuery.addActionListener(this);
        this.paint.btnQuery.addActionListener(this);
        
        this.sala.addWindowListener(this);
        this.paint.addWindowListener(this);
        
        this.init.btnSend.addActionListener(this);
        this.init.txtEntrada1.addActionListener(this);
        this.init.txtEntrada2.addActionListener(this);
        this.init.txtSalida.addActionListener(this);
        
        this.sala.btnUpdate.addActionListener(this);
        this.sala.btnChange.addActionListener(this);
        this.sala.btnStop.addActionListener(this);
        this.sala.btnRestart.addActionListener(this);
        this.sala.btnPause.addActionListener(this);
        
        
        this.paint.btnChange.addActionListener(this);
        this.paint.btnUpdate.addActionListener(this);
        this.paint.btnStop.addActionListener(this);
        this.paint.btnRestart.addActionListener(this);
        this.paint.btnPause.addActionListener(this);
        
        
        this.picos.btnBack.addActionListener(this);
        this.mov.btnBack.addActionListener(this);
        this.block.btnBack.addActionListener(this);
        
        grupo = new ButtonGroup();
        grupo.add(panel.rbBlock);
        grupo.add(panel.rbMov);
        grupo.add(panel.rbMinMax);
        this.panel.rbBlock.addActionListener(this);
        this.panel.rbMinMax.addActionListener(this);
        this.panel.rbMov.addActionListener(this);
        
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
     * @param e
    */
    @Override
    public void windowClosing(WindowEvent e){
        /**
         * Para que no salte Exception al cerrar
         * el programa con el foco en una de las
         * ventanas que nos está devolviendo
         * una consulta de la base de datos
         */
        
        
        if (picos.getFocusableWindowState()){
            
            try{
                Thread.sleep(50);
            } catch (InterruptedException ex){
                ex.printStackTrace();
            }
            picos.dispose();
        
        }else if (mov.getFocusableWindowState()){
            
            try{
                Thread.sleep(50);
            }catch (InterruptedException ex){
                ex.printStackTrace();
            }
            
            mov.dispose();
        }
            
        else if (block.getFocusableWindowState()){
            
            try{
                Thread.sleep(50);
                
            } catch (InterruptedException ex){
                ex.printStackTrace();
            }
            block.dispose();
        }
        try{
            Thread.sleep(100);
        } catch (InterruptedException ex){
            ex.printStackTrace();
        }
            
        
        
        conn.cerrarConexion();
        
        
    }
        
            
    
    /**
     * Método que posteriormente utilizaremos
     * en la clase RagnaRock para iniciar la app
     */
    public void iniciar(){  
        init.setTitle("Inicio");
        init.setLocationRelativeTo(null);
        init.setResizable(false);
        init.setVisible(true);
    }
    
   
    /**
     * 
     * @param dormir_entrada1
     * @param dormir_entrada2
     * @param dormir_salida1
     * @param dormir_salida2
     * @param cont
     * @param wait 
     * 
     * Método para indicar o actualizar
     * la frecuencia de entrada y salida 
     * del aforo
     */
    
    public void controlAforo(int dormir_entrada1, int dormir_entrada2,
            int dormir_salida1, int dormir_salida2, int cont, 
            boolean wait){
        /**
         * Si se abre la ventana init
         * por primera vez
         */
        if (cont == 0){
            conn = new Conexion(collection);
            conn.conectar();
            buffer = new Buffer();
            entrada1 = new Entrada(dormir_entrada1, buffer, sala, paint, conn);
            entrada2 = new EntradaAux(dormir_entrada2, buffer, sala, paint, conn);
            salida1 = new Salida(dormir_salida1, buffer, sala, paint, conn);
            salida2 = new SalidaAux (dormir_salida2, buffer, sala, paint, conn);
            dateNow = new DateNow(buffer, sala, paint);
            //reg = new Registrar(buffer, conn, entrada1, salida1, entrada2, salida2);
               
            entrada1.start();
            entrada2.start();
            salida1.start();
            salida2.start();
            dateNow.start();
            //reg.start();
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
        sala.setSize(485, 485);
        
        ImageIcon pause = new ImageIcon (System.getProperty("user.dir") + "/img/pause.png");
        sala.btnPause.setIcon(pause);
        sala.btnPause.setBackground (Color.white);
        sala.btnPause.setBorder(new BevelBorder(BevelBorder.RAISED));
        
        ImageIcon stop = new ImageIcon (System.getProperty("user.dir") + "/img/stop.png");
        sala.btnStop.setIcon(stop);
        sala.btnStop.setBackground (Color.white);
        sala.btnStop.setBorder(new BevelBorder(BevelBorder.RAISED));
        
        ImageIcon play = new ImageIcon (System.getProperty("user.dir") + "/img/play.png");
        sala.btnRestart.setIcon(play);
        sala.btnRestart.setBackground (Color.white);
        sala.btnRestart.setBorder(new BevelBorder(BevelBorder.RAISED));

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
        JTable tableMov = mov.tbMov, tableBlock = block.tbBlock, 
                tableMax = picos.tableMax, 
                tableMin = picos.tableMin;
        DefaultTableModel tableModelBlock = (DefaultTableModel) tableBlock.getModel();
        DefaultTableModel tableModelMov = (DefaultTableModel) tableMov.getModel();
        DefaultTableModel tableModelMax = (DefaultTableModel) tableMax.getModel();
        DefaultTableModel tableModelMin = (DefaultTableModel) tableMin.getModel();
   
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
         * Pausamos el buffer e iniciamos a cero
         */
        if (e.getSource() == sala.btnStop || 
                e.getSource() == paint.btnStop){
            buffer.setPausa(true);
            buffer.setAforo(0);
            try{
                Thread.sleep(50);
            } catch (InterruptedException ex){
                ex.printStackTrace();
            }
            conn.cerrarConexion();
            try{
                Thread.sleep(50);
            } catch (InterruptedException ex){
                ex.printStackTrace();
            }
            conn.conectar();
        }       
        /**
         * Reanudamos la interación
         */
        if (e.getSource() == sala.btnRestart ||
                e.getSource() == paint.btnRestart){
            conn.conectar();
            try{
                Thread.sleep(50);
            } catch (InterruptedException ex){
                ex.printStackTrace();
            }
            buffer.reanudar();
        }
        
        /**
         * Pausamos la interacción
         */
        if(e.getSource() == sala.btnPause ||
                e.getSource() == paint.btnPause){
            buffer.setPausa(true);

        }
        
        /**
         * Si el botón pulsado es MODIFICAR
         * de las ventanas sala o paint
         */
        if (e.getSource()==sala.btnUpdate || e.getSource()==paint.btnUpdate){
            init.setLocation(20,50);
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
            paint.setSize(495, 730);
            paint.setResizable(false);
            paint.setVisible(true);
            
            ImageIcon pause = new ImageIcon(System.getProperty("user.dir")+ "/img/pause.png");
            paint.btnPause.setIcon(pause);
            paint.btnPause.setBackground (Color.white);
            paint.btnPause.setBorder(new BevelBorder(BevelBorder.RAISED));
            
            ImageIcon stop = new ImageIcon(System.getProperty("user.dir") + "/img/stop.png");
            paint.btnStop.setIcon(stop);
            paint.btnStop.setBackground (Color.white);
            paint.btnStop.setBorder(new BevelBorder(BevelBorder.RAISED));
            
            ImageIcon play = new ImageIcon (System.getProperty("user.dir") + "/img/play.png");
            paint.btnRestart.setIcon(play);
            paint.btnRestart.setBackground (Color.white);
            paint.btnRestart.setBorder(new BevelBorder(BevelBorder.RAISED));
            
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
        if (e.getSource() == sala.btnQuery || 
                e.getSource()==paint.btnQuery){
            panel.setLocation(700,300);
            panel.setVisible(true);
            panel.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        }
        
        /**
         * Si pulsamos el RadioButton que
         * abre dentro de las posibles consultas
         * la ventana de los PICOS DE AFORO
         */
        if (e.getSource()==panel.rbMinMax){
            tableModelMax.setRowCount(0);
            tableModelMin.setRowCount(0);
            TableColumn columnMax, columnMin;
            
            
            TableColumnModel column  = tableMax.getColumnModel();
            columnMax= column.getColumn(0);
            columnMax.setPreferredWidth(190);
            
            
            ArrayList<Registro> picosMax = conn.picosMax();
            
            for (Registro picoMax: picosMax){
                Object [] rowMax = {
                    picoMax.getId(),
                    picoMax.getFecha(),
                    picoMax.getHora()
                };
                
                tableModelMax.addRow(rowMax);
            }
            
            
            column = tableMin.getColumnModel();
            columnMin = column.getColumn(0);
            columnMin.setPreferredWidth(190);
            ArrayList<Registro> picosMin = conn.picosMin();
            
            for (Registro picoMin: picosMin){
                Object [] rowMin = {
                    picoMin.getId(),
                    picoMin.getFecha(),
                    picoMin.getHora()
                };
                tableModelMin.addRow(rowMin);
            }
            
            picos.setVisible(true);
            picos.setResizable(false);
            picos.setLocationRelativeTo(panel);
            picos.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            panel.setVisible(false);
          
        }
        
        /**
         * Si pulsamos el RadioButton que
         * abre dentro de las posibles consultas
         * la ventana de los MOVIMIENTOS
         */
        if (e.getSource()==panel.rbMov){
            tableModelMov.setRowCount(0);
            TableColumn columnMov;
            
            
            TableColumnModel column  = tableMov.getColumnModel();
            columnMov = column.getColumn(0);
            columnMov.setPreferredWidth(190);
            columnMov = column.getColumn(3);
            columnMov.setPreferredWidth(50);
            columnMov = column.getColumn(4);
            columnMov.setPreferredWidth(350);
            
            ArrayList <Registro> movimientos = conn.entryExit();

            
            for (Registro movimiento: movimientos){
                Object [] rowMov = {
                    movimiento.getId(),
                    movimiento.getFecha(),
                    movimiento.getHora(),
                    movimiento.getAforo(),
                    movimiento.getMovimiento()
                };
                
                tableModelMov.addRow(rowMov);
            }
            
            mov.setTitle("Movimientos de aforo");
            mov.setSize(1154, 486);
            mov.setLocationRelativeTo(panel);
            mov.setVisible(true);
            mov.setResizable(false);            
            mov.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            panel.setVisible(false);
        }
       
        /**
         * Si pulsamos el RadioButton que
         * abre dentro de las posibles consultas
         * la ventana de los BLOQUEOS DE PUERTAS
         */
        if (e.getSource() == panel.rbBlock){
            tableModelBlock.setRowCount(0);
            TableColumn columnBlock;
            
            
            TableColumnModel column  = tableBlock.getColumnModel();
            columnBlock = column.getColumn(0);
            columnBlock.setPreferredWidth(190);
            columnBlock = column.getColumn(3);
            columnBlock.setPreferredWidth(60);
            columnBlock = column.getColumn(4);
            columnBlock.setPreferredWidth(200);
            

            ArrayList <Registro> bloqueos = conn.block();
            
            for (Registro bloqueo: bloqueos){
                Object [] rowBlock = {
                    bloqueo.getId(),
                    bloqueo.getFecha(),
                    bloqueo.getHora(),
                    bloqueo.getAforo(),
                    bloqueo.getEstado()
                };
                
                tableModelBlock.addRow(rowBlock);
                
            }
            block.setTitle("Bloqueos de Entrada/Salida");
            block.setSize(761,418);
            block.setLocationRelativeTo(panel);
            block.setVisible(true);
            block.setResizable(false);
            block.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            panel.setVisible(false);
        }
        
        /**
         *Si pulsamos el botón ATRAS de cualquiera de 
         * las pantallas de consultas
         */
        if (e.getSource()== block.btnBack){
            block.setVisible(false);
            panel.setVisible(true);
        }      
        if (e.getSource() == mov.btnBack){
            mov.setVisible(false);
            panel.setVisible(true);
        }        
        if (e.getSource() == picos.btnBack){
            picos.setVisible(false);
            panel.setVisible(true);
        }   
    } 
}

