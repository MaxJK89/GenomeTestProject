package quant.cann.genometestproject.utils;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by angboty on 6/5/2015.
 */
public class TypeFaceHelper {

    Context context;

    public TypeFaceHelper(Context context) {
        this.context = context;
    }

    public Typeface getOpenSansLight() {
        return Typeface.createFromAsset(context.getAssets(), "fonts/opensanslight.ttf");
    }

    public Typeface getOpenSansBold() {
        return Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Bold.ttf");
    }

    public Typeface getOpenSansRegular() {
        return Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
    }

}
