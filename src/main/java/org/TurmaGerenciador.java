package org;

import java.util.HashMap;
import java.util.Map;

public class TurmaGerenciador {

    private static TurmaGerenciador instancia;
    private final Map<String, Turma> turmas;

    private TurmaGerenciador() {
        turmas = new HashMap<>();
    }

    public static TurmaGerenciador getInstancia() {
        if (instancia == null) {
            instancia = new TurmaGerenciador();
        }
        return instancia;
    }

    public boolean cadastrarTurma(String nomeTurma, String dia, String horario, String nomePiscina) {

        Piscina piscina = PiscinaGerenciador.getInstancia().getPiscina(nomePiscina);

        String chave = dia + " | " + horario + " | " + nomePiscina;

        if (turmas.containsKey(chave)) {
            return false;
        }

        Turma turma = new Turma(nomeTurma, dia, horario, piscina);
        turmas.put(chave, turma);

        return true;
    }

    public Turma getTurma(String dia, String horario, String nomePiscina) {
        String chave = dia + " | " + horario + " | " + nomePiscina;
        return turmas.get(chave);
    }

    public Map<String, Turma> getTurmas() {
        return turmas;
    }

}
