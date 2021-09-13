package com.pinheiro.minhasfinancas.service;

import com.pinheiro.minhasfinancas.enums.StatusLancamento;
import com.pinheiro.minhasfinancas.model.Lancamento;

import java.util.List;
import java.util.Optional;

public interface LancamentoService {

    Lancamento salvar(Lancamento pLancamento);

    Lancamento atualizar(Lancamento pLancamento);

    void deletar(Lancamento pLancamento);

    List<Lancamento> buscar(Lancamento pLancamentoFiltro);

    void atualizarStatus(Lancamento pLancamento, StatusLancamento status);

    void validar(Lancamento pLancamento);

    Optional<Lancamento> getById(Long id);
}
