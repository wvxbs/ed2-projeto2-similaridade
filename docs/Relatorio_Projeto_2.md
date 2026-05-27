# Verificador de Similaridade de Textos

Bruno de Paula Souza - RA 10439739  
Gabriel Ferreira - RA 10442043  
Gian Lucca Campanha Ribeiro - RA 10438361

## 1. Introdução

Comparar documentos textuais é uma tarefa útil em problemas como identificação de plágio, agrupamento de documentos, recomendação de conteúdo e mecanismos de busca. Neste projeto, a similaridade entre arquivos de texto é calculada a partir das palavras relevantes de cada documento.

Cada documento é lido, normalizado e armazenado em uma tabela hash própria, na qual cada palavra funciona como chave e sua frequência como valor. Depois disso, os pares de documentos são comparados e inseridos em uma árvore AVL, usando a similaridade como chave de ordenação.

## 2. Metodologia

### 2.1 Estrutura do projeto

O código foi organizado em pacotes por responsabilidade:

| Pacote | Responsabilidade |
|---|---|
| `app` | ponto de entrada do programa |
| `service` | controle do fluxo de leitura, comparação e consulta |
| `text` | leitura e normalização dos documentos |
| `similarity` | métricas de similaridade |
| `datastructure` | tabela hash e árvore AVL |
| `model` | classes simples de dados |

Essa separação deixa o `Main` apenas com a responsabilidade de tratar argumentos e saída, enquanto as regras do projeto ficam em classes específicas.

### 2.2 Pré-processamento dos textos

Antes da comparação, o texto passa pelas seguintes etapas:

1. conversão para letras minúsculas;
2. remoção de acentos;
3. remoção de pontuação e caracteres não alfanuméricos;
4. tokenização por espaços;
5. remoção de stop words em português.

Após essas etapas, as palavras restantes são inseridas na tabela hash do documento com suas frequências.

### 2.3 Tabela hash

A tabela hash foi implementada manualmente. O tratamento de colisões é feito por sondagem linear, e a tabela é redimensionada quando o fator de carga ultrapassa 0,75.

Foram implementadas duas funções de dispersão:

| Função | Descrição |
|---|---|
| Somatória de ASCII | soma os valores dos caracteres e aplica módulo pelo tamanho da tabela |
| Hash polinomial | multiplica o valor acumulado por 31 e soma o caractere atual |

Na execução principal foi utilizado o hash polinomial. Ele tende a distribuir melhor palavras parecidas, principalmente quando duas palavras usam letras semelhantes em ordens diferentes.

### 2.4 Árvore AVL

A árvore AVL guarda os resultados das comparações ordenados pela similaridade. Como mais de um par pode ter a mesma pontuação, cada nó mantém uma lista de objetos `Resultado`.

A inserção retorna a quantidade de rotações realizadas por meio da classe `RotationInfo`. Esses valores são acumulados durante a comparação dos pares para análise posterior.

### 2.5 Métrica de similaridade

A métrica usada na execução principal foi o Cosseno. Ela foi escolhida porque considera a frequência das palavras, e não apenas se uma palavra aparece ou não no documento.

Também foram implementadas as métricas Jaccard e Dice, mantidas no código para comparação e possíveis testes.

## 3. Funcionamento

O programa recebe os argumentos no formato:

```text
java -cp target/classes br.mackenzie.ed2.app.Main <diretorio_documentos> <limiar> <modo> [argumentos_opcionais]
```

Os modos disponíveis são:

| Modo | Comportamento |
|---|---|
| `lista` | mostra os pares acima do limiar e os pares abaixo dele |
| `topK` | mostra os K pares mais semelhantes acima do limiar |
| `busca` | compara apenas dois arquivos específicos |

Em todos os modos, a saída é exibida no terminal e gravada em `resultado.txt`.

## 4. Análise experimental

Os testes foram feitos com cinco documentos de exemplo. Com a métrica Cosseno e hash polinomial, foram processados 5 documentos e comparados 10 pares.

### 4.1 Colisões

| Documento | Colisões |
|---|---:|
| doc1.txt | 80 |
| doc2.txt | 91 |
| doc3.txt | 57 |
| doc4.txt | 57 |
| doc5.txt | 76 |

O número de colisões varia conforme o vocabulário de cada arquivo. Documentos com mais palavras distintas tendem a gerar mais inserções e, consequentemente, mais chances de colisão.

### 4.2 Similaridades encontradas

| Par | Similaridade |
|---|---:|
| doc3.txt - doc4.txt | 0,53 |
| doc1.txt - doc2.txt | 0,51 |
| doc2.txt - doc4.txt | 0,20 |
| doc2.txt - doc3.txt | 0,16 |
| doc1.txt - doc4.txt | 0,14 |
| doc1.txt - doc3.txt | 0,12 |
| doc1.txt - doc5.txt | 0,01 |
| doc4.txt - doc5.txt | 0,01 |
| doc2.txt - doc5.txt | 0,00 |
| doc3.txt - doc5.txt | 0,00 |

Os maiores valores aparecem entre documentos que tratam de temas próximos. O par `doc3.txt - doc4.txt` foi o mais semelhante no conjunto testado.

### 4.3 Rotações da AVL

Na execução testada, a árvore AVL realizou:

| Tipo de rotação | Quantidade |
|---|---:|
| Simples | 1 |
| Dupla | 2 |

Esse resultado mostra que a ordem de inserção dos valores de similaridade gerou alguns desequilíbrios, mas a árvore foi rebalanceada durante as inserções.

## 5. Discussão

Substituir a árvore AVL por uma heap teria vantagens apenas em consultas focadas no maior elemento. O acesso ao topo da heap é `O(1)`, mas obter os K maiores elementos exige remoções sucessivas, normalmente em `O(k log n)`.

Para este projeto, a AVL é mais adequada porque mantém os resultados ordenados e permite percorrer os pares acima ou abaixo de um limiar. Essa propriedade é útil principalmente no modo `lista`.

O pré-processamento também influencia bastante o resultado. Sem remoção de pontuação, por exemplo, `texto` e `texto,` seriam tratados como palavras diferentes. Sem remoção de stop words, textos de temas diferentes poderiam parecer semelhantes apenas por repetirem palavras muito comuns, como `de`, `a`, `o` e `para`.

Como melhoria, o projeto poderia incluir stemming ou lematização, para aproximar palavras da mesma família, como `computação`, `computador` e `computacional`.

## 6. Conclusão

O sistema lê múltiplos documentos, normaliza os textos, armazena frequências em tabelas hash e organiza os resultados em uma árvore AVL. A estrutura com lista de resultados em cada nó permite tratar corretamente pares com a mesma similaridade.

As maiores dificuldades foram manter a contagem correta de colisões, tratar similaridades repetidas na AVL e separar a lógica do programa para que a execução por linha de comando continuasse simples.

Durante os testes, um problema importante foi impedir que `resultado.txt` fosse lido como documento de entrada. A solução adotada foi filtrar esse arquivo na etapa de carregamento dos documentos.
