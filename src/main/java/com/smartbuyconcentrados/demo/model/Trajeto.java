package com.smartbuyconcentrados.demo.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "trajetos")
public class Trajeto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User usuario;

    @Column(nullable = false)
    private String origem;

    @Column(nullable = false)
    private String destino;

    @Column(nullable = false)
    private String tipoVeiculo; // CARRO, CAMINHAO_LEVE, CAMINHAO_PESADO

    @Column(nullable = false)
    private String combustivel; // GASOLINA, ETANOL, DIESEL, GNV

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal distanciaKm;

    @Column(nullable = false, precision = 10, scale = 3)
    private BigDecimal emissaoCo2Kg; // CO2 em kg

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal consumoLitros;

    @Column(nullable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();

    // Getters e Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUsuario() { return usuario; }
    public void setUsuario(User usuario) { this.usuario = usuario; }

    public String getOrigem() { return origem; }
    public void setOrigem(String origem) { this.origem = origem; }

    public String getDestino() { return destino; }
    public void setDestino(String destino) { this.destino = destino; }

    public String getTipoVeiculo() { return tipoVeiculo; }
    public void setTipoVeiculo(String tipoVeiculo) { this.tipoVeiculo = tipoVeiculo; }

    public String getCombustivel() { return combustivel; }
    public void setCombustivel(String combustivel) { this.combustivel = combustivel; }

    public BigDecimal getDistanciaKm() { return distanciaKm; }
    public void setDistanciaKm(BigDecimal distanciaKm) { this.distanciaKm = distanciaKm; }

    public BigDecimal getEmissaoCo2Kg() { return emissaoCo2Kg; }
    public void setEmissaoCo2Kg(BigDecimal emissaoCo2Kg) { this.emissaoCo2Kg = emissaoCo2Kg; }

    public BigDecimal getConsumoCombustivel() { return consumoLitros; }
    public void setConsumoCombustivel(BigDecimal consumoLitros) { this.consumoLitros = consumoLitros; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
}