package br.edu.ifpb.ads.foodjava.exception;

public class CancelamentoNaoPermitidoException extends Exception {
    public CancelamentoNaoPermitidoException(String msg) { super(msg); }
    public CancelamentoNaoPermitidoException() { super("CancelamentoNaoPermitidoException"); }
}
