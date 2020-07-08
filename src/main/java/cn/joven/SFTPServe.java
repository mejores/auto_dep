package cn.joven;

import com.jcraft.jsch.*;

import java.io.*;
import java.util.Properties;
import java.util.Vector;

/**
 * @ClassName SFTPServe
 * @Description
 * @Author Fernando Juan(joven)
 * @Date 7/3/2020 5:56 PM
 * @Version 1.0
 **/
public class SFTPServe {
    private ChannelSftp sftp;
    private Session session;
    private String username;
    private String password;
    private String privateKey;
    private String host;
    private int port;

    public SFTPServe() {
    }

    public SFTPServe(String username, String password, String host, int port) {
        this.username = username;
        this.password = password;
        this.host = host;
        this.port = port;
    }

    public SFTPServe(String username, String host, int port, String privateKey) {
        this.username = username;
        this.host = host;
        this.port = port;
        this.privateKey = privateKey;
    }

    public void Login() {
        try {
            JSch jsch = new JSch();
            if (this.privateKey != null) {
                jsch.addIdentity(this.privateKey);
                Log.info("sftp connect,path of private key file锛歿}", new Object[]{this.privateKey});
            }

            Log.info("sftp connect by host:{} username:{}", new Object[]{this.host, this.username});
            this.session = jsch.getSession(this.username, this.host, this.port);
            Log.info("Session is build", new Object[0]);
            if (this.password != null) {
                this.session.setPassword(this.password);
            }

            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            this.session.setConfig(config);
            this.session.connect();
            Log.info("Session is connected", new Object[0]);
            Channel channel = this.session.openChannel("sftp");
            channel.connect();
            Log.info("channel is connected", new Object[0]);
            this.sftp = (ChannelSftp)channel;
            Log.info(String.format("sftp server host:[%s] port:[%s] is connect successfull", this.host, this.port), new Object[0]);
        } catch (JSchException var4) {
            Log.error("Cannot connect to specified sftp server : {}:{} \n Exception message is: {}", new Object[]{this.host, this.port, var4.getMessage()});
        }

    }

    public void Logout() {
        if (this.sftp != null && this.sftp.isConnected()) {
            this.sftp.disconnect();
            Log.info("sftp is closed already", new Object[0]);
        }

        if (this.session != null && this.session.isConnected()) {
            this.session.disconnect();
            Log.info("sshSession is closed already", new Object[0]);
        }

    }

    public void upload(String directory, String sftpFileName, InputStream input) throws SftpException {
        try {
            this.sftp.cd(directory);
        } catch (SftpException var5) {
            Log.error("directory is not exist", new Object[0]);
            this.sftp.mkdir(directory);
            this.sftp.cd(directory);
        }

        this.sftp.put(input, sftpFileName);
        Log.info("file:{} is upload successful", new Object[]{sftpFileName});
    }

    public void upload(String directory, String uploadFile) throws FileNotFoundException, SftpException {
        File file = new File(uploadFile);
        this.upload(directory, file.getName(), (InputStream)(new FileInputStream(file)));
    }

    public void upload(String directory, String sftpFileName, byte[] byteArr) throws SftpException {
        this.upload(directory, sftpFileName, (InputStream)(new ByteArrayInputStream(byteArr)));
    }

    public void upload(String directory, String sftpFileName, String dataStr, String charsetName) throws UnsupportedEncodingException, SftpException {
        this.upload(directory, sftpFileName, (InputStream)(new ByteArrayInputStream(dataStr.getBytes(charsetName))));
    }

    public void download(String directory, String downloadFile, String saveFile) throws SftpException, FileNotFoundException {
        if (directory != null && !"".equals(directory)) {
            this.sftp.cd(directory);
        }

        File file = new File(saveFile);
        this.sftp.get(downloadFile, new FileOutputStream(file));
        Log.info("file:{} is download successful", new Object[]{downloadFile});
    }

    public void delete(String directory, String deleteFile) throws SftpException {
        this.sftp.cd(directory);
        this.sftp.rm(deleteFile);
    }

    public Vector<?> listFiles(String directory) throws SftpException {
        return this.sftp.ls(directory);
    }

    public static void main(String[] args) throws SftpException, IOException {
        SFTPServe sftp = new SFTPServe("dev", "youxiuqn#@$2020", "120.79.172.144", 22);
        sftp.Login();
        File file = new File("/idcard.zip");
        InputStream is = new FileInputStream(file);
        sftp.upload("/home/dev", file.getName(), (InputStream)is);
        Log.info("涓婁紶缁撴潫锛�", new Object[0]);
        sftp.Logout();
    }
}
