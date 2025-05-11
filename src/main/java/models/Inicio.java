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
    private String metaSeguidoresProgreso;               // Ej: "Progreso: 30 / 50"
    private String metaJuegosCompletadosDescripcion;     // Ej: "Completados: 1 / 5. Meta: 50 juegos"
    private String mejorasDelCanal;                      // Última mejora realizada
    private String fechaExtensible;                      // Fecha real del próximo extensible
    private String diasParaExtensible;                   // Ej: "Faltan 5 días"
    private String metaEspecifica;                       // Descripción de meta específica activa

    // === Constructores ===
    public Inicio() {
        // constructor vacío requerido para controladores o DAOs
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
            String metaEspecifica
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
    }

    // === Getters ===
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

    // === Setters ===
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

    // === Métodos auxiliares opcionales ===
    public int getTotalElementos() {
        return totalJuegos + totalLogros + totalConsolas + totalEventos + totalMetas + totalSeguidores;
    }

    public boolean hayMetasActivas() {
        return metaSeguidoresProgreso != null || metaJuegosCompletadosDescripcion != null
                || mejorasDelCanal != null || metaEspecifica != null;
    }
}
