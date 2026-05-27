# Verificador de Similaridade de Textos

Projeto 2 de Estrutura de Dados II.

## Integrantes

- Bruno de Paula Souza - RA 10439739
- Gabriel Ferreira - RA 10442043
- Gian Lucca Campanha Ribeiro - RA 10438361

## Estrutura

```text
.
|-- Main.java
|-- Documento.java
|-- HashTable.java
|-- AVLTree.java
|-- documentos/
`-- scripts/executar_casos.sh
```

Os arquivos `.java` ficam na raiz para manter compatibilidade com o formato de correcao informado no enunciado.

## Compilacao e execucao

Comando principal:

```bash
javac *.java && java Main ./documentos 0.7 lista
```

Outros exemplos:

```bash
java Main ./documentos 0.30 lista
java Main ./documentos 0.30 topK 3
java Main ./documentos 0.0 busca doc1.txt doc4.txt
```

A saida tambem e gravada em `resultado.txt`.

## Execucao automatizada

```bash
./scripts/executar_casos.sh
```

O script compila com `javac *.java` e executa casos de `lista`, `topK` e `busca`. As saidas ficam em `target/execucoes`.

Ele tambem pode ser chamado explicitamente por `bash`, `zsh` ou `sh`:

```bash
zsh scripts/executar_casos.sh
```

Se o diretorio do projeto nao permitir escrita, o script usa uma pasta temporaria em `/tmp`.
