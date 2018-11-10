package common;

import java.util.List;

public class TrainingManager {
    private TrainingThread[] threads = new TrainingThread[Runtime.getRuntime().availableProcessors()];
    private List<DigitImage> digits;
    private int lastBunch = -1;
    private int stoppedThreads = 0;

    public TrainingManager(List<DigitImage> digits) {
        this.digits = digits;
    }

    public void trainAll() {
        lastBunch = -1;
        startThreads();
    }

    private void startThreads() {
        stoppedThreads = 0;
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new TrainingThread(this, digits);
            threads[i].start();
        }
    }

    void ask(TrainingThread thread) {
        int bunch = ++lastBunch;
        if (bunch < digits.size() / 1000) {
            thread.trainBunch(bunch * 1000, (bunch + 1) * 1000);
        } else {
            thread.stopThread();
            if (++stoppedThreads >= Runtime.getRuntime().availableProcessors())
                System.out.println("Training finished");
        }
    }

    public void waitForCompletion() {
        for (TrainingThread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
