#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT_DIR"

DOCUMENTOS="${1:-documentos}"
SAIDA_DIR="target/execucoes"

mkdir -p "$SAIDA_DIR"

run_case() {
    local nome="$1"
    shift

    echo
    echo "============================================================"
    echo "$nome"
    echo "Comando: java Main $*"
    echo "============================================================"

    java Main "$@" | tee "$SAIDA_DIR/$nome.txt"
}

echo "Compilando o projeto..."
javac *.java

run_case "01_lista_limiar_030" "$DOCUMENTOS" 0.30 lista
run_case "02_lista_limiar_050" "$DOCUMENTOS" 0.50 lista
run_case "03_topk_3_limiar_030" "$DOCUMENTOS" 0.30 topK 3
run_case "04_busca_par_mais_similar" "$DOCUMENTOS" 0.0 busca doc3.txt doc4.txt
run_case "05_busca_par_pouco_similar" "$DOCUMENTOS" 0.0 busca doc2.txt doc5.txt

echo
echo "Saidas salvas em: $SAIDA_DIR"
