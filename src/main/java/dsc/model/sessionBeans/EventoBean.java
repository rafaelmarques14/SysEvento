package dsc.model.sessionBeans;

import dsc.model.entidades.Evento;
import dsc.model.entidades.Usuario;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class EventoBean {

    @PersistenceContext(unitName = "eventoPU")
    private EntityManager em;

    public void adicionarEvento(Evento evento) {
        validarEvento(evento);
        em.persist(evento);
    }

    public void atualizarEvento(Evento eventoAtualizado) {
        validarEvento(eventoAtualizado);
        em.merge(eventoAtualizado);
    }

    public void removerEvento(Evento evento) {
        if (evento != null) {
            Evento eventoRemover = em.find(Evento.class, evento.getId());
            if (eventoRemover != null) {
                em.remove(eventoRemover);
            }
        }
    }

    public List<Evento> listarEventos(Usuario usuario) {
        return em.createQuery("SELECT e FROM Evento e WHERE e.usuario = :usuario", Evento.class)
                .setParameter("usuario", usuario)
                .getResultList();
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
