package com.tfsao.sigees;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ScannerUtils {
    
    private ScannerUtils() {
    }
    
    public static int leiaProximoIntPositivo(Scanner scanner) {
        return leiaProximoInt(scanner, 0, Integer.MAX_VALUE);
    }
    
    public static int leiaProximoInt(Scanner scanner, int min, int max) {
        boolean leuValor = false;
        int valor = 0;
        
        do {
            try {
                valor = scanner.nextInt();
                scanner.nextLine();
                
                if (valor < min || valor > max) {
                    System.out.printf("Valor deve estar entre %d e %d!%n", min, max);
                    System.out.printf("Insira o valor: ");
                } else {
                    leuValor = true;    
                }
            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.println("Valor deve ser um número!");
                System.out.printf("Insira o valor: ");
            }
        } while (!leuValor);
        
        return valor;
    }
    
    public static String leiaProximaLinha(Scanner scanner) {
        boolean leuValor = false;
        String valor = null;
        
        do {
            valor = scanner.nextLine();

            if (valor.isBlank()) {
                System.out.println("Valor não pode ser vazio!");
                System.out.printf("Insira o valor: ");
            } else {
                leuValor = true;    
            }
        } while (!leuValor);
        
        return valor;
    }
    
    public static double leiaProximoDoublePositivo(Scanner scanner) {
        boolean leuValor = false;
        double valor = 0.0;
        
        do {
            try {
                valor = scanner.nextDouble();
                scanner.nextLine();
                
                if (valor < 0.0) {
                    System.out.println("Valor não pode ser negativo!");
                    System.out.printf("Insira o valor: ");
                } else {
                    leuValor = true;    
                }
            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.println("Valor deve ser um número!");
                System.out.printf("Insira o valor: ");
            }
        } while (!leuValor);
        
        return valor;
    }
}
