package fr.gilles.components;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import fr.gilles.alert.AlertBox;
import fr.gilles.alert.TaskActionDialog;
import fr.gilles.apiconnect.ApiCall;
import fr.gilles.apiconnect.ApiResponseReader;
import fr.gilles.application.Session;
import fr.gilles.beans.Task;
import fr.gilles.beans.User;
import fr.gilles.todolist.R;

public class TasksPage extends Fragment {
    private User user ;
    private ListView tasksListView;
    private ArrayList<Task> tasksList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tasks_list, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tasksListView = view.findViewById(R.id.tasksview);
        tasksList = new ArrayList<>();
        TaskActionDialog.init(this.getContext(),(result)->{
            if (TaskActionDialog.getInstance().getResult()){
                int actionResult = TaskActionDialog.getInstance().getAction();

                switch (actionResult){
                    case 0:{//done
                        ((TaskViewAdapter)tasksListView.getAdapter()).setDone(TaskActionDialog.getInstance().getTaskId(),true);
                        ((TaskViewAdapter)tasksListView.getAdapter()).notifyDataSetChanged();
                        break;
                    }
                    case 1:{//undone
                        ((TaskViewAdapter)tasksListView.getAdapter()).setDone(TaskActionDialog.getInstance().getTaskId(),false);
                        ((TaskViewAdapter)tasksListView.getAdapter()).notifyDataSetChanged();
                        break;
                    }
                    case 2:{//update
                        AlertBox.show("Task mis a jour", this.getContext());
                        ((TaskViewAdapter)tasksListView.getAdapter()).notifyDataSetChanged();
                        break;
                    }
                    case 3:{//alarm
                        AlertBox.show("Nouvelle alarme ajoutee", this.getContext());
                        break;
                    }
                    case 4:{//delete
                        ((TaskViewAdapter)tasksListView.getAdapter()).remove(TaskActionDialog.getInstance().getTaskId());
                        ((TaskViewAdapter)tasksListView.getAdapter()).notifyDataSetChanged();
                        break;
                    }

                }

            }

        });
        loadUser();
        loadTasks();
        initListViewItemHold();
    }


    private void loadUser(){
        try {
            user  = Session.getInstance().getUserFromSession();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadTasks(){
        tasksList.clear();
        ApiCall.getInstance().getUserTasks(user.getId(), response->{
            JSONArray tasks = (JSONArray) ApiResponseReader.readResponse(response,this.getContext());
            if (tasks != null)
                for (int i = 0; i < tasks.length(); i++) {
                    try {
                        tasksList.add(new Task(tasks.getJSONObject(i)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    tasksListView.setAdapter(new TaskViewAdapter(this.getContext(),tasksList));
                }
            else
                AlertBox.show("Vous n'avez encore aucune tache",this.getContext());
        });
    }

    private void initListViewItemHold(){
        tasksListView.setOnItemLongClickListener((adapter,view,pos,time)->{
            Task choose = (Task)tasksListView.getAdapter().getItem(pos);
            TaskActionDialog.getInstance().setTask(choose);
            TaskActionDialog.getInstance().show();
            return true;
        });
    }

    public void addNewTask(Task task){
        ((TaskViewAdapter)tasksListView.getAdapter()).addNewTask(task);
        ((TaskViewAdapter)tasksListView.getAdapter()).notifyDataSetChanged();
    }



}
