package com.smartbuyconcentrados.demo.repository;

import com.smartbuyconcentrados.demo.model.ItemLista;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemListaRepository extends JpaRepository<ItemLista, Long> {
}