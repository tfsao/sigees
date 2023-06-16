package com.tfsao.sigees;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDao implements Closeable {
    
    private static final String URL = 
            "jdbc:postgresql://127.0.0.1:5432/sigees";
    private static final String USUARIO = "dev";
    private static final String SENHA = "dev";
    
    private static final String SQL_INSERIR_PRODUTO = "INSERT INTO produtos "
            + "(nome, preco, quantidade) VALUES (?, ?, ?);";
    private static final String SQL_ATUALIZAR_PRODUTO = "UPDATE produtos "
            + "SET nome = ?, preco = ?, quantidade = ? "
            + "WHERE id = ?;";
    private static final String SQL_LISTAR_PRODUTOS = "SELECT * FROM produtos;";
    private static final String SQL_LISTAR_PRODUTOS_BAIXO_ESTOQUE = "SELECT * FROM produtos p "
            + "WHERE p.quantidade < ?;";
    private static final String SQL_CONSULTAR_PRODUTO = "SELECT * FROM produtos p "
            + "WHERE p.id = ?;";
    private static final String SQL_DELETAR_PRODUTO = "DELETE FROM produtos p "
            + "WHERE p.id = ?;";
    
    private Connection connection;
    
    public ProdutoDao() {
        try {
            this.connection = DriverManager.getConnection(URL, USUARIO, SENHA);
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível conectar ao banco.", e);
        }
    }
    
    public Integer salveProduto(Produto produto) {
        if (produto == null) {
            throw new NullPointerException("produto não pode ser nulo.");
        }
        
        Integer idProduto = null;
        
        if (produto.getId() == null) {
            idProduto = insiraProduto(produto);
        } else {
            idProduto = atualizeProduto(produto);
        }
        
        return idProduto;
    }
    
    private Integer insiraProduto(Produto produto) {
        Integer idProduto = null;
        
        try (PreparedStatement statement = connection.prepareStatement(
                SQL_INSERIR_PRODUTO, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);
            
            statement.setString(1, produto.getNome());
            statement.setDouble(2, produto.getPreco());
            statement.setInt(3, produto.getQuantidade());
            
            int linhasAfetadas = statement.executeUpdate();
            
            if (linhasAfetadas > 0) {
                try (ResultSet resultSet = statement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        idProduto = resultSet.getInt(1);
                    }
                }
            }
            
            connection.commit();
        } catch (SQLException e) {
            System.out.println("Não foi possível concluir a operação!");
            System.err.printf("SQL State: %s%n%s%n", e.getSQLState(), e.getMessage());
            rollback();
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.printf("SQL State: %s%n%s%n", e.getSQLState(), e.getMessage());
            }
        }
        
        return idProduto;
    }
    
    private Integer atualizeProduto(Produto produto) {
        Integer idProduto = null;
        
        try (PreparedStatement statement = connection.prepareStatement(SQL_ATUALIZAR_PRODUTO)) {
            connection.setAutoCommit(false);
            
            statement.setString(1, produto.getNome());
            statement.setDouble(2, produto.getPreco());
            statement.setInt(3, produto.getQuantidade());
            statement.setInt(4, produto.getId());
            
            statement.executeUpdate();
      
            idProduto = produto.getId();
            
            connection.commit();
        } catch (SQLException e) {
            System.out.println("Não foi possível concluir a operação!");
            System.err.printf("SQL State: %s%n%s%n", e.getSQLState(), e.getMessage());
            rollback();
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.printf("SQL State: %s%n%s%n", e.getSQLState(), e.getMessage());
            }
        }
        
        return idProduto;
    }
    
    public List<Produto> acheTodosOsProdutos() {
        List<Produto> produtos = new ArrayList<>();
        
        try (Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(SQL_LISTAR_PRODUTOS)) {
            connection.setAutoCommit(false);
            
            while (resultSet.next()) {
                Produto produto = new Produto();
                produto.setId(resultSet.getInt(1));
                produto.setNome(resultSet.getString(2));
                produto.setPreco(resultSet.getDouble(3));
                produto.setQuantidade(resultSet.getInt(4));

                produtos.add(produto);
            }
            
            connection.commit();
        } catch (SQLException e) {
            System.out.println("Não foi possível concluir a operação!");
            System.err.printf("SQL State: %s%n%s%n", e.getSQLState(), e.getMessage());
            rollback();
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.printf("SQL State: %s%n%s%n", e.getSQLState(), e.getMessage());
            }
        }
        
        return produtos;
    }
    
    public Produto acheProdutoPeloId(int idProduto) {
        Produto produto = null;
        
        try (PreparedStatement statement = connection.prepareStatement(SQL_CONSULTAR_PRODUTO)) {
            statement.setInt(1, idProduto);
            connection.setAutoCommit(false);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    produto = new Produto();
                    produto.setId(resultSet.getInt(1));
                    produto.setNome(resultSet.getString(2));
                    produto.setPreco(resultSet.getDouble(3));
                    produto.setQuantidade(resultSet.getInt(4));
                }
            }
            
            connection.commit();
        } catch (SQLException e) {
            System.out.println("Não foi possível concluir a operação!");
            System.err.printf("SQL State: %s%n%s%n", e.getSQLState(), e.getMessage());
            rollback();
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.printf("SQL State: %s%n%s%n", e.getSQLState(), e.getMessage());
            }
        }
        
        return produto;
    }
    
    public List<Produto> acheTodosProdutosComEstoqueAbaixoDoValorMinimo(int valorMinimo) {
        List<Produto> produtos = new ArrayList<>();
        
        try (PreparedStatement statement = connection.prepareStatement(
                SQL_LISTAR_PRODUTOS_BAIXO_ESTOQUE)) {
            connection.setAutoCommit(false);
            
            statement.setInt(1, valorMinimo);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Produto produto = new Produto();
                    produto.setId(resultSet.getInt(1));
                    produto.setNome(resultSet.getString(2));
                    produto.setPreco(resultSet.getDouble(3));
                    produto.setQuantidade(resultSet.getInt(4));
                    
                    produtos.add(produto);
                }
            }
            
            connection.commit();
        } catch (SQLException e) {
            System.out.println("Não foi possível concluir a operação!");
            System.err.printf("SQL State: %s%n%s%n", e.getSQLState(), e.getMessage());
            rollback();
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.printf("SQL State: %s%n%s%n", e.getSQLState(), e.getMessage());
            }
        }
        
        return produtos;
    }
    
    public void excluaProdutoPeloId(int idProduto) {
        try (PreparedStatement statement = connection.prepareStatement(SQL_DELETAR_PRODUTO)) {
            connection.setAutoCommit(false);
            
            statement.setInt(1, idProduto);
            
            statement.executeUpdate();
            
            connection.commit();
        } catch (SQLException e) {
            System.out.println("Não foi possível concluir a operação!");
            System.err.printf("SQL State: %s%n%s%n", e.getSQLState(), e.getMessage());
            rollback();
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.printf("SQL State: %s%n%s%n", e.getSQLState(), e.getMessage());
            }
        }
    }

    @Override
    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new IllegalStateException("Não foi possível fechar a conexão!");
            }
        }
    }
    
    private void rollback() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            System.err.printf("SQL State: %s%n%s%n", e.getSQLState(), e.getMessage());
        }
    }
}
