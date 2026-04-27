package com.smartbuyconcentrados.demo.repository;

import com.smartbuyconcentrados.demo.model.Lista;
import com.smartbuyconcentrados.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ListaRepository extends JpaRepository<Lista, Long> {
    List<Lista> findByUsuario(User usuario);
}