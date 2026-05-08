package com.weektech;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

// controla a troca das abas (dia 1, 2 e 3)
public class DiasPagerAdapter extends FragmentStateAdapter {

    private static final int NUM_DIAS = 3;

    public DiasPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // cria o fragment da lista de palestras pro dia certo
        // position 0 vira dia 1, position 1 dia 2...
        return PalestrasDiaFragment.newInstance(position + 1);
    }

    @Override
    public int getItemCount() {
        return NUM_DIAS;
    }
}
