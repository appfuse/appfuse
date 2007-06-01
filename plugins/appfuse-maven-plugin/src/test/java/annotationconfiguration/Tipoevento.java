package annotationconfiguration;

import javax.persistence.*;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.io.Serializable;


@Entity
@Table(name = "Tipoevento")
public class Tipoevento implements Serializable {
    private Integer ordem;
    private String nome;
    private String descricao;
    private String crtuser;
    private String crtwkst;
    private Timestamp crtdate;

    @Id
    @Column(precision = 4, scale = 0)
    public Integer getOrdem() {
        return ordem;
    }

    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }


    @Column(name = "nome", length = 50)
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


    @Column(name = "descricao")
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }


    @Column(name = "CrtUser", length = 30)
    public String getCrtuser() {
        return crtuser;
    }

    public void setCrtuser(String crtuser) {
        this.crtuser = crtuser;
    }


    @Column(name = "CrtWkst", length = 30)
    public String getCrtwkst() {
        return crtwkst;
    }

    public void setCrtwkst(String crtwkst) {
        this.crtwkst = crtwkst;
    }


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

        Tipoevento that = (Tipoevento) o;

        if (ordem != null ? !ordem.equals(that.ordem) : that.ordem != null) return false;

        return true;
    }

    public int hashCode() {
        return (ordem != null ? ordem.hashCode() : 0);
    }


    public String toString() {
        return "Tipoevento{" +
                "ordem=" + ordem +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", crtuser='" + crtuser + '\'' +
                ", crtwkst='" + crtwkst + '\'' +
                ", crtdate=" + crtdate +
                '}';
    }
}
