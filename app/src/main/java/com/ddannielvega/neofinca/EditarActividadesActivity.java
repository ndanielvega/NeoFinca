package com.ddannielvega.neofinca;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ddannielvega.neofinca.model.actividad;
import com.ddannielvega.neofinca.model.usuario;

import io.realm.Realm;
import io.realm.RealmResults;

public class EditarActividadesActivity extends AppCompatActivity {

    public String id_element;
    public String user_email;
    public Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_actividades);

        //get user email from other activities
        Intent intent = getIntent();
        id_element = intent.getStringExtra("element_id");
        user_email = intent.getStringExtra("us_correo");


        //put data in inputs
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<actividad> results  = realm.where(actividad.class)
                .equalTo("id_actividad", Integer.parseInt(id_element)).findAll();
        realm.commitTransaction();
        actividad actividad = results.get(0);

        EditText description = findViewById(R.id.act_desc_edit);
        description.setText(actividad.getAct_descripcion());

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

        final EditText description = findViewById(R.id.act_desc_edit);
        final actividad actividad = new actividad();


        realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                usuario resultso = realm.where(usuario.class)
                        .equalTo("us_correo",user_email)
                        .findFirst();

                actividad.setAct_descripcion(description.getText().toString());
                actividad.setId_usuario(resultso.getId_usuario());
                actividad.setId_actividad(Integer.parseInt(id_element));

                realm.copyToRealmOrUpdate(actividad);
            }
        });


        //start activity
        Intent intent = new Intent(EditarActividadesActivity.this, ActividadesActivity.class);
        intent.putExtra("us_correo",user_email);
        EditarActividadesActivity.this.finish();
        startActivity(intent);

        Toast.makeText(EditarActividadesActivity.this, "Se guardaron los cambios", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EditarActividadesActivity.this, ActividadesActivity.class);
        intent.putExtra("us_correo",user_email);
        EditarActividadesActivity.this.finish();
        startActivity(intent);
    }
}
