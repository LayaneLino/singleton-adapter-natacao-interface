package org.adapter;

import javafx.scene.control.TextArea;

public class ConsoleAdapter implements ISalvarMensagens {
    private final TextArea console;
    private final Adaptado.MostradorTerminalLegado terminalLegado = new Adaptado.MostradorTerminalLegado();

    public ConsoleAdapter(TextArea console) {
        this.console = console;
    }

    @Override
    public void salvar(String mensagem) {
        this.console.appendText(mensagem + "\n");
        this.terminalLegado.exibirNoConsole(mensagem);
    }
}