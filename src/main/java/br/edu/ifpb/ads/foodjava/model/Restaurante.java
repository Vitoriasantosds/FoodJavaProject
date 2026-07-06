package br.edu.ifpb.ads.foodjava.model;

public class Restaurante {
    private String nome;
    private String cnpj;
    private String endereco;
    private String telefone;
    private String categoriaCulinaria;
    private String email;
    private String logotipoPath;
    private Gerente gerente;

    public Restaurante() {}

    public Restaurante(String nome, String cnpj, String endereco, String telefone,
                       String categoriaCulinaria, String email, Gerente gerente) {
        this.nome = nome;
        this.cnpj = cnpj;
        this.endereco = endereco;
        this.telefone = telefone;
        this.categoriaCulinaria = categoriaCulinaria;
        this.email = email;
        this.gerente = gerente;
    }

    public String getNome()              { return nome; }
    public void   setNome(String nome)   { this.nome = nome; }
    public String getCnpj()              { return cnpj; }
    public void   setCnpj(String cnpj)   { this.cnpj = cnpj; }
    public String getEndereco()                  { return endereco; }
    public void   setEndereco(String endereco)   { this.endereco = endereco; }
    public String getTelefone()                  { return telefone; }
    public void   setTelefone(String telefone)   { this.telefone = telefone; }
    public String getCategoriaCulinaria()        { return categoriaCulinaria; }
    public void   setCategoriaCulinaria(String c){ this.categoriaCulinaria = c; }
    public String getEmail()             { return email; }
    public void   setEmail(String email) { this.email = email; }
    public String getLogotipoPath()                    { return logotipoPath; }
    public void   setLogotipoPath(String logotipoPath) { this.logotipoPath = logotipoPath; }
    public Gerente getGerente()              { return gerente; }
    public void    setGerente(Gerente g)     { this.gerente = g; }
}
