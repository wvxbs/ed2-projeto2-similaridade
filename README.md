# Verificador de Similaridade de Textos:

Projeto 2 de Estrutura de Dados II.

## Integrantes:

- Bruno de Paula Souza - RA 10439739
- Gabriel Ferreira - RA 10442043
- Gian Lucca Campanha Ribeiro - RA 10438361

## Estrutura:

```text
.
|-- Main.java
|-- Documento.java
|-- HashTable.java
|-- AVLTree.java
|-- documentos/
`-- scripts/executar_casos.sh
```

## Compilação e execução:

Compilar:

```bash
javac *.java
```

Executar:

```bash
java Main ./documentos 0.30 lista
java Main ./documentos 0.30 topK 3
java Main ./documentos 0.0 busca doc1.txt doc4.txt
```

A saida tambem é gravada em `resultado.txt`.

## Execução automatizada:

```bash
./scripts/executar_casos.sh
```

O script compila com `javac *.java` e executa casos de `lista`, `topK` e `busca`. As saidas ficam em `target/execucoes`.

Se o diretorio do projeto nao permitir escrita, o script usa uma pasta temporaria em `/tmp`.

Se o sistema remover a permissao de execucao do script ao extrair o `.zip`, execute com:

```bash
sh scripts/executar_casos.sh
```
