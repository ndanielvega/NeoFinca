package com.ddannielvega.neofinca;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import java.util.Map;

public class EmpleadosActivity extends AppCompatActivity implements EmpleadosFragmentTab1.OnFragmentInteractionListener, EmpleadosFragmentTab2.OnFragmentInteractionListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    public String user_email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empleados);

        //get user email from shared prefs
        Map<String,?> result_sp = getSharedPreferences("MyPrefs", LoginActivity.MODE_PRIVATE).getAll();
        for (Map.Entry<String, ?> entry : result_sp.entrySet()) {
            user_email = entry.getKey();
        }

        initializeMenuBar();

        //tabs
        TabLayout tabLayout = findViewById(R.id.EmpleadosTabLayout);
        ViewPager viewPager = findViewById(R.id.empleadosViewPager);
        EmpleadosAdapter myPagerAdapter = new EmpleadosAdapter( getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(myPagerAdapter);
        viewPager.addOnPageChangeListener( new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

    }

    private void initializeMenuBar() {
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mActionBarDrawerToggle= new ActionBarDrawerToggle(EmpleadosActivity.this,mDrawerLayout,R.string.menu_nav_open,R.string.menu_nav_close);
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
                    Intent intent = new Intent(EmpleadosActivity.this, PerfilActivity.class);
                    startActivity(intent);
                    EmpleadosActivity.this.finish();
                }
                else if(menu_id == R.id.edit_empleados){
                    mDrawerLayout.closeDrawers();
                }
                else if(menu_id == R.id.edit_fincas){
                    Intent intent = new Intent(EmpleadosActivity.this, FincasActivity.class);
                    startActivity(intent);
                    EmpleadosActivity.this.finish();
                }
                else if(menu_id == R.id.inicio){
                    Intent intent = new Intent(EmpleadosActivity.this, HomeActivity.class);
                    startActivity(intent);
                    EmpleadosActivity.this.finish();
                }
                else if(menu_id == R.id.edit_actividades){
                    Intent intent = new Intent(EmpleadosActivity.this, ActividadesActivity.class);
                    startActivity(intent);
                    EmpleadosActivity.this.finish();
                }
                else if(menu_id == R.id.historial_fact){
                    Intent intent = new Intent(EmpleadosActivity.this, FiltroHistorialActivity.class);
                    startActivity(intent);
                    EmpleadosActivity.this.finish();
                }
                else if(menu_id == R.id.backup){
                    Intent intent = new Intent(EmpleadosActivity.this, BackupActivity.class);
                    startActivity(intent);
                    EmpleadosActivity.this.finish();
                }
                else if(menu_id == R.id.contacto){
                    Intent intent = new Intent(EmpleadosActivity.this, ContactoActivity.class);
                    startActivity(intent);
                    EmpleadosActivity.this.finish();
                }
                else if(menu_id == R.id.logout){
                    //re dirige a la pagina de inicio
                    SharedPreferences.Editor editor = getSharedPreferences("MyPrefs",0).edit();
                    editor.clear(); //clear all stored data
                    editor.commit();
                    Intent intent = new Intent(EmpleadosActivity.this, LoginActivity.class);
                    startActivity(intent);
                    EmpleadosActivity.this.finish();
                }
                return false;
            }
        });
    }


    public String getAct_email() {
        return user_email;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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
        Intent intent = new Intent(EmpleadosActivity.this, HomeActivity.class);
        startActivity(intent);
        EmpleadosActivity.this.finish();
    }

}
