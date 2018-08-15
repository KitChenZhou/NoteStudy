package com.ckt5.test.auto_dail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import java.util.List;

/**
 * Created by D22395 on 2017/8/18.
 */

public class AllCaseFragment extends Fragment{

    private RecyclerView mCaseRecyclerView;
    private CaseAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_case_list, container, false);

        mCaseRecyclerView = view.findViewById(R.id.case_recycler_view);
        mCaseRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();
        return view;
    }

    private void updateUI() {
        CaseLab caseLab = CaseLab.get(getActivity());
        List<Case> mCases = caseLab.getCases();


        mAdapter = new CaseAdapter(mCases);
        mCaseRecyclerView.setAdapter(mAdapter);
    }


    private class CaseHolder extends RecyclerView.ViewHolder {

        private CheckBox mCaseView;

        public CaseHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_case, parent, false));
            mCaseView = itemView.findViewById(R.id.case_list);
        }
    }

    private class CaseAdapter extends RecyclerView.Adapter<CaseHolder> {

        private List<Case> mCases;

        public CaseAdapter(List<Case> cases) {
            mCases = cases;
        }

        @Override
        public CaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new CaseHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(CaseHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return mCases.size();
        }
    }
}
