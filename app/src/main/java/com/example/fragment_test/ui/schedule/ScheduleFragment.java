package com.example.fragment_test.ui.schedule;

import static com.example.fragment_test.utils.setListBackground.setListBackgroundColor;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.fragment_test.R;
import com.example.fragment_test.adapter.ScheduleAdapter;
import com.example.fragment_test.entity.Recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScheduleFragment extends Fragment {

    private Map<Integer, List<Recipe>> schedule = new HashMap<>();
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

    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        scheduleContainer = view.findViewById(R.id.schedulesContainer);
        scheduleContainer.setAdapter(new ScheduleAdapter(getResources().getStringArray(R.array.day_of_week), schedule, inflater));

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
                    showDialog();
                }
                return true;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.STARTED);
    }

    //彈跳式窗
    private void showDialog(){
        //彈跳頁面
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.schedule_change_display_dialog);

        //取消與確認按鈕
        Button cancelButton = dialog.findViewById(R.id.cancel_button);
        Button confirmButton = dialog.findViewById(R.id.confirm_button);

        // 取消
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // 確認
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}