package fr.gilles.components;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import fr.gilles.beans.Task;
import fr.gilles.todolist.R;

public class TaskViewAdapter extends BaseAdapter {
    private ArrayList<Task> tasks;
    private LayoutInflater inflater;

    public TaskViewAdapter(Context context,ArrayList<Task> tasks){
        this.tasks = tasks;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return tasks.size();
    }


    @Override
    public Task getItem(int position) {
        return tasks.get(position) ;
    }

    @Override
    public long getItemId(int position) {
        return tasks.get(position).getId();
    }

    public int getPositionFrom(int taskId){
        for (int i = 0; i< tasks.size(); i++){
            if (tasks.get(i).getId() == taskId) return i;
        }
        return  0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = inflater.inflate(R.layout.fragment_task_view,null);
        }
        //get
        TextView title = convertView.findViewById(R.id.taskTitle);
        TextView content = convertView.findViewById(R.id.taskContent);
        TextView date = convertView.findViewById(R.id.taskDate);
        CheckBox done = convertView.findViewById(R.id.taskDone);
        TextView duration = convertView.findViewById(R.id.taskDuration);

        //get task
        Task task = tasks.get(position);
        //set
        title.setText(task.getTitle());
        content.setText(task.getContent());
        date.setText(new SimpleDateFormat("dd MMM yyyy HH:mm").format(task.getDate()));

        if(task.isDone()){
            done.setText("Fait");
            done.setChecked(true);
        }else {
            done.setChecked(false);
            done.setText("Non Fait");
        }

        done.setEnabled(false);
        duration.setText(task.getDuration() + " heure(s)");
        return  convertView;
    }

    public void setDone(int taskId,boolean done){
        tasks.get(getPositionFrom(taskId)).setDone(done);
    }

    public void remove(int taskId){
        tasks.remove(getPositionFrom(taskId));
    }

    public void addNewTask(Task task){
        tasks.add(0,task);
    }
}
