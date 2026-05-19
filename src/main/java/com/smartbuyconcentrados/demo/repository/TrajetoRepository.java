package com.smartbuyconcentrados.demo.repository;

import com.smartbuyconcentrados.demo.model.Trajeto;
import com.smartbuyconcentrados.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TrajetoRepository extends JpaRepository<Trajeto, Long> {
    List<Trajeto> findByUsuarioOrderByDataCriacaoDesc(User usuario);
}