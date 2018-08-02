package com.ddannielvega.neofinca;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ddannielvega.neofinca.model.empleado;
import com.ddannielvega.neofinca.model.usuario;

import io.realm.Realm;
import io.realm.RealmResults;

public class EditarEmpleadosActivity extends AppCompatActivity {

    public String id_element;
    public String user_email;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_empleados);

        //get user email from other activities
        Intent intent = getIntent();
        id_element = intent.getStringExtra("element_id");
        user_email = intent.getStringExtra("us_correo");


        //put data in inputs
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<empleado> results  = realm.where(empleado.class).equalTo("id_empleado", Integer.parseInt(id_element)).findAll();
        realm.commitTransaction();
        empleado empleado= results.get(0);


        EditText emp_name = findViewById(R.id.emp_name_edit);
        EditText emp_doc = findViewById(R.id.emp_doc_edit);
        EditText emp_tel = findViewById(R.id.emp_tel_edit);
        EditText emp_val = findViewById(R.id.emp_val_edit);

        emp_name.setText(empleado.getEmp_nombre());
        emp_doc.setText(empleado.getEmp_documento());
        emp_tel.setText(empleado.getEmp_telefono());
        emp_val.setText(String.valueOf(empleado.getEmp_valorxjornal()));


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


        final EditText emp_name = findViewById(R.id.emp_name_edit);
        final EditText emp_doc = findViewById(R.id.emp_doc_edit);
        final EditText emp_tel = findViewById(R.id.emp_tel_edit);
        final EditText emp_val = findViewById(R.id.emp_val_edit);
        final empleado empleado = new empleado();

        realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                usuario resultso = realm.where(usuario.class)
                        .equalTo("us_correo",user_email)
                        .findFirst();

                empleado.setId_empleado(Integer.parseInt(id_element));
                empleado.setEmp_nombre(emp_name.getText().toString());
                empleado.setEmp_documento(emp_doc.getText().toString());
                empleado.setEmp_telefono(emp_tel.getText().toString());
                empleado.setId_usuario(resultso.getId_usuario());
                empleado.setEmp_valorxjornal(Integer.parseInt(emp_val.getText().toString()));

                realm.copyToRealmOrUpdate(empleado);
            }
        });


        //start activity
        Intent intent = new Intent(EditarEmpleadosActivity.this,EmpleadosActivity.class);
        intent.putExtra("us_correo",user_email);
        EditarEmpleadosActivity.this.finish();
        startActivity(intent);

        EditarEmpleadosActivity.this.finish();
        Toast.makeText(EditarEmpleadosActivity.this, "Se guardaron los cambios", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EditarEmpleadosActivity.this,EmpleadosActivity.class);
        intent.putExtra("us_correo",user_email);
        EditarEmpleadosActivity.this.finish();
        startActivity(intent);
    }
}
