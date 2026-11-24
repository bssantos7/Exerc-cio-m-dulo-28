package com.example;

import java.io.File;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App {

    public static int id=0;
    public static final String DIRETORIO_PROJETO = new File(
        new File(System.getProperty("user.dir"), "src/main/java/com/example").isDirectory()
            ? System.getProperty("user.dir")
            : new File(System.getProperty("user.dir"), "demo").getAbsolutePath(),
        "src/main/java/com/example").getAbsolutePath();
    public static final String URL = DIRETORIO_PROJETO + File.separator + "ListaClientes.txt";
    public static final String URL_ARQXML = DIRETORIO_PROJETO + File.separator + "arquivo.xml";
    private static BancoDados bd = new BancoDados();

    private static int lerInteiro(Scanner scanner) {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Digite apenas números.");
            return -1;
        }
    }

    public static void setId(){
        App.id++;
    }

    public static int getId(){
        return App.id;
    }

    public static void cadastrarNovoCliente(Scanner scanner){
        System.out.println("Digite o nome: ");
        String nome=scanner.nextLine();
        System.out.println("Digite a profissão: ");
        String profissao = scanner.nextLine();

        System.out.println("Confirma o cadastro? ");
        System.out.println("Digite 1 para sim ou 2 para não");
        int confirma=lerInteiro(scanner);
        if(confirma==1){
            Cliente novoCliente = new Cliente(String.valueOf(App.getId()), nome,profissao);
            App.setId();
            try {
                App.bd.salvarClienteTxt(novoCliente, App.URL);
            } catch (Exception e) {
                System.out.println("Erro ao cadastrar cliente: " + e.getMessage());
            }
        }else{
            System.out.println("Operacao cancelada pelo usuário");
        }

    }

    
    public static  void selecionarCliente(Scanner scanner) throws Exception{
        System.out.println("Digite o código do cliente: ");
        String id = scanner.nextLine();
        Cliente clienteSelecionado=null;
        try {
            clienteSelecionado=App.bd.carregarClienteTxt(id, App.URL);
            if(clienteSelecionado==null){
                System.out.println("Cliente não encontrado. tente novamente");
            }else{
                System.out.println(clienteSelecionado);
            }
        } catch (Exception e) {
            throw new Exception("Erro ao selecionar cliente no arquivo txt");
        }
    }

    
    public static void main( String[] args ){

        Scanner scanner=new Scanner(System.in);
        int opcao=0;

        System.out.println( "Bem vindo ao programa" );
        do{
            System.out.println("Digite a opção desejada: ");
            System.out.println("1 - cadastrar Novo Cliente");
            System.out.println("2 - Selecionar Cliente pelo ID");
            System.out.println("3 - Gerar Arquivo XML");
            System.out.println("0 - Finalizar Programa");
            opcao=lerInteiro(scanner);

            switch (opcao) {
                case 1:
                    try {
                        App.cadastrarNovoCliente(scanner);        
                    } catch (Exception e) {
                        e.printStackTrace();
                    }    
                    break;
                
                case 2:
                    try {
                        App.selecionarCliente(scanner);    
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                
                case 3:
                    try {
                        App.bd.gerarXML(App.URL_ARQXML);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                
                case 0:
                    System.out.println("Programa finalizado pelo usuário");
                    scanner.close();
                    break;
            
                default:
                    System.out.println("Numero invalido. tente novamente");
                    break;
            }
        }while(opcao!=0);

    }
}
