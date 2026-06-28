package br.edu.ifpb.ads.foodjava.exception;

public class DocumentoInvalidoException extends Exception {
    public DocumentoInvalidoException(String msg) { super(msg); }
    public DocumentoInvalidoException() { super("DocumentoInvalidoException"); }
}
