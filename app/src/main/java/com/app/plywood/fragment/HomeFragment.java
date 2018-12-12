package com.app.plywood.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.plywood.R;
import com.app.plywood.adapter.DashboardAdapter;
import com.app.plywood.data.DashboardMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    RecyclerView rvDashboard;
    List<DashboardMenu> dashList;
    DashboardAdapter dashboardAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        dashList = new ArrayList<>();
        dashboardAdapter = new DashboardAdapter(getActivity(), dashList);
        rvDashboard = view.findViewById(R.id.rv_dashboard);
        mLayoutManager = new GridLayoutManager(getActivity(), 2);
        rvDashboard.setLayoutManager(mLayoutManager);
        rvDashboard.setAdapter(dashboardAdapter);
        getMenu();

        return view;
    }

    private void getMenu() {

        int [] icons = new int[]{

                R.drawable.ic_product,
                R.drawable.ic_sale,
                R.drawable.ic_purchase,
                R.drawable.ic_customer,
                R.drawable.ic_vendor,
                R.drawable.ic_report

        };
        DashboardMenu menu = new DashboardMenu("Products", icons[0]);
        dashList.add(menu);
        menu = new DashboardMenu("Sales", icons[1]);
        dashList.add(menu);
        menu = new DashboardMenu("Purchase", icons[2]);
        dashList.add(menu);
        menu = new DashboardMenu("Customers", icons[3]);
        dashList.add(menu);
        menu = new DashboardMenu("Vendors", icons[4]);
        dashList.add(menu);
        menu = new DashboardMenu("Reports", icons[5]);
        dashList.add(menu);

        dashboardAdapter.notifyDataSetChanged();
    }

}
