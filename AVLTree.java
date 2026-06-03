/*
 * Projeto 2 - Verificador de Similaridade de Textos
 * Integrantes:
 * - Bruno de Paula Souza - RA 10439739
 * - Gabriel Ferreira - RA 10442043
 * - Gian Lucca Campanha Ribeiro - RA 10438361
 */

import java.util.ArrayList;
import java.util.List;

public class AVLTree {

    private AVLNode root;

    public RotationInfo insert(double similaridade, Resultado resultado) {
        RotationInfo info = new RotationInfo();
        root = insert(root, similaridade, resultado, info);
        return info;
    }

    public List<Resultado> listarAcimaDoLimiar(double limiar) {
        List<Resultado> resultados = new ArrayList<>();
        listarAcimaDoLimiar(root, limiar, resultados);
        return resultados;
    }

    public List<Resultado> listarAbaixoDoLimiar(double limiar) {
        List<Resultado> resultados = new ArrayList<>();
        listarAbaixoDoLimiar(root, limiar, resultados);
        return resultados;
    }

    public List<Resultado> topK(double limiar, int k) {
        List<Resultado> resultados = new ArrayList<>();
        topK(root, limiar, k, resultados);
        return resultados;
    }

    private AVLNode insert(AVLNode node, double similaridade, Resultado resultado, RotationInfo info) {
        if (node == null) {
            return new AVLNode(similaridade, resultado);
        }

        if (similaridade < node.getSimilaridade()) {
            node.setLeft(insert(node.getLeft(), similaridade, resultado, info));
        } else if (similaridade > node.getSimilaridade()) {
            node.setRight(insert(node.getRight(), similaridade, resultado, info));
        } else {
            node.getResultados().add(resultado);
            return node;
        }

        node.atualizarAltura();
        return balance(node, info);
    }

    private AVLNode balance(AVLNode node, RotationInfo info) {
        int balanceFactor = node.getBalanceFactor();

        // Cada caso abaixo corresponde a um desbalanceamento clássico da AVL
        if (balanceFactor > 1 && node.getRight().getBalanceFactor() >= 0) {
            info.incrementarSimples();
            return rotateLeft(node);
        }

        if (balanceFactor < -1 && node.getLeft().getBalanceFactor() <= 0) {
            info.incrementarSimples();
            return rotateRight(node);
        }

        if (balanceFactor > 1 && node.getRight().getBalanceFactor() < 0) {
            info.incrementarDuplas();
            node.setRight(rotateRight(node.getRight()));
            return rotateLeft(node);
        }

        if (balanceFactor < -1 && node.getLeft().getBalanceFactor() > 0) {
            info.incrementarDuplas();
            node.setLeft(rotateLeft(node.getLeft()));
            return rotateRight(node);
        }

        return node;
    }

    private AVLNode rotateLeft(AVLNode node) {
        AVLNode right = node.getRight();
        AVLNode movedSubtree = right.getLeft();

        right.setLeft(node);
        node.setRight(movedSubtree);

        node.atualizarAltura();
        right.atualizarAltura();

        return right;
    }

    private AVLNode rotateRight(AVLNode node) {
        AVLNode left = node.getLeft();
        AVLNode movedSubtree = left.getRight();

        left.setRight(node);
        node.setLeft(movedSubtree);

        node.atualizarAltura();
        left.atualizarAltura();

        return left;
    }

    private void listarAcimaDoLimiar(AVLNode node, double limiar, List<Resultado> resultados) {
        if (node == null) {
            return;
        }

        listarAcimaDoLimiar(node.getRight(), limiar, resultados);

        if (node.getSimilaridade() >= limiar) {
            resultados.addAll(node.getResultados());
            listarAcimaDoLimiar(node.getLeft(), limiar, resultados);
        }
    }

    private void listarAbaixoDoLimiar(AVLNode node, double limiar, List<Resultado> resultados) {
        if (node == null) {
            return;
        }

        listarAbaixoDoLimiar(node.getLeft(), limiar, resultados);

        if (node.getSimilaridade() < limiar) {
            resultados.addAll(node.getResultados());
            listarAbaixoDoLimiar(node.getRight(), limiar, resultados);
        }
    }

    private void topK(AVLNode node, double limiar, int k, List<Resultado> resultados) {
        // Percorre os maiores valores primeiro para montar o top K
        if (node == null || resultados.size() >= k) {
            return;
        }

        topK(node.getRight(), limiar, k, resultados);

        if (node.getSimilaridade() >= limiar) {
            for (Resultado resultado : node.getResultados()) {
                if (resultados.size() >= k) {
                    return;
                }
                resultados.add(resultado);
            }
            topK(node.getLeft(), limiar, k, resultados);
        }
    }
}
