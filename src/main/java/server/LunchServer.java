package server;


import IceBox.Server;
import bottle.backup.client.FtcBackupClient;
import bottle.backup.server.Callback;
import bottle.backup.server.FtcBackupServer;
import bottle.ftc.tools.Log;
import bottle.ftc.tools.NetworkUtil;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.PathHandler;
import io.undertow.server.handlers.resource.PathResourceManager;
import io.undertow.server.handlers.resource.ResourceHandler;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.impl.DefaultFtpServer;
import org.apache.ftpserver.listener.Listener;
import org.apache.ftpserver.listener.nio.NioListener;
import org.apache.ftpserver.usermanager.ClearTextPasswordEncryptor;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import server.entity.BackupProperties;
import server.entity.FtpInfo;
import server.entity.WebProperties;
import server.servlet.imps.FileUpLoad;
import server.servlet.imps.FileUpLoadAlsoBackup;
import server.servlet.imps.GenerateZip;
import server.servlet.imps.Online;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.file.Paths;
import java.util.Properties;

import static io.undertow.servlet.Servlets.servlet;


/**
 * Created by lzp on 2017/5/13.
 * 容器入口
 */
public class LunchServer {

    public static void main(String[] args) {

        //开启文件备份服务
        startFileBackupServer();
        //开启FTP服务器
        startFTPServer();
        //开启web文件服务器
        startWebServer();
    }

    private static void startWebServer() {
        try {
            //开启web文件服务器
            DeploymentInfo servletBuilder = io.undertow.servlet.Servlets.deployment()
                    .setClassLoader(LunchServer.class.getClassLoader())
                    .setContextPath("/")
                    .setDeploymentName("file_server.war")
                    .setResourceManager(
                            new PathResourceManager(Paths.get(WebProperties.get().rootPath), 16*4069L)
                    );
//            servletBuilder.addServlets(io.undertow.servlet.Servlets.servlet("文件上传", FileUpLoad.class).addMapping("/upload"));
            servletBuilder.addServlet(servlet("文件上传并同步", FileUpLoadAlsoBackup.class).addMapping("/upload"));
            servletBuilder.addServlet(servlet("服务器在线监测", Online.class).addMapping("/online"));
            servletBuilder.addServlet(servlet("指定文件列表生成zip", GenerateZip.class).addMapping("/zip"));

            DeploymentManager manager = Servlets.defaultContainer().addDeployment(servletBuilder);

            manager.deploy();

            HttpHandler httpHandler = manager.start();

            //默认处理程序 - 文件资源管理器
            PathHandler pathHandler =
                    Handlers.path(httpHandler);

            Undertow.builder()
                    .addHttpListener(WebProperties.get().webPort, WebProperties.get().webIp, pathHandler)
                    .build()
                    .start();

            Log.i("已启动HTTP服务");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void startFTPServer() {
        try{
            FtpServer server;
            try {
                //配置文件存在
                FileSystemXmlApplicationContext ctx = new FileSystemXmlApplicationContext(String.valueOf(LunchServer.class.getResource("/ftpconfig.xml")));
                if(ctx.containsBean("server")) {
                    server = (FtpServer)ctx.getBean("server");
                } else {
                    String[] beanNames = ctx.getBeanNamesForType(FtpServer.class);
                    if(beanNames.length >= 1) {
                        server = (FtpServer)ctx.getBean(beanNames[0]);
                    } else {
                        throw new IllegalArgumentException("无效的ftp配置信息");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                server = ftpApplicationDefault();
            }
            server.start();

            if (server instanceof DefaultFtpServer){
                int port = 21;
                String host = WebProperties.get().webIp;
                String user,pass,home;
                DefaultFtpServer dfs = (DefaultFtpServer)server;
                 Listener listener = dfs.getListener("default");
                if(listener!=null && listener instanceof NioListener){
                    NioListener nio =  (NioListener)listener;
                    port =nio.getPort();
                }
                user = dfs.getUserManager().getAdminName();
                home =  dfs.getUserManager().getUserByName(user).getHomeDirectory();
                InputStream is = null;
                try{
                    is = LunchServer.class.getResourceAsStream("/ftpusers.properties");
                    Properties properties = new Properties();
                    properties.load(is);
                    pass = properties.getProperty("ftpserver.user.admin.userpassword");
                }catch (Exception e){
                    pass = user;
                }finally{
                    if(is!=null){
                        try{is.close();}catch(Exception es){}
                    }
                }
                FtpInfo.get().setInfo(host,port,user,pass);
                Log.i("已启动FTP服务");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static FtpServer ftpApplicationDefault() throws Exception{
        PropertiesUserManagerFactory propertiesUserManagerFactory = new PropertiesUserManagerFactory();
        propertiesUserManagerFactory.setFile(new File(String.valueOf(LunchServer.class.getResource("/ftpusers.properties"))));
        propertiesUserManagerFactory.setPasswordEncryptor(new ClearTextPasswordEncryptor());
        FtpServerFactory ftpServerFactory = new FtpServerFactory();
        ftpServerFactory.setUserManager( propertiesUserManagerFactory.createUserManager());
        return ftpServerFactory.createServer();
    }


    private static void startFileBackupServer() {
        try {
            BackupProperties.get().ftcBackupServer = new FtcBackupServer(WebProperties.get().rootPath,WebProperties.get().webIp, BackupProperties.get().localPort,64,5000);

            BackupProperties.get().ftcBackupServer.setCallback(file -> {
                BackupProperties.get().ftcBackupServer.getClient().addBackupFile(file);
            });

            FtcBackupClient client = BackupProperties.get().ftcBackupServer.getClient();
            client.addFilterSuffix(".tmp");
            client.addServerAddress(BackupProperties.get().remoteList);
            if (BackupProperties.get().isBoot){
                client.ergodicDirectory();
            }
            client.setTime(BackupProperties.get().time);
//            client.watchDirectory(true);
            Log.i("已启动BACKUP服务");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}




