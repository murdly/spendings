package com.akarbowy.spendingsapp.managers;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImportManager {

    private static final int TASK_COMPLETE = 1;
    private static final int TASK_FAIL = 2;
    private static final ImportManager instance;
    private static ExecutorService executor;

    static {
        instance = new ImportManager();
    }

    private final Handler handler;

    private ImportManager() {
        executor = Executors.newSingleThreadExecutor();

        handler = new Handler(Looper.getMainLooper()) {
            @Override public void handleMessage(Message msg) {
                final ImportDataTask importDataTask = (ImportDataTask) msg.obj;
                final ImportManager.Callback callback = importDataTask.callback;

                switch (msg.what) {
                    case TASK_COMPLETE:
                        Log.d("ImportManager", "task completed");
                        if (callback != null) {
                            callback.onDataLoaded(importDataTask.data);
                        }
                        break;
                    case TASK_FAIL:
                        Log.d("ImportManager", "task failed");
                        if (callback != null) {
                            callback.onDataError();
                        }
                        break;
                    default:
                        super.handleMessage(msg);
                }
            }
        };
    }

    public static ImportManager get() {
        return instance;
    }

    public static ImportDataTask startImportData(InputStream inputStream, Callback callback) {
        final ImportDataTask task = new ImportDataTask(inputStream, callback);

        executor.execute(new ImportDataRunnable(task));

        return task;
    }

    public static void cancel(ImportDataTask task) {
        if (task != null) {

            synchronized (instance) {
                final Thread thread = task.thread;
                if (thread != null) {
                    thread.interrupt();
                }
            }
        }
    }

    private void handleState(ImportDataTask task, int state) {
        switch (state) {
            case TASK_COMPLETE:
                final Message complete = handler.obtainMessage(state, task);
                complete.sendToTarget();
                break;
            case TASK_FAIL:
                final Message fail = handler.obtainMessage(state);
                fail.sendToTarget();
                break;
        }
    }

    public interface Callback {
        void onDataLoaded(List<String> data);

        void onDataError();
    }

    public static class ImportDataTask {

        private final ImportManager importManager;
        private final InputStream inputStream;
        private final Callback callback;
        private Thread thread;
        private List<String> data;

        public ImportDataTask(InputStream inputStream, Callback callback) {
            this.inputStream = inputStream;
            this.callback = callback;

            importManager = ImportManager.get();
        }

        public void setData(List<String> data) {
            this.data = data;
        }

        void handleDataState(int state) {
            int outState = ImportManager.TASK_FAIL;

            switch (state) {
                case ImportDataRunnable.STATE_COMPLETE:
                    outState = ImportManager.TASK_COMPLETE;
                    break;
                case ImportDataRunnable.STATE_FAIL:
                    outState = ImportManager.TASK_FAIL;
                    break;
            }

            handleState(outState);
        }

        private void handleState(int outState) {
            importManager.handleState(this, outState);
        }

        void setThread(Thread thread) {
            this.thread = thread;
        }
    }

    private static class ImportDataRunnable implements Runnable {
        private static final int STATE_COMPLETE = 1;
        private static final int STATE_FAIL = 2;

        private final ImportDataTask task;

        ImportDataRunnable(ImportDataTask task) {
            this.task = task;
        }

        @Override public void run() {
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);

            task.setThread(Thread.currentThread());

            try {
                final List<String> data = read(task.inputStream);

                task.setData(data);

                task.handleDataState(STATE_COMPLETE);

            } catch (IOException e) {
                task.handleDataState(STATE_FAIL);
            }
        }

        private List<String> read(InputStream inputStream) throws IOException {
            final List<String> data = new ArrayList<>();

            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                data.add(line);
            }

            inputStream.close();

            return data;
        }
    }
}
