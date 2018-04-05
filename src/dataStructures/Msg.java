/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataStructures;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 * @author William Joahnsson
 */
public class Msg {

    public static String createMsg(Player player) {
        StringBuilder stringBuilder = new StringBuilder();
        
        stringBuilder.append(player.name);
        
        stringBuilder.append(";:;");
        
        stringBuilder.append(player.action);
        
        stringBuilder.append(";:;");
        
        stringBuilder.append(player.score);
        
        stringBuilder.append(";:;");       
        
        stringBuilder.append(player.round);
        
        stringBuilder.append(";:;");       
        
        stringBuilder.append(player.address);
        
        stringBuilder.append(";:;");       
        
        stringBuilder.append(player.port);
        
        stringBuilder.append(";:;"); 
        
        return stringBuilder.toString();
    }
    
    public static Player getMSG(String msg) {
        Player player = new Player();
        player.name = msg.split(";:;")[0];
        switch(msg.split(";:;")[1]) {
            case "PAPER":
                player.action = PlayerAction.PAPER;
                break;
            case "ROCK":
                player.action = PlayerAction.ROCK;
                break;
            case "SCICCORS":
                player.action = PlayerAction.SCISSORS;
                break;
            case "NOTHING":
                player.action = PlayerAction.NOTHING;
                break;
        }
        player.score = Integer.valueOf(msg.split(";:;")[2]);
        player.round = Integer.valueOf(msg.split(";:;")[3]);
        try {
            String temp = msg.split(";:;")[4];
            if (temp.contains("/")) {
                temp = temp.split("/")[1];
            }
            player.address = InetAddress.getByName(temp);
        } 
        catch (UnknownHostException e) {
            System.out.println(e.toString() + msg.split(";:;")[4]);
        }

        player.port = Integer.valueOf(msg.split(";:;")[5]);
        
        return player;
    }
}
