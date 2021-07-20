package fr.gilles.alert;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.os.Build;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import fr.gilles.apiconnect.ApiCall;
import fr.gilles.apiconnect.ApiResponseReader;
import fr.gilles.beans.Task;
import fr.gilles.components.AlarmReceiver;
import fr.gilles.todolist.R;

public class TaskActionDialog extends Dialog {
    private Task task;
    private final  String CHANNEL_ID = "TodoList" ;
    private static TaskActionDialog instance;
    private boolean dialogResult;
    private NotificationManager notificationManager;
    private int notificationID = 0;
    private int action;
    public TaskActionDialog(@NonNull Context context, DialogInterface.OnDismissListener listener) {
        super(context);
        this.setContentView(R.layout.dialog_task_option);
        this.setOnDismissListener(listener);
        createNotificationChannel();
    }

    public TaskActionDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected TaskActionDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    private void initAction(){
        Button markDone = findViewById(R.id.markAsDone);
        Button markUnDone = findViewById(R.id.markAsUnDone);
        Button update = findViewById(R.id.updateTask);
        Button setAsAlarm = findViewById(R.id.defineAlarm);
        Button delete = findViewById(R.id.deleteTask);
        markDone.setEnabled(true);
        markUnDone.setEnabled(true);
        if (task.isDone())markDone.setEnabled(false);
        else markUnDone.setEnabled(false);
        markDone.setOnClickListener((click)->markAsDone());
        markUnDone.setOnClickListener((click)-> makeAsUnDone());
        update.setOnClickListener((click)->updateTask());
        setAsAlarm.setOnClickListener((click)->defineAsAlarm());
        delete.setOnClickListener((click)->delete());
    }

    private void makeAsUnDone(){
        action = 1;
        ApiCall.getInstance().undoneTask(task.getId(),(response)->{
            String result = (String)ApiResponseReader.readResponse(response,this.getContext());
            if (result != null){
                AlertBox.show(result,this.getContext());
                dialogResult = true;
            }else{
                dialogResult = false;
            }
            dismiss();
        });

    }

    private void markAsDone(){
        action = 0 ;
        ApiCall.getInstance().doneTask(task.getId(),(response)->{
            String result = (String) ApiResponseReader.readResponse(response,this.getContext());
            if (result != null){
                AlertBox.show(result,this.getContext());
                dialogResult = true;
            }else{
                dialogResult = false;
            }
            dismiss();
        });

    }

    public int getTaskId(){
        return task.getId();
    }

    private void updateTask(){
        action = 2;
        dismiss();
    }

    private void delete(){
        action = 4;
        ApiCall.getInstance().deleteTask(task.getId(),(response)->{
            String result  = (String) ApiResponseReader.readResponse(response,this.getContext());
            if (result != null){
                AlertBox.show(result,this.getContext());
                dialogResult = true;
            }else{
                dialogResult = false;
            }
            dismiss();
        });

    }

    private  void defineAsAlarm(){
        action = 3;
        AlarmManager alarm = (AlarmManager) this.getContext().getSystemService(Context.ALARM_SERVICE);
        alarm.set(AlarmManager.RTC_WAKEUP,task.getDate().getTime(), PendingIntent.getBroadcast(this.getContext(), 0, new Intent(this.getContext(), AlarmReceiver.class), 0));
        makeNotification();
        dismiss();
    }

    public void setTask(Task task){
        this.task = task;
        instance.initAction();
    }

    public boolean getResult(){
        return  dialogResult;
    }

    public int getAction(){
        return  action;
    }

    public  static  void init(Context context,DialogInterface.OnDismissListener listener){
        instance = new TaskActionDialog(context,listener);
        instance.setTitle("Actions");
    }


    public static  TaskActionDialog getInstance(){
        return instance;
    }


    private void makeNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this.getContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.list)
                .setContentTitle("Nouvelle alarme definie")
                .setContentText("La tache "+task.getTitle()+" a ete programmee pour le "+task.getDate())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        notificationManager.notify(notificationID, builder.build());
        notificationID++;
    }

    private void createNotificationChannel(){
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = this.getContext().getString(R.string.channel_name);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(this.getContext().getString(R.string.channel_description));
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager = this.getContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
    }
}
