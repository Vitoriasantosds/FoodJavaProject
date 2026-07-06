package br.edu.ifpb.ads.foodjava.controller;

import br.edu.ifpb.ads.foodjava.exception.*;
import br.edu.ifpb.ads.foodjava.model.*;
import br.edu.ifpb.ads.foodjava.repository.PedidoRepository;

import java.util.List;
import java.util.Map;

public class PedidoController {
    private final PedidoRepository pedidoRepo = new PedidoRepository();

    public Pedido criarPedido(String emailCliente, Map<ItemCardapio, Integer> carrinho)
            throws CarrinhoVazioException {
        if (carrinho == null || carrinho.isEmpty())
            throw new CarrinhoVazioException();

        Pedido pedido = new Pedido(emailCliente);
        for (Map.Entry<ItemCardapio, Integer> e : carrinho.entrySet()) {
            pedido.adicionarItem(new ItemPedido(e.getKey(), e.getValue()));
        }
        pedidoRepo.adicionar(pedido);
        return pedido;
    }

    public void avancarStatus(String pedidoId) throws StatusInvalidoException {
        Pedido p = pedidoRepo.buscarPorId(pedidoId);
        if (p == null) throw new StatusInvalidoException("Pedido não encontrado.");
        StatusPedido proximo = p.getStatus().proximo();
        if (proximo == null)
            throw new StatusInvalidoException("Pedido já está no status final.");
        p.setStatus(proximo);
        pedidoRepo.atualizar(p);
    }

    public void cancelarPedido(String pedidoId) throws CancelamentoNaoPermitidoException {
        Pedido p = pedidoRepo.buscarPorId(pedidoId);
        if (p == null) throw new CancelamentoNaoPermitidoException("Pedido não encontrado.");
        if (p.getStatus() != StatusPedido.AGUARDANDO_CONFIRMACAO)
            throw new CancelamentoNaoPermitidoException();
        p.setStatus(StatusPedido.CANCELADO);
        pedidoRepo.atualizar(p);
    }

    public List<Pedido> listarTodos() {
        return pedidoRepo.listarTodos();
    }

    public List<Pedido> listarPorCliente(String email) {
        return pedidoRepo.listarPorCliente(email);
    }
}
