package com.google.android.gms.internal;

import com.facebook.internal.AnalyticsEvents;
import com.facebook.internal.NativeProtocol;

public enum zzvv {
    CLIENT_LOGIN_DISABLED("ClientLoginDisabled"),
    DEVICE_MANAGEMENT_REQUIRED("DeviceManagementRequiredOrSyncDisabled"),
    SOCKET_TIMEOUT("SocketTimeout"),
    SUCCESS("Ok"),
    UNKNOWN_ERROR("UNKNOWN_ERR"),
    NETWORK_ERROR(NativeProtocol.ERROR_NETWORK_ERROR),
    SERVICE_UNAVAILABLE("ServiceUnavailable"),
    INTNERNAL_ERROR("InternalError"),
    BAD_AUTHENTICATION("BadAuthentication"),
    EMPTY_CONSUMER_PKG_OR_SIG("EmptyConsumerPackageOrSig"),
    NEEDS_2F("InvalidSecondFactor"),
    NEEDS_POST_SIGN_IN_FLOW("PostSignInFlowRequired"),
    NEEDS_BROWSER("NeedsBrowser"),
    UNKNOWN(AnalyticsEvents.PARAMETER_DIALOG_OUTCOME_VALUE_UNKNOWN),
    NOT_VERIFIED("NotVerified"),
    TERMS_NOT_AGREED("TermsNotAgreed"),
    ACCOUNT_DISABLED("AccountDisabled"),
    CAPTCHA("CaptchaRequired"),
    ACCOUNT_DELETED("AccountDeleted"),
    SERVICE_DISABLED(NativeProtocol.ERROR_SERVICE_DISABLED),
    NEED_PERMISSION("NeedPermission"),
    NEED_REMOTE_CONSENT("NeedRemoteConsent"),
    INVALID_SCOPE("INVALID_SCOPE"),
    USER_CANCEL("UserCancel"),
    PERMISSION_DENIED(NativeProtocol.ERROR_PERMISSION_DENIED),
    INVALID_AUDIENCE("INVALID_AUDIENCE"),
    UNREGISTERED_ON_API_CONSOLE("UNREGISTERED_ON_API_CONSOLE"),
    THIRD_PARTY_DEVICE_MANAGEMENT_REQUIRED("ThirdPartyDeviceManagementRequired"),
    DM_INTERNAL_ERROR("DeviceManagementInternalError"),
    DM_SYNC_DISABLED("DeviceManagementSyncDisabled"),
    DM_ADMIN_BLOCKED("DeviceManagementAdminBlocked"),
    DM_ADMIN_PENDING_APPROVAL("DeviceManagementAdminPendingApproval"),
    DM_STALE_SYNC_REQUIRED("DeviceManagementStaleSyncRequired"),
    DM_DEACTIVATED("DeviceManagementDeactivated"),
    DM_SCREENLOCK_REQUIRED("DeviceManagementScreenlockRequired"),
    DM_REQUIRED("DeviceManagementRequired"),
    ALREADY_HAS_GMAIL("ALREADY_HAS_GMAIL"),
    BAD_PASSWORD("WeakPassword"),
    BAD_REQUEST("BadRequest"),
    BAD_USERNAME("BadUsername"),
    DELETED_GMAIL("DeletedGmail"),
    EXISTING_USERNAME("ExistingUsername"),
    LOGIN_FAIL("LoginFail"),
    NOT_LOGGED_IN("NotLoggedIn"),
    NO_GMAIL("NoGmail"),
    REQUEST_DENIED("RequestDenied"),
    SERVER_ERROR("ServerError"),
    USERNAME_UNAVAILABLE("UsernameUnavailable"),
    GPLUS_OTHER("GPlusOther"),
    GPLUS_NICKNAME("GPlusNickname"),
    GPLUS_INVALID_CHAR("GPlusInvalidChar"),
    GPLUS_INTERSTITIAL("GPlusInterstitial"),
    GPLUS_PROFILE_ERROR("ProfileUpgradeError");
    
    private final String zzalT;

    private zzvv(String str) {
        this.zzalT = str;
    }

    public static boolean zza(zzvv com_google_android_gms_internal_zzvv) {
        return BAD_AUTHENTICATION.equals(com_google_android_gms_internal_zzvv) || CAPTCHA.equals(com_google_android_gms_internal_zzvv) || NEED_PERMISSION.equals(com_google_android_gms_internal_zzvv) || NEED_REMOTE_CONSENT.equals(com_google_android_gms_internal_zzvv) || NEEDS_BROWSER.equals(com_google_android_gms_internal_zzvv) || USER_CANCEL.equals(com_google_android_gms_internal_zzvv) || DEVICE_MANAGEMENT_REQUIRED.equals(com_google_android_gms_internal_zzvv) || DM_INTERNAL_ERROR.equals(com_google_android_gms_internal_zzvv) || DM_SYNC_DISABLED.equals(com_google_android_gms_internal_zzvv) || DM_ADMIN_BLOCKED.equals(com_google_android_gms_internal_zzvv) || DM_ADMIN_PENDING_APPROVAL.equals(com_google_android_gms_internal_zzvv) || DM_STALE_SYNC_REQUIRED.equals(com_google_android_gms_internal_zzvv) || DM_DEACTIVATED.equals(com_google_android_gms_internal_zzvv) || DM_REQUIRED.equals(com_google_android_gms_internal_zzvv) || THIRD_PARTY_DEVICE_MANAGEMENT_REQUIRED.equals(com_google_android_gms_internal_zzvv) || DM_SCREENLOCK_REQUIRED.equals(com_google_android_gms_internal_zzvv);
    }

    public static boolean zzb(zzvv com_google_android_gms_internal_zzvv) {
        return NETWORK_ERROR.equals(com_google_android_gms_internal_zzvv) || SERVICE_UNAVAILABLE.equals(com_google_android_gms_internal_zzvv);
    }

    public static final zzvv zzcE(String str) {
        zzvv com_google_android_gms_internal_zzvv = null;
        zzvv[] values = values();
        int length = values.length;
        int i = 0;
        while (i < length) {
            zzvv com_google_android_gms_internal_zzvv2 = values[i];
            if (!com_google_android_gms_internal_zzvv2.zzalT.equals(str)) {
                com_google_android_gms_internal_zzvv2 = com_google_android_gms_internal_zzvv;
            }
            i++;
            com_google_android_gms_internal_zzvv = com_google_android_gms_internal_zzvv2;
        }
        return com_google_android_gms_internal_zzvv;
    }
}
