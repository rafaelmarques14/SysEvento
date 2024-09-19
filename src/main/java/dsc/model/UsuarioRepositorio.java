package dsc.model;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class UsuarioRepositorio {

    @PersistenceContext(unitName = "usuarioPU")
    private EntityManager em;

    public void adicionarUsuario(Usuario usuario) {
        em.persist(usuario);
    }

    public void atualizarUsuario(Usuario usuarioAtualizado) {
        em.merge(usuarioAtualizado);
    }

    public void removerUsuario(Usuario usuario) {
        if (usuario != null) {
            em.remove(em.contains(usuario) ? usuario : em.merge(usuario));
        }
    }

    public Usuario buscarUsuarioPeloEmail(String email) {
        List<Usuario> usuarios = em.createQuery("SELECT u FROM Usuario u WHERE u.email = :email", Usuario.class)
                .setParameter("email", email)
                .getResultList();
        return usuarios.isEmpty() ? null : usuarios.get(0);
    }

    public List<Usuario> listarUsuarios() {
        return em.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
    }
}
