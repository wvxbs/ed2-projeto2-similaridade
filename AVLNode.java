/*
 * Projeto 2 - Verificador de Similaridade de Textos
 * Integrantes:
 * - Bruno de Paula Souza - RA 10439739
 * - Gabriel Ferreira - RA 10442043
 * - Gian Lucca Campanha Ribeiro - RA 10438361
 */

import java.util.ArrayList;
import java.util.List;

class AVLNode {

    private final double similaridade;
    private int altura;
    private AVLNode left;
    private AVLNode right;
    private final List<Resultado> resultados;

    AVLNode(double similaridade, Resultado resultado) {
        this.similaridade = similaridade;
        this.altura = 1;
        this.resultados = new ArrayList<>();
        this.resultados.add(resultado);
    }

    double getSimilaridade() {
        return similaridade;
    }

    List<Resultado> getResultados() {
        return resultados;
    }

    AVLNode getLeft() {
        return left;
    }

    void setLeft(AVLNode left) {
        this.left = left;
    }

    AVLNode getRight() {
        return right;
    }

    void setRight(AVLNode right) {
        this.right = right;
    }

    void atualizarAltura() {
        altura = 1 + Math.max(altura(left), altura(right));
    }

    int getBalanceFactor() {
        return altura(right) - altura(left);
    }

    private int altura(AVLNode node) {
        return node == null ? 0 : node.altura;
    }
}
