package annotationconfiguration;

import javax.persistence.*;
import java.math.BigInteger;
import java.sql.Timestamp;

@Entity
//@IdClass(EventoPK.class)
@Table(name = "Evento")
public class Evento {
    private Integer tipoevento;
    private Tipoevento tipoeventoO;
    private String numcartao;
    private String nome;
    private Timestamp data;
    private String crtuser;
    private String crtwkst;
    private Timestamp crtdate;
    private Cartao cartao;

    @Id
    @Column(precision = 4, scale = 0)
    public Integer getTipoevento() {
        return tipoevento;
    }

    public void setTipoevento(Integer tipoevento) {
        this.tipoevento = tipoevento;
    }

    @Id
    @Column(length = 20)
    public String getNumcartao() {
        return numcartao;
    }

    public void setNumcartao(String numcartao) {
        this.numcartao = numcartao;
    }

    @Basic
    @Column(name = "nome", length = 50)
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Basic
    @Column(name = "data", length = 23)
    public Timestamp getData() {
        return data;
    }

    public void setData(Timestamp data) {
        this.data = data;
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



    @JoinColumn(name = "numcartao", referencedColumnName = "numcartao", insertable = false, updatable = false)
    @ManyToOne
    public Cartao getCartao() {
        return cartao;
    }


    @JoinColumn(name = "tipoevento", referencedColumnName = "ordem", insertable = false, updatable = false)
    @ManyToOne
    public Tipoevento getTipoeventoO() {
        return tipoeventoO;
    }

    public void setTipoeventoO(Tipoevento tipoeventoO) {
        this.tipoeventoO = tipoeventoO;
    }

    public void setCartao(Cartao cartao) {
        this.cartao = cartao;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Evento evento = (Evento) o;

        if (numcartao != null ? !numcartao.equals(evento.numcartao) : evento.numcartao != null) return false;
        if (tipoevento != null ? !tipoevento.equals(evento.tipoevento) : evento.tipoevento != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (tipoevento != null ? tipoevento.hashCode() : 0);
        result = 31 * result + (numcartao != null ? numcartao.hashCode() : 0);
        return result;
    }
}