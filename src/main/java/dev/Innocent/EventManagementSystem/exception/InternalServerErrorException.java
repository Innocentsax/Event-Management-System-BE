package dev.Innocent.EventManagementSystem.exception;


import org.springframework.http.HttpStatus;

public class InternalServerErrorException extends Throwable {
    public InternalServerErrorException(String s, HttpStatus httpStatus) {
    }
}

