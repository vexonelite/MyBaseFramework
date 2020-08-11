package tw.com.goglobal.project.rxjava2.reactives.wlans;

import android.util.Log;

import androidx.annotation.NonNull;

import io.reactivex.functions.Consumer;

import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.widgets.networks.wlan.IeNetworkDelegate;
import tw.realtime.project.rtbaseframework.widgets.networks.wlan.IeNetworkStateInfo;


public abstract class AbsRxNetworkStateCallback implements IeNetworkDelegate, Consumer<Object> {
    @Override
    public final void accept(@NonNull Object anyObject) throws Exception {
        LogWrapper.showLog(Log.INFO, "AbsRxNetworkStateCallback", "accept: " + anyObject.getClass().getSimpleName());
        if (!(anyObject instanceof IeNetworkStateInfo)) { return; }
        final IeNetworkStateInfo networkStateInfo = (IeNetworkStateInfo) anyObject;
        LogWrapper.showLog(Log.INFO, "AbsRxNetworkStateCallback", "accept: " + anyObject.getClass().getSimpleName());
        switch (networkStateInfo.callbackState) {
            case AVAILABLE: {
                LogWrapper.showLog(Log.INFO, "AbsRxNetworkStateCallback", "accept - AVAILABLE");
                if (null != networkStateInfo.network ) { this.onAvailable(networkStateInfo.network); }
                else { LogWrapper.showLog(Log.ERROR, "AbsRxNetworkStateCallback", "accept - AVAILABLE - networkStateInfo.network is null!!"); }
                break;
            }
            case CAPABILITIES_CHANGED: {
                LogWrapper.showLog(Log.INFO, "AbsRxNetworkStateCallback", "accept - CAPABILITIES_CHANGED");
                if ( (null != networkStateInfo.network ) && (null != networkStateInfo.networkCapabilities) ) {
                    this.onCapabilitiesChanged(networkStateInfo.network, networkStateInfo.networkCapabilities);
                }
                else {
                    LogWrapper.showLog(Log.ERROR, "AbsRxNetworkStateCallback", "accept - CAPABILITIES_CHANGED - " +
                            "either networkStateInfo.network or networkStateInfo.networkCapabilities is null!!");
                }
                break;
            }
            case LOST: {
                LogWrapper.showLog(Log.INFO, "AbsRxNetworkStateCallback", "accept - LOST");
                if (null != networkStateInfo.network ) { this.onLost(networkStateInfo.network); }
                else { LogWrapper.showLog(Log.ERROR, "AbsRxNetworkStateCallback", "accept - LOST - networkStateInfo.network is null!!"); }
                break;
            }
            default: {
                LogWrapper.showLog(Log.INFO, "AbsRxNetworkStateCallback", "accept - networkStateInfo.callbackState: " + networkStateInfo.callbackState);
                break;
            }
        }
    }
}
