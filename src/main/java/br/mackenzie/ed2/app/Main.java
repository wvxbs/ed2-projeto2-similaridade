/*
 * Projeto 2 - Verificador de Similaridade de Textos
 * Integrantes:
 * - Bruno de Paula Souza - RA 10439739
 * - Gabriel Ferreira - RA 10442043
 * - Gian Lucca Campanha Ribeiro - RA 10438361
 */

package br.mackenzie.ed2.app;

import br.mackenzie.ed2.datastructure.HashFunction;
import br.mackenzie.ed2.model.Resultado;
import br.mackenzie.ed2.service.VerificadorSimilaridade;
import br.mackenzie.ed2.similarity.MetricaSimilaridade;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

public class Main {

    private static final String ARQUIVO_RESULTADO = "resultado.txt";

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);

        if (!validarArgumentos(args)) {
            imprimirUso();
            return;
        }

        Path diretorio = Path.of(args[0]);
        double limiar = Double.parseDouble(args[1]);
        String modo = args[2].toLowerCase(Locale.ROOT);
        MetricaSimilaridade metrica = MetricaSimilaridade.COSSENO;
        HashFunction hashFunction = HashFunction.POLINOMIAL;

        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(Path.of(ARQUIVO_RESULTADO), StandardCharsets.UTF_8))) {
            VerificadorSimilaridade verificador = new VerificadorSimilaridade(metrica, hashFunction);

            imprimir(writer, "=== VERIFICADOR DE SIMILARIDADE DE TEXTOS ===");

            if ("busca".equals(modo)) {
                executarBusca(verificador, writer, diretorio, args[3], args[4], metrica, hashFunction);
                return;
            }

            verificador.carregarDocumentos(diretorio);
            verificador.compararTodos();

            imprimirResumo(writer, verificador, metrica, hashFunction);

            if ("lista".equals(modo)) {
                executarLista(verificador, writer, limiar);
            } else {
                int k = Integer.parseInt(args[3]);
                executarTopK(verificador, writer, limiar, k);
            }
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static boolean validarArgumentos(String[] args) {
        if (args.length < 3) {
            return false;
        }

        try {
            double limiar = Double.parseDouble(args[1]);
            if (limiar < 0 || limiar > 1) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }

        String modo = args[2].toLowerCase(Locale.ROOT);
        if ("lista".equals(modo)) {
            return args.length == 3;
        }
        if ("topk".equals(modo)) {
            return args.length == 4 && args[3].matches("\\d+");
        }
        if ("busca".equals(modo)) {
            return args.length == 5;
        }

        return false;
    }

    private static void executarBusca(VerificadorSimilaridade verificador, PrintWriter writer, Path diretorio,
                                      String arquivo1, String arquivo2, MetricaSimilaridade metrica,
                                      HashFunction hashFunction) throws IOException {
        Resultado resultado = verificador.compararArquivos(diretorio.resolve(arquivo1), diretorio.resolve(arquivo2));

        imprimir(writer, "Comparando: " + resultado.getDoc1() + " <-> " + resultado.getDoc2());
        imprimir(writer, "Similaridade calculada: " + formatar(resultado.getSimilaridade()));
        imprimir(writer, "Funcao hash utilizada: " + hashFunction.getDescricao());
        imprimir(writer, "Metrica utilizada: " + metrica.getDescricao());
    }

    private static void executarLista(VerificadorSimilaridade verificador, PrintWriter writer, double limiar) {
        imprimir(writer, "");
        imprimir(writer, "Pares com similaridade >= " + formatar(limiar) + ":");
        imprimir(writer, "---------------------------------");
        imprimirResultados(writer, verificador.listarAcimaDoLimiar(limiar));

        imprimir(writer, "");
        imprimir(writer, "Pares com menor similaridade:");
        imprimir(writer, "---------------------------------");
        imprimirResultados(writer, verificador.listarAbaixoDoLimiar(limiar));
    }

    private static void executarTopK(VerificadorSimilaridade verificador, PrintWriter writer, double limiar, int k) {
        imprimir(writer, "");
        imprimir(writer, "Top " + k + " pares mais semelhantes com similaridade >= " + formatar(limiar) + ":");
        imprimir(writer, "---------------------------------");
        imprimirResultados(writer, verificador.topK(limiar, k));
    }

    private static void imprimirResumo(PrintWriter writer, VerificadorSimilaridade verificador,
                                       MetricaSimilaridade metrica, HashFunction hashFunction) {
        imprimir(writer, "Total de documentos processados: " + verificador.getTotalDocumentos());
        imprimir(writer, "Total de pares comparados: " + verificador.getTotalParesComparados());
        imprimir(writer, "Funcao hash utilizada: " + hashFunction.getDescricao());
        imprimir(writer, "Metrica de similaridade: " + metrica.getDescricao());

        imprimir(writer, "");
        imprimir(writer, "Colisoes por documento:");
        verificador.getDocumentos().forEach(documento ->
                imprimir(writer, documento.getNome() + ": " + documento.getTabela().getColisoes()));

        imprimir(writer, "");
        imprimir(writer, "Rotacoes realizadas na AVL:");
        imprimir(writer, "Simples: " + verificador.getTotalRotacoesSimples());
        imprimir(writer, "Duplas: " + verificador.getTotalRotacoesDuplas());
    }

    private static void imprimirResultados(PrintWriter writer, List<Resultado> resultados) {
        if (resultados.isEmpty()) {
            imprimir(writer, "Nenhum par encontrado.");
            return;
        }

        for (Resultado resultado : resultados) {
            imprimir(writer, resultado.getDoc1() + " <-> " + resultado.getDoc2() + " = " + formatar(resultado.getSimilaridade()));
        }
    }

    private static void imprimir(PrintWriter writer, String texto) {
        System.out.println(texto);
        writer.println(texto);
    }

    private static String formatar(double valor) {
        return String.format(Locale.US, "%.2f", valor);
    }

    private static void imprimirUso() {
        System.out.println("Uso:");
        System.out.println("java -cp target/classes br.mackenzie.ed2.app.Main <diretorio_documentos> <limiar> <modo> [argumentos_opcionais]");
        System.out.println("Modos:");
        System.out.println("lista: <diretorio_documentos> <limiar> lista");
        System.out.println("topK: <diretorio_documentos> <limiar> topK <k>");
        System.out.println("busca: <diretorio_documentos> 0.0 busca <doc1.txt> <doc2.txt>");
    }
}
