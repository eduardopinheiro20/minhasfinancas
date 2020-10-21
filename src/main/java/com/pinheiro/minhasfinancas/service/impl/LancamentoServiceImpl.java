package com.pinheiro.minhasfinancas.service.impl;

import com.pinheiro.minhasfinancas.exception.RegraNegocioException;
import com.pinheiro.minhasfinancas.model.entity.Lancamento;
import com.pinheiro.minhasfinancas.model.enums.StatusLancamento;
import com.pinheiro.minhasfinancas.model.enums.TipoLancamento;
import com.pinheiro.minhasfinancas.model.repository.LancamentoRepository;
import com.pinheiro.minhasfinancas.service.LancamentoService;
import com.sun.org.apache.regexp.internal.RESyntaxException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class LancamentoServiceImpl implements LancamentoService {

    private LancamentoRepository lancamentoRepository;

    public LancamentoServiceImpl(LancamentoRepository lancamentoRepository) {
        this.lancamentoRepository = lancamentoRepository;
    }

    @Override
    @Transactional
    public Lancamento salvar(Lancamento lancamento) {
        validar(lancamento);
        lancamento.setStatus(StatusLancamento.PENDENTE);
        return lancamentoRepository.save(lancamento);
    }

    @Override
    @Transactional
    public Lancamento atualizar(Lancamento lancamento) {
        validar(lancamento);
        Objects.requireNonNull(lancamento.getId());
        return lancamentoRepository.save(lancamento);
    }

    @Override
    @Transactional
    public void deletar(Lancamento lancamento) {
        Objects.requireNonNull(lancamento.getId());
        lancamentoRepository.delete(lancamento);
    }

    @Override
    @Transactional
    public List<Lancamento> buscar(Lancamento lancamentoFiltro) {

        Example example = Example.of(lancamentoFiltro,
                ExampleMatcher.matching()
                        .withIgnoreCase()
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));

        return lancamentoRepository.findAll(example);
    }

    @Override
    @Transactional
    public void atualizarStatus(Lancamento lancamento, StatusLancamento status) {
        lancamento.setStatus(status);
        atualizar(lancamento);
    }

    @Override
    public void validar(Lancamento lancamento) {

        if (lancamento.getDescricao() == null || lancamento.getDescricao().trim().equals("")) {
            throw new RegraNegocioException("Informe uma Descrição válida. ");
        }

        if (lancamento.getMes() == null || lancamento.getMes() < 1 || lancamento.getMes() > 12) {
            throw new RegraNegocioException("Informe um Mês válido. ");
        }

        if (lancamento.getAno() == null || lancamento.getAno().toString().length() != 4) {
            throw new RegraNegocioException("Informe um Ano válido. ");
        }

        if (lancamento.getUsuario() == null || lancamento.getUsuario().getId() == null) {
            throw new RegraNegocioException("Informe um Usuário");
        }

        if (lancamento.getValor() == null || lancamento.getValor().compareTo(BigDecimal.ZERO) < 1) {
            throw new RegraNegocioException("Informe um Valor válido. ");
        }

        if (lancamento.getTipo() == null) {
            throw new RegraNegocioException("Informe um Tipo de Lançamento");
        }
    }

    @Override
    public Optional<Lancamento> oberPorId(Long id) {
        return lancamentoRepository.findById(id);
    }

    @Override
    @Transactional
    public BigDecimal obterSaldoPorUsuario(Long id) {

        BigDecimal receitas = lancamentoRepository.obterSaldoPorTipoLancamentoUsuario(id, TipoLancamento.RECEITA);
        BigDecimal despesas = lancamentoRepository.obterSaldoPorTipoLancamentoUsuario(id, TipoLancamento.DESPESA);

        if (receitas == null) {
            receitas = BigDecimal.ZERO;

        }
        if (despesas == null) {
            despesas = BigDecimal.ZERO;
        }

        return receitas.subtract(despesas);
    }
}
