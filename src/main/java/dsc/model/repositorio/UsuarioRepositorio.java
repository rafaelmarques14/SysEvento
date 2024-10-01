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

    public Usuario atualizarUsuario(Usuario usuarioAtualizado) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        // Busca o usuário existente no banco de dados
        Usuario usuarioExistente = buscarUsuarioPorId(usuarioAtualizado.getId());

        // Verifica se o usuário existe e se é válido
        if (usuarioExistente != null && validarUsuario(usuarioAtualizado)) {
            // Atualiza a senha se foi alterada
            if (!usuarioAtualizado.getSenha().equals(usuarioExistente.getSenha())) {
                usuarioAtualizado.setSenha(encodeSHA256(usuarioAtualizado.getSenha()));
            }

            // Atualiza o usuário no banco de dados
            Usuario usuarioAtualizadoPersistido = this.em.merge(usuarioAtualizado);

            // Verifica se o email foi alterado
            if (!usuarioAtualizado.getEmail().equals(usuarioExistente.getEmail())) {
                // Busca o UsuarioPerfil associado ao email antigo
                UsuarioPerfil perfilExistente = buscarPerfilPorUsuarioEmail(usuarioExistente.getEmail());
                if (perfilExistente != null) {
                    // Atualiza o email no UsuarioPerfil
                    perfilExistente.setEmail(usuarioAtualizado.getEmail());
                    em.merge(perfilExistente); // Persistir as alterações do perfil
                }
            }

            return usuarioAtualizadoPersistido;
        }
        return null;
    }

    public void removerUsuario(String id) {
        Usuario usuario = em.find(Usuario.class, id);
        if (usuario != null) {
            // Remover os eventos associados ao usuário
            List<Evento> eventos = buscarEventosPorUsuario(usuario);
            for (Evento evento : eventos) {
                eventoEm.remove(evento);
            }

            // Remover o UsuarioPerfil associado usando o email do usuario
            UsuarioPerfil perfil = buscarPerfilPorUsuarioEmail(usuario.getEmail());
            if (perfil != null) {
                em.remove(perfil);
            }
            em.remove(usuario);
        }
    }

    public UsuarioPerfil buscarPerfilPorUsuarioEmail(String email) {
        try {
            return em.createQuery("SELECT up FROM UsuarioPerfil up WHERE up.email = :email", UsuarioPerfil.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null; // Retornar null se não encontrar
        }
    }

    public List<Evento> buscarEventosPorUsuario(Usuario usuario) {
        // Método para buscar eventos associados a um usuário
        return eventoEm.createQuery("SELECT e FROM Evento e WHERE e.usuario = :usuario", Evento.class)
                .setParameter("usuario", usuario)
                .getResultList();
    }

    private boolean validarUsuario(Usuario usuario) {
        // Implementar a lógica de validação aqui
        return true; // Placeholder
    }

    private String encodeSHA256(String senha) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        // Implementar a lógica de codificação aqui
        return senha; // Placeholder
    }
}
