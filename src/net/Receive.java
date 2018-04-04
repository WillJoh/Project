/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import controller.Controller;
import dataStructures.Msg;
import dataStructures.Player;

/**
 *
 * @author William Joahnsson
 */
public class Receive extends Thread{
    private DatagramSocket socket;
    private boolean running;
    private byte[] buffer = new byte[512];
    private Controller controller;
    private Player inPlayer;
    
    public void connect(int port, OutputHandler outputHandler, Controller controller) {
        try {
            socket = new DatagramSocket(port);
            this.controller = controller;
            new Thread(new Listener(outputHandler)).start();
        } 
        catch(SocketException e) {
            System.out.println("could not establish socket: " + e.toString());
        }
    }
    
    public void disconnect() {
        socket.close();
    }
    
    
    
    private class Listener implements Runnable {
        private final OutputHandler outputHandler;
        
        private Listener(OutputHandler outputHandler) {
            this.outputHandler = outputHandler;
        }
        @Override
        public void run() {
            running = true;

            while (running) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                try {
                    socket.receive(packet);
                }
                catch (IOException e) {
                    System.out.println("could not recieve packet: " + e.toString());
                }

                Player player = Msg.getMSG(new String(packet.getData()));
                boolean validPlayer = controller.processMsg(player);

                if(!validPlayer) {
                    String notValid = "NO";
                    byte[] tempBuffer = notValid.getBytes();
                    packet = new DatagramPacket(tempBuffer, tempBuffer.length, packet.getAddress(), packet.getPort());
                    try {
                        socket.send(packet);
                    }
                    catch(IOException e) {
                        System.out.println("Could not send message");
                    }
                }
            }
            socket.close();
    
        }
    }
}
