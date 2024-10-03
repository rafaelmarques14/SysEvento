package dsc.model.repositorio;

import dsc.model.entidades.Usuario;
import dsc.model.entidades.UsuarioPerfil;
import dsc.model.entidades.Evento; // Importar a classe Evento
import jakarta.ejb.Singleton;
import jakarta.persistence.*;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Singleton
public class UsuarioRepositorio {

    @PersistenceContext(unitName = "usuarioPU")
    private EntityManager em;

    @PersistenceContext(unitName = "eventoPU")
    private EntityManager eventoEm;

    public Usuario adicionarUsuario(Usuario usuario) {
        this.em.persist(usuario);
        this.em.flush();
        return usuario;
    }

    public Usuario buscarUsuarioPorId(String id) {
        return em.find(Usuario.class, id);
    }

    public Usuario buscarUsuarioPorEmail(String email) {
        return em.createQuery("SELECT u FROM Usuario u WHERE u.email = :email", Usuario.class)
                .setParameter("email", email).getSingleResult();
    }

    public List<Usuario> listarUsuarios() {
        return em.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
    }

    public List<Usuario> findAll() {
        Query q = em.createQuery("from Usuario U", Usuario.class);
        return q.getResultList();
    }

    public void saveUsuarioPerfil(UsuarioPerfil perfil) {
        this.em.persist(perfil);
    }

    public Usuario atualizarUsuario(Usuario usuario) {
        return this.em.merge(usuario);
    }


    public void removerUsuario(String id) {
        Usuario usuario = em.find(Usuario.class, id);
        if (usuario != null) {
            em.remove(usuario);
        }
    }

    public void removerEventosPorUsuario(String usuarioId) {
        TypedQuery<Evento> query = em.createQuery("SELECT e FROM Evento e WHERE e.usuario.id = :usuarioId", Evento.class);
        query.setParameter("usuarioId", usuarioId);
        List<Evento> eventos = query.getResultList();
        for (Evento evento : eventos) {
            em.remove(evento);
        }
    }

}
