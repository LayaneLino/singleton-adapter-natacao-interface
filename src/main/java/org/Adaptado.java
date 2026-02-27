package org;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Adaptado {

    public static class SalvadorArquivosLegado {
        public void gravarNoArquivo(String conteudo, String nome) {
            try (FileWriter fw = new FileWriter(nome, true);
                 PrintWriter pw = new PrintWriter(fw)) {

                String dataHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
                pw.println("[" + dataHora + "] " + conteudo);

            } catch (IOException e) {
                System.err.println("Erro ao criar arquivo: " + e.getMessage());
            }
        }
    }

    public static class MostradorTerminalLegado {
        public void exibirNoConsole(String texto) {
            System.out.println(texto);
        }
    }
}
