package org;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class Main {

    @FXML private TextField nomePiscina;
    @FXML private TextField nomeTurma;
    @FXML private TextField diaTurma;
    @FXML private TextField horarioTurma;
    @FXML private ComboBox<String> selecionarPiscina;
    @FXML private TextField nomeAluno;
    @FXML private ComboBox<String> selecionarTurmaAluno;
    @FXML private ComboBox<String> removerAlunoTurma;
    @FXML private ComboBox<String> selecionarAlunoTurma;
    @FXML private ComboBox<String> selecionarMapeamento;
    @FXML private TextArea console;
    private final ObservableList<String> listaPiscinas = FXCollections.observableArrayList();
    private final ObservableList<String> listaTurmas = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        selecionarPiscina.setItems(listaPiscinas);
        selecionarTurmaAluno.setItems(listaTurmas);
        removerAlunoTurma.setItems(listaTurmas);
        selecionarMapeamento.setItems(listaTurmas);

        removerAlunoTurma.setOnAction(e -> {
            String turmaChave = removerAlunoTurma.getValue();
            String[] partes = turmaChave.split(" \\| ");
            if (partes.length >= 4) {
                Turma turma = TurmaGerenciador.getInstancia().getTurma(partes[1], partes[2], partes[3]);
                if (turma != null) {
                    selecionarAlunoTurma.getItems().setAll(turma.getAlunos());
                }
            }
        });

        selecionarMapeamento.setOnMouseClicked(e -> {
            String turmaChave = selecionarMapeamento.getValue();
            if (turmaChave != null) {
                listarAlunos();
            }
        });
    }

    @FXML
    private void cadastrarPiscina() {
        String nome = nomePiscina.getText().trim();

        if (nome.isEmpty()) {
            console.appendText("\nPreencha todos os campos!\n");
            return;
        }

        PiscinaGerenciador gerenciador = PiscinaGerenciador.getInstancia();
        if (gerenciador.getPiscina(nome) != null) {
            console.appendText("\nPiscina já cadastrada!\n");
            return;
        }

        PiscinaGerenciador.getInstancia().cadastrarPiscina(nome);
        console.appendText("\nPiscina cadastrada: " + nome + "\n");

        if (!listaPiscinas.contains(nome)){
            listaPiscinas.add(nome);
        }
        nomePiscina.clear();
    }

    @FXML
    private void listarPiscinas() {
        console.appendText("\n=== Piscinas ===\n");
        for (String nome : listaPiscinas) {
            console.appendText("- " + nome + "\n");
        }
    }

    @FXML
    private void cadastrarTurma() {
        String nome = nomeTurma.getText().trim();
        String dia = diaTurma.getText().trim();
        String horario = horarioTurma.getText().trim();
        String piscina = selecionarPiscina.getValue();

        if (nome.isEmpty() || dia.isEmpty() || horario.isEmpty() || piscina == null) {
            console.appendText("\nPreencha todos os campos!\n");
            return;
        }

        boolean sucesso = TurmaGerenciador.getInstancia().cadastrarTurma(nome, dia, horario, piscina);

        if (sucesso) {
            Turma turmaCriada = TurmaGerenciador.getInstancia().getTurma(dia, horario, piscina);

            if (turmaCriada != null) {
                console.appendText("\nTurma cadastrada:\n");
                console.appendText(turmaCriada + "\n");

                String chave = turmaCriada.gerarChaveCompleta();
                if (!listaTurmas.contains(chave)) {
                    listaTurmas.add(chave);
                }
            }

            nomeTurma.clear();
            diaTurma.clear();
            horarioTurma.clear();
            selecionarPiscina.setValue(null);

        } else {
            console.appendText("\nJá existe uma turma neste dia, horário e piscina!\n");
        }

    }

    @FXML
    private void listarTurmas() {
        console.appendText("\n=== Turmas ===\n");
        for (Turma turma : TurmaGerenciador.getInstancia().getTurmas().values()) {
            console.appendText(turma.toString() + "\n");
        }
    }

    @FXML
    private void adicionarAluno() {
        String aluno = nomeAluno.getText().trim();
        String turmaChave = selecionarTurmaAluno.getValue();

        if (aluno.isEmpty() || turmaChave == null) {
            console.appendText("\nPreencha todos os campos!\n");
            return;
        }

        String[] partes = turmaChave.split(" \\| ");

        if (partes.length >= 4) {
            Turma turma = TurmaGerenciador.getInstancia().getTurma(partes[1], partes[2], partes[3]);

            if (turma != null) {
                String resultado = turma.adicionarAluno(aluno);
                if (resultado.equals("deu certo")) {
                    console.appendText("\nAluno cadastrado!\n");
                    nomeAluno.clear();
                } else {
                    console.appendText("\n" + resultado + "\n");
                }
            }
        }
    }

    @FXML
    private void removerAluno() {
        String turmaChave = removerAlunoTurma.getValue();
        String aluno = selecionarAlunoTurma.getValue();

        if (turmaChave == null || aluno == null){
            console.appendText("\nPreencha todos os campos!\n");
            return;
        }

        String[] partes = turmaChave.split(" \\| ");
        Turma turma = TurmaGerenciador.getInstancia().getTurma(partes[1], partes[2], partes[3]);

        if (turma != null && turma.removerAluno(aluno)) {
            console.appendText("\nAluno removido!\n");
            selecionarAlunoTurma.getItems().remove(aluno);
        }
    }

    @FXML
    private void listarAlunos() {
        String turmaChave = selecionarMapeamento.getValue();

        if (turmaChave == null) {
            return;
        }

        String[] partes = turmaChave.split(" \\| ");

        if (partes.length >= 4) {
            Turma turma = TurmaGerenciador.getInstancia().getTurma(partes[1], partes[2], partes[3]);

            if (turma != null) {
                console.appendText("\n=== Mapeamento da Turma: " + turma.getNome() + " ===\n");
                for (String aluno : turma.getAlunos()) {
                    console.appendText(aluno + "\n");
                }
                console.appendText("Total: " + turma.getAlunos().size() + "/" + Turma.LIMITE + "\n");
            }
        }
    }
}
