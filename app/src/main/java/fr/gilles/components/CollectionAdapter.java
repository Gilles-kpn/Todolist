package fr.gilles.components;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import fr.gilles.beans.Task;

public class CollectionAdapter extends FragmentStateAdapter {
    private TasksPage tasksPage = new TasksPage();
    private  Account account = new Account();
    private AlarmReceiver alarmReceiver = new AlarmReceiver();

    public CollectionAdapter(Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return tasksPage;
            case 1:
                return  alarmReceiver;
            case 2:
                return account;
        }
        return tasksPage;
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public void addNewTask(Task task){
        tasksPage.addNewTask(task);
    }

}
