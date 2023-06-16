package com.tfsao.sigees;

import java.util.Objects;

public class Produto {
    
    private Integer id;
    private String nome;
    private double preco;
    private int quantidade;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null ) {
            throw new NullPointerException("nome não pode ser nulo.");
        }
        if (nome.isBlank()) {
            throw new IllegalArgumentException("nome não pode ser em branco.");
        }
        this.nome = nome;
    }
    
    public double getPreco() {
        return preco;
    }

    public void setPreco(double preço) {
        if (preco < 0) {
            throw new IllegalArgumentException("preço não pode ser negativo.");
        }
        this.preco = preço;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        if (quantidade < 0) {
            throw new IllegalArgumentException("quantidade não pode ser negativa.");
        }
        this.quantidade = quantidade;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Produto other = (Produto) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return String.format(
                "id: %d - nome: %s preço: R$ %.2f quantidade: %d",
                id,
                nome,
                preco,
                quantidade);
        
    }
}
