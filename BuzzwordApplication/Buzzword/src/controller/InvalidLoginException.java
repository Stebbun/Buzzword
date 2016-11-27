package controller;

/**
 * Created by stebbun on 11/26/2016.
 */
public class InvalidLoginException extends Exception {
    public InvalidLoginException(String message){
        super(message);
    }
}
