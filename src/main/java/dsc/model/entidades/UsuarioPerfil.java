package dsc.model.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class UsuarioPerfil {

    @Id
    @Column(length = 255,
            nullable = false,
            unique = true,
            updatable = false)
    private String email;

    @Column(length = 10,
            nullable = false)
    private String perfil;

    public String getEmail() {
        return email;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }
}