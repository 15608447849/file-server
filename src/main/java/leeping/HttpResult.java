package leeping;

import java.io.File;
import java.io.InputStream;
import java.util.*;

public class HttpResult implements HttpUtil.Callback  {

    String text;

    void bindParam(StringBuffer sb,Map<String,String > map){
        Iterator<Map.Entry<String,String>> it = map.entrySet().iterator();
        Map.Entry<String,String> entry ;

        while (it.hasNext()) {
            entry = it.next();
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        sb.deleteCharAt(sb.length()-1);
    }

    public HttpResult accessUrl(String url){
        new HttpUtil.Request(url,this).setReadTimeout(1000).setConnectTimeout(1000).text().execute();
        return this;
    }

    private List<String> pathList = new ArrayList<>();
    private List<String> nameList = new ArrayList<>();
    private List<HttpUtil.FormItem> formItems = new ArrayList<>();

    public HttpResult addFile(File file,String remotePath,String remoteFileName){
        if (remotePath==null) remotePath = "/java/";
        if (remoteFileName==null) remoteFileName = file.getName();
        pathList.add(remotePath);
        nameList.add(remoteFileName);
        formItems.add(new HttpUtil.FormItem("file", file.getName(), file));
        return this;
    }

    public HttpResult addStream(InputStream stream, String remotePath, String remoteFileName){
        if (remotePath==null) remotePath = "/java/";
        if (remoteFileName==null) throw new NullPointerException("需要上传的远程文件名不可以为空");
        pathList.add(remotePath);
        nameList.add(remoteFileName);
        formItems.add(new HttpUtil.FormItem("file", remoteFileName, stream));
        return this;
    }

    public static String join(List list, String separator) {
        StringBuffer sb = new StringBuffer();
        for (Object obj : list){
            sb.append(obj.toString()).append(separator);
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public HttpResult fileUpload(String url){
        HashMap<String,String> headParams = new HashMap<>();

        headParams.put("specify-path",join(pathList,";"));

        headParams.put("specify-filename",join(nameList,";"));
        System.out.println(headParams);
        new HttpUtil.Request(url, HttpUtil.Request.POST, this)
                .setForm().setParams(headParams).addFormItemList(formItems).upload().execute();
        return this;
    }

    public String getRespondContent(){
        return text;
    }

    @Override
    public void onProgress(File file, long progress, long total) {
        //pass
    }

    @Override
    public void onResult(HttpUtil.Response response) {
        this.text = response.getMessage();
    }

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
        this.text = null;
    }

}
