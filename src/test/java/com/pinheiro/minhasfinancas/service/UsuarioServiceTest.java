package com.pinheiro.minhasfinancas.service;

import com.pinheiro.minhasfinancas.exception.ErroAutenticacaoException;
import com.pinheiro.minhasfinancas.exception.RegraNegocioException;
import com.pinheiro.minhasfinancas.model.Usuario;
import com.pinheiro.minhasfinancas.repository.UsuarioRepository;
import com.pinheiro.minhasfinancas.service.impl.UsuarioServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

    @SpyBean
    UsuarioServiceImpl usuarioService;

    @MockBean
    UsuarioRepository usuarioRepository;

    @Test
    public void deveSalvarUmUsuario() {
        Mockito.doNothing().when(usuarioService).validarEmail(Mockito.anyString());
        Usuario usuario = Usuario.builder()
                        .id(1L)
                        .nome("Nome")
                        .email("email@email.com")
                        .senha("senha")
                        .build();

        Mockito.when(usuarioRepository.save(Mockito.any(Usuario.class))).thenReturn(usuario);

        Usuario usuarioSalvo = usuarioService.salvarUsuario(new Usuario());

        Assertions.assertThat(usuarioSalvo).isNotNull();
        Assertions.assertThat(usuarioSalvo.getId()).isEqualTo(1L);
        Assertions.assertThat(usuarioSalvo.getNome()).isEqualTo("Nome");
        Assertions.assertThat(usuarioSalvo.getEmail()).isEqualTo("email@email.com");
        Assertions.assertThat(usuarioSalvo).isNotNull();

    }

    @Test
    public void naoDeveSalvarUsuarioComEmailJaCadastrado() {
        String email = "email@email.com";

        Usuario usuario = Usuario.builder().email(email).build();
        Mockito.doThrow(RegraNegocioException.class).when(usuarioService).validarEmail(email);

        usuarioService.salvarUsuario(usuario);

        Mockito.verify(usuarioRepository, Mockito.never()).save(usuario);
    }

    @Test
    public void deveAutentiarUmUsuarioComSucesso() {
        String email = "email@email.com";
        String senha = "senha";

        Usuario usuario = Usuario.builder().email(email).senha(senha).id(1L).build();
        Mockito.when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));

        Usuario result = usuarioService.autenticar(email, senha);

        Assertions.assertThat(result).isNotNull();
    }

    @Test
    public void deveLancarErroQuandoNaoEncontrarCadastroComOEmailInformadl() {

        Mockito.when(usuarioRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());

        Throwable exception = Assertions.catchThrowable( () -> usuarioService.autenticar("email@email.com", "senha"));

        Assertions.assertThat(exception).isInstanceOf(ErroAutenticacaoException.class).hasMessage("Usuario nao encontrado para o email informado.");

    }

    @Test
    public void deveLancarErroQuandoSenhaNaoBater() {
        String email = "email@email.com";
        String senha = "senha";

        Usuario usuario = Usuario.builder().email(email).senha(senha).id(1L).build();
        Mockito.when(usuarioRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));

        Throwable exception = Assertions.catchThrowable( () -> usuarioService.autenticar("email@email.com", "123"));

        Assertions.assertThat(exception).isInstanceOf(ErroAutenticacaoException.class).hasMessage("a");

    }

    @Test
    public void deveValidarEmail() {
        Mockito.when(usuarioRepository.existsByEmail(Mockito.anyString())).thenReturn(false);

        usuarioService.validarEmail("email@email.com");
    }

    @Test
    public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado() {
        Mockito.when(usuarioRepository.existsByEmail(Mockito.anyString())).thenReturn(true);
        usuarioService.validarEmail("email@email.com");
    }

}
