package br.edu.ifpb.ads.foodjava.exception;

public class PrecoInvalidoException extends Exception {
    public PrecoInvalidoException(String msg) { super(msg); }
    public PrecoInvalidoException() { super("PrecoInvalidoException"); }
}
