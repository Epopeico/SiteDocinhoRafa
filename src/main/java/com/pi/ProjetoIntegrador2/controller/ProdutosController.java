package com.pi.ProjetoIntegrador2.controller;

import com.pi.ProjetoIntegrador2.model.Produto;
import com.pi.ProjetoIntegrador2.model.ProdutoDto;
import com.pi.ProjetoIntegrador2.services.ProdutoRepository;
import jakarta.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/produto")
public class ProdutosController {

    @Autowired
    private ProdutoRepository repo;

    
    @GetMapping({"", "/"})
    public String showProductList(Model model) {
        List<Produto> produtos = repo.findAll();
        model.addAttribute("produtos", produtos);
        return "lista";
    }
    
    
    @GetMapping("/cadastrar")
    public String showPaginaCadastro(Model model) {
        ProdutoDto produtoDto = new ProdutoDto();
        model.addAttribute("produtoDto", produtoDto);
        return "cadastrar";
    }

    @PostMapping("/cadastrar")
    public String criarProduto(
            @Valid @ModelAttribute ProdutoDto produtoDto,
            BindingResult result
    ) {
        if (produtoDto.getCampoImagem().isEmpty()) {
            result.addError(new FieldError("produtoDto", "campoImagem", "O campo da imagem está vazio"));
        }
        if (result.hasErrors()) {
            System.out.println("Erros encontrados no formulário: " + result.getAllErrors());
            return "cadastrar";
        }

        
        MultipartFile imagem = produtoDto.getCampoImagem();
        String storageFileName = imagem.getOriginalFilename();
        try {
            String uploadDir = "src/main/resources/static/imagem/";
            Path uploadPath = Paths.get(uploadDir + storageFileName);

            
            if (!Files.exists(uploadPath.getParent())) {
                Files.createDirectories(uploadPath.getParent());
            }

            
            Files.copy(imagem.getInputStream(), uploadPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            System.out.println("Erro ao salvar a imagem: " + ex.getMessage());
        }

        Produto produto = new Produto();
        produto.setNome(produtoDto.getNome());
        produto.setSabor(produtoDto.getSabor()); 
        produto.setCategoria(produtoDto.getCategoria());
        produto.setPreco(produtoDto.getPreco());
        produto.setDescricao(produtoDto.getDescricao());
        produto.setImagemPath(storageFileName); 

        System.out.println("Salvando produto: " + produto.getNome() + ", Categoria: " + produto.getCategoria());

        repo.save(produto);

        return "redirect:/produto";
    }
    
    
    @GetMapping("/edit")
    public String showPaginaEdicao(
        Model model,
        @RequestParam int id) { 
        
        try {
            Produto produto = repo.findById(id).get();
            model.addAttribute("produto" , produto);
            
        ProdutoDto produtoDto = new ProdutoDto();
        produtoDto.setNome(produto.getNome());
        produtoDto.setSabor(produto.getSabor()); 
        produtoDto.setCategoria(produto.getCategoria());
        produtoDto.setPreco(produto.getPreco());
        produtoDto.setDescricao(produto.getDescricao());
        
        model.addAttribute("produtoDto", produtoDto);       
        }
        catch (Exception ex) {
        System.out.println(" Exception:" + ex.getMessage());
        
        return "redirect:/produto";
    }
        return "produtos/editProduto";
    }
 
   

@GetMapping("/excluir/{id}")
public String excluirProduto(@PathVariable("id") int id) {
    Produto produto = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Produto inválido:" + id));
    repo.delete(produto);
    return "redirect:/produto";
}
 
// Exibe o formulário de edição do produto
@RequestMapping(value = "editar/{id}", method = RequestMethod.GET)
public String exibirFormularioEdicao(@PathVariable int id, Model model) {
    System.out.println("Chamando o formulário de edição para o produto com ID: " + id);
    
    // Busca o produto pelo ID
    Produto produtoExistente = repo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Produto inválido: " + id));

    // Cria um ProdutoDto a partir do produto existente
    ProdutoDto produtoDto = new ProdutoDto();
    produtoDto.setNome(produtoExistente.getNome());
    produtoDto.setSabor(produtoExistente.getSabor());
    produtoDto.setCategoria(produtoExistente.getCategoria());
    produtoDto.setPreco(produtoExistente.getPreco());
    produtoDto.setDescricao(produtoExistente.getDescricao());

    // Adiciona o ProdutoDto ao modelo
    model.addAttribute("produtoDto", produtoDto);
    model.addAttribute("id", id);

    // Retorna o nome da página de edição (editarProduto.html)
    return "editarProduto";
}

// Processa a submissão do formulário de edição
@RequestMapping(value = "editar/{id}", method = RequestMethod.POST)
public String editarProduto(
        @PathVariable int id, 
        @Valid @ModelAttribute ProdutoDto produtoDto, 
        BindingResult result, 
        Model model) {

    // Verificar se há erros de validação
    if (result.hasErrors()) {
        // Se houver erros, retornar para a página de edição com mensagens de erro
        model.addAttribute("produtoDto", produtoDto);
        return "editarProduto";
    }

    // Busca e atualiza o produto
    Produto produtoExistente = repo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Produto inválido: " + id));
    produtoExistente.setNome(produtoDto.getNome());
    produtoExistente.setSabor(produtoDto.getSabor());
    produtoExistente.setCategoria(produtoDto.getCategoria());
    produtoExistente.setPreco(produtoDto.getPreco());
    produtoExistente.setDescricao(produtoDto.getDescricao());
    
     // Verifica se uma nova imagem foi enviada
    if (!produtoDto.getCampoImagem().isEmpty()) {
        MultipartFile imagem = produtoDto.getCampoImagem();
        String storageFileName = imagem.getOriginalFilename();
        try {
            String uploadDir = "src/main/resources/static/imagem/";
            Path uploadPath = Paths.get(uploadDir + storageFileName);

            if (!Files.exists(uploadPath.getParent())) {
                Files.createDirectories(uploadPath.getParent());
            }

            Files.copy(imagem.getInputStream(), uploadPath, StandardCopyOption.REPLACE_EXISTING);
            produtoExistente.setImagemPath(storageFileName); // Atualiza o caminho da imagem
        } catch (IOException ex) {
            System.out.println("Erro ao salvar a imagem: " + ex.getMessage());
        }
    }

    
   

    // Salva o produto atualizado no banco de dados
    repo.save(produtoExistente);

    // Redireciona para a página de lista de produtos
    return "redirect:/produto";
}
    
       
}




