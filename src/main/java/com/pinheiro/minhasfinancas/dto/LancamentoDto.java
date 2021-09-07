package com.pinheiro.minhasfinancas.dto;

import com.pinheiro.minhasfinancas.enums.StatusLancamento;
import com.pinheiro.minhasfinancas.enums.TipoLancamento;
import com.pinheiro.minhasfinancas.model.Usuario;
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

    private String tipoLancamento;

    private String statusLancamento;

    private String descricao;
}
