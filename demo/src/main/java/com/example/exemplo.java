package com.example;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class exemplo {
    public void gerarXML(ArrayList<Cliente> listaClientes, String caminhoArquivo) throws Exception {
        try {
            // 1. Verifica se o arquivo já existe e deleta para substituir
            File arquivo = new File(caminhoArquivo);
            if (arquivo.exists()) {
                arquivo.delete();
            }

            // 2. Inicializa o construtor do documento DOM
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            // 3. Cria a tag raiz: <clientes>
            Element rootElement = doc.createElement("clientes");
            doc.appendChild(rootElement);

            // 4. Percorre a lista de clientes
            for (Cliente cliente : listaClientes) {
                // Cria a tag de cada item: <cliente>
                Element clienteElement = doc.createElement("cliente");
                rootElement.appendChild(clienteElement);

                // Pega todos os atributos da classe Cliente dinamicamente
                Field[] fields = cliente.getClass().getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    
                    // Nome da tag será o nome do atributo (ex: <nome>, <cpf>)
                    Element campoElement = doc.createElement(field.getName());
                    
                    // Pega o valor do atributo (evita erro se o valor for null)
                    Object valor = field.get(cliente);
                    campoElement.appendChild(doc.createTextNode(valor != null ? valor.toString() : ""));
                    
                    clienteElement.appendChild(campoElement);
                }
            }

            // 5. Configura a gravação e a formatação (identação) do XML
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            
            // Formata o XML com quebras de linha e espaços (para não ficar tudo em uma linha só)
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://apache.org}indent-amount", "4");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            // 6. Salva no disco
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(arquivo);
            transformer.transform(source, result);

        } catch (Exception e) {
            throw new Exception("Erro ao gerar o arquivo XML de clientes", e);
        }
    }
}
