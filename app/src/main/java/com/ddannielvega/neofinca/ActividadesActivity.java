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

public class ActividadesActivity extends AppCompatActivity implements ActividadesFragmentTab1.OnFragmentInteractionListener, ActividadesFragmentTab2.OnFragmentInteractionListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    public String user_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividades);

        //get user email from shared prefs
            Map<String,?> result_sp = getSharedPreferences("MyPrefs", LoginActivity.MODE_PRIVATE).getAll();
            for (Map.Entry<String, ?> entry : result_sp.entrySet()) {
                user_email = entry.getKey();
            }

        //initialize menu bar
            initializeMenuBar();


        //set tabs views and listeners
            TabLayout tabLayout = findViewById(R.id.ActividadesTabLayout);
            ViewPager viewPager = findViewById(R.id.activivadesViewPager);
            ActividadesAdapter myPagerAdapter = new ActividadesAdapter( getSupportFragmentManager(), tabLayout.getTabCount());
            viewPager.setAdapter(myPagerAdapter);
            viewPager.addOnPageChangeListener( new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.addOnTabSelectedListener( new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

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
        Intent intent = new Intent(ActividadesActivity.this, HomeActivity.class);
        startActivity(intent);
        ActividadesActivity.this.finish();
    }
    public String getAct_email() {
        return user_email;
    }
    private void initializeMenuBar() {
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mActionBarDrawerToggle= new ActionBarDrawerToggle(ActividadesActivity.this,mDrawerLayout,R.string.menu_nav_open,R.string.menu_nav_close);
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
                    Intent intent = new Intent(ActividadesActivity.this, PerfilActivity.class);
                    startActivity(intent);
                    ActividadesActivity.this.finish();
                }
                else if(menu_id == R.id.edit_actividades){
                    mDrawerLayout.closeDrawers();
                }
                else if(menu_id == R.id.edit_fincas){
                    Intent intent = new Intent(ActividadesActivity.this, FincasActivity.class);
                    startActivity(intent);
                    ActividadesActivity.this.finish();
                }
                else if(menu_id == R.id.inicio){
                    Intent intent = new Intent(ActividadesActivity.this, HomeActivity.class);
                    startActivity(intent);
                    ActividadesActivity.this.finish();
                }
                else if(menu_id == R.id.edit_empleados){
                    Intent intent = new Intent(ActividadesActivity.this, EmpleadosActivity.class);
                    startActivity(intent);
                    ActividadesActivity.this.finish();
                }
                else if(menu_id == R.id.historial_fact){
                    Intent intent = new Intent(ActividadesActivity.this, FiltroHistorialActivity.class);
                    startActivity(intent);
                    ActividadesActivity.this.finish();
                }
                else if(menu_id == R.id.backup){
                    Intent intent = new Intent(ActividadesActivity.this, BackupActivity.class);
                    startActivity(intent);
                    ActividadesActivity.this.finish();
                }
                else if(menu_id == R.id.contacto){
                    Intent intent = new Intent(ActividadesActivity.this, ContactoActivity.class);
                    startActivity(intent);
                    ActividadesActivity.this.finish();
                }
                else if(menu_id == R.id.logout){
                    //re dirige a la pagina de inicio
                    SharedPreferences.Editor editor = getSharedPreferences("MyPrefs",0).edit();
                    editor.clear(); //clear all stored data
                    editor.commit();
                    Intent intent = new Intent(ActividadesActivity.this, LoginActivity.class);
                    startActivity(intent);
                    ActividadesActivity.this.finish();
                }
                return false;
            }
        });
    }

}



