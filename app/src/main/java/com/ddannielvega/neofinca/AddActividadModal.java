package com.ddannielvega.neofinca;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ddannielvega.neofinca.model.actividad;
import com.ddannielvega.neofinca.model.usuario;

import io.realm.Realm;
import io.realm.RealmResults;


public class AddActividadModal extends AppCompatDialogFragment {

    private ModalListener listener;
    Realm realm;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder nBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView  = inflater.inflate(R.layout.add_actividad_modal,null);
        nBuilder.setView(dialogView);
        final AlertDialog dialog = nBuilder.create();

        //get email from user
            AsignacionesActivity asigActivity= (AsignacionesActivity) getActivity();
            final String emailact = asigActivity.getAct_email();


        //set listener for create actividad button
            dialogView.findViewById(R.id.modal_create_actividad).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent2 = new Intent(getActivity(), ActividadesActivity.class);
                    intent2.putExtra("us_correo",emailact);
                    startActivity(intent2);
                    getActivity().finish();
                }
            });

        //get all actividades from user and show the results
            realm = Realm.getDefaultInstance();
            realm.beginTransaction();
                usuario user = realm.where(usuario.class)
                        .equalTo("us_correo",emailact)
                        .findFirst();
                final RealmResults<actividad> results = realm.where(actividad.class)
                        .equalTo("deleted",false)
                        .and()
                        .equalTo("id_usuario",user.getId_usuario())
                        .findAll();
            realm.commitTransaction();

        if (results.size()>0){
            //insert views
                LinearLayout layout_container = dialogView.findViewById(R.id.layout_container);
                layout_container.setPadding(10,10,10,10);
                for (final actividad result:results){

                    //Create view
                        TextView newTV= new TextView(getActivity());
                        newTV.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        ));
                        newTV.setPadding(0,30,0,30);
                        newTV.setGravity(Gravity.CENTER);
                        newTV.setText(result.getAct_descripcion());
                        newTV.setBackgroundColor(getResources().getColor(R.color.colorGray));
                        newTV.setTextColor(Color.BLACK);
                        newTV.setTextSize(16);
                        TextView space = new TextView(getActivity());
                        space.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                3
                        ));
                    //Create Listener
                        newTV.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String name = result.getAct_descripcion();
                                listener.passText(name);
                                dialog.dismiss();

                            }
                        });

                    //Add views
                        layout_container.addView(newTV);
                        layout_container.addView(space);
                }
        }
        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (ModalListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "Implementar ModalListener");
        }
    }

    public interface ModalListener{
        void passText(String actividadName );
    }
}
