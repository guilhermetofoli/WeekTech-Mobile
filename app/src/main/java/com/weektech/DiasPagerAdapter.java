package com.weektech;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class DiasPagerAdapter extends FragmentStateAdapter {

    private static final int NUM_DIAS = 3;

    public DiasPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // position 0 → Dia 1, position 1 → Dia 2, position 2 → Dia 3
        return PalestrasDiaFragment.newInstance(position + 1);
    }

    @Override
    public int getItemCount() {
        return NUM_DIAS;
    }
}
