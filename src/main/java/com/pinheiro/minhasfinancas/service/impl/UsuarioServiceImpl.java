package com.pinheiro.minhasfinancas.service.impl;

import com.pinheiro.minhasfinancas.exception.ErroAutenticacaoException;
import com.pinheiro.minhasfinancas.exception.RegraNegocioException;
import com.pinheiro.minhasfinancas.model.Usuario;
import com.pinheiro.minhasfinancas.repository.UsuarioRepository;
import com.pinheiro.minhasfinancas.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository pRepository) {
        this.usuarioRepository = pRepository;
    }

    @Override
    public Usuario autenticar(String email, String senha) {

        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);

        if(!usuario.isPresent()) {
            throw new ErroAutenticacaoException("Usuario nao encontrado para o email informado.");
        }

        if(!usuario.get().getSenha().equals(senha)) {
            throw new ErroAutenticacaoException("Senha invalida.");
        }

        return usuario.get();
    }

    @Override
    public Usuario salvarUsuario(Usuario pUsuario) {
        validarEmail(pUsuario.getEmail());
        return usuarioRepository.save(pUsuario);
    }

    @Override
    public void validarEmail(String email) {
        boolean existe = usuarioRepository.existsByEmail(email);
        if(existe) {
            throw new RegraNegocioException("Ja existe uma usuario cadastrado com esse email.");
        }
    }

    @Override
    public Optional<Usuario> getById(Long id) {
        return usuarioRepository.findById(id);
    }
}
