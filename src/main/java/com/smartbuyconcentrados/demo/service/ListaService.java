package com.smartbuyconcentrados.demo.service;

import com.smartbuyconcentrados.demo.model.ItemLista;
import com.smartbuyconcentrados.demo.model.Lista;
import com.smartbuyconcentrados.demo.model.User;
import com.smartbuyconcentrados.demo.repository.ItemListaRepository;
import com.smartbuyconcentrados.demo.repository.ListaRepository;
import com.smartbuyconcentrados.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListaService {

    @Autowired
    private ListaRepository listaRepository;

    @Autowired
    private ItemListaRepository itemListaRepository;

    @Autowired
    private UserRepository userRepository;

    private User getUsuarioLogado() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
    }

    public List<Lista> listarDoUsuario() {
        return listaRepository.findByUsuario(getUsuarioLogado());
    }

    public Lista buscarPorId(Long id) {
        return listaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lista não encontrada."));
    }

    public void criarLista(String nome) {
        Lista lista = new Lista();
        lista.setNome(nome);
        lista.setUsuario(getUsuarioLogado());
        listaRepository.save(lista);
    }

    public void adicionarItem(Long listaId, ItemLista item) {
        Lista lista = buscarPorId(listaId);
        item.setId(null);
        item.setLista(lista);
        itemListaRepository.save(item);
    }

    public void removerItem(Long itemId) {
        itemListaRepository.deleteById(itemId);
    }

    public void deletarLista(Long id) {
        listaRepository.deleteById(id);
    }
}