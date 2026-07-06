package br.edu.ifpb.ads.foodjava.util;

public class Validador {

    public static boolean validarCpf(String cpf) {
        cpf = cpf.replaceAll("[^0-9]", "");
        if (cpf.length() != 11) return false;
        if (cpf.matches("(\\d)\\1{10}")) return false;

        int soma = 0;
        for (int i = 0; i < 9; i++) soma += (cpf.charAt(i) - '0') * (10 - i);
        int r1 = (soma * 10) % 11;
        if (r1 == 10 || r1 == 11) r1 = 0;
        if (r1 != (cpf.charAt(9) - '0')) return false;

        soma = 0;
        for (int i = 0; i < 10; i++) soma += (cpf.charAt(i) - '0') * (11 - i);
        int r2 = (soma * 10) % 11;
        if (r2 == 10 || r2 == 11) r2 = 0;
        return r2 == (cpf.charAt(10) - '0');
    }

    public static boolean validarCnpj(String cnpj) {
        cnpj = cnpj.replaceAll("[^0-9]", "");
        if (cnpj.length() != 14) return false;
        if (cnpj.matches("(\\d)\\1{13}")) return false;

        int[] pesos1 = {5,4,3,2,9,8,7,6,5,4,3,2};
        int[] pesos2 = {6,5,4,3,2,9,8,7,6,5,4,3,2};

        int soma = 0;
        for (int i = 0; i < 12; i++) soma += (cnpj.charAt(i) - '0') * pesos1[i];
        int r1 = soma % 11;
        r1 = (r1 < 2) ? 0 : 11 - r1;
        if (r1 != (cnpj.charAt(12) - '0')) return false;

        soma = 0;
        for (int i = 0; i < 13; i++) soma += (cnpj.charAt(i) - '0') * pesos2[i];
        int r2 = soma % 11;
        r2 = (r2 < 2) ? 0 : 11 - r2;
        return r2 == (cnpj.charAt(13) - '0');
    }

    public static boolean validarSenha(String senha) {
        if (senha == null || senha.length() < 8) return false;
        return senha.chars().anyMatch(Character::isDigit);
    }
}
