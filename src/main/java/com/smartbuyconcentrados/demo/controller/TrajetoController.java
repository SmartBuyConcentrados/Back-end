package com.smartbuyconcentrados.demo.controller;

import com.smartbuyconcentrados.demo.model.Trajeto;
import com.smartbuyconcentrados.demo.service.TrajetoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/trajetos")
public class TrajetoController {

    @Autowired
    private TrajetoService trajetoService;

    @GetMapping
    public String formulario() {
        return "trajetos/calcular";
    }

    @PostMapping("/calcular")
    public String calcular(@RequestParam String origem,
                           @RequestParam String destino,
                           @RequestParam String tipoVeiculo,
                           @RequestParam String combustivel,
                           @RequestParam BigDecimal distanciaKm,
                           Model model) {
        try {
            Trajeto resultado = trajetoService.calcularESalvar(origem, destino, tipoVeiculo, combustivel, distanciaKm);
            model.addAttribute("resultado", resultado);
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao calcular: " + e.getMessage());
        }
        return "trajetos/calcular";
    }

    @GetMapping("/historico")
    public String historico(Model model) {
        try {
            List<Trajeto> trajetos = trajetoService.historicoDoUsuario();

            BigDecimal totalDistancia = trajetos.stream()
                    .map(Trajeto::getDistanciaKm)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal totalCo2 = trajetos.stream()
                    .map(Trajeto::getEmissaoCo2Kg)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            model.addAttribute("trajetos", trajetos);
            model.addAttribute("totalDistancia", totalDistancia);
            model.addAttribute("totalCo2", totalCo2);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("trajetos", new java.util.ArrayList<>());
            model.addAttribute("totalDistancia", BigDecimal.ZERO);
            model.addAttribute("totalCo2", BigDecimal.ZERO);
            model.addAttribute("erro", "Erro ao carregar histórico: " + e.getMessage());
        }
        return "trajetos/historico";
    }

    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        trajetoService.deletar(id);
        redirectAttributes.addFlashAttribute("msg", "Trajeto removido.");
        return "redirect:/trajetos/historico";
    }
}