package br.edu.ifpb.ads.foodjava.controller;

import br.edu.ifpb.ads.foodjava.exception.DocumentoInvalidoException;
import br.edu.ifpb.ads.foodjava.exception.SenhaInvalidaException;
import br.edu.ifpb.ads.foodjava.model.Gerente;
import br.edu.ifpb.ads.foodjava.model.Restaurante;
import br.edu.ifpb.ads.foodjava.repository.RestauranteRepository;
import br.edu.ifpb.ads.foodjava.util.Validador;

public class RestauranteController {
    private final RestauranteRepository repo = new RestauranteRepository();

    public void configurar(String nome, String cnpj, String endereco, String telefone,
                           String categoriaCulinaria, String email, String nomeGerente, String senha)
            throws DocumentoInvalidoException, SenhaInvalidaException {

        if (!Validador.validarCnpj(cnpj))
            throw new DocumentoInvalidoException("CNPJ inválido.");
        if (!Validador.validarSenha(senha))
            throw new SenhaInvalidaException();

        Gerente gerente = new Gerente(nomeGerente, email, senha);
        Restaurante r = new Restaurante(nome, cnpj, endereco, telefone, categoriaCulinaria, email, gerente);
        repo.salvar(r);
    }

    public Restaurante carregar() {
        return repo.carregar();
    }

    public void salvar(Restaurante r) {
        repo.salvar(r);
    }
}
