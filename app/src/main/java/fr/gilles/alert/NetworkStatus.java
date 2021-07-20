package fr.gilles.alert;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;


import fr.gilles.todolist.R;

public class NetworkStatus extends Dialog {

    public NetworkStatus(Context context) {
        super(context);
        setContentView(R.layout.dialog_network_status);
        Button close = findViewById(R.id.closeNetworkStatus);
        close.setOnClickListener((click)->{
            this.dismiss();
        });
    }

    public NetworkStatus(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected NetworkStatus(Context context, boolean cancelable,  OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

}
