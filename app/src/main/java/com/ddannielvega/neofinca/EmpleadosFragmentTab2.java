package com.ddannielvega.neofinca;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ddannielvega.neofinca.model.empleado;
import com.ddannielvega.neofinca.model.usuario;

import io.realm.Realm;


public class EmpleadosFragmentTab2 extends Fragment {

    //Required attributes
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private OnFragmentInteractionListener mListener;
    private String mParam1;
    private String mParam2;

    //New Attributes
    public Realm realm;
    public String error_ms;


    public EmpleadosFragmentTab2() {
        // Required empty public constructor
    }
    public static EmpleadosFragmentTab2 newInstance(String param1, String param2) {
        EmpleadosFragmentTab2 fragment = new EmpleadosFragmentTab2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        return inflater.inflate(R.layout.fragment_empleados_fragment_tab2, container, false);
    }
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button registerButton =  getActivity().findViewById(R.id.empleado_page_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView error_message = getActivity().findViewById(R.id.error_message);
                error_message.setText("");
                if (validateUserData()){
                    registerObject();
                    Toast.makeText(getActivity(), "Se ha registrado el empleado", Toast.LENGTH_SHORT).show();

                    EditText emp_name = getActivity().findViewById(R.id.empleado_name);
                    EditText emp_doc = getActivity().findViewById(R.id.empleado_doc);
                    EditText emp_val = getActivity().findViewById(R.id.empleado_valorxjornal);
                    EditText emp_tel = getActivity().findViewById(R.id.empleado_tel);

                    emp_name.setText("");
                    emp_doc.setText("");
                    emp_tel.setText("");
                    emp_val.setText("");


                    ViewPager empleadosViewPager = getActivity().findViewById(R.id.empleadosViewPager);
                    empleadosViewPager.setCurrentItem(0);
                    empleadosViewPager.getAdapter().notifyDataSetChanged();

                }else{
                    error_message.setText(error_ms);
                }

            }
        });
    }

    public boolean validateUserData(){

        EditText emp_name = getActivity().findViewById(R.id.empleado_name);
        EditText emp_doc = getActivity().findViewById(R.id.empleado_doc);
        EditText emp_val = getActivity().findViewById(R.id.empleado_valorxjornal);
        EditText emp_tel = getActivity().findViewById(R.id.empleado_tel);

        if (emp_name.getText().toString().length()<4 || emp_doc.getText().toString().length()<4 || emp_val.getText().toString().length()<4 ) {
            error_ms= "Todos los campos son obligatorios y de mínimo 4 caracteres.";
            return false;
        }else {
            if (emp_tel.getText().toString().length()<10){
                error_ms= "El número telefónico no es válido, debe ser de 10 caracteres. ";
                return false;
            }else return true;

        }
    }

    public void registerObject(){

        EditText emp_name = getActivity().findViewById(R.id.empleado_name);
        EditText emp_doc = getActivity().findViewById(R.id.empleado_doc);
        EditText emp_val = getActivity().findViewById(R.id.empleado_valorxjornal);
        EditText emp_tel = getActivity().findViewById(R.id.empleado_tel);


        EmpleadosActivity empleadosActivity= (EmpleadosActivity) getActivity();
        String emailact = empleadosActivity.getAct_email();
        //get the user object
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
            final usuario user = realm.where(usuario.class)
                    .equalTo("us_correo",emailact)
                    .findFirst();
        realm.commitTransaction();
        final empleado empleado = new empleado();

        empleado.setEmp_nombre(emp_name.getText().toString());
        empleado.setEmp_documento(emp_doc.getText().toString());
        empleado.setEmp_valorxjornal(Integer.parseInt(emp_val.getText().toString()));
        empleado.setEmp_telefono(emp_tel.getText().toString());
        empleado.setId_usuario(user.getId_usuario());

        realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
            //primary key
            Number maxId = realm.where(empleado.class).max("id_empleado");
            int nextId = (maxId == null) ? 1 : maxId.intValue() + 1;
            empleado.setId_empleado(nextId);
            empleado.setDeleted(false);
            realm.copyToRealmOrUpdate(empleado);
            }
        });

        //close keyboard
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);



    }
}
