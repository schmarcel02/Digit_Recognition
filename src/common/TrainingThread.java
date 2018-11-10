package common;

import java.util.List;

public class TrainingThread extends Thread{
    private TrainingManager manager;

    private List<DigitImage> digits;
    private volatile boolean running = false, training = false;
    private int start, end;

    TrainingThread(TrainingManager manager, List<DigitImage> digits) {
        this.manager = manager;
        this.digits = digits;
    }

    @Override
    public void run() {
        running = true;
        manager.ask(this);
        while (running) {
            while (!training) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (running) {
                DigitImage image = null;
                for (int i = start; i < end; i++) {
                    image = digits.get(i);
                    NN.train(image.imageData, image.label);
                }
                if (image != null) {
                    int guess = NN.feedForward(image.imageData);
                    System.out.println("Label: " + image.label + " Guess: " + guess + (image.label == guess ? " Rite" : " Wong on so many levels"));
                }
                manager.ask(this);
            }
        }
    }

    void trainBunch(int start, int end) {
        this.start = start;
        this.end = end;
        training = true;

    }

    void stopThread() {
        running = false;
        training = true;
    }
}
