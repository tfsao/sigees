package com.tfsao.sigees;

import java.util.List;
import java.util.Scanner;

public class App {
    
    private Scanner scanner;
    private ProdutoDao produtoDao;
    
    public App(Scanner scanner, ProdutoDao produtoDao) {
        this.scanner = scanner;
        this.produtoDao = produtoDao;
    }

    public Scanner getScanner() {
        return scanner;
    }

    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    public ProdutoDao getProdutoDao() {
        return produtoDao;
    }

    public void setProdutoDao(ProdutoDao produtoDao) {
        this.produtoDao = produtoDao;
    }

    public static void main(String[] args) {
        try (ProdutoDao produtoDao = new ProdutoDao(); 
                Scanner scanner = new Scanner(System.in)){
            App app = new App(scanner, produtoDao);
        
            app.execute();
        } catch(Exception e) {
            System.err.printf("%nErro: %s%n", e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void execute() {
        int opcao = 0;
        
        do {
            imprimaMenu();
        
            System.out.printf("%nInsira a opção: ");
            opcao = ScannerUtils.leiaProximoInt(scanner, 0, 7);
            
            switch (opcao) {
                case 1:
                    cadastreNovoProduto();
                    break;
                case 2:
                    listeTodosOsProdutos();
                    break;
                case 3: 
                    encontreProdutoPeloId();
                    break;
                case 4:
                    listeProdutosComBaixoEstoque();
                    break;
                case 5:
                    atualizePrecoDoProduto();
                    break;
                case 6:
                    atualizeQuantidadeDoProduto();
                    break;
                case 7: 
                    excluaProduto();
                    break;
                case 0:
                    System.out.printf("%nSaindo...%n");
                    break;
                default:
                    throw new IllegalArgumentException("Opção inválida!");
                }
        } while (opcao != 0);
    }
    
    private void imprimaMenu() {
        System.out.printf("%nSistema de Gerenciamento de Estoque%n");
        System.out.println("1 - Cadastrar produto");
        System.out.println("2 - Listar todos os produtos");
        System.out.println("3 - Consultar produto por id");
        System.out.println("4 - Listar produtos com pouco estoque");
        System.out.println("5 - Atualizar preço do produto");
        System.out.println("6 - Atualizar quantidade do produto");
        System.out.println("7 - Excluir produto");
        System.out.println("0 - Sair");
    }
    
    private void cadastreNovoProduto() {
        System.out.printf("%nCadastrar produto%n");        
                
        System.out.printf("Insira o nome: ");
        String nome = ScannerUtils.leiaProximaLinha(scanner);

        System.out.printf("Insira o preço: ");
        double preco = ScannerUtils.leiaProximoDoublePositivo(scanner);

        System.out.printf("Insira a quantidade: ");
        int quantidade = ScannerUtils.leiaProximoIntPositivo(scanner);

        Produto produto = new Produto();
        produto.setNome(nome);
        produto.setPreco(preco);
        produto.setQuantidade(quantidade);

        Integer idProduto = produtoDao.salveProduto(produto);

        System.out.println("Produto cadastrado com o id: " + idProduto);
    }
    
    private void listeTodosOsProdutos() {
        System.out.printf("%nListar todos os produtos%n");
        
        List<Produto> produtos = produtoDao.acheTodosOsProdutos();
        
        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto foi encontrado!");
        } else {
            for (Produto produto : produtos) {
                System.out.println(produto);
            }
        }
    }
    
    private void encontreProdutoPeloId() {
        System.out.printf("%nConsultar produto por id%n");
        
        System.out.printf("Insira o id: "); 
        int idProduto = ScannerUtils.leiaProximoIntPositivo(scanner);
        
        Produto produtoEncontrado = produtoDao.acheProdutoPeloId(idProduto);
        
        if (produtoEncontrado != null) {
            System.out.println(produtoEncontrado);
        } else {
            System.out.println("Produto não encontrado!");
        }
    }
    
    private void listeProdutosComBaixoEstoque() {
        System.out.printf("%nListar produtos com pouco estoque%n");
        
        System.out.printf("Insira o estoque mínimo: ");
        int valorMinimo = ScannerUtils.leiaProximoIntPositivo(scanner);
        
        List<Produto> produtos = produtoDao.acheTodosProdutosComEstoqueAbaixoDoValorMinimo(valorMinimo);
        
        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto foi encontrado!");
        } else {
            for (Produto produto : produtos) {
                System.out.println(produto);
            }
        }
    }
    
    private void atualizePrecoDoProduto() {
        System.out.printf("%nAtualizar preço do produto%n");
        
        System.out.printf("Insira o id do produto: ");
        int idProduto = ScannerUtils.leiaProximoIntPositivo(scanner);
        
        System.out.printf("Insira o novo preço: ");
        int novoPreco = ScannerUtils.leiaProximoIntPositivo(scanner);
        
        Produto produto = produtoDao.acheProdutoPeloId(idProduto);
        
        if (produto != null) {
            produto.setPreco(novoPreco);
            
            produtoDao.salveProduto(produto);
            
            System.out.println("Preço atualizado!");
        } else {
            System.out.println("Produto não encontrado!");
        }
    }
    
    private void atualizeQuantidadeDoProduto() {
        System.out.printf("%nAtualizar quantidade do produto%n");
        
        System.out.printf("Insira o id do produto: ");
        int idProduto = ScannerUtils.leiaProximoIntPositivo(scanner);
        
        System.out.printf("Insira a nova quantidade: ");
        int novaQuantidade = ScannerUtils.leiaProximoIntPositivo(scanner);
        
        Produto produto = produtoDao.acheProdutoPeloId(idProduto);
        
        if (produto != null) {
            produto.setQuantidade(novaQuantidade);
            
            produtoDao.salveProduto(produto);
            
            System.out.println("Quantidade atualizada!");
        } else {
            System.out.println("Produto não encontrado!");
        }
    }
    
    private void excluaProduto() {
        System.out.printf("%nExcluir Produto%n");
        
        System.out.printf("Insira o id do produto: ");
        int idProduto = ScannerUtils.leiaProximoIntPositivo(scanner);
        
        Produto produto = produtoDao.acheProdutoPeloId(idProduto);
        
        if (produto != null) {
            produtoDao.excluaProdutoPeloId(idProduto);
            
            System.out.println("Produto excluido com sucesso!");
        } else {
            System.out.println("Produto não encontrado!");
        }
    }
}
