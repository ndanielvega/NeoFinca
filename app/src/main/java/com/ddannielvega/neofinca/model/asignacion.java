package com.ddannielvega.neofinca.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class asignacion extends RealmObject {

    @PrimaryKey
    private int id_asignacion;
    private String asiig_fecha;
    private int asig_cant_jornales;
    private String asig_estado;
    private String asig_totalxactividad;
    private boolean deleted;
    //foraneas
    private int id_finca;
    private int id_actividad;
    private int id_empleado;
    private int id_factura;

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public asignacion(){
    }

    public int getId_asignacion() {
        return id_asignacion;
    }

    public void setId_asignacion(int id_asignacion) {
        this.id_asignacion = id_asignacion;
    }

    public String getAsiig_fecha() {
        return asiig_fecha;
    }

    public void setAsiig_fecha(String asiig_fecha) {
        this.asiig_fecha = asiig_fecha;
    }

    public int getAsig_cant_jornales() {
        return asig_cant_jornales;
    }

    public void setAsig_cant_jornales(int asig_cant_jornales) {
        this.asig_cant_jornales = asig_cant_jornales;
    }

    public String getAsig_estado() {
        return asig_estado;
    }

    public void setAsig_estado(String asig_estado) {
        this.asig_estado = asig_estado;
    }

    public String getAsig_totalxactividad() {
        return asig_totalxactividad;
    }

    public void setAsig_totalxactividad(String asig_totalxactividad) {
        this.asig_totalxactividad = asig_totalxactividad;
    }

    public int getId_finca() {
        return id_finca;
    }

    public void setId_finca(int id_finca) {
        this.id_finca = id_finca;
    }

    public int getId_actividad() {
        return id_actividad;
    }

    public void setId_actividad(int id_actividad) {
        this.id_actividad = id_actividad;
    }

    public int getId_empleado() {
        return id_empleado;
    }

    public void setId_empleado(int id_empleado) {
        this.id_empleado = id_empleado;
    }

    public int getId_factura() {
        return id_factura;
    }

    public void setId_factura(int id_factura) {
        this.id_factura = id_factura;
    }

    @Override
    public String toString() {
        return "asignacion{" +
                "id_asignacion=" + id_asignacion +
                ", asiig_fecha='" + asiig_fecha + '\'' +
                ", asig_cant_jornales=" + asig_cant_jornales +
                ", asig_estado='" + asig_estado + '\'' +
                ", asig_totalxactividad='" + asig_totalxactividad + '\'' +
                ", id_finca=" + id_finca +
                ", id_actividad=" + id_actividad +
                ", id_empleado=" + id_empleado +
                ", id_factura=" + id_factura +
                '}';
    }
}
