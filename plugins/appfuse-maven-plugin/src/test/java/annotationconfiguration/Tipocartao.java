package annotationconfiguration;

import javax.persistence.*;
import java.math.BigInteger;
import java.sql.Timestamp;

@Entity
@Table(name = "Tipocartao")
public class Tipocartao {
    private Integer idtipocartao;
    private String nome;
    private String crtuser;
    private String crtwkst;
    private Timestamp crtdate;

    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "IDTipoCartao", nullable = false, precision = 4, scale = 0)
    public Integer getIdtipocartao() {
        return idtipocartao;
    }

    public void setIdtipocartao(Integer idtipocartao) {
        this.idtipocartao = idtipocartao;
    }

    @Basic
    @Column(name = "nome", nullable = false, length = 50)
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

        Tipocartao that = (Tipocartao) o;

        if (idtipocartao != null ? !idtipocartao.equals(that.idtipocartao) : that.idtipocartao != null) return false;

        return true;
    }

    public int hashCode() {
        return (idtipocartao != null ? idtipocartao.hashCode() : 0);
    }
}
