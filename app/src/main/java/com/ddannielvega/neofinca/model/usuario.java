package com.ddannielvega.neofinca.model;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class usuario extends RealmObject{

    @PrimaryKey
    private int id_usuario;
    private String us_nombre_usuario;
    private String us_contraseña;
    private String us_correo;
    private String us_telefonc;


    public usuario() {
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getUs_nombre_usuario() {
        return us_nombre_usuario;
    }

    public void setUs_nombre_usuario(String us_nombre_usuario) {
        this.us_nombre_usuario = us_nombre_usuario;
    }

    public String getUs_contraseña() {
        return us_contraseña;
    }

    public void setUs_contraseña(String us_contraseña) {
        this.us_contraseña = us_contraseña;
    }

    public String getUs_correo() {
        return us_correo;
    }

    public void setUs_correo(String us_correo) {
        this.us_correo = us_correo;
    }

    public String getUs_telefonc() {
        return us_telefonc;
    }

    public void setUs_telefonc(String us_telefonc) {
        this.us_telefonc = us_telefonc;
    }

    @Override
    public String toString() {
        return "usuario{" +
                "id_usuario=" + id_usuario +
                ", us_nombre_usuario='" + us_nombre_usuario + '\'' +
                ", us_contraseña='" + us_contraseña + '\'' +
                ", us_correo='" + us_correo + '\'' +
                ", us_telefonc='" + us_telefonc + '\'' +
                '}';
    }

}
