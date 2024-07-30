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

    public void atualizarUsuario(Usuario usuarioAtualizado) {
        for (int i = 0; i < usuarios.size(); i++) {
            Usuario u = usuarios.get(i);
            if (u.getEmail().equals(usuarioAtualizado.getEmail())) {
                // Atualiza as informações do usuário
                u.setNome(usuarioAtualizado.getNome());
                u.setEmail(usuarioAtualizado.getEmail());
                u.setSenha(usuarioAtualizado.getSenha());
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
