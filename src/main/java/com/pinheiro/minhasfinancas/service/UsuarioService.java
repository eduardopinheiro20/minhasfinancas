package com.pinheiro.minhasfinancas.service;

import com.pinheiro.minhasfinancas.model.Usuario;

public interface UsuarioService {

    Usuario autenticar(String email, String senha);

    Usuario salvarUsuario(Usuario pUsuario);

    void validarEmail(String email);
}
