package com.customsdk.wargaming.util;

import com.facebook.share.internal.ShareConstants;
import org.json.JSONException;
import org.json.JSONObject;

public class SkuDetails {
    String mCurrencyCode;
    String mDescription;
    String mItemType;
    String mJson;
    String mPrice;
    long mPriceInMicros;
    String mSku;
    String mTitle;
    String mType;

    public SkuDetails(String jsonSkuDetails) throws JSONException {
        this(IabHelper.ITEM_TYPE_INAPP, jsonSkuDetails);
    }

    public SkuDetails(String itemType, String jsonSkuDetails) throws JSONException {
        this.mItemType = itemType;
        this.mJson = jsonSkuDetails;
        JSONObject o = new JSONObject(this.mJson);
        this.mSku = o.optString("productId");
        this.mType = o.optString(ShareConstants.MEDIA_TYPE);
        this.mPrice = o.optString("price");
        this.mTitle = o.optString(ShareConstants.WEB_DIALOG_PARAM_TITLE);
        this.mDescription = o.optString("description");
        this.mCurrencyCode = o.optString("price_currency_code");
        this.mPriceInMicros = o.optLong("price_amount_micros");
    }

    public String getSku() {
        return this.mSku;
    }

    public String getType() {
        return this.mType;
    }

    public String getPrice() {
        return this.mPrice;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public String getDescription() {
        return this.mDescription;
    }

    public String getCurrencyCode() {
        return this.mCurrencyCode;
    }

    public long getPriceInMicros() {
        return this.mPriceInMicros;
    }

    public String toString() {
        return "SkuDetails:" + this.mJson;
    }
}
