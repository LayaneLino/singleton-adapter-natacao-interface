package org;

public class TxtAdapter implements ISalvarMensagens {
    private final Adaptado.SalvadorArquivosLegado arquivoLegado = new Adaptado.SalvadorArquivosLegado();
    private final String nomeArquivo = "logs_piscina.txt";

    @Override
    public void salvar(String mensagem) {
        arquivoLegado.gravarNoArquivo(mensagem, nomeArquivo);
    }

}
