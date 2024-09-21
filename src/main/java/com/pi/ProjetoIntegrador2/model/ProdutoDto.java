package com.pi.ProjetoIntegrador2.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import org.springframework.web.multipart.MultipartFile;

public class ProdutoDto {

    @NotEmpty(message = "O nome é obrigatório")
    private String nome;

    @NotEmpty(message = "A categoria é obrigatória")
    private String categoria;
    
    @NotEmpty(message = "O sabor é obrigatório")
    private String sabor; 

    @Min(value = 0, message = "O preço deve ser um valor positivo")
    private BigDecimal preco; // Corrigido para double

    @Size(min = 10, max = 200, message = "A descrição deve ter entre 10 e 200 caracteres")
    private String descricao;

    private MultipartFile campoImagem;

    public ProdutoDto() {
    }

    public ProdutoDto(String nome, String categoria, String sabor, BigDecimal preco, String descricao, MultipartFile campoImagem) {
        this.nome = nome;
        this.categoria = categoria;
        this.sabor = sabor;
        this.preco = preco;
        this.descricao = descricao;
        this.campoImagem = campoImagem;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getSabor() {
        return sabor;
    }

    public void setSabor(String sabor) {
        this.sabor = sabor;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public MultipartFile getCampoImagem() {
        return campoImagem;
    }

    public void setCampoImagem(MultipartFile campoImagem) {
        this.campoImagem = campoImagem;
    }


    
}
