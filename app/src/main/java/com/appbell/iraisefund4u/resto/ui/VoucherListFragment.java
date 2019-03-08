package com.appbell.iraisefund4u.resto.ui;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.appbell.iraisefund4u.R;
import com.appbell.iraisefund4u.common.exception.ApplicationException;
import com.appbell.iraisefund4u.common.util.AndroidAppUtil;
import com.appbell.iraisefund4u.common.util.DateUtil;
import com.appbell.iraisefund4u.resto.app.tasks.RestoCommonAsynkTask;
import com.appbell.iraisefund4u.resto.db.DatabaseManager;

import java.util.ArrayList;
import java.util.Date;

public class VoucherListFragment extends Fragment implements VoucherListRecyclerAdapter.OnfilterClickListener {

    int voucherBookID;
    RecyclerView rvVoucherList;
    View parentView;
    long startDate;
    long endDate;
    String selectedItem;
    ArrayList<VoucherData> voucherlist;

    public static VoucherListFragment getInstance(int voucherBookID, long startdate, long endDate, String selectedItem4Filter) {
        VoucherListFragment fragment = new VoucherListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("voucherBookID", voucherBookID);
        bundle.putLong("startdate", startdate);
        bundle.putLong("endDate", endDate);
        bundle.putString("currentAppliedFilter",selectedItem4Filter);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_voucher_list, null);
        return parentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {

        voucherBookID = getArguments().getInt("voucherBookID", 0);
        startDate = getArguments().getLong("startdate", 0);
        endDate = getArguments().getLong("endDate", 0);
        selectedItem = getArguments().getString("currentAppliedFilter");
        rvVoucherList = parentView.findViewById(R.id.rvVoucherList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvVoucherList.setLayoutManager(linearLayoutManager);

        ((VoucherListActivity)getActivity()).showRestoNameAndMonthSpineer();

        loadVoucherlistData();
        new GetVoucherListTask(getActivity()).execute();
    }

    private void loadVoucherlistData() {

        ViewModel viewModel = ViewModelProviders.of(getActivity()).get(ViewModel.class);
        voucherlist = (viewModel.getVoucherList(voucherBookID));

        if(voucherlist.size() == 0){
            ((VoucherListActivity)getActivity()).hideNavigation();
        }else {
            ((VoucherListActivity)getActivity()).showNavigation();
        }

        rvVoucherList.setVisibility(View.VISIBLE);
        parentView.findViewById(R.id.btnNoData).setVisibility(View.GONE);
        VoucherListRecyclerAdapter adapter = new VoucherListRecyclerAdapter(getActivity(), VoucherListFragment.this,selectedItem,voucherBookID);
        rvVoucherList.setAdapter(adapter);

        final FloatingActionButton mFloatingActionButton = parentView.findViewById(R.id.floating_action_button);
        mFloatingActionButton.hide();
        rvVoucherList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy < 0  ) {
                    mFloatingActionButton.hide();
                } else if (dy > 0 ) {
                    mFloatingActionButton.show();
                }
            }
        });


        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rvVoucherList.scrollToPosition(0);
            }
        });

    }

    public void refreshList() {

      new GetVoucherListTask(getActivity()).execute();
    }

    public void applyFilter(String selectedItem4Filter ) {

        selectedItem = selectedItem4Filter;

        if (rvVoucherList.getAdapter() != null) {
            ((VoucherListRecyclerAdapter) rvVoucherList.getAdapter()).getFilter().filter(selectedItem);
        }

    }

    private class GetVoucherListTask extends RestoCommonAsynkTask {
        String errorMsg = null;

        public GetVoucherListTask(Activity actContext) {
            super(actContext, false);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            parentView.findViewById(R.id.progressBar).setVisibility(voucherlist == null || voucherlist.size() == 0 ? View.VISIBLE : View.GONE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                long lastSyncTime = DatabaseManager.getInstance(getActivity()).getVoucherBookDao().getlastSyncTime(voucherBookID);
                voucherlist = new VoucherBookService(appContext).getVoucherList(voucherBookID, lastSyncTime);
                DatabaseManager.getInstance(getActivity()).getVoucherBookDao().updatelastSyncTime(DateUtil.getCurrentTime(actContext, new Date()).getTime(), voucherBookID);

            } catch (ApplicationException e) {
                errorMsg = e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (actContext == null || actContext.isFinishing() || ((VoucherListActivity) getActivity()) == null)
                return;

            parentView.findViewById(R.id.progressBar).setVisibility(View.GONE);

            ViewModel viewModel = ViewModelProviders.of(getActivity()).get(ViewModel.class);
            viewModel.resetVoucherList(voucherBookID);

            loadVoucherlistData();

            if (voucherlist != null && voucherlist.size() > 0) {

                parentView.findViewById(R.id.btnNoData).setVisibility(View.GONE);

                ((VoucherListActivity)actContext).initRestNameFilter();
                applyFilter(selectedItem);

            } else {

                Button btnNoData = parentView.findViewById(R.id.btnNoData);

                btnNoData.setVisibility(View.VISIBLE);
                if (!AndroidAppUtil.isBlank(errorMsg)) {
                    btnNoData.setText(errorMsg);
                    btnNoData.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_error_black, 0, 0);
                }
            }
        }
    }

    @Override
    public void onFilterApplied(ArrayList<VoucherData> listFiltered) {

        if (listFiltered == null || listFiltered.size() == 0) {
            parentView.findViewById(R.id.btnNoData).setVisibility(View.VISIBLE);
        } else {
            parentView.findViewById(R.id.btnNoData).setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshList();
    }
}
