package com.ddannielvega.neofinca;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.ddannielvega.neofinca.model.actividad;
import com.ddannielvega.neofinca.model.asignacion;
import com.ddannielvega.neofinca.model.usuario;

import io.realm.Realm;
import io.realm.RealmResults;


public class ActividadesFragmentTab1 extends Fragment {

    //Required parameters
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    //New attributes
    private Realm realm;
    public static final String us_correo = "us_correo";
    public static final String element_id = "element_id";

    public ActividadesFragmentTab1() {
        // Required empty public constructor
    }
    public static ActividadesFragmentTab1 newInstance(String param1, String param2) {
        ActividadesFragmentTab1 fragment = new ActividadesFragmentTab1();
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
        return inflater.inflate(R.layout.fragment_actividades_fragment_tab1, container, false);
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
        //get initial data
        getData();
    }
    public void getData( ){

        /*function summary

        * Get the actividad objects
        * Insert each object in the container with a for loop
        * If there is not results add the create view in the container

        * */
        //function parameters
            ActividadesActivity actividadesActivity = (ActividadesActivity) getActivity();
            LinearLayout containerView = getActivity().findViewById(R.id.test_lo);
            int resultCounter =0;
            final String actEmail = actividadesActivity.getAct_email();


        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
            usuario userResult = realm.where(usuario.class)
                    .equalTo(us_correo,actEmail)
                    .findFirst();
            RealmResults<actividad> actResult = realm.where(actividad.class)
                    .equalTo("id_usuario",userResult.getId_usuario())
                    .and()
                    .equalTo("deleted",false)
                    .findAll();
        realm.commitTransaction();


        for (actividad result:actResult){
            try {
                resultCounter = resultCounter+1;
                final int idElement = result.getId_actividad();
                Button actButton = new Button(getActivity());
                actButton.setLayoutParams( new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                actButton.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_mode_edit_black_24dp,0);
                actButton.setMinHeight(40);
                actButton.setTextSize(17);
                actButton.setGravity(0);
                actButton.setAllCaps(false);
                actButton.setPadding(40,60,40,60);
                actButton.setText(result.getAct_descripcion());
                actButton.setBackgroundResource(R.color.colorGray);
                containerView.addView(actButton);

                //listener to delete
                    actButton.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setMessage("Al eliminar esta actividad se eliminarán tambien todas las asignaciones asociadas a esta, ¿Estás seguro de eliminar?");
                            builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //eliminar actividad
                                    realm = Realm.getDefaultInstance();
                                    realm.beginTransaction();
                                        //change state to actividad
                                        actividad actividadDelete = realm.where(actividad.class)
                                                .and()
                                                .equalTo("id_actividad",idElement)
                                                .findFirst();
                                        actividadDelete.setDeleted(true);
                                        realm.copyToRealmOrUpdate(actividadDelete);
                                        //delete all asignaciones
                                        RealmResults<asignacion> asigRessult = realm.where(asignacion.class)
                                                .and()
                                                .equalTo("id_actividad",idElement)
                                                .findAll();
                                                for (asignacion result:asigRessult){
                                                    try {
                                                        result.setDeleted(true);
                                                        realm.copyToRealmOrUpdate(result);
                                                    }catch (NullPointerException e){
                                                        e.printStackTrace();
                                                    }
                                                }
                                    realm.commitTransaction();
                                    //update view
                                    Intent intent = new Intent(getActivity(), ActividadesActivity.class);
                                    intent.putExtra(us_correo,actEmail);
                                    startActivity(intent);
                                    getActivity().finish();

                                    Toast.makeText(getActivity(), "Se ha eliminado la actividad", Toast.LENGTH_SHORT).show();
                                }
                            });
                            builder.setNegativeButton("No", null);
                            // create and show the alert dialog
                            AlertDialog dialog = builder.create();
                            dialog.show();
                            return true;
                        }
                    });
                //listener to edit
                    actButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(), EditarActividadesActivity.class);
                            //send email
                            intent.putExtra(us_correo,actEmail);
                            intent.putExtra(element_id,String.valueOf(idElement));
                            startActivity(intent);
                        }
                    });

            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }


        if ( resultCounter < 1){

            //Swipe automatically to the create tab
            ViewPager actividadesViewPager = getActivity().findViewById(R.id.activivadesViewPager);
            actividadesViewPager.setCurrentItem(1);

            //Create view
            TextView mes = new TextView(getActivity());
            mes.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            mes.setTextColor(Color.BLACK);
            mes.setText("No has creado ninguna actividad");
            mes.setGravity(Gravity.CENTER);
            mes.setTextSize(17);
            mes.setPadding(0,100,0,100);

            //Create view
            Button button= new Button(getActivity());
            button.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            button.setGravity(Gravity.CENTER);
            button.setText("Crear actividad");
            button.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            button.setTextColor(Color.WHITE);

            //Add views
            containerView.setPadding(15,0,15,0);
            containerView.addView(mes);
            containerView.addView(button);

            //Set listener
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ViewPager actividadesViewPager = getActivity().findViewById(R.id.activivadesViewPager);
                    actividadesViewPager.setCurrentItem(1);
                }
            });
        }
        else{
            Toast.makeText(getActivity(), "Mantén presionada la actividad para eliminarla", Toast.LENGTH_SHORT).show();
        }

    }




}

