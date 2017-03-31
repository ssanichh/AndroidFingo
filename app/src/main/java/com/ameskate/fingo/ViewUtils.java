package com.ameskate.fingo;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
                context.getResources().getDimension(R.dimen.text_little));
        button.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL);
        button.setAllCaps(false);
        button.setOnCheckedChangeListener(listener);
        int padding = (int) context.getResources().getDimension(R.dimen.dm_small);
        button.setPadding(padding, padding, padding, padding);
        button.setEllipsize(TextUtils.TruncateAt.END);

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

    public static Button getMenuButton(Context context, GameMode tag, View.OnClickListener listener){
        Button button = new Button(context);
        LinearLayout.LayoutParams params = new LinearLayout
                .LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 1;
        button.setLayoutParams(params);

        button.setBackground(ContextCompat.getDrawable(context, R.drawable.regular_button_selector));
        button.setTextColor(ContextCompat.getColor(context, R.color.purple));
        button.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                context.getResources().getDimension(R.dimen.text_little));
        button.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
        button.setAllCaps(false);
        int padding = (int) context.getResources().getDimension(R.dimen.dm_small);
        button.setPadding(padding, padding, padding, padding);

        if(tag != null){
            button.setText(tag.getName());
            button.setTag(tag);
        }

        button.setOnClickListener(listener);

        return button;
    }

    public static Button getMenuUnicorn(Context context, GameMode tag, View.OnClickListener listener){
        Button button = getMenuButton(context, tag, listener);

        button.setText("");
        button.setBackground(ContextCompat.getDrawable(context, R.drawable.unicorn_button_selector));

        return button;
    }
}
