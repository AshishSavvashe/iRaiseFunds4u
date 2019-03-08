package com.appbell.iraisefund4u.resto.ui;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appbell.iraisefund4u.R;
import com.appbell.iraisefund4u.common.universalimageloader.core.ImageLoader;
import com.appbell.iraisefund4u.common.universalimageloader.core.ImageLoaderConfiguration;
import com.appbell.iraisefund4u.common.util.DateUtil;
import com.appbell.iraisefund4u.resto.service.MiscService;
import com.appbell.iraisefund4u.resto.util.NavigationUtil;
import com.appbell.iraisefund4u.resto.util.RestoAppCache;

import java.util.ArrayList;

public class VoucherBookRecyclerAdapter extends RecyclerView.Adapter<VoucherBookRecyclerAdapter.ViewHolder> {

    ArrayList<VoucherBookData> voucherbooklist;
    Activity actContext = null;
    boolean isFromBuyBook;
    ImageLoader imageLoader;
    String bookBuyerPhoneNo;
    private int lastAdded;

    public VoucherBookRecyclerAdapter(ArrayList<VoucherBookData> voucherbooklist, Activity context, Boolean webViewFlag,String bookBuyerPhoneNo) {
        this.actContext = context;
        this.voucherbooklist = voucherbooklist;
        this.isFromBuyBook = webViewFlag;
        this.bookBuyerPhoneNo= bookBuyerPhoneNo;
        lastAdded = voucherbooklist.size() - 1;
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(actContext.getApplicationContext()));
    }


    @NonNull
    @Override
    public VoucherBookRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.voucher_book_recycler_adapter, parent, false);
        VoucherBookRecyclerAdapter.ViewHolder vh = new VoucherBookRecyclerAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherBookRecyclerAdapter.ViewHolder holder, int position) {

        VoucherBookData voucherBookData = voucherbooklist.get(holder.getAdapterPosition());

        String imageUrl = new MiscService(actContext).getBaseUrl() + "FileRendererServlet?fileName=" + voucherBookData.getFileName();
        imageLoader.displayImage(imageUrl, holder.imgVoucher);

        holder.tvCampainTagLine.setText(voucherBookData.getCampaignTagLine());

        StringBuilder sbAddSpaceAfter4Digit = new StringBuilder(voucherBookData.getBookCode());

        for (int i = 4; i < sbAddSpaceAfter4Digit.length(); i += 5) {
            sbAddSpaceAfter4Digit.insert(i, " ");
        }

        holder.tvVoucherBookCode.setText(sbAddSpaceAfter4Digit.toString());

        if(bookBuyerPhoneNo.equalsIgnoreCase(RestoAppCache.getAppConfig(actContext).getCustomerPhone()) && isFromBuyBook && lastAdded == position){

            holder.linearLayout.setBackgroundColor(actContext.getResources().getColor(R.color.tabIndicatorColor));

        }else {
            holder.linearLayout.setBackgroundColor(actContext.getResources().getColor(R.color.white));
        }
    }

    @Override
    public int getItemCount() {
        return voucherbooklist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgVoucher;
        TextView tvVoucherBookCode;
        TextView tvCampainTagLine;
        LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            imgVoucher = itemView.findViewById(R.id.imgVoucher);
            tvCampainTagLine = itemView.findViewById(R.id.campainTagLine);
            tvVoucherBookCode = itemView.findViewById(R.id.voucherbookcode);
            linearLayout = itemView.findViewById(R.id.linearLayout);

            imgVoucher.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VoucherBookData voucherBookData = voucherbooklist.get(getAdapterPosition());
                    DateUtil.saveBookTimeZone(actContext,voucherBookData.getTimeZoneProp());
                    NavigationUtil.navigate2VoucherListActivity(voucherBookData.getBookId(), voucherBookData.getCampaignTagLine(), actContext, false, voucherBookData.getVoucherStartTime(), voucherBookData.getVoucherEndTime());
                }
            });
        }
    }
}
