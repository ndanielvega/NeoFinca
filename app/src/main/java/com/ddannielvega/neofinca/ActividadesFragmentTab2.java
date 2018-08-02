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

import com.ddannielvega.neofinca.model.actividad;
import com.ddannielvega.neofinca.model.usuario;

import io.realm.Realm;


public class ActividadesFragmentTab2 extends Fragment {

    //Required parameters
    private OnFragmentInteractionListener mListener;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    //New attributes
    public Realm realm;
    public String error_ms;
    public EditText description;
    public Button registerButton;


    public ActividadesFragmentTab2() {
        // Required empty public constructor
    }

    public static ActividadesFragmentTab2 newInstance(String param1, String param2) {
        ActividadesFragmentTab2 fragment = new ActividadesFragmentTab2();
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
        return inflater.inflate(R.layout.fragment_actividades_fragment_tab2, container, false);
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

        //initialize views
            registerButton =  getActivity().findViewById(R.id.activity_page_button);
            description = getActivity().findViewById(R.id.acvtivity_description);

        //create listener to create object
            registerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    TextView error_message = getActivity().findViewById(R.id.error_message);
                    error_message.setText("");
                    if (validateUserData()){
                        //if data is valid register object
                        registerObject();
                        Toast.makeText(getActivity(), "Se ha registrado la actividad", Toast.LENGTH_SHORT).show();
                        //clear inputs
                        EditText description = getActivity().findViewById(R.id.acvtivity_description);
                        description.setText("");
                        //swipe to the other fragment
                        ViewPager actividadesViewPager = getActivity().findViewById(R.id.activivadesViewPager);
                        actividadesViewPager.setCurrentItem(0);
                        actividadesViewPager.getAdapter().notifyDataSetChanged();
                    }else{
                        //show the error
                        error_message.setText(error_ms);
                    }

                }
            });
    }

    public boolean validateUserData(){

        //get input data to validate
        if (description.getText().toString().length()<4 ) {
            error_ms= "Todos los campos son obligatorios y de minimo 4 caracteres";
            return false;
        }else return true;
    }

    public void registerObject(){

        //get the user object
        ActividadesActivity actividadesActivity= (ActividadesActivity) getActivity();
        String emailact = actividadesActivity.getAct_email();
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
            final usuario user = realm.where(usuario.class)
                    .equalTo("us_correo",emailact)
                    .findFirst();
        realm.commitTransaction();

        //create new object
        final actividad actividad = new actividad();
        actividad.setAct_descripcion( description.getText().toString());
        actividad.setId_usuario(user.getId_usuario());
        realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                //primary key
                Number maxId = realm.where(actividad.class).max("id_actividad");
                int nextId = (maxId == null) ? 1 : maxId.intValue() + 1;
                actividad.setId_actividad(nextId);
                actividad.setDeleted(false);
                realm.copyToRealmOrUpdate(actividad);
            }
        });

        //close keyboard
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

    }
}
