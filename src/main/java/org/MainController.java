package org;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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
        if (gerenciadorLogs.adaptador instanceof TxtAdapter) {
            gerenciadorLogs = new GerenciadorLogs(new ConsoleAdapter(console));
            console.appendText("Output alterado para console.\n");
        } else {
            gerenciadorLogs = new GerenciadorLogs(new TxtAdapter());
            console.appendText("Output alterado para arquivo de texto.\n");
        }
    }

    @FXML
    private void cadastrarPiscina() {
        String nome = nomePiscina.getText().trim();

        if (nome.isEmpty()) {
            gerenciadorLogs.exibeConsoleSalvaTxt("Preencha todos os campos!");
            return;
        }

        PiscinaGerenciador gerenciador = PiscinaGerenciador.getInstancia();
        if (gerenciador.getPiscina(nome) != null) {
            gerenciadorLogs.exibeConsoleSalvaTxt("Piscina " + nome + " já existe!");
            return;
        }

        PiscinaGerenciador.getInstancia().cadastrarPiscina(nome);
        gerenciadorLogs.exibeConsoleSalvaTxt("Piscina cadastrada: " + nome);

        if (!listaPiscinas.contains(nome)){
            listaPiscinas.add(nome);
        }
        nomePiscina.clear();
    }

    @FXML
    private void listarPiscinas() {
        gerenciadorLogs.exibeConsoleSalvaTxt("=== Piscinas ===");
        for (String nome : listaPiscinas) {
            gerenciadorLogs.exibeConsoleSalvaTxt("- " + nome);
        }
    }

    @FXML
    private void cadastrarTurma() {
        String nome = nomeTurma.getText().trim();
        String dia = diaTurma.getText().trim();
        String horario = horarioTurma.getText().trim();
        String piscina = selecionarPiscina.getValue();

        if (nome.isEmpty() || dia.isEmpty() || horario.isEmpty() || piscina == null) {
            gerenciadorLogs.exibeConsoleSalvaTxt("Preencha todos os campos!");
            return;
        }

        boolean sucesso = TurmaGerenciador.getInstancia().cadastrarTurma(nome, dia, horario, piscina);

        if (sucesso) {
            Turma turmaCriada = TurmaGerenciador.getInstancia().getTurma(dia, horario, piscina);

            if (turmaCriada != null) {
                gerenciadorLogs.exibeConsoleSalvaTxt("Turma cadastrada:");
                gerenciadorLogs.exibeConsoleSalvaTxt("" + turmaCriada);

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
            gerenciadorLogs.exibeConsoleSalvaTxt("Já existe uma turma em " + piscina + " no dia " + dia + " às " + horario + "!");
        }

    }

    @FXML
    private void listarTurmas() {
        gerenciadorLogs.exibeConsoleSalvaTxt("=== Turmas ===");
        for (Turma turma : TurmaGerenciador.getInstancia().getTurmas().values()) {
            gerenciadorLogs.exibeConsoleSalvaTxt(turma.toString());
        }
    }

    @FXML
    private void adicionarAluno() {
        String aluno = nomeAluno.getText().trim();
        String turmaChave = selecionarTurmaAluno.getValue();

        if (aluno.isEmpty() || turmaChave == null) {
            gerenciadorLogs.exibeConsoleSalvaTxt("Preencha todos os campos!");
            return;
        }

        String[] partes = turmaChave.split(" \\| ");

        if (partes.length >= 4) {
            Turma turma = TurmaGerenciador.getInstancia().getTurma(partes[1], partes[2], partes[3]);

            if (turma != null) {
                String resultado = turma.adicionarAluno(aluno);
                if (resultado.equals("deu certo")) {
                    gerenciadorLogs.exibeConsoleSalvaTxt("Aluno " + aluno + " cadastrado da turma " + turma.getNome() + ".");
                    nomeAluno.clear();
                } else {
                    gerenciadorLogs.exibeConsoleSalvaTxt(resultado);
                }
            }
        }
    }

    @FXML
    private void removerAluno() {
        String turmaChave = removerAlunoTurma.getValue();
        String aluno = selecionarAlunoTurma.getValue();

        if (turmaChave == null || aluno == null){
            gerenciadorLogs.exibeConsoleSalvaTxt("Preencha todos os campos!");
            return;
        }

        String[] partes = turmaChave.split(" \\| ");
        Turma turma = TurmaGerenciador.getInstancia().getTurma(partes[1], partes[2], partes[3]);

        if (turma != null && turma.removerAluno(aluno)) {
            gerenciadorLogs.exibeConsoleSalvaTxt("Aluno " + aluno + " removido da turma " + turma.getNome() + ".");
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
                gerenciadorLogs.exibeConsoleSalvaTxt("=== Mapeamento da Turma: " + turma.getNome() + " ===");
                for (String aluno : turma.getAlunos()) {
                    gerenciadorLogs.exibeConsoleSalvaTxt(aluno);
                }
                gerenciadorLogs.exibeConsoleSalvaTxt("Total: " + turma.getAlunos().size() + "/" + Turma.limite);
            }
        }
    }
}
