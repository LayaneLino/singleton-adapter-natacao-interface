package org;

import javafx.scene.control.TextArea;

public class ConsoleAdapter implements ISalvarMensagens {
    private TextArea console;
    
    ConsoleAdapter(TextArea console) {
        this.console = console;
    }
    
    @Override
    public void salvar(String mensagem) {
        console.appendText(mensagem + "\n");
    }   
}
