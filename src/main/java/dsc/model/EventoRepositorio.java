package dsc.model;

import jakarta.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class EventoRepositorio {

    private List<Evento> eventos = new ArrayList<>();

    public void adicionarEvento(Evento evento) {
        eventos.add(evento);
    }

    public void removerEvento(Evento evento) {
        eventos.removeIf(e -> e.getNome().equals(evento.getNome()) && e.getUsuario().equals(evento.getUsuario()));
    }

    public Evento encontrarEvento(String nome, Usuario usuario) {
        return eventos.stream()
                .filter(e -> e.getNome().equals(nome) && e.getUsuario().equals(usuario))
                .findFirst()
                .orElse(null);
    }

    public List<Evento> listarEventos(Usuario usuario) {
        return eventos.stream()
                .filter(e -> e.getUsuario().equals(usuario))
                .collect(Collectors.toList());
    }
}
