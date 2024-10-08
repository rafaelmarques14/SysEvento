package dsc.controller;

import java.io.Serializable;
import java.io.IOException;
import java.security.Principal;
import java.util.Map;

import dsc.model.entidades.Usuario;
import dsc.model.sessionBeans.UsuarioBean;
import jakarta.annotation.security.DeclareRoles;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

@Named
@SessionScoped
@DeclareRoles({ "user", "admin" })
public class LoginMB implements Serializable {
    private static final long serialVersionUID = 1L;

    @EJB
    private UsuarioBean usuarioSessionBean;

    private String email;
    private String senha;
    private Usuario usuarioLogado;

    public String login() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

        try {
            request.login(email, senha);
        } catch (ServletException e) {

            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Login falhou: " + e.getMessage(), "Verifique seu e-mail e senha."));
            return "login.xhtml";
        }

        Principal principal = request.getUserPrincipal();
        if (principal == null) {

            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Login falhou", "Usuário não encontrado."));
            return "login.xhtml";
        }

        String userEmail = principal.getName();
        this.usuarioLogado = usuarioSessionBean.buscarUsuarioPorEmail(userEmail);

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        sessionMap.put("usuarioLogado", this.usuarioLogado);

        if (request.isUserInRole("user")) {
            return "/user/home.xhtml?faces-redirect=true";
        } else if (request.isUserInRole("admin")) {
            return "/admin/home.xhtml?faces-redirect=true";
        } else {
            return "error.xhtml";
        }
    }

    public String logout() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();

        try {
            request.logout();

            externalContext.invalidateSession();

            externalContext.redirect(request.getRequestURI());

        } catch (ServletException e) {
            return "error.xhtml?faces-redirect=true";
        }

        return null;
    }

    // Getters e Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public void setUsuarioLogado(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
    }
}
