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
    private String perfil;
    private List<Usuario> usuarios;

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
        } else {

            usuario = (Usuario) session.getAttribute("usuarioLogado");
            perfil = usuario.getPerfil();

            String requestedURI = facesContext.getExternalContext().getRequestServletPath();
            if ("admin".equals(perfil) && requestedURI.startsWith("/user")) {
                redirect("/admin/home");
            } else if ("user".equals(perfil) && requestedURI.startsWith("/admin")) {
                redirect("/user/home");
            }
        }
    }

    private void redirect(String page) {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(page + "?faces-redirect=true");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String login() {
        try {
            if (usuarioBean.isValidPassword(emailLogin, senhaLogin)) {
                usuario = usuarioBean.buscarUsuarioPeloEmail(emailLogin);
                loggedIn = true;
                HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
                session.setAttribute("usuarioLogado", usuario);

                if ("admin".equals(usuario.getPerfil())) {
                    return "/admin/home?faces-redirect=true";
                } else {
                    return "/user/home?faces-redirect=true";
                }
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

        try {
            String contextPath = facesContext.getExternalContext().getRequestContextPath();
            facesContext.getExternalContext().redirect(contextPath + "/login.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void adicionarUsuario() {
        try {
            usuario.setPerfil(perfil);
            usuarioBean.adicionarUsuario(usuario);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso: Usuário cadastrado com sucesso.", "Usuário cadastrado com sucesso."));
            usuario = new Usuario();
            perfil = null;
        } catch (IllegalArgumentException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", e.getMessage()));
        }
    }

    public void atualizarUsuario() {
        try {
            if (usuario.getNome() == null || usuario.getNome().isEmpty() ||
                    usuario.getEmail() == null || usuario.getEmail().isEmpty() ||
                    usuario.getSenha() == null || usuario.getSenha().isEmpty()) {
                throw new IllegalArgumentException("Preencha todos os campos.");
            }
            usuarioBean.atualizarUsuario(usuario);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Usuário atualizado com sucesso."));
        } catch (IllegalArgumentException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", e.getMessage()));
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
            loggedIn = false;
            usuario = new Usuario();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso: Usuário removido com sucesso. Você será deslogado.", "Usuário removido com sucesso. Você será deslogado."));
            logout();
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IllegalArgumentException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", e.getMessage()));
        }
    }

    public void listarUsuarios() {
        usuarios = usuarioBean.buscarUsuariosPorPerfil("user");
    }

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

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }
}
