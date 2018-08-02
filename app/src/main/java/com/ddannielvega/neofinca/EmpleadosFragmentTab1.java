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
import com.ddannielvega.neofinca.model.empleado;
import com.ddannielvega.neofinca.model.finxemp;
import com.ddannielvega.neofinca.model.usuario;

import io.realm.Realm;
import io.realm.RealmResults;


public class EmpleadosFragmentTab1 extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    //New Attributes
    private Realm realm;
    public static final String us_correo = "us_correo";
    public static final String element_id = "element_id";

    public EmpleadosFragmentTab1() {
        // Required empty public constructor
    }
    public static EmpleadosFragmentTab1 newInstance(String param1, String param2) {
        EmpleadosFragmentTab1 fragment = new EmpleadosFragmentTab1();
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
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_empleados_fragment_tab1, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //get initial data
        getData();
    }


    public void getData(){


        /*function summary

         * Get the empleados objects
         * Insert each object in the container with a for loop
         * If there is not results add the create view in the container

         * */

        int resultCounter =0;
        LinearLayout linearLayout = getActivity().findViewById(R.id.test_lo);
        final EmpleadosActivity empleadosActivity= (EmpleadosActivity) getActivity();
        final String emailact = empleadosActivity.getAct_email();

        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        usuario userResult = realm.where(usuario.class)
                    .equalTo("us_correo",emailact)
                    .findFirst();

        RealmResults<empleado> empResults = realm.where(empleado.class)
                    .equalTo("id_usuario",userResult.getId_usuario())
                    .and()
                    .equalTo("deleted",false)
                    .findAll();
        realm.commitTransaction();


        for (empleado result:empResults){
            try {
                resultCounter = resultCounter+1;
                final int id_element = result.getId_empleado();

                //Create view
                Button empButton = new Button(getActivity());
                empButton.setLayoutParams( new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                empButton.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_mode_edit_black_24dp,0);
                empButton.setMinHeight(40);
                empButton.setTextSize(17);
                empButton.setGravity(0);
                empButton.setAllCaps(false);
                empButton.setPadding(40,60,40,60);
                empButton.setText(result.getEmp_nombre());
                empButton.setBackgroundResource(R.color.colorGray);
                linearLayout.addView(empButton);

                //listener delete
                empButton.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Al eliminar este empleado se eliminaran las asignaciones hechas a este ¿Estás seguro de eliminar?");
                        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                //if there is a empleado working on a finca it can't be deleted
                                boolean posible = false;
                                realm = Realm.getDefaultInstance();
                                realm.beginTransaction();
                                RealmResults<finxemp> resultsfxe = realm.where(finxemp.class)
                                        .and()
                                        .equalTo("id_empleado",id_element)
                                        .findAll();
                                realm.commitTransaction();

                                if (resultsfxe.size()<1){
                                    posible=true;
                                }
                                for (finxemp res:resultsfxe){
                                    try {
                                        //If the empleado is not active in any other finca, he can eliminate it
                                        if ( res.getFinxemp_estado().equals("0") ){
                                            posible=true;
                                        }
                                    }
                                    catch (NullPointerException e){
                                        e.printStackTrace();
                                    }
                                }
                                if (posible){
                                    realm = Realm.getDefaultInstance();
                                    realm.beginTransaction();
                                        //delete empleado
                                        empleado empleado_del = realm.where(empleado.class)
                                                .and()
                                                .equalTo("id_empleado",id_element)
                                                .findFirst();
                                        empleado_del.setDeleted(true);
                                        realm.copyToRealmOrUpdate(empleado_del);
                                        //delete asignaciones
                                        RealmResults<asignacion> asig_ressult = realm.where(asignacion.class)
                                                .and()
                                                .equalTo("id_empleado",id_element)
                                                .findAll();
                                        for (asignacion result:asig_ressult){
                                            try {
                                                result.setDeleted(true);
                                                realm.copyToRealmOrUpdate(result);
                                            }catch (NullPointerException e){
                                                e.printStackTrace();
                                            }
                                        }
                                    realm.commitTransaction();
                                    //update view
                                    Intent intent = new Intent(getActivity(), EmpleadosActivity.class);
                                    intent.putExtra(us_correo,emailact);
                                    startActivity(intent);
                                    getActivity().finish();

                                    Toast.makeText(getActivity(), "Se ha eliminado el empleado", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(getActivity(), "No se puede eliminar el empleado, está activo en una finca", Toast.LENGTH_SHORT).show();
                                }
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
                empButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), EditarEmpleadosActivity.class);
                        //send email
                        intent.putExtra(element_id,String.valueOf(id_element));
                        intent.putExtra(us_correo,emailact);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });

            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }


        if (resultCounter < 1){

            //Swipe automatically to the create tab
            ViewPager empleadosViewPager = getActivity().findViewById(R.id.empleadosViewPager);
            empleadosViewPager.setCurrentItem(1);

            //Create view
            TextView mes = new TextView(getActivity());
            mes.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            mes.setTextColor(Color.BLACK);
            mes.setText("No has creado ningun empleado");
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
            button.setText("Crear empleado");
            button.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            button.setTextColor(Color.WHITE);

            //Add views
            LinearLayout ll = getActivity().findViewById(R.id.empleados_data);
            ll.setPadding(15,0,15,0);
            ll.addView(mes);
            ll.addView(button);

            //Add listener
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ViewPager empleadosViewPager = getActivity().findViewById(R.id.empleadosViewPager);
                    empleadosViewPager.setCurrentItem(1);
                }
            });
        }
        else{
            Toast.makeText(getActivity(), "Mantén presionado el empleado para eliminarlo", Toast.LENGTH_SHORT).show();
        }

    }

}