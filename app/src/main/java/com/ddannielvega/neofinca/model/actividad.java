package com.ddannielvega.neofinca.model;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class actividad extends RealmObject{

    @PrimaryKey
    private int id_actividad;
    private String act_descripcion;
    private boolean deleted;
    //foreign
    private int id_usuario;

    public actividad(){
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    public int getId_actividad() {
        return id_actividad;
    }
    public void setId_actividad(int id_actividad) {
        this.id_actividad = id_actividad;
    }
    public String getAct_descripcion() {
        return act_descripcion;
    }
    public void setAct_descripcion(String act_descripcion) {
        this.act_descripcion = act_descripcion;
    }
    public int getId_usuario() {
        return id_usuario;
    }
    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public boolean isDeleted() {
        return deleted;
    }
    @Override
    public String toString() {
        return "actividad{" +
                "id_actividad=" + id_actividad +
                ", act_descripcion='" + act_descripcion + '\'' +
                ", deleted=" + deleted +
                ", id_usuario=" + id_usuario +
                '}';
    }
}


