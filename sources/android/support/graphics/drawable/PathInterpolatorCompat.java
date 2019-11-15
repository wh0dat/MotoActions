package android.support.graphics.drawable;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.p001v4.content.res.TypedArrayUtils;
import android.support.p001v4.graphics.PathParser;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.animation.Interpolator;
import org.xmlpull.v1.XmlPullParser;

@RestrictTo({Scope.LIBRARY_GROUP})
public class PathInterpolatorCompat implements Interpolator {
    public static final double EPSILON = 1.0E-5d;
    public static final int MAX_NUM_POINTS = 3000;
    private static final float PRECISION = 0.002f;

    /* renamed from: mX */
    private float[] f5mX;

    /* renamed from: mY */
    private float[] f6mY;

    public PathInterpolatorCompat(Context context, AttributeSet attributeSet, XmlPullParser xmlPullParser) {
        this(context.getResources(), context.getTheme(), attributeSet, xmlPullParser);
    }

    public PathInterpolatorCompat(Resources resources, Theme theme, AttributeSet attributeSet, XmlPullParser xmlPullParser) {
        TypedArray obtainAttributes = TypedArrayUtils.obtainAttributes(resources, theme, attributeSet, AndroidResources.STYLEABLE_PATH_INTERPOLATOR);
        parseInterpolatorFromTypeArray(obtainAttributes, xmlPullParser);
        obtainAttributes.recycle();
    }

    private void parseInterpolatorFromTypeArray(TypedArray typedArray, XmlPullParser xmlPullParser) {
        if (TypedArrayUtils.hasAttribute(xmlPullParser, "pathData")) {
            String namedString = TypedArrayUtils.getNamedString(typedArray, xmlPullParser, "pathData", 4);
            Path createPathFromPathData = PathParser.createPathFromPathData(namedString);
            if (createPathFromPathData == null) {
                StringBuilder sb = new StringBuilder();
                sb.append("The path is null, which is created from ");
                sb.append(namedString);
                throw new InflateException(sb.toString());
            }
            initPath(createPathFromPathData);
        } else if (!TypedArrayUtils.hasAttribute(xmlPullParser, "controlX1")) {
            throw new InflateException("pathInterpolator requires the controlX1 attribute");
        } else if (!TypedArrayUtils.hasAttribute(xmlPullParser, "controlY1")) {
            throw new InflateException("pathInterpolator requires the controlY1 attribute");
        } else {
            float namedFloat = TypedArrayUtils.getNamedFloat(typedArray, xmlPullParser, "controlX1", 0, 0.0f);
            float namedFloat2 = TypedArrayUtils.getNamedFloat(typedArray, xmlPullParser, "controlY1", 1, 0.0f);
            boolean hasAttribute = TypedArrayUtils.hasAttribute(xmlPullParser, "controlX2");
            if (hasAttribute != TypedArrayUtils.hasAttribute(xmlPullParser, "controlY2")) {
                throw new InflateException("pathInterpolator requires both controlX2 and controlY2 for cubic Beziers.");
            } else if (!hasAttribute) {
                initQuad(namedFloat, namedFloat2);
            } else {
                initCubic(namedFloat, namedFloat2, TypedArrayUtils.getNamedFloat(typedArray, xmlPullParser, "controlX2", 2, 0.0f), TypedArrayUtils.getNamedFloat(typedArray, xmlPullParser, "controlY2", 3, 0.0f));
            }
        }
    }

    private void initQuad(float f, float f2) {
        Path path = new Path();
        path.moveTo(0.0f, 0.0f);
        path.quadTo(f, f2, 1.0f, 1.0f);
        initPath(path);
    }

    private void initCubic(float f, float f2, float f3, float f4) {
        Path path = new Path();
        path.moveTo(0.0f, 0.0f);
        path.cubicTo(f, f2, f3, f4, 1.0f, 1.0f);
        initPath(path);
    }

    private void initPath(Path path) {
        int i = 0;
        PathMeasure pathMeasure = new PathMeasure(path, false);
        float length = pathMeasure.getLength();
        int min = Math.min(MAX_NUM_POINTS, ((int) (length / PRECISION)) + 1);
        if (min <= 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("The Path has a invalid length ");
            sb.append(length);
            throw new IllegalArgumentException(sb.toString());
        }
        this.f5mX = new float[min];
        this.f6mY = new float[min];
        float[] fArr = new float[2];
        for (int i2 = 0; i2 < min; i2++) {
            pathMeasure.getPosTan((((float) i2) * length) / ((float) (min - 1)), fArr, null);
            this.f5mX[i2] = fArr[0];
            this.f6mY[i2] = fArr[1];
        }
        if (((double) Math.abs(this.f5mX[0])) <= 1.0E-5d && ((double) Math.abs(this.f6mY[0])) <= 1.0E-5d) {
            int i3 = min - 1;
            if (((double) Math.abs(this.f5mX[i3] - 1.0f)) <= 1.0E-5d && ((double) Math.abs(this.f6mY[i3] - 1.0f)) <= 1.0E-5d) {
                float f = 0.0f;
                int i4 = 0;
                while (i < min) {
                    int i5 = i4 + 1;
                    float f2 = this.f5mX[i4];
                    if (f2 < f) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("The Path cannot loop back on itself, x :");
                        sb2.append(f2);
                        throw new IllegalArgumentException(sb2.toString());
                    }
                    this.f5mX[i] = f2;
                    i++;
                    f = f2;
                    i4 = i5;
                }
                if (pathMeasure.nextContour()) {
                    throw new IllegalArgumentException("The Path should be continuous, can't have 2+ contours");
                }
                return;
            }
        }
        StringBuilder sb3 = new StringBuilder();
        sb3.append("The Path must start at (0,0) and end at (1,1) start: ");
        sb3.append(this.f5mX[0]);
        sb3.append(",");
        sb3.append(this.f6mY[0]);
        sb3.append(" end:");
        int i6 = min - 1;
        sb3.append(this.f5mX[i6]);
        sb3.append(",");
        sb3.append(this.f6mY[i6]);
        throw new IllegalArgumentException(sb3.toString());
    }

    public float getInterpolation(float f) {
        if (f <= 0.0f) {
            return 0.0f;
        }
        if (f >= 1.0f) {
            return 1.0f;
        }
        int i = 0;
        int length = this.f5mX.length - 1;
        while (length - i > 1) {
            int i2 = (i + length) / 2;
            if (f < this.f5mX[i2]) {
                length = i2;
            } else {
                i = i2;
            }
        }
        float f2 = this.f5mX[length] - this.f5mX[i];
        if (f2 == 0.0f) {
            return this.f6mY[i];
        }
        float f3 = (f - this.f5mX[i]) / f2;
        float f4 = this.f6mY[i];
        return f4 + (f3 * (this.f6mY[length] - f4));
    }
}
