package com.example.music.utils;


import com.example.music.entity.pojo.ResultObjectModel;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Component
public class QiNiuYunUtils {
    private static final String ACCESS_KEY = "9H79eq_nap9cC8zflgmVQgEaAQzpR8zroyAyi4Uz";
    private static final String SECRET_KEY = "NhlQIUifNnNe7T2XkXD6t-WSeLlMB0C2J3L3mlV-";
    private static final String BUCKET = "musiclsw";
    private Auth auth;
    private Configuration configuration;
    private UploadManager uploadManager;
    private String []imgType = {"BMP","JPG","PNG","GIF","JPEG"};
    private String []musicType = {"MP3","FLAC","APE"};

    public QiNiuYunUtils(){
        auth = Auth.create(ACCESS_KEY,SECRET_KEY);
        configuration = new Configuration(Region.autoRegion());
        uploadManager = new UploadManager(configuration);
    }

    public ResultObjectModel upload(int type, MultipartFile ... files){
        if (checkFileIsEmpty(files)) {
            return ResultObjectModel.fail("部分文件为空！");
        }
        boolean flag = checkFileType(type,files);
        if (!flag) {
            return ResultObjectModel.fail("文件类型不符合要求！");
        }
        String token = auth.uploadToken(BUCKET);
        List<String> list = new ArrayList<>();
        try {
            for (MultipartFile file : files) {
                String key = file.getOriginalFilename();
                Response put = uploadManager.put(file.getBytes(), key, token);
                System.out.println(put.toString());
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResultObjectModel.fail("上传失败！");
        }
        return null;
    }

    private  boolean checkFileType(int type, MultipartFile ...files) {
        switch (type) {
            //图片
            case 1:
                for (MultipartFile file : files) {
                    String fileName = file.getOriginalFilename();
                    String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
                    if (!isContain(suffix,imgType)) {
                        return false;
                    }
                }
                break;
            //歌曲
            case 2:
                for (MultipartFile file : files) {
                    String fileName = file.getOriginalFilename();
                    String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
                    if (!isContain(suffix,musicType)) {
                        return false;
                    }
                }
                break;
            //歌词
            case 3:
                for (MultipartFile file : files) {
                    String fileName = file.getOriginalFilename();
                    String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
                    if(!"lrc".equals(suffix)){
                        return false;
                    }
                }
                break;
        }
        return true;
    }

    private static boolean isContain(String suffix, String[] imgType) {
        for (String type : imgType) {
            if (type.equalsIgnoreCase(suffix)) {
                return true;
            }
        }
        return false;
    }

    private static boolean checkFileIsEmpty(MultipartFile ...files) {
        for (MultipartFile file : files) {
            System.out.println(file.getSize());
            if (file.isEmpty()) {
                return true;
            }
        }
        return false;
    }
}
