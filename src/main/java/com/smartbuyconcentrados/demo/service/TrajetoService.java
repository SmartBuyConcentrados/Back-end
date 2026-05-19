package com.smartbuyconcentrados.demo.service;

import com.smartbuyconcentrados.demo.model.Trajeto;
import com.smartbuyconcentrados.demo.model.User;
import com.smartbuyconcentrados.demo.repository.TrajetoRepository;
import com.smartbuyconcentrados.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class TrajetoService {

    @Autowired
    private TrajetoRepository trajetoRepository;

    @Autowired
    private UserRepository userRepository;

    // Consumo médio em km/L por tipo de veículo e combustível
    // Fontes: INMETRO / DENATRAN / EPA
    private BigDecimal getConsumoMedio(String tipoVeiculo, String combustivel) {
        return switch (tipoVeiculo + "_" + combustivel) {
            case "CARRO_GASOLINA"       -> new BigDecimal("12.0");
            case "CARRO_ETANOL"         -> new BigDecimal("8.5");
            case "CARRO_DIESEL"         -> new BigDecimal("14.0");
            case "CARRO_GNV"            -> new BigDecimal("11.0");
            case "CAMINHAO_LEVE_DIESEL" -> new BigDecimal("8.0");
            case "CAMINHAO_LEVE_GNV"    -> new BigDecimal("7.0");
            case "CAMINHAO_PESADO_DIESEL"-> new BigDecimal("3.5");
            case "CAMINHAO_PESADO_GNV"  -> new BigDecimal("3.0");
            default                     -> new BigDecimal("10.0");
        };
    }

    // Fator de emissão em kg CO2 por litro (ou m³ para GNV)
    // Fontes: IPCC / MCT Brasil
    private BigDecimal getFatorEmissao(String combustivel) {
        return switch (combustivel) {
            case "GASOLINA" -> new BigDecimal("2.31");  // kg CO2/litro
            case "ETANOL"   -> new BigDecimal("1.48");  // kg CO2/litro (ciclo parcial)
            case "DIESEL"   -> new BigDecimal("2.68");  // kg CO2/litro
            case "GNV"      -> new BigDecimal("1.96");  // kg CO2/m³
            default         -> new BigDecimal("2.31");
        };
    }

    public Trajeto calcular(String origem, String destino, String tipoVeiculo,
                            String combustivel, BigDecimal distanciaKm) {

        BigDecimal consumoMedio = getConsumoMedio(tipoVeiculo, combustivel);
        BigDecimal consumoLitros = distanciaKm.divide(consumoMedio, 3, RoundingMode.HALF_UP);
        BigDecimal fatorEmissao = getFatorEmissao(combustivel);
        BigDecimal emissaoCo2 = consumoLitros.multiply(fatorEmissao).setScale(3, RoundingMode.HALF_UP);

        Trajeto trajeto = new Trajeto();
        trajeto.setOrigem(origem);
        trajeto.setDestino(destino);
        trajeto.setTipoVeiculo(tipoVeiculo);
        trajeto.setCombustivel(combustivel);
        trajeto.setDistanciaKm(distanciaKm);
        trajeto.setConsumoCombustivel(consumoLitros);
        trajeto.setEmissaoCo2Kg(emissaoCo2);

        return trajeto;
    }

    public Trajeto calcularESalvar(String origem, String destino, String tipoVeiculo,
                                   String combustivel, BigDecimal distanciaKm) {
        Trajeto trajeto = calcular(origem, destino, tipoVeiculo, combustivel, distanciaKm);

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
        trajeto.setUsuario(usuario);

        return trajetoRepository.save(trajeto);
    }

    public List<Trajeto> historicoDoUsuario() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
        return trajetoRepository.findByUsuarioOrderByDataCriacaoDesc(usuario);
    }

    public void deletar(Long id) {
        trajetoRepository.deleteById(id);
    }
}