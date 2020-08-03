package matrix;

import java.util.concurrent.ForkJoinPool;

/**
 * @ClassName TestMain
 * @Description
 * @Author Fernando Juan(joven)
 * @Date 7/17/2020 5:14 PM
 * @Version 1.0
 **/
public class TestMain {
    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = new ForkJoinPool(3,ForkJoinPool.defaultForkJoinWorkerThreadFactory, null,false);
        //forkJoinPool.submit()
    }
}
