#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT_DIR"

DOCUMENTOS="${1:-src/test/resources/documentos}"
MAIN_CLASS="br.mackenzie.ed2.app.Main"
SAIDA_DIR="target/execucoes"

mkdir -p "$SAIDA_DIR"

run_case() {
    local nome="$1"
    shift

    echo
    echo "============================================================"
    echo "$nome"
    echo "Comando: java -cp target/classes $MAIN_CLASS $*"
    echo "============================================================"

    java -cp target/classes "$MAIN_CLASS" "$@" | tee "$SAIDA_DIR/$nome.txt"
}

echo "Compilando o projeto..."
mvn -q compile

run_case "01_lista_limiar_030" "$DOCUMENTOS" 0.30 lista
run_case "02_lista_limiar_050" "$DOCUMENTOS" 0.50 lista
run_case "03_topk_3_limiar_030" "$DOCUMENTOS" 0.30 topK 3
run_case "04_busca_par_mais_similar" "$DOCUMENTOS" 0.0 busca doc3.txt doc4.txt
run_case "05_busca_par_pouco_similar" "$DOCUMENTOS" 0.0 busca doc2.txt doc5.txt

echo
echo "Saidas salvas em: $SAIDA_DIR"
