package dsc.model;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.util.List;

@Stateless
public class EventoSessionBean {

    @Inject
    private EventoRepositorio eventoRepositorio;

    public void adicionarEvento(Evento evento) {
        eventoRepositorio.adicionarEvento(evento);
    }

    public void removerEvento(Evento evento) {
        eventoRepositorio.removerEvento(evento);
    }

    public Evento encontrarEvento(String nome, Usuario usuario) {
        return eventoRepositorio.encontrarEvento(nome, usuario);
    }

    public List<Evento> listarEventos(Usuario usuario) {
        return eventoRepositorio.listarEventos(usuario);
    }
}
