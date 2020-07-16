package tw.realtime.project.rtbaseframework.widgets;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import tw.realtime.project.rtbaseframework.LogWrapper;


/**
 * [Semaphores in Java](https://www.baeldung.com/java-semaphore)
 */
public final class Mutexs {

    public static final class DataWrapper<T> {

        private Semaphore mutex = new Semaphore(1);
        private T data = null;

        public boolean hasQueuedThreads() { return mutex.hasQueuedThreads(); }

        @Nullable
        public T getCurrentData() { return this.data; }

        public void updateCurrentData(@Nullable final T givenData) throws InterruptedException {
            this.mutex.acquire();
            this.data = givenData;
            this.mutex.release();
        }
    }

    public static final class ListWrapper<T> {

        private Semaphore mutex = new Semaphore(1);
        private final ArrayList<T> dataList = new ArrayList<>();

        public boolean hasQueuedThreads() { return mutex.hasQueuedThreads(); }

        public void clearList() throws InterruptedException {
            this.mutex.acquire();
            this.dataList.clear();
            this.mutex.release();
        }

        @NonNull
        public List<T> readList() { return dataList; }

        public void  updateCurrentList(@Nullable final T givenData, final boolean isAddition) throws InterruptedException {
            if (null == givenData) { return; }
            this.mutex.acquire();
            if (isAddition) {
                LogWrapper.showLog(Log.INFO, "Mutexs_ListWrapper", "updateCurrentData [Add][Pre] dataList.size: " + dataList.size());
                if (!this.dataList.contains(givenData)) {
                    this.dataList.add(givenData);
                    LogWrapper.showLog(Log.INFO, "Mutexs_ListWrapper", "updateCurrentData [Add][Post] dataList.size: " + dataList.size());
                }
            }
            else {
                LogWrapper.showLog(Log.INFO, "Mutexs_ListWrapper", "updateCurrentData [Remove][Pre] dataList.size: " + dataList.size());
                if (this.dataList.contains(givenData)) {
                    this.dataList.remove(givenData);
                    LogWrapper.showLog(Log.INFO, "Mutexs_ListWrapper", "updateCurrentData [Remove][Post] dataList.size: " + dataList.size());
                }
            }
            this.mutex.release();
        }
    }
}
