package com.ddannielvega.neofinca;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ddannielvega.neofinca.model.empleado;
import com.ddannielvega.neofinca.model.finca;
import com.ddannielvega.neofinca.model.finxemp;
import com.ddannielvega.neofinca.model.usuario;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;



public class FincasFragmentTab2 extends Fragment {

    //Required attributes
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private OnFragmentInteractionListener mListener;
    private String mParam1;
    private String mParam2;

    //New attributes
    public static final String us_correo = "us_correo";
    public Realm realm;
    public String error_ms;
    Spinner spinner_empleados;
    public List<String> emp_nombres;
    public List<String> emp_ids;



    public FincasFragmentTab2() {
        // Required empty public constructor
    }
    public static FincasFragmentTab2 newInstance(String param1, String param2) {
        FincasFragmentTab2 fragment = new FincasFragmentTab2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fincas_fragment_tab2, container, false);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //carga datos de spinner or button to create empleado
            spinner_empleados = getActivity().findViewById(R.id.spiner_empleados);
            //get data
            emp_nombres = new ArrayList<String>();
            emp_ids = new ArrayList<String>();

            realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            RealmResults<empleado> results = realm.where(empleado.class)
                    .and()
                    .equalTo("deleted",false)
                    .findAll();
            realm.commitTransaction();
            for (empleado result:results){
                try {
                    realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    RealmResults<finxemp> resultsfxe = realm.where(finxemp.class)
                            .and()
                            .equalTo("id_empleado",result.getId_empleado())
                            .and()
                            .equalTo("finxemp_estado","1")
                            .findAll();
                    realm.commitTransaction();
                    if (resultsfxe.size()==0){
                        emp_nombres.add(result.getEmp_nombre());
                        emp_ids.add(String.valueOf(result.getId_empleado()));
                    }
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }

            if (emp_ids.size()==0){
                //si no hay empleados disponibles muestra un boton para crear nuevos empleados

                //hide everything
                Button agregar_bt= getActivity().findViewById(R.id.finca_page_button);
                EditText finca_name = getActivity().findViewById(R.id.finca_name);
                EditText finca_dir = getActivity().findViewById(R.id.finca_dir);
                TextView label_fin_name = getActivity().findViewById(R.id.label_fin_name);
                TextView label_fin_dir= getActivity().findViewById(R.id.label_fin_dir);
                    agregar_bt.setVisibility(View.GONE);
                    label_fin_name.setVisibility(View.GONE);
                    label_fin_dir.setVisibility(View.GONE);
                    finca_name.setVisibility(View.GONE);
                    finca_dir.setVisibility(View.GONE);


                //remove spinner and change label
                RelativeLayout rl = getActivity().findViewById(R.id.empleado_opt);
                TextView label = getActivity().findViewById(R.id.emp_label);
                    rl.setBackgroundColor(Color.WHITE);
                    rl.removeView(getActivity().findViewById(R.id.spiner_empleados));
                    label.setTextColor(Color.BLACK);
                    label.setText("No hay ningun empleado disponible");
                    label.setGravity(Gravity.CENTER);
                    label.setPadding(0,45,0,30);

                Button button= new Button(getActivity());
                button.setLayoutParams(new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                ));
                button.setGravity(Gravity.CENTER);
                button.setText("Crear empleado");
                button.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                button.setTextColor(Color.WHITE);
                rl.addView(button);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), EmpleadosActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,emp_nombres);
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            spinner_empleados.setAdapter(adapter);
            adapter.notifyDataSetChanged();


        Button registerButton =  getActivity().findViewById(R.id.finca_page_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView error_message = getActivity().findViewById(R.id.error_message);
                error_message.setText("");
                if (validateUserData()){
                    registerObjet(); //REGISTRA AL USUARIO
                    Toast.makeText(getActivity(), "Se ha registrado la finca", Toast.LENGTH_SHORT).show();
                    EditText finca_name = getActivity().findViewById(R.id.finca_name);
                    EditText finca_dir = getActivity().findViewById(R.id.finca_dir);
                        finca_name.setText("");
                        finca_dir.setText("");

                    final ViewPager fincasViewPager = getActivity().findViewById(R.id.fincasViewPager);
                    fincasViewPager.setCurrentItem(0);
                    fincasViewPager.getAdapter().notifyDataSetChanged();
                }else{
                    error_message.setText(error_ms);
                }
            }
        });
    }

    public boolean validateUserData(){
        EditText finca_name = getActivity().findViewById(R.id.finca_name);
        EditText finca_dir = getActivity().findViewById(R.id.finca_dir);
        if (finca_name.getText().toString().length()<4 || finca_dir.getText().toString().length()<4 ) {
            error_ms= "Todos los campos son obligatorios y de minimo 4 caracteres";
            return false;
        }else return true;
    }

    public void registerObjet(){

        EditText finca_name = getActivity().findViewById(R.id.finca_name);
        EditText finca_dir = getActivity().findViewById(R.id.finca_dir);
        Spinner spinner_empleado = getActivity().findViewById(R.id.spiner_empleados);


        FincasActivity fincasActivity= (FincasActivity) getActivity();
        String emailact = fincasActivity.getAct_email();
        //get the user object
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        final usuario user = realm.where(usuario.class)
                .equalTo("us_correo",emailact)
                .findFirst();
        realm.commitTransaction();

        //get the id of the selected empleado
        String selected = spinner_empleado.getSelectedItem().toString();
        String id_val = emp_ids.get(emp_nombres.indexOf(selected));
        //search the empleado object
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<empleado> results = realm.where(empleado.class)
                .equalTo("id_empleado", Integer.valueOf(id_val) )
                .findAll();
        realm.commitTransaction();
        final empleado seletected_emp = results.get(0);


        final finca finca = new finca();
        final finxemp finxemp = new finxemp();

        finca.setFin_nombre(finca_name.getText().toString());
        finca.setFin_ubicacion(finca_dir.getText().toString());
        finca.setId_usuario(user.getId_usuario());

        realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                //primary key finca
                    Number maxId = realm.where(finca.class).max("id_finca");
                    int nextId = (maxId == null) ? 1 : maxId.intValue() + 1;
                    finca.setId_finca(nextId);
                    finca.setDeleted(false);
                //create finca
                    realm.copyToRealmOrUpdate(finca);

                //primary key finxemp
                    Number maxId2 = realm.where(finxemp.class).max("id_finxemp");
                    int nextId2 = (maxId2 == null) ? 1 : maxId2.intValue() + 1;
                //create finxemp
                    finxemp.setId_finxemp(nextId2);
                    finxemp.setId_finca(nextId);
                    finxemp.setId_empleado(seletected_emp.getId_empleado());
                    finxemp.setFinxemp_estado("1");
                    realm.copyToRealmOrUpdate(finxemp);
            }
        });

        //close keyboard
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);



    }
}

