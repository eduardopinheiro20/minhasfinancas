package com.pinheiro.minhasfinancas.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class LancamentoDto {


    private Long id;

    private Integer mes;

    private Integer ano;

    private Long usuario;

    private BigDecimal valor;

    private LocalDate dataCadastro;

    private String tipo;

    private String status;

    private String descricao;
}
