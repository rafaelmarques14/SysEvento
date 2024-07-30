package dsc.model;

import jakarta.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class UsuarioSessionBean {

    private List<Usuario> usuarios = new ArrayList<>();

    public void adicionarUsuario(Usuario usuario) {
        if (usuario != null && usuario.getEmail() != null){
            usuarios.add(usuario);
            usuario = new Usuario(); // Limpa o formulário após o cadastro
        }
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

    public void removerUsuario(String email) {
        usuarios.removeIf(u -> u.getEmail().equals(email));
    }

    public Usuario buscarUsuarioPeloEmail(String email) {
        return usuarios.stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }


    public List<Usuario> listarUsuarios() {
        return usuarios;
    }
}
