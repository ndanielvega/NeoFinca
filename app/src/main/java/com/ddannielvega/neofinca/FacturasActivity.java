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
import com.ddannielvega.neofinca.model.factura;

import io.realm.Realm;
import io.realm.RealmResults;

public class FacturasActivity extends AppCompatActivity {

    //New attributes
    public String id_finca;
    public String emp_name;
    public String emp_id;
    public String emp_val;
    public String user_email;
    public static final String finca_mess = "finca_mess";
    public static final String us_correo = "us_correo";
    public Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facturas);


        //get user email from other activities
        Intent intent = getIntent();
        user_email= intent.getStringExtra("us_correo_mess_fact");
        id_finca = intent.getStringExtra("asig_id_fin");
        emp_name = intent.getStringExtra("empleado_name_mess");
        emp_val = intent.getStringExtra("empleado_val_mess");
        emp_id = intent.getStringExtra("empleado_id_mess");

        //put initial data
            TextView emp_name_tv = findViewById(R.id.emp_name);
            String s1 = "Empleado responsable: " + emp_name ;
            emp_name_tv.setText(s1);

            TextView emp_val_tv = findViewById(R.id.emp_val);
            String s2 = "Valor por jornal trabajado: " + emp_val + " COP" ;
            emp_val_tv.setText(s2);

            //set date
            DatePicker datePicker = new DatePicker(FacturasActivity.this);
            String year = String.valueOf(datePicker.getYear());
            String month = String.valueOf(datePicker.getMonth()+1);
            String day = String.valueOf(datePicker.getDayOfMonth());
            final String fullDate = day + "/" + month + "/" + year;

            TextView fact_date = findViewById(R.id.fact_date);
            String s3 = "Fecha de factura: " + fullDate ;
            fact_date.setText(s3);


            //get all asignaciones
            realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            final RealmResults<asignacion> asginaciones_all= realm.where(asignacion.class)
                    .equalTo("id_finca", Integer.parseInt(id_finca))
                    .and()
                    .notEqualTo("asig_estado","2")
                    .and()
                    .notEqualTo("asig_estado","0")
                    .and()
                    .equalTo("deleted",false)
                    .and()
                    .notEqualTo("asig_totalxactividad","0")
                    .findAll();
            realm.commitTransaction();
            int sum=0;
            for (asignacion result:asginaciones_all){
                try {
                    sum = sum + Integer.parseInt(result.getAsig_totalxactividad());

                    //get the actividad name
                    realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    actividad actividad_this = realm.where(actividad.class)
                            .and()
                            .equalTo("id_actividad",result.getId_actividad())
                            .findFirst();
                    realm.commitTransaction();

                    //layout container
                    final LinearLayout newLayout = new LinearLayout(FacturasActivity.this);
                    newLayout.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));
                    newLayout.setOrientation(LinearLayout.HORIZONTAL);
                    newLayout.setGravity(Gravity.FILL);
                    newLayout.setPadding(0,5,0,20);

                    //activity name
                    TextView act_name_tv= new TextView(FacturasActivity.this);
                    act_name_tv.setLayoutParams(new LinearLayout.LayoutParams(
                            750,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));
                    act_name_tv.setText(actividad_this.getAct_descripcion());
                    act_name_tv.setTextColor(Color.BLACK);
                    act_name_tv.setTextSize(17);
                    newLayout.addView(act_name_tv);
                    //activity val
                    TextView act_val_tv= new TextView(FacturasActivity.this);
                    act_val_tv.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));
                    act_val_tv.setPadding(10,0,0,0);
                    act_val_tv.setText(result.getAsig_totalxactividad());
                    act_val_tv.setTextColor(Color.BLACK);
                    act_val_tv.setTextSize(17);
                    newLayout.addView(act_val_tv);

                    TextView act_jornales_tv= new TextView(FacturasActivity.this);
                    act_jornales_tv.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));
                    act_jornales_tv.setText(String.valueOf(result.getAsig_cant_jornales())+ " jornales");
                    act_jornales_tv.setPadding(30,0,0,30);

                    LinearLayout linearLayout = findViewById(R.id.data_lo);
                    linearLayout.addView(newLayout);
                    linearLayout.addView(act_jornales_tv);

                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }

            TextView fact_total = findViewById(R.id.fact_total);
            final String fact_t = String.valueOf(sum);
            String s4 = "Total de factura: " + String.valueOf(sum) + " COP";
            fact_total.setText(s4);

        //buttons listeners
            //cancel
                Button cancel = findViewById(R.id.cancel_bt);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(FacturasActivity.this, AsignacionesActivity.class);
                        intent.putExtra(us_correo,user_email);
                        intent.putExtra("finca_mess",id_finca);
                        startActivity(intent);
                        FacturasActivity.this.finish();
                    }
                });

            //acept
                Button acept = findViewById(R.id.acept_bt);
                if (sum==0){
                    //desactiva crear factura
                    acept.setEnabled(false);
                    acept.setBackgroundColor(getResources().getColor(R.color.colorGray));
                    acept.setTextColor(Color.GRAY);
                    Toast.makeText(FacturasActivity.this, "No hay actividades asignadas", Toast.LENGTH_SHORT).show();
                }
                acept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //change state for every asgination
                        realm = Realm.getDefaultInstance();
                        realm.beginTransaction();

                            //create factura
                            factura factura = new factura();
                            Number maxId = realm.where(factura.class).max("id_factura");
                            int nextId = (maxId == null) ? 1 : maxId.intValue() + 1;
                            factura.setId_factura(nextId);
                            factura.setFact_valortotal(fact_t);
                            factura.setFact_fecha(fullDate);
                            factura.setId_finca(Integer.parseInt(id_finca));
                            factura.setId_empleado(Integer.parseInt(emp_id));

                            //change state and add fact id to asignation
                            final RealmResults<asignacion> asginaciones_all= realm.where(asignacion.class)
                                    .equalTo("id_finca", Integer.parseInt(id_finca))
                                    .and()
                                    .notEqualTo("asig_estado","2")
                                    .and()
                                    .notEqualTo("asig_estado","0")
                                    .findAll();

                            for (asignacion result:asginaciones_all){
                                try {
                                    result.setId_factura(nextId);
                                    result.setAsig_estado("2");
                                    realm.copyToRealmOrUpdate(factura);
                                    realm.copyToRealmOrUpdate(result);
                                }catch (NullPointerException e){
                                    e.printStackTrace();
                                }
                            }
                        realm.commitTransaction();

                        Intent intent = new Intent(FacturasActivity.this, AsignacionesActivity.class);
                        intent.putExtra(us_correo,user_email);
                        intent.putExtra("finca_mess",id_finca);
                        startActivity(intent);
                        FacturasActivity.this.finish();
                    }
                });

    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(FacturasActivity.this, AsignacionesActivity.class);
        intent.putExtra(us_correo,user_email);
        intent.putExtra(finca_mess,id_finca);
        startActivity(intent);

        FacturasActivity.this.finish();
    }
}
