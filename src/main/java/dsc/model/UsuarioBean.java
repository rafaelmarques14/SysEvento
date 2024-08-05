package dsc.model;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import java.util.List;
import java.util.regex.Pattern;

@Stateless
public class UsuarioBean {

    @EJB
    private UsuarioRepositorio usuarioRepositorio;

    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    public void adicionarUsuario(Usuario usuario) throws IllegalArgumentException {
        validarUsuarioParaCadastro(usuario);
        if (usuarioRepositorio.buscarUsuarioPeloEmail(usuario.getEmail()) != null) {
            throw new IllegalArgumentException("Usuário com este email já existe.");
        }
        usuarioRepositorio.adicionarUsuario(usuario);
    }

    public void atualizarUsuario(Usuario usuarioAtualizado) throws IllegalArgumentException {
        validarUsuarioParaAtualizacao(usuarioAtualizado);
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
        usuarioRepositorio.removerUsuario(email);
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

    private void validarUsuarioParaCadastro(Usuario usuario) throws IllegalArgumentException {
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

    private void validarUsuarioParaAtualizacao(Usuario usuario) throws IllegalArgumentException {
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
