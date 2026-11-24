package com.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;

import javax.swing.text.Document;
import javax.swing.text.html.parser.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Node;

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

    private static ArrayList<Cliente> carrgarListaClientesTxt(String url) throws Exception{
        
        BufferedReader reader=null;
        ArrayList <Cliente> listaClientes = new ArrayList<>();
        try {
            reader=new BufferedReader(new FileReader(url));
            String linha = reader.readLine();
            while(linha!=null){
                if(linha.trim().isEmpty()){
                    continue;
                }else{
                    Cliente clienteAtual = new Cliente();
                    String [] atributos = linha.split(",");
                    Field [] fields = clienteAtual.getClass().getDeclaredFields();
                    for(int i=0;i<fields.length;i++){
                        fields[i].setAccessible(true);
                        fields[i].set(clienteAtual, atributos[i].trim());
                    }
                    listaClientes.add(clienteAtual);
                    linha=reader.readLine();
                }
            }
        } catch (Exception e) {
            throw new Exception("Erro ao gerar lista de clinetes a partir do arquivo txt");
        }finally{
            reader.close();
        }
        return listaClientes;
    }

    
    public void gerarXML(String url){

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {

            File arquivo = new File(url);
            if(arquivo.exists()){
                arquivo.delete();
            }

            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = (Document) documentBuilder.newDocument();

            org.w3c.dom.Element raiz = ((org.w3c.dom.Document) document).createElement("listaClientes");
            ((Node) document).appendChild(raiz);

            ArrayList <Cliente> listaClientes = BancoDados.carrgarListaClientesTxt(url);

            for(Cliente clienteAtual :listaClientes){
                org.w3c.dom.Element cliente = ((org.w3c.dom.Document) document).createElement("cliente");
                raiz.appendChild(cliente);

                Field[] listaAtributos = clienteAtual.getClass().getDeclaredFields();
                for(Field atributo : listaAtributos){
                    atributo.setAccessible(true);
                    
                    org.w3c.dom.Element campo = ((org.w3c.dom.Document) document).createElement(atributo.getName());
                    Object valor = atributo.get(clienteAtual);
                    campo.appendChild(((org.w3c.dom.Document) document).createTextNode(valor!=null?valor.toString():""));
                    cliente.appendChild(campo);
                }
            }

            TransformerFactory transformerFactory = TransformerFactory.newDefaultInstance()
            Transformer transformer = transformerFactory.newTransformer();

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://apache.org}indent-amount", "4");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            DOMSource source = new DOMSource();
            StreamResult result = new StreamResult(arquivo);
            transformer.transform(source, result);

        } catch (Exception e) {
            throw new Exception("Erro ao gerar arquivo xml");
        }

    }
    
    



    

}
