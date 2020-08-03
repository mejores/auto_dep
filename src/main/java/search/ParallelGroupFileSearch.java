package search;

import java.io.File;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;

/**
 * @ClassName ParallelGroupFileSearch
 * @Description
 * @Author Fernando Juan(joven)
 * @Date 7/17/2020 6:12 PM
 * @Version 1.0
 **/
public class ParallelGroupFileSearch {
    public static void searchFiles(File file, String fileName,Result parallelResult){
        ConcurrentLinkedQueue directories = new ConcurrentLinkedQueue();
        File[] contents = file.listFiles();
        if(contents == null || contents.length == 0){
            for (File content :
                    contents) {
                directories.add(content);
            }
        }
        int processors = Runtime.getRuntime().availableProcessors();
        Thread[] threads = new Thread[processors];
        ParallelGroupFileTask[] tasks = new ParallelGroupFileTask[processors];

        //生成任务
        for (int i = 0; i <processors; i++) {
            tasks[i] = new ParallelGroupFileTask(fileName,directories,parallelResult);
            threads[i] = new Thread(tasks[i]);
            threads[i].start();
        }

        boolean finish = false;
        int numFinished = 0;

        //循环判断是否已经完成文件查找
        while (!finish) {
            numFinished = 0;
            for (int i = 0; i < threads.length; i++) {
                if (threads[i].getState() == Thread.State.TERMINATED) {
                    numFinished++;
                    if (tasks[i].getFound()) {
                        finish = true;
                    }
                }
            }
            if (numFinished == threads.length) {
                finish = true;
            }
        }
        //如果文件已经找到，但是还有现成在运行，就终止其它线程
        if (numFinished != threads.length) {
            for (Thread thread : threads) {
                thread.interrupt();
            }
        }
    }



}
