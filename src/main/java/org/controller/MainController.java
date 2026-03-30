package org.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.adapter.ConsoleAdapter;
import org.adapter.GerenciadorLogs;
import org.adapter.TxtAdapter;
import org.entity.Turma;
import org.singleton.PiscinaGerenciador;
import org.singleton.TurmaGerenciador;

public class MainController {

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

    private GerenciadorLogs gerenciadorLogs;

    @FXML
    public void initialize() {
        gerenciadorLogs =  new GerenciadorLogs(new ConsoleAdapter(console));
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
                mostrarMapeamento();
            }
        });
    }

    @FXML
    private void alterarOutput() {
        if (gerenciadorLogs.getAdaptador() instanceof TxtAdapter) {
            gerenciadorLogs = new GerenciadorLogs(new ConsoleAdapter(console));
            gerenciadorLogs.registrar("Output alterado para console.");
        } else {
            gerenciadorLogs = new GerenciadorLogs(new TxtAdapter());
            console.appendText("Output alterado para arquivo de texto.\n");
            gerenciadorLogs.registrar("Output alterado para arquivo de texto.");
        }
    }

    @FXML
    private void cadastrarPiscina() {
        String nome = nomePiscina.getText().trim();

        if (nome.isEmpty()) {
            gerenciadorLogs.registrar("Preencha todos os campos!");
            return;
        }

        PiscinaGerenciador gerenciador = PiscinaGerenciador.getInstancia();
        if (gerenciador.getPiscina(nome) != null) {
            gerenciadorLogs.registrar("Piscina " + nome + " já existe!");
            return;
        }

        PiscinaGerenciador.getInstancia().cadastrarPiscina(nome);
        gerenciadorLogs.registrar("Piscina cadastrada: " + nome);

        if (!listaPiscinas.contains(nome)){
            listaPiscinas.add(nome);
        }
        nomePiscina.clear();
    }

    @FXML
    private void listarPiscinas() {
        gerenciadorLogs.registrar("=== Piscinas ===");
        for (String nome : listaPiscinas) {
            gerenciadorLogs.registrar("- " + nome);
        }
    }

    @FXML
    private void cadastrarTurma() {
        String nome = nomeTurma.getText().trim();
        String dia = diaTurma.getText().trim();
        String horario = horarioTurma.getText().trim();
        String piscina = selecionarPiscina.getValue();

        if (nome.isEmpty() || dia.isEmpty() || horario.isEmpty() || piscina == null) {
            gerenciadorLogs.registrar("Preencha todos os campos!");
            return;
        }

        boolean sucesso = TurmaGerenciador.getInstancia().cadastrarTurma(nome, dia, horario, piscina);

        if (sucesso) {
            Turma turmaCriada = TurmaGerenciador.getInstancia().getTurma(dia, horario, piscina);

            if (turmaCriada != null) {
                gerenciadorLogs.registrar("Turma cadastrada:");
                gerenciadorLogs.registrar("" + turmaCriada);

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
            gerenciadorLogs.registrar("Já existe uma turma em " + piscina + " no dia " + dia + " às " + horario + "!");
        }

    }

    @FXML
    private void listarTurmas() {
        gerenciadorLogs.registrar("=== Turmas ===");
        for (Turma turma : TurmaGerenciador.getInstancia().getTurmas().values()) {
            gerenciadorLogs.registrar(turma.toString());
        }
    }

    @FXML
    private void adicionarAluno() {
        String aluno = nomeAluno.getText().trim();
        String turmaChave = selecionarTurmaAluno.getValue();

        if (aluno.isEmpty() || turmaChave == null) {
            gerenciadorLogs.registrar("Preencha todos os campos!");
            return;
        }

        String[] partes = turmaChave.split(" \\| ");

        if (partes.length >= 4) {
            Turma turma = TurmaGerenciador.getInstancia().getTurma(partes[1], partes[2], partes[3]);

            if (turma != null) {
                String resultado = turma.adicionarAluno(aluno);
                if (resultado.equals("deu certo")) {
                    gerenciadorLogs.registrar("Aluno " + aluno + " cadastrado da turma " + turma.getNome() + ".");
                    nomeAluno.clear();
                } else {
                    gerenciadorLogs.registrar(resultado);
                }
            }
        }
    }

    @FXML
    private void removerAluno() {
        String turmaChave = removerAlunoTurma.getValue();
        String aluno = selecionarAlunoTurma.getValue();

        if (turmaChave == null || aluno == null){
            gerenciadorLogs.registrar("Preencha todos os campos!");
            return;
        }

        String[] partes = turmaChave.split(" \\| ");
        Turma turma = TurmaGerenciador.getInstancia().getTurma(partes[1], partes[2], partes[3]);

        if (turma != null && turma.removerAluno(aluno)) {
            gerenciadorLogs.registrar("Aluno " + aluno + " removido da turma " + turma.getNome() + ".");
            selecionarAlunoTurma.getItems().remove(aluno);
        }
    }

    @FXML
    private void mostrarMapeamento() {
        String turmaChave = selecionarMapeamento.getValue();

        if (turmaChave == null) {
            return;
        }

        String[] partes = turmaChave.split(" \\| ");

        if (partes.length >= 4) {
            Turma turma = TurmaGerenciador.getInstancia().getTurma(partes[1], partes[2], partes[3]);

            if (turma != null) {
                gerenciadorLogs.registrar("=== Mapeamento da Turma: " + turma.getNome() + " ===");
                for (String aluno : turma.getAlunos()) {
                    gerenciadorLogs.registrar(aluno);
                }
                gerenciadorLogs.registrar("Total: " + turma.getAlunos().size() + "/" + Turma.limite);
            }
        }
    }
}
