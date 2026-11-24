package com.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Field;

public class BancoDados implements SalvarArquivoTxt{

    @Override
    public void salvarClienteTxt(Cliente novoCliente, String url) throws Exception {
        BufferedWriter writer = null;
        try {
             writer= new BufferedWriter(new FileWriter(url,true));
            writer.write(novoCliente.salvarCliente());
            writer.newLine();
        } catch (Exception e) {
            throw new Exception ("Erro ao salvar novo Cliente no arquvo txt");
        }finally{
            writer.close();
        }
    }

    @Override
    public Cliente carregarClienteTxt(String id, String url) throws Exception {
        BufferedReader reader = null;
        Cliente clienteSelecionado = new Cliente();
        try {
            reader=new BufferedReader(new FileReader(url));
            String linha = reader.readLine();
            while (linha!=null) {
                if(linha.substring(0,linha.indexOf(",")).equals(id)){
                    String [] atributos = linha.split(",");
                    Field[] fields = clienteSelecionado.getClass().getDeclaredFields();
                    for(int i=0;i<fields.length;i++){
                        fields[i].setAccessible(true);
                        fields[i].set(clienteSelecionado, atributos[i]);
                    }
                }else{
                    linha=reader.readLine();
                }    
            }
        } catch (Exception e) {
            throw new Exception("Erro ao carregar cliente do arquivo txt. provavelmente o cliente não está na lista");
        }
        return clienteSelecionado;
    }

    

}
