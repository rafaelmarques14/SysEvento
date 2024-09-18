package dsc.model;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class EventoRepositorio {

    @PersistenceContext(unitName = "eventoPU")
    private EntityManager entityManager;

    public void adicionarEvento(Evento evento) {
        entityManager.persist(evento);
    }

    public void removerEvento(Evento evento) {
        Evento eventoExistente = encontrarEvento(evento.getNome(), evento.getUsuario());
        if (eventoExistente != null) {
            entityManager.merge(eventoExistente);
        }
    }

    public Evento encontrarEvento(String nome, Usuario usuario) {
        return entityManager.createQuery("SELECT e FROM Evento e WHERE e.nome = :nome AND e.usuario = :usuario", Evento.class)
                .setParameter("nome", nome)
                .setParameter("usuario", usuario)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    public List<Evento> listarEventos(Usuario usuario) {
        return entityManager.createQuery("SELECT e FROM Evento e WHERE e.usuario = :usuario", Evento.class)
                .setParameter("usuario", usuario)
                .getResultList();
    }
}
