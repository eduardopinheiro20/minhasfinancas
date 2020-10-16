package com.pinheiro.minhasfinancas.service.impl;

import com.pinheiro.minhasfinancas.model.entity.Usuario;
import com.pinheiro.minhasfinancas.model.repository.UsuarioRepository;
import com.pinheiro.minhasfinancas.service.UsuarioService;

public class UsuarioServiceImpl  implements UsuarioService {

    private UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository){
        super();
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario autenticar(String email, String senha) {
        return null;
    }

    @Override
    public Usuario salvarUsuario(Usuario usuario) {
        return null;
    }

    @Override
    public void validarEmail(String email) {

    }
}
