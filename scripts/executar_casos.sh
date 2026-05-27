#!/usr/bin/env sh
set -eu

SCRIPT_PATH=$0
case "$SCRIPT_PATH" in
    /*) ;;
    *) SCRIPT_PATH="$(pwd)/$SCRIPT_PATH" ;;
esac

SCRIPT_DIR=$(CDPATH= cd -- "$(dirname -- "$SCRIPT_PATH")" && pwd)
ROOT_DIR=$(CDPATH= cd -- "$SCRIPT_DIR/.." && pwd)

DOCUMENTOS=${1:-documentos}
case "$DOCUMENTOS" in
    /*) DOCUMENTOS_ABS=$DOCUMENTOS ;;
    *) DOCUMENTOS_ABS="$ROOT_DIR/$DOCUMENTOS" ;;
esac

if ! command -v javac >/dev/null 2>&1; then
    echo "Erro: javac nao encontrado no PATH."
    exit 1
fi

if ! command -v java >/dev/null 2>&1; then
    echo "Erro: java nao encontrado no PATH."
    exit 1
fi

if [ ! -d "$DOCUMENTOS_ABS" ]; then
    echo "Erro: diretorio de documentos nao encontrado: $DOCUMENTOS_ABS"
    exit 1
fi

WORK_DIR="$ROOT_DIR/target"
if ! mkdir -p "$WORK_DIR/classes" "$WORK_DIR/execucoes" 2>/dev/null; then
    WORK_DIR=$(mktemp -d "${TMPDIR:-/tmp}/ed2-projeto2.XXXXXX")
    mkdir -p "$WORK_DIR/classes" "$WORK_DIR/execucoes"
    echo "Aviso: sem permissao para escrever em $ROOT_DIR/target."
    echo "Usando pasta temporaria: $WORK_DIR"
fi

if [ ! -w "$WORK_DIR" ]; then
    WORK_DIR=$(mktemp -d "${TMPDIR:-/tmp}/ed2-projeto2.XXXXXX")
    mkdir -p "$WORK_DIR/classes" "$WORK_DIR/execucoes"
    echo "Aviso: pasta de saida sem permissao de escrita."
    echo "Usando pasta temporaria: $WORK_DIR"
fi

BUILD_DIR="$WORK_DIR/classes"
SAIDA_DIR="$WORK_DIR/execucoes"

run_case() {
    nome=$1
    shift
    arquivo_saida="$SAIDA_DIR/$nome.txt"

    echo
    echo "============================================================"
    echo "$nome"
    echo "Comando: java -cp $BUILD_DIR Main $*"
    echo "============================================================"

    if ! (cd "$WORK_DIR" && java -cp "$BUILD_DIR" Main "$@") > "$arquivo_saida" 2>&1; then
        cat "$arquivo_saida"
        echo "Erro: caso '$nome' falhou."
        exit 1
    fi

    cat "$arquivo_saida"
}

echo "Compilando o projeto..."
find "$ROOT_DIR" -maxdepth 1 -name "*.java" -print0 | xargs -0 javac -encoding UTF-8 -d "$BUILD_DIR"

run_case "01_lista_limiar_030" "$DOCUMENTOS_ABS" 0.30 lista
run_case "02_lista_limiar_050" "$DOCUMENTOS_ABS" 0.50 lista
run_case "03_topk_3_limiar_030" "$DOCUMENTOS_ABS" 0.30 topK 3
run_case "04_busca_par_mais_similar" "$DOCUMENTOS_ABS" 0.0 busca doc3.txt doc4.txt
run_case "05_busca_par_pouco_similar" "$DOCUMENTOS_ABS" 0.0 busca doc2.txt doc5.txt

echo
echo "Saidas salvas em: $SAIDA_DIR"
