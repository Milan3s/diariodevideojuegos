package models;

import java.time.LocalDate;

public class MetasTwitch {

    private int idMeta;
    private String descripcion;
    private int meta;
    private int actual;
    private String mes;
    private int anio;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private LocalDate fechaRegistro;

    public MetasTwitch() {
    }

    public MetasTwitch(int idMeta, String descripcion, int meta, int actual, String mes, int anio,
            LocalDate fechaInicio, LocalDate fechaFin, LocalDate fechaRegistro) {
        this.idMeta = idMeta;
        this.descripcion = descripcion;
        this.meta = meta;
        this.actual = actual;
        this.mes = mes;
        this.anio = anio;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.fechaRegistro = fechaRegistro;
    }

    public int getIdMeta() {
        return idMeta;
    }

    public void setIdMeta(int idMeta) {
        this.idMeta = idMeta;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getMeta() {
        return meta;
    }

    public void setMeta(int meta) {
        this.meta = meta;
    }

    public int getActual() {
        return actual;
    }

    public void setActual(int actual) {
        this.actual = actual;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    @Override
    public String toString() {
        return descripcion + " (" + mes + " " + anio + ")";
    }
}
