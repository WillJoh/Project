/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataStructures;

/**
 *
 * @author William Joahnsson
 */
public enum PlayerAction {
    NOTHING("Nothing"), ROCK("rock"), PAPER("paper"), SCICCORS("paper");
    
    public final String id;
    PlayerAction(String id) {
        this.id = id;
    }
}
