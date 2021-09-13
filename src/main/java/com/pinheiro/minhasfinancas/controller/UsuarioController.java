package com.pinheiro.minhasfinancas.controller;

import com.pinheiro.minhasfinancas.dto.UsuarioDto;
import com.pinheiro.minhasfinancas.exception.ErroAutenticacaoException;
import com.pinheiro.minhasfinancas.exception.RegraNegocioException;
import com.pinheiro.minhasfinancas.model.Usuario;
import com.pinheiro.minhasfinancas.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping("/autenticar")
    public ResponseEntity autenticar(@RequestBody UsuarioDto dto) {
        try {
           Usuario usuarioAutnticado =  usuarioService.autenticar(dto.getEmail(), dto.getSenha());
           return ResponseEntity.ok(usuarioAutnticado);
        } catch (ErroAutenticacaoException e) {
            return  ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity salvar(@RequestBody UsuarioDto dto) {
        Usuario usuario = Usuario.builder()
                        .nome(dto.getNome())
                        .email(dto.getEmail())
                        .senha(dto.getSenha()).build();
        try {
            Usuario usuarioSalvo =  usuarioService.salvarUsuario(usuario);
            return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
