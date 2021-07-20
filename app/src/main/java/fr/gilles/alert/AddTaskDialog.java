package fr.gilles.alert;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Build;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.RequiresApi;

import org.json.JSONException;
import org.json.JSONObject;

import fr.gilles.apiconnect.ApiCall;
import fr.gilles.apiconnect.ApiResponseReader;
import fr.gilles.beans.Task;
import fr.gilles.todolist.R;

public class AddTaskDialog extends Dialog {

    private static AddTaskDialog instance;
    private DatePickerDialog datePicker;
    private TimePickerDialog timePicker;
    private int userId;
    private Task taskAdded;
    private JSONObject task ;

    @RequiresApi(api = Build.VERSION_CODES.N)
    private AddTaskDialog(Context context) {
        super(context);
        this.setContentView(R.layout.dialog_add_task);
        Button cancel = findViewById(R.id.cancel);
        Button confirm = findViewById(R.id.confirm);
        cancel.setOnClickListener((r)->{
            cancelTaskAdd();
        });
        confirm.setOnClickListener((r)->{
            try {
                addTask();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        datePicker = new DatePickerDialog(this.getContext());
        initDateTimeChooser();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initDateTimeChooser(){
        EditText timeText = findViewById(R.id.addTaskTime);
        EditText dateText = findViewById(R.id.addTaskDate);
        //

        datePicker.setOnDateSetListener((a,year,month,day)->{
            dateText.setText(year+"-"+month+"-"+day);
        });
        timePicker = new TimePickerDialog(this.getContext(), (picker,hour,min)->{
            timeText.setText(hour + ":" + min+":00" );
        },0, 0,true);

        dateText.setClickable(true);
        timeText.setClickable(true);

        dateText.setOnFocusChangeListener((a,b)->{
            if (b) datePicker.show();
        });
        timeText.setOnFocusChangeListener((a,b)->{
            if (b) timePicker.show();
        });

    }

    private AddTaskDialog( Context context, int themeResId) {
        super(context, themeResId);
    }

    private AddTaskDialog( Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    private void addTask() throws JSONException {
        loadProperty();
        ApiCall.getInstance().addTask(userId,task,response -> {
            JSONObject result = (JSONObject) ApiResponseReader.readResponse(response,this.getContext());
            if (result != null){
                AlertBox.show("Nouvelle tache ajoutee", this.getContext());
                taskAdded = new Task(result);
                dismiss();
            }
        });
    }

    private void  loadProperty() throws JSONException {
        task = new JSONObject();
        EditText taskName = findViewById(R.id.addTaskName);
        EditText taskDate = findViewById(R.id.addTaskDate);
        EditText taskTime = findViewById(R.id.addTaskTime);
        EditText taskContent = findViewById(R.id.addTaskContent);
        EditText taskDuration = findViewById(R.id.addTaskDuration);

        task.accumulate("duration",Integer.parseInt(taskDuration.getText().toString()));
        task.accumulate("title",taskName.getText().toString());
        task.accumulate("content",taskContent.getText().toString());
        task.accumulate("date",taskDate.getText().toString()+" "+taskTime.getText().toString());
    }

    public  void cancelTaskAdd()
    {
        this.cancel();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void init(Context context, int userId){
        instance = new AddTaskDialog(context);
        instance.userId = userId;
    }

    public Task getAddedTask (){
        return taskAdded;
    }


    public static  AddTaskDialog getInstance(){
        return instance;
    }

}
