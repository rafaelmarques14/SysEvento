package dsc.model.sessionBeans;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import dsc.model.entidades.Usuario;
import dsc.model.entidades.UsuarioPerfil;
import dsc.model.repositorio.UsuarioRepositorio;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.xml.bind.DatatypeConverter;

@Stateless
public class UsuarioBean {

    @EJB
    private UsuarioRepositorio urep;

    public Usuario criarUsuario(Usuario usuario, UsuarioPerfil perfil)
            throws UnsupportedEncodingException, NoSuchAlgorithmException {

        if (!this.validarUsuario(usuario)) {
            return null;
        }
        usuario.setSenha(encodeSHA256(usuario.getSenha()));
        this.urep.saveUsuarioPerfil(perfil);
        return this.urep.adicionarUsuario(usuario);
    }

    public Usuario buscarUsuarioPorId(String id) {
        return urep.buscarUsuarioPorId(id);
    }

    public Usuario buscarUsuarioPorEmail(String email) {
        return urep.buscarUsuarioPorEmail(email);
    }

    public List<Usuario> listarUsuarios() {
        return urep.listarUsuarios();
    }

    public Usuario atualizarUsuario(Usuario usuarioAtualizado)
            throws UnsupportedEncodingException, NoSuchAlgorithmException {

        Usuario usuarioExistente = urep.buscarUsuarioPorId(usuarioAtualizado.getId());
        if (usuarioExistente != null && validarUsuario(usuarioAtualizado)) {
            if (!usuarioAtualizado.getSenha().equals(usuarioExistente.getSenha())) {
                usuarioAtualizado.setSenha(encodeSHA256(usuarioAtualizado.getSenha()));
            }
            return urep.atualizarUsuario(usuarioAtualizado);
        }
        return null;
    }

    public void removerUsuario(String id) {
        Usuario usuario = urep.buscarUsuarioPorId(id);
        if (usuario != null) {
            urep.removerEventosPorUsuario(usuario.getId());
            urep.removerUsuario(id);
        }
    }

    private boolean validarUsuario(Usuario usuario) {
        if (usuario == null || usuario.getNome() == null || usuario.getNome().trim().isEmpty()
                || usuario.getEmail() == null || usuario.getEmail().trim().isEmpty() || usuario.getSenha() == null
                || usuario.getSenha().trim().isEmpty()) {
            return false;
        }
        return true;
    }

    private static String encodeSHA256(String password) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(password.getBytes("UTF-8"));
        byte[] digest = md.digest();
        return DatatypeConverter.printBase64Binary(digest).toString();
    }
}