package com.ddannielvega.neofinca;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ddannielvega.neofinca.model.empleado;
import com.ddannielvega.neofinca.model.finca;
import com.ddannielvega.neofinca.model.finxemp;
import com.ddannielvega.neofinca.model.usuario;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class EditarFincasActivity extends AppCompatActivity {

    public String id_element;
    public String user_email;
    public finxemp finxempthis;
    Realm realm;

    Spinner spinner_empleados;
    public List<String> emp_nombres;
    public List<String> emp_ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_fincas);

        //get user email from other activities
        Intent intent = getIntent();
        id_element = intent.getStringExtra("element_id");
        user_email = intent.getStringExtra("us_correo");


        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        //finca
            finca results_finca  = realm.where(finca.class)
                    .equalTo("id_finca", Integer.parseInt(id_element))
                    .findFirst();
        //search the emp asigned to the fin
            //search in finxemp
            finxemp resultfxe = realm.where(finxemp.class)
                    .equalTo("id_finca", Integer.parseInt(id_element))
                    .and()
                    .equalTo("finxemp_estado", "1") //que esté activo
                    .findFirst();
            //search in emp
            empleado result_name_emp = realm.where(empleado.class)
                    .equalTo("id_empleado", resultfxe.getId_empleado())
                    .findFirst();
        //spinner
            RealmResults<empleado> resultsemp = realm.where(empleado.class).findAll();
        realm.commitTransaction();

        finxempthis = resultfxe;

        EditText finca_name = findViewById(R.id.finca_name);
        finca_name.setText(results_finca.getFin_nombre());
        EditText finca_dir = findViewById(R.id.finca_dir);
        finca_dir.setText(results_finca.getFin_ubicacion());



        //carga datos de spinner
            spinner_empleados = findViewById(R.id.spiner_empleados);
            //get data
            emp_nombres = new ArrayList<String>();
            emp_ids = new ArrayList<String>();
            if (resultsemp.size()>0){
                for (empleado result:resultsemp){
                    try {
                        emp_nombres.add(result.getEmp_nombre());
                        emp_ids.add(String.valueOf(result.getId_empleado()));
                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }
                }
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,emp_nombres);
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            spinner_empleados.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            spinner_empleados.setSelection(adapter.getPosition(result_name_emp.getEmp_nombre()));

        //update data
        Button update_bt = findViewById(R.id.save_changes);
        update_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
            }
        });

        //cancel
        Button cancel_bt = findViewById(R.id.cancel_changes);
        cancel_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void updateData(){
        final EditText fin_name = findViewById(R.id.finca_name);
        final EditText fin_dir = findViewById(R.id.finca_dir);
        final finca finca = new finca();
        final finxemp finxempnew = new finxemp();
        final finxemp finxempAct = new finxemp();

        Spinner spinner_empleado = findViewById(R.id.spiner_empleados);
        //get the id of the selected empleado
        String selected = spinner_empleado.getSelectedItem().toString();
        String id_emp_val = emp_ids.get(emp_nombres.indexOf(selected));

        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
            //fincaData
                usuario resultsusuario = realm.where(usuario.class)
                        .equalTo("us_correo", user_email )
                        .findFirst();

            //create fincaempnew
            //primary key finxemp
                Number maxId2 = realm.where(finxemp.class).max("id_finxemp");
                int nextId2 = (maxId2 == null) ? 1 : maxId2.intValue() + 1;

                finxempnew.setId_finxemp(nextId2);
                finxempnew.setId_finca(Integer.parseInt(id_element));
                finxempnew.setId_empleado(Integer.valueOf(id_emp_val));
                finxempnew.setFinxemp_estado("1");

                finca.setId_finca(Integer.parseInt(id_element));
                finca.setFin_nombre(fin_name.getText().toString());
                finca.setFin_ubicacion(fin_dir.getText().toString());
                finca.setId_usuario(resultsusuario.getId_usuario());

                //update the finxempthis to 0 = inactive
                finxempAct.setId_finxemp(finxempthis.getId_finxemp());
                finxempAct.setId_empleado(finxempthis.getId_empleado());
                finxempAct.setId_finca(finxempthis.getId_finca());
                finxempAct.setFinxemp_estado("0");

                realm.copyToRealmOrUpdate(finxempnew);
                realm.copyToRealmOrUpdate(finxempAct);
                realm.copyToRealmOrUpdate(finca);

        realm.commitTransaction();



        //start activity
        Intent intent = new Intent(EditarFincasActivity.this,FincasActivity.class);
        intent.putExtra("us_correo",user_email);
        EditarFincasActivity.this.finish();
        startActivity(intent);

        EditarFincasActivity.this.finish();
        Toast.makeText(EditarFincasActivity.this, "Se guardaron los cambios", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EditarFincasActivity.this,FincasActivity.class);
        intent.putExtra("us_correo",user_email);
        EditarFincasActivity.this.finish();
        startActivity(intent);
    }
}
