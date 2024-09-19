package dsc.model;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class EventoBean {

    @PersistenceContext(unitName = "eventoPU")
    private EntityManager em;

    public void adicionarEvento(Evento evento) {
        em.persist(evento);
    }

    public void atualizarEvento(Evento eventoAtualizado) {
        em.merge(eventoAtualizado);
    }

    public void removerEvento(Evento evento) {
        Evento eventoRemover = em.find(Evento.class, evento.getId());
        if (eventoRemover != null) {
            em.remove(eventoRemover);
        }
    }

    public List<Evento> listarEventos(Usuario usuario) {
        return em.createQuery("SELECT e FROM Evento e WHERE e.usuario = :usuario", Evento.class)
                .setParameter("usuario", usuario)
                .getResultList();
    }
}
