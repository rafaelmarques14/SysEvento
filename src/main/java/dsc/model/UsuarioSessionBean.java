package dsc.model;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.util.List;

@Stateless
public class UsuarioSessionBean {

    @Inject
    private UsuarioRepositorio usuarioRepositorio;

    public void adicionarUsuario(Usuario usuario) {
        usuarioRepositorio.adicionarUsuario(usuario);
    }

    public void atualizarUsuario(Usuario usuarioAtualizado) {
        usuarioRepositorio.atualizarUsuario(usuarioAtualizado);
    }

    public void removerUsuario(String email) {
        usuarioRepositorio.removerUsuario(email);
    }

    public Usuario buscarUsuarioPeloEmail(String email) {
        return usuarioRepositorio.buscarUsuarioPeloEmail(email);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepositorio.listarUsuarios();
    }
}
