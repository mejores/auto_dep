package matrix;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName ParallelRowMultiplier
 * @Description
 * @Author Fernando Juan(joven)
 * @Date 7/17/2020 4:47 PM
 * @Version 1.0
 **/
public class ParallelRowMultiplier {
    public void multiply(double[][] matrix1, double[][] matrix2, double[][] res) throws InterruptedException {
        double[][] matrixRes = new double[matrix1.length][matrix2[0].length];

        List<Thread> threads = new ArrayList<>(10);
        for (int i = 0; i <matrix1.length ; i++) {
            Thread thread = new Thread(new RowMultiplierTask(matrixRes,matrix1,matrix2,i));
            thread.start();
            threads.add(thread);
            if(threads.size()>=10){
                wait4Finish(threads);
                threads.clear();
            }
        }
    }

    private void wait4Finish (List<Thread> threads) throws InterruptedException {
        for (Thread thread: threads) {
            //等待该现成执行完毕
            thread.join();
        }
    }
}
