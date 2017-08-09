package com.lhd.wellcome;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lhd.applock.R;
import com.lhd.main.MainActivity;
import com.lhd.module.CircleIndicator;
import com.lhd.module.Config;


/**
 * Created by D on 8/8/2017.
 */

public class WellcomeFragment extends Fragment implements WellcomeView {

    private MainActivity mainActivity;
    private Button btnNext;
    private Button btnSkip;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewWellcome = inflater.inflate(R.layout.wellcome_layout, null);
        this.mainActivity = (MainActivity) getActivity();
        initView(viewWellcome);
        mSectionsPagerAdapter = new SectionsPagerAdapter(mainActivity.getSupportFragmentManager());
        mViewPager = (ViewPager) viewWellcome.findViewById(R.id.wellcome_viewpager);
        mainActivity.hideAllPermisstion();
        btnSkip = viewWellcome.findViewById(R.id.wellcome_btn_skip);
        btnNext = viewWellcome.findViewById(R.id.wellcome_btn_skip);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextFragment();
            }
        });
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               skipView();
            }
        });
        mViewPager.setAdapter(mSectionsPagerAdapter);
        CircleIndicator indicatorView = viewWellcome.findViewById(R.id.indicator);
        indicatorView.setViewPager(mViewPager);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 1:
                        mainActivity.hideAllPermisstion();
                        break;
                    case 2:
                        mainActivity.showAllPermisstion();
                        break;
                    case 0:
                        mainActivity.hideAllPermisstion();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return viewWellcome;
    }

    private void initView(View viewWellcome) {

    }

    @Override
    public void nextFragment() {
        int index = mViewPager.getCurrentItem();
        if (index < 2)
            mViewPager.setCurrentItem(index + 1,true);
        else if (index==2) skipView();
    }

    @Override
    public void skipView() {
        mainActivity.startSetPinFragment();
    }

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    public static class PlaceholderFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(getArguments().getInt(Config.LAYOUT_FRAGMENT), container, false);
            return rootView;
        }
    }

    public static PlaceholderFragment newInstance(int idLayout) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(Config.LAYOUT_FRAGMENT, idLayout);
        fragment.setArguments(args);
        return fragment;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 1:
                    return newInstance(R.layout.wellcome_fragment_2);
                case 2:
                    return newInstance(R.layout.wellcome_fragment_3);
                default:
                case 0:
                    return newInstance(R.layout.wellcome_fragment_1);
            }

        }

        @Override
        public int getCount() {
            return 3;
        }

    }
}
