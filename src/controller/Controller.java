/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dataStructures.Player;
import dataStructures.PlayerAction;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetAddress;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import model.Game;
import net.OutputHandler;
import net.Receive;
import net.Send;

/**
 *
 * @author William Joahnsson
 */
public class Controller {
    Game game;
    Send send;
    Receive receive = new Receive();
    OutputHandler outputHandler;
    
    public Controller(OutputHandler outputHandler){
        this.outputHandler = outputHandler;
    }
    
    public boolean processMsg(Player player) {
        return game.updatePlayer(player);
    }
    
    public void passOnTxt(String txt) {
        outputHandler.handleMsg(txt);
    }
    
    public void play(PlayerAction action) {
        game.currPlayer.action = action;
        game.currPlayer.round = game.currRound + 1;
        game.updateRound();
    }
    
    public void sendToSpecificPlayer(Player currPlayer, List<Player> players, Player toPlayer) {
        send.sendMsg(currPlayer, toPlayer.address, toPlayer.port);
        for (Player player : players) {
            send.sendMsg(player, toPlayer.address, toPlayer.port);
        }
    }
    
    public void sendToAllPlayer(Player msg) {
        for(Player player : game.players) {
            send.sendMsg(msg, player.address, player.port);
        }
    }
    
    public void showActivePlayers() {
        outputHandler.handleMsg("You are playing with: ");
        for(Player player : game.players) {
            outputHandler.handleMsg(player.name);
        }
    }
    
    public void showNewPlayer(Player player) {
        outputHandler.handleMsg(player.name + " joined the game");
    }
    
    public void updateScore(int score, boolean won) {
        outputHandler.handleMsg("You " + (won ? "won " : "did not win ") + "your current score is " + score + " and current round is " + game.currRound);
    }
    
    public void connect(int port, String name, OutputHandler outputHandler) {
        game = new Game(this, name, port);
        send = new Send(port);
        CompletableFuture.runAsync(() -> {
            receive.connect(port, outputHandler, this);

        }).thenRun(() -> outputHandler.handleMsg("Successfully started at" +
                ":" + port + ", to connect to a game please enter \"connect [IP] [port]\", you can not enter a game that has already started")); 
    }
    
    public void initContact(InetAddress address, int port) {
        send.sendMsg(game.currPlayer, address, port);
        outputHandler.handleMsg("To play write \"rock\", \"paper\" or \"sciccors\", to quit just close the program");
    }
    
    public void disconnect() {
        receive.disconnect();
    }
}
