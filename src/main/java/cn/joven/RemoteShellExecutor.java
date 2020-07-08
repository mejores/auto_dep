package cn.joven;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * @ClassName RemoteShellExecutor
 * @Description
 * @Author Fernando Juan(joven)
 * @Date 7/3/2020 5:56 PM
 * @Version 1.0
 **/
public class RemoteShellExecutor {
    private Connection conn;
    private String ip;
    private String osUsername;
    private String password;
    private String charset = Charset.defaultCharset().toString();
    private static final int TIME_OUT = 300000;

    public RemoteShellExecutor(String ip, String usr, String pasword) {
        this.ip = ip;
        this.osUsername = usr;
        this.password = pasword;
    }

    private boolean login() throws IOException {
        this.conn = new Connection(this.ip);
        this.conn.connect();
        return this.conn.authenticateWithPassword(this.osUsername, this.password);
    }

    public int exec(String cmds) throws Exception {
        InputStream stdOut = null;
        InputStream stdErr = null;
        boolean var6 = true;

        int ret;
        try {
            if (!this.login()) {
                Log.error("Failed to log on to the remote machine {}", new Object[]{this.ip});
                throw new Exception("鐧诲綍杩滅▼鏈哄櫒澶辫触" + this.ip);
            }

            Session session = this.conn.openSession();
            session.execCommand(cmds);
            stdOut = new StreamGobbler(session.getStdout());
            String outStr = this.processStream(stdOut, this.charset);
            stdErr = new StreamGobbler(session.getStderr());
            String outErr = this.processStream(stdErr, this.charset);
            session.waitForCondition(32, 300000L);
            if (!"".equals(stdOut)) {
                Log.info("shell outStr :\n{}", new Object[]{outStr});
            }

            if (!"".equals(outErr)) {
                Log.info("shell outErr = {}", new Object[]{outErr});
            }

            ret = session.getExitStatus();
        } finally {
            if (this.conn != null) {
                this.conn.close();
            }

            if (stdOut != null) {
                stdOut.close();
            }

            if (stdErr != null) {
                stdErr.close();
            }

        }

        return ret;
    }

    private String processStream(InputStream in, String charset) throws Exception {
        byte[] buf = new byte[1024];
        StringBuilder sb = new StringBuilder();

        while(in.read(buf) != -1) {
            sb.append(new String(buf, charset));
        }

        return sb.toString();
    }

    public static void main(String[] args) throws Exception {
        RemoteShellExecutor executor = new RemoteShellExecutor("120.79.172.144", "dev", "youxiuqn#@$2020");
        System.out.println(executor.exec("/home/dev/test.sh"));
    }
}
