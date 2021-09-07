package com.pinheiro.minhasfinancas.service;

import com.pinheiro.minhasfinancas.model.Usuario;

import java.util.Optional;

public interface UsuarioService {

    Usuario autenticar(String email, String senha);

    Usuario salvarUsuario(Usuario pUsuario);

    void validarEmail(String email);

    Optional<Usuario> getById(Long id);
}
