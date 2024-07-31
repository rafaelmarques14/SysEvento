package dsc.controller;

import dsc.model.Usuario;
import dsc.model.UsuarioSessionBean;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.ejb.EJB;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

@Named
@SessionScoped
public class UsuarioController implements Serializable {

    @EJB
    private UsuarioSessionBean usuarioSessionBean;

    private Usuario usuario = new Usuario();
    private String emailLogin;
    private String senhaLogin;
    private boolean loggedIn = false;

    public String login() {
        Usuario usuarioLogado = usuarioSessionBean.buscarUsuarioPeloEmail(emailLogin);
        if (usuarioLogado != null && usuarioLogado.getSenha().equals(senhaLogin)) {
            usuario = usuarioLogado; // Configura o usuário atual
            loggedIn = true;
            return "home?faces-redirect=true"; // Redireciona para a página principal após login
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Email ou senha inválidos."));
            return null; // Fica na mesma página
        }
    }

    public String logout() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        if (session != null) {
            session.invalidate();
        }
        loggedIn = false;
        usuario = new Usuario(); // Limpa o usuário atual
        return "login.xhtml?faces-redirect=true"; // Redireciona para a página de login após logout
    }

    public void adicionarUsuario() {
        usuarioSessionBean.adicionarUsuario(usuario);
        // Redirecionar ou mostrar mensagem de sucesso
    }

    public void atualizarUsuario() {
        if (loggedIn) {
            usuarioSessionBean.atualizarUsuario(usuario);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Usuário atualizado com sucesso."));
            usuario = new Usuario(); // Limpa o formulário após a atualização
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Você precisa estar logado para atualizar o usuário."));
        }
    }

    public void removerUsuario() {
        if (emailLogin != null && !emailLogin.isEmpty()) {
            usuarioSessionBean.removerUsuario(emailLogin);
            emailLogin = null; // Limpa o campo após remoção
            loggedIn = false; // Desloga o usuário após a remoção
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Usuário removido com sucesso. Você será deslogado."));
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml"); // Redireciona para a página de login
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public List<Usuario> listarUsuarios() {
        return usuarioSessionBean.listarUsuarios();
    }

    // Getters e Setters para o Usuario e campos de login
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getEmailLogin() {
        return emailLogin;
    }

    public void setEmailLogin(String emailLogin) {
        this.emailLogin = emailLogin;
    }

    public String getSenhaLogin() {
        return senhaLogin;
    }

    public void setSenhaLogin(String senhaLogin) {
        this.senhaLogin = senhaLogin;
    }
}