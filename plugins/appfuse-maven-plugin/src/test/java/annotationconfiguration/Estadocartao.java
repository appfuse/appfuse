package annotationconfiguration;

import javax.persistence.*;

@Entity
@Table(name = "Estadocartao")
public class Estadocartao {
    private Integer idestadocartao;
    private String nome;

    @Id
    @Column(name = "IDEstadoCartao", nullable = false, precision = 4, scale = 0)
    public Integer getIdestadocartao() {
        return idestadocartao;
    }

    public void setIdestadocartao(Integer idestadocartao) {
        this.idestadocartao = idestadocartao;
    }

    @Basic
    @Column(name = "nome", length = 30)
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Estadocartao that = (Estadocartao) o;

        if (idestadocartao != null ? !idestadocartao.equals(that.idestadocartao) : that.idestadocartao != null)
            return false;

        return true;
    }

    public int hashCode() {
        return (idestadocartao != null ? idestadocartao.hashCode() : 0);
    }
}
