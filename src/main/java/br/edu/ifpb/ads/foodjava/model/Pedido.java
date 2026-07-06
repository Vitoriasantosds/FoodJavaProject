package br.edu.ifpb.ads.foodjava.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Pedido {
    private String id;
    private String emailCliente;
    private List<ItemPedido> itens;
    private StatusPedido status;
    private String dataHora;

    public Pedido(String emailCliente) {
        this.id = UUID.randomUUID().toString();
        this.emailCliente = emailCliente;
        this.itens = new ArrayList<>();
        this.status = StatusPedido.AGUARDANDO_CONFIRMACAO;
        this.dataHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    public String getId()                   { return id; }
    public String getEmailCliente()         { return emailCliente; }
    public List<ItemPedido> getItens()      { return itens; }
    public StatusPedido getStatus()         { return status; }
    public void setStatus(StatusPedido s)   { this.status = s; }
    public String getDataHora()             { return dataHora; }

    public void adicionarItem(ItemPedido ip) { itens.add(ip); }

    public double getTotal() {
        return itens.stream().mapToDouble(ItemPedido::getSubtotal).sum();
    }
}
