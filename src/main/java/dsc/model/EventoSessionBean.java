package dsc.model;

import jakarta.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class EventoSessionBean {

    private List<Evento> eventos = new ArrayList<>();

    public void adicionarEvento(Evento evento) {
        eventos.add(evento);
    }

    public void removerEvento(Evento evento) {
        eventos.removeIf(e -> e.getNome().equals(evento.getNome()));
    }

    // Método para encontrar um evento pela chave
    public Evento encontrarEvento(String nome) {
        return eventos.stream()
                .filter(e -> e.getNome().equals(nome))
                .findFirst()
                .orElse(null);
    }

    public List<Evento> listarEventos() {
        return eventos;
    }
}
