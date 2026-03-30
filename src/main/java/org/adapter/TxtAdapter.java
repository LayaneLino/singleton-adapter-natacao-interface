package org.adapter;

public class TxtAdapter implements ISalvarMensagens {
    private final Adaptado.SalvadorArquivosLegado arquivoLegado = new Adaptado.SalvadorArquivosLegado();

    @Override
    public void salvar(String mensagem) {
        String nomeArquivo = "logs_piscina.txt";
        arquivoLegado.gravarNoArquivo(mensagem, nomeArquivo);
    }

}
