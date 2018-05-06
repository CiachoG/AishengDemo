package modular_forum;

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import modular_forum.modular_forum_detail.ForumPostDetailListRow;
import modular_forum.modular_forum_detail.ForumPostDetailTopRow;
import modular_forum.modular_forum_main.ForumListRow;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ForumDataLoader {
    private static final String TargetURL="http://47.100.170.83:8080/";
    public static final SimpleDateFormat DATE_FORMAT=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //加载论坛界面第page页的帖子数据
    public static List<ForumListRow> loadingForumListDara(int page){
        List<ForumListRow>dataList=null;
        try{
            OkHttpClient client=new OkHttpClient();

            //提交请求数据给服务器
            RequestBody reqBody=new FormBody.Builder().
                    add("page",page+"").build();

            Request request=new Request.Builder().
                    url(TargetURL+"aisheng/GetPost").post(reqBody).build();

            Response response=client.newCall(request).execute();
            String jsonResult=response.body().string();
            jsonResult=new String(jsonResult.getBytes("UTF-8"));

            if(jsonResult.equals("false"))
                throw new Exception("请求帖子异常");

            Gson gson=new Gson();
            dataList=gson.fromJson(jsonResult,new TypeToken<List<ForumListRow>>(){}.getType());

        }catch (Exception e){
            Log.e("加载论坛帖子列表数据出错:",e.toString());
        }
        if(dataList==null)  return new ArrayList<>();
        return dataList;
    }

    //加载帖子顶层数据
    public static ForumPostDetailTopRow loadingPostDetailTopData(String PostId){
        ForumPostDetailTopRow topItem=null;
        try{
            OkHttpClient client=new OkHttpClient();

            RequestBody reqBody=new FormBody.Builder().
                    add("PostId",PostId+"").build();

            Request request=new Request.Builder().
                    url(TargetURL+"aisheng/GetPost").post(reqBody).build();

            Response response=client.newCall(request).execute();
            String jsonResult=response.body().string();
            jsonResult=new String(jsonResult.getBytes("UTF-8"));

            Gson gson=new Gson();
            topItem=gson.fromJson(jsonResult,new TypeToken<ForumPostDetailTopRow>(){}.getType());
        }catch (Exception e){
            Log.e("加载帖子顶层内容数据出错:",e.toString());
        }
        return topItem;
    }

    //加载帖子内的评论数据
    public static List<ForumPostDetailListRow> loadingPostDetailCommData(String PostId,int page){
        List<ForumPostDetailListRow>dataList=new ArrayList<>();
        try{
            OkHttpClient client=new OkHttpClient();
            RequestBody reqBody=new FormBody.Builder().
                    add("PostId",PostId+"")
                    .add("page",page+"").build();

            Request request=new Request.Builder().
                    url(TargetURL+"aisheng/GetComment").post(reqBody).build();

            Response response=client.newCall(request).execute();
            String jsonResult=response.body().string();
            jsonResult=new String(jsonResult.getBytes("UTF-8"));

            if(!jsonResult.equals("[]")){
                Gson gson=new Gson();
                dataList=gson.fromJson(jsonResult,new TypeToken<List<ForumPostDetailListRow>>(){}.getType());
            }
        }catch (Exception e){
            Log.e("加载帖子评论内容数据出错:",e.toString());
        }
        return dataList;
    }

    //发帖
    public static String sendPostData(String PostTitle,String PostDate,String PostContent,String UserId){
        String result="";
        try{
            OkHttpClient client=new OkHttpClient();

            PostTitle=new String(PostTitle.getBytes(),"UTF-8");
            PostDate=new String(PostDate.getBytes(),"UTF-8");
            PostContent=new String(PostContent.getBytes(),"UTF-8");
            UserId=new String(UserId.getBytes(),"UTF-8");

            RequestBody reqBody=new FormBody.Builder().
                    add("PostTitle",PostTitle+"").
                    add("PostDate",PostDate+"").
                    add("PostContent",PostContent+"").
                    add("UserId",UserId+"").
                    build();

            Request request=new Request.Builder().
                    url(TargetURL+"aisheng/InsertPost").post(reqBody).build();

            Response response=client.newCall(request).execute();
            result=response.body().string();

            result=new String(result.getBytes("UTF-8"));
        }catch (Exception e){
            Log.e("发帖数据解析错误:",e.toString());
        }
        return result;
    }

    //提交评论数据
    public static String sendCommentData(String CommContent,String CommDate,String UserId, String PostId){
        String result="";
        try{
            OkHttpClient client=new OkHttpClient();

            CommContent=new String(CommContent.getBytes(),"UTF-8");
            CommDate=new String(CommDate.getBytes(),"UTF-8");
            UserId=new String(UserId.getBytes(),"UTF-8");
            PostId=new String(PostId.getBytes(),"UTF-8");

            RequestBody reqBody=new FormBody.Builder().
                    add("CommContent",CommContent+"").
                    add("CommDate",CommDate+"").
                    add("UserId",UserId+"").
                    add("PostId",PostId+"").
                    build();

            Request request=new Request.Builder().
                    url(TargetURL+"aisheng/InsertComment").post(reqBody).build();

            Response response=client.newCall(request).execute();
            result=response.body().string();
            result=new String(result.getBytes("UTF-8"));
        }catch (Exception e){
            Log.e("发表评论出错:",e.toString());
        }
        return result;
    }
}
