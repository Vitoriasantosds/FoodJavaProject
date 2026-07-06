package br.edu.ifpb.ads.foodjava.exception;

public class UsuarioDuplicadoException extends Exception {
    public UsuarioDuplicadoException(String msg) { super(msg); }
    public UsuarioDuplicadoException() { super("UsuarioDuplicadoException"); }
}
