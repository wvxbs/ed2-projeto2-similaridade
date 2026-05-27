/*
 * Projeto 2 - Verificador de Similaridade de Textos
 * Integrantes:
 * - Bruno de Paula Souza - RA 10439739
 * - Gabriel Ferreira - RA 10442043
 * - Gian Lucca Campanha Ribeiro - RA 10438361
 */

package br.mackenzie.ed2.service;

import br.mackenzie.ed2.datastructure.AVLTree;
import br.mackenzie.ed2.datastructure.HashFunction;
import br.mackenzie.ed2.datastructure.RotationInfo;
import br.mackenzie.ed2.model.Resultado;
import br.mackenzie.ed2.similarity.MetricaSimilaridade;
import br.mackenzie.ed2.text.Documento;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class VerificadorSimilaridade {

    private final MetricaSimilaridade metrica;
    private final HashFunction hashFunction;
    private final List<Documento> documentos;
    private final AVLTree avlTree;
    private int totalRotacoesSimples;
    private int totalRotacoesDuplas;
    private int totalParesComparados;

    public VerificadorSimilaridade(MetricaSimilaridade metrica, HashFunction hashFunction) {
        this.metrica = metrica;
        this.hashFunction = hashFunction;
        this.documentos = new ArrayList<>();
        this.avlTree = new AVLTree();
    }

    public void carregarDocumentos(Path diretorio) throws IOException {
        if (!Files.isDirectory(diretorio)) {
            throw new IllegalArgumentException("Diretorio de documentos invalido: " + diretorio);
        }

        try (var paths = Files.list(diretorio)) {
            List<Path> arquivos = paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().toLowerCase().endsWith(".txt"))
                    .filter(path -> !path.getFileName().toString().equalsIgnoreCase("resultado.txt"))
                    .sorted(Comparator.comparing(path -> path.getFileName().toString()))
                    .toList();

            if (arquivos.isEmpty()) {
                throw new IllegalArgumentException("Nenhum arquivo .txt encontrado em: " + diretorio);
            }

            for (Path arquivo : arquivos) {
                documentos.add(new Documento(arquivo, hashFunction));
            }
        }
    }

    public void compararTodos() {
        for (int i = 0; i < documentos.size(); i++) {
            for (int j = i + 1; j < documentos.size(); j++) {
                Resultado resultado = comparar(documentos.get(i), documentos.get(j));
                RotationInfo rotacoes = avlTree.insert(resultado.getSimilaridade(), resultado);
                totalRotacoesSimples += rotacoes.getSimples();
                totalRotacoesDuplas += rotacoes.getDuplas();
                totalParesComparados++;
            }
        }
    }

    public Resultado compararArquivos(Path arquivo1, Path arquivo2) throws IOException {
        Documento doc1 = new Documento(arquivo1, hashFunction);
        Documento doc2 = new Documento(arquivo2, hashFunction);
        return comparar(doc1, doc2);
    }

    public List<Resultado> listarAcimaDoLimiar(double limiar) {
        return avlTree.listarAcimaDoLimiar(limiar);
    }

    public List<Resultado> listarAbaixoDoLimiar(double limiar) {
        return avlTree.listarAbaixoDoLimiar(limiar);
    }

    public List<Resultado> topK(double limiar, int k) {
        return avlTree.topK(limiar, k);
    }

    public List<Documento> getDocumentos() {
        return documentos;
    }

    public int getTotalDocumentos() {
        return documentos.size();
    }

    public int getTotalParesComparados() {
        return totalParesComparados;
    }

    public int getTotalRotacoesSimples() {
        return totalRotacoesSimples;
    }

    public int getTotalRotacoesDuplas() {
        return totalRotacoesDuplas;
    }

    private Resultado comparar(Documento doc1, Documento doc2) {
        double similaridade = metrica.calcular(doc1, doc2);
        return new Resultado(doc1.getNome(), doc2.getNome(), similaridade);
    }
}
