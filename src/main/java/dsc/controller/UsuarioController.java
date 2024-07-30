package dsc.controller;

import dsc.model.Usuario;
import dsc.model.UsuarioSessionBean;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.ejb.EJB;
import jakarta.servlet.http.HttpSession;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class UsuarioController implements Serializable {

    @EJB
    private UsuarioSessionBean usuarioSessionBean;

    private Usuario usuario = new Usuario();
    private String emailLogin;
    private String senhaLogin;
    private boolean loggedIn = false;

    public String login() {
        Usuario usuarioLogado = usuarioSessionBean.buscarUsuarioPorEmail(emailLogin);
        if (usuarioLogado != null && usuarioLogado.getSenha().equals(senhaLogin)) {
            loggedIn = true;
            return "home?faces-redirect=true"; // Redirecionar para a página principal após login
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new jakarta.faces.application.FacesMessage("Email ou senha inválidos."));
            return null; // Ficar na mesma página
        }
    }

    public String logout() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        if (session != null) {
            session.invalidate();
        }
        loggedIn = false;
        return "login.xhtml?faces-redirect=true"; // Redirecionar para a página de login após logout
    }

    public void adicionarUsuario() {
        usuarioSessionBean.adicionarUsuario(usuario);
        // Redirecionar ou mostrar mensagem de sucesso
    }

    public void atualizarUsuario() {
        usuarioSessionBean.atualizarUsuario(usuario);
        // Redirecionar ou mostrar mensagem de sucesso
    }

    public void removerUsuario(Long id) {
        usuarioSessionBean.removerUsuario(id);
        // Redirecionar ou mostrar mensagem de sucesso
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
