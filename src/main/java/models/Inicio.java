package models;

public class Inicio {

    private int totalJuegos;
    private int totalLogros;
    private int totalConsolas;
    private int totalEventos;
    private int totalMetas;
    private int totalSeguidores;

    private String metaSeguidoresProgreso;
    private String metaJuegosCompletadosDescripcion;
    private String mejorasDelCanal;
    private String fechaExtensible;
    private String diasParaExtensible;
    private String metaEspecifica;

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

    // Getters
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

    // Setters
    public void setMetaEspecifica(String metaEspecifica) {
        this.metaEspecifica = metaEspecifica;
    }
}
