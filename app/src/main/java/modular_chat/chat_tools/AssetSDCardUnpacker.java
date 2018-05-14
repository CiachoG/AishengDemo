package modular_chat.chat_tools;

import android.content.Context;
import android.content.res.AssetManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class AssetSDCardUnpacker {
    public static void work(Context context,String sdTargetPath, String assetsPath)throws Exception{
        File file=new File(sdTargetPath);
        if(file.exists())     //手机缓存中已有，可不用再从资产中读取exl文件
            return;

        AssetManager assetManager=context.getAssets();
        InputStream inputStream=assetManager.open(assetsPath);
        FileOutputStream outputStream=new FileOutputStream(file);

        byte[] buffer = new byte[1024];
        int byteCount=0;
        while((byteCount=inputStream.read(buffer))!=-1)
            outputStream.write(buffer,0,byteCount);

        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }
}
