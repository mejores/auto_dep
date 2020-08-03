package search;

import java.io.File;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @ClassName ParallelGroupFileTask
 * @Description
 * @Author Fernando Juan(joven)
 * @Date 7/17/2020 5:49 PM
 * @Version 1.0
 **/
public class ParallelGroupFileTask implements Runnable {
    private final String fileName;
    private final ConcurrentLinkedQueue<File> directories;
    private final Result parallelResult;
    private boolean found;

    public ParallelGroupFileTask(String fileName, ConcurrentLinkedQueue<File> directories, Result parallelResult) {
        this.fileName = fileName;
        this.directories = directories;
        this.parallelResult = parallelResult;
    }

    @Override
    public void run() {
        while (directories.size()>0){
            File file = directories.poll();
            try {
                processDirectory(file,fileName,parallelResult);
                if(found){
                    System.out.println("文件找到了");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param file
     * @param fileName
     * @param parallelResult
     */
    private void processDirectory(File file, String fileName,Result parallelResult) throws InterruptedException {
        File[] contents = file.listFiles();
        if(contents == null || contents.length == 0){
            return;
        }
        for (File content : contents){
            if(file.isDirectory()){
                processDirectory(file,fileName,parallelResult);
            }else {
                if (content.getName().equals(fileName)) {
                    parallelResult.setPath(content.getAbsolutePath());
                    this.found = true;
                }
            }
            //当前现成是否被中断
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }
            if (found) {
                return;
            }
        }
    }


    public boolean getFound() {
        return found;
    }
}
