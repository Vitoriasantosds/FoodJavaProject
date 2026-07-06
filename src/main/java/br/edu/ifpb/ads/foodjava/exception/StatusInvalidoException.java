package br.edu.ifpb.ads.foodjava.exception;

public class StatusInvalidoException extends Exception {
    public StatusInvalidoException(String msg) { super(msg); }
    public StatusInvalidoException() { super("StatusInvalidoException"); }
}
