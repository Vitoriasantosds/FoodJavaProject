package br.edu.ifpb.ads.foodjava.exception;

public class ItemVinculadoException extends Exception {
    public ItemVinculadoException(String msg) { super(msg); }
    public ItemVinculadoException() { super("ItemVinculadoException"); }
}
