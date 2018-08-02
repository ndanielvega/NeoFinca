package com.ddannielvega.neofinca.model;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class finca extends RealmObject {

    @PrimaryKey
    private int id_finca;
    private String fin_nombre;
    private String fin_ubicacion;
    private boolean deleted;
    //foranea
    private int id_usuario;

    public finca() {
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public int getId_finca() {
        return id_finca;
    }

    public void setId_finca(int id_finca) {
        this.id_finca = id_finca;
    }

    public String getFin_nombre() {
        return fin_nombre;
    }

    public void setFin_nombre(String fin_nombre) {
        this.fin_nombre = fin_nombre;
    }

    public String getFin_ubicacion() {
        return fin_ubicacion;
    }

    public void setFin_ubicacion(String fin_ubicacion) {
        this.fin_ubicacion = fin_ubicacion;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    @Override
    public String toString() {
        return "finca{" +
                "id_finca=" + id_finca +
                ", fin_nombre='" + fin_nombre + '\'' +
                ", fin_ubicacion='" + fin_ubicacion + '\'' +
                ", id_usuario=" + id_usuario +
                '}';
    }
}
