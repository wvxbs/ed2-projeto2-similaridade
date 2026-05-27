/*
 * Projeto 2 - Verificador de Similaridade de Textos
 * Integrantes:
 * - Bruno de Paula Souza - RA 10439739
 * - Gabriel Ferreira - RA 10442043
 * - Gian Lucca Campanha Ribeiro - RA 10438361
 */

package br.mackenzie.ed2.model;

public class Resultado {

    private final String doc1;
    private final String doc2;
    private final double similaridade;

    public Resultado(String doc1, String doc2, double similaridade) {
        this.doc1 = doc1;
        this.doc2 = doc2;
        this.similaridade = similaridade;
    }

    public String getDoc1() {
        return doc1;
    }

    public String getDoc2() {
        return doc2;
    }

    public double getSimilaridade() {
        return similaridade;
    }
}
