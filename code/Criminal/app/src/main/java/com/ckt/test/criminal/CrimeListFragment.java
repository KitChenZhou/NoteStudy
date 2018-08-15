package com.ckt.test.criminal;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

/**
 * Created by D22395 on 2017/9/28.
 */

public class CrimeListFragment extends Fragment{

    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";

    public static ImageView ivImg;
    public static TextView tv_text;

    private static final int REQUEST_CRIME = 1;
    private RecyclerView mCrimeRecyclerView;
    private TextView emptyText;
    private CrimeAdapter mAdapter;
    private int mPosition = 0;
    private boolean mSubtitleVisible;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mCrimeRecyclerView = view.findViewById(R.id.crime_recycler_view);
        emptyText = view.findViewById(R.id.crime_set_empty_text_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Log.i("TAG", "onCreate");

        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

        updateUI();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CRIME) {
            //Handle result
            if(data != null) {
                mPosition = data.getIntExtra("crime_id", REQUEST_CRIME);
                Log.i("TAG", "mPosition:" + mPosition);
              }
         }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if (mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_Subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    private void updateSubtitle() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        int crimeCount = crimeLab.getCrimes().size();
        String subtitle = getString(R.string.subtitle_fomat, crimeCount);

        if (!mSubtitleVisible) {
            subtitle = null;
        }

        // 设置子标题
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_crime:
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getId());
                startActivity(intent);
                return true;
            case R.id.show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();

        if (crimes.isEmpty()) {
            mCrimeRecyclerView.setVisibility(View.INVISIBLE);
            emptyText.setVisibility(View.VISIBLE);
        } else {
            mCrimeRecyclerView.setVisibility(View.VISIBLE);
            emptyText.setVisibility(View.INVISIBLE);
        }
        Log.i("TAG", "crime.toString :" + crimes.size());

        if (mAdapter == null) {
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
            //先实例化Callback
            ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
            //用Callback构造ItemtouchHelper
            ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
            //调用ItemTouchHelper的attachToRecyclerView方法建立联系
            touchHelper.attachToRecyclerView(mCrimeRecyclerView);
        } else  {
//            mAdapter.setCrimes(crimes);
            Log.i("TAG", "mAdapter is not null");
//            mAdapter.notifyDataSetChanged();
            mAdapter.setCrimes(crimes);
            mAdapter.notifyItemChanged(mPosition);
            mAdapter.notifyItemChanged(crimes.size() - 1);
        }



        updateSubtitle();
    }



    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private Crime mCrime;
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private ImageView mSolvedImageView;


        private void bind(Crime crime) {
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(mCrime.getDate()));
            mSolvedImageView.setVisibility(crime.isSolved() ? View.VISIBLE : View.GONE);
        }

        public CrimeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_crime, parent, false));

            mTitleTextView = itemView.findViewById(R.id.crime_title);
            mDateTextView = itemView.findViewById(R.id.crime_date);
            mSolvedImageView = itemView.findViewById(R.id.crime_solved);
            ivImg = itemView.findViewById(R.id.iv_img);
            tv_text = itemView.findViewById(R.id.tv_text);
            itemView.setOnClickListener(this);
            Log.i("TAG", "CrimeHolder");
        }


        @Override
        public void onClick(View view) {
//            Intent intent = new Intent(getActivity(), CrimeActivity.class);
            Log.i("TAG", "click");
//            Intent intent = CrimeActivity.newIntent(getActivity(), mCrime.getId());
            Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getId());
            startActivityForResult(intent, REQUEST_CRIME);
        }

    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> implements ItemTouchHelperAdapter {

        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new CrimeHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Log.i("TAG", "BindViewHolder");
            Crime crime = mCrimes.get(position);
            holder.bind(crime);

        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }

        public void setCrimes(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @Override
        public void onItemMove(int fromPosition, int toPosition) {

            Collections.swap(mCrimes,fromPosition,toPosition);
            notifyItemMoved(fromPosition,toPosition);

        }

        @Override
        public void onItemDissmiss(int position) {
            CrimeLab.get(getActivity()).removeCrime(position);
            Log.i("TAG", "direction:" + position);
            notifyItemRemoved(position);
            updateUI();
        }

    }

    public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback{

        private ItemTouchHelperAdapter mAdapter;

        public SimpleItemTouchHelperCallback(ItemTouchHelperAdapter adapter){
            mAdapter = adapter;
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            int swipeFlags = ItemTouchHelper.LEFT;
            return makeMovementFlags(dragFlags,swipeFlags);
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return true;
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return true;
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            mAdapter.onItemMove(viewHolder.getAdapterPosition(),target.getAdapterPosition());
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            mAdapter.onItemDissmiss(viewHolder.getAdapterPosition());
        }

        //限制ImageView长度所能增加的最大值
        private double ICON_MAX_SIZE = 50;
        //ImageView的初始长宽
        private int fixedWidth = 150;

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            //重置改变，防止由于复用而导致的显示问题
            viewHolder.itemView.setScrollX(0);
//            ((CrimeAdapter.NormalItem)viewHolder).tv.setText("左滑删除");
            tv_text.setText("左滑删除");
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) ivImg.getLayoutParams();
            params.width = 150;
            params.height = 150;
            ivImg.setLayoutParams(params);
            ivImg.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            //仅对侧滑状态下的效果做出改变
            if (actionState ==ItemTouchHelper.ACTION_STATE_SWIPE){
                //如果dX小于等于删除方块的宽度，那么我们把该方块滑出来
                if (Math.abs(dX) <= getSlideLimitation(viewHolder)){
                    viewHolder.itemView.scrollTo(-(int) dX,0);
                }
                //如果dX还未达到能删除的距离，此时慢慢增加“眼睛”的大小，增加的最大值为ICON_MAX_SIZE
                else if (Math.abs(dX) <= recyclerView.getWidth() / 2){
                    double distance = (recyclerView.getWidth() / 2 -getSlideLimitation(viewHolder));
                    double factor = ICON_MAX_SIZE / distance;
                    double diff =  (Math.abs(dX) - getSlideLimitation(viewHolder)) * factor;
                    if (diff >= ICON_MAX_SIZE)
                        diff = ICON_MAX_SIZE;
                    tv_text.setText("");   //把文字去掉
                    ivImg.setVisibility(View.VISIBLE);  //显示眼睛
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) ivImg.getLayoutParams();
                    params.width = (int) (fixedWidth + diff);
                    params.height = (int) (fixedWidth + diff);
                    ivImg.setLayoutParams(params);
                }
            }else {
                //拖拽状态下不做改变，需要调用父类的方法
                super.onChildDraw(c,recyclerView,viewHolder,dX,dY,actionState,isCurrentlyActive);
            }
        }
        /**
         * 获取删除方块的宽度
         */
        public int getSlideLimitation(RecyclerView.ViewHolder viewHolder){
            ViewGroup viewGroup = (ViewGroup) viewHolder.itemView;
            return viewGroup.getChildAt(1).getLayoutParams().width;
        }
    }
}
