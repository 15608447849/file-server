package server.servlet.imps;

import com.winone.ftc.mtools.FileUtil;
import com.winone.ftc.mtools.Log;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;
import server.entity.Result;
import server.entity.WebProperties;
import server.servlet.iface.Mservlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static server.servlet.iface.Mservlet.RESULT_CODE.EXCEPTION;
import static server.servlet.iface.Mservlet.RESULT_CODE.FILE_NOT_FOUNT;
import static server.servlet.iface.Mservlet.RESULT_CODE.UNKNOWN;

/**
 * Created by user on 2018/12/17.
 * 传递 需要打包下载的文件的相对路径
 * 生成zip包
 * 请求重定向
 */
public class GenerateZip extends Mservlet {

    private final String ZIP_TEMP_DIR_PREV = "zip_dir_temp_";

    /**
     * 文件夹压缩zip
     * @param dir 指定文件夹
     * @return 压缩包相对路径
     */
    private String compressZip(File dir) throws Exception{
            String zipPath = File.separator + dir.getName()+".zip";
            File zipFile = new File(WebProperties.get() + zipPath);
            if (zipFile.exists()) zipFile.delete();
            Project prj = new Project();
            Zip zip = new Zip();
            zip.setProject(prj);
            zip.setDestFile(zipFile);
            FileSet fileSet = new FileSet();
            fileSet.setProject(prj);
            fileSet.setDir(dir);
            zip.addFileset(fileSet);
            zip.execute();
            return zipPath;
    }

    /**
     *
     * @param paths 需要打包的文件的相对路径 例如: /Music/jcs.msv
     * @return 存放了需要打包文件的临时目录全路径
     */
    private File cpFileListToDir(List<String> paths) throws  Exception{
            String homePath = WebProperties.get().rootPath;

            String dirPath = homePath + FileUtil.SEPARATOR + ZIP_TEMP_DIR_PREV +System.currentTimeMillis();

            File dir = new File(dirPath);

            if (dir.exists()) dir.delete();

            dir.mkdirs();

            int size = paths.size();

            File temp ;

            String path;

            for (int i = 0;i<size;i++){
                path = paths.get(i);

                if (!path.startsWith(FileUtil.SEPARATOR)) path = FileUtil.SEPARATOR+ path;//保证前面有 '/'

                temp = new File(homePath  + path);

                if (temp.exists()){
                    FileUtil.copyFile(temp, new File(dirPath + path));
                }
                else throw new FileNotFoundException();
            }

            return dir;
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
        Result result = new Result<>().Info(UNKNOWN);
        List<String> pathList = filterData(req.getHeader("path-list"));
        Log.i("zip 文件列表: "+ pathList);
        try {
            String zipPath  = compressZip(cpFileListToDir(pathList));
            //返回ZIP包URL ,注册定时器 - 10分钟后自动删除
            String url = String.format(Locale.CANADA,"http://%s:%d%s",
                    WebProperties.get().webIp,
                    WebProperties.get().webPort,
                    zipPath
            );
            Log.i("zip URL : " + url);
            resp.sendRedirect(url);
        } catch (Exception e) {
            e.printStackTrace();
            result.Info(EXCEPTION);
            writeJson(resp,result);
        }
    }
}
