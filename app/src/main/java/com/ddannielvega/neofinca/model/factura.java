package com.ddannielvega.neofinca.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class factura  extends RealmObject {

    @PrimaryKey
    private int id_factura;
    private String fact_valortotal;
    private String fact_fecha;
    //foraneas
    private int id_empleado;
    private int id_finca;


    public factura(){
    }

    public int getId_factura() {
        return id_factura;
    }

    public void setId_factura(int id_factura) {
        this.id_factura = id_factura;
    }

    public String getFact_valortotal() {
        return fact_valortotal;
    }

    public void setFact_valortotal(String fact_valortotal) {
        this.fact_valortotal = fact_valortotal;
    }

    public String getFact_fecha() {
        return fact_fecha;
    }

    public void setFact_fecha(String fact_fecha) {
        this.fact_fecha = fact_fecha;
    }

    public int getId_empleado() {
        return id_empleado;
    }

    public void setId_empleado(int id_empleado) {
        this.id_empleado = id_empleado;
    }

    public int getId_finca() {
        return id_finca;
    }

    public void setId_finca(int id_finca) {
        this.id_finca = id_finca;
    }

    @Override
    public String toString() {
        return "factura{" +
                "id_factura=" + id_factura +
                ", fact_valortotal='" + fact_valortotal + '\'' +
                ", fact_fecha='" + fact_fecha + '\'' +
                ", id_empleado=" + id_empleado +
                ", id_finca=" + id_finca +
                '}';
    }
}
