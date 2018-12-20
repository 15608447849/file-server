package server.servlet.beans;


import bottle.backup.client.FtcBackupClient;
import bottle.ftc.tools.NetworkUtil;
import server.entity.BackupProperties;
import server.entity.Result;
import server.entity.WebProperties;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import static server.servlet.iface.Mservlet.RESULT_CODE.*;

/**
 * Created by user on 2017/12/14.
 */
public class FileBackupOperation {

    private ArrayList<String> fileItems;
    public FileBackupOperation(ArrayList<String> fileItems) {
      this.fileItems = fileItems;
    }

    public List<Result> execute() throws Exception{
        List<Result> resultList = new ArrayList<>();
        for (InetSocketAddress remoteAddress :  BackupProperties.get().remoteList){
            Result result = new Result();
            translate(remoteAddress,result);
            resultList.add(result);
        }
        return resultList;
    }

    private void translate(InetSocketAddress add, Result result) {
        if (!NetworkUtil.ping(add.getAddress().getHostAddress())){
            result.info(NETWORK_ANOMALY, add.getAddress().getHostAddress() +"网络不可达");
            return;
        }
        FtcBackupClient client =  BackupProperties.get().ftcBackupServer.getClient();

        if (fileItems==null || fileItems.size()==0){
            result.info(PARAM_ERROR);
            return;
        }

        File file;
        StringBuilder stringBuilder = new StringBuilder();
        for (String path : fileItems){
            file = new File(WebProperties.get().rootPath + path);
            try {
                if (file.exists()){
                    client.addBackupFile(file,add);
                }else{
                    throw new FileNotFoundException();
                }
            } catch (Exception e) {
                stringBuilder.append(path+",");
            }
        }
        if (stringBuilder.length() > 0){
            stringBuilder.deleteCharAt(stringBuilder.length()-1);
            result.info(FILE_NOT_FOUNT,"服务器不存在文件列表:["+stringBuilder.toString()+"]");
        }else{
            result.info(SUCCESS);
        }

    }
}
