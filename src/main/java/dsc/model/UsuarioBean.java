package dsc.model;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.regex.Pattern;

@Stateless
public class UsuarioBean {

    @EJB
    private UsuarioRepositorio usuarioRepositorio;

    @EJB
    private EventoBean eventoBean; // Adicionado para acessar eventos relacionados

    @Inject
    private HttpServletRequest request; // Para acessar a sessão HTTP

    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    public void adicionarUsuario(Usuario usuario) throws IllegalArgumentException {
        validarUsuario(usuario);
        if (usuarioRepositorio.buscarUsuarioPeloEmail(usuario.getEmail()) != null) {
            throw new IllegalArgumentException("Usuário com este email já existe.");
        }
        usuarioRepositorio.adicionarUsuario(usuario);
    }

    public void atualizarUsuario(Usuario usuarioAtualizado) throws IllegalArgumentException {
        validarUsuario(usuarioAtualizado);
        Usuario usuarioExistente = usuarioRepositorio.buscarUsuarioPeloEmail(usuarioAtualizado.getEmail());
        if (usuarioExistente == null) {
            throw new IllegalArgumentException("Usuário não encontrado.");
        }
        usuarioRepositorio.atualizarUsuario(usuarioAtualizado);
    }

    public void removerUsuario(String email) throws IllegalArgumentException {
        if (email == null || email.isEmpty() || !pattern.matcher(email).matches()) {
            throw new IllegalArgumentException("Email inválido.");
        }
        Usuario usuarioExistente = usuarioRepositorio.buscarUsuarioPeloEmail(email);
        if (usuarioExistente == null) {
            throw new IllegalArgumentException("Usuário não encontrado.");
        }

        // Remover todos os eventos associados
        List<Evento> eventos = eventoBean.listarEventos(usuarioExistente);
        for (Evento evento : eventos) {
            eventoBean.removerEvento(evento);
        }

        // Remover o usuário
        usuarioRepositorio.removerUsuario(usuarioExistente);

        // Realizar o logout
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    public Usuario buscarUsuarioPeloEmail(String email) throws IllegalArgumentException {
        if (email == null || email.isEmpty() || !pattern.matcher(email).matches()) {
            throw new IllegalArgumentException("Email inválido.");
        }
        return usuarioRepositorio.buscarUsuarioPeloEmail(email);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepositorio.listarUsuarios();
    }

    private void validarUsuario(Usuario usuario) throws IllegalArgumentException {
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não pode ser nulo.");
        }
        if (usuario.getNome() == null || usuario.getNome().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório.");
        }
        if (usuario.getEmail() == null || usuario.getEmail().isEmpty() || !pattern.matcher(usuario.getEmail()).matches()) {
            throw new IllegalArgumentException("Email inválido.");
        }
        if (usuario.getSenha() == null || usuario.getSenha().isEmpty()) {
            throw new IllegalArgumentException("Senha é obrigatória.");
        }
    }
}
