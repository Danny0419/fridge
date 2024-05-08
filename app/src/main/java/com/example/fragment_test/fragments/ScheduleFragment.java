package com.example.fragment_test.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.fragment_test.R;
import com.example.fragment_test.adapter.ScheduleAdapter;
import com.example.fragment_test.pojo.Day;
import com.example.fragment_test.pojo.Recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScheduleFragment extends Fragment {

    private Map<String, ArrayList<Recipe>> schedule = new HashMap<>();
    private ListView scheduleContainer;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ScheduleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScheduleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScheduleFragment newInstance(String param1, String param2) {
        ScheduleFragment fragment = new ScheduleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        ArrayList<Recipe> monday = new ArrayList<>();
        ArrayList<Recipe> tuesday = new ArrayList<>();
        for (int i = 1; i <8; i++) {
            monday.add(new Recipe("蛋炒飯照片"+i,"蛋炒飯"+i) );
            tuesday.add(new Recipe("煎蛋照片"+i,"煎蛋"+i) );
        }

        schedule.put(Day.MONDAY.getDay(), monday);
        schedule.put(Day.TUESDAY.getDay(), tuesday);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        scheduleContainer = (ListView) view.findViewById(R.id.schedulesContainer);
        scheduleContainer.setAdapter(new ScheduleAdapter(inflater,schedule));
        return view;
    }

    public void setSchedule(Map<String, ArrayList<Recipe>> schedule){
        this.schedule = schedule;
    }
}