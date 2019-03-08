package com.appbell.iraisefund4u.resto.ui;

public class VoucherReportData {

    private String restaurantName;
    private String couponDescription;
    private long redeemDate;

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getCouponDescription() {
        return couponDescription;
    }

    public void setCouponDescription(String couponDescription) {
        this.couponDescription = couponDescription;
    }

    public long getRedeemDate() {
        return redeemDate;
    }

    public void setRedeemDate(long redeemDate) {
        this.redeemDate = redeemDate;
    }
}
