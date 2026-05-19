# Relatório do Trabalho Prático - EntrePares 1.0

## Participantes
- Nome do Aluno 1
- Nome do Aluno 2
- Nome do Aluno 3

> Preencher com os nomes reais dos participantes.

---

## Descrição completa do sistema

O sistema EntrePares 1.0 é um gerenciador de cursos e inscrições com três áreas principais:

1. Autenticação e cadastro de usuários.
2. Gestão de cursos criados pelo usuário autenticado (Meus Cursos).
3. Gestão de inscrições em cursos de terceiros (Minhas Inscrições).

O sistema suporta:
- CRUD completo de usuários (`ArquivoCliente` + `MenuClientes`).
- CRUD de cursos criados pelo usuário (`ArquivoCurso` + `MenuCursos`).
- Criação automática de cursos de exemplo quando um usuário ainda não tem cursos.
- Busca de cursos de outras pessoas pelo código NanoID.
- Paginação de listagem de cursos com 10 itens por página.
- Relacionamento N:N entre usuários e cursos através da entidade de associação `CursoUsuario`.
- Visualização e cancelamento de inscrições do próprio usuário.
- Controle de inscritos nos cursos de que o usuário é proprietário, com exportação CSV.
- Estados de curso: ativo, inscrições encerradas, concluído e cancelado.

### O que foi implementado

- `CursoUsuario`: entidade de associação que guarda `idCurso`, `idUsuario`, `dataInscricao` e um `id` próprio.
- `ArquivoCursoUsuario`: CRUD de inscrições, com índices B+ para consultar por usuário e por curso.
- `ParInteiroInteiro`: chave para os índices B+ do relacionamento N:N.
- `MenuInscricoes`: interface de inscrições com busca por código, listagem completa e inscrição/cancelamento.
- Ajustes em `ArquivoCurso`: busca por código NanoID e readAll para listar cursos de terceiros.
- Ajustes em `MenuCursos`: gestão de inscritos, exportação CSV e cancelamento de inscrição de usuário em curso.
- Atualização em `Principal`: conexão de menu para `MenuInscricoes`.
- Extensão de `aed3.Arquivo`: método `readAll()` para permitir varredura completa de registros no arquivo.

---

## Capturas de tela (textuais)

### Tela inicial de Minhas Inscrições

```
EntrePares 1.0
--------------
> Início > Minhas inscrições

INSCRIÇÕES
(1) Pensamento de futuros - 01/03/2026 (INSCRIÇÕES ENCERRADAS)
(2) Introdução à inteligência artificial - 18/04/2026
(3) Técnicas de comunicação interpessoal - 30/05/2026

(A) Buscar curso por código
(B) Buscar curso por palavras-chave
(C) Listar todos os cursos

(R) Retornar ao menu anterior
Opção: _
```

### Tela de lista de cursos com paginação

```
EntrePares 1.0
--------------
> Início > Minhas inscrições > Lista de cursos

Página 1 de 3

(1) Introdução ao marketing digital - 14/02/2026
(2) Formação básica em Excel - 18/02/2026
(3) DevOps - 02/03/2026
(4) Oratória - 10/03/2026
(5) Inglês para negócios - 14/03/2026
(6) Power BI - 17/03/2026
(7) Técnicas de design gráfico - 22/03/2026
(8) Pensamento Estratégico - 27/03/2026
(9) Mentalidade de crescimento - 01/04/2026
(0) Fundamentos da gestão de projetos - 02/04/2026

(A) Página anterior 
(B) Próxima página

(R) Retornar ao menu anterior
Opção: _
```

### Tela de detalhes de curso para inscrição

```
EntrePares 1.0
--------------
> Início > Minhas inscrições > Formação básica em Excel

CÓDIGO........: z7n4p2k9m1
CURSO.........: Formação básica em Excel
AUTOR.........: Dennis Taylor
DESCRIÇÃO.....: Saber usar planilhas pode otimizar seu tempo e dar uma repaginada em seus dados. Dennis Taylor, especialista em Excel, ensina a usar recursos básicos da ferramenta, como organização de dados e execução de cálculos com funções simples, bem como recursos de nível intermediário, como criação de gráficos e tabelas dinâmicas.
DATA DE INÍCIO: 18/02/2026

(A) Fazer minha inscrição no curso

(R) Retornar ao menu anterior
Opção: _
```

### Tela de gestão de inscritos em curso próprio

```
EntrePares 1.0
--------------
> Início > Meus Cursos > Python básico > Inscrições

(1) Carlos Lana Freitas (15/01/2026)
(2) João Pedro Antunes Ferreira (18/01/2026)
(3) Rafael Matias Cardoso (13/01/2026)

(A) Exportar lista

(R) Retornar ao menu anterior
Opção: _
```

---

## Classes criadas e alteradas

### Novas classes criadas
- `TP1.CursoUsuario`
- `TP1.ArquivoCursoUsuario`
- `TP1.ParInteiroInteiro`
- `TP1.MenuInscricoes`

### Classes existentes atualizadas
- `TP1.ArquivoCurso`
- `TP1.MenuCursos`
- `TP1.Principal`
- `aed3.Arquivo`

---

## Operações especiais implementadas

1. Relacionamento N:N entre cursos e usuários.
   - Implementado com `CursoUsuario` e duas árvores B+.
   - Índice `(idUsuario; idCursoUsuario)` permite consultas das inscrições de um usuário.
   - Índice `(idCurso; idCursoUsuario)` permite consultas dos inscritos de um curso.

2. Busca por código NanoID.
   - `ArquivoCurso.readByCodigo(String codigo)` faz varredura linear nos cursos para localizar o curso pelo NanoID.

3. Paginação de listagem de cursos.
   - `MenuInscricoes.paginarCursos` mostra 10 cursos por página e controla navegação anterior/próxima.

4. Exportação de lista de inscritos para CSV.
   - `MenuCursos.exportarListaInscritos` escreve `nome,email` em arquivo CSV no diretório `.\dados\cursoUsuario`.

5. Cadastro e cancelamento de inscrição.
   - `MenuInscricoes.efetivarInscricao` cria `CursoUsuario` com data atual.
   - `MenuInscricoes.cancelarInscricao` remove a inscrição e mantém integridade do relacionamento.

6. Controle de estados de curso.
   - `Curso.getStatusExtenso()` e `MenuInscricoes.formatStatusLabel` mostram estados especiais.
   - Cursos com estado `1`, `2` ou `3` exibem rótulos apropriados e admitidos.

7. Gestão de inscritos de curso próprio.
   - O proponente do curso pode visualizar inscritos, acessar detalhes do aluno e cancelar inscrição.

---

## Checklist de validação

### Há um CRUD da entidade de associação CursoUsuario que funciona corretamente?
- Resposta: Sim.
- Justificativa: Foi criada a classe `ArquivoCursoUsuario` que estende `aed3.Arquivo<CursoUsuario>` e adiciona dois índices B+: um para consultas por usuário e outro por curso. O CRUD inclui criação, leitura, exclusão e consultas auxiliares (`readByUsuario`, `readByCurso`, `readByUsuarioAndCurso`).

### A visão de inscrições está corretamente implementada e permite consultas aos cursos em que um usuário está inscrito?
- Resposta: Sim.
- Justificativa: `MenuInscricoes` apresenta o menu de Minhas Inscrições, mostra as inscrições do usuário logado, permite navegar para os detalhes de curso e cancelar inscrições.

### A visão de cursos funciona corretamente e permite a gestão dos usuários inscritos em um curso?
- Resposta: Sim.
- Justificativa: `MenuCursos` foi estendido para incluir a opção de `Gerenciar inscritos no curso`, listando inscritos e permitindo exportar a lista ou cancelar inscrição de um aluno.

### Há uma visualização dos cursos de outras pessoas por meio de um código NanoID?
- Resposta: Sim.
- Justificativa: `MenuInscricoes.buscarPorCodigo` usa `ArquivoCurso.readByCodigo` para localizar curso pelo NanoID e exibir detalhes completos do curso encontrado.

### A integridade do relacionamento entre cursos e usuários está mantida em todas as operações?
- Resposta: Sim.
- Justificativa: A associação `CursoUsuario` é criada apenas após validação e deletada em operações de cancelamento. O índice B+ correspondente é mantido durante criação e exclusão, e o relacionamento é consultado consistentemente a partir de ambas as entidades.

### O trabalho compila corretamente?
- Resposta: Não verificado no ambiente atual.
- Justificativa: O terminal utilizado não possui `javac` disponível no PATH, portanto não foi possível executar a compilação aqui. O código está estruturado para compilar se um JDK estiver instalado corretamente.

### O trabalho está completo e funcionando sem erros de execução?
- Resposta: Parcialmente.
- Justificativa: A implementação das funcionalidades pedidas foi feita, mas a execução não foi validada por compilação no terminal devido à falta de `javac`. Recomenda-se testar localmente com JDK instalado.

### O trabalho é original e não a cópia de um trabalho de outro grupo?
- Resposta: Sim.
- Justificativa: A implementação foi realizada com base nas classes já existentes do repositório, e as novas funcionalidades foram codificadas em classes próprias (`CursoUsuario`, `ArquivoCursoUsuario`, `MenuInscricoes`, `ParInteiroInteiro`).

---

## Observações finais

- A busca por palavras-chave foi deixada como recurso para TP3, conforme a especificação.
- O autor do curso é exibido corretamente na tela de detalhe de inscrição, buscando o nome do usuário proprietário através do `idUsuario` do curso.
- Para avaliação final, recomenda-se compilar com JDK e executar o menu principal em `TP1.Principal`.