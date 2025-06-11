import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class PageRank {

    // Record para armazenar os resultados de forma organizada
    record Site(String nome, double score) {}

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int escolha = -1;

        while (escolha != 0) {
            exibirMenu();
            try {
                escolha = scanner.nextInt();
                scanner.nextLine(); // Consome a quebra de linha

                switch (escolha) {
                    case 1:
                        resolverProblemaA();
                        break;
                    case 2:
                        resolverProblemaB();
                        break;
                    case 3:
                        resolverProblemaC();
                        break;
                    case 4:
                        receberMatrizDoConsole(scanner);
                        break;
                    case 0:
                        System.out.println("Encerrando o programa...");
                        break;
                    default:
                        System.out.println("Opção inválida! Tente novamente.");
                        break;
                }
                if (escolha != 0) {
                    System.out.print("\nPressione Enter para voltar ao menu...");
                    scanner.nextLine();
                }

            } catch (Exception e) {
                System.out.println("Entrada inválida. Por favor, digite um número.");
                scanner.nextLine(); // Limpa o buffer do scanner
            }
        }
        scanner.close();
    }

    public static void exibirMenu() {
        System.out.println("\n--- MENU DE OPÇÕES - PAGERANK ---");
        System.out.println("1. Resolver Problema A (4 sites)");
        System.out.println("2. Resolver Problema B (5 sites)");
        System.out.println("3. Resolver Problema C (10 sites)");
        System.out.println("4. Inserir nova matriz pelo console");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
    }

    // --- Métodos para os Problemas ---

    public static void resolverProblemaA() {
        System.out.println("\n--- Resolvendo Problema A (4 sites) ---");
        double[][] matriz = {
                {0, 1, 1, 0},
                {1, 0, 0, 0},
                {1, 0, 0, 0},
                {1, 0, 0, 0}
        };
        String[] nomes = {"Site 1", "Site 2", "Site 3", "Site 4"};
        calcularEExibirPageRank(matriz, nomes);
    }

    public static void resolverProblemaB() {
        System.out.println("\n--- Resolvendo Problema B (5 sites) ---");
        double[][] matriz = {
                {0, 1, 1, 1, 0},
                {1, 0, 0, 0, 1},
                {1, 0, 0, 0, 1},
                {1, 0, 0, 0, 1},
                {0, 0, 0, 0, 0}
        };
        String[] nomes = {"Site 1", "Site 2", "Site 3", "Site 4", "Site 5"};
        calcularEExibirPageRank(matriz, nomes);
    }

    public static void resolverProblemaC() {
        System.out.println("\n--- Resolvendo Problema C (10 sites) ---");
        double[][] matriz = {
                {0, 0, 0, 0, 1, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
                {0, 1, 0, 1, 0, 0, 0, 0, 0, 0},
                {0, 0, 1, 0, 0, 0, 1, 0, 0, 0},
                {0, 0, 0, 1, 0, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 1, 0, 0, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 1, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 1, 0}
        };
        String[] nomes = {"Site 1", "Site 2", "Site 3", "Site 4", "Site 5", "Site 6", "Site 7", "Site 8", "Site 9", "Site 10"};
        calcularEExibirPageRank(matriz, nomes);
    }

    // --- Lógica de Cálculo e Input Manual ---

    public static void receberMatrizDoConsole(Scanner scanner) {
        System.out.println("\n--- Inserção de Nova Matriz ---");
        System.out.print("Digite o número total de sites: ");
        int n = scanner.nextInt();
        scanner.nextLine();

        if (n <= 0) {
            System.out.println("Número de sites deve ser positivo.");
            return;
        }

        String[] nomesSites = new String[n];
        for (int i = 0; i < n; i++) {
            System.out.printf("Nome do Site %d: ", i + 1);
            nomesSites[i] = scanner.nextLine();
        }

        double[][] matrizConexoes = new double[n][n];
        System.out.println("\nPara cada site, informe os NÚMEROS dos sites para os quais ele aponta (separados por espaço).");
        for (int j = 0; j < n; j++) {
            System.out.printf("Para o Site %d (%s), digite os destinos: ", j + 1, nomesSites[j]);
            String linha = scanner.nextLine();
            if (!linha.trim().isEmpty()) {
                String[] destinos = linha.split("\\s+");
                for (String destinoStr : destinos) {
                    try {
                        int i = Integer.parseInt(destinoStr) - 1;
                        if (i >= 0 && i < n) {
                            matrizConexoes[i][j] = 1;
                        }
                    } catch (NumberFormatException e) {
                        // Ignora entradas que não são números
                    }
                }
            }
        }
        calcularEExibirPageRank(matrizConexoes, nomesSites);
    }

    public static void calcularEExibirPageRank(double[][] matrizConexoes, String[] nomesSites) {
        int n = matrizConexoes.length;
        double d = 0.85;
        double tolerancia = 1e-5;

        double[][] M = new double[n][n];
        double[] totalSaidas = new double[n];
        for (int j = 0; j < n; j++) {
            for (int i = 0; i < n; i++) totalSaidas[j] += matrizConexoes[i][j];
        }
        for (int j = 0; j < n; j++) {
            if (totalSaidas[j] == 0) {
                for (int i = 0; i < n; i++) M[i][j] = 1.0 / n;
            } else {
                for (int i = 0; i < n; i++) M[i][j] = matrizConexoes[i][j] / totalSaidas[j];
            }
        }

        double[][] matrizGoogle = new double[n][n];
        double fatorSaltoAleatorio = (1.0 - d) / n;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrizGoogle[i][j] = d * M[i][j] + fatorSaltoAleatorio;
            }
        }

        double[] rank = new double[n];
        Arrays.fill(rank, 1.0 / n);
        while (true) {
            double[] rankAnterior = rank.clone();
            for (int i = 0; i < n; i++) {
                double soma = 0;
                for (int j = 0; j < n; j++) soma += matrizGoogle[i][j] * rankAnterior[j];
                rank[i] = soma;
            }
            double diferenca = 0;
            for (int i = 0; i < n; i++) diferenca += Math.abs(rank[i] - rankAnterior[i]);
            if (diferenca < tolerancia) break;
        }

        List<Site> resultados = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            resultados.add(new Site(nomesSites[i], rank[i]));
        }
        resultados.sort(Comparator.comparingDouble(Site::score).reversed());

        System.out.println("\n--- Ranking Final dos Sites ---");
        System.out.printf("%-10s %-20s %-20s\n", "Posição", "Site", "Score de Relevância");
        System.out.println("-".repeat(55));
        for (int i = 0; i < resultados.size(); i++) {
            Site site = resultados.get(i);
            System.out.printf("%-10d %-20s %.8f\n", (i + 1), site.nome(), site.score());
        }
    }
}
