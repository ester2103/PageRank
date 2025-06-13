import java.util.*;

public class PageRank {

    static final double TOLERANCIA = 1e-5;

    record Site(String nome, double autoridade, double hub) {}

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int escolha = -1;

        while (escolha != 0) {
            exibirMenu();
            try {
                escolha = scanner.nextInt();
                scanner.nextLine();
                switch (escolha) {
                    case 1 -> resolverProblemaA();
                    case 2 -> resolverProblemaB();
                    case 3 -> resolverProblemaC();
                    case 4 -> receberMatrizDoConsole(scanner);
                    case 0 -> System.out.println("Encerrando o programa...");
                    default -> System.out.println("Opção inválida! Tente novamente.");
                }
                if (escolha != 0) {
                    System.out.print("\nPressione Enter para voltar ao menu...");
                    scanner.nextLine();
                }
            } catch (Exception e) {
                System.out.println("Entrada inválida.");
                scanner.nextLine();
            }
        }
        scanner.close();
    }

    public static void exibirMenu() {
        System.out.println("\n--- MENU DE OPÇÕES - HITS ---");
        System.out.println("1. Resolver Problema A (4 sites)");
        System.out.println("2. Resolver Problema B (5 sites)");
        System.out.println("3. Resolver Problema C (10 sites)");
        System.out.println("4. Inserir nova matriz pelo console");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
    }

    public static void resolverProblemaA() {
        double[][] A = {
            {0, 1, 1, 0},
            {0, 0, 1, 0},
            {1, 0, 0, 1},
            {1, 0, 0, 0}
        };
        String[] nomes = {"Site 1", "Site 2", "Site 3", "Site 4"};
        calcularEExibirHITS(A, nomes, "Questão 6");
    }

    public static void resolverProblemaB() {
        double[][] A = {
            {0, 1, 1, 1, 0},
            {1, 0, 0, 0, 1},
            {0, 0, 0, 0, 1},
            {0, 1, 0, 0, 0},
            {0, 1, 1, 0, 0}
        };
        String[] nomes = {"Site 1", "Site 2", "Site 3", "Site 4", "Site 5"};
        calcularEExibirHITS(A, nomes, "Questão 7");
    }

    public static void resolverProblemaC() {
        double[][] A = {
            {0, 1, 1, 0, 1, 1, 0, 0, 0, 1},
            {0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {0, 1, 1, 0, 0, 1, 1, 0, 0, 1},
            {0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
            {0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 1, 0},
            {0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
            {0, 1, 1, 0, 0, 1, 0, 1, 0, 1},
            {0, 0, 0, 0, 0, 1, 0, 0, 0, 0}
        };
        String[] nomes = {
            "Site 1", "Site 2", "Site 3", "Site 4", "Site 5",
            "Site 6", "Site 7", "Site 8", "Site 9", "Site 10"
        };
        calcularEExibirHITS(A, nomes, "Questão 8");
    }

    public static void receberMatrizDoConsole(Scanner scanner) {
        System.out.println("\n>>> Entrada personalizada de matriz de adjacência <<<");
        System.out.print("Quantos sites? ");
        int n = scanner.nextInt();
        scanner.nextLine();

        String[] nomes = new String[n];
        for (int i = 0; i < n; i++) {
            System.out.print("Nome do site " + (i + 1) + ": ");
            nomes[i] = scanner.nextLine();
        }

        double[][] A = new double[n][n];
        System.out.println("Exemplo de relevância: 0 1 0 1");
        for (int i = 0; i < n; i++) {
            System.out.print("Linha " + (i + 1) + ": ");
            String[] linha = scanner.nextLine().trim().split("\\s+");
            for (int j = 0; j < linha.length; j++) {
                A[i][j] = Double.parseDouble(linha[j]);
            }
        }

        calcularEExibirHITS(A, nomes, "Matriz Personalizada");
    }

    public static void calcularEExibirHITS(double[][] A, String[] nomes, String titulo) {
        int n = A.length;
        double[] h = new double[n];
        double[] a = new double[n];
        Arrays.fill(h, 1.0);
        Arrays.fill(a, 1.0);

        double[] hNew = new double[n];
        double[] aNew = new double[n];

        for (int iter = 0; iter < 100; iter++) {
            // Autoridade = A^T * h
            for (int i = 0; i < n; i++) {
                aNew[i] = 0;
                for (int j = 0; j < n; j++) {
                    aNew[i] += A[j][i] * h[j];
                }
            }

            normalize(aNew);

            // Hub = A * a
            for (int i = 0; i < n; i++) {
                hNew[i] = 0;
                for (int j = 0; j < n; j++) {
                    hNew[i] += A[i][j] * aNew[j];
                }
            }

            normalize(hNew);

            double diff = 0;
            for (int i = 0; i < n; i++) {
                diff += Math.abs(aNew[i] - a[i]) + Math.abs(hNew[i] - h[i]);
            }

            a = Arrays.copyOf(aNew, n);
            h = Arrays.copyOf(hNew, n);

            if (diff < TOLERANCIA) break;
        }

        List<Site> resultados = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            resultados.add(new Site(nomes[i], a[i], h[i]));
        }

        resultados.sort(Comparator.comparingDouble(Site::autoridade).reversed());

        System.out.println("\n======= " + titulo + " =======");
        System.out.printf("%-10s %-20s %-15s %-15s\n", "Posição", "Site", "Autoridade", "Hub");
        System.out.println("-".repeat(65));
        for (int i = 0; i < resultados.size(); i++) {
            Site site = resultados.get(i);
            System.out.printf("%-10d %-20s %-15.6f %-15.6f\n", i + 1, site.nome(), site.autoridade(), site.hub());
        }
    }

    public static void normalize(double[] v) {
        double norm = 0.0;
        for (double val : v) norm += val * val;
        norm = Math.sqrt(norm);
        if (norm == 0) return;
        for (int i = 0; i < v.length; i++) v[i] /= norm;
    }
}
