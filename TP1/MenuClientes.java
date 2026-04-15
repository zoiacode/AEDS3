package TP1;
import java.util.Scanner;

public class MenuClientes {
    
    ArquivoCliente arqClientes;
    private static Scanner console = new Scanner(System.in);

    public MenuClientes() throws Exception {
        arqClientes = new ArquivoCliente();
    }

    public Cliente login() {
        System.out.println("\nENTREPARES 1.0 - LOGIN");
        System.out.println("-----------------------");
        System.out.print("Email: ");
        String email = console.nextLine();
        System.out.print("Senha: ");
        String senha = console.nextLine();

        try {
            email = email.trim();
            // O arqClientes.read deve usar a Tabela Hash para achar o email
            Cliente c = arqClientes.read(email);

            if (c != null && c.senha.equals(String.valueOf(senha.hashCode()))) {
                System.out.println("\nLogin efetuado com sucesso!");
                return c;
            } else {
                System.out.println("\n[Erro] Email ou senha incorretos.");
            }
        } catch (Exception e) {
            System.out.println("Erro ao acessar dados de segurança.");
        }
        return null;
    }

    public void gerenciarDados(Cliente logado) {
        System.out.println("\n> Início > Meus Dados");
        mostraCliente(logado);

        System.out.println("\n(1) Alterar meus dados");
        System.out.println("(2) Excluir minha conta");
        System.out.println("(0) Voltar");
        System.out.print("\nOpção: ");
        
        String opt = console.nextLine();
        switch (opt) {
            case "1": alterarDadosLogado(logado); break;
            case "2": excluirContaLogado(logado); break;
        }
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
                if(arqClientes.read(email) != null) {
                    System.out.println("Erro: Este e-mail já está cadastrado!");
                    return;
                }
                String senhaHash = String.valueOf(senha.hashCode());
                Cliente c = new Cliente(id, nome, email, senhaHash, perguntaSecreta, respostaSecreta);
                arqClientes.create(c);
                System.out.println("Usuário cadastrado com sucesso!");
            } catch(Exception e) {
                System.out.println("Erro ao salvar.");
                e.printStackTrace();
            }
        }
    }

    public void alterarDadosLogado(Cliente logado) {
        System.out.println("\nAlteração de cliente");
        String email = logado.email;
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


    public void excluirContaLogado(Cliente logado) {
        System.out.println("\nExclusão de cliente");
        String emailString = logado.email;
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