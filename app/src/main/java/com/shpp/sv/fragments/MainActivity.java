package com.shpp.sv.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Fragment leftFragment;
    private Fragment rightFragment;
    private Fragment bottomFragment;
    private FragmentManager fragmentManager;
    private ArrayList<Integer> fragmentColors;
    private ArrayList<Integer> usedColors;
    private Random rnd;
    private static final int fragmentsNumber = 3;
    private static final int addColorsNumber = 3;
    private ArrayList<View> fragmentsViews;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        generateColors();
        initFragmentsViews();
        registerFragmentsForContextMenu();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        initMenuItems(menu);
        return true;
    }

    private void initMenuItems(Menu menu) {
        if (!leftFragment.isHidden()) menu.getItem(0).setChecked(true);
        if (!rightFragment.isHidden()) menu.getItem(1).setChecked(true);
        if (!bottomFragment.isHidden()) menu.getItem(2).setChecked(true);
    }



    private void init() {
        fragmentManager = getFragmentManager();
        leftFragment = fragmentManager.findFragmentById(R.id.fragment_left);
        rightFragment = fragmentManager.findFragmentById(R.id.fragment_right);
        bottomFragment = fragmentManager.findFragmentById(R.id.fragment_bottom);
        fragmentColors = new ArrayList<>();
        usedColors = new ArrayList<>();
        fragmentsViews = new ArrayList<>();
        rnd = new Random();
    }


    private void changeFragmentColor(View v, int color) {
        ColorDrawable vievBackground = (ColorDrawable)v.getBackground();
        int prevColor = vievBackground.getColor();

        v.setBackgroundColor(color);
        usedColors.add(color);
        int prevColorIndex = usedColors.lastIndexOf(prevColor);
        usedColors.remove(prevColorIndex);

    }

    private void initFragmentsViews() {
        fragmentsViews.add(leftFragment.getView());
        fragmentsViews.add(rightFragment.getView());
        fragmentsViews.add(bottomFragment.getView());

        for (int i = 0; i < fragmentsNumber; i++){
            fragmentsViews.get(i).setBackgroundColor(fragmentColors.get(i));
            usedColors.add(fragmentColors.get(i));
        }
    }

    void registerFragmentsForContextMenu(){
        registerForContextMenu(leftFragment.getView());
        registerForContextMenu(rightFragment.getView());
        registerForContextMenu(bottomFragment.getView());
    }

    private void generateColors() {
        for (int i = 0; i < fragmentsNumber + addColorsNumber; i++){
            int color = Color.rgb(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            fragmentColors.add(color);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_fragment_left:
                updateFragmentState(item, leftFragment);
                return true;
            case R.id.menu_fragment_right:
                updateFragmentState(item, rightFragment);
                return true;
            case R.id.menu_ftagment_bottom:
                updateFragmentState(item, bottomFragment);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateFragmentState(MenuItem item, Fragment fragment){
        if (fragment.isHidden()){
            fragmentManager.beginTransaction().show(fragment).commit();
            item.setChecked(true);
        } else {
            fragmentManager.beginTransaction().hide(fragment).commit();
            item.setChecked(false);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        for (int color: fragmentColors){
            if (!usedColors.contains(color)){
                Spannable menuLabel = new SpannableString("#"+Integer.toHexString(color).toUpperCase());

                menuLabel.setSpan(new ForegroundColorSpan(color), 0,
                        menuLabel.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                menuLabel.setSpan(new StyleSpan(Typeface.BOLD), 0,
                        menuLabel.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                menu.add(v.getId(), color, 1, menuLabel);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getGroupId()){
            case R.id.fragment_left:
                changeFragmentColor(leftFragment.getView(), item.getItemId());
                return true;
            case R.id.fragment_right:
                changeFragmentColor(rightFragment.getView(), item.getItemId());
                return true;
            case R.id.fragment_bottom:
                changeFragmentColor(bottomFragment.getView(), item.getItemId());
                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }
}
