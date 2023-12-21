/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.Timer;
import model.Buffer;
import model.DateNow;
import model.Entrada;
import model.Salida;
import view.DrawView;
import view.InicioSala;
import view.Sala;

/**
 *
 * @author rafacampa9
 */
public class Ctrl implements ActionListener{
    private Buffer buffer;
    private Entrada entrada1, entrada2;
    private Salida salida;
    private InicioSala init;
    private Sala sala;
    private DrawView paint;
    private DateNow dateNow;
    private int cont;
    private Timer timer;

    public Ctrl(Buffer buffer, Entrada entrada1, Entrada entrada2, 
            Salida salida, InicioSala init, Sala sala, DrawView paint) {
        this.sala = sala;
        this.entrada1 = entrada1;
        this.entrada2 = entrada2;
        this.salida = salida;
        this.init = init;
        this.sala = sala;
        this.paint = paint;
        
        
        this.init.btnSend.addActionListener(this);
        this.init.txtEntrada1.addActionListener(this);
        this.init.txtEntrada2.addActionListener(this);
        this.init.txtSalida.addActionListener(this);
        
        this.sala.btnUpdate.addActionListener(this);
        this.sala.btnChange.addActionListener(this);
        
        this.paint.btnChange.addActionListener(this);
        this.paint.btnUpdate.addActionListener(this);
        
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paint.repaint();
            }
        });
        timer.setRepeats(true);
    }
    
    
    public void iniciar(){
        init.setTitle("Controller");
        init.setLocationRelativeTo(null);
        init.setResizable(false);
    }
    
    public void instancia(int dormir_entrada1, int dormir_entrada2, int dormir_salida){

        buffer = new Buffer();
        entrada1 = new Entrada(dormir_entrada1, buffer, sala, paint);
        entrada2 = new Entrada(dormir_entrada2, buffer, sala, paint);
        salida = new Salida(dormir_salida, buffer, sala, paint);
        dateNow = new DateNow(buffer, sala, paint);
    }

    
    
    
    public void controlAforo(int dormir_entrada1, int dormir_entrada2, int dormir_salida, int cont, boolean wait){

            if (cont == 0){
                instancia(dormir_entrada1, dormir_entrada2, dormir_salida);
                entrada1.start();
                entrada2.start();
                salida.start();
                dateNow.start();
                cont = 1;
                
            } else {
                entrada1.setWait(wait);
                entrada2.setWait(wait);
                salida.setWait(wait);
                
                entrada1.setDormir(dormir_entrada1);
                entrada2.setDormir(dormir_entrada2);
                salida.setDormir(dormir_salida);
    
            }
    
        
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        
        
        int dormirEntrada1, dormirEntrada2, dormirSalida;
        if (e.getSource() == init.btnSend){
            if (cont != 0){
                controlAforo(0, 0, 0, cont, true);
            }
            if (!init.txtEntrada1.getText().isEmpty() && init.txtEntrada1.getText()!= null
                    && !init.txtEntrada2.getText().isEmpty() && init.txtEntrada2.getText()!= null 
                    && !init.txtSalida.getText().isEmpty() && init.txtSalida.getText()!= null){
                
                try {
                    dormirEntrada1 = (int) Math.round(Double.parseDouble(init.txtEntrada1.getText()));
                    dormirEntrada2 = (int) Math.round(Double.parseDouble(init.txtEntrada2.getText()));
                    dormirSalida = (int) Math.round(Double.parseDouble(init.txtSalida.getText()));
                } catch (NumberFormatException ex) {
           
                    Random rand = new Random();
                    dormirEntrada1 = rand.nextInt(9001) + 1000;
                    dormirEntrada2 = rand.nextInt(9001) + 1000;
                    dormirSalida = rand.nextInt(9001) + 1000;
                }   
            } else {
                Random rand = new Random();               
                dormirEntrada1 = rand.nextInt(9001) + 1000;
                dormirEntrada2 = rand.nextInt(9001) + 1000;
                dormirSalida = rand.nextInt(9001) + 1000;   
            }
            
            controlAforo(
                dormirEntrada1,
                dormirEntrada2,
                dormirSalida,
                cont,
                false
            );

            
            if (cont == 0){
                
                initSalaPrincipal();
                init.setVisible(false);
                cont++;
            } 
            
        } 
        
        if (e.getSource()==sala.btnUpdate || e.getSource()==paint.btnUpdate){
            init.setLocation(20,50);
            init.setResizable(false);
            init.setVisible(true);
            init.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            
        }
        
        if (e.getSource()==sala.btnChange){
            paint.setTitle("Vista aforo");
            paint.setLocationRelativeTo(null);
            paint.setSize(500, 400);
            paint.setResizable(false);
            paint.setVisible(true);
            sala.setVisible(false);
            
            startTimer();
  
        }
        
        if (e.getSource()==paint.btnChange){
            stopTimer();
            initSalaPrincipal();
            paint.setVisible(false);
        }
        
    }
    
    public void startTimer() {
        if (timer != null && !timer.isRunning()) {
            timer.start();
        }
    }

    public void stopTimer() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
    }
    
    public void initSalaPrincipal(){
        sala.setTitle("RagnaRock");
            sala.setLocationRelativeTo(null);
            sala.setResizable(false);
            sala.setVisible(true);
            sala.setSize(600, 300);
    }
}

