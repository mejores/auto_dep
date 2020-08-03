package cn.joven;

import com.jcraft.jsch.SftpException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * @ClassName MMGoHome
 * @Description
 * @Author Fernando Juan(joven)
 * @Date 7/3/2020 5:53 PM
 * @Version 1.0
 **/
@Mojo(name = "install")
public class AutoDepHome extends AbstractMojo {
    @Parameter( property = "module", required = true)
    private String module;
    /**
     * 是否有父项目
     */
    @Parameter(defaultValue = "false",property = "hasParent", required = true)
    private Boolean hasParent;

    private static String remoteHost = "120.79.172.144";
    private static String username = "dev";
    private static String password = "youxiuqn#@$2020";
    private static Integer port = 22;
    private static String workspace = "/home/dev";


    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        //this.module = "manage";
        if(module == null || module.length()<3){
            Log.error("执行失败，请先配置module参数");
            return;
        }
        if(hasParent == null || module.length()<3){
            Log.info("未配置【hasParent】参数，使用默认值【false】");;
        }
       String projectPath =System.getProperty("user.dir");
        Log.info("the mode for deploying : {}", new Object[]{this.module});
        Log.info("----------------------------------MMGOHOME IS START--------------------------------------", new Object[0]);
        //File f = this.outputDirectory;

        Log.info("================installing=====================");
        try {
            //执行mvn打包
            mvnStart(module,hasParent);
            //获取已经打包好的jar文件
            Log.info("================check if installed=====================");
            String targetJarFile = mvnCheckFile(new File(projectPath+File.separator+"target"));
            Log.info("================installed=====================");
            Thread.sleep(5000L);
            //上传到服务器
            uploadJar(targetJarFile);
            //部署
            deploy(this.module);
        } catch (InterruptedException var4) {
            var4.printStackTrace();
            Log.error("执行失败",var4);
        }

        Log.info("部署完毕");
        Log.info("----------------------------------the program already got deployed--------------------------------------", new Object[0]);
    }

    public static void main(String[] args) throws Exception {
        Log.info("----------------------------------MMGOHOME IS START--------------------------------------", new Object[0]);
        File f = new File("D:\\workspace\\space01\\auto_dep\\target");
        //Object res = exec("mvn clean package -T 1C -Dmaven.compile.fork=true -Dmaven.test.skip=true&&exit");
        Log.info("================installing=====================");
        mvnStart("manage",false);
        //System.out.println(res);
        Log.info("================check if installed=====================");
        String targetJarFile = mvnCheckFile(f);
        Log.info("================installed=====================");
        uploadJar(targetJarFile);
        deploy("crm");
        Log.info("----------------------------------DEPLOYING IS OVER--------------------------------------", new Object[0]);
    }

    /**
     * 在远程服务器上进行部署
     * @param module
     */
    private static void deploy(String module) {
        RemoteShellExecutor executor = new RemoteShellExecutor(remoteHost, username, password);
        //String shell = workspace + "/rmt_deploy.sh " + module;
        String shell = "/data/deploy/rmt_deploy.sh " + module;
        int exec = -1;
        try {
            Log.info(" exec shell : {}", new Object[]{shell});
            exec = executor.exec(shell);
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        if (exec == 0) {
            Log.info("BOOT SUEECSS!", new Object[0]);
        } else {
            Log.error("BOOT FAILLURE!", new Object[0]);
        }

    }

    /**
     * 上传文件到远程服务器
     * @param targetJarFile
     */
    private static void uploadJar(String targetJarFile) {
        SFTPServe sftp = new SFTPServe(username, password, remoteHost, port);
        sftp.Login();
        Log.info("The file is ready to be uploaded : FILE NAME[{}]", new Object[]{targetJarFile});
        File file = new File(targetJarFile);
        FileInputStream is = null;

        try {
            is = new FileInputStream(file);
            sftp.upload(workspace, file.getName(), is);
        } catch (FileNotFoundException|SftpException var5) {
            var5.printStackTrace();
        }

        sftp.Logout();
    }

    /**
     * 打包程序
     */
    private static void mvnStart(String module, boolean hasParent) {

            String multipleModelsCommand = "cmd /C start /b mvn clean package -T 1C -pl yxqn-"+module+" -am -Dmaven.compile.fork=true -Dmaven.test.skip=true&&exit";
        String exec = "cmd /C start /b mvn clean package -T 1C -Dmaven.compile.fork=true -Dmaven.test.skip=true&&exit";
        File file = new File(System.getProperty("user.dir"));
        if(hasParent){
            exec = multipleModelsCommand;
            file = file.getParentFile();
        }
        Runtime runtime = Runtime.getRuntime();
        Process process = null;
        String result="";
        Log.info("........the package is getting packaged.......exec mvn package : {}", new Object[]{exec});
        try {
           // process = runtime.exec(exec);
            process = runtime.exec(exec,null,file);
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), Charset.forName("GBK")));
            Log.info("br is null:... "+(br == null));
            String line = null;
            while ((line = br.readLine()) != null) {
                if("".equals(line.trim())){
                    break;
                }
                Log.info(line);
                result+=line+"\n";
            }
            br.close();
            Log.info("close ... ");

            process.waitFor();
            System.out.println("wait over ...");
        } catch (InterruptedException var4) {
            var4.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.info("........the package got.......module : {}", module);
            process.destroy();
            Log.info("........the package already got packaged.......module : {}", new Object[]{exec});
    }
    /**
     * 检查已经打包的文件是否存在并返回路径
     * @param classPathFile
     * @return
     */
    private static String mvnCheckFile(File classPathFile) {
        Log.info("...............project target : " + classPathFile, new Object[0]);
        String targetFile = "";
        boolean over = false;
        int taskNum = 0;
        boolean clean = false;
        Log.info("...............mvn package is running, Please wait", new Object[0]);
        //循环检查文件是否已经打包好了
        while(true) {
            String[] files = classPathFile.list();
            if(files == null){
                continue;
            }
            int fileNum = files.length;
            if(fileNum <1){
                Log.info("文件还未打包完毕");
                continue;
            }else {
                Log.info("等待文件打包完毕：");
            }
            for(int i = 0; i < fileNum; ++i) {
                String singleItem = files[i];
                File file = new File(classPathFile, singleItem);
                if (file.getName().endsWith(".jar")) {
                    ++taskNum;
                    targetFile = classPathFile + File.separator + file.getName();
                }

                if (file.getName().endsWith(".jar.original")) {
                    ++taskNum;
                }

                if (taskNum == 2) {
                    Log.info("...............mvn package is over", new Object[0]);
                    return targetFile;
                }
            }

            taskNum = 0;
            //Log.info("...............mvn package got over", new Object[0]);
        }
    }
}
