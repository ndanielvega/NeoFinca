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
import android.view.inputmethod.InputMethodManager;

public class ContactoActivity extends AppCompatActivity {
    //menu_nav
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacto);

        initializeMenuBar();
    }

    private void initializeMenuBar() {
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
                    Intent intent = new Intent(ContactoActivity.this, HomeActivity.class);
                    startActivity(intent);
                    ContactoActivity.this.finish();
                }
                else if(menu_id == R.id.edit_perfil){
                    Intent intent = new Intent(ContactoActivity.this, PerfilActivity.class);
                    startActivity(intent);
                    ContactoActivity.this.finish();
                }
                else if(menu_id == R.id.edit_fincas){
                    Intent intent = new Intent(ContactoActivity.this, FincasActivity.class);
                    startActivity(intent);
                    ContactoActivity.this.finish();
                }
                else if(menu_id == R.id.edit_actividades){
                    Intent intent = new Intent(ContactoActivity.this, ActividadesActivity.class);
                    startActivity(intent);
                    ContactoActivity.this.finish();
                }
                else if(menu_id == R.id.edit_empleados){
                    Intent intent = new Intent(ContactoActivity.this, EmpleadosActivity.class);
                    startActivity(intent);
                    ContactoActivity.this.finish();
                }
                else if(menu_id == R.id.historial_fact){
                    Intent intent = new Intent(ContactoActivity.this, FiltroHistorialActivity.class);
                    startActivity(intent);
                    ContactoActivity.this.finish();
                }
                else if(menu_id == R.id.backup){
                    Intent intent = new Intent(ContactoActivity.this, BackupActivity.class);
                    startActivity(intent);
                    ContactoActivity.this.finish();
                }
                else if(menu_id == R.id.contacto){
                    mDrawerLayout.closeDrawers();
                }
                else if(menu_id == R.id.logout){
                    //close session
                    SharedPreferences.Editor editor = getSharedPreferences("MyPrefs",0).edit();
                    editor.clear(); //clear all stored data
                    editor.commit();
                    Intent intent = new Intent(ContactoActivity.this, LoginActivity.class);
                    startActivity(intent);
                    ContactoActivity.this.finish();
                }
                return false;
            }
        });
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
        Intent intent = new Intent(ContactoActivity.this, HomeActivity.class);
        startActivity(intent);
        ContactoActivity.this.finish();
    }

}
