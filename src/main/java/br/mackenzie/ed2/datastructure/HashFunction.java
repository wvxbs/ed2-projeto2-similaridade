/*
 * Projeto 2 - Verificador de Similaridade de Textos
 * Integrantes:
 * - Bruno de Paula Souza - RA 10439739
 * - Gabriel Ferreira - RA 10442043
 * - Gian Lucca Campanha Ribeiro - RA 10438361
 */

package br.mackenzie.ed2.datastructure;

public enum HashFunction {
    ASCII("Somatoria de ASCII"),
    POLINOMIAL("Hash polinomial");

    private final String descricao;

    HashFunction(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
