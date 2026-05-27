/*
 * Projeto 2 - Verificador de Similaridade de Textos
 * Integrantes:
 * - Bruno de Paula Souza - RA 10439739
 * - Gabriel Ferreira - RA 10442043
 * - Gian Lucca Campanha Ribeiro - RA 10438361
 */

package br.mackenzie.ed2.datastructure;

import java.util.ArrayList;
import java.util.List;

public class HashTable {

    private static final int CAPACIDADE_INICIAL = 11;
    private static final double FATOR_CARGA_MAX = 0.75;

    private Entry[] tabela;
    private int numElementos;
    private int colisoes;
    private final HashFunction hashFunction;

    private static class Entry {
        private final String chave;
        private int valor;
        private int status;

        private Entry(String chave, int valor) {
            this.chave = chave;
            this.valor = valor;
            this.status = 1;
        }
    }

    public HashTable(HashFunction hashFunction) {
        this(CAPACIDADE_INICIAL, hashFunction);
    }

    public HashTable(int capacidade, HashFunction hashFunction) {
        if (capacidade < 2) {
            throw new IllegalArgumentException("Capacidade minima: 2");
        }

        this.tabela = new Entry[capacidade];
        this.hashFunction = hashFunction;
    }

    public void put(String chave, int valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("Valor deve ser positivo");
        }

        if ((double) (numElementos + 1) / tabela.length > FATOR_CARGA_MAX) {
            rehash();
        }

        int posicao = procurarPosicao(chave, true);
        if (tabela[posicao] != null && tabela[posicao].status == 1) {
            tabela[posicao].valor = valor;
            return;
        }

        tabela[posicao] = new Entry(chave, valor);
        numElementos++;
    }

    public int get(String chave) {
        int posicao = procurarPosicao(chave, false);

        if (tabela[posicao] != null && tabela[posicao].status == 1 && tabela[posicao].chave.equals(chave)) {
            return tabela[posicao].valor;
        }

        return -1;
    }

    public int size() {
        return numElementos;
    }

    public int capacity() {
        return tabela.length;
    }

    public int getColisoes() {
        return colisoes;
    }

    public String[] getChaves() {
        List<String> chaves = new ArrayList<>();

        for (Entry entry : tabela) {
            if (entry != null && entry.status == 1) {
                chaves.add(entry.chave);
            }
        }

        return chaves.toArray(new String[0]);
    }

    private int procurarPosicao(String chave, boolean contarColisoes) {
        int indiceInicial = hash(chave);

        for (int tentativa = 0; tentativa < tabela.length; tentativa++) {
            int posicao = (indiceInicial + tentativa) % tabela.length;

            if (tentativa > 0 && contarColisoes) {
                colisoes++;
            }

            if (tabela[posicao] != null && tabela[posicao].status == 1 && tabela[posicao].chave.equals(chave)) {
                return posicao;
            }

            if (tabela[posicao] == null || tabela[posicao].status == -1) {
                return posicao;
            }
        }

        throw new IllegalStateException("Tabela cheia apos sondagem linear completa");
    }

    private int hash(String chave) {
        if (hashFunction == HashFunction.ASCII) {
            return hashAscii(chave);
        }

        return hashPolinomial(chave);
    }

    private int hashAscii(String chave) {
        int soma = 0;

        for (char caractere : chave.toCharArray()) {
            soma += caractere;
        }

        return Math.floorMod(soma, tabela.length);
    }

    private int hashPolinomial(String chave) {
        int hash = 0;
        int primo = 31;

        for (char caractere : chave.toCharArray()) {
            hash = hash * primo + caractere;
        }

        return Math.floorMod(hash, tabela.length);
    }

    private void rehash() {
        Entry[] tabelaAntiga = tabela;
        tabela = new Entry[proximoPrimo(tabelaAntiga.length * 2)];
        numElementos = 0;

        for (Entry entry : tabelaAntiga) {
            if (entry != null && entry.status == 1) {
                put(entry.chave, entry.valor);
            }
        }
    }

    private int proximoPrimo(int numero) {
        int candidato = numero % 2 == 0 ? numero + 1 : numero;

        while (!ehPrimo(candidato)) {
            candidato += 2;
        }

        return candidato;
    }

    private boolean ehPrimo(int numero) {
        if (numero < 2) {
            return false;
        }
        if (numero == 2) {
            return true;
        }
        if (numero % 2 == 0) {
            return false;
        }

        for (int divisor = 3; divisor * divisor <= numero; divisor += 2) {
            if (numero % divisor == 0) {
                return false;
            }
        }

        return true;
    }
}
