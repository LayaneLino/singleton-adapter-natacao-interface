package org;

public class GerenciadorLogs {
    public ISalvarMensagens adaptador;

    GerenciadorLogs(ISalvarMensagens salvador) {
        this.adaptador = salvador;
    }

    public void exibeConsoleSalvaTxt(String mensagem) {
        adaptador.salvar(mensagem);
    }
}
