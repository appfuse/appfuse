package annotationconfiguration;

import javax.persistence.Column;
import java.io.Serializable;

public class EventoPK implements Serializable {
    private Integer tipoevento;
    private String numcartao;

    @Column(precision = 4, scale = 0)
    public Integer getTipoevento() {
        return tipoevento;
    }

    public void setTipoevento(Integer tipoevento) {
        this.tipoevento = tipoevento;
    }

    @Column(length = 20)
    public String getNumcartao() {
        return numcartao;
    }

    public void setNumcartao(String numcartao) {
        this.numcartao = numcartao;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventoPK eventoPK = (EventoPK) o;

        if (numcartao != null ? !numcartao.equals(eventoPK.numcartao) : eventoPK.numcartao != null) return false;
        if (tipoevento != null ? !tipoevento.equals(eventoPK.tipoevento) : eventoPK.tipoevento != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (tipoevento != null ? tipoevento.hashCode() : 0);
        result = 31 * result + (numcartao != null ? numcartao.hashCode() : 0);
        return result;
    }
}
