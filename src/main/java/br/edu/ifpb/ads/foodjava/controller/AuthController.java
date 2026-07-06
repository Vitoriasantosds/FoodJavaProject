package br.edu.ifpb.ads.foodjava.controller;

import br.edu.ifpb.ads.foodjava.exception.*;
import br.edu.ifpb.ads.foodjava.model.*;
import br.edu.ifpb.ads.foodjava.repository.*;
import br.edu.ifpb.ads.foodjava.util.Validador;

public class AuthController {
    private final ClienteRepository clienteRepo = new ClienteRepository();
    private final RestauranteRepository restauranteRepo = new RestauranteRepository();

    public Cliente cadastrarCliente(String nome, String email, String senha,
                                    String cpf, String telefone, String endereco)
            throws UsuarioDuplicadoException, SenhaInvalidaException, DocumentoInvalidoException {

        if (!Validador.validarSenha(senha))
            throw new SenhaInvalidaException();
        if (!Validador.validarCpf(cpf))
            throw new DocumentoInvalidoException("CPF inválido.");
        if (clienteRepo.existeEmail(email))
            throw new UsuarioDuplicadoException("E-mail já cadastrado.");
        if (clienteRepo.existeCpf(cpf))
            throw new UsuarioDuplicadoException("CPF já cadastrado.");

        Cliente c = new Cliente(nome, email, senha, cpf, telefone, endereco);
        clienteRepo.adicionar(c);
        return c;
    }

    public Usuario login(String email, String senha) {
        Restaurante restaurante = restauranteRepo.carregar();
        if (restaurante != null) {
            Gerente g = restaurante.getGerente();
            if (g != null && g.getEmail().equalsIgnoreCase(email) && g.getSenha().equals(senha)) {
                return g;
            }
        }
        Cliente c = clienteRepo.buscarPorEmail(email);
        if (c != null && c.getSenha().equals(senha)) return c;
        return null;
    }
}
