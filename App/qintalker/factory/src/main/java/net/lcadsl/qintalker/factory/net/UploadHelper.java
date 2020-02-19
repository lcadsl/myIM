package net.lcadsl.qintalker.factory.net;


import android.text.format.DateFormat;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSAuthCredentialsProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;

import net.lcadsl.qintalker.factory.Factory;
import net.lcadsl.qintalker.utils.HashUtil;

import java.io.File;
import java.util.Date;

/**
 * 上传工具类，用于上传文件到阿里云OSS
 */
public class UploadHelper {
    private static final String TAG = UploadHelper.class.getSimpleName();

    private static final String ENDPOINT = "oss-cn-beijing.aliyuncs.com";
    //OSS存储库名字
    private static final String BUCKET_NAME = "qintalker";

    private static OSS getClient() {
        String endpoint = ENDPOINT;


        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider("LTAI4FjPht7fsQXwhHJQ74Zx", "OIeKJs8sZOOWuahs8hUAw40tvO7b9c");


        return new OSSClient(Factory.app(), ENDPOINT, credentialProvider);

    }


    /**
     * 上传的方法
     *
     * @param objKey 上传到仓库后文件的key
     * @param path   需要上传的文件的路径
     * @return 存储的地址
     */
    private static String upload(String objKey, String path) {
// 构造上传请求。
        PutObjectRequest request = new PutObjectRequest(BUCKET_NAME, objKey, path);

        try {
            //初始化上传
            OSS client = getClient();
            //开始上传（同步接口）
            PutObjectResult result = client.putObject(request);
            //得到一个外网可访问的地址
            String url = client.presignPublicObjectURL(BUCKET_NAME, objKey);
            //格式打印输出
            Log.d(TAG, String.format("PublicObjectURL:%s", url));
            return url;

        } catch (Exception e) {
            e.printStackTrace();
            //如果有异常则返回空
            return null;
        }
    }

    /**
     * 上传普通图片
     *
     * @param path 本地地址
     * @return 服务器地址
     */
    public static String uploadImage(String path) {
        String key = getImageObjKey(path);
        return upload(key, path);
    }

    /**
     * 上传头像图片
     *
     * @param path 本地地址
     * @return 服务器地址
     */
    public static String uploadPortrait(String path) {
        String key = getPortraitObjKey(path);
        return upload(key, path);
    }

    /**
     * 上传音频
     *
     * @param path 本地地址
     * @return 服务器地址
     */
    public static String uploadAudio(String path) {
        String key = getAudioObjKey(path);
        return upload(key, path);
    }


    /**
     * 分月存储文件
     *
     * @return yyyyMM
     */
    private static String getDateString() {
        return DateFormat.format("yyyyMM", new Date()).toString();
    }


    //  image/yyyymm/<字符串>.jpg
    private static String getImageObjKey(String path) {
        String fileMd5 = HashUtil.getMD5String(new File(path));
        String dateString = getDateString();


        return String.format("image/%s/%s.jpg", dateString, fileMd5);

    }

    //  portrait/yyyymm/<字符串>.jpg
    private static String getPortraitObjKey(String path) {
        String fileMd5 = HashUtil.getMD5String(new File(path));
        String dateString = getDateString();


        return String.format("portrait/%s/%s.jpg", dateString, fileMd5);

    }

    //  audio/yyyymm/<字符串>.mp3
    private static String getAudioObjKey(String path) {
        String fileMd5 = HashUtil.getMD5String(new File(path));
        String dateString = getDateString();


        return String.format("audio/%s/%s.mp3", dateString, fileMd5);

    }
}



