package com.pinheiro.minhasfinancas.repository;

import com.pinheiro.minhasfinancas.model.Lancamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {
}
