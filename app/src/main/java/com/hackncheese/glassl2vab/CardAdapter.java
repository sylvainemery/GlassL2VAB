package com.hackncheese.glassl2vab;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollAdapter;

import java.util.Hashtable;


/**
 * Populates views in a {@code CardScrollView} with a card built from a custom embedded layout
 */
public class CardAdapter extends CardScrollAdapter {

    private final Context mContext;
    private final Hashtable<String, String> mTaskResult;

    /**
     * Initializes a new adapter with the specified context and list of items.
     */
    public CardAdapter(Context context,  Hashtable<String, String> taskResult) {
        mContext = context;
        mTaskResult = taskResult;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public int getPosition(Object item) {
        return AdapterView.INVALID_POSITION;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CardBuilder card = new CardBuilder(mContext, CardBuilder.Layout.TEXT);
        card.setText(mTaskResult.get("result"));

        View view = card.getView(convertView, parent);

        return view;
    }

}
