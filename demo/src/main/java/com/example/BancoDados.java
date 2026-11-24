package com.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class BancoDados implements SalvarArquivoTxt{

    @Override
    public void salvarClienteTxt(Cliente novoCliente, String url) throws Exception {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(url, true))) {
            writer.write(novoCliente.salvarCliente());
            writer.newLine();
        } catch (Exception e) {
            throw new Exception("Erro ao salvar novo cliente no arquivo txt", e);
        }
    }

    @Override
    public Cliente carregarClienteTxt(String id, String url) throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader(url))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (linha.trim().isEmpty()) {
                    continue;
                }

                String[] atributos = linha.split(",", -1);
                if (atributos.length == 3 && atributos[0].trim().equals(id.trim())) {
                    return new Cliente(atributos[0].trim(), atributos[1].trim(), atributos[2].trim());
                }
            }
            return null;
        } catch (Exception e) {
            throw new Exception("Erro ao carregar cliente do arquivo txt", e);
        }
    }

    private static ArrayList<Cliente> carrgarListaClientesTxt(String url) throws Exception{
        ArrayList<Cliente> listaClientes = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(url))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (linha.trim().isEmpty()) {
                    continue;
                }

                String[] atributos = linha.split(",", -1);
                if (atributos.length == 3) {
                    listaClientes.add(new Cliente(
                        atributos[0].trim(), atributos[1].trim(), atributos[2].trim()));
                }
            }
        } catch (Exception e) {
            throw new Exception("Erro ao gerar lista de clientes a partir do arquivo txt", e);
        }
        return listaClientes;
    }

    
    public void gerarXML(String url) throws Exception{

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            File arquivo = new File(url);
            File diretorio = arquivo.getAbsoluteFile().getParentFile();
            File arquivoClientes = new File(diretorio, "ListaClientes.txt");

            if (arquivo.exists()) {
                arquivo.delete();
            }

            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            Element raiz = document.createElement("listaClientes");
            document.appendChild(raiz);

            ArrayList<Cliente> listaClientes = BancoDados.carrgarListaClientesTxt(arquivoClientes.getAbsolutePath());

            for (Cliente clienteAtual : listaClientes) {
                Element cliente = document.createElement("cliente");
                raiz.appendChild(cliente);

                Field[] listaAtributos = clienteAtual.getClass().getDeclaredFields();
                for (Field atributo : listaAtributos) {
                    atributo.setAccessible(true);
                    Element campo = document.createElement(atributo.getName());
                    Object valor = atributo.get(clienteAtual);
                    campo.appendChild(document.createTextNode(valor != null ? valor.toString() : ""));
                    cliente.appendChild(campo);
                }
            }

            TransformerFactory transformerFactory = TransformerFactory.newDefaultInstance();
            Transformer transformer = transformerFactory.newTransformer();

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://apache.org}indent-amount", "4");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(arquivo);
            transformer.transform(source, result);

        } catch (Exception e) {
            throw new Exception("Erro ao gerar arquivo xml", e);
        }
    }
    
    



    

}
