package com.example;

import java.util.Scanner;

import org.jcp.xml.dsig.internal.dom.ApacheOctetStreamData;

/**
 * Hello world!
 *
 */
public class App {

    public static int id=0;
    public static final String URL="C:\\Users\\Bruno\\Searches\\EBAC\\BACKEND\\Exercício módulo 28\\demo\\src\\main\\java\\com\\example\\ListaClientes.txt";
    private static BancoDados bd = new BancoDados();

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
        int confirma=Integer.parseInt(scanner.nextLine());
        if(confirma==1){
            Cliente novoCliente = new Cliente(String.valueOf(App.getId()), nome,profissao);
            try {
                App.bd.salvarClienteTxt(novoCliente, App.URL);
            } catch (Exception e) {
                // TODO: handle exception
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
                clienteSelecionado.toString();
            }
        } catch (Exception e) {
            throw new Exception("Erro ao selecionar cliente no arquivo txt");
        }
    }

    public static void gerarXML(){

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
            opcao=Integer.parseInt(scanner.nextLine());

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
                    App.gerarXML();
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
