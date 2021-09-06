package com.pinheiro.minhasfinancas.service;

import com.pinheiro.minhasfinancas.enums.StatusLancamento;
import com.pinheiro.minhasfinancas.model.Lancamento;

import java.util.List;

public interface LancamentoService {

    Lancamento salvar(Lancamento pLancamento);

    Lancamento atualizar(Lancamento  pLancamento);

    void deletar(Lancamento pLancamento);

    List<Lancamento> buscar(Lancamento pLancamentoFiltro);

    void atualizarStatus(Lancamento pLancamento, StatusLancamento status);

    void validar(Lancamento pLancamento);

}
