package src.inscricoes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import src.usuarios.Usuario;
import src.cursos.Curso;
import src.usuarios.ArquivoUsuario;
import src.cursos.ArquivoCurso;

public class MenuInscricoes {

    private ArquivoCurso arqCursos;
    private ArquivoInscricao arqInscricoes;
    private ArquivoUsuario arqClientes;
    private static Scanner console = new Scanner(System.in);

    public MenuInscricoes() throws Exception {
        arqCursos = new ArquivoCurso();
        arqInscricoes = new ArquivoInscricao();
        arqClientes = new ArquivoUsuario();
    }

    public void menu(Usuario usuario) {
        String opcao;
        do {
            System.out.println("\nEntrePares 1.0");
            System.out.println("--------------");
            System.out.println("> Início > Minhas inscrições");
            System.out.println();
            System.out.println("INSCRIÇÕES");

            List<Inscricao> minhasInscricoes = carregarInscricoesDoUsuario(usuario);
            if (minhasInscricoes.isEmpty()) {
                System.out.println("Você ainda não possui inscrições.");
            } else {
                for (int i = 0; i < minhasInscricoes.size(); i++) {
                    Inscricao inscricao = minhasInscricoes.get(i);
                    try {
                        Curso curso = arqCursos.read(inscricao.getIdCurso());
                        if (curso != null) {
                            System.out.printf("(%d) %s - %s%s\n",
                                i + 1,
                                curso.nome,
                                curso.dataInicio,
                                formatStatusLabel(curso)
                            );
                        }
                    } catch (Exception e) {
                        System.out.println("Erro ao carregar inscrição: " + e.getMessage());
                    }
                }
            }

            System.out.println();
            System.out.println("(A) Buscar curso por código");
            System.out.println("(B) Buscar curso por palavras-chave");
            System.out.println("(C) Listar todos os cursos");
            System.out.println();
            System.out.println("(R) Retornar ao menu anterior");
            System.out.print("\nOpção: ");
            opcao = console.nextLine().toUpperCase();

            switch (opcao) {
                case "A":
                    buscarPorCodigo(usuario);
                    break;
                case "B":
                    buscarPorPalavras(usuario);
                    break;
                case "C":
                    listarTodosCursos(usuario);
                    break;
                case "R":
                    break;
                default:
                    try {
                        int escolha = Integer.parseInt(opcao);
                        if (escolha >= 1 && escolha <= minhasInscricoes.size()) {
                            Inscricao inscricao = minhasInscricoes.get(escolha - 1);
                            try {
                                Curso curso = arqCursos.read(inscricao.getIdCurso());
                                if (curso != null) {
                                    exibirDetalhesCursoParaInscricao(curso, usuario);
                                } else {
                                    System.out.println("Curso não encontrado.");
                                }
                            } catch (Exception e) {
                                System.out.println("Erro ao ler curso: " + e.getMessage());
                            }
                        } else {
                            System.out.println("Número de inscrição inválido!");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Opção inválida!");
                    }
            }
        } while (!opcao.equals("R"));
    }

    private List<Inscricao> carregarInscricoesDoUsuario(Usuario usuario) {
        try {
            int[] inscricoesIds = arqInscricoes.readByUsuario(usuario.getId());
            List<Inscricao> resultado = new ArrayList<>();
            if (inscricoesIds != null) {
                for (int idInscricao : inscricoesIds) {
                    Inscricao inscricao = arqInscricoes.read(idInscricao);
                    if (inscricao != null) {
                        resultado.add(inscricao);
                    }
                }
            }
            return resultado;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private void buscarPorCodigo(Usuario usuario) {
        System.out.print("\nCódigo do curso: ");
        String codigo = console.nextLine();
        try {
            Curso curso = arqCursos.readByCodigo(codigo);
            if (curso == null) {
                System.out.println("Curso não encontrado.");
                return;
            }
            exibirDetalhesCursoParaInscricao(curso, usuario);
        } catch (Exception e) {
            System.out.println("Erro ao buscar curso: " + e.getMessage());
        }
    }

    private void listarTodosCursos(Usuario usuario) {
        try {
            List<Curso> cursos = arqCursos.readAll();
            if (cursos.isEmpty()) {
                System.out.println("\nNenhum curso cadastrado.");
                return;
            }
            cursos.sort(Comparator.comparing(c -> parseData(c.dataInicio)));
            paginarCursos(cursos, usuario);
        } catch (Exception e) {
            System.out.println("Erro ao listar cursos: " + e.getMessage());
        }
    }

    private void paginarCursos(List<Curso> cursos, Usuario usuario) {
        int total = cursos.size();
        int paginaAtual = 0;
        int paginas = (total + 9) / 10;

        while (true) {
            int inicio = paginaAtual * 10;
            int fim = Math.min(inicio + 10, total);

            System.out.println("\nEntrePares 1.0");
            System.out.println("--------------");
            System.out.println("> Início > Minhas inscrições > Lista de cursos");
            System.out.println();
            System.out.printf("Página %d de %d\n\n", paginaAtual + 1, paginas);

            for (int i = inicio; i < fim; i++) {
                Curso curso = cursos.get(i);
                int numero = i - inicio + 1;
                String marcador = (numero == 10) ? "0" : String.valueOf(numero);
                System.out.printf("(%s) %s - %s%s\n", marcador, curso.nome, curso.dataInicio, formatStatusLabel(curso));
            }

            System.out.println();
            if (paginaAtual > 0) {
                System.out.println("(A) Página anterior");
            }
            if (paginaAtual < paginas - 1) {
                System.out.println("(B) Próxima página");
            }
            System.out.println();
            System.out.println("(R) Retornar ao menu anterior");
            System.out.print("\nOpção: ");
            String opcao = console.nextLine().toUpperCase();

            if (opcao.equals("A") && paginaAtual > 0) {
                paginaAtual--;
                continue;
            }
            if (opcao.equals("B") && paginaAtual < paginas - 1) {
                paginaAtual++;
                continue;
            }
            if (opcao.equals("R")) {
                return;
            }

            try {
                int escolha = Integer.parseInt(opcao);
                if (escolha == 0) {
                    escolha = 10;
                }
                if (escolha >= 1 && escolha <= fim - inicio) {
                    Curso curso = cursos.get(inicio + escolha - 1);
                    exibirDetalhesCursoParaInscricao(curso, usuario);
                } else {
                    System.out.println("Número de curso inválido!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Opção inválida!");
            }
        }
    }

    private Date parseData(String data) {
        try {
            String[] partes = data.split("/");
            int dia = Integer.parseInt(partes[0]);
            int mes = Integer.parseInt(partes[1]);
            int ano = Integer.parseInt(partes[2]);
            return new Date(ano - 1900, mes - 1, dia);
        } catch (Exception e) {
            return new Date(0);
        }
    }

    private String formatStatusLabel(Curso curso) {
        switch (curso.estado) {
            case 1:
                return " (INSCRIÇÕES ENCERRADAS)";
            case 2:
                return " (CURSO JÁ REALIZADO)";
            case 3:
                return " (CURSO CANCELADO)";
            default:
                return "";
        }
    }

    private void exibirDetalhesCursoParaInscricao(Curso curso, Usuario usuario) {
        boolean inscrito = false;
        try {
            inscrito = arqInscricoes.readByUsuarioAndCurso(usuario.getId(), curso.getId()) != null;
        } catch (Exception e) {
            inscrito = false;
        }

        System.out.println("\nEntrePares 1.0");
        System.out.println("--------------");
        System.out.println("> Início > Minhas inscrições > " + curso.nome);
        System.out.println();
        System.out.printf("CÓDIGO........: %s%n", curso.codigoNanoID);
        System.out.printf("CURSO.........: %s%n", curso.nome);
        System.out.printf("AUTOR.........: %s%n", getAutorDoCurso(curso));
        System.out.printf("DESCRIÇÃO.....: %s%n", curso.descricao);
        System.out.printf("DATA DE INÍCIO: %s%n", curso.dataInicio);
        System.out.println();

        if (inscrito) {
            System.out.println("(A) Cancelar minha inscrição no curso");
        } else if (curso.estado == 0) {
            System.out.println("(A) Fazer minha inscrição no curso");
        } else {
            System.out.println("Este curso não está aberto para inscrições.");
        }

        System.out.println();
        System.out.println("(R) Retornar ao menu anterior");
        System.out.print("\nOpção: ");
        String opcao = console.nextLine().toUpperCase();

        switch (opcao) {
            case "A":
                if (inscrito) {
                    cancelarInscricao(usuario, curso);
                } else {
                    efetivarInscricao(usuario, curso);
                }
                break;
            case "R":
                break;
            default:
                System.out.println("Opção inválida!");
        }
    }

    private String getAutorDoCurso(Curso curso) {
        try {
            Usuario autor = arqClientes.read(curso.getIdUsuario());
            if (autor != null) {
                return autor.nome;
            }
        } catch (Exception e) {
            // Ignora erro e retorna padrão
        }
        return "Não disponível";
    }

    private void efetivarInscricao(Usuario usuario, Curso curso) {
        try {
            if (arqInscricoes.readByUsuarioAndCurso(usuario.getId(), curso.getId()) != null) {
                System.out.println("Você já está inscrito neste curso.");
                return;
            }

            if (curso.estado != 0) {
                System.out.println("Não é possível inscrever-se neste curso neste momento.");
                return;
            }

            String data = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
            Inscricao novaInscricao = new Inscricao(
                -1,
                curso.getId(),
                usuario.getId(),
                data
            );
            arqInscricoes.create(novaInscricao);
            System.out.println("Inscrição realizada com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao efetivar inscrição: " + e.getMessage());
        }
    }

    private void cancelarInscricao(Usuario usuario, Curso curso) {
        try {
            Inscricao inscricao = arqInscricoes.readByUsuarioAndCurso(usuario.getId(), curso.getId());
            if (inscricao == null) {
                System.out.println("Você não está inscrito neste curso.");
                return;
            }
            arqInscricoes.delete(inscricao.getId());
            System.out.println("Inscrição cancelada com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao cancelar inscrição: " + e.getMessage());
        }
    }

    private void buscarPorPalavras(Usuario usuario) {
        System.out.println("\nEntrePares 1.0");
        System.out.println("--------------");
        System.out.println("> Início > Minhas inscrições > Busca por palavras-chave");
        System.out.println();
        System.out.print("Digite as palavras-chave: ");
        String consulta = console.nextLine().trim();

        if (consulta.isEmpty()) {
            System.out.println("Nenhuma palavra informada.");
            return;
        }

        try {
            List<Curso> cursos = arqCursos.buscarPorPalavras(consulta);

            if (cursos.isEmpty()) {
                System.out.println("\nNenhum curso encontrado para \"" + consulta + "\".");
                return;
            }

            System.out.println("\nResultados para \"" + consulta + "\" (" + cursos.size() + " encontrado(s)):\n");
            for (int i = 0; i < cursos.size(); i++) {
                Curso c = cursos.get(i);
                System.out.printf("(%d) %s - %s%s%n",
                    i + 1, c.nome, c.dataInicio, formatStatusLabel(c));
            }

            System.out.println("\nDigite o número para ver detalhes ou (R) para retornar:");
            System.out.print("Opção: ");
            String opcao = console.nextLine().trim().toUpperCase();

            if (!opcao.equals("R")) {
                try {
                    int escolha = Integer.parseInt(opcao);
                    if (escolha >= 1 && escolha <= cursos.size()) {
                        exibirDetalhesCursoParaInscricao(cursos.get(escolha - 1), usuario);
                    } else {
                        System.out.println("Número inválido.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Opção inválida.");
                }
            }
        } catch (Exception e) {
            System.out.println("Erro na busca: " + e.getMessage());
            e.printStackTrace();
        }
    }
}


