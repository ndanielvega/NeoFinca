package com.ddannielvega.neofinca;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ddannielvega.neofinca.model.usuario;

import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;

public class PerfilActivity extends AppCompatActivity {

    //menu_nav
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;

    public String user_email;
    public Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        //get user email from shared prefs
        Map<String,?> result_sp = getSharedPreferences("MyPrefs", LoginActivity.MODE_PRIVATE).getAll();
        for (Map.Entry<String, ?> entry : result_sp.entrySet()) {
            user_email = entry.getKey();
        }
        //menu nav
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mActionBarDrawerToggle= new ActionBarDrawerToggle(this,mDrawerLayout,R.string.menu_nav_open,R.string.menu_nav_close);
        mActionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int menu_id = item.getItemId();

                if (menu_id == R.id.inicio){
                    Intent intent = new Intent(PerfilActivity.this, HomeActivity.class);
                    startActivity(intent);
                    PerfilActivity.this.finish();
                }
                else if(menu_id == R.id.edit_perfil){
                    mDrawerLayout.closeDrawers();
                }
                else if(menu_id == R.id.edit_fincas){
                    Intent intent = new Intent(PerfilActivity.this, FincasActivity.class);
                    startActivity(intent);
                    PerfilActivity.this.finish();
                }
                else if(menu_id == R.id.edit_actividades){
                    Intent intent = new Intent(PerfilActivity.this, ActividadesActivity.class);
                    startActivity(intent);
                    PerfilActivity.this.finish();
                }
                else if(menu_id == R.id.edit_empleados){
                    Intent intent = new Intent(PerfilActivity.this, EmpleadosActivity.class);
                    startActivity(intent);
                    PerfilActivity.this.finish();
                }
                else if(menu_id == R.id.historial_fact){
                    Intent intent = new Intent(PerfilActivity.this, FiltroHistorialActivity.class);
                    startActivity(intent);
                    PerfilActivity.this.finish();
                }
                else if(menu_id == R.id.backup){
                    Intent intent = new Intent(PerfilActivity.this, BackupActivity.class);
                    startActivity(intent);
                    PerfilActivity.this.finish();
                }
                else if(menu_id == R.id.contacto){
                    Intent intent = new Intent(PerfilActivity.this, ContactoActivity.class);
                    startActivity(intent);
                    PerfilActivity.this.finish();
                }
                else if(menu_id == R.id.logout){
                    //close session
                    SharedPreferences.Editor editor = getSharedPreferences("MyPrefs",0).edit();
                    editor.clear(); //clear all stored data
                    editor.commit();
                    Intent intent = new Intent(PerfilActivity.this, LoginActivity.class);
                    startActivity(intent);
                    PerfilActivity.this.finish();
                }
                return false;
            }
        });

        //put data in inputs
            realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            usuario results = realm.where(usuario.class)
                    .equalTo("us_correo", user_email)
                    .findFirst();
            realm.commitTransaction();


            EditText name = findViewById(R.id.user_name_edit);
            name.setText(results.getUs_nombre_usuario());

            EditText tel = findViewById(R.id.user_tel_edit);
            tel.setText(results.getUs_telefonc());

            EditText emailet = findViewById(R.id.user_email_edit);
            emailet.setText(results.getUs_correo());


        //update info
            Button update_bt = findViewById(R.id.register_page_button);
            update_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //views
                    final EditText name = findViewById(R.id.user_name_edit);
                    final EditText tel = findViewById(R.id.user_tel_edit);
                    final EditText emailet = findViewById(R.id.user_email_edit);
                    EditText pass_old = findViewById(R.id.user_pass_old);
                    final EditText pass_1 = findViewById(R.id.user_pass_1);
                    EditText pass_2 = findViewById(R.id.user_pass_2);
                    TextView tv = findViewById(R.id.error_message); //error

                    //if there is not password changes update the rest of data
                        if (pass_old.getText().toString().length()<1 || pass_2.getText().toString().length()<1 || pass_1.getText().toString().length()<1){
                            //validate length data (email,name,tel)
                            if (name.getText().toString().length()<4 || tel.getText().toString().length()<10 || emailet.getText().toString().length()<4){
                                tv.setText("Los campos deben ser minimo de tamaño 4 y telefono de 10");
                            }else{
                                //validate email
                                if (!(isValidEmail(emailet.getText().toString()))){
                                    tv.setText("Ingresa un correo electronico valido");
                                }else{
                                    //update name, email and tel
                                    realm = Realm.getDefaultInstance();
                                    realm.beginTransaction();
                                    final RealmResults<usuario> results = realm.where(usuario.class).equalTo("us_correo", user_email).findAll();
                                    realm.commitTransaction();


                                    realm = Realm.getDefaultInstance();
                                    realm.executeTransaction(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            usuario user = results.get(0);

                                            user.setUs_nombre_usuario(  name.getText().toString()  );
                                            user.setUs_correo( emailet.getText().toString() );
                                            user.setUs_telefonc( tel.getText().toString() );

                                            realm.copyToRealmOrUpdate(user);
                                        }
                                    });
                                    Toast.makeText(PerfilActivity.this, "Se guardaron los cambios", Toast.LENGTH_SHORT).show();
                                    tv.setText("");
                                    //close keyboard
                                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                                }
                            }

                        }
                    //if there is password changes update all data
                        else{
                            //validate passwords
                                //old password
                                realm = Realm.getDefaultInstance();
                                realm.beginTransaction();
                                final RealmResults<usuario> results = realm.where(usuario.class).equalTo("us_correo", user_email).findAll();
                                realm.commitTransaction();
                                usuario user = results.get(0);
                                //verify old password
                                if ( !((pass_old.getText().toString()).equals( user.getUs_contraseña() )) ){
                                    tv.setText("La contraseña antigua no es correcta");
                                }else{
                                    //verify length of passwords
                                    if (pass_old.getText().toString().length()<4 || pass_2.getText().toString().length()<4 || pass_1.getText().toString().length()<4){
                                        tv.setText("Tamaño minimo 4");
                                    }else{
                                        //verify new paswords
                                        if ( !((pass_1.getText().toString()).equals( pass_2.getText().toString() )) ){
                                            tv.setText("Lass contraseñas nuevas no coinciden");
                                        }else {

                                            //update name, email, tel and pass
                                            realm = Realm.getDefaultInstance();
                                            realm.executeTransaction(new Realm.Transaction() {
                                                @Override
                                                public void execute(Realm realm) {
                                                    usuario user = results.get(0);

                                                    user.setUs_nombre_usuario(  name.getText().toString()  );
                                                    user.setUs_correo( emailet.getText().toString() );
                                                    user.setUs_telefonc( tel.getText().toString() );
                                                    user.setUs_contraseña(pass_1.getText().toString());

                                                    realm.copyToRealmOrUpdate(user);
                                                }
                                            });
                                            Toast.makeText(PerfilActivity.this, "Se guardaron los cambios", Toast.LENGTH_SHORT).show();
                                            tv.setText("");
                                            pass_old.setText("");
                                            pass_1.setText("");
                                            pass_2.setText("");
                                        }
                                    }
                                }
                        }
                }
            });
            realm.close();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //close keyboard
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        //toggle side menu
        return  mActionBarDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(PerfilActivity.this, HomeActivity.class);
        startActivity(intent);
        PerfilActivity.this.finish();
    }

    public  static boolean isValidEmail(CharSequence target) {
        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
