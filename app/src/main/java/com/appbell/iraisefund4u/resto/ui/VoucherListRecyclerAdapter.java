package com.appbell.iraisefund4u.resto.ui;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appbell.common.codevalues.service.CodeValueConstants;
import com.appbell.common.util.AppUtil;
import com.appbell.iraisefund4u.R;
import com.appbell.iraisefund4u.common.universalimageloader.core.ImageLoader;
import com.appbell.iraisefund4u.common.universalimageloader.core.ImageLoaderConfiguration;
import com.appbell.iraisefund4u.common.util.AndroidAppUtil;
import com.appbell.iraisefund4u.common.util.DateUtil;
import com.appbell.iraisefund4u.resto.service.MiscService;
import com.appbell.iraisefund4u.resto.util.AndroidAppConstants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

public class VoucherListRecyclerAdapter extends RecyclerView.Adapter<VoucherListRecyclerAdapter.ViewHolder> implements Filterable {

    Activity context;
    int voucherBookID;

    ArrayList<VoucherData> listVouchersFiltered;
    VoucherListRecyclerAdapter.OnfilterClickListener callback;

    Date time4LastDayOfMonth;
    ImageLoader imageLoader;

    SimpleDateFormat sdf ;
    SimpleDateFormat sdff ;

    public interface OnfilterClickListener {
        void onFilterApplied(ArrayList<VoucherData> listFiltered);
    }

    public VoucherListRecyclerAdapter(Activity actContext, OnfilterClickListener callback, String selectedItem, int voucherBookID) {
        this.context = actContext;
        this.callback = callback;
        this.voucherBookID = voucherBookID;
        this.listVouchersFiltered = getFilteredList(selectedItem);

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context.getApplicationContext()));

        Calendar lastDay = DateUtil.getCalanderObject(context,DateUtil.getCurrentTime(context, new Date()));
        lastDay.setTime(DateUtil.getCurrentTime(context, new Date()));

        lastDay.set(Calendar.DAY_OF_MONTH, lastDay.getActualMaximum(Calendar.DAY_OF_MONTH));
        lastDay.set(Calendar.HOUR_OF_DAY, 23);
        lastDay.set(Calendar.MINUTE, 59);
        lastDay.set(Calendar.SECOND, 59);

        time4LastDayOfMonth = lastDay.getTime();

        sdf = DateUtil.getDateFormatter(context,"MMM-yy");
        sdff = DateUtil.getDateFormatter(context,"dd-MMM-yyyy");

    }


    @Override
    public VoucherListRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == AndroidAppConstants.ITEM_TYPE_HEADER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.voucher_list_withheader, parent, false);
            return new ViewHolder(view);
        } else if (viewType == AndroidAppConstants.ITEM_TYPE_NORMAL) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.voucher_list, parent, false);
            return new ViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final VoucherListRecyclerAdapter.ViewHolder holder, int position) {

        VoucherData voucherListdata = listVouchersFiltered.get(holder.getAdapterPosition());

        final int itemType = getItemViewType(position);

        if (itemType == AndroidAppConstants.ITEM_TYPE_HEADER) {

            String imageUrl = new MiscService(context).getBaseUrl() + "FileRendererServlet?fileName=" + voucherListdata.getImageName();
            imageLoader.displayImage(imageUrl, holder.imgVoucher);

            holder.tvRestaurantName.setText(voucherListdata.getRestaurantName());
            holder.tvVoucherDescription.setText(voucherListdata.getVoucherDesc());

            String restoAddress = voucherListdata.getRestaurantAddress();
            String address = restoAddress.replaceAll(",", ", ");

            holder.tvRestaurantAddress.setText(address);
            holder.tvRestaurantPhoneNo.setText(voucherListdata.getRestaurantPhone());
            holder.tvVoucherUsages.setText(voucherListdata.getUsageCountStr());
            holder.tvVoucherMonth.setText(sdf.format(voucherListdata.getVoucherStartTime()));

            Date voucherStartTime = new Date(voucherListdata.getVoucherStartTime());

            if (AndroidAppUtil.compareOnlyDates(context,voucherStartTime, time4LastDayOfMonth) > 0) {
                holder.imgComingSoon.setVisibility(View.VISIBLE);
                holder.btnViewCoupon.setVisibility(View.GONE);
                holder.linearRestoCall.setVisibility(View.GONE);
            } else {
                holder.linearRestoCall.setVisibility(View.VISIBLE);
                holder.imgComingSoon.setVisibility(View.GONE);
                holder.btnViewCoupon.setVisibility(View.VISIBLE);
            }

            Date cureentDate = DateUtil.getCurrentTime(context, new Date());
            Date VoucherEndDate = new Date(voucherListdata.getVoucherEndTime());

            if (AndroidAppUtil.compareOnlyDates(context,cureentDate, VoucherEndDate) > 0 && !(CodeValueConstants.VOUCHER_STATUS_Redeemed.equals(voucherListdata.getVoucherStatus()))) {
                holder.imgExpiredVoucher.setVisibility(View.VISIBLE);
                holder.btnViewCoupon.setVisibility(View.GONE);
                holder.linearRestoCall.setVisibility(View.GONE);
            } else {
                holder.imgExpiredVoucher.setVisibility(View.GONE);
            }

            if (CodeValueConstants.VOUCHER_STATUS_Redeemed.equals(voucherListdata.getVoucherStatus())) {
                holder.imgRedeemedVoucher.setVisibility(View.VISIBLE);
            } else {
                holder.imgRedeemedVoucher.setVisibility(View.GONE);
            }

        } else if (itemType == AndroidAppConstants.ITEM_TYPE_NORMAL) {

            String imageUrl = new MiscService(context).getBaseUrl() + "FileRendererServlet?fileName=" + voucherListdata.getImageName();
            imageLoader.displayImage(imageUrl, holder.imgVoucher);

            holder.tvRestaurantName.setText(voucherListdata.getRestaurantName());
            holder.tvVoucherDescription.setText(voucherListdata.getVoucherDesc());
            holder.tvRestaurantAddress.setText(voucherListdata.getRestaurantAddress());
            holder.tvRestaurantPhoneNo.setText(voucherListdata.getRestaurantPhone());

            holder.tvVoucherUsages.setText(voucherListdata.getUsageCountStr());

            Date voucherStartTime = new Date(voucherListdata.getVoucherStartTime());



            if (AndroidAppUtil.compareOnlyDates(context,voucherStartTime, time4LastDayOfMonth) > 0) {
                holder.imgComingSoon.setVisibility(View.VISIBLE);
                holder.btnViewCoupon.setVisibility(View.GONE);
                holder.linearRestoCall.setVisibility(View.GONE);
            } else {
                holder.linearRestoCall.setVisibility(View.VISIBLE);
                holder.imgComingSoon.setVisibility(View.GONE);
                holder.btnViewCoupon.setVisibility(View.VISIBLE);
            }

            Date cureentDate = DateUtil.getCurrentTime(context, new Date());
            Date VoucherEndDate = new Date(voucherListdata.getVoucherEndTime());


            if (AndroidAppUtil.compareOnlyDates(context,cureentDate, VoucherEndDate) > 0 && !(CodeValueConstants.VOUCHER_STATUS_Redeemed.equals(voucherListdata.getVoucherStatus()))) {
                holder.imgExpiredVoucher.setVisibility(View.VISIBLE);
                holder.btnViewCoupon.setVisibility(View.GONE);
                holder.linearRestoCall.setVisibility(View.GONE);
            } else {
                holder.imgExpiredVoucher.setVisibility(View.GONE);
            }

            if (CodeValueConstants.VOUCHER_STATUS_Redeemed.equals(voucherListdata.getVoucherStatus())){
                holder.imgRedeemedVoucher.setVisibility(View.VISIBLE);
            } else {
                holder.imgRedeemedVoucher.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {

        VoucherData voucherData = listVouchersFiltered.get(position);

        if (voucherData.isIsheader()) {
            return AndroidAppConstants.ITEM_TYPE_HEADER;
        } else {
            return AndroidAppConstants.ITEM_TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return listVouchersFiltered.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                filterResults.values = getFilteredList(constraint);
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listVouchersFiltered.clear();
                listVouchersFiltered.addAll((Collection<? extends VoucherData>) filterResults.values);
                callback.onFilterApplied(listVouchersFiltered);
                notifyDataSetChanged();
            }
        };
    }

    private ArrayList<VoucherData> getFilteredList(CharSequence constraint) {

        SimpleDateFormat sdfff =  DateUtil.getDateFormatter(context,"MMM-yy");

        ArrayList<VoucherData> filterResults = null;

        ArrayList<VoucherData> listBaseVouchers = ViewModelProviders.of((FragmentActivity) context).get(ViewModel.class).getVoucherList(voucherBookID);

        String[] selectedItem = constraint.toString().split("~");

        if (constraint != null && constraint.length() > 0) {

            if (selectedItem[0].contains(AndroidAppConstants.ALLRestaurant) && selectedItem[1].contains(AndroidAppConstants.ALLMonth)) {

                filterResults = new ArrayList<>();
                filterResults.addAll(listBaseVouchers);

            } else {
                ArrayList<VoucherData> voucherFiltered = new ArrayList<>();
                boolean restMatched;
                boolean monthMatched;

                for (int i = 0, len = listBaseVouchers.size(); i < len; i++) {

                    restMatched = false;
                    monthMatched = false;

                    if (AppUtil.getValAtIndex(selectedItem, 0).contains(AndroidAppConstants.ALLRestaurant) || AppUtil.getValAtIndex(selectedItem, 0).contains(listBaseVouchers.get(i).getRestaurantName())) {
                        restMatched = true;
                    }
                    if (AppUtil.getValAtIndex(selectedItem, 1).contains(AndroidAppConstants.ALLMonth) || AppUtil.getValAtIndex(selectedItem, 1).contains(sdfff.format(listBaseVouchers.get(i).getVoucherStartTime()))) {
                        monthMatched = true;
                    }
                    if (restMatched && monthMatched) {
                        voucherFiltered.add(listBaseVouchers.get(i));
                    }
                }
                filterResults = voucherFiltered;
            }
        } else {
            filterResults = listBaseVouchers;
        }

        String lastMonth = "";
        VoucherData voucherData;

        ArrayList<VoucherData> tempList = new ArrayList<>();

        for (int i = 0; i < filterResults.size(); i++) {

            voucherData = filterResults.get(i);

            String cureentMonth = new String(sdfff.format(voucherData.getVoucherStartTime()));

            if (!cureentMonth.equalsIgnoreCase(lastMonth)) {
                lastMonth = cureentMonth;
                voucherData.setIsheader(true);
                tempList.add(voucherData);
            } else {
                tempList.add(filterResults.get(i));
            }
        }

        filterResults = tempList;
        return filterResults;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgVoucher;
        ImageView imgComingSoon;
        ImageView imgRedeemedVoucher;
        ImageView imgExpiredVoucher;
        ImageView imgRestoCall;
        ImageView imgRestoAdd;
        TextView tvVoucherDescription;
        TextView tvRestaurantName;
        TextView tvRestaurantAddress;
        TextView tvRestaurantPhoneNo;
        TextView tvVoucherUsages;
        TextView tvVoucherMonth;
        Button btnViewCoupon;
        LinearLayout linearRestoCall;




        public ViewHolder(View itemView) {
            super(itemView);

            imgVoucher = itemView.findViewById(R.id.imgVoucher);
            imgComingSoon = itemView.findViewById(R.id.imgComingSoon);
            imgRedeemedVoucher = itemView.findViewById(R.id.imgRedeemedVoucher);
            imgExpiredVoucher = itemView.findViewById(R.id.imgExpiredVoucher);
            imgRestoCall = itemView.findViewById(R.id.imgRestoCall);
            imgRestoAdd = itemView.findViewById(R.id.imgRestoAdd);
            tvVoucherDescription = itemView.findViewById(R.id.tvVoucherDescription);
            tvRestaurantName = itemView.findViewById(R.id.tvRestaurantName);
            tvRestaurantAddress = itemView.findViewById(R.id.tvRestaurantAddress);
            tvRestaurantPhoneNo = itemView.findViewById(R.id.tvRestaurantPhoneNo);
            tvVoucherUsages = itemView.findViewById(R.id.tvVoucherUsages);
            tvVoucherMonth = itemView.findViewById(R.id.tvVoucherMonth);
            btnViewCoupon = itemView.findViewById(R.id.btnViewCoupon);
            linearRestoCall = itemView.findViewById(R.id.linearRestoCall);


            tvRestaurantAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showResturantOnMap();
                }
            });

            imgRestoAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    showResturantOnMap();
                }
            });

            tvRestaurantPhoneNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callRestaurantMethodd();
                }
            });

            imgRestoCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    callRestaurantMethodd();
                }

            });

            imgVoucher.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (getAdapterPosition() == RecyclerView.NO_POSITION)
                        return;

                    VoucherData voucherData = listVouchersFiltered.get(getAdapterPosition());

                    Date cureentDate = DateUtil.getCurrentTime(context, new Date());
                    Date VoucherEndDate = new Date(listVouchersFiltered.get(getAdapterPosition()).getVoucherEndTime());
                    Date voucherSrartDate = new Date(voucherData.getVoucherStartTime());

                    if (AndroidAppUtil.compareOnlyDates(context,voucherSrartDate, time4LastDayOfMonth) > 0) {
                        String voucherCommingSoonMsg = "This " + listVouchersFiltered.get(getAdapterPosition()).getVoucherAlies() + " will be activated from " + sdff.format(listVouchersFiltered.get(getAdapterPosition()).getVoucherStartTime());
                        new CommonAlertDialog().showOkDialog(context, voucherCommingSoonMsg);
                    } else if (AndroidAppUtil.compareOnlyDates(context,cureentDate, VoucherEndDate) > 0 && !(CodeValueConstants.VOUCHER_STATUS_Redeemed.equals(listVouchersFiltered.get(getAdapterPosition()).getVoucherStatus()))) {
                        String voucherExpiredMsg = "This " + listVouchersFiltered.get(getAdapterPosition()).getVoucherAlies() + " is expired";
                        new CommonAlertDialog().showOkDialog(context, voucherExpiredMsg);
                    } else {
                        Intent intent = new Intent(context, RedeemVoucherActivity.class);
                        intent.putExtra("voucherListdata", listVouchersFiltered.get(getAdapterPosition()));
                        context.startActivity(intent);
                    }

                }
            });

            btnViewCoupon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, RedeemVoucherActivity.class);
                    intent.putExtra("voucherListdata", listVouchersFiltered.get(getAdapterPosition()));
                    context.startActivity(intent);
                }
            });
        }

        private void showResturantOnMap() {

            String restaurantAddress = listVouchersFiltered.get(getAdapterPosition()).getRestaurantAddress();
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + restaurantAddress);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            context.startActivity(mapIntent);
        }

        private void callRestaurantMethodd() {

            String restaurantPhone = listVouchersFiltered.get(getAdapterPosition()).getRestaurantPhone();
            if (!AppUtil.isBlank(restaurantPhone)) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + restaurantPhone));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }

        }
    }
}
