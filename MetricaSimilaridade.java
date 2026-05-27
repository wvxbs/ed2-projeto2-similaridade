/*
 * Projeto 2 - Verificador de Similaridade de Textos
 * Integrantes:
 * - Bruno de Paula Souza - RA 10439739
 * - Gabriel Ferreira - RA 10442043
 * - Gian Lucca Campanha Ribeiro - RA 10438361
 */

public enum MetricaSimilaridade {
    COSSENO("Cosseno") {
        @Override
        public double calcular(Documento doc1, Documento doc2) {
            String[] chavesDoc1 = doc1.getTabela().getChaves();
            String[] chavesDoc2 = doc2.getTabela().getChaves();
            double produtoEscalar = 0;
            double somaQuadradosD1 = 0;
            double somaQuadradosD2 = 0;

            for (String palavra : chavesDoc1) {
                int freqDoc1 = doc1.getTabela().get(palavra);
                int freqDoc2 = doc2.getTabela().get(palavra);

                if (freqDoc2 == -1) {
                    freqDoc2 = 0;
                }

                produtoEscalar += freqDoc1 * freqDoc2;
                somaQuadradosD1 += freqDoc1 * freqDoc1;
                somaQuadradosD2 += freqDoc2 * freqDoc2;
            }

            for (String palavra : chavesDoc2) {
                if (doc1.getTabela().get(palavra) == -1) {
                    int freqDoc2 = doc2.getTabela().get(palavra);
                    somaQuadradosD2 += freqDoc2 * freqDoc2;
                }
            }

            double denominador = Math.sqrt(somaQuadradosD1) * Math.sqrt(somaQuadradosD2);
            return denominador == 0 ? 0 : produtoEscalar / denominador;
        }
    },

    JACCARD("Jaccard") {
        @Override
        public double calcular(Documento doc1, Documento doc2) {
            int interseccao = contarInterseccao(doc1, doc2);
            int uniao = doc1.getTabela().size() + doc2.getTabela().size() - interseccao;
            return uniao == 0 ? 0 : (double) interseccao / uniao;
        }
    },

    DICE("Dice") {
        @Override
        public double calcular(Documento doc1, Documento doc2) {
            int somaTamanhos = doc1.getTabela().size() + doc2.getTabela().size();
            return somaTamanhos == 0 ? 0 : (2.0 * contarInterseccao(doc1, doc2)) / somaTamanhos;
        }
    };

    private final String descricao;

    MetricaSimilaridade(String descricao) {
        this.descricao = descricao;
    }

    public abstract double calcular(Documento doc1, Documento doc2);

    public String getDescricao() {
        return descricao;
    }

    private static int contarInterseccao(Documento doc1, Documento doc2) {
        int interseccao = 0;

        for (String palavra : doc1.getTabela().getChaves()) {
            if (doc2.getTabela().get(palavra) != -1) {
                interseccao++;
            }
        }

        return interseccao;
    }
}
