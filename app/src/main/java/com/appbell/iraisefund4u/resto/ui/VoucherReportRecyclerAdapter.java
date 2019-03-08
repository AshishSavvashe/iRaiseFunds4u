package com.appbell.iraisefund4u.resto.ui;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appbell.common.util.AppUtil;
import com.appbell.iraisefund4u.R;
import com.appbell.iraisefund4u.common.util.DateUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class VoucherReportRecyclerAdapter extends RecyclerView.Adapter<VoucherReportRecyclerAdapter.ViewHolder> {

    Activity context;
    ArrayList<VoucherData> listBaseVoucherReport;
    SimpleDateFormat sdf = DateUtil.getDateFormatter(context,"dd-MM-yyyy");


    public VoucherReportRecyclerAdapter(ArrayList<VoucherData> list, Activity actContext) {
        this.context=actContext;
        this.listBaseVoucherReport=list;

    }


    @Override
    public VoucherReportRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.voucher_report_recycler_adapter, parent, false);
        VoucherReportRecyclerAdapter.ViewHolder vh = new VoucherReportRecyclerAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherReportRecyclerAdapter.ViewHolder holder, int position) {

        VoucherData voucherData = listBaseVoucherReport.get(holder.getAdapterPosition());


        String voucherRedeemDate = new VoucherBookService(context).getVoucherRedeemDates(voucherData.getBookId());

        String redeemVoucherRestoName = new VoucherBookService(context).getRedeemVoucherRestoName(voucherData.getBookId(),voucherRedeemDate);

        String redeemVoucherDesc = new VoucherBookService(context).getRedeemVoucherDesc(voucherData.getBookId(),redeemVoucherRestoName);

        String seprater = " ";


        String[] strRedeemdDates = voucherRedeemDate.split(",");

        if (strRedeemdDates != null ) {

            holder.tvRedeemDate.setText("");
            for (int i = 0, len = strRedeemdDates.length; i < len; i++) {

                holder.tvRestaurantName.append(seprater + redeemVoucherRestoName);
                holder.tvCoupon.setText(seprater + redeemVoucherDesc);
                holder.tvRedeemDate.append(seprater + sdf.format(new Date(AppUtil.getLongValAtIndex(strRedeemdDates, i))));

            }
        }

    }

    @Override
    public int getItemCount() {
        return listBaseVoucherReport.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvRestaurantName;
        TextView tvCoupon;
        TextView tvRedeemDate;

        public ViewHolder(View itemView) {
            super(itemView);

            tvRestaurantName = itemView.findViewById(R.id.tvRestaurantName);
            tvCoupon = itemView.findViewById(R.id.tvCoupon);
            tvRedeemDate = itemView.findViewById(R.id.tvRedeemDate);

        }
    }
}
