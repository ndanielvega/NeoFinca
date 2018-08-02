package com.ddannielvega.neofinca;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.ddannielvega.neofinca.model.finca;
import com.ddannielvega.neofinca.model.usuario;

import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;

public class HomeActivity extends AppCompatActivity {


    public static final String us_correo = "us_correo"; //send data to asignaciones
    public static final String finca_mess = "finca_mess"; //send data to asignaciones
    public String user_email;
    private Realm realm;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Get user email from shared prefs
            Map<String,?> result_sp = getSharedPreferences("MyPrefs", LoginActivity.MODE_PRIVATE).getAll();
            for (Map.Entry<String, ?> entry : result_sp.entrySet()) {
                user_email = entry.getKey();
            }

        initializeMenuBar();



        //Fincas data
            realm = Realm.getDefaultInstance();
            realm.beginTransaction();
                usuario userResult = realm.where(usuario.class)
                        .equalTo("us_correo", user_email)
                        .findFirst();
                RealmResults<finca> finResult = realm.where(finca.class)
                        .equalTo("id_usuario", userResult.getId_usuario())
                        .and()
                        .equalTo("deleted",false)
                        .findAll();
            realm.commitTransaction();




        int resultCounter =0;
        LinearLayout linearLayout = findViewById(R.id.test_lo);
        for (finca result:finResult){
            try {
                resultCounter = resultCounter+1;
                Button finButton = new Button(HomeActivity.this);
                finButton.setLayoutParams( new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                finButton.setMinHeight(40);
                finButton.setTextSize(19);
                finButton.setGravity(0);
                finButton.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_keyboard_arrow_right_black_24dp,0);
                finButton.setBackgroundColor(getResources().getColor(R.color.colorGray));
                finButton.setAllCaps(false);
                finButton.setPadding(40,50,40,50);
                finButton.setText(result.getFin_nombre());
                linearLayout.addView(finButton);

                final String fin_ms = String.valueOf(result.getId_finca());
                finButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(HomeActivity.this, AsignacionesActivity.class);
                        intent.putExtra(us_correo,user_email);
                        intent.putExtra(finca_mess,fin_ms);
                        startActivity(intent);
                    }
                });

            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }


        if (resultCounter < 1) {

            TextView mes = new TextView(HomeActivity.this);
            mes.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            mes.setTextColor(Color.BLACK);
            mes.setText("No has creado ninguna finca");
            mes.setGravity(Gravity.CENTER);
            mes.setTextSize(15);
            mes.setPadding(0, 100, 0, 100);

            Button button = new Button(HomeActivity.this);
            button.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            button.setGravity(Gravity.CENTER);
            button.setText("Crear finca");
            button.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            button.setTextColor(Color.WHITE);

            LinearLayout ll = findViewById(R.id.ll_parent);
            ll.setBackgroundColor(Color.WHITE);
            ll.addView(mes);
            ll.addView(button);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(HomeActivity.this, FincasActivity.class);
                    intent.putExtra(us_correo, user_email);
                    startActivity(intent);
                    HomeActivity.this.finish();
                }
            });
        }
    }

    private void initializeMenuBar() {
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

                if(menu_id == R.id.edit_perfil){
                    Intent intent = new Intent(HomeActivity.this, PerfilActivity.class);
                    startActivity(intent);
                    HomeActivity.this.finish();
                }
                else if(menu_id == R.id.inicio){
                    mDrawerLayout.closeDrawers();
                }
                else if(menu_id == R.id.edit_fincas){

                    Intent intent = new Intent(HomeActivity.this, FincasActivity.class);
                    startActivity(intent);
                    HomeActivity.this.finish();
                }
                else if(menu_id == R.id.edit_actividades){
                    Intent intent = new Intent(HomeActivity.this, ActividadesActivity.class);
                    startActivity(intent);
                    HomeActivity.this.finish();
                }
                else if(menu_id == R.id.edit_empleados){
                    Intent intent = new Intent(HomeActivity.this, EmpleadosActivity.class);
                    startActivity(intent);
                    HomeActivity.this.finish();
                }
                else if(menu_id == R.id.historial_fact){
                    Intent intent = new Intent(HomeActivity.this, FiltroHistorialActivity.class);
                    startActivity(intent);
                    HomeActivity.this.finish();
                }
                else if(menu_id == R.id.backup){
                    Intent intent = new Intent(HomeActivity.this, BackupActivity.class);
                    startActivity(intent);
                    HomeActivity.this.finish();
                }
                else if(menu_id == R.id.contacto){
                    Intent intent = new Intent(HomeActivity.this, ContactoActivity.class);
                    startActivity(intent);
                    HomeActivity.this.finish();
                }
                else if(menu_id == R.id.logout){
                    //re dirige a la pagina de inicio y borra los shared pref
                    SharedPreferences.Editor editor = getSharedPreferences("MyPrefs",0).edit();
                    editor.clear(); //clear all stored data
                    editor.commit();
                    Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                    HomeActivity.this.finish();
                }
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return  mActionBarDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
