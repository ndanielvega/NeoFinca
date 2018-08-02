package com.ddannielvega.neofinca;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import io.realm.Realm;

public class BackupActivity extends AppCompatActivity {


    //Menu_nav
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    public String user_email;


    //Backup directories
    private File EXPORT_REALM_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    //The name of the backup file corresponds to the user's email so that it is individual
    private String EXPORT_REALM_FILE_NAME = "temporalname.realm"; //is updated when the activity starts
    private String IMPORT_REALM_FILE_NAME = "myrealm.realm"; //default name inRealmApplication.class
    public Realm realm;


    //Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);


        //Get user email from shared prefs
        Map<String,?> result_sp = getSharedPreferences("MyPrefs", LoginActivity.MODE_PRIVATE).getAll();
        for (Map.Entry<String, ?> entry : result_sp.entrySet()) {
            user_email = entry.getKey();
        }
        EXPORT_REALM_FILE_NAME = user_email + ".realm";


        initializeMenuBar();

        // First check if we have storage permissions
        checkStoragePermissions(BackupActivity.this);

        //button listeners
            Button backup_bt = findViewById(R.id.backup_bt);
            backup_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    backup();
                }
            });

            Button restore_bt = findViewById(R.id.restore_bt);
            restore_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // setup the alert builder
                    AlertDialog.Builder builder = new AlertDialog.Builder(BackupActivity.this);
                    builder.setMessage("Toda la información actual se eliminará ¿Estás seguro?");
                    builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            restore();
                        }
                    });
                    builder.setNegativeButton("No", null);
                    // create and show the alert dialog
                    AlertDialog dialog = builder.create();
                    dialog.show();
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
                    Intent intent = new Intent(BackupActivity.this, HomeActivity.class);
                    startActivity(intent);
                    BackupActivity.this.finish();
                }
                else if(menu_id == R.id.backup){
                    mDrawerLayout.closeDrawers();
                }
                else if(menu_id == R.id.edit_perfil){
                    Intent intent = new Intent(BackupActivity.this, PerfilActivity.class);
                    startActivity(intent);
                    BackupActivity.this.finish();
                }
                else if(menu_id == R.id.edit_fincas){
                    Intent intent = new Intent(BackupActivity.this, FincasActivity.class);
                    startActivity(intent);
                    BackupActivity.this.finish();
                }
                else if(menu_id == R.id.edit_actividades){
                    Intent intent = new Intent(BackupActivity.this, ActividadesActivity.class);
                    startActivity(intent);
                    BackupActivity.this.finish();
                }
                else if(menu_id == R.id.edit_empleados){
                    Intent intent = new Intent(BackupActivity.this, EmpleadosActivity.class);
                    startActivity(intent);
                    BackupActivity.this.finish();
                }
                else if(menu_id == R.id.historial_fact){
                    Intent intent = new Intent(BackupActivity.this, FiltroHistorialActivity.class);
                    startActivity(intent);
                    BackupActivity.this.finish();
                }
                else if(menu_id == R.id.contacto){
                    Intent intent = new Intent(BackupActivity.this, ContactoActivity.class);
                    startActivity(intent);
                    BackupActivity.this.finish();
                }
                else if(menu_id == R.id.logout){
                    //re dirige a la pagina de inicio
                    SharedPreferences.Editor editor = getSharedPreferences("MyPrefs",0).edit();
                    editor.clear(); //clear all stored data
                    editor.commit();
                    Intent intent = new Intent(BackupActivity.this, LoginActivity.class);
                    startActivity(intent);
                    BackupActivity.this.finish();
                }
                return false;
            }
        });
    }

    //permissions
    private void checkStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if(permission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }


    public void backup() {

        realm = Realm.getDefaultInstance();
        Toast.makeText(BackupActivity.this, "original pathh " + realm.getPath(), Toast.LENGTH_LONG).show();

        File exportRealmFile;

        EXPORT_REALM_PATH.mkdirs();

        // create a backup file
        exportRealmFile = new File(EXPORT_REALM_PATH, EXPORT_REALM_FILE_NAME);

        // if backup file already exists, delete it
        exportRealmFile.delete();

        // copy current realm to backup file

        realm.writeCopyTo(exportRealmFile);
        realm.close();


        String msg = "File exported to Path: " + EXPORT_REALM_PATH + "/" + EXPORT_REALM_FILE_NAME;
        Toast.makeText(BackupActivity.this.getApplicationContext(), msg, Toast.LENGTH_LONG).show();


    }

    public void restore() {
        String restoreFilePath = EXPORT_REALM_PATH + "/" + EXPORT_REALM_FILE_NAME; //backup file path


        copyBundledRealmFile(restoreFilePath, IMPORT_REALM_FILE_NAME); //import = default

    }

    private String copyBundledRealmFile(String oldFilePath, String outFileName) {
        try {
                File file = new File((BackupActivity.this).getFilesDir(), outFileName);

                File check = new File(oldFilePath); //backup file

                if (check.length() == 0 ){
                    Toast.makeText(BackupActivity.this, "No existe archivo de respaldo", Toast.LENGTH_LONG).show();
                }else{
                    FileOutputStream outputStream = new FileOutputStream(file);

                    FileInputStream inputStream = new FileInputStream(new File(oldFilePath));

                    byte[] buf = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buf)) > 0) {
                        outputStream.write(buf, 0, bytesRead);
                    }
                    outputStream.close();

                    restartApp();

                    return file.getAbsolutePath();


                }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void restartApp() {

        AlarmManager mgr = (AlarmManager)getSystemService(BackupActivity.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, PendingIntent.getActivity(this.getBaseContext(), 0, new    Intent(getIntent()), getIntent().getFlags()));
        System.exit(2);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return  mActionBarDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(BackupActivity.this, HomeActivity.class);
        startActivity(intent);
        BackupActivity.this.finish();
    }
}
