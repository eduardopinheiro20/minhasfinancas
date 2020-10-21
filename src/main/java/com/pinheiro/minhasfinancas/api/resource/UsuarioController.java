package com.pinheiro.minhasfinancas.api.resource;

import com.pinheiro.minhasfinancas.api.dto.UsuarioDto;
import com.pinheiro.minhasfinancas.exception.ErroAutenticacao;
import com.pinheiro.minhasfinancas.exception.RegraNegocioException;
import com.pinheiro.minhasfinancas.model.entity.Usuario;
import com.pinheiro.minhasfinancas.service.LancamentoService;
import com.pinheiro.minhasfinancas.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController<ReponseEntity> {

    private final UsuarioService usuarioService;
    private final LancamentoService lancamentoService;

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

    @GetMapping("{id}/saldo")
    public ReponseEntity obterSaldo( @PathVariable("id") Long id){

        Optional<Usuario> usuario = usuarioService.obterPorId(id);

        if(!usuario.isPresent()){
            return (ReponseEntity) new ResponseEntity( HttpStatus.NOT_FOUND );
        }

        BigDecimal saldo = lancamentoService.obterSaldoPorUsuario(id);
        return (ReponseEntity) ResponseEntity.ok(saldo);
    }

}
