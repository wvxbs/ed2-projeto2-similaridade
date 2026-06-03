# Verificador de Similaridade de Textos:

Projeto 2 de Estrutura de Dados II.

## Integrantes:

* Bruno de Paula Souza - RA 10439739
* Gabriel Ferreira - RA 10442043
* Gian Lucca Campanha Ribeiro - RA 10438361

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

## Compilação e Execução:

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

A saída também é gravada em `resultado.txt`.

## Execução Automatizada:

```bash
./scripts/executar_casos.sh
```

O script compila com `javac *.java` e executa casos de `lista`, `topK` e `busca`. As saídas ficam em `target/execucoes`.

Se o diretório do projeto não permitir escrita, o script usa uma pasta temporária em `/tmp`.

Se o sistema remover a permissão de execução do script ao extrair o `.zip`, execute com:

```bash
sh scripts/executar_casos.sh
```
