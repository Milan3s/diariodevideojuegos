package models;

/**
 * Modelo que representa los datos mostrados en el panel de "Resumen del
 * Diario". Incluye totales generales y el estado actual de metas relevantes.
 */
public class Inicio {

    // === Totales generales ===
    private int totalJuegos;
    private int totalLogros;
    private int totalConsolas;
    private int totalEventos;
    private int totalMetas;
    private int totalSeguidores;

    // === Detalle de metas ===
    private String metaSeguidoresProgreso;
    private String metaJuegosCompletadosDescripcion;
    private String mejorasDelCanal;
    private String fechaExtensible;
    private String diasParaExtensible;
    private String metaEspecifica;
    private String metaJuegosDescripcion;
    private String nombreExtensible;

    public Inicio() {
    }

    public Inicio(
            int totalJuegos,
            int totalLogros,
            int totalConsolas,
            int totalEventos,
            int totalMetas,
            int totalSeguidores,
            String metaSeguidoresProgreso,
            String metaJuegosCompletadosDescripcion,
            String mejorasDelCanal,
            String fechaExtensible,
            String diasParaExtensible,
            String metaEspecifica,
            String metaJuegosDescripcion,
            String nombreExtensible
    ) {
        this.totalJuegos = totalJuegos;
        this.totalLogros = totalLogros;
        this.totalConsolas = totalConsolas;
        this.totalEventos = totalEventos;
        this.totalMetas = totalMetas;
        this.totalSeguidores = totalSeguidores;
        this.metaSeguidoresProgreso = metaSeguidoresProgreso;
        this.metaJuegosCompletadosDescripcion = metaJuegosCompletadosDescripcion;
        this.mejorasDelCanal = mejorasDelCanal;
        this.fechaExtensible = fechaExtensible;
        this.diasParaExtensible = diasParaExtensible;
        this.metaEspecifica = metaEspecifica;
        this.metaJuegosDescripcion = metaJuegosDescripcion;
        this.nombreExtensible = nombreExtensible;
    }

    public int getTotalJuegos() {
        return totalJuegos;
    }

    public int getTotalLogros() {
        return totalLogros;
    }

    public int getTotalConsolas() {
        return totalConsolas;
    }

    public int getTotalEventos() {
        return totalEventos;
    }

    public int getTotalMetas() {
        return totalMetas;
    }

    public int getTotalSeguidores() {
        return totalSeguidores;
    }

    public String getMetaSeguidoresProgreso() {
        return metaSeguidoresProgreso;
    }

    public String getMetaJuegosCompletadosDescripcion() {
        return metaJuegosCompletadosDescripcion;
    }

    public String getMejorasDelCanal() {
        return mejorasDelCanal;
    }

    public String getFechaExtensible() {
        return fechaExtensible;
    }

    public String getDiasParaExtensible() {
        return diasParaExtensible;
    }

    public String getMetaEspecifica() {
        return metaEspecifica;
    }

    public String getMetaJuegosDescripcion() {
        return metaJuegosDescripcion;
    }

    public String getNombreExtensible() {
        return nombreExtensible;
    }

    public void setTotalJuegos(int totalJuegos) {
        this.totalJuegos = totalJuegos;
    }

    public void setTotalLogros(int totalLogros) {
        this.totalLogros = totalLogros;
    }

    public void setTotalConsolas(int totalConsolas) {
        this.totalConsolas = totalConsolas;
    }

    public void setTotalEventos(int totalEventos) {
        this.totalEventos = totalEventos;
    }

    public void setTotalMetas(int totalMetas) {
        this.totalMetas = totalMetas;
    }

    public void setTotalSeguidores(int totalSeguidores) {
        this.totalSeguidores = totalSeguidores;
    }

    public void setMetaSeguidoresProgreso(String metaSeguidoresProgreso) {
        this.metaSeguidoresProgreso = metaSeguidoresProgreso;
    }

    public void setMetaJuegosCompletadosDescripcion(String metaJuegosCompletadosDescripcion) {
        this.metaJuegosCompletadosDescripcion = metaJuegosCompletadosDescripcion;
    }

    public void setMejorasDelCanal(String mejorasDelCanal) {
        this.mejorasDelCanal = mejorasDelCanal;
    }

    public void setFechaExtensible(String fechaExtensible) {
        this.fechaExtensible = fechaExtensible;
    }

    public void setDiasParaExtensible(String diasParaExtensible) {
        this.diasParaExtensible = diasParaExtensible;
    }

    public void setMetaEspecifica(String metaEspecifica) {
        this.metaEspecifica = metaEspecifica;
    }

    public void setMetaJuegosDescripcion(String metaJuegosDescripcion) {
        this.metaJuegosDescripcion = metaJuegosDescripcion;
    }

    public void setNombreExtensible(String nombreExtensible) {
        this.nombreExtensible = nombreExtensible;
    }

    public int getTotalElementos() {
        return totalJuegos + totalLogros + totalConsolas + totalEventos + totalMetas + totalSeguidores;
    }

    public boolean hayMetasActivas() {
        return metaSeguidoresProgreso != null || metaJuegosCompletadosDescripcion != null
                || mejorasDelCanal != null || metaEspecifica != null || metaJuegosDescripcion != null || nombreExtensible != null;
    }
}
