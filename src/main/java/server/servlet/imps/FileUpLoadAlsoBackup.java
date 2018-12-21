package server.servlet.imps;

import server.entity.BackupProperties;
import server.entity.Result;
import server.entity.UploadResult;
import server.entity.WebProperties;
import server.servlet.beans.FileBackupOperation;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2017/12/14.
 * 上传完文件并且同步文件
 */
public class FileUpLoadAlsoBackup extends FileUpLoad {

    @Override
    protected void subHook(HttpServletRequest req, List<Result> resultList) {

        if (!BackupProperties.get().isAuto) return;

        if (resultList!=null && resultList.size() > 0){

                    String rootPath = WebProperties.get().rootPath;

                    ArrayList<String> paths = new ArrayList<>();

                    for (Result it : resultList){
                        if ( it instanceof UploadResult && it.code == RESULT_CODE.SUCCESS){
                            UploadResult uploadResult = (UploadResult) it;
                            paths.add(rootPath + uploadResult.relativePath); //添加文件的相对路径
                            if (!uploadResult.md5FileRelativePath.equals("node")){
                                paths.add(rootPath + uploadResult.md5FileRelativePath); //添加MD5文件的相对路径
                            }
                        }
                    }

                    new FileBackupOperation(paths).execute();
        }
    }
}
