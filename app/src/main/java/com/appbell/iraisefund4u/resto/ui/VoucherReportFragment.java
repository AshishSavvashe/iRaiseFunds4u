package com.appbell.iraisefund4u.resto.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appbell.iraisefund4u.R;
import com.appbell.iraisefund4u.common.exception.ApplicationException;
import com.appbell.iraisefund4u.common.util.AppLoggingUtility;
import com.appbell.iraisefund4u.common.util.DateUtil;
import com.appbell.iraisefund4u.resto.app.tasks.RestoCommonAsynkTask;
import com.appbell.iraisefund4u.resto.db.DatabaseManager;

import java.util.ArrayList;
import java.util.Date;

public class VoucherReportFragment extends Fragment {

    private static final String CLASS_ID = "VoucherReportFragment" ;
    View parentView;
    int voucherBookID;
    RecyclerView rvVoucherReport;

    public static VoucherReportFragment getInstance(int voucherBookID){
       VoucherReportFragment fragment = new VoucherReportFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("voucherBookID", voucherBookID);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        parentView = inflater.inflate(R.layout.fragment_voucher_report,null);
        return parentView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {

        voucherBookID= getArguments().getInt("voucherBookID",0);

        rvVoucherReport = parentView.findViewById(R.id.rvVoucherReport);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvVoucherReport.setLayoutManager(linearLayoutManager);

        new getVoucherRedeemDateTask(getActivity()).execute();
        new getVoucherRedeemReportTask(getActivity()).execute();

    }

    private class getVoucherRedeemReportTask extends RestoCommonAsynkTask {
        String errorMsg = null;
        ArrayList<VoucherData> voucherReportList;

        public getVoucherRedeemReportTask(Activity actContext) {
            super(actContext, false);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            parentView.findViewById(R.id.progressBar).setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... parms) {

            try {

                long lastSyncTime = DatabaseManager.getInstance(getActivity()).getVoucherBookDao().getlastSyncTime(voucherBookID);
                voucherReportList = new VoucherBookService(appContext).getVoucherList(voucherBookID, lastSyncTime);
                DatabaseManager.getInstance(getActivity()).getVoucherBookDao().updatelastSyncTime(DateUtil.getCurrentTime(actContext, new Date()).getTime(), voucherBookID);

            }catch(ApplicationException e) {
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


            ArrayList<VoucherData> voucherList = new VoucherBookService(getActivity()).getAllVoucherList(voucherBookID);

            rvVoucherReport.setVisibility(View.VISIBLE);
            parentView.findViewById(R.id.btnNoData).setVisibility(View.GONE);
            VoucherReportRecyclerAdapter adapter = new VoucherReportRecyclerAdapter(voucherList, getActivity());
            rvVoucherReport.setAdapter(adapter);


            /*if (voucherReportList != null && voucherReportList.size() > 0) {

            } else {
                Button btnNoData = parentView.findViewById(R.id.btnNoData);
                btnNoData.setVisibility(View.VISIBLE);
                if (!AndroidAppUtil.isBlank(errorMsg)) {
                    btnNoData.setText(errorMsg);
                    btnNoData.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_error_black, 0, 0);
                }
            }*/
        }
    }

    private class getVoucherRedeemDateTask extends RestoCommonAsynkTask {

        String errorMsg = null;
        ArrayList<VoucherData> voucherReportDateList;


        public getVoucherRedeemDateTask(Activity actContext) {
            super(actContext, false);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
               new VoucherBookService(appContext).getvoucherReddemDate4Report(voucherBookID);
            } catch (Throwable e) {
                errorMsg = e.getMessage();
                AppLoggingUtility.logError(appContext, e, CLASS_ID +  "");
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            try{
                super.onPostExecute(result);

                if(actContext == null || actContext.isFinishing())
                    return;

                if (this.voucherReportDateList != null && voucherReportDateList.size() > 0) {

                    /*String seprater = " ";
                    String[] strRedeemdDates = resultStr.split(",");

                    if (strRedeemdDates != null ) {
                        for (int i = 0, len = strRedeemdDates.length; i < len; i++) {
                            //tvVoucherRedeemDate.append(seprater + sdf.format(new Date(AppUtil.getLongValAtIndex(strRedeemdDates, i))));
                            seprater = ", ";
                        }
                    }*/

                } else {

                }

            }catch(Throwable t){
                AppLoggingUtility.logError(appContext,t, CLASS_ID +  "VoucherReportFragment");
            }
        }

    }
}
