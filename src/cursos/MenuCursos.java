package src.cursos;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import src.usuarios.Usuario;
import src.usuarios.ArquivoUsuario;
import src.inscricoes.ArquivoInscricao;
import src.inscricoes.Inscricao;

public class MenuCursos {
    
    private ArquivoCurso arqCursos;
    private ArquivoInscricao arqInscricoes;
    private ArquivoUsuario arqClientes;
    private static Scanner console = new Scanner(System.in);

    public MenuCursos() throws Exception {
        arqCursos = new ArquivoCurso();
        arqInscricoes = new ArquivoInscricao();
        arqClientes = new ArquivoUsuario();
    }

    /**
     * Menu principal de cursos do usuário logado
     * @param usuario Usuario logado
     */
    public void menu(Usuario usuario) {
        String opcao;
        do {
            System.out.println("\nEntrePares 1.0");
            System.out.println("--------------");
            System.out.println("> Início > Meus Cursos");
            System.out.println("\nOlá, " + usuario.nome + "!");
            
            int[] cursosIds = listarCursos(usuario.getId());
            
            System.out.println("\n(A) Novo curso");
            System.out.println("(R) Retornar ao menu anterior");
            System.out.print("\nOpção: ");
            opcao = console.nextLine().toUpperCase();
            
            switch (opcao) {
                case "A":
                    novoCurso(usuario);
                    break;
                case "R":
                    break;
                default:
                    // Tenta interpretar como número para selecionar um curso
                    try {
                        int numCurso = Integer.parseInt(opcao);
                        if (numCurso >= 1 && numCurso <= cursosIds.length) {
                            int idCurso = cursosIds[numCurso - 1];
                            try {
                                Curso curso = arqCursos.read(idCurso);
                                if (curso != null) {
                                    menuCurso(curso, usuario);
                                } else {
                                    System.out.println("Erro: Curso não encontrado.");
                                }
                            } catch (Exception e) {
                                System.out.println("Erro ao ler curso: " + e.getMessage());
                            }
                        } else {
                            System.out.println("Número de curso inválido!");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Opção inválida!");
                    }
            }
        } while (!opcao.equals("R"));
    }

    /**
     * Lista todos os cursos do usuário ordenados alfabeticamente por nome
     * @param idUsuario ID do usuário
     * @return Array de IDs dos cursos na ordem de exibição
     */
    private int[] listarCursos(int idUsuario) {
        System.out.println("\nSEUS CURSOS:");
        System.out.println("-----------");
        try {
            int[] cursosIds = arqCursos.readByUsuario(idUsuario);
            if (cursosIds == null || cursosIds.length == 0) {
                System.out.println("Você ainda não criou nenhum curso.");
                return new int[0];
            } else {
                // Busca todos os cursos e ordena por nome
                java.util.List<Curso> cursos = new java.util.ArrayList<>();
                for (int id : cursosIds) {
                    Curso c = arqCursos.read(id);
                    if (c != null) {
                        cursos.add(c);
                    }
                }
                
                // Ordena por nome alfabeticamente
                cursos.sort((c1, c2) -> c1.nome.compareToIgnoreCase(c2.nome));
                
                // Exibe os cursos ordenados
                for (int i = 0; i < cursos.size(); i++) {
                    Curso c = cursos.get(i);
                    System.out.printf("(%d) %s - %s\n", 
                        i + 1, c.nome, c.dataInicio);
                }
                
                // Retorna os IDs na ordem de exibição
                int[] idsOrdenados = new int[cursos.size()];
                for (int i = 0; i < cursos.size(); i++) {
                    idsOrdenados[i] = cursos.get(i).getId();
                }
                
                return idsOrdenados;
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar cursos!");
            e.printStackTrace();
            return new int[0];
        }
    }

    /**
     * Cria um novo curso vinculado ao usuário
     * @param usuario Usuario proprietário do curso
     */
    public void novoCurso(Usuario usuario) {
        System.out.println("\nCriação de Novo Curso");
        System.out.println("---------------------");
        
        String nome = "";
        String dataInicio = "";
        String descricao = "";
        
        // Validação e leitura do nome do curso
        do {
            System.out.print("\nNome do curso (mínimo 3 caracteres ou vazio para cancelar): ");
            nome = console.nextLine();
            if (nome.isEmpty()) {
                return;
            }
            if (nome.length() < 3) {
                System.out.println("O nome deve ter no mínimo 3 caracteres.");
            }
        } while (nome.length() < 3);

        // Validação e leitura da data de início
        do {
            System.out.print("Data de início (formato DD/MM/YYYY): ");
            dataInicio = console.nextLine();
            if (dataInicio.isEmpty()) {
                System.out.println("Data é obrigatória.");
            } else if (!validarDataFormatoDDMMAA(dataInicio)) {
                System.out.println("Data inválida. Use o formato DD/MM/YYYY.");
            }
        } while (dataInicio.isEmpty() || !validarDataFormatoDDMMAA(dataInicio));

        // Leitura da descrição
        do {
            System.out.print("Descrição do curso: ");
            descricao = console.nextLine();
            if (descricao.isEmpty()) {
                System.out.println("Descrição é obrigatória.");
            }
        } while (descricao.isEmpty());

        // Confirmação antes de criar
        System.out.print("\nConfirma a criação do curso? (S/N): ");
        char resp = console.nextLine().toUpperCase().charAt(0);
        
        if (resp == 'S') {
            try {
                // Cria o novo curso (o ID será atribuído automaticamente)
                // Estado inicial: 0 = Ativo e recebendo inscrições
                Curso novoCurso = new Curso(
                    -1,                    // ID será atribuído automaticamente
                    usuario.getId(),       // ID do usuário proprietário
                    nome,                  
                    dataInicio,           
                    descricao,            
                    "",                   // codigoNanoID será gerado automaticamente
                    (byte) 0              // Estado: Ativo e recebendo inscrições
                );
                
                int id = arqCursos.create(novoCurso);
                System.out.println("\nCurso criado com sucesso! Código NanoId: " + novoCurso.codigoNanoID);
            } catch (Exception e) {
                System.out.println("Erro ao criar curso!");
                e.printStackTrace();
            }
        }
    }

    /**
     * Menu de um curso específico
     * @param curso Curso selecionado
     * @param usuario Proprietário do curso
     */
    private void menuCurso(Curso curso, Usuario usuario) {
        String opcao;
        do {
            System.out.println("\nEntrePares 1.0");
            System.out.println("--------------");
            System.out.println("> Início > Meus Cursos > " + curso.nome);
            System.out.println();
            System.out.printf("CÓDIGO........: %s%n", curso.codigoNanoID);
            System.out.printf("NOME..........: %s%n", curso.nome);
            System.out.printf("DESCRIÇÃO.....: %s%n", curso.descricao);
            System.out.printf("DATA DE INÍCIO: %s%n", curso.dataInicio);
            System.out.println();
            System.out.println("Este curso " + curso.getStatusExtenso().toLowerCase() + "!");

            System.out.println("\n(A) Gerenciar inscritos no curso");
            System.out.println("(B) Corrigir dados do curso");
            System.out.println("(C) Encerrar inscrições");
            System.out.println("(D) Concluir curso");
            System.out.println("(E) Cancelar curso");
            System.out.println("\n(R) Retornar ao menu anterior");
            System.out.print("\nOpção: ");
            opcao = console.nextLine().toUpperCase();

            switch (opcao) {
                case "A":
                    gerenciarInscritos(curso, usuario);
                    break;
                case "B":
                    editarCurso(curso, usuario);
                    break;
                case "C":
                    encerrarInscricoes(curso, usuario);
                    break;
                case "D":
                    concluirCurso(curso, usuario);
                    break;
                case "E":
                    cancelarCurso(curso, usuario);
                    break;
                case "R":
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (!opcao.equals("R"));
    }

    /**
     * Permite editar os dados de um curso
     * @param curso Curso a editar
     * @param usuario Proprietário do curso
     */
    public void editarCurso(Curso curso, Usuario usuario) {
        System.out.println("\nEdição de Curso");
        System.out.println("---------------");
        
        exibirDetalhesCurso(curso);
        
        // Menu de edição
        System.out.println("\n(1) Editar nome");
        System.out.println("(2) Editar data de início");
        System.out.println("(3) Editar descrição");
        System.out.println("(0) Cancelar");
        System.out.print("\nOpção: ");
        
        String opcao = console.nextLine();
        switch (opcao) {
            case "1":
                System.out.print("Novo nome: ");
                curso.nome = console.nextLine();
                break;
            case "2":
                System.out.print("Nova data (DD/MM/YYYY): ");
                String novaData = console.nextLine();
                if (validarDataFormatoDDMMAA(novaData)) {
                    curso.dataInicio = novaData;
                } else {
                    System.out.println("Data inválida!");
                    return;
                }
                break;
            case "3":
                System.out.print("Nova descrição: ");
                curso.descricao = console.nextLine();
                break;
            case "0":
                return;
        }
        
        System.out.print("Confirma as alterações? (S/N): ");
        if (console.nextLine().toUpperCase().charAt(0) == 'S') {
            try {
                arqCursos.update(curso);
                System.out.println("Curso atualizado com sucesso!");
            } catch (Exception e) {
                System.out.println("Erro ao atualizar curso!");
                e.printStackTrace();
            }
        }
    }

    /**
     * Encerra inscrições no curso
     * @param curso Curso a modificar
     * @param usuario Proprietário do curso
     */
    public void encerrarInscricoes(Curso curso, Usuario usuario) {
        System.out.println("\nEncerrar Inscrições");
        System.out.println("-------------------");
        
        if (curso.estado == 0) {
            System.out.print("Confirma encerrar inscrições? (S/N): ");
            if (console.nextLine().toUpperCase().charAt(0) == 'S') {
                curso.estado = 1; // Ativo, mas sem novas inscrições
                try {
                    arqCursos.update(curso);
                    System.out.println("Inscrições encerradas com sucesso!");
                } catch (Exception e) {
                    System.out.println("Erro ao encerrar inscrições!");
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("Este curso já não aceita inscrições.");
        }
    }

    /**
     * Marca um curso como concluído
     * @param curso Curso a modificar
     * @param usuario Proprietário do curso
     */
    public void concluirCurso(Curso curso, Usuario usuario) {
        System.out.println("\nConcluir Curso");
        System.out.println("--------------");
        
        System.out.print("Confirma marcar curso como concluído? (S/N): ");
        if (console.nextLine().toUpperCase().charAt(0) == 'S') {
            curso.estado = 2; // Concluído
            try {
                arqCursos.update(curso);
                System.out.println("Curso marcado como concluído!");
            } catch (Exception e) {
                System.out.println("Erro ao concluir curso!");
                e.printStackTrace();
            }
        }
    }

    /**
     * Cancela um curso
     * @param curso Curso a cancelar
     * @param usuario Proprietário do curso
     */
    public void cancelarCurso(Curso curso, Usuario usuario) {
        System.out.println("\nCancelar Curso");
        System.out.println("--------------");
        
        // No TP1, não há alunos, então sempre cancela
        System.out.print("Confirma cancelar o curso? (S/N): ");
        if (console.nextLine().toUpperCase().charAt(0) == 'S') {
            try {
                curso.estado = 3; // Cancelado
                if (arqCursos.update(curso)) {
                    System.out.println("Curso cancelado com sucesso!");
                } else {
                    System.out.println("Erro ao cancelar curso!");
                }
            } catch (Exception e) {
                System.out.println("Erro ao cancelar curso!");
                e.printStackTrace();
            }
        }
    }

    private void gerenciarInscritos(Curso curso, Usuario usuario) {
        try {
            int[] inscricoesIds = arqInscricoes.readByCurso(curso.getId());
            if (inscricoesIds == null || inscricoesIds.length == 0) {
                System.out.println("\nNenhum inscrito neste curso.");
                return;
            }

            List<Inscricao> inscricoes = new ArrayList<>();
            for (int idInscricao : inscricoesIds) {
                Inscricao inscricao = arqInscricoes.read(idInscricao);
                if (inscricao != null) {
                    inscricoes.add(inscricao);
                }
            }

            boolean sair = false;
            while (!sair) {
                System.out.println("\nEntrePares 1.0");
                System.out.println("--------------");
                System.out.println("> Início > Meus Cursos > " + curso.nome + " > Inscrições");
                System.out.println();

                for (int i = 0; i < inscricoes.size(); i++) {
                    Inscricao inscricao = inscricoes.get(i);
                    Usuario aluno = arqClientes.read(inscricao.getIdUsuario());
                    if (aluno != null) {
                        System.out.printf("(%d) %s (%s)\n", i + 1, aluno.nome, inscricao.dataInscricao);
                    }
                }

                System.out.println();
                System.out.println("(A) Exportar lista");
                System.out.println("\n(R) Retornar ao menu anterior");
                System.out.print("\nOpção: ");
                String opcao = console.nextLine().toUpperCase();

                if (opcao.equals("A")) {
                    exportarListaInscritos(curso, inscricoes);
                } else if (opcao.equals("R")) {
                    sair = true;
                } else {
                    try {
                        int escolha = Integer.parseInt(opcao);
                        if (escolha >= 1 && escolha <= inscricoes.size()) {
                            Inscricao inscricao = inscricoes.get(escolha - 1);
                            Usuario aluno = arqClientes.read(inscricao.getIdUsuario());
                            if (aluno != null) {
                                menuDetalhesInscrito(curso, inscricao, aluno);
                                inscricoes = new ArrayList<>();
                                int[] idsAtualizados = arqInscricoes.readByCurso(curso.getId());
                                if (idsAtualizados != null) {
                                    for (int idInscricao : idsAtualizados) {
                                        Inscricao inscricaoAtual = arqInscricoes.read(idInscricao);
                                        if (inscricaoAtual != null) {
                                            inscricoes.add(inscricaoAtual);
                                        }
                                    }
                                }
                            }
                        } else {
                            System.out.println("Número inválido!");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Opção inválida!");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao gerenciar inscritos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void exportarListaInscritos(Curso curso, List<Inscricao> inscricoes) {
        try {
            File pasta = new File(".\\dados\\cursoUsuario");
            if (!pasta.exists()) {
                pasta.mkdirs();
            }
            File arquivoCSV = new File(pasta, "inscritos-" + curso.codigoNanoID + ".csv");
            try (FileWriter writer = new FileWriter(arquivoCSV)) {
                writer.write("nome,email\n");
                for (Inscricao inscricao : inscricoes) {
                    Usuario aluno = arqClientes.read(inscricao.getIdUsuario());
                    if (aluno != null) {
                        writer.write(aluno.nome.replace("\n", " ") + "," + aluno.email.replace("\n", " ") + "\n");
                    }
                }
            }
            System.out.println("Lista exportada para: " + arquivoCSV.getAbsolutePath());
        } catch (Exception e) {
            System.out.println("Erro ao exportar lista: " + e.getMessage());
        }
    }

    private void menuDetalhesInscrito(Curso curso, Inscricao inscricao, Usuario aluno) {
        System.out.println("\nEntrePares 1.0");
        System.out.println("--------------");
        System.out.println("> Início > Meus Cursos > " + curso.nome + " > Inscrições > " + aluno.nome);
        System.out.println();
        System.out.printf("NOME......: %s%n", aluno.nome);
        System.out.printf("EMAIL.....: %s%n", aluno.email);
        System.out.printf("INSCRIÇÃO.: %s%n", inscricao.dataInscricao);
        System.out.println();
        System.out.println("(A) Cancelar inscrição");
        System.out.println("\n(R) Retornar ao menu anterior");
        System.out.print("\nOpção: ");
        String opcao = console.nextLine().toUpperCase();

        if (opcao.equals("A")) {
            try {
                arqInscricoes.delete(inscricao.getId());
                System.out.println("Inscrição cancelada com sucesso!");
            } catch (Exception e) {
                System.out.println("Erro ao cancelar inscrição: " + e.getMessage());
            }
        }
    }

    /**
     * Exibe os detalhes de um curso
     * @param curso Curso a ser exibido
     */
    private void exibirDetalhesCurso(Curso curso) {
        System.out.println("\nCÓDIGO........: " + curso.codigoNanoID);
        System.out.println("NOME..........: " + curso.nome);
        System.out.println("DESCRIÇÃO.....: " + curso.descricao);
        System.out.println("DATA DE INÍCIO: " + curso.dataInicio);
        
        // Mostra o estado do curso
        System.out.println("\n" + curso.getStatusExtenso() + "!");
    }

    /**
     * Valida se a data está no formato DD/MM/YYYY
     * @param data String com a data
     * @return true se válida
     */
    private boolean validarDataFormatoDDMMAA(String data) {
        if (data.length() != 10 || data.charAt(2) != '/' || data.charAt(5) != '/') {
            return false;
        }
        try {
            int dia = Integer.parseInt(data.substring(0, 2));
            int mes = Integer.parseInt(data.substring(3, 5));
            int ano = Integer.parseInt(data.substring(6, 10));
            
            if (dia < 1 || dia > 31 || mes < 1 || mes > 12 || ano < 2000) {
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}


