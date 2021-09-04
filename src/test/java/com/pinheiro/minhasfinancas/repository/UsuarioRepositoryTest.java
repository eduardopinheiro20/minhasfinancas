package com.pinheiro.minhasfinancas.repository;

import com.pinheiro.minhasfinancas.model.Usuario;
import org.assertj.core.api.Assertions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UsuarioRepositoryTest {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired TestEntityManager entityManager;

    @Test
    public void deveVerificarAExistenciaDeUmEmail() {
        Usuario usuario = criaUsuario();
        entityManager.persist(usuario);

        boolean existe = usuarioRepository.existsByEmail("usuario@email.com");

        Assertions.assertThat(existe).isTrue();
    }

    @Test
    public void deveVerificarQuandoNaoExisteDeUmEmailCadastrado() {

        boolean existe = usuarioRepository.existsByEmail("usuario@email.com");

        Assertions.assertThat(existe).isFalse();
    }

    @Test
    public void devePersistirUmUsuarioNaBasaDeDados() {
        Usuario usuario =  criaUsuario();

        final Usuario usuarioSalvo = usuarioRepository.save(usuario);

        Assertions.assertThat(usuarioSalvo.getId()).isNotNull();
    }
    
    @Test
    public void deveBuscarUmUsuarioPorEmail() {
        Usuario usuario =  criaUsuario();
        entityManager.persist(usuario);

        Optional<Usuario> result = usuarioRepository.findByEmail("usuario@email.com");

        Assertions.assertThat(result.isPresent()).isTrue();
    }

    @Test
    public void deveRetornarVazioAoBuscarUsuarioPorEmailQuandoNaoExisteNaBase() {

        Optional<Usuario> result = usuarioRepository.findByEmail("usuario@email.com");

        Assertions.assertThat(result.isPresent()).isFalse();
    }

    public static Usuario criaUsuario() {
        return Usuario
                    .builder()
                    .nome("usuario")
                    .email("usuario@email.com")
                    .senha("senha")
                    .build();
    }
}
