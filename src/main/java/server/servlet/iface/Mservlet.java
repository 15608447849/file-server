package server.servlet.iface;

import bottle.ftc.tools.FileUtil;
import bottle.ftc.tools.Log;
import bottle.ftc.tools.StringUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import server.entity.WebProperties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/31.
 */
public class Mservlet extends javax.servlet.http.HttpServlet {


    public interface RESULT_CODE {
        int UNKNOWN = 199;
        int SUCCESS = 200;
        int EXCEPTION = 400;
        int NETWORK_ANOMALY = 405;
        int PARAM_ERROR = 406;
        int FILE_NOT_FOUNT = 407;

    }

    private final String PARAM_SEPARATOR = ";";

    //跨域
    protected void filter(HttpServletRequest req,HttpServletResponse resp) {
//        http://www.ruanyifeng.com/blog/2016/04/cors.html
        try {
            resp.setCharacterEncoding("UTF-8");
            req.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods","GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers",
                        "specify-path,specify-filename,save-md5,path-list,excel-path,ergodic-sub");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        filter(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        filter(req,resp);

    }

    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        filter(req,resp);
        super.doOptions(req, resp);
    }

    protected ArrayList<String> filterData(String data) {
        ArrayList<String> dataList = new ArrayList<>();
        try {
            if (!StringUtil.isEntry(data)) {
                data = URLDecoder.decode(data, "UTF-8");
                if (data.contains(PARAM_SEPARATOR)) {
                    String[] pathArray = data.split(PARAM_SEPARATOR);
                    for (String path : pathArray) {
                        dataList.add(path);
                    }
                } else {
                    dataList.add(data);
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return dataList;
    }

    protected String checkDirPath(String path) {
            path = path.replace("\\\\", FileUtil.SEPARATOR);
            if (!path.startsWith(FileUtil.SEPARATOR)) path = FileUtil.SEPARATOR + path;//保证前面有 '/'
            if (!path.endsWith(FileUtil.SEPARATOR)) path += FileUtil.SEPARATOR; //后面保证 '/'
            return path;
    }


    protected <T> T getJsonObject(HttpServletRequest req, String headerKey, Class<T> clazzType) throws JsonSyntaxException {

        final String json = req.getHeader(headerKey);
        if (json != null) {
            T t = new Gson().fromJson(json, clazzType);
            return t;
        }
        return null;
    }


    protected void writeString(HttpServletResponse resp, String str, boolean isClose) {
        try {
            PrintWriter out = resp.getWriter();
            out.write(str);
            out.flush();
            if (isClose) out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void writeJson(HttpServletResponse resp, Object o) {
        writeString(resp, new Gson().toJson(o), true);
    }

}
