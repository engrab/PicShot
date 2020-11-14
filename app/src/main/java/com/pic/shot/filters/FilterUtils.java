package com.pic.shot.filters;

import android.graphics.Bitmap;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import org.wysaid.common.SharedContext;
import org.wysaid.nativePort.CGEImageHandler;

public class FilterUtils {
    public static final FilterBean[] EFFECTS_CONFIGS = {new FilterBean("", "Original"), new FilterBean("#unpack @krblend sr overlay/overlay_1.webp 100", "Overlay 1"), new FilterBean("#unpack @krblend sr overlay/overlay_2.jpg 100", "Overlay 2"), new FilterBean("#unpack @krblend sr overlay/overlay_3.jpg 100", "Overlay 3"), new FilterBean("#unpack @krblend sr overlay/overlay_4.jpg 100", "Overlay 4"), new FilterBean("#unpack @krblend sr overlay/overlay_5.jpg 100", "Overlay 5"), new FilterBean("#unpack @krblend sr overlay/overlay_6.jpg 100", "Overlay 6"), new FilterBean("#unpack @krblend sr overlay/overlay_7.jpg 100", "Overlay 7"), new FilterBean("#unpack @krblend sr overlay/overlay_8.jpg 100", "Overlay 8"), new FilterBean("#unpack @krblend sr overlay/overlay_9.jpg 100", "Overlay 9"), new FilterBean("#unpack @krblend sr overlay/overlay_10.jpg 100", "Overlay 10"), new FilterBean("#unpack @krblend sr overlay/overlay_11.jpg 100", "Overlay 11"), new FilterBean("#unpack @krblend sr overlay/overlay_12.jpg 100", "Overlay 12"), new FilterBean("#unpack @krblend sr overlay/overlay_13.jpg 100", "Overlay 13"), new FilterBean("#unpack @krblend sr overlay/overlay_14.jpg 100", "Overlay 14"), new FilterBean("#unpack @krblend sr overlay/overlay_15.jpg 100", "Overlay 15"), new FilterBean("#unpack @krblend sr overlay/overlay_16.jpg 100", "Overlay 16"), new FilterBean("#unpack @krblend sr overlay/overlay_17.jpg 100", "Overlay 17"), new FilterBean("#unpack @krblend sr overlay/overlay_18.jpg 100", "Overlay 18"), new FilterBean("#unpack @krblend sr overlay/overlay_19.jpg 100", "Overlay 19"), new FilterBean("#unpack @krblend sr overlay/overlay_20.jpg 100", "Overlay 20"), new FilterBean("#unpack @krblend sr overlay/overlay_21.jpg 100", "Overlay 21"), new FilterBean("#unpack @krblend sr overlay/overlay_22.jpg 100", "Overlay 22"), new FilterBean("#unpack @krblend sr overlay/overlay_23.jpg 100", "Overlay 23"), new FilterBean("#unpack @krblend sr overlay/overlay_24.jpg 100", "Overlay 24"), new FilterBean("#unpack @krblend sr overlay/overlay_25.jpg 100", "Overlay 25"), new FilterBean("#unpack @krblend sr overlay/overlay_26.jpg 100", "Overlay 26"), new FilterBean("#unpack @krblend sr overlay/overlay_27.jpg 100", "Overlay 27"), new FilterBean("#unpack @krblend sr overlay/overlay_28.jpg 100", "Overlay 28"), new FilterBean("#unpack @krblend sr overlay/overlay_29.jpg 100", "Overlay 29"), new FilterBean("#unpack @krblend sr overlay/overlay_30.jpg 100", "Overlay 30"), new FilterBean("#unpack @krblend sr overlay/overlay_31.jpg 100", "Overlay 31"), new FilterBean("#unpack @krblend sr overlay/overlay_32.jpg 100", "Overlay 32"), new FilterBean("#unpack @krblend sr overlay/overlay_33.jpg 100", "Overlay 33"), new FilterBean("#unpack @krblend sr overlay/overlay_34.jpg 100", "Overlay 34"), new FilterBean("#unpack @krblend sr overlay/overlay_35.jpg 100", "Overlay 35"), new FilterBean("#unpack @krblend sr overlay/overlay_36.jpg 100", "Overlay 36"), new FilterBean("#unpack @krblend sr overlay/overlay_37.jpg 100", "Overlay 37"), new FilterBean("#unpack @krblend sr overlay/overlay_38.jpg 100", "Overlay 38"), new FilterBean("#unpack @krblend sr overlay/overlay_39.jpg 100", "Overlay 39"), new FilterBean("#unpack @krblend sr overlay/overlay_40.jpg 100", "Overlay 40"), new FilterBean("#unpack @krblend sr overlay/overlay_41.jpg 100", "Overlay 41"), new FilterBean("#unpack @krblend sr overlay/overlay_42.jpg 100", "Overlay 42"), new FilterBean("#unpack @krblend sr overlay/overlay_43.jpg 100", "Overlay 43"), new FilterBean("#unpack @krblend sr overlay/overlay_44.jpg 100", "Overlay 44"), new FilterBean("#unpack @krblend sr overlay/overlay_45.jpg 100", "Overlay 45"), new FilterBean("#unpack @krblend sr overlay/overlay_46.jpg 100", "Overlay 46"), new FilterBean("#unpack @krblend sr overlay/overlay_47.jpg 100", "Overlay 47"), new FilterBean("#unpack @krblend sr overlay/overlay_48.jpg 100", "Overlay 48"), new FilterBean("#unpack @krblend sr overlay/overlay_49.jpg 100", "Overlay 49"), new FilterBean("#unpack @krblend sr overlay/overlay_50.jpg 100", "Overlay 50"), new FilterBean("#unpack @krblend sr overlay/overlay_51.jpg 100", "Overlay 51")};
    public static final FilterBean[] FILTERS_CONFIGS = {new FilterBean("", "Original"), new FilterBean("@adjust lut filters/bright01.webp", "Fresh 01"), new FilterBean("@adjust lut filters/bright02.webp", "Fresh 02"), new FilterBean("@adjust lut filters/bright03.webp", "Fresh 03"), new FilterBean("@adjust lut filters/bright05.webp", "Fresh 04"), new FilterBean("@adjust lut filters/euro01.webp", "Euro 01"), new FilterBean("@adjust lut filters/euro02.webp", "Euro 02"), new FilterBean("@adjust lut filters/euro05.webp", "Euro 03"), new FilterBean("@adjust lut filters/euro04.webp", "Euro 04"), new FilterBean("@adjust lut filters/euro06.webp", "Euro 05"), new FilterBean("@adjust lut filters/euro07.webp", "Euro 06"), new FilterBean("@adjust lut filters/film01.webp", "Film 01"), new FilterBean("@adjust lut filters/film02.webp", "Film 02"), new FilterBean("@adjust lut filters/film03.webp", "Film 03"), new FilterBean("@adjust lut filters/film04.webp", "Film 04"), new FilterBean("@adjust lut filters/film05.webp", "Film 05"), new FilterBean("@adjust lut filters/lomo1.webp", "Lomo 01"), new FilterBean("@adjust lut filters/lomo2.webp", "Lomo 02"), new FilterBean("@adjust lut filters/lomo3.webp", "Lomo 03"), new FilterBean("@adjust lut filters/lomo4.webp", "Lomo 04"), new FilterBean("@adjust lut filters/lomo5.webp", "Lomo 05"), new FilterBean("@adjust lut filters/movie01.webp", "Movie 01"), new FilterBean("@adjust lut filters/movie02.webp", "Movie 02"), new FilterBean("@adjust lut filters/movie03.webp", "Movie 03"), new FilterBean("@adjust lut filters/movie04.webp", "Movie 04"), new FilterBean("@adjust lut filters/movie05.webp", "Movie 05"), new FilterBean("@adjust lut filters/blend_twill1.webp", "Color 01"), new FilterBean("@adjust lut filters/glitch_lookup.webp", "Color 02"), new FilterBean("@adjust lut filters/blend_twill2.webp", "Color 03")};

    public static class FilterBean {
        private String config;
        private String name;

        FilterBean(String str, String str2) {
            this.config = str;
            this.name = str2;
        }

        public String getConfig() {
            return this.config;
        }

        public void setConfig(String str) {
            this.config = str;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String str) {
            this.name = str;
        }
    }

    public static Bitmap getBlurImageFromBitmap(Bitmap bitmap) {
        SharedContext create = SharedContext.create();
        create.makeCurrent();
        CGEImageHandler cGEImageHandler = new CGEImageHandler();
        cGEImageHandler.initWithBitmap(bitmap);
        cGEImageHandler.setFilterWithConfig("@blur lerp 0.6");
        cGEImageHandler.processFilters();
        Bitmap resultBitmap = cGEImageHandler.getResultBitmap();
        create.release();
        return resultBitmap;
    }

    public static Bitmap getBlurImageFromBitmap(Bitmap bitmap, float f) {
        SharedContext create = SharedContext.create();
        create.makeCurrent();
        CGEImageHandler cGEImageHandler = new CGEImageHandler();
        cGEImageHandler.initWithBitmap(bitmap);
        cGEImageHandler.setFilterWithConfig(MessageFormat.format("@blur lerp {0}", new Object[]{(f / 10.0f) + ""}));
        cGEImageHandler.processFilters();
        Bitmap resultBitmap = cGEImageHandler.getResultBitmap();
        create.release();
        return resultBitmap;
    }

    public static Bitmap cloneBitmap(Bitmap bitmap) {
        SharedContext create = SharedContext.create();
        create.makeCurrent();
        CGEImageHandler cGEImageHandler = new CGEImageHandler();
        cGEImageHandler.initWithBitmap(bitmap);
        cGEImageHandler.setFilterWithConfig("");
        cGEImageHandler.processFilters();
        Bitmap resultBitmap = cGEImageHandler.getResultBitmap();
        create.release();
        return resultBitmap;
    }

    public static Bitmap getBlackAndWhiteImageFromBitmap(Bitmap bitmap) {
        SharedContext create = SharedContext.create();
        create.makeCurrent();
        CGEImageHandler cGEImageHandler = new CGEImageHandler();
        cGEImageHandler.initWithBitmap(bitmap);
        cGEImageHandler.setFilterWithConfig("@adjust saturation 0");
        cGEImageHandler.processFilters();
        Bitmap resultBitmap = cGEImageHandler.getResultBitmap();
        create.release();
        return resultBitmap;
    }

    public static Bitmap getBitmapWithFilter(Bitmap bitmap, String str) {
        SharedContext create = SharedContext.create();
        create.makeCurrent();
        CGEImageHandler cGEImageHandler = new CGEImageHandler();
        cGEImageHandler.initWithBitmap(bitmap);
        cGEImageHandler.setFilterWithConfig(str);
        cGEImageHandler.setFilterIntensity(0.8f);
        cGEImageHandler.processFilters();
        Bitmap resultBitmap = cGEImageHandler.getResultBitmap();
        create.release();
        return resultBitmap;
    }

    public static List<Bitmap> getLstBitmapWithFilter(Bitmap bitmap) {
        ArrayList arrayList = new ArrayList();
        SharedContext create = SharedContext.create();
        create.makeCurrent();
        CGEImageHandler cGEImageHandler = new CGEImageHandler();
        cGEImageHandler.initWithBitmap(bitmap);
        for (FilterBean config : FILTERS_CONFIGS) {
            cGEImageHandler.setFilterWithConfig(config.getConfig());
            cGEImageHandler.processFilters();
            arrayList.add(cGEImageHandler.getResultBitmap());
        }
        create.release();
        return arrayList;
    }

    public static List<Bitmap> getLstBitmapWithOverlay(Bitmap bitmap) {
        ArrayList arrayList = new ArrayList();
        SharedContext create = SharedContext.create();
        create.makeCurrent();
        CGEImageHandler cGEImageHandler = new CGEImageHandler();
        cGEImageHandler.initWithBitmap(bitmap);
        for (FilterBean config : EFFECTS_CONFIGS) {
            cGEImageHandler.setFilterWithConfig(config.getConfig());
            cGEImageHandler.processFilters();
            arrayList.add(cGEImageHandler.getResultBitmap());
        }
        create.release();
        return arrayList;
    }
}
