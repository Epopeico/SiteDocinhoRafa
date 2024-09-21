package com.pi.ProjetoIntegrador2.controller;

import com.pi.ProjetoIntegrador2.model.Produto;
import com.pi.ProjetoIntegrador2.services.ProdutoService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PaginaWebController {
    
    @Autowired
    private ProdutoService produtoService;
    
    
  @GetMapping("/paginaInicial")
    public String paginaInicial() {
        return "paginaInicial"; 
    }
    
    @GetMapping("/copoFelicidade")
    public String copoFelicidade(Model model) {
        List<Produto> produtos = produtoService.buscarPorCategoria("Copo da Felicidade");
        model.addAttribute("produtos", produtos);
        return "copoFelicidade";
    }
    
    @GetMapping("/docesTradicionais")
    public String docesTradicionais(Model model) {
        List<Produto> produtos = produtoService.buscarPorCategoria("Doce tradicional");
        model.addAttribute("produtos", produtos);
        return "docesTradicionais";
    }
    
    @GetMapping("/palhaItaliana")
    public String palhaItaliana(Model model) {
        List<Produto> produtos = produtoService.buscarPorCategoria("Palha italiana");
        model.addAttribute("produtos", produtos);
        return "palhaItaliana";
    }
    
}
