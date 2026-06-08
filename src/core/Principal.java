package src.core;

import java.util.Scanner;
import src.usuarios.MenuUsuarios;
import src.cursos.MenuCursos;
import src.inscricoes.MenuInscricoes;
import src.usuarios.Usuario;

public class Principal {

    public static void main(String[] args) {

        Scanner console;

        try {
            console = new Scanner(System.in);
            String opcao; // Alterado para String para aceitar (A, B, S) conforme o PDF
            
            do {
                System.out.println("\nEntrePares 1.0");
                System.out.println("--------------");
                System.out.println("> Início");
                System.out.println("\n(A) Login");
                System.out.println("(B) Novo usuário");
                System.out.println("(C) Recuperar senha");
                System.out.println("\n(S) Sair");

                System.out.print("\nOpção: ");
                opcao = console.nextLine().toUpperCase();

                switch (opcao) {
                    case "A":
                        // O login deve retornar o usuário autenticado para ser usado depois
                        Usuario usuarioLogado = (new MenuUsuarios()).login();
                        if (usuarioLogado != null) {
                            menuLogado(usuarioLogado, console);
                        }
                        break;
                    case "B":
                        (new MenuUsuarios()).incluirCliente();
                        break;
                    case "C":
                        (new MenuUsuarios()).recuperarSenha();
                        break;
                    case "S":
                        System.out.println("Encerrando o sistema...");
                        break;
                    default:
                        System.out.println("Opção inválida!");
                        break;
                }

            } while (!opcao.equals("S"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Este método representa a tela principal após o login
    public static void menuLogado(Usuario usuario, Scanner console) {
        String opcao;
        do {
            System.out.println("\n AEDS III");
            System.out.println("-----------------");
            System.out.println("> Início");
            System.out.println("\nOlá, " + usuario.nome + "!");
            System.out.println("(A) Meus dados");
            System.out.println("(B) Meus cursos");
            System.out.println("(C) Minhas inscrições");
            System.out.println("\n(S) Sair");

            System.out.print("\nOpção: ");
            opcao = console.nextLine().toUpperCase();

            switch (opcao) {
                case "A":
                    try {
                        (new MenuUsuarios()).gerenciarDados(usuario);
                    } catch (Exception e) {
                        System.out.println("Erro ao abrir o menu de dados do cliente.");
                        e.printStackTrace();
                    }
                    break;
                case "B":
                    try {
                        (new MenuCursos()).menu(usuario); // Passa o usuário para filtrar os cursos dele
                    } catch (Exception e) {
                        System.out.println("Erro ao abrir o menu de cursos.");
                        e.printStackTrace();
                    }
                    break;
                case "C":
                    try {
                        (new MenuInscricoes()).menu(usuario);
                    } catch (Exception e) {
                        System.out.println("Erro ao abrir o menu de inscrições.");
                        e.printStackTrace();
                    }
                    break;
                case "S":
                    System.out.println("Fazendo logout...");
                    break;
                default:
                    System.out.println("Opção inválida!");
                    break;
            }
        } while (!opcao.equals("S"));
    }
}

