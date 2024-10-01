package dsc.controller;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import dsc.model.entidades.Usuario;
import dsc.model.entidades.UsuarioPerfil;
import dsc.model.sessionBeans.UsuarioBean;
import jakarta.annotation.security.DeclareRoles;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

//@Named
//@RequestScoped

@Named
@SessionScoped
@DeclareRoles({ "user", "admin" })
public class UsuarioMB implements Serializable {
    private static final long serialVersionUID = -4272609180484517298L;

    @EJB
    private UsuarioBean usuarioSessionBean;

    private Usuario usuario;
    private String id;
    private Usuario usuarioSelecionado = new Usuario();
    private String perfil; // Campo para armazenar o perfil do usuário

    public UsuarioMB() {
        this.usuario = new Usuario();
    }

    public String criarUsuario() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        try {
            UsuarioPerfil usuarioPerfil = new UsuarioPerfil();
            usuarioPerfil.setPerfil(this.perfil); // Definindo o perfil
            usuarioPerfil.setEmail(usuario.getEmail());

            usuarioSessionBean.criarUsuario(usuario, usuarioPerfil);
            usuario = new Usuario();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuário criado com sucesso!", null));
        } catch (IllegalArgumentException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
            return null;
        }
        return "home?faces-redirect=true";
    }

    public String carregarUsuarioParaEdicao(String id) {
        this.usuarioSelecionado = usuarioSessionBean.buscarUsuarioPorId(id);
        if (this.usuarioSelecionado != null) {
            return null; // Sucesso ao carregar
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao carregar usuário para edição.", null));
            return "usuarios?faces-redirect=true"; // Erro ao carregar
        }
    }

    public String atualizarUsuario() {
        try {
            usuarioSessionBean.atualizarUsuario(usuarioSelecionado);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuário atualizado com sucesso!", null));
            return "usuarios?faces-redirect=true"; // Redireciona para a lista de usuários
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Erro ao atualizar usuário: " + e.getMessage(), null));
            return null;
        }
    }

    public String removerUsuario(String id) {
        try {
            usuarioSessionBean.removerUsuario(id);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuário removido com sucesso!", null));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao remover usuário: " + e.getMessage(), null));
            return null;
        }
        return "usuarios?faces-redirect=true"; // Redireciona para a lista de usuários
    }

    public Usuario buscarUsuario() {
        usuario = usuarioSessionBean.buscarUsuarioPorId(id);
        return usuario;
    }

    public Usuario buscarUsuarioPorId(String id) {
        return usuarioSessionBean.buscarUsuarioPorId(id);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioSessionBean.listarUsuarios();
    }

    // Getters e Setters
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Usuario getUsuarioSelecionado() {
        return usuarioSelecionado;
    }

    public void setUsuarioSelecionado(Usuario usuarioSelecionado) {
        this.usuarioSelecionado = usuarioSelecionado;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }
}