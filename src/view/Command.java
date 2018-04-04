/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import dataStructures.MsgHeaders;



/**
 *
 * @author William Joahnsson
 */
public class Command {
    private String command;
    private MsgHeaders header;
    private String body;
    public Command(String command) {
        this.command = command.toLowerCase();
        extractHeaderAndBody(command);
    }
    
    private void extractHeaderAndBody(String command) {
        if (command.equals("start game")) {
            header = MsgHeaders.START_GAME;
            body = "";
        } else if (command.equals("disconnect")) {
            header = MsgHeaders.DISCONNECT;
            body = "";
        } else if (command.split(" ")[0].equals("connect")) {
            header = MsgHeaders.CONNECT;
            body = command;
        } else if (command.split(" ")[0].equals("start")) {
            header = MsgHeaders.START;
            body = command;
        } else if (command.equalsIgnoreCase("rock") || 
                command.equalsIgnoreCase("paper") || 
                command.equalsIgnoreCase("sciccors")) {
            header = MsgHeaders.PLAY;
            body = command.toLowerCase();
        }
    }
    
    public MsgHeaders getHeader() {
        return header;
    }
    
    public String getBody() {
        return body;
    }
}
