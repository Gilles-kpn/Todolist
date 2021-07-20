package fr.gilles.alert;

import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;

public class AlertBox  {

    private static AlertDialog dialog;
    public static void show(String data, Context context){
        dialog = new AlertDialog.Builder(context) // Pass a reference to your main activity here
                .setMessage(data)
                .setPositiveButton("OK", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        dialog.cancel();
                    }
                })
                .show();
    }
}
