/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.Controller;
import dataStructures.PlayerAction;
import java.net.InetAddress;
import java.util.Scanner;
import net.OutputHandler;

/**
 *
 * @author William Joahnsson
 */
public class Interpreter implements Runnable{
    private final Scanner scanner;
    private boolean receiving;
    private Controller controller;
    private final ThreadSafeOutput output;
    OutputHandler outputHandler;
    public Interpreter() {
        scanner = new Scanner(System.in);
        receiving = false;
        output = new ThreadSafeOutput();
    }
    
    public void init() {
        if (receiving) {
            return;
        }
        receiving = true;
        outputHandler = new ConsoleOutput();
        controller = new Controller(outputHandler);
        outputHandler.handleMsg("To start please enter \"start [port number] [userName]\" ");

        new Thread(this).start();
    }
    
    @Override
    public void run() {
        
        while(receiving) {
            try {
                Command command = new Command(scanner.nextLine());
                switch (command.getHeader()) {
                    case PLAY:
                        switch (command.getBody()) {
                            case "rock" :
                            controller.play(PlayerAction.ROCK);    
                            break;
                            
                            case "paper" :
                            controller.play(PlayerAction.PAPER);    
                            break;
                            
                            case "scissors" :
                            controller.play(PlayerAction.SCISSORS);    
                            break;
                        }                        
                        break;
                    case DISCONNECT:
                        controller.disconnect();
                        break;
                    case START:
                        String[] temp = command.getBody().split(" ");
                        controller.connect(Integer.parseInt(temp[1]), temp[2], 
                                outputHandler);
                        break;
                    case CONNECT:
                        temp = command.getBody().split(" ");
                        controller.initContact(InetAddress.getByName(temp[1]), Integer.parseInt(temp[2]));
                }
                
            } catch (Exception e) {
                output.println("Not a valid command");
            }
        }
    }
    
    private class ConsoleOutput implements OutputHandler {
        @Override
        public void handleMsg(String msg) {
            output.println(msg);
        }
    }
}
