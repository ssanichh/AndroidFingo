package com.example.ssani.fingo;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

public final class ViewUtils {
    private ViewUtils(){}

    public static LinearLayout getRow(Context context){
        LinearLayout layout = new LinearLayout(context);

        LinearLayout.LayoutParams params = new LinearLayout
                .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        params.weight = 1;

        layout.setLayoutParams(params);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setWeightSum(5);

        return layout;
    }

    public static ToggleButton getButton(Context context,
                                         CompoundButton.OnCheckedChangeListener listener,
                                         String text){
        ToggleButton button = new ToggleButton(context);

        LinearLayout.LayoutParams params = new LinearLayout
                .LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 1;

        button.setLayoutParams(params);
        button.setTextColor(ContextCompat
                .getColorStateList(context, R.color.toggle_button_textcolor_selector));
        button.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                context.getResources().getDimension(R.dimen.text_normal));
        button.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
        button.setOnCheckedChangeListener(listener);

        button.setBackground(ContextCompat.getDrawable(context, R.drawable.toggle_button_selector));
        button.setChecked(false);
        button.setEnabled(true);

        button.setText(text);
        button.setTextOn(text);
        button.setTextOff(text);

        return button;
    }

    public static ToggleButton getUnicornButton(Context context,
                                                CompoundButton.OnCheckedChangeListener listener){
        ToggleButton unicorn = getButton(context, listener, "");
        unicorn.setBackground(ContextCompat.getDrawable(context,
                R.drawable.unicorn_button_selector));
        return unicorn;
    }
}
