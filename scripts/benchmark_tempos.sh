#!/usr/bin/env sh
set -eu

# Bruno de Paula Souza - RA 10439739
# Gabriel Ferreira - RA 10442043
# Gian Lucca Campanha Ribeiro - RA 10438361

SCRIPT_PATH=$0
case "$SCRIPT_PATH" in
    /*) ;;
    *) SCRIPT_PATH="$(pwd)/$SCRIPT_PATH" ;;
esac

SCRIPT_DIR=$(CDPATH= cd -- "$(dirname -- "$SCRIPT_PATH")" && pwd)
ROOT_DIR=$(CDPATH= cd -- "$SCRIPT_DIR/.." && pwd)

DOCUMENTOS=${1:-documentos}
VOLUMES=${2:-"2 5 10 15 30 50 100 200"}
REPETICOES=${3:-25}

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
if ! mkdir -p "$WORK_DIR/classes" "$WORK_DIR/benchmark" 2>/dev/null; then
    WORK_DIR=$(mktemp -d "${TMPDIR:-/tmp}/ed2-projeto2-benchmark.XXXXXX")
    mkdir -p "$WORK_DIR/classes" "$WORK_DIR/benchmark"
    echo "Aviso: sem permissao para escrever em $ROOT_DIR/target."
    echo "Usando pasta temporaria: $WORK_DIR"
fi

if [ ! -w "$WORK_DIR" ]; then
    WORK_DIR=$(mktemp -d "${TMPDIR:-/tmp}/ed2-projeto2-benchmark.XXXXXX")
    mkdir -p "$WORK_DIR/classes" "$WORK_DIR/benchmark"
    echo "Aviso: pasta de saida sem permissao de escrita."
    echo "Usando pasta temporaria: $WORK_DIR"
fi

BUILD_DIR="$WORK_DIR/classes"
BENCH_DIR="$WORK_DIR/benchmark"
FONTES="$BENCH_DIR/fontes.txt"
CSV="$BENCH_DIR/tempos.csv"

find "$DOCUMENTOS_ABS" -maxdepth 1 -type f -name "*.txt" ! -name "resultado.txt" | sort > "$FONTES"
TOTAL_FONTES=$(wc -l < "$FONTES" | tr -d " ")

if [ "$TOTAL_FONTES" -eq 0 ]; then
    echo "Erro: nenhum arquivo .txt encontrado em $DOCUMENTOS_ABS"
    exit 1
fi

rm -rf "$BUILD_DIR" "$BENCH_DIR"/docs_*
mkdir -p "$BUILD_DIR"

echo "Compilando o projeto..."
find "$ROOT_DIR" -maxdepth 1 -name "*.java" -print0 | xargs -0 javac -encoding UTF-8 --release 17 -d "$BUILD_DIR"

montar_conjunto() {
    volume=$1
    destino="$BENCH_DIR/docs_$volume"
    rm -rf "$destino"
    mkdir -p "$destino"

    i=1
    while [ "$i" -le "$volume" ]; do
        linha=$(( ((i - 1) % TOTAL_FONTES) + 1 ))
        origem=$(sed -n "${linha}p" "$FONTES")
        numero=$(printf "%03d" "$i")
        cp "$origem" "$destino/doc_${numero}_$(basename -- "$origem")"
        i=$((i + 1))
    done
}

medir_volume() {
    volume=$1
    repeticoes=$2
    docs="$BENCH_DIR/docs_$volume"
    pares=$((volume * (volume - 1) / 2))

    java -cp "$BUILD_DIR" Main "$docs" 0.30 lista >/dev/null

    valores=""
    i=1
    while [ "$i" -le "$repeticoes" ]; do
        inicio=$(date +%s%N)
        java -cp "$BUILD_DIR" Main "$docs" 0.30 lista >/dev/null
        fim=$(date +%s%N)
        valores="$valores $(((fim - inicio) / 1000000))"
        i=$((i + 1))
    done

    awk -v volume="$volume" -v pares="$pares" -v repeticoes="$repeticoes" '
        BEGIN { soma = 0; minimo = 999999999; maximo = 0; qtd = 0 }
        {
            for (i = 1; i <= NF; i++) {
                valor = $i
                soma += valor
                if (valor < minimo) minimo = valor
                if (valor > maximo) maximo = valor
                qtd++
            }
        }
        END {
            printf "%d,%d,%d,%.2f,%d,%d\n", volume, pares, repeticoes, soma / qtd, minimo, maximo
        }
    ' <<EOF
$valores
EOF
}

echo "volume,pares,repeticoes,media_ms,min_ms,max_ms" > "$CSV"

for volume in $VOLUMES; do
    montar_conjunto "$volume"
    medir_volume "$volume" "$REPETICOES" | tee -a "$CSV"
done

echo
echo "Resultado salvo em: $CSV"
