package dsc.model;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.NoResultException;
import java.util.List;

@Stateless
public class UsuarioRepositorio {

    @PersistenceContext(unitName = "usuarioPU")
    private EntityManager em;

    @PersistenceContext(unitName = "eventoPU") // Adiciona a unidade de persistência dos eventos
    private EntityManager eventoEm;

    public void adicionarUsuario(Usuario usuario) {
        if (usuario != null) {
            em.persist(usuario);
        } else {
            throw new IllegalArgumentException("Usuário não pode ser nulo.");
        }
    }

    public void atualizarUsuario(Usuario usuarioAtualizado) {
        if (usuarioAtualizado != null) {
            em.merge(usuarioAtualizado);
        } else {
            throw new IllegalArgumentException("Usuário não pode ser nulo.");
        }
    }

    public void removerUsuario(Usuario usuario) {
        if (usuario != null) {
            // Remover eventos associados
            List<Evento> eventos = eventoEm.createQuery("SELECT e FROM Evento e WHERE e.usuario = :usuario", Evento.class)
                    .setParameter("usuario", usuario)
                    .getResultList();
            for (Evento evento : eventos) {
                eventoEm.remove(evento);
            }

            // Remover o usuário
            if (em.contains(usuario)) {
                em.remove(usuario);
            } else {
                Usuario usuarioRemover = em.find(Usuario.class, usuario.getId());
                if (usuarioRemover != null) {
                    em.remove(usuarioRemover);
                } else {
                    throw new IllegalArgumentException("Usuário não encontrado para remoção.");
                }
            }
        } else {
            throw new IllegalArgumentException("Usuário não pode ser nulo.");
        }
    }

    public Usuario buscarUsuarioPeloEmail(String email) {
        try {
            return em.createQuery("SELECT u FROM Usuario u WHERE u.email = :email", Usuario.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Usuario> listarUsuarios() {
        return em.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
    }
}
