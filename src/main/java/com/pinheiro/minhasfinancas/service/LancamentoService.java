package com.pinheiro.minhasfinancas.service;

import com.pinheiro.minhasfinancas.model.entity.Lancamento;
import com.pinheiro.minhasfinancas.model.enums.StatusLancamento;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface LancamentoService {

    Lancamento salvar(Lancamento lancamento);

    Lancamento atualizar(Lancamento lancamento);

    void deletar(Lancamento lancamento);

    List<Lancamento> buscar (Lancamento lancamentoFiltro);

    void atualizarStatus(Lancamento lancamento, StatusLancamento status);

    void validar(Lancamento lancamento);

    Optional<Lancamento> oberPorId(Long id);

    BigDecimal obterSaldoPorUsuario(Long id);

}
