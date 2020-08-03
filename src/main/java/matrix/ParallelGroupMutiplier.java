package matrix;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName ParallelGroupMutiplier
 * @Description
 * @Author Fernando Juan(joven)
 * @Date 7/17/2020 5:06 PM
 * @Version 1.0
 **/
public class ParallelGroupMutiplier {

    public void multiply(double[][] matrix1, double[][] matrix2, double[][] res) throws InterruptedException {
        double[][] matrixRes = new double[matrix1.length][matrix2[0].length];
        int row = matrix1.length;

        int processorNum = Runtime.getRuntime().availableProcessors();
        int startIndex = 0;
        int step = row/processorNum;
        int endIndex = step;

        List<Thread> threads = new ArrayList<>(10);
        for (int i = 0; i <processorNum ; i++) {
            Thread thread = new Thread(new GroupMultiplierTask(matrixRes,matrix1,matrix2,startIndex,endIndex));
            thread.start();
            threads.add(thread);
            startIndex = endIndex;

            endIndex= i==processorNum-2?row:endIndex+step;
        }
    }

    private void wait4Finish (List<Thread> threads) throws InterruptedException {
        for (Thread thread: threads) {
            //等待该现成执行完毕
            thread.join();
        }
    }
}
