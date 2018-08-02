package com.ddannielvega.neofinca;


import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ddannielvega.neofinca.model.usuario;

import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;

public class LoginActivity extends AppCompatActivity {

    public String err_ms;
    public Realm realm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);



    //check log
        SharedPreferences sharedpreferences;
        sharedpreferences = getSharedPreferences("MyPrefs", LoginActivity.MODE_PRIVATE);

        sharedpreferences.getAll();
        Map<String,?> result = sharedpreferences.getAll();
        if (result.size()!=0){
            //Start home activity
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            //get correo from shared pref
            for (Map.Entry<String, ?> entry : result.entrySet()) {
                intent.putExtra("us_correo",entry.getKey());
            }
            startActivity(intent);
            LoginActivity.this.finish();
        }




        Button loginButton =  findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView error_message = findViewById(R.id.error_message);
                error_message.setText("");
                if (validateUserData()){

                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    //save email
                    EditText emailView = findViewById(R.id.user_email);
                    String email = emailView.getText().toString();
                    SharedPreferences sharedpreferences;
                    sharedpreferences = getSharedPreferences("MyPrefs", LoginActivity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(email, email);
                    sharedpreferences.getAll();
                    editor.apply();



                    startActivity(intent);
                    LoginActivity.this.finish();

                }else{
                    error_message.setText(err_ms);
                }


            }
        });

        Button registerButton =  findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);

            }
        });
    }

    public boolean validateUserData(){
        EditText email = findViewById(R.id.user_email);
        EditText pass = findViewById(R.id.user_pass);
        String user_email_s = email.getText().toString();
        String user_pass_s = pass.getText().toString();

        if (user_email_s.length()<4 && user_pass_s.length()<4) {
            err_ms= "Los campos deben ser minimo de tamaño 4";
            return false;
        }else {
            //CHECK THE NAME AND PASSWORD IN THE DATABASE
            realm = Realm.getDefaultInstance();
            realm.beginTransaction();
                final RealmResults<usuario> results = realm.where(usuario.class)
                        .equalTo("us_correo", user_email_s)
                        .and()
                        .equalTo("us_contraseña", user_pass_s)
                        .findAll();
            realm.commitTransaction();

            StringBuilder stringResults= new StringBuilder();
            for (usuario result:results){
                try {
                    stringResults.append("\n").append(result.toString());

                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }
            if (stringResults.length() < 1){
                err_ms= "Error en el correo  o contraseña ";
                return false;
            }else return true;
        }
    }
}

