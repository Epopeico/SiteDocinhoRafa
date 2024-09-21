package com.pi.ProjetoIntegrador2.services;

import com.pi.ProjetoIntegrador2.model.Produto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
     List<Produto> findByCategoria(String categoria);

}
