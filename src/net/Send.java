/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net;

import controller.Controller;
import dataStructures.Msg;
import dataStructures.Player;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 *
 * @author William Joahnsson
 */
public class Send {
    private DatagramSocket socket;
    private InetAddress address;
    private int port;
    private byte[] buf;
    
    
    public Send(int port) {
        try {
            socket = new DatagramSocket();
            address = InetAddress.getByName("localhost");
            this.port = port;
        } 
        catch(SocketException e) {
            System.out.println("could not establish socket: " + e.toString());
        }
        catch(UnknownHostException e) {
            System.out.println("could not get local ip: " + e.toString());
        }
    }
    
    public void sendMsg(Player player, InetAddress outAddress, int outPort) {
        buf = Msg.createMsg(player).getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, outAddress, outPort);
        try {
            socket.send(packet);
        }
        catch(IOException e) {
            System.out.println("Could not send message");
        }
    }
}
