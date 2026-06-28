package br.edu.ifpb.ads.foodjava.exception;

public class CarrinhoVazioException extends Exception {
    public CarrinhoVazioException(String msg) { super(msg); }
    public CarrinhoVazioException() { super("CarrinhoVazioException"); }
}
