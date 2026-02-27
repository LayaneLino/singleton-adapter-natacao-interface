package org;

public class GerenciadorLogs {
    private final ISalvarMensagens adaptador;

    GerenciadorLogs(ISalvarMensagens adaptador) {
        this.adaptador = adaptador;
    }

    public void registrar(String mensagem) {
        adaptador.salvar(mensagem);
    }

    public ISalvarMensagens getAdaptador() {
        return adaptador;
    }

}
