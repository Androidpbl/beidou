package frame.imgtools;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.concurrent.ConcurrentHashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import cn.novacomm.igatelibtest.framework.Constants;
import frame.commom.AppContext;
import frame.util.Log;

public class ImgUtil {

    /**
     * 软引用bitmap缓存
     */
    public static ConcurrentHashMap<String, SoftReference<Bitmap>> cacheBitmap = new ConcurrentHashMap<String, SoftReference<Bitmap>>();

    /**
     * 图片缓存路径
     */
    public static String IMGCACHE = Constants.IMGCACHE;

    public static int IMG_DOWNOK = 6661;

    /**
     * 默认最小压缩
     */
    private static int default_minWH = 200;

    /**
     * 清空缓存
     */
    public static void recycle() {
        for (String string : cacheBitmap.keySet()) {
            Bitmap b = cacheBitmap.get(string).get();
            if (b != null && !b.isRecycled()) {
                b.recycle();
            }
        }
        cacheBitmap.clear();
    }

    public static Bitmap fileNameToBitmap(String name) {
        return fileNameToBitmap(name, -1, -1);
    }

    public static Bitmap fileNameToBitmap(String name, int compressW,
            int compressH) {
        File f = new File(IMGCACHE, name);
        return filePathToBitmap(f.getPath(), compressW, compressH);
    }

    public static Bitmap filePathToBitmap(String path) {
        return filePathToBitmap(path, -1, -1);
    }

    /**
     * 图片名称 转化成Bitmap 压缩图片的最小宽高
     */
    public static Bitmap filePathToBitmap(String path, int compressW,
            int compressH) {

        if (path == null)
            return null;
        File f = new File(path);
        File parent = f.getParentFile();
        if (parent == null)
            return null;
        parent.mkdirs();
        try {
            if (!f.exists())
                return null;

            if (compressW != -1 || compressH != -1) {
                return decodeFile(f, compressW, compressH);
            }

            Uri uri = Uri.fromFile(f);
            if (uri != null) {

                int degree = readPictureDegree(path);

                byte[] bt = getBytes(AppContext.curContext.getContentResolver()
                        .openInputStream(uri));
                Bitmap bitMap = BitmapFactory.decodeByteArray(bt, 0, bt.length);
                if (degree != 0) {
                    bitMap = rotaingImageView(degree, bitMap);
                }
                return bitMap;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            return decodeFile(f);
        }
        return null;

    }

    /**
     * 流转换为字节数组
     */
    public static byte[] getBytes(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] b = new byte[1024];
        int len = 0;
        while ((len = is.read(b, 0, 1024)) != -1) {
            baos.write(b, 0, len);
            baos.flush();
        }
        byte[] bytes = baos.toByteArray();
        return bytes;
    }

    /**
     * md5加密
     * 
     * @param s
     * @return
     */
    public static String md5(String s) {
        if (s == null)
            return "";
        try {
            MessageDigest digest = java.security.MessageDigest
                    .getInstance("MD5");
            digest.update(s.getBytes("utf-8"));
            byte messageDigest[] = digest.digest();

            return toHexString(messageDigest);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    public static String toHexString(byte[] b) { // String to byte
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
            sb.append(HEX_DIGITS[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    public static void downImg(String url) {
        downImg(url, false);
    }

    /**
     * 下载图片
     */
    public static String downImg(String url, boolean isDelete) {
        return downImg(url, isDelete, IMGCACHE);
    }

    /**
     * 下载图片
     * 
     * @param url
     *            图片地址
     * @param isDelete
     *            是否删除之前的
     * @param filePath
     *            下载保存文件夹
     * @return
     */
    public static String downImg(String url, boolean isDelete, String filePath) {
        if (url == null || url.equals(""))
            return null;

        String urlPath = url;

        File cacheFile = new File(filePath);
        cacheFile.mkdirs();
        File file = new File(cacheFile, md5(urlPath));
        try {
            if (!isDelete) {
                // 判断文件是否存在， 是否能正常转化为bitmap ， 转化失败则删除
                if (file.exists()) {
                    Bitmap bitmap = decodeFile(file, 20);
                    if (bitmap == null) {
                        file.delete();
                    } else {
                        if (bitmap != null && !bitmap.isRecycled()) {
                            bitmap.recycle();
                        }
                        return file.getPath();
                    }
                }
            } else {
                file.delete();
            }
            FileOutputStream outStream = new FileOutputStream(file);
            Log.e("urlPath", "urlPath:" + urlPath);
            HttpURLConnection conn = (HttpURLConnection) new URL(urlPath)
                    .openConnection();
            conn.setConnectTimeout(10 * 1000);
            if (conn.getResponseCode() == 200) {
                InputStream inStream = conn.getInputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = inStream.read(buffer)) != -1) {
                    outStream.write(buffer, 0, len);
                }
                outStream.close();
                inStream.close();

                return file.getPath();
            } else {
                file.delete();
                return null;
            }

        } catch (Exception ex) {
            Log.d("getFileURL", "getFileURL error = " + ex.getMessage());
            file.delete();
            ex.printStackTrace();
            return null;
        } finally {
            if (file.length() < 10) {
                file.delete();
                return null;
            }
        }
    }

    /**
     * 通过URL 获取图片url
     * 
     * @param ctx
     * @param url
     * @return
     * @throws IOException
     */
    public static String getFileURL(String url) {
        return downImg(url, false);
    }

    public static Drawable fileToDrawable(Context context, String fileUrl) {
        return fileToDrawable(context, fileUrl, true);
    }

    /**
     * 
     * @param context
     * @param fileUrl
     * @param compress
     *            是否压缩
     * @return
     */
    public static Drawable fileToDrawable(Context context, String fileUrl,
            boolean compress) {

        if (fileUrl == null) {
            return null;
        }
        File f = new File(fileUrl);
        try {
            if (!f.exists()) {
                return null;
            }
            if (compress)
                return new BitmapDrawable(decodeFile(f));
            else {
                Uri uri = Uri.fromFile(f);
                if (uri != null) {
                    return Drawable.createFromStream(context
                            .getContentResolver().openInputStream(uri), null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            return new BitmapDrawable(decodeFile(f));
        }
        return null;
    }

    /**
     * 压缩最小的宽高，
     * 
     * @param f
     *            文件
     * @param wh
     *            压缩最小的宽高，
     * @return
     */
    public static Bitmap decodeFile(File f, int wh) {
        return decodeFile(f, wh, wh);
    }

    /**
     * 默认压缩到100 100
     * 
     * @param f
     *            文件
     * @param wh
     *            压缩最小的宽高，
     * @return
     */
    private static Bitmap decodeFile(File f) {
        return decodeFile(f, default_minWH);
    }

    /**
     * 文件获取 Bitmap
     * 
     * @param f
     * @param width
     *            压缩宽
     * @param height
     *            压缩高
     * @return
     */
    private static Bitmap decodeFile(File f, int width, int height) {
        try {
            // decode image size
            if (f == null || !f.exists()) {
                return null;
            }
            int degree = readPictureDegree(f.getPath());

            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            FileInputStream stream1 = new FileInputStream(f);
            BitmapFactory.decodeStream(stream1, null, o);
            stream1.close();

            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < width || height_tmp / 2 < height) {
                    break;
                }
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            FileInputStream stream2 = new FileInputStream(f);
            Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, o2);
            stream2.close();
            bitmap = bitmapCompress(bitmap, width, height, degree);
            return bitmap;
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 压缩图片 按宽压缩
     * 
     * @param oldbmp
     * @param w
     *            最大宽
     * @return
     */
    public static Bitmap bitmapCompressWith(Bitmap oldbmp, int w) {
        return bitmapCompress(oldbmp, w, -1);

    }

    /**
     * 压缩图片 按高压缩
     * 
     * @param oldbmp
     * @param h
     *            最大高
     * @return
     */
    public static Bitmap bitmapCompressHeight(Bitmap oldbmp, int h) {
        return bitmapCompress(oldbmp, -1, h);
    }

    public static Bitmap bitmapCompress(Bitmap oldbmp, int w, int h) {
        return bitmapCompress(oldbmp, w, h, 0);
    }

    /**
     * 压缩图片
     * 
     * @param oldbmp
     * @param w
     *            最大宽
     * @param h
     *            最大高
     * @return
     */
    public static Bitmap bitmapCompress(Bitmap oldbmp, int w, int h, int degree) {
        int width = oldbmp.getWidth();
        int height = oldbmp.getHeight();
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        float a;
        float scaleWidth = ((float) w / width);
        float scaleHeight = ((float) h / height);
        if (w == -1) {
            a = scaleHeight;
        } else if (h == -1) {
            a = scaleWidth;
        } else {
            a = scaleWidth > scaleHeight ? scaleHeight : scaleWidth;
        }

        matrix.postScale(a, a);
        Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,
                matrix, true);
        SoftReference<Bitmap> soft = new SoftReference<Bitmap>(oldbmp);
        return newbmp;
    }

    /**
     * 读取图片属性：旋转的角度
     * 
     * @param path
     *            图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                degree = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                degree = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                degree = 270;
                break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转图片
     * 
     * @param angle
     * @param bitmap
     * @return Bitmap
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (bitmap != null && bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return resizedBitmap;
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

}
