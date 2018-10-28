/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author Hong Chew
 */
public class NoSuchGuestException extends Exception {

    /**
     * Creates a new instance of <code>NoSuchGuestException</code> without
     * detail message.
     */
    public NoSuchGuestException() {
    }

    /**
     * Constructs an instance of <code>NoSuchGuestException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public NoSuchGuestException(String msg) {
        super(msg);
    }
}
