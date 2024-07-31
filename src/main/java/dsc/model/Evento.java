package dsc.model;

import java.util.Date;

public class Evento {
    private String nome;
    private Date data;
    private Usuario usuario;  // Adiciona o atributo usuario

    public Evento() {}

    public Evento(String nome, Date data, Usuario usuario) {
        this.nome = nome;
        this.data = data;
        this.usuario = usuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
