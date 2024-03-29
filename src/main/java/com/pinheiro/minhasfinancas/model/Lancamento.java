package com.pinheiro.minhasfinancas.model;

import com.pinheiro.minhasfinancas.enums.StatusLancamento;
import com.pinheiro.minhasfinancas.enums.TipoLancamento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Table( name = "lancamento", schema = "financas")
@Entity
@Data
@Builder
public class Lancamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer mes;

    private Integer ano;

    @ManyToOne
    private Usuario usuario;

    private BigDecimal valor;

    @Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
    private LocalDate dataCadastro;

    @Enumerated(value = EnumType.STRING)
    private TipoLancamento tipo;

    @Enumerated(value = EnumType.STRING)
    private StatusLancamento status;

    private String descricao;

}
