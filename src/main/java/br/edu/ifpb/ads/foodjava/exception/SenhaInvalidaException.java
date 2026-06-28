package br.edu.ifpb.ads.foodjava.exception;

public class SenhaInvalidaException extends Exception {
    public SenhaInvalidaException(String msg) { super(msg); }
    public SenhaInvalidaException() { super("SenhaInvalidaException"); }
}
