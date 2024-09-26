package dsc.model;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.xml.bind.DatatypeConverter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.regex.Pattern;

@Stateless
public class UsuarioBean {

    @EJB
    private UsuarioRepositorio usuarioRepositorio;

    @EJB
    private EventoBean eventoBean;

    @Inject
    private HttpServletRequest request;

    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    public void adicionarUsuario(Usuario usuario) throws IllegalArgumentException {
        validarUsuario(usuario);
        if (usuarioRepositorio.buscarUsuarioPeloEmail(usuario.getEmail()) != null) {
            throw new IllegalArgumentException("Usuário com este email já existe.");
        }
        usuario.setSenha(encodeSHA256(usuario.getSenha()));
        usuarioRepositorio.adicionarUsuario(usuario);
    }

    public void atualizarUsuario(Usuario usuarioAtualizado) throws IllegalArgumentException {
        validarUsuario(usuarioAtualizado);
        Usuario usuarioExistente = usuarioRepositorio.buscarUsuarioPeloEmail(usuarioAtualizado.getEmail());
        if (usuarioExistente == null) {
            throw new IllegalArgumentException("Usuário não encontrado.");
        }
        usuarioAtualizado.setSenha(encodeSHA256(usuarioAtualizado.getSenha()));
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

        List<Evento> eventos = eventoBean.listarEventos(usuarioExistente);
        for (Evento evento : eventos) {
            eventoBean.removerEvento(evento);
        }

        usuarioRepositorio.removerUsuario(usuarioExistente);

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

    public List<Usuario> buscarUsuariosPorPerfil(String perfil) {
        return usuarioRepositorio.buscarUsuariosPorPerfil(perfil);
    }

    public boolean isValidPassword(String email, String senha) {
        Usuario usuarioLogado = buscarUsuarioPeloEmail(email);
        if (usuarioLogado != null) {
            return encodeSHA256(senha).equals(usuarioLogado.getSenha());
        }
        return false;
    }

    private String encodeSHA256(String senha) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(senha.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao codificar a senha", e);
        }
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
