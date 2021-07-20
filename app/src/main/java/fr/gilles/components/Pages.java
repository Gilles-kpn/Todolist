package fr.gilles.components;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import fr.gilles.beans.Task;
import fr.gilles.todolist.R;
public class Pages extends Fragment {

    private CollectionAdapter adapter;
    private ViewPager2 viewPage;
    private TabLayout tabs ;
    private TabLayoutMediator mediator;
    @Nullable
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pages,container,false);
    }

    @Override
    public void onViewCreated( View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new CollectionAdapter(this);
        viewPage = view.findViewById(R.id.viewPage);
        viewPage.setAdapter(adapter);
        tabs = view.findViewById(R.id.tabs);
        mediator = new TabLayoutMediator(tabs, viewPage,(tab,position)->{
            switch (position) {
                case 0:
                    tab.setText(R.string.tasks);
                    break;
                case 1:
                    tab.setText(R.string.alarm);
                    break;
                case 2:
                    tab.setText(R.string.accounts);
                    break;
            }

        } );
        mediator.attach();
    }
    public void setAddedTask(Task task){
        adapter.addNewTask(task);
    }

}
