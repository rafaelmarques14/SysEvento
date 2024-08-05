package dsc.model;

import jakarta.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class EventoBean {

    private List<Evento> eventos = new ArrayList<>();

    public void adicionarEvento(Evento evento) throws IllegalArgumentException {
        validarEvento(evento);
        eventos.add(evento);
    }

    public void removerEvento(Evento evento) throws IllegalArgumentException {
        validarEvento(evento);
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

    private void validarEvento(Evento evento) throws IllegalArgumentException {
        if (evento == null) {
            throw new IllegalArgumentException("Evento não pode ser nulo.");
        }
        if (evento.getNome() == null || evento.getNome().isEmpty()) {
            throw new IllegalArgumentException("Nome do evento é obrigatório.");
        }
        if (evento.getData() == null) {
            throw new IllegalArgumentException("Data do evento é obrigatória.");
        }
        if (evento.getUsuario() == null) {
            throw new IllegalArgumentException("Usuário do evento é obrigatório.");
        }
    }
}
