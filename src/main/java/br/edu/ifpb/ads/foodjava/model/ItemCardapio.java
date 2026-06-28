package br.edu.ifpb.ads.foodjava.model;

import java.util.UUID;

public class ItemCardapio {
    private String id;
    private String nome;
    private String descricao;
    private double preco;
    private Categoria categoria;
    private boolean disponivel;
    private String imagemPath;

    public ItemCardapio() {
        this.id = UUID.randomUUID().toString();
    }

    public ItemCardapio(String nome, String descricao, double preco, Categoria categoria, boolean disponivel, String imagemPath) {
        this.id = UUID.randomUUID().toString();
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.categoria = categoria;
        this.disponivel = disponivel;
        this.imagemPath = imagemPath;
    }

    public String getId()               { return id; }
    public String getNome()             { return nome; }
    public void   setNome(String nome)  { this.nome = nome; }
    public String getDescricao()                    { return descricao; }
    public void   setDescricao(String descricao)    { this.descricao = descricao; }
    public double getPreco()                { return preco; }
    public void   setPreco(double preco)    { this.preco = preco; }
    public Categoria getCategoria()                   { return categoria; }
    public void      setCategoria(Categoria categoria){ this.categoria = categoria; }
    public boolean isDisponivel()                   { return disponivel; }
    public void    setDisponivel(boolean disponivel) { this.disponivel = disponivel; }
    public String getImagemPath()                     { return imagemPath; }
    public void   setImagemPath(String imagemPath)    { this.imagemPath = imagemPath; }

    @Override
    public String toString() { return nome + " - R$ " + String.format("%.2f", preco); }
}
