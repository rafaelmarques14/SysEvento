package dsc.model;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import java.util.List;

@Stateless
public class EventoBean {

    @EJB
    private EventoRepositorio eventoRepositorio;

    public void adicionarEvento(Evento evento) throws IllegalArgumentException {
        validarEvento(evento);
        eventoRepositorio.adicionarEvento(evento);
    }

    public void removerEvento(Evento evento) throws IllegalArgumentException {
        validarEvento(evento);
        eventoRepositorio.removerEvento(evento);
    }

    public Evento encontrarEvento(String nome, Usuario usuario) {
        return eventoRepositorio.encontrarEvento(nome, usuario);
    }

    public List<Evento> listarEventos(Usuario usuario) {
        return eventoRepositorio.listarEventos(usuario);
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
