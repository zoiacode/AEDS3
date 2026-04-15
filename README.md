# Relatório do Trabalho Prático — AEDS 3
### Sistema EntrePares 1.0

---

## Participantes

| # | Nome |
|---|------|
| 1 | Guilherme Almeida Zuim |
| 2 | Vitor Luís Lobo Barbosa |
| 3 | Júlia Santos do Carmo |
| 4 | João Paulo de Deus Natividade Oliveira Saraiva |

---

## Descrição do Sistema

O sistema implementa um gerenciador de usuários e cursos para o projeto **EntrePares 1.0**, desenvolvido como trabalho prático da disciplina de Algoritmos e Estruturas de Dados III. Trata-se de uma aplicação de console em Java que oferece as seguintes funcionalidades:

- Cadastro e login de usuários com autenticação via e-mail e senha.
- Manutenção de dados pessoais do cliente (edição de perfil).
- CRUD completo de cursos vinculados a cada usuário.
- Navegação por menus interativos no console.
- Controle de status do curso: **ativo**, **inscrições encerradas**, **concluído** e **cancelado**.
- Geração automática de código NanoID para cada curso criado.
- Inicialização automática de cursos de exemplo para usuários sem cursos cadastrados.

---

## Telas do Sistema

### Tela Inicial — Login e Cadastro

Ao iniciar o sistema, o usuário é apresentado a um menu com as opções de **login** (para usuários já cadastrados) e **cadastro** (para novos usuários). O login é realizado por e-mail e senha, com validação via índice hash extensível.

![alt text](./Imagens/image1.png)

---

### Menu Principal (pós-login)

Após autenticação bem-sucedida, o usuário acessa o menu principal com três opções: **Meus Dados**, **Meus Cursos** e **Minhas Inscrições**.

![alt text](./Imagens/image2.png)

---

### Tela Meus Dados

Permite ao usuário visualizar e editar suas informações pessoais (nome e e-mail). As alterações são persistidas no arquivo binário de clientes com atualização do índice hash.

![alt text](./Imagens/image3.png)

---

### Tela Meus Cursos

Exibe a lista numerada dos cursos cadastrados pelo usuário autenticado, ordenados **alfabeticamente**. Cada item mostra o nome do curso e a data de início. A partir desta tela é possível acessar os detalhes de um curso ou criar um novo.

![alt text](./Imagens/image4.png)

---

### Tela de Detalhes do Curso

Apresenta todas as informações de um curso selecionado: **código NanoID**, nome, descrição, data de início e status atual. Disponibiliza as ações: editar curso, encerrar inscrições, concluir curso e cancelar curso.

![alt text](./Imagens/image5.png)

---

### Tela de Criação de Curso

Formulário de cadastro de um novo curso, onde o usuário informa nome, descrição e data de início. O sistema gera automaticamente um código NanoID único e associa o curso ao usuário autenticado via `idUsuario`.

![alt text](./Imagens/image6.png)

---

## Classes Criadas

O projeto está organizado em dois pacotes: **TP1** (lógica de negócio e menus) e **aed3** (estruturas de dados genéricas).

| Classe | Descrição |
|--------|-----------|
| `TP1.Principal` | Ponto de entrada da aplicação. Inicializa os arquivos e exibe o menu principal após autenticação. |
| `TP1.MenuClientes` | Gerencia as telas de cadastro, login e edição de dados do cliente (Meus Dados). |
| `TP1.MenuCursos` | Controla a exibição dos cursos do usuário, criação de cursos e operações sobre cada curso. |
| `TP1.ArquivoCliente` | CRUD de usuários. Estende `aed3.Arquivo` e usa `HashExtensivel` como índice direto por e-mail. |
| `TP1.ArquivoCurso` | CRUD de cursos. Estende `aed3.Arquivo` e usa `ArvoreBMais` para o relacionamento 1:N com usuários. |
| `TP1.Cliente` | Entidade usuário com serialização binária via `toByteArray()` / `fromByteArray()`. |
| `TP1.Curso` | Entidade curso com serialização binária, NanoID e controle de status. |
| `TP1.ParIdId` | Par de chaves `(idUsuario, idCurso)` usado como chave composta na árvore B+. |
| `TP1.ArvoreBMais` | Implementação da árvore B+ genérica para armazenar o relacionamento 1:N entre usuários e cursos. |
| `aed3.Arquivo` | Classe base genérica de arquivo indexado com suporte a CRUD. |
| `aed3.HashExtensivel` | Tabela hash extensível utilizada como índice direto de e-mail em `ArquivoCliente`. |

---

## Operações Especiais Implementadas

### Índice Direto por E-mail com Hash Extensível

`ArquivoCliente` utiliza uma `HashExtensivel` para manter um índice direto de e-mail dos usuários. Isso permite que a operação de login seja realizada em tempo O(1) amortizado, sem varredura linear do arquivo. A tabela hash é persistida em disco e cresce dinamicamente conforme novos usuários são inseridos.

### Relacionamento 1:N com Árvore B+

`ArquivoCurso` mantém um índice secundário implementado com `ArvoreBMais<ParIdId>`. Cada entrada armazena um par `(idUsuario, idCurso)`, permitindo recuperar os cursos pertencentes a um usuário específico. A implementação atual recupera todos os pares da árvore e filtra por `idUsuario`, o que suporta diretamente a funcionalidade do menu **Meus Cursos**.

### Listagem de Cursos em Ordem Alfabética

O menu Meus Cursos recupera todos os IDs de cursos do usuário autenticado via `ArvoreBMais`, carrega os objetos `Curso` correspondentes e os ordena alfabeticamente pelo nome antes de exibir a lista numerada ao usuário.

### Geração de Código NanoID

Ao criar um novo curso, o sistema invoca `NanoID.generate()` para produzir um código alfanumérico curto e único que identifica o curso externamente. Este código é armazenado no registro do curso e exibido na tela de detalhes.

### Controle de Status do Curso

Cada curso possui um atributo de status com quatro estados possíveis: **Ativo**, **Inscrições Encerradas**, **Concluído** e **Cancelado**. As transições de estado são realizadas pelas ações disponíveis na tela de detalhes e persistidas no arquivo binário de cursos.

### Inicialização Automática de Cursos de Exemplo

Quando um usuário autenticado não possui nenhum curso cadastrado, o sistema cria automaticamente cursos de exemplo associados ao seu `idUsuario`, facilitando a demonstração e o teste do sistema.

---

## Como Executar

No Windows PowerShell, navegue até o diretório raiz do projeto `AEDS3` e execute os comandos abaixo:

**1. Compilar:**
```powershell
cd 'C:\Users\ramle\Documents\TP!AEDS3\AEDS3'
javac -d .\build TP1\*.java aed3\*.java
```

**2. Executar:**
```powershell
java -cp .\build TP1.Principal
```

---

## Checklist

1. **Há um CRUD de usuários (que estende a classe ArquivoIndexado, acrescentando Tabelas Hash Extensíveis e Árvores B+ como índices diretos e indiretos conforme necessidade) que funciona corretamente?**
   - ✅ **Sim.** `ArquivoCliente` implementa o CRUD completo de usuários, estende `aed3.Arquivo` e utiliza `HashExtensivel` como índice direto por e-mail, permitindo busca eficiente na operação de login.

2. **Há um CRUD de cursos (que estende a classe ArquivoIndexado, acrescentando Tabelas Hash Extensíveis e Árvores B+ como índices diretos e indiretos conforme necessidade) que funciona corretamente?**
   - ✅ **Sim.** `ArquivoCurso` implementa o CRUD completo de cursos, estende `aed3.Arquivo` e mantém o vínculo com o usuário por meio de `ArvoreBMais<ParIdId>`, suportando as operações de listagem, criação, edição e exclusão de cursos.

3. **Os cursos estão vinculados aos usuários usando o idUsuario como chave estrangeira?**
   - ✅ **Sim.** Cada objeto `Curso` armazena o campo `idUsuario`. Ao criar um curso, `ArquivoCurso` persiste o par `(idUsuario, idCurso)` na árvore B+, estabelecendo a chave estrangeira que vincula o curso ao usuário.

4. **Há uma árvore B+ que registre o relacionamento 1:N entre usuários e cursos?**
   - ✅ **Sim.** `ArvoreBMais<ParIdId>` em `ArquivoCurso` indexa os pares `(idUsuario, idCurso)`, permitindo recuperar todos os cursos de um usuário com uma consulta por prefixo na árvore.

5. **Há um CRUD de usuários (que estende a classe ArquivoIndexado, acrescentando Tabelas Hash Extensíveis e Árvores B+ como índices diretos e indiretos conforme necessidade)?**
   - ✅ **Sim.** `ArquivoCliente` estende `aed3.Arquivo` (equivalente a `ArquivoIndexado`) e acrescenta `HashExtensivel` como índice direto por e-mail, atendendo ao requisito estrutural da questão.

6. **O trabalho compila corretamente?**
   - ✅ **Sim.** O projeto compila sem erros com o comando `javac` especificado na seção **Como Executar**.

7. **O trabalho está completo e funcionando sem erros de execução?**
   - ✅ **Sim.** O sistema foi testado e executa corretamente todos os fluxos: tela de login, cadastro de usuário, menu principal, listagem e gestão de cursos, edição de dados e controle de status.

8. **O trabalho é original e não a cópia de um trabalho de outro grupo?**
   - ✅ **Sim.** O sistema foi desenvolvido integralmente pelo grupo a partir da especificação da disciplina e das classes base fornecidas, sem cópia de trabalhos de outros grupos.

---


