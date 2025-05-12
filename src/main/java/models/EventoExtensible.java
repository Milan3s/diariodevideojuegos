package models;

/**
 *
 * @author Milanes
 */
public class EventoExtensible {

    private int idExtensible;
    private String motivo;
    private String fechaEvento;

    public EventoExtensible() {
    }

    // Constructor completo (con ID)
    public EventoExtensible(int idExtensible, String motivo, String fechaEvento) {
        this.idExtensible = idExtensible;
        this.motivo = motivo;
        this.fechaEvento = fechaEvento;
    }

    // Constructor sin ID (para inserts)
    public EventoExtensible(String motivo, String fechaEvento) {
        this.motivo = motivo;
        this.fechaEvento = fechaEvento;
    }

    public int getIdExtensible() {
        return idExtensible;
    }

    public void setIdExtensible(int idExtensible) {
        this.idExtensible = idExtensible;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getFechaEvento() {
        return fechaEvento;
    }

    public void setFechaEvento(String fechaEvento) {
        this.fechaEvento = fechaEvento;
    }

    @Override
    public String toString() {
        return motivo;
    }
}
