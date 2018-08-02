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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ddannielvega.neofinca.model.empleado;
import com.ddannielvega.neofinca.model.finca;
import com.ddannielvega.neofinca.model.usuario;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;

public class FiltroHistorialActivity extends AppCompatActivity {

    //New Attributes
    public static final String filter = "filter";
    public static final String option = "option";
    public String selected_filter;
    public String selected_opt;
    //spinner attributes
        Spinner spinner_empleados;
        public List<String> emp_nombres;
        public List<String> emp_ids;
        Spinner spiner_fincas;
        public List<String> fin_nombres;
        public List<String> fin_ids;
        public Realm realm;
    //menu nav attributes
        private DrawerLayout mDrawerLayout;
        private ActionBarDrawerToggle mActionBarDrawerToggle;
        public String user_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtro_historial);

        /*function summary

         * Get user email from shared prefs
         * Initialize menu bar
         * Set data in filter spinner
         * Get data from empleados spinner
         * Get data from fincas spinner
         * Set search button listener

         * */

        //Get user email from shared prefs
            Map<String,?> result_sp = getSharedPreferences("MyPrefs", LoginActivity.MODE_PRIVATE).getAll();
            for (Map.Entry<String, ?> entry : result_sp.entrySet()) {
                user_email = entry.getKey();
            }

        //Initialize menu bar
            initializeMenuBar();

        //Set data in filter spinner
            Spinner filter_spinner = findViewById(R.id.filter_spinner);
            List<String> filter_opt = Arrays.asList("Todas", "Empleado", "Finca","Fecha");
            ArrayAdapter<String> adapteropt = new ArrayAdapter<String>(this,R.layout.spinner_item,filter_opt);
            adapteropt.setDropDownViewResource(R.layout.spinner_dropdown_item);
            filter_spinner.setAdapter(adapteropt);
            adapteropt.notifyDataSetChanged();
            filter_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int selected_item_position, long l) {
                  //hide everything
                    LinearLayout options_layout = findViewById(R.id.options_container);
                    TextView all_tv = (TextView) options_layout.getChildAt(0);
                    LinearLayout empleado_rl = (LinearLayout) options_layout.getChildAt(1);
                    LinearLayout finca_rl = (LinearLayout ) options_layout.getChildAt(2);
                    DatePicker datepicker_opt = (DatePicker) options_layout.getChildAt(3);
                        all_tv.setVisibility(View.GONE);
                        empleado_rl.setVisibility(View.GONE);
                        finca_rl.setVisibility(View.GONE);
                        datepicker_opt.setVisibility(View.GONE);

                    //show selected option
                    if (selected_item_position == 0){
                        all_tv.setVisibility(View.VISIBLE);
                    }else{
                        if (selected_item_position == 1){
                            empleado_rl.setVisibility(View.VISIBLE);
                        }else{
                            if (selected_item_position == 2){
                                finca_rl.setVisibility(View.VISIBLE);
                            }else{
                                if (selected_item_position == 3){
                                    datepicker_opt.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }

                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        //Get data from empleados spinner
            spinner_empleados = findViewById(R.id.spiner_empleados);
            //get data
            emp_nombres = new ArrayList<String>();
            emp_ids = new ArrayList<String>();
            int c1 = 0;
            realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            RealmResults<empleado> results = realm.where(empleado.class).findAll();
            realm.commitTransaction();

            if (results.size()>0){
                for (empleado result:results){
                    try {
                        c1=c1+1;
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
            final int cont1 = c1;

        //Get data from fincas spinner
            spiner_fincas = findViewById(R.id.spiner_fincas);
            //get data
            fin_nombres = new ArrayList<String>();
            fin_ids = new ArrayList<String>();

            int c2=0;
            realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            usuario resultsuser = realm.where(usuario.class)
                    .equalTo("us_correo", user_email)
                    .findFirst();
            RealmResults<finca> resultsfin = realm.where(finca.class)
                    .equalTo("id_usuario", resultsuser.getId_usuario())
                    .findAll();
            realm.commitTransaction();

            if (resultsfin.size()>0){
                for (finca result:resultsfin){
                    try {
                        c2=c2+1;
                        fin_nombres.add(result.getFin_nombre());
                        fin_ids.add(String.valueOf(result.getId_finca()));
                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }
                }
            }
            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,fin_nombres);
            adapter2.setDropDownViewResource(R.layout.spinner_dropdown_item);
            spiner_fincas.setAdapter(adapter2);
            adapter2.notifyDataSetChanged();
            final int cont2 = c2;


        //Set search button listener
            Button edit_button = findViewById(R.id.search_button);
            edit_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //get selected option
                    Spinner filter_spinner = findViewById(R.id.filter_spinner);
                    selected_opt = filter_spinner.getSelectedItem().toString();
                    if (selected_opt.equals("Todas") ){
                        selected_opt = "all";
                        selected_filter = "none";
                        startHistorialAct();
                    }else{
                        if (selected_opt.equals("Empleado") ){

                            if (cont1==0){
                                Toast.makeText(FiltroHistorialActivity.this, "No hay empleados", Toast.LENGTH_SHORT).show();
                            }else{
                                Spinner spinner_empleado = findViewById(R.id.spiner_empleados);
                                String selected = spinner_empleado.getSelectedItem().toString();
                                String id_val = emp_ids.get(emp_nombres.indexOf(selected));
                                //search the empleado object
                                realm = Realm.getDefaultInstance();
                                realm.beginTransaction();
                                empleado empleado= realm.where(empleado.class).equalTo("id_empleado", Integer.valueOf(id_val) ).findFirst();
                                realm.commitTransaction();

                                selected_opt = "empleado";
                                selected_filter = String.valueOf(empleado.getId_empleado());
                                startHistorialAct();
                            }
                        }else{
                            if (selected_opt.equals("Finca") ){

                                if (cont2==0){
                                    Toast.makeText(FiltroHistorialActivity.this, "No hay fincas", Toast.LENGTH_SHORT).show();
                                }else{
                                    Spinner spinner_finca = findViewById(R.id.spiner_fincas);
                                    String selected = spinner_finca.getSelectedItem().toString();
                                    String id_val = fin_ids.get(fin_nombres.indexOf(selected));
                                    //search the finca object
                                    realm = Realm.getDefaultInstance();
                                    realm.beginTransaction();
                                    finca finca= realm.where(finca.class).equalTo("id_finca", Integer.valueOf(id_val) ).findFirst();
                                    realm.commitTransaction();

                                    selected_opt = "finca";
                                    selected_filter = String.valueOf(finca.getId_finca());
                                    startHistorialAct();
                                }
                            }else{
                                if (selected_opt.equals("Fecha") ){
                                    DatePicker datePicker = findViewById(R.id.datepicker);
                                    String year = String.valueOf(datePicker.getYear());
                                    String month = String.valueOf(datePicker.getMonth()+1);
                                    String day = String.valueOf(datePicker.getDayOfMonth());
                                    String fullDate = day + "/" + month + "/" + year;

                                    selected_opt = "fecha";
                                    selected_filter = fullDate;
                                    startHistorialAct();
                                }else{
                                    Toast.makeText(FiltroHistorialActivity.this, "No se ha seleccionado ningun filtro", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                    }
                }
            });


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
                if (menu_id == R.id.inicio){
                    Intent intent = new Intent(FiltroHistorialActivity.this, HomeActivity.class);
                    startActivity(intent);
                    FiltroHistorialActivity.this.finish();
                }
                else if(menu_id == R.id.historial_fact){
                    mDrawerLayout.closeDrawers();
                }
                else if(menu_id == R.id.edit_fincas){
                    Intent intent = new Intent(FiltroHistorialActivity.this, FincasActivity.class);
                    startActivity(intent);
                    FiltroHistorialActivity.this.finish();
                }
                else if(menu_id == R.id.edit_perfil){
                    Intent intent = new Intent(FiltroHistorialActivity.this, PerfilActivity.class);
                    startActivity(intent);
                    FiltroHistorialActivity.this.finish();
                }
                else if(menu_id == R.id.edit_actividades){
                    Intent intent = new Intent(FiltroHistorialActivity.this, ActividadesActivity.class);
                    startActivity(intent);
                    FiltroHistorialActivity.this.finish();
                }
                else if(menu_id == R.id.edit_empleados){
                    Intent intent = new Intent(FiltroHistorialActivity.this, EmpleadosActivity.class);
                    startActivity(intent);
                    FiltroHistorialActivity.this.finish();
                }
                else if(menu_id == R.id.backup){
                    Intent intent = new Intent(FiltroHistorialActivity.this, BackupActivity.class);
                    startActivity(intent);
                    FiltroHistorialActivity.this.finish();
                }
                else if(menu_id == R.id.contacto){
                    Intent intent = new Intent(FiltroHistorialActivity.this, ContactoActivity.class);
                    startActivity(intent);
                    FiltroHistorialActivity.this.finish();
                }
                else if(menu_id == R.id.logout){
                    //re dirige a la pagina de inicio
                    SharedPreferences.Editor editor = getSharedPreferences("MyPrefs",0).edit();
                    editor.clear(); //clear all stored data
                    editor.apply();
                    Intent intent = new Intent(FiltroHistorialActivity.this, LoginActivity.class);
                    startActivity(intent);
                    FiltroHistorialActivity.this.finish();
                }
                return false;
            }
        });
    }

    public void startHistorialAct(){
        Intent intent = new Intent(FiltroHistorialActivity.this, HistorialActivity.class);
        intent.putExtra(option,selected_opt);
        intent.putExtra(filter,selected_filter);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return  mActionBarDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(FiltroHistorialActivity.this, HomeActivity.class);
        startActivity(intent);
        FiltroHistorialActivity.this.finish();
    }
}
