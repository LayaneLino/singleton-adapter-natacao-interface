package org;

import java.util.ArrayList;
import java.util.List;

public class Turma {

    private final String nome;
    private final String dia;
    private final String horario;
    private final Piscina piscina;
    private List<String> alunos;
    public static final int LIMITE = 20;

    public Turma(String nome, String dia, String horario, Piscina piscina) {
        this.nome = nome;
        this.dia = dia;
        this.horario = horario;
        this.piscina = piscina;
        this.alunos = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public String getDia() {
        return dia;
    }

    public String getHorario() {
        return horario;
    }

    public Piscina getPiscina() {
        return piscina;
    }

    public List<String> getAlunos() {
        return alunos;
    }

    public String adicionarAluno(String aluno) {

        if (alunos.contains(aluno)) {
            return "Aluno já cadastrado nesta turma!";
        }

        if (alunos.size() >= LIMITE) {
            return "Turma cheia! Máximo de 20 alunos.";
        }

        alunos.add(aluno);
        return "deu certo";
    }

    public boolean removerAluno(String aluno) {
        return this.alunos.remove(aluno);
    }

    @Override
    public String toString() {
        return "Nome: " + nome +
                " | Dia: " + dia +
                " | Horário: " + horario +
                " | Piscina: " + piscina.getNome();
    }

    public String gerarChaveCompleta() {
        return nome + " | " + dia + " | " + horario + " | " + piscina.getNome();
    }

}
