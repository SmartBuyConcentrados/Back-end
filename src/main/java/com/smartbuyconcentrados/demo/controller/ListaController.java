package com.smartbuyconcentrados.demo.controller;

import com.smartbuyconcentrados.demo.model.ItemLista;
import com.smartbuyconcentrados.demo.service.ListaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/listas")
public class ListaController {

    @Autowired
    private ListaService listaService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("listas", listaService.listarDoUsuario());
        return "listas/index";
    }

    @PostMapping("/criar")
    public String criar(@RequestParam String nome, RedirectAttributes redirectAttributes) {
        try {
            listaService.criarLista(nome);
            redirectAttributes.addFlashAttribute("msg", "Lista criada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao criar lista.");
        }
        return "redirect:/listas";
    }

    @GetMapping("/{id}")
    public String verLista(@PathVariable Long id, Model model) {
        model.addAttribute("lista", listaService.buscarPorId(id));
        model.addAttribute("item", new ItemLista());
        return "listas/detalhes";
    }

    @PostMapping("/{id}/adicionar")
    public String adicionarItem(@PathVariable Long id,
                                @ModelAttribute ItemLista item,
                                RedirectAttributes redirectAttributes) {
        try {
            listaService.adicionarItem(id, item);
            redirectAttributes.addFlashAttribute("msg", "Item adicionado!");
        } catch (Exception e) {
            e.printStackTrace(); // vai mostrar o erro real no console
            redirectAttributes.addFlashAttribute("erro", "Erro: " + e.getMessage());
        }
        return "redirect:/listas/" + id;
    }

    @GetMapping("/{listaId}/remover/{itemId}")
    public String removerItem(@PathVariable Long listaId,
                              @PathVariable Long itemId,
                              RedirectAttributes redirectAttributes) {
        listaService.removerItem(itemId);
        redirectAttributes.addFlashAttribute("msg", "Item removido.");
        return "redirect:/listas/" + listaId;
    }

    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        listaService.deletarLista(id);
        redirectAttributes.addFlashAttribute("msg", "Lista removida.");
        return "redirect:/listas";
    }
}