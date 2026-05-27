/*
 * Projeto 2 - Verificador de Similaridade de Textos
 * Integrantes:
 * - Bruno de Paula Souza - RA 10439739
 * - Gabriel Ferreira - RA 10442043
 * - Gian Lucca Campanha Ribeiro - RA 10438361
 */

package br.mackenzie.ed2.datastructure;

public class RotationInfo {

    private int simples;
    private int duplas;

    public void incrementarSimples() {
        simples++;
    }

    public void incrementarDuplas() {
        duplas++;
    }

    public int getSimples() {
        return simples;
    }

    public int getDuplas() {
        return duplas;
    }
}
