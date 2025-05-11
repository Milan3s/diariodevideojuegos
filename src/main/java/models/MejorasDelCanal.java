package models;

import java.time.LocalDate;

public class MejorasDelCanal {

    private int id;
    private String descripcion;
    private int meta;
    private int actual;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private boolean cumplida;

    // Constructor completo
    public MejorasDelCanal(int id, String descripcion, int meta, int actual,
                           LocalDate fechaInicio, LocalDate fechaFin, boolean cumplida) {
        this.id = id;
        this.descripcion = descripcion;
        this.meta = meta;
        this.actual = actual;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.cumplida = cumplida;
    }

    // Constructor sin ID (para inserciones)
    public MejorasDelCanal(String descripcion, int meta, int actual,
                           LocalDate fechaInicio, LocalDate fechaFin, boolean cumplida) {
        this(-1, descripcion, meta, actual, fechaInicio, fechaFin, cumplida);
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public boolean isCumplida() {
        return cumplida;
    }

    public void setCumplida(boolean cumplida) {
        this.cumplida = cumplida;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
