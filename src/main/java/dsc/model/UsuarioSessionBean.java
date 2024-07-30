package dsc.model;

import dsc.model.Usuario;
import jakarta.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class UsuarioSessionBean {

    private List<Usuario> usuarios = new ArrayList<>();

    public void adicionarUsuario(Usuario usuario) {
        usuarios.add(usuario);
    }

    public void atualizarUsuario(Usuario usuario) {
        for (Usuario u : usuarios) {
            if (u.getId().equals(usuario.getId())) {
                u.setNome(usuario.getNome());
                u.setEmail(usuario.getEmail());
                u.setSenha(usuario.getSenha());
                break;
            }
        }
    }

    public void removerUsuario(Long id) {
        usuarios.removeIf(u -> u.getId().equals(id));
    }

    public Usuario buscarUsuarioPorEmail(String email) {
        for (Usuario u : usuarios) {
            if (u.getEmail().equals(email)) {
                return u;
            }
        }
        return null;
    }

    public List<Usuario> listarUsuarios() {
        return usuarios;
    }
}
