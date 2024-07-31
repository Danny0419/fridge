package com.example.fragment_test.ui.schedule;

import static com.example.fragment_test.utils.setListBackground.setListBackgroundColor;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.fragment_test.R;
import com.example.fragment_test.adapter.ScheduleAdapter;
import com.example.fragment_test.entity.Day;
import com.example.fragment_test.entity.Recipe;

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
//        for (int i = 1; i <8; i++) {
//            monday.add(new Recipe("蛋炒飯照片"+i,"蛋炒飯"+i) );
//            tuesday.add(new Recipe("煎蛋照片"+i,"煎蛋"+i) );
//        }

        schedule.put(Day.MONDAY.getDay(), monday);
        schedule.put(Day.TUESDAY.getDay(), tuesday);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        scheduleContainer = (ListView) view.findViewById(R.id.schedulesContainer);
        scheduleContainer.setAdapter(new ScheduleAdapter(inflater,schedule));

        //去除邊框(分隔線為透明色、高度為0)
        scheduleContainer.setDivider(null);
        scheduleContainer.setDividerHeight(0);

        //設定奇偶行數背景顏色
        setListBackgroundColor(scheduleContainer,requireContext());

//        // 失敗的底線
//        View viewRecipeItem = inflater.inflate(R.layout.recipe_item, container, false);
//        TextView eachDayText=viewRecipeItem.findViewById(R.id.eachDayText);
//        eachDayText.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        addToolbar();

        return view;
    }

    public void setSchedule(Map<String, ArrayList<Recipe>> schedule){
        this.schedule = schedule;
    }

    //toolbar
    private void addToolbar() {
        FragmentActivity fragmentActivity = requireActivity();
        fragmentActivity.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.schedule_toolbar, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main2);
                if (itemId == R.id.foodMenu) {
//                    navController.navigate(R.id.action_navigation_home_to_navigation_camera);
                }
                if (itemId == R.id.editdate) {
//                    navController.navigate(R.id.action_navigation_home_to_navigation_camera);
                }
                return true;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.STARTED);
    }
}