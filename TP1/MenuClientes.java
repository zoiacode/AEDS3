package TP1;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class MenuClientes {
    
    ArquivoCliente arqClientes;
    private static Scanner console = new Scanner(System.in);

    public MenuClientes() throws Exception {
        arqClientes = new ArquivoCliente();
    }

    public void menu() {

        int opcao;
        do {

            System.out.println("\n\nAEDsIII");
            System.out.println("-------");
            System.out.println("> Início > Clientes");
            System.out.println("\n1 - Buscar");
            System.out.println("2 - Incluir");
            System.out.println("3 - Alterar");
            System.out.println("4 - Excluir");
            System.out.println("0 - Voltar");

            System.out.print("\nOpção: ");
            try {
                opcao = Integer.valueOf(console.nextLine());
            } catch(NumberFormatException e) {
                opcao = -1;
            }

            switch (opcao) {
                case 1:
                    buscarCliente();
                    break;
                case 2:
                    incluirCliente();
                    break;
                case 3:
                    alterarCliente();
                    break;
                case 4:
                    excluirCliente();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida!");
                    break;
            }

        } while (opcao != 0);
    }


    public void buscarCliente() {
        System.out.println("\nBusca de cliente");
        String email;
        boolean emailValido = false;

        do {
            System.out.print("\nEmail: ");
            email = console.nextLine();  // Lê o email digitado pelo usuário

            if(email.isEmpty())
                return; 

            // Validação do email (formato básico)
            if (email.contains("@") && email.contains(".")) {
                emailValido = true;  // Email válido
            } else {
                System.out.println("Email inválido. O email deve conter '@' e '.com'.");
            }
        } while (!emailValido);

        try {
            Cliente cliente = arqClientes.read(email);  // Chama o método de leitura da classe Arquivo
            if (cliente != null) {
                mostraCliente(cliente);  // Exibe os detalhes do cliente encontrado
            } else {
                System.out.println("Cliente não encontrado.");
            }
        } catch(Exception e) {
            System.out.println("Erro do sistema. Não foi possível buscar o cliente!");
            e.printStackTrace();
        }
    }   


    public void incluirCliente() {
        System.out.println("\nInclusão de cliente");
        int id = -1;
        String nome = "";
        String email = "";
        String senha = "";
        String perguntaSecreta = "";
        String respostaSecreta = "";
        boolean dadosCorretos = false;

        do {
            System.out.print("\nNome (min. de 4 letras ou vazio para cancelar): ");
            nome = console.nextLine();
            if(nome.length()==0)
                return;
            if(nome.length()<4)
                System.err.println("O nome do cliente deve ter no mínimo 4 caracteres.");
        } while(nome.length()<4);

        do {
            System.out.print("Email: ");
            email = console.nextLine();
        } while(email.isEmpty());

        do {
            System.out.print("Pergunta secreta: ");
            perguntaSecreta = console.nextLine();
        } while(perguntaSecreta.isEmpty());

        do {
            System.out.print("Resposta secreta: ");
            respostaSecreta = console.nextLine();
        } while(respostaSecreta.isEmpty());

        do {
            System.out.print("Senha: ");
            senha = console.nextLine();
        } while(senha.isEmpty());

        System.out.print("\nConfirma a inclusão do usuário? (S/N) ");
        char resp = console.nextLine().charAt(0);
        if(resp=='S' || resp=='s') {
            try {
                Cliente c = new Cliente(id, nome, email, senha, perguntaSecreta, respostaSecreta);
                arqClientes.create(c);
                System.out.println("Cliente incluído com sucesso.");
            } catch(Exception e) {
                System.out.println("Erro do sistema. Não foi possível incluir o cliente!");
            }
        }
    }

    public void alterarCliente() {
        System.out.println("\nAlteração de cliente");
        String email;
        boolean emailValido = false;

        do {
            System.out.print("\nEmail: ");
            email = console.nextLine();  // Lê o email digitado pelo usuário

            if(email.isEmpty())
                return; 

            // Validação do email (formato básico)
            if (email.contains("@") && email.contains(".")) {
                emailValido = true;  // Email válido
            } else {
                System.out.println("Email inválido. O email deve conter '@' e '.']");
            }
        } while (!emailValido);


        try {
            // Tenta ler o cliente com o ID fornecido
            Cliente cliente = arqClientes.read(email);
            if (cliente != null) {
                System.out.println("Cliente encontrado:");
                mostraCliente(cliente);  // Exibe os dados do cliente para confirmação

                // Alteração de email
                System.out.print("\nNovo email (deixe em branco para manter o anterior): ");
                String novoEmail = console.nextLine();
                if (!novoEmail.isEmpty()) {
                    cliente.email = novoEmail;  // Atualiza o email se fornecido
                }

                // Confirmação da alteração
                System.out.print("\nConfirma as alterações? (S/N) ");
                char resp = console.next().charAt(0);
                if (resp == 'S' || resp == 's') {
                    // Salva as alterações no arquivo
                    boolean alterado = arqClientes.update(cliente);
                    if (alterado) {
                        System.out.println("Cliente alterado com sucesso.");
                    } else {
                        System.out.println("Erro ao alterar o cliente.");
                    }
                } else {
                    System.out.println("Alterações canceladas.");
                }
            } else {
                System.out.println("Cliente não encontrado.");
            }
        } catch (Exception e) {
            System.out.println("Erro do sistema. Não foi possível alterar o cliente!");
            e.printStackTrace();
        }
        
    }


    public void excluirCliente() {
        System.out.println("\nExclusão de cliente");
        String emailString;
        boolean emailValido = false;

        do {
            System.out.print("\nEmail: ");
            emailString = console.nextLine();  // Lê o email digitado pelo usuário

            if(emailString.isEmpty())
                return; 

            // Validação do email (formato básico)
            if (emailString.contains("@") && emailString.contains(".")) {
                emailValido = true;  // Email válido
            } else {
                System.out.println("Email inválido. O email deve conter '@' e '.']");
            }
        } while (!emailValido);

        try {
            // Tenta ler o cliente com o ID fornecido
            Cliente cliente = arqClientes.read(emailString);
            if (cliente != null) {
                System.out.println("Cliente encontrado:");
                mostraCliente(cliente);  // Exibe os dados do cliente para confirmação

                System.out.print("\nConfirma a exclusão do cliente? (S/N) ");
                char resp = console.next().charAt(0);  // Lê a resposta do usuário

                if (resp == 'S' || resp == 's') {
                    boolean excluido = arqClientes.delete(emailString);  // Chama o método de exclusão no arquivo
                    if (excluido) {
                        System.out.println("Cliente excluído com sucesso.");
                    } else {
                        System.out.println("Erro ao excluir o cliente.");
                    }
                } else {
                    System.out.println("Exclusão cancelada.");
                }
            } else {
                System.out.println("Cliente não encontrado.");
            }
        } catch (Exception e) {
            System.out.println("Erro do sistema. Não foi possível excluir o cliente!");
            e.printStackTrace();
        }
    }


    public void mostraCliente(Cliente cliente) {
    if (cliente != null) {
        System.out.println("\nDetalhes do Cliente:");
        System.out.println("----------------------");
        System.out.printf("Nome......: %s%n", cliente.nome);
        System.out.printf("Email.....: %s%n", cliente.email);
        System.out.println("----------------------");
    }
}
}