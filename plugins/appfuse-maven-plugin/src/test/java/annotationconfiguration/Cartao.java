package annotationconfiguration;

import javax.persistence.*;
import java.sql.Timestamp;
import java.math.BigInteger;

@Entity
@Table(name = "Cartao")
public class Cartao {
    private String numcartao;
    private String nomegravar;
    private Tipocartao tipocartao;
    private Double limitecredito;
    private Timestamp validadecartao;
    private Double valoranuidade;
    private String moedaref;
    private Estadocartao estadocartao;
    private Integer balcao;
    private Integer cliente;
    private Byte natureza;
    private Short sequencia;
    private String moeda;
    private String situacaocarta;
    private String ultimoestado;
    private BigInteger numenvpin;
    private String obs;
    private Integer gestor;
    private String email;
    private String cel;
    private String crtuser;
    private String crtwkst;
    private Timestamp crtdate;

    @Id
    @Column(length = 20)
    public String getNumcartao() {
        return numcartao;
    }

    public void setNumcartao(String numcartao) {
        this.numcartao = numcartao;
    }

    @Basic
    @Column(name = "nomeGravar", length = 50)
    public String getNomegravar() {
        return nomegravar;
    }

    public void setNomegravar(String nomegravar) {
        this.nomegravar = nomegravar;
    }

    @JoinColumn(name = "tipoCartao", referencedColumnName = "idtipocartao")
    @ManyToOne
    public Tipocartao getTipocartao() {
        return tipocartao;
    }

    public void setTipocartao(Tipocartao tipocartao) {
        this.tipocartao = tipocartao;
    }

    @Basic
    @Column(name = "limiteCredito", precision = 15, scale = 2)
    public Double getLimitecredito() {
        return limitecredito;
    }

    public void setLimitecredito(Double limitecredito) {
        this.limitecredito = limitecredito;
    }

    @Basic
    @Column(name = "validadeCartao", length = 23)
    public Timestamp getValidadecartao() {
        return validadecartao;
    }

    public void setValidadecartao(Timestamp validadecartao) {
        this.validadecartao = validadecartao;
    }

    @Basic
    @Column(name = "valoranuidade", precision = 15, scale = 2)
    public Double getValoranuidade() {
        return valoranuidade;
    }

    public void setValoranuidade(Double valoranuidade) {
        this.valoranuidade = valoranuidade;
    }

    @Basic
    @Column(name = "moedaRef", length = 3)
    public String getMoedaref() {
        return moedaref;
    }

    public void setMoedaref(String moedaref) {
        this.moedaref = moedaref;
    }

    @JoinColumn(name = "estadoCartao", referencedColumnName = "idestadocartao")
    @ManyToOne
    public Estadocartao getEstadocartao() {
        return estadocartao;
    }

    public void setEstadocartao(Estadocartao estadocartao) {
        this.estadocartao = estadocartao;
    }

    @Basic
    @Column(name = "balcao", precision = 4, scale = 0)
    public Integer getBalcao() {
        return balcao;
    }

    public void setBalcao(Integer balcao) {
        this.balcao = balcao;
    }

    @Basic
    @Column(name = "cliente", precision = 9, scale = 0)
    public Integer getCliente() {
        return cliente;
    }

    public void setCliente(Integer cliente) {
        this.cliente = cliente;
    }

    @Basic
    @Column(name = "natureza", precision = 2, scale = 0)
    public Byte getNatureza() {
        return natureza;
    }

    public void setNatureza(Byte natureza) {
        this.natureza = natureza;
    }

    @Basic
    @Column(name = "sequencia", precision = 3, scale = 0)
    public Short getSequencia() {
        return sequencia;
    }

    public void setSequencia(Short sequencia) {
        this.sequencia = sequencia;
    }

    @Basic
    @Column(name = "moeda", length = 3)
    public String getMoeda() {
        return moeda;
    }

    public void setMoeda(String moeda) {
        this.moeda = moeda;
    }

    @Basic
    @Column(name = "situacaoCarta")
    public String getSituacaocarta() {
        return situacaocarta;
    }

    public void setSituacaocarta(String situacaocarta) {
        this.situacaocarta = situacaocarta;
    }

    @Basic
    @Column(name = "ultimoEstado")
    public String getUltimoestado() {
        return ultimoestado;
    }

    public void setUltimoestado(String ultimoestado) {
        this.ultimoestado = ultimoestado;
    }

    @Basic
    @Column(name = "numEnvPIN", precision = 18, scale = 0)
    public BigInteger getNumenvpin() {
        return numenvpin;
    }

    public void setNumenvpin(BigInteger numenvpin) {
        this.numenvpin = numenvpin;
    }

    @Basic
    @Column(name = "obs", length = 100)
    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    @Basic
    @Column(name = "gestor", precision = 9, scale = 0)
    public Integer getGestor() {
        return gestor;
    }

    public void setGestor(Integer gestor) {
        this.gestor = gestor;
    }

    @Basic
    @Column(name = "email", length = 50)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "cel", length = 15)
    public String getCel() {
        return cel;
    }

    public void setCel(String cel) {
        this.cel = cel;
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

        Cartao cartao = (Cartao) o;

        if (numcartao != null ? !numcartao.equals(cartao.numcartao) : cartao.numcartao != null) return false;

        return true;
    }

    public int hashCode() {
        return (numcartao != null ? numcartao.hashCode() : 0);
    }
}

