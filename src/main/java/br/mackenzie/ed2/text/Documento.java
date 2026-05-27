/*
 * Projeto 2 - Verificador de Similaridade de Textos
 * Integrantes:
 * - Bruno de Paula Souza - RA 10439739
 * - Gabriel Ferreira - RA 10442043
 * - Gian Lucca Campanha Ribeiro - RA 10438361
 */

package br.mackenzie.ed2.text;

import br.mackenzie.ed2.datastructure.HashFunction;
import br.mackenzie.ed2.datastructure.HashTable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Normalizer;

public class Documento {

    private static final String[] STOP_WORDS = {
            "a", "o", "e", "de", "do", "da", "em", "no", "na",
            "para", "com", "por", "um", "uma", "os", "as", "dos",
            "das", "que", "se", "ao", "aos", "mas", "ou", "ela",
            "ele", "eles", "elas", "eu", "nos", "seu", "sua"
    };

    private final Path arquivo;
    private final HashTable tabela;

    public Documento(Path arquivo, HashFunction hashFunction) throws IOException {
        if (!Files.isRegularFile(arquivo)) {
            throw new IllegalArgumentException("Arquivo nao encontrado: " + arquivo);
        }

        this.arquivo = arquivo;
        this.tabela = new HashTable(hashFunction);
        processarTexto(Files.readString(arquivo));
    }

    public String getNome() {
        return arquivo.getFileName().toString();
    }

    public HashTable getTabela() {
        return tabela;
    }

    private void processarTexto(String texto) {
        String textoNormalizado = Normalizer.normalize(texto.toLowerCase(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replaceAll("[^a-z0-9\\s]", " ");

        String[] palavras = textoNormalizado.split("\\s+");

        for (String palavra : palavras) {
            if (palavra.isBlank() || ehStopWord(palavra)) {
                continue;
            }

            int frequencia = tabela.get(palavra);
            tabela.put(palavra, frequencia == -1 ? 1 : frequencia + 1);
        }
    }

    private boolean ehStopWord(String palavra) {
        for (String stopWord : STOP_WORDS) {
            if (palavra.equals(stopWord)) {
                return true;
            }
        }

        return false;
    }
}
