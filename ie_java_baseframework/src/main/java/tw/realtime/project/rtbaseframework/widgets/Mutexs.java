package tw.realtime.project.rtbaseframework.widgets;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

        public void clear() throws InterruptedException {
            this.mutex.acquire();
            this.dataList.clear();
            this.mutex.release();
        }

        @NonNull
        public List<T> readList() { return dataList; }

        public void  updateCurrentData(@Nullable final T givenData, final boolean isAddition) throws InterruptedException {
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

        public void updateList(@Nullable final List<T> givenList) throws InterruptedException {
            if ( (null == givenList) || (givenList.isEmpty()) ) { return; }
            final int preSize = this.dataList.size();
            this.mutex.acquire();
            this.dataList.clear();
            this.dataList.addAll(givenList);
            LogWrapper.showLog(Log.INFO, "Mutexs_ListWrapper", "updateList - preSize: " + preSize + ", postSize: " + dataList.size());
            this.mutex.release();
        }
    }

    public static final class HashMapWrapper<K, T> {

        private Semaphore mutex = new Semaphore(1);
        private final HashMap<K, T> hashMap = new HashMap<>();

        public boolean hasQueuedThreads() { return mutex.hasQueuedThreads(); }

        public void clear() throws InterruptedException {
            this.mutex.acquire();
            this.hashMap.clear();
            this.mutex.release();
        }

        @NonNull
        public Map<K, T> readHashMap() { return hashMap; }

        public void  updateCurrentData(
                @Nullable final K key, @Nullable final T givenData, final boolean isAddition) throws InterruptedException {
            if ( (null == key) || (null == givenData) ) { return; }
            this.mutex.acquire();
            if (isAddition) {
                LogWrapper.showLog(Log.INFO, "Mutexs_HashMapWrapper", "updateCurrentData [Add][Pre] hashMap.size: " + hashMap.size());
                this.hashMap.put(key, givenData);
                LogWrapper.showLog(Log.INFO, "Mutexs_HashMapWrapper", "updateCurrentData [Add][Post] hashMap.size: " + hashMap.size());
            }
            else {
                LogWrapper.showLog(Log.INFO, "Mutexs_HashMapWrapper", "updateCurrentData [Remove][Pre] hashMap.size: " + hashMap.size());
                if (this.hashMap.containsKey(key)) {
                    //final T removedOne = this.hashMap.remove(key);
                    this.hashMap.remove(key);
                    LogWrapper.showLog(Log.INFO, "Mutexs_HashMapWrapper", "updateCurrentData [Remove][Post] hashMap.size: " + hashMap.size());
                }
            }
            this.mutex.release();
        }
    }
}
