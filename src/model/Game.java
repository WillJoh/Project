/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import dataStructures.Player;
import dataStructures.PlayerAction;
import java.util.ArrayList;
import java.util.List;
import controller.Controller;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 * @author William Joahnsson
 */
public class Game {
    Controller controller;
    public int currRound;
    public Player currPlayer;
    public List<Player> players  = new ArrayList<Player>();
    
    public Game(Controller controller, String name, int port){
        this.controller = controller;
        currPlayer = new Player();
        currPlayer.action = PlayerAction.NOTHING;
        try {
            currPlayer.address = InetAddress.getByName("localhost");
        }
        catch (UnknownHostException e) {
            System.out.println(e.toString());
        }
        currPlayer.name = name;
        currPlayer.port = port;
        currPlayer.round = 0;
        currPlayer.score = 0;
    }
    
    public boolean updatePlayer(Player newPlayer) {

        if(newPlayer.name.equals(currPlayer.name)) {
            return false;
        }
        for (Player player : players) {
            if(player.name.equals(newPlayer.name)) {
                if(newPlayer.round > player.round && newPlayer.round > currRound) {
                    player.action = newPlayer.action;
                    player.round = newPlayer.round;
                    player.score = newPlayer.score;
                    controller.passOnTxt(player.name + " played");
                    updateRound();
                    return true;
                }
                else {
                    return false;
                }
            }
        }
        if (currRound <= 0) {
            sendAllPlayers(newPlayer);  
            players.add(newPlayer); 
            controller.showNewPlayer(newPlayer);
            return true;
        }
        return false;
    }
    
    public void sendAllPlayers(Player toPlayer){
        controller.sendToSpecificPlayer(currPlayer, players, toPlayer);
        controller.sendToAllPlayer(toPlayer);
    }
    
    public void updateRound() {
        controller.sendToAllPlayer(currPlayer);
        boolean rock = false;
        boolean paper = false;
        boolean sciccors = false;
        if (currPlayer.action == PlayerAction.NOTHING) {
            return;
        }
        else {
            switch (currPlayer.action){
                case PAPER :
                    paper = true;
                    break;
                case ROCK :
                    rock = true;
                    break;
                case SCICCORS : 
                    sciccors = true;
                    break;
            }
        }
        for (Player player : players) {
            if(player.action == PlayerAction.NOTHING) {
                return;
            }
            
            else {
                switch (player.action){
                    case PAPER :
                        paper = true;
                        break;
                    case ROCK :
                        rock = true;
                        break;
                    case SCICCORS : 
                        sciccors = true;
                        break;
                }
            }
        }
        currPlayer.round = currRound + 1;
        currRound++;
        if (rock == true && paper == true && sciccors == true) {
            controller.updateScore(currPlayer.score, false);
        } 
        else if(rock == true && paper == true) {
            if (currPlayer.action == PlayerAction.PAPER) {
                currPlayer.score++;
                controller.updateScore(currPlayer.score, true);
            }
            else {
                controller.updateScore(currPlayer.score, false);
            }
        }
        else if(paper == true && sciccors == true) {
            if (currPlayer.action == PlayerAction.SCICCORS) {
                currPlayer.score++;
                controller.updateScore(currPlayer.score, true);
            }
            else {
                controller.updateScore(currPlayer.score, false);
            }
        }
        else if(sciccors == true && rock == true) {
            if (currPlayer.action == PlayerAction.ROCK) {
                currPlayer.score++;
                controller.updateScore(currPlayer.score, true);
            }
            else {
                controller.updateScore(currPlayer.score, false);
            }
        }
        else {
            controller.updateScore(currPlayer.score, false);
        }
        
        currPlayer.action = PlayerAction.NOTHING;
        for(Player player : players) {
            player.action = PlayerAction.NOTHING;
        }
       
    }
}
