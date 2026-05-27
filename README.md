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

Os arquivos `.java` ficam na raiz para manter compatibilidade com o formato de correção informado no enunciado.

## Compilação e execução

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

A saída também é gravada em `resultado.txt`.

## Execução automatizada

```bash
./scripts/executar_casos.sh
```

O script compila com `javac *.java` e executa casos de `lista`, `topK` e `busca`. As saídas ficam em `target/execucoes`.
