package com.haber.pdf;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.IntelliJTheme;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by haber on 2017/4/11.
 */
public class PDFExportImage{


    private static final List excludePage = Arrays.asList(0);
    private static final boolean isCut = true;
    public static PDF pd= new PDF();

    public void setup(String filePath, String outDirPath) throws IOException, InterruptedException {


        File file = new File(filePath);
        PDDocument document = PDDocument.load(file);
        PDFRenderer renderer = new PDFRenderer(document);

        int pageTotal = document.getNumberOfPages();
        System.out.println("页数：" + pageTotal);
        Dimension d1 = pd.label2().getSize();
        Rectangle rect1 = new Rectangle(0, 0, d1.width, d1.height);
        pd.label2().setText("总页数：" + pageTotal);
        pd.label2().paintImmediately(rect1);


        File outDir = new File(outDirPath);
        if (!outDir.exists()) {
            outDir.mkdirs();
        }

        if (!outDir.isDirectory()) {
            System.err.println("请填写正确的输出路径");

            System.exit(0);
        }

        int pageName = 0;
        for (int pageIndex = 0; pageIndex < pageTotal; pageIndex++) {
            System.out.println("正在转换第 " + pageIndex + " 页");

            Dimension d2 = pd.label1().getSize();
            Rectangle rect2 = new Rectangle(0, 0, d2.width, d2.height);
            pd.label1().setText("正在转换第 " + pageIndex + " 页");
            pd.label1().paintImmediately(rect2);


            BufferedImage image = renderer.renderImageWithDPI(pageIndex, 700, ImageType.RGB);

            if (!isCut && pageIndex != (pageTotal - 1) && !excludePage.contains(pageIndex)) {
                String fileName1 = outDir + "/" + file.getName() + "-" + (pageName++) + ".jpg";
                cut(image, fileName1, 0, 0, image.getWidth() / 2, image.getHeight());

                String fileName2 = outDir + "/" + file.getName() + "-" + (pageName++) + ".jpg";
                cut(image, fileName2, image.getWidth() / 2, 0, image.getWidth() / 2, image.getHeight());

            } else {

                String fileName = outDir + "/" + file.getName() + "-" + (pageName++) + ".jpg";
                ImageIO.write(image, "jpg", new File(fileName));
            }

        }
        Dimension d3 = pd.label1().getSize();
        Rectangle rect3 = new Rectangle(0, 0, d3.width, d3.height);
        pd.label1().setText("转换完成");
        pd.label1().paintImmediately(rect3);

        document.close();

    }

    public final static void cut(BufferedImage bufferedImage, String result,
                                 int x, int y, int width, int height) {
        try {
            // 读取源图像
            int srcWidth = bufferedImage.getWidth(); // 源图宽度
            int srcHeight = bufferedImage.getHeight(); // 源图高度
            if (srcWidth > 0 && srcHeight > 0) {
                Image image = bufferedImage.getScaledInstance(srcWidth, srcHeight,
                        Image.SCALE_DEFAULT);
                // 四个参数分别为图像起点坐标和宽高
                // 即: CropImageFilter(int x,int y,int width,int height)
                ImageFilter cropFilter = new CropImageFilter(x, y, width, height);
                Image img = Toolkit.getDefaultToolkit().createImage(
                        new FilteredImageSource(image.getSource(),
                                cropFilter));
                BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                Graphics g = tag.getGraphics();
                g.drawImage(img, 0, 0, width, height, null); // 绘制切割后的图
                g.dispose();
                // 输出为文件
                ImageIO.write(tag, "jpg", new File(result));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @Description:图片拼接 （注意：必须两张图片长宽一致哦）
     * @param type   1 横向拼接， 2 纵向拼接
     * @return
     */
    public static void mergeImage(String[] files, int type, String targetFile) {
        int len = files.length;
        if (len < 1) {
            throw new RuntimeException("图片数量小于1");
        }
        File[] src = new File[len];
        BufferedImage[] images = new BufferedImage[len];
        int[][] ImageArrays = new int[len][];
        for (int i = 0; i < len; i++) {
            try {
                src[i] = new File(files[i]);
                images[i] = ImageIO.read(src[i]);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            int width = images[i].getWidth();
            int height = images[i].getHeight();
            ImageArrays[i] = new int[width * height];
            ImageArrays[i] = images[i].getRGB(0, 0, width, height, ImageArrays[i], 0, width);
        }
        int newHeight = 0;
        int newWidth = 0;
        for (int i = 0; i < images.length; i++) {
            // 横向
            if (type == 1) {
                newHeight = newHeight > images[i].getHeight() ? newHeight : images[i].getHeight();
                newWidth += images[i].getWidth();
            } else if (type == 2) {// 纵向
                newWidth = newWidth > images[i].getWidth() ? newWidth : images[i].getWidth();
                newHeight += images[i].getHeight();
            }
        }
        if (type == 1 && newWidth < 1) {
            return;
        }
        if (type == 2 && newHeight < 1) {
            return;
        }

        // 生成新图片
        try {
            BufferedImage ImageNew = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
            int height_i = 0;
            int width_i = 0;
            for (int i = 0; i < images.length; i++) {
                if (type == 1) {
                    ImageNew.setRGB(width_i, 0, images[i].getWidth(), newHeight, ImageArrays[i], 0,
                            images[i].getWidth());
                    width_i += images[i].getWidth();
                } else if (type == 2) {
                    ImageNew.setRGB(0, height_i, newWidth, images[i].getHeight(), ImageArrays[i], 0, newWidth);
                    height_i += images[i].getHeight();
                }
            }
            //输出想要的图片
            ImageIO.write(ImageNew, "jpg", new File(targetFile));
            System.out.println("finished.");

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args){
        FlatLightLaf.install();
        IntelliJTheme.install(PDF.class.getResourceAsStream("theme.json"));
        pd.run();
    }

}
