package annotationconfiguration;

import javax.persistence.*;
import java.sql.Blob;
import java.sql.Timestamp;

@Entity
@Table(name = "Ficheiro")
public class Ficheiro {
    private String nome;
    private Blob dados;
    private Integer estado;
    private String crtuser;
    private String crtwkst;
    private Timestamp crtdate;

    @Id
    @Column(name = "nome", nullable = false, length = 50)
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Basic
    @Column(name = "dados", length = 2147483647)
    public Blob getDados() {
        return dados;
    }

    public void setDados(Blob dados) {
        this.dados = dados;
    }

    @Basic
    @Column(name = "estado", precision = 4, scale = 0)
    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    @Basic
    @Column(name = "CrtUser", length = 30)
    public String getCrtuser() {
        return crtuser;
    }

    public void setCrtuser(String crtuser) {
        this.crtuser = crtuser;
    }

    @Basic
    @Column(name = "CrtWkst", length = 30)
    public String getCrtwkst() {
        return crtwkst;
    }

    public void setCrtwkst(String crtwkst) {
        this.crtwkst = crtwkst;
    }

    @Basic
    @Column(name = "CrtDate", length = 23)
    public Timestamp getCrtdate() {
        return crtdate;
    }

    public void setCrtdate(Timestamp crtdate) {
        this.crtdate = crtdate;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ficheiro ficheiro = (Ficheiro) o;

        if (nome != null ? !nome.equals(ficheiro.nome) : ficheiro.nome != null) return false;

        return true;
    }

    public int hashCode() {
        return (nome != null ? nome.hashCode() : 0);
    }
}
