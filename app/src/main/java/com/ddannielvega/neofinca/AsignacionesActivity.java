package com.ddannielvega.neofinca;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.ddannielvega.neofinca.model.actividad;
import com.ddannielvega.neofinca.model.asignacion;
import com.ddannielvega.neofinca.model.empleado;
import com.ddannielvega.neofinca.model.finca;
import com.ddannielvega.neofinca.model.finxemp;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmResults;


public class AsignacionesActivity extends AppCompatActivity implements AddActividadModal.ModalListener {


    //New attributes
    public Realm realm;
    public static final String us_correo = "us_correo";
    public static final String asig_id_fin = "asig_id_fin";
    public String id_finca;
        //Attributes to start FacturasActivity
        public empleado empleado_actividad;
        public static final String empleado_id_mess = "empleado_id_mess";
        public String emp_id_mess;
        public static final String empleado_name_mess = "empleado_name_mess";
        public String emp_name_mess;
        public static final String empleado_val_mess = "empleado_val_mess";
        public String emp_val_mess;
        public static final String us_correo_mess_fact = "us_correo_mess_fact";
        public String us_correo_mess;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asignaciones);

        /*function summary

         * Get user from other activities
         * Facturar button listener
         * Get and add initial views

         * */

        //Get user email from other activities
            Intent intent = getIntent();
            id_finca = intent.getStringExtra("finca_mess");
            final String email_user = intent.getStringExtra("us_correo");
            us_correo_mess = email_user;


        //Facturar button listener
            Button edit_button = findViewById(R.id.fact_button);
            edit_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(AsignacionesActivity.this, FacturasActivity.class);
                    intent.putExtra(us_correo_mess_fact,us_correo_mess);
                    intent.putExtra(asig_id_fin,id_finca);
                    intent.putExtra(empleado_id_mess,emp_id_mess);
                    intent.putExtra(empleado_name_mess,emp_name_mess);
                    intent.putExtra(empleado_val_mess,emp_val_mess);
                    startActivity(intent);
                    AsignacionesActivity.this.finish();
                }
            });

        //Get and add initial views
            realm = Realm.getDefaultInstance();
            realm.beginTransaction();
                //finca
                finca finca = realm.where(finca.class)
                        .equalTo("id_finca", Integer.parseInt(id_finca))
                        .findFirst();
                //empleado
                finxemp finxemp = realm.where(finxemp.class)
                        .equalTo("id_finca", Integer.parseInt(id_finca))
                        .and()
                        .equalTo("finxemp_estado", "1")
                        .findFirst();
                final empleado empleado= realm.where(empleado.class)
                        .equalTo("id_empleado",finxemp.getId_empleado())
                        .findFirst();

                empleado_actividad = empleado;
            realm.commitTransaction();
            emp_id_mess = String.valueOf(empleado.getId_empleado());
            emp_name_mess = empleado.getEmp_nombre();
            emp_val_mess = String.valueOf(empleado.getEmp_valorxjornal());

        //Add data to views
            TextView fin_name =  findViewById(R.id.fin_name);
            TextView emp_name =  findViewById(R.id.emp_name);
            TextView emp_val =  findViewById(R.id.emp_val);
            String fin_nametext= "Finca: " + finca.getFin_nombre();
            fin_name.setText(fin_nametext);
            String emp_nametext= "Empleado asignado: " + empleado.getEmp_nombre();
            emp_name.setText(emp_nametext);
            String emp_valtext = "Valor por jornal: "+ String.valueOf(empleado.getEmp_valorxjornal()) + " COP";
            emp_val.setText(emp_valtext);



        //get all asignaciones
            int ct = 0;
            realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            final RealmResults<asignacion> asginaciones_all= realm.where(asignacion.class)
                    .equalTo("id_finca", Integer.parseInt(id_finca))
                    .and()
                    .notEqualTo("asig_estado","2")
                    .and()
                    .equalTo("deleted",false)
                    .findAll();
            realm.commitTransaction();
            for (asignacion result:asginaciones_all){
                try {
                    ct= ct +1;
                    //get the actividad name
                        realm = Realm.getDefaultInstance();
                        realm.beginTransaction();
                        actividad actividad_this = realm.where(actividad.class)
                                .and()
                                .equalTo("id_actividad",result.getId_actividad())
                                .findFirst();
                        realm.commitTransaction();
                    createAsignacionesViews(actividad_this.getAct_descripcion(),
                            String.valueOf(result.getId_asignacion()),
                            String.valueOf(result.getAsig_cant_jornales()),
                            result.getAsig_estado(),
                            result.getAsig_totalxactividad()
                            );

                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }
            if (ct>0){
                Toast.makeText(AsignacionesActivity.this, "Manten presionada la actividad para eliminarla", Toast.LENGTH_SHORT).show();
            }
            updateTotal();


        //Add actividad listener
            findViewById(R.id.add_act_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openModal();
                }
            });


    }


    public String getAct_email() {
        return us_correo_mess;
    }

    private void openModal() {
        AddActividadModal modal = new AddActividadModal();
        modal.show(getSupportFragmentManager(),"modal window");
    }

    public void addActividad(String ac_name, int e_i){
        //Add a new object to the database

        final int emp_id = e_i;
        final String  act_name = ac_name;
        //create asginaciones objects
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                //Get id from actividad
                    actividad actividad = realm.where(actividad.class)
                        .and()
                        .equalTo("act_descripcion",act_name)
                        .findFirst();
                //Create object
                    asignacion asignacion = new asignacion();
                    Number maxId = realm.where(asignacion.class).max("id_asignacion");
                    int nextId = (maxId == null) ? 1 : maxId.intValue() + 1;
                    asignacion.setDeleted(false);
                    asignacion.setId_asignacion(nextId);
                    asignacion.setId_finca(Integer.parseInt(id_finca));
                    asignacion.setId_actividad(actividad.getId_actividad());
                    asignacion.setId_empleado(emp_id);
                    asignacion.setAsig_cant_jornales(0);
                    asignacion.setAsig_estado("0");
                    final String timeStamp = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
                    asignacion.setAsiig_fecha(timeStamp);
                    asignacion.setAsig_totalxactividad("0");
                    realm.copyToRealmOrUpdate(asignacion);

                //Add new object to the container view
                    createAsignacionesViews(act_name,String.valueOf(nextId),"0","0","0");
                }
            });

    }

    public void createAsignacionesViews(String act_name, String asig_id, String asig_jorn , String asig_state, String asig_total ){
        final String id_asignacion = asig_id;

        /*
        Structure of the added views

        layout container

            blockLayout
                block1
                jornalText
                block2
            space


        * */
        //Create views
            LinearLayout containerLayout = findViewById(R.id.layout_container);
            containerLayout.setPadding(20,40,20,30);
            containerLayout.setBackgroundColor(getResources().getColor(R.color.colorGray));

            final LinearLayout blockLayout = new LinearLayout(AsignacionesActivity.this);
            blockLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            blockLayout.setOrientation(LinearLayout.VERTICAL);
            blockLayout.setPadding(40,0,40,20);
            blockLayout.setBackgroundColor(Color.WHITE);

                final LinearLayout block1 = new LinearLayout(AsignacionesActivity.this);
                block1.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                block1.setOrientation(LinearLayout.HORIZONTAL);
                block1.setPadding(0,5,0,0);

                    TextView actividadNameView= new TextView(AsignacionesActivity.this);
                    actividadNameView.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            1
                    ));
                    actividadNameView.setGravity(Gravity.CENTER_VERTICAL);
                    actividadNameView.setTextColor(Color.BLACK);
                    actividadNameView.setTextSize(16);
                    actividadNameView.setText(act_name);

                    TextView jornalLabel= new TextView(AsignacionesActivity.this);
                    jornalLabel.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.MATCH_PARENT
                    ));
                    jornalLabel.setGravity(Gravity.CENTER_VERTICAL);
                    jornalLabel.setText("Jornales: ");

                    EditText jornalInput= new EditText(AsignacionesActivity.this);
                    jornalInput.setLayoutParams(new LinearLayout.LayoutParams(
                            120,
                            110
                    ));
                    jornalInput.setText(asig_jorn);
                    int maxLength1 = 2;
                    jornalInput.setText(asig_jorn);
                    jornalInput.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                    jornalInput.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength1)});
                    jornalInput.setGravity(Gravity.CENTER);
                    jornalInput.setTextColor(Color.BLACK);
                    jornalInput.setInputType(InputType.TYPE_CLASS_NUMBER);

                    block1.addView(actividadNameView);
                    block1.addView(jornalLabel);
                    block1.addView(jornalInput);

                TextView jornalText= new TextView(AsignacionesActivity.this);
                jornalText.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        80
                ));
                    jornalText.setGravity(Gravity.CENTER_VERTICAL);
                    String stringJornalValue= "Valor por actividad: " + asig_total + " COP";
                    jornalText.setText(stringJornalValue);

                    //Create listener to jornales EditText
                    final EditText selectedJornal = jornalInput;
                    final TextView jornalTextView = jornalText;
                    jornalInput.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void afterTextChanged(Editable editable) {

                            realm = Realm.getDefaultInstance();
                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    asignacion asig_update = realm.where(asignacion.class)
                                            .and()
                                            .equalTo("id_asignacion",Integer.parseInt(id_asignacion))
                                            .and()
                                            .equalTo("deleted",false)
                                            .findFirst();
                                    empleado empleado= realm.where(empleado.class)
                                            .and()
                                            .equalTo("id_empleado",asig_update.getId_empleado())
                                            .findFirst();

                                    int num_val,total;
                                    if (TextUtils.isEmpty(selectedJornal.getText().toString())){
                                        num_val = 0;
                                        total = 0;
                                    }else {
                                        num_val = Integer.parseInt(selectedJornal.getText().toString());
                                        total = empleado.getEmp_valorxjornal() * num_val ;
                                    }

                                    DatePicker datePicker = new DatePicker(AsignacionesActivity.this);
                                    String year = String.valueOf(datePicker.getYear());
                                    String month = String.valueOf(datePicker.getMonth()+1);
                                    String day = String.valueOf(datePicker.getDayOfMonth());
                                    final String fullDate = day + "/" + month + "/" + year;
                                    asig_update.setAsiig_fecha(fullDate);
                                    asig_update.setAsig_cant_jornales(num_val);
                                    asig_update.setAsig_totalxactividad(String.valueOf(total));
                                    realm.copyToRealmOrUpdate(asig_update);

                                    String s = "Valor por actividad: " + String.valueOf(total) + " COP";
                                    jornalTextView.setText(s);

                                    updateTotal();
                                }
                            });
                        }
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        }
                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                    });

                final LinearLayout block2 = new LinearLayout(AsignacionesActivity.this);
                block2.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                block2.setOrientation(LinearLayout.HORIZONTAL);
                block2.setGravity(Gravity.CENTER);
                block2.setPadding(5,5,5,5);

                    TextView finalizadoText= new TextView(AsignacionesActivity.this);
                    finalizadoText.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));
                    finalizadoText.setGravity(Gravity.CENTER_VERTICAL);
                    finalizadoText.setText("        Finalizada");

                    final Switch stateView = new Switch(AsignacionesActivity.this);
                    stateView.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));
                    stateView.setText("Pendiente        ");
                    stateView.setTextColor(Color.GRAY);

                    if (asig_state.equals("0")){
                        stateView.setChecked(false);
                    }else {
                        stateView.setChecked(true);
                    }

                    stateView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (stateView.isChecked()){
                                //change state to 'finalizada'
                                realm = Realm.getDefaultInstance();
                                realm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        asignacion asig_update = realm.where(asignacion.class)
                                                .and()
                                                .equalTo("id_asignacion",Integer.parseInt(id_asignacion))
                                                .findFirst();
                                        asig_update.setAsig_estado("1");
                                        realm.copyToRealmOrUpdate(asig_update);
                                    }
                                });
                            }else{
                                //change state to 'pendiente'
                                realm = Realm.getDefaultInstance();
                                realm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        asignacion asig_update = realm.where(asignacion.class)
                                                .and()
                                                .equalTo("id_asignacion",Integer.parseInt(id_asignacion))
                                                .findFirst();
                                        asig_update.setAsig_estado("0");
                                        realm.copyToRealmOrUpdate(asig_update);
                                    }
                                });

                            }
                        }
                    });
                    block2.addView(stateView);
                    block2.addView(finalizadoText);

                View space = new LinearLayout(AsignacionesActivity.this);
                space.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        15
                ));

                blockLayout.addView(block1);
                blockLayout.addView(jornalText);
                blockLayout.addView(block2);
                containerLayout.addView(blockLayout);
                containerLayout.addView(space);

        //Set long click listener to delete object
            blockLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    // setup the alert builder
                    AlertDialog.Builder builder = new AlertDialog.Builder(AsignacionesActivity.this);
                    builder.setMessage("¿Estás seguro de eliminar la actividad?");
                    // add a button
                    builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            ViewGroup container = findViewById(R.id.layout_container);
                            LinearLayout lin_del = blockLayout;
                            View nextView = container.getChildAt(container.indexOfChild(lin_del)+1);

                            nextView.setVisibility(View.GONE);
                            lin_del.setVisibility(View.GONE);

                            LinearLayout lin = findViewById(R.id.test_lo);
                            lin.removeView(lin_del);
                            lin.removeView(nextView);


                            realm = Realm.getDefaultInstance();
                            realm.beginTransaction();
                            final asignacion asig_delete = realm.where(asignacion.class)
                                    .and()
                                    .equalTo("id_asignacion",Integer.parseInt(id_asignacion))
                                    .findFirst();
                            asig_delete.setDeleted(true);
                            realm.commitTransaction();

                            updateTotal();
                            Toast.makeText(AsignacionesActivity.this, "Se ha eliminado", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.setNegativeButton("No", null);
                    // create and show the alert dialog
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return false;
                }
            });

    }

    public void updateTotal(){
        //Change the value of valorTotal view

            ViewGroup container = findViewById(R.id.layout_container);
            int size = container.getChildCount();
            int temp_val;
            int sum = 0;

            /*
            get blockContainer element and then get the second child which have the value
            Second because the structure is
                layoutContainer
                    blockContainer -> 0
                    Space -> 1
            * */
            for (int n=0; n<size;n++){
                if (n%2 == 0){
                    LinearLayout blockContainer = (LinearLayout) container.getChildAt(n);
                    TextView TV = (TextView) blockContainer.getChildAt(1); //second element
                    String sentence = TV.getText().toString();
                    String[] split = sentence.split(" ");
                    temp_val = Integer.parseInt(split[3]);
                    sum = sum + temp_val;
                }
            }
            TextView totalTV = findViewById(R.id.fact_total);
            String s = "TOTAL: "+ String.valueOf(sum) +" COP";
            totalTV.setText(s);
    }

    @Override
    public void passText(String actividadName) {
        //get the actividad name from the modal window and then add it to the view using addActividad
        addActividad(actividadName,empleado_actividad.getId_empleado());
    }

    @Override
    public void onBackPressed() {
        AsignacionesActivity.this.finish();
    }
}
