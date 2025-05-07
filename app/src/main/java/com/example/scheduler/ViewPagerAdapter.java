package com.example.scheduler;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.scheduler.Tabs.ScheduleFrag;
import com.example.scheduler.Tabs.PastFrag;
import com.example.scheduler.Tabs.NotificationFrag;
import com.example.scheduler.Tabs.ProfileFrag;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new ScheduleFrag();
            case 1: return new PastFrag();
            case 2: return new NotificationFrag();
            case 3: return new ProfileFrag();
            default: return new ScheduleFrag();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
