package dev.Innocent.EventManagementSystem.exception;

public class RequestedResourceNotFoundException extends RuntimeException{
    public RequestedResourceNotFoundException(String message){
        super(message);
    }
}
