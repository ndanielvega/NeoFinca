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


import com.ddannielvega.neofinca.model.asignacion;
import com.ddannielvega.neofinca.model.finca;
import com.ddannielvega.neofinca.model.finxemp;
import com.ddannielvega.neofinca.model.usuario;

import io.realm.Realm;
import io.realm.RealmResults;


public class FincasFragmentTab1 extends Fragment {

    //Required attributes
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private OnFragmentInteractionListener mListener;
    private String mParam1;
    private String mParam2;

    //New attributes
    public Realm realm;
    public static final String us_correo = "us_correo";
    public static final String element_id = "element_id";


    public FincasFragmentTab1() {
        // Required empty public constructor
    }
    public static FincasFragmentTab1 newInstance(String param1, String param2) {
        FincasFragmentTab1 fragment = new FincasFragmentTab1();
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
        return inflater.inflate(R.layout.fragment_fincas_fragment_tab1, container, false);
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
        void onFragmentInteraction(Uri uri);
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //get initial data
        getData();
    }


    public void getData( ){

        final FincasActivity fincasActivity= (FincasActivity) getActivity();
        final String emailact = fincasActivity.getAct_email();
        int resultCounter =0;
        LinearLayout linearLayout = getActivity().findViewById(R.id.test_lo);

        //get the user object
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
            usuario resultso = realm.where(usuario.class)
                    .equalTo("us_correo",emailact)
                    .findFirst();

            RealmResults<finca> results = realm.where(finca.class)
                    .equalTo("id_usuario",resultso.getId_usuario())
                    .and()
                    .equalTo("deleted",false)
                    .findAll();
        realm.commitTransaction();


        for (finca result:results){
            try {
                final int id_element = result.getId_finca();
                resultCounter = resultCounter+1;

                Button finButton = new Button(getActivity());
                finButton.setLayoutParams( new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                finButton.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_mode_edit_black_24dp,0);
                finButton.setAllCaps(false);
                finButton.setMinHeight(40);
                finButton.setTextSize(19);
                finButton.setGravity(0);
                finButton.setPadding(40,60,40,60);
                finButton.setText(result.getFin_nombre());
                finButton.setBackgroundResource(R.color.colorGray);
                linearLayout.addView(finButton);

                //listener delete
                finButton.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Al eliminar esta finca se eliminaran tambien todas las asignaciones, ¿Estás seguro de eliminar?");
                        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                realm = Realm.getDefaultInstance();
                                realm.beginTransaction();
                                    //elimina la finca
                                    finca finca_del = realm.where(finca.class)
                                            .and()
                                            .equalTo("id_finca",id_element)
                                            .findFirst();
                                    finca_del.setDeleted(true);
                                    realm.copyToRealmOrUpdate(finca_del);

                                    //elimina las asignaciones con este empleado
                                    RealmResults<asignacion> asig_ressult = realm.where(asignacion.class)
                                            .and()
                                            .equalTo("id_finca",id_element)
                                            .findAll();
                                    for (asignacion result:asig_ressult){
                                        try {
                                            result.setDeleted(true);
                                            realm.copyToRealmOrUpdate(result);
                                        }catch (NullPointerException e){
                                            e.printStackTrace();
                                        }
                                    }
                                    //Cambia el estado de todos los empleados asociados a esta finca
                                    RealmResults<finxemp> resultsfxe = realm.where(finxemp.class)
                                            .and()
                                            .equalTo("id_finca",id_element)
                                            .findAll();
                                    for (finxemp res:resultsfxe){
                                        try {
                                            res.setFinxemp_estado("0");
                                            realm.copyToRealmOrUpdate(res);
                                        }
                                        catch (NullPointerException e){
                                            e.printStackTrace();
                                        }
                                    }
                                realm.commitTransaction();

                                //update view
                                Intent intent = new Intent(getActivity(), FincasActivity.class);
                                intent.putExtra(us_correo,emailact);
                                startActivity(intent);
                                getActivity().finish();

                                Toast.makeText(getActivity(), "Se ha eliminado la finca", Toast.LENGTH_SHORT).show();
                            }
                        });
                        builder.setNegativeButton("No", null);
                        // create and show the alert dialog
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        return true;
                    }
                });
                //listener edit
                finButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), EditarFincasActivity.class);
                        //send email
                        intent.putExtra(element_id,String.valueOf(id_element));
                        FincasActivity fincasActivity  = (FincasActivity) getActivity();
                        intent.putExtra(us_correo,fincasActivity.getAct_email());
                        startActivity(intent);
                        getActivity().finish();

                    }
                });
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }



        if ( resultCounter < 1){

            //Swipe automatically to the create tab
            ViewPager fincasViewPager = getActivity().findViewById(R.id.fincasViewPager);
            fincasViewPager.setCurrentItem(1);

            //Create view
            TextView mes = new TextView(getActivity());
            mes.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            mes.setTextColor(Color.BLACK);
            mes.setText("No has creado ninguna finca");
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
            button.setText("Crear finca");
            button.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            button.setTextColor(Color.WHITE);

            //Add views
            LinearLayout ll = getActivity().findViewById(R.id.fincas_data);
            ll.setPadding(15,0,15,0);
            ll.addView(mes);
            ll.addView(button);

            //Create listener
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ViewPager fincasViewPager = getActivity().findViewById(R.id.fincasViewPager);
                    fincasViewPager.setCurrentItem(1);
                }
            });
        }
        else{
            Toast.makeText(getActivity(), "Mantén presionada la finca para eliminarla", Toast.LENGTH_SHORT).show();
        }
    }
}