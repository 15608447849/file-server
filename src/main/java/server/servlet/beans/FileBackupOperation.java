package server.servlet.beans;


import bottle.backup.client.FtcBackupClient;
import bottle.ftc.tools.Log;
import server.entity.BackupProperties;
import server.entity.Result;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.InetSocketAddress;
import java.util.ArrayList;

import static server.servlet.iface.Mservlet.RESULT_CODE.*;

/**
 * Created by user on 2017/12/14.
 */
public class FileBackupOperation {

    private ArrayList<String> filePaths;
    public FileBackupOperation(ArrayList<String> filePaths) {
        if (filePaths==null || filePaths.size()==0) throw new NullPointerException();
        this.filePaths = filePaths;
    }

    public void execute(){
        FtcBackupClient client =  BackupProperties.get().ftcBackupServer.getClient();
        for (String path : filePaths){
            File file = new File(path);
//            Log.i("同步文件 : "+file);
            client.addBackupFile(file);
        }
    }
}
