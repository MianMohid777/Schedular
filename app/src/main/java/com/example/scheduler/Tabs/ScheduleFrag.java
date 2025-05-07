package com.example.scheduler.Tabs;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Use the {@link ScheduleFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScheduleFrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private TaskAdapter adapter;
    private List<Task> allTasks = new ArrayList<>();
    private List<Task> filteredTasks = new ArrayList<>();
    private AppDatabaseHelper dbHelper;
    private FloatingActionButton fab;
    private String selectedDate = "", selectedTime = "";
    private CalendarView calendarView;

    public ScheduleFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScheduleFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static ScheduleFrag newInstance(String param1, String param2) {
        ScheduleFrag fragment = new ScheduleFrag();
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
        calendarView = view.findViewById(R.id.calendarView);
        recyclerView = view.findViewById(R.id.recyclerViewSchedule);
        fab = view.findViewById(R.id.fabAddTask);
        dbHelper = new AppDatabaseHelper(getContext());

        adapter = new TaskAdapter(filteredTasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // Load all upcoming tasks initially
        allTasks = dbHelper.getAllUpcomingTasks("0000-00-00 00:00"); // or use a method to get all tasks
        // Show today's tasks by default
        filterTasksByDate(new Date(calendarView.getDate()));

        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            Calendar cal = Calendar.getInstance();
            cal.set(year, month, dayOfMonth);
            filterTasksByDate(cal.getTime());
        });

        fab.setOnClickListener(v -> showAddTaskDialog());

        return view;
    }

    private void filterTasksByDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String selectedDate = sdf.format(date);
        filteredTasks.clear();
        for (Task task : allTasks) {
            if (task.getDatetime().startsWith(selectedDate)) {
                filteredTasks.add(task);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void showAddTaskDialog() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_task, null);
        EditText etTitle = dialogView.findViewById(R.id.etTitle);
        EditText etDescription = dialogView.findViewById(R.id.etDescription);
        Button btnPickDate = dialogView.findViewById(R.id.btnPickDate);
        Button btnPickTime = dialogView.findViewById(R.id.btnPickTime);
        TextView tvDateTime = dialogView.findViewById(R.id.tvDateTime);
        Button btnSave = dialogView.findViewById(R.id.btnSaveTask);

        selectedDate = "";
        selectedTime = "";

        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(dialogView)
                .create();

        btnPickDate.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(getContext(), (view, year, month, dayOfMonth) -> {
                selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
                tvDateTime.setText(selectedDate + " " + selectedTime);
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
        });

        btnPickTime.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new TimePickerDialog(getContext(), (view, hour, minute) -> {
                selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
                tvDateTime.setText(selectedDate + " " + selectedTime);
            }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
        });

        btnSave.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String desc = etDescription.getText().toString().trim();
            String datetime = (selectedDate + " " + selectedTime).trim();

            if (TextUtils.isEmpty(title) || TextUtils.isEmpty(desc) || selectedDate.isEmpty() || selectedTime.isEmpty()) {
                Toast.makeText(getContext(), "Fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            dbHelper.insertTask(new Task(0, title, desc, datetime, "Upcoming"));
            dialog.dismiss();
            filterTasksByDate(new Date());
        });

        dialog.show();
    }
}