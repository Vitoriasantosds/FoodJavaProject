package br.edu.ifpb.ads.foodjava.model;

public enum StatusPedido {
    AGUARDANDO_CONFIRMACAO("Aguardando Confirmação"),
    CONFIRMADO("Confirmado"),
    EM_PREPARO("Em Preparo"),
    SAIU_PARA_ENTREGA("Saiu para Entrega"),
    ENTREGUE("Entregue"),
    CANCELADO("Cancelado");

    private final String descricao;

    StatusPedido(String descricao) { this.descricao = descricao; }

    public String getDescricao() { return descricao; }

    @Override
    public String toString() { return descricao; }

    public StatusPedido proximo() {
        return switch (this) {
            case AGUARDANDO_CONFIRMACAO -> CONFIRMADO;
            case CONFIRMADO             -> EM_PREPARO;
            case EM_PREPARO             -> SAIU_PARA_ENTREGA;
            case SAIU_PARA_ENTREGA      -> ENTREGUE;
            default -> null;
        };
    }
}
