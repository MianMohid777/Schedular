package com.example.scheduler.Tabs;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import com.example.scheduler.R;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText etFirstName, etLastName, etPhone, etDOB;
    private Spinner spinnerGender;
    private Button btnUpdateProfile;
    private SwitchCompat switchTheme;
    private SharedPreferences prefs;
    private ImageView imageAvatar;

    public ProfileFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFrag newInstance(String param1, String param2) {
        ProfileFrag fragment = new ProfileFrag();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        etFirstName = view.findViewById(R.id.editTextFirstName);
        etLastName = view.findViewById(R.id.editTextLastName);
        etPhone = view.findViewById(R.id.editTextPhone);
        etDOB = view.findViewById(R.id.editTextDOB);
        spinnerGender = view.findViewById(R.id.spinnerGender);
        btnUpdateProfile = view.findViewById(R.id.buttonUpdateProfile);
        switchTheme = view.findViewById(R.id.switchTheme);
        imageAvatar = view.findViewById(R.id.imageAvatar);

        prefs = getContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);

        // Set up gender spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapter);

        // Load saved values
        etFirstName.setText(prefs.getString("first_name", ""));
        etLastName.setText(prefs.getString("last_name", ""));
        etPhone.setText(prefs.getString("phone", ""));
        etDOB.setText(prefs.getString("dob", ""));
        String gender = prefs.getString("gender", "");
        if (!gender.isEmpty()) {
            int pos = adapter.getPosition(gender);
            spinnerGender.setSelection(pos);
        }
        switchTheme.setChecked(prefs.getBoolean("dark_mode", false));

        // Date picker for DOB
        etDOB.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            DatePickerDialog dpd = new DatePickerDialog(getContext(), (view1, year, month, dayOfMonth) -> {
                String dob = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
                etDOB.setText(dob);
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
            dpd.show();
        });

        switchTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("dark_mode", isChecked);
            editor.apply();
            AppCompatDelegate.setDefaultNightMode(isChecked ?
                    AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        });

        btnUpdateProfile.setOnClickListener(v -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("first_name", etFirstName.getText().toString().trim());
            editor.putString("last_name", etLastName.getText().toString().trim());
            editor.putString("phone", etPhone.getText().toString().trim());
            editor.putString("dob", etDOB.getText().toString().trim());
            editor.putString("gender", spinnerGender.getSelectedItem().toString());
            editor.apply();
            Toast.makeText(getContext(), "Profile saved", Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}