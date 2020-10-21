package com.pinheiro.minhasfinancas.service.impl;

import com.pinheiro.minhasfinancas.exception.ErroAutenticacao;
import com.pinheiro.minhasfinancas.exception.RegraNegocioException;
import com.pinheiro.minhasfinancas.model.entity.Usuario;
import com.pinheiro.minhasfinancas.model.repository.UsuarioRepository;
import com.pinheiro.minhasfinancas.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        super();
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario autenticar(String email, String senha) {

        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);

        if (!usuario.isPresent()) {
            throw new ErroAutenticacao("Usuário não encontrado.");
        }

        if (!usuario.get().getSenha().equals(senha)) {
            throw new ErroAutenticacao("Senha inválida.");
        }


        return usuario.get();
    }

    @Override
    @Transactional
    public Usuario salvarUsuario(Usuario usuario) {

        validarEmail(usuario.getEmail());
        return usuarioRepository.save(usuario);
    }

    @Override
    public void validarEmail(String email) {

        boolean existe = usuarioRepository.existsByEmail(email);
        if (existe) {
            throw new RegraNegocioException("Já existe um usuario cadastrado com este email.");
        }
    }

    @Override
    public Optional<Usuario> obterPorId(Long id) {
        return usuarioRepository.findById(id);
    }
}
