package org;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TxtAdapter implements ISalvarMensagens {
    private final String nomeArquivo = "logs_piscina.txt";

    @Override
    public void salvar(String mensagem) {
        try (FileWriter fw = new FileWriter(nomeArquivo, true);
             PrintWriter pw = new PrintWriter(fw)) {

            String dataHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            pw.println("[" + dataHora + "] " + mensagem);

        } catch (IOException e) {
            System.err.println("Erro ao salvar logs: " + e.getMessage());
        }
    }

}
