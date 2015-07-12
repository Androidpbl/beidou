package frame.imgtools;

import java.lang.ref.SoftReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

public class ImgShowUtil {

    private Object imgViewTag;
    private String imgName;
    private String imgURL;
    private int time;
    private int defalutId;
    private boolean isYuanJiao = false;
    private static ExecutorService pool = Executors.newCachedThreadPool();

    public ImgShowUtil(String imgURL, Object tag) {
        this.imgViewTag = tag;
        this.imgURL = imgURL;
        this.imgName = ImgUtil.md5(imgURL);
        this.time = 500;
    }

    public ImgShowUtil(String imgURL, Object tag, int time) {
        this.imgViewTag = tag;
        this.imgURL = imgURL;
        this.imgName = ImgUtil.md5(imgURL);
        this.time = time;
    }

    public ImgShowUtil(String imgURL, Object tag, int time, boolean isYuanJiao) {
        this.imgViewTag = tag;
        this.isYuanJiao = isYuanJiao;
        this.imgURL = imgURL;
        this.imgName = ImgUtil.md5(imgURL);
        this.time = time;
    }

    class ImgHandler extends Handler {

        ImageView img;

        public ImgHandler(ImageView img) {
            this.img = img;
        }

        public void handleMessage(Message msg) {
            if ((imgViewTag == null && img.getTag() == null)
                    || (imgViewTag != null && imgViewTag.equals(img.getTag()))) {
                if (msg.obj != null) {
                    Bitmap bitmap = (Bitmap) msg.obj;
                    img.setImageBitmap(bitmap);
                } else {
                    setDefault(img, defalutId);
                }
            }
        }
    }

    class SetImgThread extends Thread {

        ImageView img;
        ImgHandler handler;
        Boolean isDown;
        int compressW;
        int compressH;

        public SetImgThread(ImgHandler handler, ImageView img, boolean isDown,
                int compressW, int compressH) {
            this.img = img;
            this.handler = handler;
            this.isDown = isDown;
            this.compressW = compressW;
            this.compressH = compressH;
        }

        public void run() {

            try {
                sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if ((imgViewTag == null && img.getTag() == null)
                    || (imgViewTag != null && imgViewTag.equals(img.getTag()))) {
                Bitmap bitmap = ImgUtil.fileNameToBitmap(imgName, compressW,
                        compressH);
                if (isDown && bitmap == null) {
                    ImgUtil.downImg(imgURL);
                    bitmap = ImgUtil.fileNameToBitmap(imgName, compressW,
                            compressH);
                }
                if (bitmap != null) {
                    SoftReference<Bitmap> bsr = new SoftReference<Bitmap>(
                            bitmap);
                    ImgUtil.cacheBitmap.put(imgName, bsr);
                }

                Message msg = new Message();
                msg.obj = bitmap;
                handler.sendMessage(msg);
            }
        }
    }

    /**
     * 设置图片背景
     * 
     * @param imageView
     */
    public void setCover(ImageView imageView) {
        setCover(imageView, -1);
    }

    /**
     * 设置图片背景
     * 
     * @param imageView
     * @param id
     *            默认图片id
     */
    public void setCover(ImageView imageView, int id) {
        setCoverDown(imageView, id, false, -1, -1);
    }

    /**
     * 设置图片背景
     * 
     * @param imageView
     * @param minWH
     *            压缩的最大宽高
     */
    public void setCoverCompress(ImageView imageView, int maxW, int maxH) {
        setCoverCompress(imageView, -1, maxW, maxH);
    }

    /**
     * 设置图片背景
     * 
     * @param imageView
     * @param minWH
     *            压缩的最大宽高
     */
    public void setCoverCompressWidth(ImageView imageView, int maxW) {
        setCoverCompress(imageView, -1, maxW, -1);
    }

    /**
     * 设置图片背景
     * 
     * @param imageView
     * @param minWH
     *            压缩的最大宽高
     */
    public void setCoverCompressHeight(ImageView imageView, int maxH) {
        setCoverCompress(imageView, -1, -1, maxH);
    }

    /**
     * 设置图片背景
     * 
     * @param imageView
     * @param id
     *            默认图片id
     * @param minWH
     *            压缩的最大宽高
     */
    public void setCoverCompress(ImageView imageView, int id, int maxW, int maxH) {
        setCoverDown(imageView, id, false, maxW, maxH);
    }

    /**
     * 设置图片背景
     * 
     * @param imageView
     * @param id
     *            默认图片id
     * @param minWH
     *            压缩的最大宽高
     */
    public void setCoverCompressWidth(ImageView imageView, int id, int maxW) {
        setCoverDown(imageView, id, false, maxW, -1);
    }

    /**
     * 设置图片背景
     * 
     * @param imageView
     * @param id
     *            默认图片id
     * @param minWH
     *            压缩的最大宽高
     */
    public void setCoverCompressHeight(ImageView imageView, int id, int maxH) {
        setCoverDown(imageView, id, false, -1, maxH);
    }

    /**
     * 设置图片背景 没有图片去下载
     * 
     * @param imageView
     */
    public void setCoverDown(ImageView imageView) {
        setCoverDown(imageView, -1);
    }

    /**
     * 设置图片背景 没有图片去下载
     * 
     * @param imageView
     * @param id
     *            默认图片id
     */
    public void setCoverDown(ImageView imageView, int id) {
        setCoverDown(imageView, id, true, -1, -1);
    }

    /**
     * 设置图片背景 没有图片去下载 并压缩
     * 
     * @param imageView
     * @param minWH
     *            压缩的最大宽高
     */
    public void setCoverDownCompress(ImageView imageView, int maxWH) {
        setCoverDownCompress(imageView, -1, maxWH);
    }

    /**
     * 
     * @param imageView
     * @param maxW
     *            最大宽
     * @param maxH
     *            最大高
     */
    public void setCoverDownCompressWH(ImageView imageView, int maxW, int maxH) {
        setCoverDownCompress(imageView, -1, maxW, maxH);
    }

    /**
     * 
     * @param imageView
     * @param maxW
     *            最大宽
     */
    public void setCoverDownCompressWidth(ImageView imageView, int maxW) {
        setCoverDownCompress(imageView, -1, -1);
    }

    /**
     * 
     * @param imageView
     * @param maxW
     *            最大高
     */
    public void setCoverDownCompressHeight(ImageView imageView, int maxH) {
        setCoverDownCompress(imageView, -1, -1);
    }

    /**
     * 设置图片背景 没有图片去下载 并压缩
     * 
     * @param imageView
     * @param id
     *            默认图片id
     * @param minW
     *            minH 压缩的最大宽高
     */
    public void setCoverDownCompress(ImageView imageView, int id, int maxW,
            int maxH) {
        setCoverDown(imageView, id, true, maxW, maxH);
    }

    public void setCoverDownCompress(ImageView imageView, int id, int maxWH) {
        setCoverDown(imageView, id, true, maxWH, maxWH);
    }

    public void setCoverDownCompressWidth(ImageView imageView, int id, int maxW) {
        setCoverDown(imageView, id, true, maxW, maxW);
    }

    public void setCoverDownCompressHeight(ImageView imageView, int id, int maxH) {
        setCoverDown(imageView, id, true, maxH, maxH);
    }

    private void setCoverDown(ImageView imageView, int id, boolean down,
            int compressW, int compressH) {
        imageView.setTag(imgViewTag);
        if (ImgUtil.cacheBitmap.containsKey(imgName)) {
            SoftReference<Bitmap> reference = ImgUtil.cacheBitmap.get(imgName);
            if (reference.get() != null) {
                imageView.setImageBitmap(reference.get());
                return;
            }
        }
        setDefault(imageView, id);
        this.defalutId = id;
        ImgHandler handler = new ImgHandler(imageView);
        pool.execute(new SetImgThread(handler, imageView, down, compressW,
                compressH));
    }

    private void setDefault(ImageView imageView, int id) {
        if (id != -1) {
            imageView.setImageResource(id);
        } else {
            imageView.setImageDrawable(null);
        }
    }

}
