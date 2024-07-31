package dsc.controller;

import dsc.model.Evento;
import dsc.model.EventoSessionBean;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.ejb.EJB;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Named
@SessionScoped
public class EventoController implements Serializable {

    @EJB
    private EventoSessionBean eventoSessionBean;

    private Evento evento = new Evento();
    private List<Evento> eventos; // List to hold the events

    private static final String DATE_FORMAT = "dd/MM/yyyy"; // Definir o formato da data


    public void cadastrarEvento() {
        try {
            eventoSessionBean.adicionarEvento(evento);
            eventos = eventoSessionBean.listarEventos();  // Atualiza a lista de eventos
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Evento cadastrado com sucesso."));
            evento = new Evento();  // Limpa o formulário
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao cadastrar evento.", e.getMessage()));
        }
    }


    public void prepararRemocao(Evento evento) {
        this.evento = evento; // Configura o evento para ser removido
    }

    public void removerEvento() {
        try {
            eventoSessionBean.removerEvento(evento);
            eventos = eventoSessionBean.listarEventos();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Evento removido com sucesso."));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao remover evento.", e.getMessage()));
        }
    }

    public void prepararAtualizacao(Evento evento) {
        this.evento = evento; // Configurar o evento para o diálogo de atualização
    }

    public void atualizarEvento() {
        // Atualizar o evento na lista
        for (Evento e : eventos) {
            if (e.getNome().equals(evento.getNome())) {
                e.setData(evento.getData());
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Evento atualizado com sucesso!"));
                break;
            }
        }
        evento = new Evento(); // Resetar o formulário
    }

    // Método para formatar a data
    public String formatarData(Date data) {
        if (data != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
            return sdf.format(data);
        }
        return "";
    }

    public List<Evento> getEventos() {
        if (eventos == null) {
            eventos = eventoSessionBean.listarEventos();
        }
        return eventos;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }
}
