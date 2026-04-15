# RESUMO DE IMPLEMENTAÇÃO - EntrePares TP1

## Projeto: Sistema de Gestão de Cursos Online

### ✅ IMPLEMENTAÇÕES REALIZADAS

#### 1. **NanoID.java** (NOVO)
- Gerador de códigos alfanuméricos aleatórios (padrão NanoID)
- Tamanho padrão: 10 caracteres
- Alfabeto: números, letras minúsculas/maiúsculas, e caracteres especiais (_-)
- Método estático: `generate()` e `generate(int size)`

#### 2. **ParIdId.java** (NOVO)
- Implementa `RegistroArvoreBMais<ParIdId>`
- Representa relação 1:N entre Usuário e Curso
- Armazena: `idUsuario` (4 bytes) + `idCurso` (4 bytes) = 8 bytes totais
- Métodos obrigatórios:
  - `size()` → 8
  - `toByteArray()` / `fromByteArray()`
  - `compareTo()` → primeiro por idUsuario, depois por idCurso
  - `clone()`

#### 3. **ArquivoCurso.java** (NOVO)
- Estende `Arquivo<Curso>` para CRUD genérico com Hash Extensível
- Mantém uma **Árvore B+** para armazenar relação 1:N
- Arquivo: `.\\dados\\cursos\\relacao.b.db` (Ordem 4)
- Métodos principais:
  ```java
  create(Curso)                    // Cria e vincula ao usuário
  read(int id)                     // Lê pelo ID
  readByUsuario(int idUsuario)     // Retorna int[] com IDs dos cursos
  delete(int id)                   // Deleta e remove vinculação
  update(Curso)                    // Atualiza dados
  deleteByUsuario(int idUsuario)   // Deleta todos os cursos do usuário
  ```

#### 4. **MenuCursos.java** (NOVO)
- Interface textual para gerenciar cursos
- Menu principal estruturado com breadcrumb
- Recursos:
  - Listar cursos do usuário logado
  - Criar novo curso (valida data DD/MM/YYYY)
  - Editar dados do curso
  - Encerrar inscrições
  - Concluir curso
  - Cancelar curso
  - Deletar curso

#### 5. **Principal.java** (JÁ ESTAVA PRONTO)
- Menu principal de login/novo usuário
- Menu após autenticação com opções:
  - (A) Meus dados → chama `MenuClientes.gerenciarDados()`
  - (B) Meus cursos → chama `MenuCursos.menu(usuario)`
  - (C) Minhas inscrições → [Indisponível em TP1]
  - (S) Sair

---

## 🏗️ ARQUITETURA

### Estrutura de Diretórios
```
.\\dados\\
  ├── clientes\\
  │   ├── clientes.db
  │   ├── clientes.d.db (diretório hash)
  │   └── clientes.c.db (cestos hash)
  │
  └── cursos\\
      ├── cursos.db
      ├── cursos.d.db (diretório hash)
      ├── cursos.c.db (cestos hash)
      └── relacao.b.db (Árvore B+ de ParIdId)
```

### Estrutura de Dados

**Curso** (8 atributos):
```
id: int
idUsuario: int (FK)
nome: String
dataInicio: String (DD/MM/YYYY)
descricao: String
codigoNanoID: String (10 chars)
estado: byte (0=Ativo, 1=Ativo-Sem-Inscrições, 2=Concluído, 3=Cancelado)
```

**Cliente** (6 atributos):
```
id: int
nome: String
email: String (chave hash)
senha: String
perguntaSecreta: String
respostaSecreta: String
```

### Índices Utilizados

1. **Hash Extensível** (Cliente):
   - Chave: email
   - Valor: id e endereço
   - Recupera cliente por email (login)

2. **Hash Extensível** (Curso):
   - Chave: id
   - Valor: endereço
   - Recupera curso por ID

3. **Árvore B+** (Relação 1:N):
   - Chave: (idUsuario, idCurso)
   - Recupera todos os cursos de um usuário
   - Permite busca ordenada e eficiente

---

## 📋 FLUXO DE USO

### Login e Cadastro
```
Início → (A) Login / (B) Novo Usuário
         ↓
         Email + Senha
         ↓
    Menu Logado (Usuário Autenticado)
```

### Menu de Cursos
```
Meus Cursos
  ├─ Listar Cursos do Usuário
  ├─ (A) Novo Curso
  │   ├─ Nome (min. 3 caracteres)
  │   ├─ Data DD/MM/YYYY
  │   ├─ Descrição
  │   └─ Gera automaticamente: ID, codigoNanoID
  │
  └─ Operações em Curso Existente:
      ├─ Editar dados
      ├─ Encerrar inscrições
      ├─ Concluir
      ├─ Cancelar
      └─ Deletar
```

---

## ⚠️ OBSERVAÇÕES IMPORTANTES

1. **Hash de Senha**: Atualmente armazenado como string. 
   - ⚠️ Implementação futura deve usar hash seguro (SHA-256 ou bcrypt)

2. **NanoID**: Implementação adaptada do padrão NanoID
   - Cada novo curso recebe automaticamente um código de 10 caracteres
   - Ideal para compartilhamento entre usuários

3. **Validação de Data**: Formato DD/MM/YYYY
   - Validações básicas (dia 1-31, mês 1-12, ano ≥ 2000)
   - Poderia ser melhorado com calendar validation

4. **Relação 1:N**: Implementada via Árvore B+
   - Suporta busca eficiente de todos os cursos de um usuário
   - Mantém integridade referencial

5. **TP2 - Funcionalidades Futuras**:
   - Sistema de inscrições em cursos
   - Índice de nome de curso (Árvore B+)
   - Verificação de inscritos antes de deletar
   - Busca por código compartilhável

---

## 🧪 TESTES RECOMENDADOS

1. Criar usuário e fazer login
2. Criar novo curso (valida automaticamente o NanoID)
3. Listar cursos do usuário
4. Editar curso existente
5. Mudar estado do curso (encerrar → concluir → cancelar)
6. Deletar curso

