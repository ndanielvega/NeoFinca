package com.ddannielvega.neofinca.model;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class finxemp extends RealmObject {

    @PrimaryKey
    private int id_finxemp;
    private String finxemp_estado;
    //foraneas
    private int id_finca;
    private int id_empleado;



    public finxemp() {
    }

    public int getId_finxemp() {
        return id_finxemp;
    }

    public void setId_finxemp(int id_finxemp) {
        this.id_finxemp = id_finxemp;
    }

    public String getFinxemp_estado() {
        return finxemp_estado;
    }

    public void setFinxemp_estado(String finxemp_estado) {
        this.finxemp_estado = finxemp_estado;
    }

    public int getId_finca() {
        return id_finca;
    }

    public void setId_finca(int id_finca) {
        this.id_finca = id_finca;
    }

    public int getId_empleado() {
        return id_empleado;
    }

    public void setId_empleado(int id_empleado) {
        this.id_empleado = id_empleado;
    }

    @Override
    public String toString() {
        return "finxemp{" +
                "id_finxemp=" + id_finxemp +
                ", finxemp_estado='" + finxemp_estado + '\'' +
                ", id_finca=" + id_finca +
                ", id_empleado=" + id_empleado +
                '}';
    }
}
