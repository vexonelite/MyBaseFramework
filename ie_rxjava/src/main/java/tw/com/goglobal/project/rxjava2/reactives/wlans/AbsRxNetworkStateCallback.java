package tw.com.goglobal.project.rxjava2.reactives.wlans;

import android.util.Log;

import androidx.annotation.NonNull;

import io.reactivex.rxjava3.functions.Consumer;

import tw.realtime.project.rtbaseframework.LogWrapper;
import tw.realtime.project.rtbaseframework.widgets.networks.wlan.IeNetworkDelegate;
import tw.realtime.project.rtbaseframework.widgets.networks.wlan.IeNetworkStateInfoDelegate;


public abstract class AbsRxNetworkStateCallback implements IeNetworkDelegate, Consumer<Object> {
    @Override
    public final void accept(@NonNull Object anyObject) throws Exception {
        LogWrapper.showLog(Log.INFO, "AbsRxNetworkStateCallback", "accept: " + anyObject.getClass().getSimpleName());
        if (!(anyObject instanceof IeNetworkStateInfoDelegate)) { return; }
        final IeNetworkStateInfoDelegate networkStateInfo = (IeNetworkStateInfoDelegate) anyObject;
        LogWrapper.showLog(Log.INFO, "AbsRxNetworkStateCallback", "accept: " + anyObject.getClass().getSimpleName());
        switch (networkStateInfo.theCallbackState()) {
            case AVAILABLE: {
                LogWrapper.showLog(Log.INFO, "AbsRxNetworkStateCallback", "accept - AVAILABLE");
                if (null != networkStateInfo.theNetwork() ) { this.onAvailable(networkStateInfo.theNetwork()); }
                else { LogWrapper.showLog(Log.ERROR, "AbsRxNetworkStateCallback", "accept - AVAILABLE - networkStateInfo.network is null!!"); }
                break;
            }
            case CAPABILITIES_CHANGED: {
                LogWrapper.showLog(Log.INFO, "AbsRxNetworkStateCallback", "accept - CAPABILITIES_CHANGED");
                if ( (null != networkStateInfo.theNetwork() ) && (null != networkStateInfo.theNetworkCapabilities()) ) {
                    this.onCapabilitiesChanged(networkStateInfo.theNetwork(), networkStateInfo.theNetworkCapabilities());
                }
                else {
                    LogWrapper.showLog(Log.ERROR, "AbsRxNetworkStateCallback", "accept - CAPABILITIES_CHANGED - " +
                            "either networkStateInfo.network or networkStateInfo.networkCapabilities is null!!");
                }
                break;
            }
            case LOST: {
                LogWrapper.showLog(Log.INFO, "AbsRxNetworkStateCallback", "accept - LOST");
                if (null != networkStateInfo.theNetwork() ) { this.onLost(networkStateInfo.theNetwork()); }
                else { LogWrapper.showLog(Log.ERROR, "AbsRxNetworkStateCallback", "accept - LOST - networkStateInfo.network is null!!"); }
                break;
            }
            default: {
                LogWrapper.showLog(Log.INFO, "AbsRxNetworkStateCallback", "accept - networkStateInfo.callbackState: " + networkStateInfo.theCallbackState());
                break;
            }
        }
    }
}
