package TP1;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class MenuCursos {
    
    private ArquivoCurso arqCursos;
    private static Scanner console = new Scanner(System.in);

    public MenuCursos() throws Exception {
        arqCursos = new ArquivoCurso();
    }

    /**
     * Menu principal de cursos do usuário logado
     * @param usuario Cliente logado
     */
    public void menu(Cliente usuario) {
        String opcao;
        do {
            System.out.println("\nEntrePares 1.0");
            System.out.println("--------------");
            System.out.println("> Início > Meus Cursos");
            System.out.println("\nOlá, " + usuario.nome + "!");
            
            listarCursos(usuario.getId());
            
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
                        // Implementação futura para selecionar curso por número
                        System.out.println("[Funcionalidade em desenvolvimento]");
                    } catch (NumberFormatException e) {
                        System.out.println("Opção inválida!");
                    }
            }
        } while (!opcao.equals("R"));
    }

    /**
     * Lista todos os cursos do usuário
     * @param idUsuario ID do usuário
     */
    private void listarCursos(int idUsuario) {
        System.out.println("\nSEUS CURSOS:");
        System.out.println("-----------");
        try {
            int[] cursosIds = arqCursos.readByUsuario(idUsuario);
            if (cursosIds == null || cursosIds.length == 0) {
                System.out.println("Você ainda não criou nenhum curso.");
            } else {
                for (int i = 0; i < cursosIds.length; i++) {
                    Curso c = arqCursos.read(cursosIds[i]);
                    if (c != null) {
                        System.out.printf("(%d) %s - %s\n", 
                            i + 1, c.nome, c.dataInicio);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar cursos!");
            e.printStackTrace();
        }
    }

    /**
     * Cria um novo curso vinculado ao usuário
     * @param usuario Cliente proprietário do curso
     */
    public void novoCurso(Cliente usuario) {
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
                System.out.println("\nCurso criado com sucesso! ID: " + id);
            } catch (Exception e) {
                System.out.println("Erro ao criar curso!");
                e.printStackTrace();
            }
        }
    }

    /**
     * Exibe os detalhes completos de um curso
     * @param curso Curso a ser exibido
     */
    private void exibirDetalhesCurso(Curso curso) {
        if (curso != null) {
            System.out.println("\nEntrePares 1.0");
            System.out.println("--------------");
            System.out.println("> Início > Meus Cursos > " + curso.nome);
            System.out.println();
            System.out.printf("CÓDIGO........: %s%n", curso.codigoNanoID);
            System.out.printf("NOME..........: %s%n", curso.nome);
            System.out.printf("DESCRIÇÃO.....: %s%n", curso.descricao);
            System.out.printf("DATA DE INÍCIO: %s%n", curso.dataInicio);
            System.out.println();
            System.out.println("Este curso " + (curso.estado == 0 ? "está aberto para inscrições!" : "não está em inscrições."));
        }
    }

    /**
     * Permite editar os dados de um curso
     * @param usuario Proprietário do curso
     */
    public void editarCurso(Cliente usuario) {
        System.out.println("\nEdição de Curso");
        System.out.println("---------------");
        
        // Pedir ID do curso
        System.out.print("ID do curso a editar: ");
        try {
            int idCurso = Integer.parseInt(console.nextLine());
            Curso curso = arqCursos.read(idCurso);
            
            if (curso == null) {
                System.out.println("Curso não encontrado.");
                return;
            }
            
            if (curso.getIdUsuario() != usuario.getId()) {
                System.out.println("Você não tem permissão para editar este curso.");
                return;
            }
            
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
                arqCursos.update(curso);
                System.out.println("Curso atualizado com sucesso!");
            }
        } catch (Exception e) {
            System.out.println("Erro ao editar curso!");
            e.printStackTrace();
        }
    }

    /**
     * Encerra inscrições no curso
     * @param usuario Proprietário do curso
     */
    public void encerrarInscricoes(Cliente usuario) {
        System.out.println("\nEncerrar Inscrições");
        System.out.println("-------------------");
        
        System.out.print("ID do curso: ");
        try {
            int idCurso = Integer.parseInt(console.nextLine());
            Curso curso = arqCursos.read(idCurso);
            
            if (curso == null || curso.getIdUsuario() != usuario.getId()) {
                System.out.println("Curso não encontrado ou acesso negado.");
                return;
            }
            
            if (curso.estado == 0) {
                curso.estado = 1; // Ativo, mas sem novas inscrições
                arqCursos.update(curso);
                System.out.println("Inscrições encerradas com sucesso!");
            } else {
                System.out.println("Inscrições já estão encerradas.");
            }
        } catch (Exception e) {
            System.out.println("Erro ao encerrar inscrições!");
            e.printStackTrace();
        }
    }

    /**
     * Marca um curso como concluído
     * @param usuario Proprietário do curso
     */
    public void concluirCurso(Cliente usuario) {
        System.out.println("\nConcluir Curso");
        System.out.println("--------------");
        
        System.out.print("ID do curso: ");
        try {
            int idCurso = Integer.parseInt(console.nextLine());
            Curso curso = arqCursos.read(idCurso);
            
            if (curso == null || curso.getIdUsuario() != usuario.getId()) {
                System.out.println("Curso não encontrado ou acesso negado.");
                return;
            }
            
            System.out.print("Confirma a conclusão do curso? (S/N): ");
            if (console.nextLine().toUpperCase().charAt(0) == 'S') {
                curso.estado = 2; // Realizado e concluído
                arqCursos.update(curso);
                System.out.println("Curso marcado como concluído!");
            }
        } catch (Exception e) {
            System.out.println("Erro ao concluir curso!");
            e.printStackTrace();
        }
    }

    /**
     * Cancela um curso
     * @param usuario Proprietário do curso
     */
    public void cancelarCurso(Cliente usuario) {
        System.out.println("\nCancelar Curso");
        System.out.println("--------------");
        
        System.out.print("ID do curso: ");
        try {
            int idCurso = Integer.parseInt(console.nextLine());
            Curso curso = arqCursos.read(idCurso);
            
            if (curso == null || curso.getIdUsuario() != usuario.getId()) {
                System.out.println("Curso não encontrado ou acesso negado.");
                return;
            }
            
            System.out.print("Confirma o cancelamento? (S/N): ");
            if (console.nextLine().toUpperCase().charAt(0) == 'S') {
                curso.estado = 3; // Cancelado
                arqCursos.update(curso);
                System.out.println("Curso cancelado!");
            }
        } catch (Exception e) {
            System.out.println("Erro ao cancelar curso!");
            e.printStackTrace();
        }
    }

    /**
     * Deleta um curso (apenas se não houver inscritos)
     * @param usuario Proprietário do curso
     */
    public void deletarCurso(Cliente usuario) {
        System.out.println("\nDeletar Curso");
        System.out.println("-------------");
        
        System.out.print("ID do curso: ");
        try {
            int idCurso = Integer.parseInt(console.nextLine());
            Curso curso = arqCursos.read(idCurso);
            
            if (curso == null || curso.getIdUsuario() != usuario.getId()) {
                System.out.println("Curso não encontrado ou acesso negado.");
                return;
            }
            
            // Verificação: só permite deletar se não há inscritos (TP2)
            // Por enquanto, sempre permite
            System.out.print("Confirma a exclusão? (S/N): ");
            if (console.nextLine().toUpperCase().charAt(0) == 'S') {
                if (arqCursos.delete(idCurso)) {
                    System.out.println("Curso deletado com sucesso!");
                } else {
                    System.out.println("Erro ao deletar curso!");
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao deletar curso!");
            e.printStackTrace();
        }
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
