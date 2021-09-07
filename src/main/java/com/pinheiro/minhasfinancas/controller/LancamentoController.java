package com.pinheiro.minhasfinancas.controller;

import com.pinheiro.minhasfinancas.dto.LancamentoDto;
import com.pinheiro.minhasfinancas.enums.StatusLancamento;
import com.pinheiro.minhasfinancas.enums.TipoLancamento;
import com.pinheiro.minhasfinancas.exception.RegraNegocioException;
import com.pinheiro.minhasfinancas.model.Lancamento;
import com.pinheiro.minhasfinancas.model.Usuario;
import com.pinheiro.minhasfinancas.service.LancamentoService;
import com.pinheiro.minhasfinancas.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/lancamentos")
public class LancamentoController {

    @Autowired
    private LancamentoService lancamentoService;

    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity salvar(@RequestBody LancamentoDto pLancamenoDto) {
        try {
            Lancamento lancamento = converter(pLancamenoDto);
            lancamento = lancamentoService.salvar(lancamento);
            return new ResponseEntity(lancamento, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity atualizar( @PathVariable("id") Long id, @RequestBody LancamentoDto pLancamentoDto) {
           return lancamentoService.getById(id).map( entity -> {
                try {
                    Lancamento lancamento = converter(pLancamentoDto);
                    lancamento.setId(entity.getId());
                    lancamentoService.atualizar(lancamento);
                    return ResponseEntity.ok(lancamento);
                } catch (RegraNegocioException e ) {
                    return ResponseEntity.badRequest().body(e.getMessage());
                }
            }).orElseGet( () ->
                            new ResponseEntity("Lançamento não encontrado na base de Dados.", HttpStatus.BAD_REQUEST) );

    }

    private Lancamento converter(LancamentoDto pLancamenDto) {
        Lancamento lancamento = new Lancamento();
        lancamento.setDescricao(pLancamenDto.getDescricao());
        lancamento.setAno(pLancamenDto.getAno());
        lancamento.setMes(pLancamenDto.getMes());
        lancamento.setValor(pLancamenDto.getValor());

        Usuario usuario = usuarioService.getById(pLancamenDto.getUsuario())
                        .orElseThrow(() -> new RegraNegocioException("Usuario não encontrado para o Id informado."));

        lancamento.setUsuario(usuario);
        lancamento.setTipoLancamento(TipoLancamento.valueOf(pLancamenDto.getTipoLancamento()));
        lancamento.setStatusLancamento(StatusLancamento.valueOf(pLancamenDto.getStatusLancamento()));

        return lancamento;
    }

}
