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

    private String montarChaveInterna(String dia, String horario, String nomePiscina) {
        return dia + " | " + horario + " | " + nomePiscina;
    }

    public boolean cadastrarTurma(String nomeTurma, String dia, String horario, String nomePiscina) {
        Piscina piscina = PiscinaGerenciador.getInstancia().getPiscina(nomePiscina);

        if (piscina == null) return false;

        String chave = montarChaveInterna(dia, horario, nomePiscina);
        if (turmas.containsKey(chave)) return false;

        turmas.put(chave, new Turma(nomeTurma, dia, horario, piscina));
        return true;
    }

    public Turma getTurma(String dia, String horario, String nomePiscina) {
        return turmas.get(montarChaveInterna(dia, horario, nomePiscina));
    }

    public Map<String, Turma> getTurmas() {
        return turmas;
    }

}
