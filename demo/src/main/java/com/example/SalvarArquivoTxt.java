package com.example;

public interface SalvarArquivoTxt {

    public void salvarClienteTxt(Cliente novoCliente, String url)throws Exception;

    public Cliente carregarClienteTxt(String id, String url)throws Exception ;


}
