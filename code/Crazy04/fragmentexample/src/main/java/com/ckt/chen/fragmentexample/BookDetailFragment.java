package com.ckt.chen.fragmentexample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by D22395 on 2017/12/20.
 */

public class BookDetailFragment extends android.app.Fragment {

    public static final String ITEM_ID = "item_id";
    BookContent.Book mBook;

    public BookDetailFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(ITEM_ID)) {
            mBook = BookContent.ITEM_MAP.get(getArguments().getInt(ITEM_ID));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_book_detail, container, false);
        if (mBook != null) {
            TextView title = (TextView) rootView.findViewById(R.id.book_title);
            title.setText(mBook.title);
            TextView desc = rootView.findViewById(R.id.book_desc);
            desc.setText(mBook.desc);
        }
        return rootView;
    }
}
