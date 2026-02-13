package org;

import java.util.HashMap;
import java.util.Map;

public class PiscinaGerenciador {

    private static PiscinaGerenciador instancia;
    private Map<String, Piscina> piscinas;

    private PiscinaGerenciador() {
        piscinas = new HashMap<>();
    }

    public static PiscinaGerenciador getInstancia() {
        if (instancia == null) {
            instancia = new PiscinaGerenciador();
        }
        return instancia;
    }

    public void cadastrarPiscina(String nome) {
        piscinas.put(nome, new Piscina(nome));
    }

    public Piscina getPiscina(String nome) {
        return piscinas.get(nome);
    }

}
