package com.pinheiro.minhasfinancas.api.resource;

import com.pinheiro.minhasfinancas.api.dto.UsuarioDto;
import com.pinheiro.minhasfinancas.exception.ErroAutenticacao;
import com.pinheiro.minhasfinancas.exception.RegraNegocioException;
import com.pinheiro.minhasfinancas.model.entity.Usuario;
import com.pinheiro.minhasfinancas.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    private UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/autenticar")
    public ResponseEntity autenticar(@RequestBody UsuarioDto usuarioDto) {

        try {
            Usuario autenticado = usuarioService.autenticar(usuarioDto.getEmail(), usuarioDto.getSenha());
            return ResponseEntity.ok(autenticado);
        } catch (ErroAutenticacao e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping
    public ResponseEntity salvar(@RequestBody UsuarioDto usuarioDto) {

        Usuario usuario = Usuario.builder()
                .nome(usuarioDto.getNome())
                .email(usuarioDto.getEmail())
                .senha(usuarioDto.getSenha()).build();

        try {
            Usuario usuarioSalvo = usuarioService.salvarUsuario(usuario);
            return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
