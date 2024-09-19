package dsc.controller;

import dsc.model.Usuario;
import dsc.model.UsuarioBean;
import jakarta.annotation.PostConstruct;
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
public class UsuarioMB implements Serializable {

    @EJB
    private UsuarioBean usuarioBean;

    private Usuario usuario = new Usuario();
    private String emailLogin;
    private String senhaLogin;
    private boolean loggedIn = false;

    @PostConstruct
    public void init() {
        verificarAutenticacao();
    }

    public void verificarAutenticacao() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        if (session == null || session.getAttribute("usuarioLogado") == null) {
            try {
                facesContext.getExternalContext().redirect("login.xhtml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String login() {
        try {
            Usuario usuarioLogado = usuarioBean.buscarUsuarioPeloEmail(emailLogin);
            if (usuarioLogado != null && usuarioLogado.getSenha().equals(senhaLogin)) {
                usuario = usuarioLogado;
                loggedIn = true;
                HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
                session.setAttribute("usuarioLogado", usuarioLogado);
                return "home?faces-redirect=true";
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Email ou senha inválidos."));
                return null;
            }
        } catch (IllegalArgumentException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", e.getMessage()));
            return null;
        }
    }

    public String logout() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        if (session != null) {
            session.invalidate();
        }
        loggedIn = false;
        usuario = new Usuario();
        return "login.xhtml?faces-redirect=true";
    }

    public void adicionarUsuario() {
        try {
            usuarioBean.adicionarUsuario(usuario);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso: Usuário cadastrado com sucesso.", "Usuário cadastrado com sucesso."));
            usuario = new Usuario();
        } catch (IllegalArgumentException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", e.getMessage()));
        }
    }

    public void atualizarUsuario() {
        try {
            if (usuario.getNome() == null || usuario.getNome().isEmpty() ||
                    usuario.getEmail() == null || usuario.getEmail().isEmpty() ||
                    usuario.getSenha() == null || usuario.getSenha().isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro: Nome, email e senha são obrigatórios.", "Nome, email e senha são obrigatórios."));
                return;
            }

            if (!usuario.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro: Email inválido.", "Email inválido."));
                return;
            }

            if (loggedIn) {
                usuarioBean.atualizarUsuario(usuario);
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso: Usuário atualizado com sucesso.", "Usuário atualizado com sucesso."));
                usuario = new Usuario();
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro: Você precisa estar logado para atualizar o usuário.", "Você precisa estar logado para atualizar o usuário."));
            }
        } catch (IllegalArgumentException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", e.getMessage()));
        }
    }

    public void removerUsuario() {
        try {
            if (emailLogin == null || emailLogin.isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro: O campo email é obrigatório para remover um usuário.", "O campo email é obrigatório para remover um usuário."));
                return;
            }
            usuarioBean.removerUsuario(emailLogin);
            emailLogin = null;
            loggedIn = false; // Definir como não logado
            usuario = new Usuario(); // Limpar o usuário atual
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso: Usuário removido com sucesso. Você será deslogado.", "Usuário removido com sucesso. Você será deslogado."));
            logout(); // Chamar o método de logout para encerrar a sessão
        } catch (IllegalArgumentException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", e.getMessage()));
        }
    }

    // Getters e Setters

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

    public boolean isLoggedIn() {
        return loggedIn;
    }
}
