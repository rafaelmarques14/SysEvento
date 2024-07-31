package dsc.controller;

import dsc.model.Evento;
import dsc.model.EventoSessionBean;
import dsc.model.Usuario;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.ejb.EJB;
import java.io.Serializable;
import java.util.List;

@Named
@SessionScoped
public class EventoController implements Serializable {

    @EJB
    private EventoSessionBean eventoSessionBean;

    private Evento evento = new Evento();
    private List<Evento> eventos;

    private Usuario usuarioAutenticado;

    public EventoController() {
        // Simula a obtenção do usuário autenticado
        // Em uma aplicação real, você obteria isso de um serviço de autenticação
        this.usuarioAutenticado = new Usuario();
    }

    public String cadastrarEvento() {
        try {
            evento.setUsuario(usuarioAutenticado);
            eventoSessionBean.adicionarEvento(evento);
            eventos = eventoSessionBean.listarEventos(usuarioAutenticado);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Evento cadastrado com sucesso."));
            evento = new Evento();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao cadastrar evento.", e.getMessage()));
        }
        return "home?faces-redirect=true";
    }

    public void prepararRemocao(Evento evento) {
        this.evento = evento;
    }

    public void removerEvento() {
        try {
            eventoSessionBean.removerEvento(evento);
            eventos = eventoSessionBean.listarEventos(usuarioAutenticado);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Evento removido com sucesso."));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao remover evento.", e.getMessage()));
        }
    }

    public void prepararAtualizacao(Evento evento) {
        this.evento = evento;
    }

    public String atualizarEvento() {
        for (Evento e : eventos) {
            if (e.getNome().equals(evento.getNome()) && e.getUsuario().equals(usuarioAutenticado)) {
                e.setData(evento.getData());
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Evento atualizado com sucesso!"));
                break;
            }
        }
        evento = new Evento();
        return "home?faces-redirect=true";
    }

    public List<Evento> getEventos() {
        if (eventos == null) {
            eventos = eventoSessionBean.listarEventos(usuarioAutenticado);
        }
        return eventos;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public Usuario getUsuarioAutenticado() {
        return usuarioAutenticado;
    }

    public void setUsuarioAutenticado(Usuario usuarioAutenticado) {
        this.usuarioAutenticado = usuarioAutenticado;
    }
}
