# Verificador de Similaridade de Textos

Projeto 2 de Estrutura de Dados II.

## Integrantes

- Bruno de Paula Souza - RA 10439739
- Gabriel Ferreira - RA 10442043
- Gian Lucca Campanha Ribeiro - RA 10438361

## Estrutura

```text
src/main/java/br/mackenzie/ed2
|-- app             # entrada do programa
|-- datastructure   # HashTable e AVL
|-- model           # classes de dados
|-- service         # fluxo principal de comparação
|-- similarity      # métricas de similaridade
`-- text            # leitura e tratamento dos documentos
```

Os textos usados nos testes ficam em `src/test/resources/documentos`.

Para a correção automática, também existe uma cópia dos arquivos Java e dos documentos de teste na raiz do projeto. Isso permite executar exatamente o formato pedido no enunciado.

## Compilação e execução para correção

```bash
javac *.java && java Main ./documentos 0.7 lista
```

Outros exemplos no mesmo formato:

```bash
java Main ./documentos 0.30 lista
java Main ./documentos 0.30 topK 3
java Main ./documentos 0.0 busca doc1.txt doc4.txt
```

A saída também é gravada em `resultado.txt`.

## Compilação com Maven

```bash
mvn compile
```

## Execução com Maven

```bash
java -cp target/classes br.mackenzie.ed2.app.Main src/test/resources/documentos 0.30 lista
java -cp target/classes br.mackenzie.ed2.app.Main src/test/resources/documentos 0.30 topK 3
java -cp target/classes br.mackenzie.ed2.app.Main src/test/resources/documentos 0.0 busca doc1.txt doc4.txt
```

## Execução automatizada

```bash
./scripts/executar_casos.sh
```

O script compila o projeto e executa casos de `lista`, `topK` e `busca`. As saídas ficam em `target/execucoes`.
