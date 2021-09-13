package com.pinheiro.minhasfinancas.controller;

import com.pinheiro.minhasfinancas.dto.LancamentoDto;
import com.pinheiro.minhasfinancas.enums.StatusLancamento;
import com.pinheiro.minhasfinancas.enums.TipoLancamento;
import com.pinheiro.minhasfinancas.exception.RegraNegocioException;
import com.pinheiro.minhasfinancas.model.Lancamento;
import com.pinheiro.minhasfinancas.model.Usuario;
import com.pinheiro.minhasfinancas.service.LancamentoService;
import com.pinheiro.minhasfinancas.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/lancamentos")
@RequiredArgsConstructor
public class LancamentoController {


    private final LancamentoService lancamentoService;
    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity buscar(
                    @RequestParam(value = "descricao", required = false) String descricao,
                    @RequestParam(value = "mes", required = false) Integer mes,
                    @RequestParam(value = "ano", required = false) Integer ano,
                    @RequestParam("usuario") Long idUsuario
    ) {
        Lancamento lancamentoFiltro = new Lancamento();
        lancamentoFiltro.setDescricao(descricao);
        lancamentoFiltro.setAno(ano);
        lancamentoFiltro.setMes(mes);

        Optional<Usuario> optional = usuarioService.getById(idUsuario);
        if(optional.isPresent()) {
            return ResponseEntity.badRequest().body("Não foi possivel realizar a consulta, Usuario não encontrado para o Id informado.");
        } else {
            lancamentoFiltro.setUsuario(optional.get());
        }

        List<Lancamento> lancamentos = lancamentoService.buscar(lancamentoFiltro);
        return ResponseEntity.ok(lancamentos);
    }

    @PostMapping
    public ResponseEntity salvar(@RequestBody LancamentoDto pLancamentoDto) {
        try {
            Lancamento lancamento = converter(pLancamentoDto);
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

    @DeleteMapping({"{id"})
    public ResponseEntity deletar(@PathVariable("id") Long id) {
        return lancamentoService.getById(id).map( entity -> {
            lancamentoService.deletar(entity);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }).orElseGet( () ->
                        new ResponseEntity("Laçamento nao encontrado na base de dados.", HttpStatus.BAD_REQUEST) );
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

        if(pLancamenDto.getTipo() != null ) {
            lancamento.setTipo(TipoLancamento.valueOf(pLancamenDto.getTipo()));
        }

        if(pLancamenDto.getStatus() != null) {
            lancamento.setStatus(StatusLancamento.valueOf(pLancamenDto.getStatus()));
        }

        return lancamento;
    }

}
