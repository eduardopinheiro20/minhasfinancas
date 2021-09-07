package com.pinheiro.minhasfinancas.service.impl;

import com.pinheiro.minhasfinancas.enums.StatusLancamento;
import com.pinheiro.minhasfinancas.exception.RegraNegocioException;
import com.pinheiro.minhasfinancas.model.Lancamento;
import com.pinheiro.minhasfinancas.repository.LancamentoRepository;
import com.pinheiro.minhasfinancas.service.LancamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class LancamentoServiceImpl implements LancamentoService {

    @Autowired
    private LancamentoRepository lancamentoRepository;


    @Override
    @Transactional
    public Lancamento salvar(final Lancamento pLancamento) {
        validar(pLancamento);
        pLancamento.setStatusLancamento(StatusLancamento.PEDENTE);
        return lancamentoRepository.save(pLancamento);
    }

    @Override
    @Transactional
    public Lancamento atualizar(final Lancamento pLancamento) {
        Objects.requireNonNull(pLancamento.getId());
        validar(pLancamento);
        return lancamentoRepository.save(pLancamento);
    }

    @Override
    @Transactional
    public void deletar(final Lancamento pLancamento) {
        Objects.requireNonNull(pLancamento.getId());
        lancamentoRepository.delete(pLancamento);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Lancamento> buscar(final Lancamento pLancamentoFiltro) {
        Example example = Example.of(pLancamentoFiltro,
                        ExampleMatcher.matching()
                                        .withIgnoreCase()
                                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));

        return lancamentoRepository.findAll(example);
    }

    @Override
    public void atualizarStatus(final Lancamento pLancamento, final StatusLancamento status) {
        pLancamento.setStatusLancamento(status);
        atualizar(pLancamento);
    }

    @Override
    public void validar(final Lancamento pLancamento) {

        if(pLancamento.getDescricao() == null || pLancamento.getDescricao().trim().equals("")) {
            throw new RegraNegocioException("Informe uma Descrição valida");
        }

        if(pLancamento.getMes() == null || pLancamento.getMes() < 1 || pLancamento.getMes() > 12) {
            throw new RegraNegocioException("Informe um Mês Válido.");
        }

        if(pLancamento.getAno() == null || pLancamento.getAno().toString().length() != 4 ) {
            throw new RegraNegocioException("Informe um Ano Valido.");
        }

        if(pLancamento.getUsuario() == null || pLancamento.getUsuario().getId() == null) {
            throw new RegraNegocioException("Informe um Usuário.");
        }

        if(pLancamento.getValor() == null || pLancamento.getValor().compareTo(BigDecimal.ZERO) < 1 ) {
            throw new RegraNegocioException("Informe um Valor Valído.");
        }

        if(pLancamento.getTipoLancamento() == null) {
            throw new RegraNegocioException("Informe um Tipo de Lançamento.");
        }
    }

    @Override
    public Optional<Lancamento> getById(Long id) {
        return lancamentoRepository.findById(id);
    }
}
