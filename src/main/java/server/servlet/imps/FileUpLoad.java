package server.servlet.imps;

import bottle.ftc.tools.FileUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import server.entity.Result;
import server.entity.UploadResult;
import server.entity.WebProperties;
import server.servlet.beans.FileUploadOperation;
import server.servlet.iface.Mservlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static server.servlet.iface.Mservlet.RESULT_CODE.EXCEPTION;
import static server.servlet.iface.Mservlet.RESULT_CODE.UNKNOWN;

/**
 * Created by lzp on 2017/5/13.
 * 文件上传接收
 */
public class FileUpLoad extends Mservlet {

    private final int _200M = 1024 * 1024 * 1024 * 200;



    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req,resp);

        List<Result> resultList = null;
        Result result = new Result<UploadResult>().info(UNKNOWN);
        //根据判断是否指定保存路径
        ArrayList<String> pathList = filterData(req.getHeader("specify-path"));
        if (pathList.size()>0){
            String path;
            for(int i=0;i<pathList.size();i++){
                path = pathList.get(i);
                path = checkDirPath(path);
                pathList.set(i,path);
            }
        }
        //根据判断是否指定保存文件名
        ArrayList<String> fileNameList = filterData( req.getHeader("specify-filename"));

        //判断是否保存成md5文件名
        ArrayList<String> fileSaveMD5 = filterData(req.getHeader("save-md5"));
        try {

            if (!ServletFileUpload.isMultipartContent(req)){
                throw new IllegalArgumentException("content-type is not 'multipart/form-data'");
            }

            DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
            diskFileItemFactory.setRepository(new File(WebProperties.get().tempPath));
            // 设定上传文件的值，如果上传文件大于200M，就可能在repository所代表的文件夹中产生临时文件，否则直接在内存中进行处理
            diskFileItemFactory.setSizeThreshold(_200M);
            // 创建一个ServletFileUpload对象
            ServletFileUpload uploader = new ServletFileUpload(diskFileItemFactory);

            List<FileItem> listItems = uploader.parseRequest(req);
            resultList = new FileUploadOperation(pathList,fileNameList,fileSaveMD5,listItems).execute();
            subHook(req,resultList);
        } catch (Exception e) {
            e.printStackTrace();
            result.info(EXCEPTION);
        }finally {
          //向客户端返回结果
          Object object = resultList == null ? result : resultList;
          writeJson(resp,object);
        }
    }
    protected void subHook(HttpServletRequest req, List<Result> resultList){}
}
