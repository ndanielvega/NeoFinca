package com.ddannielvega.neofinca;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ddannielvega.neofinca.model.usuario;


import io.realm.Realm;
import io.realm.RealmResults;

public class RegisterActivity extends AppCompatActivity {

    public String error_ms;
    public Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button registerButton =  findViewById(R.id.register_page_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView error_message = findViewById(R.id.error_message_register);
                error_message.setText("");
                if (validateUserData()) {
                    registerUser(); //REGISTRA AL USUARIO

                    Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);

                    //save email
                    EditText emailView = findViewById(R.id.user_email_register);
                    String email = emailView.getText().toString();
                    SharedPreferences sharedpreferences;
                    sharedpreferences = getSharedPreferences("MyPrefs", LoginActivity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(email, email);
                    sharedpreferences.getAll();
                    editor.commit();

                    startActivity(intent);
                    RegisterActivity.this.finish();

                }else{
                    error_message.setText(error_ms);
                }

            }
        });
    }
    public boolean validateUserData(){

        EditText name = findViewById(R.id.user_name_register);
        EditText email = findViewById(R.id.user_email_register);
        EditText tel= findViewById(R.id.user_tel_register);
        EditText pass1 = findViewById(R.id.user_pass_1);
        EditText pass2 = findViewById(R.id.user_pass_2);

        String user_pass_1 = pass1.getText().toString();
        String user_pass_2 = pass2.getText().toString();

        //true if data length >0
        if (name.getText().toString().length()<4 || user_pass_1.length()<4 || user_pass_2.length()<4 || email.getText().toString().length()<4 || tel.getText().toString().length()<4 ) {
            error_ms= "Todos los campos son obligatorios y de minimo 4 caracteres";
            return false;
        }else{
            //validate email
            if (!(isValidEmail(email.getText().toString()))){
                error_ms= "Ingresa un correo electrónico válido";
                return false;
            }
            else{
                if (emailExist(email.getText().toString())){
                    error_ms= "El correo ingresado ya esxiste";
                    return false;
                }
                else{
                    //validate phone number
                    if ( tel.getText().toString().length() != 10){
                        error_ms= "Número telefónico invalido (Tamaño 10)";
                        return false;
                    }else{
                        //compare both pass
                        if ( !(user_pass_1.equals( user_pass_2 )) ){
                            error_ms= "Las contraseñas no coinciden";
                            return false;
                        }else return true;
                    }
                }
            }
        }
    }

    private boolean emailExist(String email) {
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<usuario> results = realm.where(usuario.class).equalTo("us_correo", email).findAll();
        realm.commitTransaction();

        return results.size() > 0;
    }

    public  static boolean isValidEmail(CharSequence target) {
        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public void registerUser(){

        EditText name = findViewById(R.id.user_name_register);
        EditText pass = findViewById(R.id.user_pass_1);
        EditText tel = findViewById(R.id.user_tel_register);
        EditText email = findViewById(R.id.user_email_register);


        final usuario user = new usuario();
        user.setUs_nombre_usuario(  name.getText().toString()  );
        user.setUs_contraseña(  pass.getText().toString()  );
        user.setUs_correo( email.getText().toString() );
        user.setUs_telefonc( tel.getText().toString() );

        realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                //primary key
                Number maxId = realm.where(usuario.class).max("id_usuario");
                int nextId = (maxId == null) ? 1 : maxId.intValue() + 1;
                user.setId_usuario(nextId);

                realm.copyToRealmOrUpdate(user);
            }
        });
    }
}
