package com.ddannielvega.neofinca.model;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class empleado extends RealmObject {


    @PrimaryKey
    private int id_empleado;
    private String emp_nombre;
    private String emp_documento;
    private String emp_telefono;
    private int emp_valorxjornal;
    private boolean deleted;
    //foranea
    private int id_usuario;

    public empleado() {
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public int getId_empleado() {
        return id_empleado;
    }

    public void setId_empleado(int id_empleado) {
        this.id_empleado = id_empleado;
    }

    public String getEmp_nombre() {
        return emp_nombre;
    }

    public void setEmp_nombre(String emp_nombre) {
        this.emp_nombre = emp_nombre;
    }

    public String getEmp_documento() {
        return emp_documento;
    }

    public void setEmp_documento(String emp_documento) {
        this.emp_documento = emp_documento;
    }

    public String getEmp_telefono() {
        return emp_telefono;
    }

    public void setEmp_telefono(String emp_telefono) {
        this.emp_telefono = emp_telefono;
    }

    public int getEmp_valorxjornal() {
        return emp_valorxjornal;
    }

    public void setEmp_valorxjornal(int emp_valorxjornal) {
        this.emp_valorxjornal = emp_valorxjornal;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    @Override
    public String toString() {
        return "empleado{" +
                "id_empleado=" + id_empleado +
                ", emp_nombre='" + emp_nombre + '\'' +
                ", emp_documento='" + emp_documento + '\'' +
                ", emp_telefono='" + emp_telefono + '\'' +
                ", emp_valorxjornal=" + emp_valorxjornal +
                ", deleted=" + deleted +
                ", id_usuario=" + id_usuario +
                '}';
    }
}
