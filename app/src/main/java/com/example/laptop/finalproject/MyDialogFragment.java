package com.example.laptop.finalproject;



import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

/**
 * Created by Laptop on 04-Feb-17.
 */

public class MyDialogFragment extends DialogFragment{

boolean agree;
Bundle bundle;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
           try{
               bundle = getArguments();
               agree = bundle.getBoolean("Agree");
           }catch (Exception e){
               e.printStackTrace();
           }

                if (!agree) {

                    builder.setMessage(R.string.not_available)
                            .setNeutralButton(R.string.ok_button, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    return builder.create();

                }else {

                    builder.setMessage(R.string.show_explanation)
                            .setNeutralButton(R.string.ok_button, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    return builder.create();
                }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {

        if (!agree) {


        } else {
            if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            }
        }
    }
}
