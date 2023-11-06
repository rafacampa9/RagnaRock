/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;





/**
 *
 * @author rafacampa9
 */

public class Buffer {
    private int aforo;





    public synchronized void put(int value)  {
        while (aforo == 10){
            try {
                wait();
            } catch (InterruptedException e){
                e.printStackTrace();
            }

        }
        notify();
        aforo += value;
        System.out.println("Aforo actual: " + aforo);

    }

    public synchronized void quit(int value){
        while (aforo == 0){
            try{
                wait();
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        notify();
        aforo -= value;
        System.out.println("Aforo actual: " + aforo);

    }

    public synchronized int get(){

        return aforo;
    }



}