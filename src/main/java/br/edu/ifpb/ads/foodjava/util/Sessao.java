package br.edu.ifpb.ads.foodjava.util;

import br.edu.ifpb.ads.foodjava.model.Usuario;

/** Mantém o usuário logado durante a sessão. */
public class Sessao {
    private static Sessao instancia;
    private Usuario usuarioLogado;

    private Sessao() {}

    public static Sessao get() {
        if (instancia == null) instancia = new Sessao();
        return instancia;
    }

    public Usuario getUsuarioLogado()              { return usuarioLogado; }
    public void    setUsuarioLogado(Usuario u)     { this.usuarioLogado = u; }

    public void encerrar() { usuarioLogado = null; }
}
