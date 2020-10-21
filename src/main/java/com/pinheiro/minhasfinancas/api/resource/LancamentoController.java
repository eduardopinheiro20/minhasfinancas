package com.pinheiro.minhasfinancas.api.resource;

import com.pinheiro.minhasfinancas.api.dto.AtualizaStatusDto;
import com.pinheiro.minhasfinancas.api.dto.LancamentoDto;
import com.pinheiro.minhasfinancas.exception.RegraNegocioException;
import com.pinheiro.minhasfinancas.model.entity.Lancamento;
import com.pinheiro.minhasfinancas.model.entity.Usuario;
import com.pinheiro.minhasfinancas.model.enums.StatusLancamento;
import com.pinheiro.minhasfinancas.model.enums.TipoLancamento;
import com.pinheiro.minhasfinancas.service.LancamentoService;
import com.pinheiro.minhasfinancas.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lancamentos")
@RequiredArgsConstructor
public class LancamentoController {

    private final LancamentoService lancamentoService;
    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity buscar(@RequestParam(value = "descricao", required = false) String descricao,
                                 @RequestParam(value = "mes", required = false) Integer mes,
                                 @RequestParam(value = "ano", required = false) Integer ano,
                                 @RequestParam( "usuario") Long idUsuario){

        Lancamento lancamentoFiltro = new Lancamento();
        lancamentoFiltro.setDescricao(descricao);
        lancamentoFiltro.setMes(mes);
        lancamentoFiltro.setAno(ano);

        Optional<Usuario> usuario = usuarioService.obterPorId(idUsuario);

        if(usuario.isPresent()){
            return ResponseEntity.badRequest().body("Não foi possível realizar a consulta. Usuário não encontrado para o Id informado. ");

        } else {
            lancamentoFiltro.setUsuario(usuario.get());
        }

        List<Lancamento> lancamentos = lancamentoService.buscar(lancamentoFiltro);
        return ResponseEntity.ok(lancamentos);

    }


    @PostMapping
    public ResponseEntity salvar (@RequestBody LancamentoDto lancamentoDto){

        try{
            Lancamento entidade = converter(lancamentoDto);
            entidade = lancamentoService.salvar(entidade);
            return new ResponseEntity(entidade, HttpStatus.CREATED);
        } catch (RegraNegocioException e){
            return ResponseEntity.badRequest().body((e.getMessage()));
        }

    }

    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody LancamentoDto lancamentoDto){

        return lancamentoService.oberPorId(id).map(entity -> {
            try{
                Lancamento lancamento = converter(lancamentoDto);
                lancamento.setId(entity.getId());
                lancamentoService.atualizar(lancamento);
                return ResponseEntity.ok(lancamento);
            }catch (RegraNegocioException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }

        }).orElseGet( () -> new ResponseEntity("Lançamento não encontrado na base de Dados.", HttpStatus.BAD_REQUEST));

    }

    @PutMapping("{id}/atualiza-status")
    public ResponseEntity atualizaStatus( @PathVariable("id") Long id, @RequestBody AtualizaStatusDto atualizaStatusDto) {
        return lancamentoService.oberPorId(id).map( entity -> {
            StatusLancamento statusLancamentoSelecionado = StatusLancamento.valueOf(atualizaStatusDto.getStatus());

            if(statusLancamentoSelecionado == null) {
                return ResponseEntity.badRequest().body("Nõ foi possivel atualizar o status do lançamento, envie um satus valído. ");

            }
            try{
                entity.setStatus(statusLancamentoSelecionado);
                lancamentoService.atualizar(entity);
                return ResponseEntity.ok(entity);
            }catch (RegraNegocioException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }

        }).orElseGet( () ->
                new ResponseEntity("Lançamento não encontrado na base de Dados. ", HttpStatus.BAD_REQUEST));
    }

    @DeleteMapping("{id}")
    public ResponseEntity deletar( @PathVariable("id") Long id){
        return lancamentoService.oberPorId(id).map( entidade -> {
            lancamentoService.deletar(entidade);
            return  new ResponseEntity(HttpStatus.NO_CONTENT);
        }).orElseGet( () -> new ResponseEntity("Lançamento não encontrado na base de Dados.", HttpStatus.BAD_REQUEST));
    }

    private Lancamento converter(LancamentoDto lancamentoDto){

        Lancamento lancamento = new Lancamento();
        lancamento.setDescricao(lancamentoDto.getDescricao());
        lancamento.setAno(lancamentoDto.getAno());
        lancamento.setMes(lancamentoDto.getMes());
        lancamento.setValor(lancamentoDto.getValor());

        Usuario usuario = usuarioService
                .obterPorId(lancamentoDto.getIdUsuario())
                .orElseThrow( () -> new RegraNegocioException("Usuário não encontrado para o Id informado. "));

        lancamento.setUsuario(usuario);

        if(lancamentoDto.getTipo() != null){
            lancamento.setTipo(TipoLancamento.valueOf(lancamentoDto.getTipo()));
        }

        if (lancamentoDto.getStatus() != null) {
            lancamento.setStatus(StatusLancamento.valueOf(lancamentoDto.getStatus()));
        }

        return lancamento;
    }
}
