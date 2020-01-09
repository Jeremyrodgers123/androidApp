package com.main.project1;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class DashboardFragment extends Fragment {

    private AppViewModel mAppViewModel;

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        //Create the view model
        mAppViewModel = new ViewModelProvider(this).get(AppViewModel.class);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        //Get the recycler view
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.dashboard_rv);

        //Tell Android that we know the size of the recyclerview doesn't change
        recyclerView.setHasFixedSize(true);

        //Set the layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        //Set the adapter
        DashboardAdapter dashboardAdapter = new DashboardAdapter(mAppViewModel.getDashboardItems());
        recyclerView.setAdapter(dashboardAdapter);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
