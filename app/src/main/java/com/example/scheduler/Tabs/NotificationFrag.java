package com.example.scheduler.Tabs;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.scheduler.*;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotificationFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationFrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private List<NotificationItem> notificationList = new ArrayList<>();
    private AppDatabaseHelper dbHelper;
    private FloatingActionButton fab;

    public NotificationFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationFrag newInstance(String param1, String param2) {
        NotificationFrag fragment = new NotificationFrag();
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewTasks);
        fab = view.findViewById(R.id.fabAddTask);
        dbHelper = new AppDatabaseHelper(getContext());

        adapter = new NotificationAdapter(notificationList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        loadNotifications();

        fab.setOnClickListener(v -> showAddNotificationDialog());

        return view;
    }

    private void loadNotifications() {
        notificationList = dbHelper.getAllNotifications();
        adapter.setNotificationList(notificationList);
    }

    private void showAddNotificationDialog() {
        EditText et = new EditText(getContext());
        new AlertDialog.Builder(getContext())
                .setTitle("Add Notification")
                .setView(et)
                .setPositiveButton("Add", (dialog, which) -> {
                    String msg = et.getText().toString().trim();
                    if (!TextUtils.isEmpty(msg)) {
                        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());
                        dbHelper.insertNotification(new NotificationItem(0, msg, now));
                        loadNotifications();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}