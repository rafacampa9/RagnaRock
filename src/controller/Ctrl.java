/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import model.Buffer;
import model.Entrada;
import model.Salida;
import view.InicioSala;
import view.Sala;

/**
 *
 * @author rafacampa9
 */
public class Ctrl implements ActionListener{
    private Buffer sala;
    private Entrada entrada1, entrada2;
    private Salida salida;
    private InicioSala init;
    private Sala salaView;

    public Ctrl(Buffer sala, Entrada entrada1, Entrada entrada2, 
            Salida salida, InicioSala init, Sala salaView) {
        this.sala = sala;
        this.entrada1 = entrada1;
        this.entrada2 = entrada2;
        this.salida = salida;
        this.init = init;
        this.salaView = salaView;
        
        this.init.btnSend.addActionListener(this);
        this.init.txtEntrada1.addActionListener(this);
        this.init.txtEntrada2.addActionListener(this);
        this.init.txtSalida.addActionListener(this);
    }
    
    
    public void iniciar(){
        init.setTitle("Inicio");
        init.setLocationRelativeTo(null);
    }


    public void controlAforo(int dormir_entrada1, int dormir_entrada2, int dormir_salida){
        sala = new Buffer();
        entrada1 = new Entrada(dormir_entrada1, sala, salaView);
        entrada2 = new Entrada(dormir_entrada2, sala, salaView);
        salida = new Salida(dormir_salida, sala, salaView);

        entrada1.start();
        entrada2.start();
        salida.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int dormirEntrada1, dormirEntrada2, dormirSalida;
        if (e.getSource() == init.btnSend){
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
                dormirSalida
            );
            
            salaView.setTitle("RagnaRock");
            salaView.setLocationRelativeTo(null);
            salaView.setVisible(true);
            init.setVisible(false);
        }
    }
}

