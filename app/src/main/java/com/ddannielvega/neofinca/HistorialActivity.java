package com.ddannielvega.neofinca;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ddannielvega.neofinca.model.actividad;
import com.ddannielvega.neofinca.model.asignacion;
import com.ddannielvega.neofinca.model.empleado;
import com.ddannielvega.neofinca.model.factura;
import com.ddannielvega.neofinca.model.finca;
import com.ddannielvega.neofinca.model.finxemp;

import org.w3c.dom.Text;

import io.realm.Realm;
import io.realm.RealmResults;

public class HistorialActivity extends AppCompatActivity {

    public String selected_option;
    public String selected_filter;
    public Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);


        //get data from other activities
            Intent intent = getIntent();
            selected_option = intent.getStringExtra("option");
            selected_filter = intent.getStringExtra("filter");

            if (selected_option.equals("all")){
                searchByAll();
            }else{
                if (selected_option.equals("empleado")){
                    searchByEmpleado(selected_filter);
                }else{
                    if (selected_option.equals("finca")){
                        searchByFinca(selected_filter);
                    }else{
                        if (selected_option.equals("fecha")){
                            searchByFecha(selected_filter);
                        }else{
                            Toast.makeText(this, "No hay filtro", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

    }

    public void searchByAll(){
        //get all data
        //get user field
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
            final RealmResults<factura> resultso = realm.where(factura.class).findAll();
        realm.commitTransaction();
        realm.close();
        printResults(resultso);
    }
    public void searchByEmpleado(String emp_id){
        //get all data
        //get user field
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
            final RealmResults<factura> resultso = realm.where(factura.class).equalTo("id_empleado", Integer.parseInt(emp_id)).findAll();
        realm.commitTransaction();
        realm.close();
        printResults(resultso);
    }
    public void searchByFinca(String fin_id){
        //get all data
        //get user field
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
            final RealmResults<factura> resultso = realm.where(factura.class).equalTo("id_finca", Integer.parseInt(fin_id)).findAll();
        realm.commitTransaction();
        realm.close();
        printResults(resultso);
    }
    public void searchByFecha(String fact_date){
        //get all data
        //get user field
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
         final RealmResults<factura> resultso = realm.where(factura.class).equalTo("fact_fecha", fact_date).findAll();
        realm.commitTransaction();
        realm.close();
        printResults(resultso);
    }

    public void printResults(RealmResults<factura> resultso){

        int resultCounter =0;
        LinearLayout linearLayout = findViewById(R.id.test_lo);
        for (factura result:resultso){
            try {
                resultCounter = resultCounter+1;
                //empleado name
                    realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                        empleado empleado= realm.where(empleado.class)
                                .and()
                                .equalTo("id_empleado",result.getId_empleado())
                                .findFirst();
                        finca finca  = realm.where(finca.class)
                                .and()
                                .equalTo("id_finca",result.getId_finca())
                                .findFirst();
                    realm.commitTransaction();
                    realm.close();
                    TextView emp_name = new TextView(HistorialActivity.this);
                    emp_name.setLayoutParams( new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));
                    emp_name.setPadding(20,0,20,30);
                    emp_name.setText("Empleado: "+empleado.getEmp_nombre());
                    emp_name.setBackgroundResource(R.color.colorGray);
                    emp_name.setTextColor(Color.BLACK);
                //fact date
                    TextView fact_dt = new TextView(HistorialActivity.this);
                    fact_dt.setLayoutParams( new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));
                    fact_dt.setPadding(20,0,20,0);
                    fact_dt.setText("Fecha de facturacion: "+result.getFact_fecha());
                    fact_dt.setBackgroundResource(R.color.colorGray);
                    fact_dt.setTextColor(Color.BLACK);
                //fact val
                    TextView fact_val= new TextView(HistorialActivity.this);
                    fact_val.setLayoutParams( new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        ));
                    fact_val.setPadding(20,30,20,0);
                    fact_val.setText("Total cancelado:  "+result.getFact_valortotal());
                    fact_val.setBackgroundResource(R.color.colorGray);
                    fact_val.setTextColor(Color.BLACK);
                //title
                    TextView title = new TextView(HistorialActivity.this);
                    title.setLayoutParams( new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));
                    title.setTextSize(16);
                    title.setPadding(5,5,5,5);
                    title.setGravity(Gravity.CENTER);
                    title.setText("Registro "+String.valueOf(result.getId_factura()));
                    title.setBackgroundResource(R.color.colorGray);
                    title.setTextColor(Color.BLACK);
                //finca name
                    TextView finca_tv = new TextView(HistorialActivity.this);
                    finca_tv.setLayoutParams( new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));
                    finca_tv.setTextSize(18);
                    finca_tv.setPadding(5,10,5,10);
                    finca_tv.setGravity(Gravity.CENTER);
                    finca_tv.setText(finca.getFin_nombre());
                    finca_tv.setBackgroundResource(R.color.colorGray);
                    finca_tv.setTextColor(Color.BLACK);


                //layout container
                    final LinearLayout newLayout = new LinearLayout(HistorialActivity.this);
                    newLayout.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));
                    newLayout.setOrientation(LinearLayout.VERTICAL);
                    newLayout.setGravity(Gravity.FILL);
                    newLayout.setPadding(0,5,0,20);


                newLayout.addView(title);
                newLayout.addView(finca_tv);


                //asgincaciones
                    realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    final RealmResults<asignacion> asginaciones_all= realm.where(asignacion.class)
                            .equalTo("id_factura", result.getId_factura())
                            .findAll();
                    realm.commitTransaction();
                    realm.close();
                    for (asignacion resultado:asginaciones_all){
                        try {
                            //layout container
                                LinearLayout asig_layout = new LinearLayout(HistorialActivity.this);
                                newLayout.setLayoutParams(new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                ));
                                asig_layout.setOrientation(LinearLayout.HORIZONTAL);
                                asig_layout.setGravity(Gravity.FILL);
                                asig_layout.setPadding(30,30,30,20);


                            //get the actividad name
                                realm = Realm.getDefaultInstance();
                                realm.beginTransaction();
                                    actividad actividad_this = realm.where(actividad.class)
                                            .and()
                                            .equalTo("id_actividad",resultado.getId_actividad())
                                            .findFirst();
                                realm.commitTransaction();
                                realm.close();

                            //activity name
                                TextView act_name_tv= new TextView(HistorialActivity.this);
                                act_name_tv.setLayoutParams(new LinearLayout.LayoutParams(
                                        750,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                ));
                                act_name_tv.setText(actividad_this.getAct_descripcion());
                                act_name_tv.setTextColor(Color.BLACK);
                                act_name_tv.setTextSize(17);
                                asig_layout.addView(act_name_tv);
                            //activity val
                                TextView act_val_tv= new TextView(HistorialActivity.this);
                                act_val_tv.setLayoutParams(new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                ));
                                act_val_tv.setPadding(30,0,0,0);
                                act_val_tv.setText(resultado.getAsig_totalxactividad());
                                act_val_tv.setTextColor(Color.BLACK);
                                act_val_tv.setTextSize(18);
                                asig_layout.addView(act_val_tv);
                            //jornales
                                TextView act_jornales_tv= new TextView(HistorialActivity.this);
                                act_jornales_tv.setLayoutParams(new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                ));
                                act_jornales_tv.setText(String.valueOf(resultado.getAsig_cant_jornales())+ " jornales");
                                act_jornales_tv.setPadding(40,0,0,30);

                            //add views
                            newLayout.addView(asig_layout);
                            newLayout.addView(act_jornales_tv);

                        }catch (NullPointerException e){
                            e.printStackTrace();
                        }
                    }

                //add views
                newLayout.addView(fact_val);
                newLayout.addView(fact_dt);
                newLayout.addView(emp_name);
                linearLayout.addView(newLayout);

            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }

        TextView message = findViewById(R.id.message);
        if (resultCounter < 1){
            String err_ms= "NO HAY FACTURAS";
            message.setText(err_ms);
        }

    }


    @Override
    public void onBackPressed() {
        HistorialActivity.this.finish();
    }
}
