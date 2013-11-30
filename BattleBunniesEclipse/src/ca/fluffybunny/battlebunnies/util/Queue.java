package ca.fluffybunny.battlebunnies.util;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Simple queue for single player channel communication.
 *
 * @author Taras Mychaskiw
 * @version 1.0
 * @since 2013-11-23
 */
public class Queue<T extends Serializable> {

    private LinkedList<T> data;

    /**
     * Default constructor. Initializes the list.
     */
    public Queue(){
        data = new LinkedList<T>();
    }


    /**
     * Pulls at the list until there is data to return
     * 
     * @return element in the list that we received
     */
    public synchronized T pull(){
        while (data.isEmpty()){
            try {
                wait(); //block until notify() is called, give up the lock
            } catch (InterruptedException e){}
        }
        return data.removeFirst();
    }


    /**
     * Pushes the item into the list.
     * 
     * @param item the item to add to the list
     */
    public synchronized void push(T item){
        data.add(item);
        notify();
    }
}
