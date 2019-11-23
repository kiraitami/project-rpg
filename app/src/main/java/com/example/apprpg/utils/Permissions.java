package com.example.apprpg.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Permissions {
    public static boolean validattePermissions(String[] permissions, Activity activity, int requestCode){

        if (Build.VERSION.SDK_INT >= 23 ){

            List<String> permissionsList = new ArrayList<>();

            /*Percorre as permissões passadas,
            verificando uma a uma
            * se já tem a permissao liberada */
            for ( String permission : permissions ){
                boolean hasPermission = ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
                if ( !hasPermission ) permissionsList.add(permission);
            }

            /*Caso a lista esteja vazia, não é necessário solicitar permissão*/
            if ( permissionsList.isEmpty() ) return true;
            String[] newPermissions = new String[ permissionsList.size() ];
            permissionsList.toArray( newPermissions );

            //Solicita permissão
            ActivityCompat.requestPermissions(activity, newPermissions, requestCode );


        }

        return true;

    }
}
