package dsc.model;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class EventoRepositorio {

    @PersistenceContext(unitName = "eventoPU")
    private EntityManager entityManager;

    // Adicionar Evento
    public void adicionarEvento(Evento evento) {
        try {
            entityManager.persist(evento);
        } catch (Exception e) {
            e.printStackTrace();  // Registrar exceções para análise
        }
    }

    // Remover Evento
    public void removerEvento(Evento evento) {
        try {
            Evento eventoExistente = entityManager.find(Evento.class, evento.getId());
            if (eventoExistente != null) {
                entityManager.remove(eventoExistente);
            }
        } catch (Exception e) {
            e.printStackTrace();  // Registrar exceções para análise
        }
    }

    // Atualizar Evento
    public void atualizarEvento(Evento eventoAtualizado) {
        try {
            Evento eventoExistente = entityManager.find(Evento.class, eventoAtualizado.getId());
            if (eventoExistente != null) {
                eventoExistente.setNome(eventoAtualizado.getNome());
                eventoExistente.setData(eventoAtualizado.getData());
                eventoExistente.setUsuario(eventoAtualizado.getUsuario());
                entityManager.merge(eventoExistente);  // Sincroniza com o banco
            } else {
                throw new IllegalArgumentException("Evento não encontrado para atualização.");
            }
        } catch (Exception e) {
            e.printStackTrace();  // Registrar exceções para análise
        }
    }

    // Encontrar Evento
    public Evento encontrarEvento(String nome, Usuario usuario) {
        return entityManager.createQuery("SELECT e FROM Evento e WHERE e.nome = :nome AND e.usuario = :usuario", Evento.class)
                .setParameter("nome", nome)
                .setParameter("usuario", usuario)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    // Listar Eventos
    public List<Evento> listarEventos(Usuario usuario) {
        return entityManager.createQuery("SELECT e FROM Evento e WHERE e.usuario = :usuario", Evento.class)
                .setParameter("usuario", usuario)
                .getResultList();
    }
}
