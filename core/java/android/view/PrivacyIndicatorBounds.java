/*
 * Copyright (C) 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.view;

import static android.view.Surface.ROTATION_0;

import android.annotation.NonNull;
import android.annotation.Nullable;
import android.graphics.Rect;
import android.os.Parcelable;

import com.android.internal.util.ArrayUtils;
import com.android.internal.util.DataClass;

/**
 * A class which manages the bounds of the privacy indicator
 *
 * @hide
 */
@DataClass(
        genEqualsHashCode = true,
        genConstructor = false,
        genAidl = false,
        genGetters = false
)
public class PrivacyIndicatorBounds implements Parcelable {

    private final @NonNull Rect[] mStaticBounds;
    private final int mRotation;

    public PrivacyIndicatorBounds() {
        mStaticBounds = new Rect[4];
        mRotation = ROTATION_0;
    }

    public PrivacyIndicatorBounds(@NonNull Rect[] staticBounds, @Surface.Rotation int rotation) {
        mStaticBounds = staticBounds;
        mRotation = rotation;
    }

    /**
     * Return a PrivacyIndicatorBounds with updated static bounds
     */
    public PrivacyIndicatorBounds updateStaticBounds(@NonNull Rect[] staticPositions) {
        return new PrivacyIndicatorBounds(staticPositions, mRotation);
    }

    /**
     * Update the bounds of the indicator for a specific rotation, or ROTATION_0, if the provided
     * rotation integer isn't found
     */
    public PrivacyIndicatorBounds updateBoundsForRotation(@Nullable Rect bounds,
            @Surface.Rotation int rotation) {
        if (rotation >= mStaticBounds.length || rotation < 0) {
            return this;
        }
        Rect[] newBounds = ArrayUtils.cloneOrNull(mStaticBounds);
        newBounds[rotation] = bounds;
        return updateStaticBounds(newBounds);
    }

    /**
     * Return an inset PrivacyIndicatorBounds
     */
    public PrivacyIndicatorBounds inset(int insetLeft, int insetTop, int insetRight,
            int insetBottom) {
        if (insetLeft == 0 && insetTop == 0 && insetRight == 0 && insetBottom == 0) {
            return this;
        }
        Rect[] insetStaticBounds = new Rect[mStaticBounds.length];
        for (int i = 0; i < mStaticBounds.length; i++) {
            insetStaticBounds[i] =
                    insetRect(mStaticBounds[i], insetLeft, insetTop, insetRight, insetBottom);
        }
        return updateStaticBounds(insetStaticBounds);
    }

    private static Rect insetRect(Rect orig, int insetLeft, int insetTop, int insetRight,
            int insetBottom) {
        if (orig == null) {
            return null;
        }
        int left = Math.max(0, orig.left - insetLeft);
        int top = Math.max(0, orig.top - insetTop);
        int right = Math.max(left, orig.right - insetRight);
        int bottom = Math.max(top, orig.bottom - insetBottom);
        return new Rect(left, top, right, bottom);
    }

    /**
     * Return a PrivacyIndicatorBounds with the static position rotated.
     */
    public PrivacyIndicatorBounds rotate(@Surface.Rotation int rotation) {
        if (rotation == ROTATION_0) {
            return this;
        }
        return new PrivacyIndicatorBounds(mStaticBounds, rotation);
    }

    /**
     * Returns a scaled PrivacyIndicatorBounds
     */
    public PrivacyIndicatorBounds scale(float scale) {
        if (scale == 1f) {
            return this;
        }

        Rect[] scaledStaticPos = new Rect[mStaticBounds.length];
        for (int i = 0; i < mStaticBounds.length; i++) {
            scaledStaticPos[i] = scaleRect(mStaticBounds[i], scale);
        }
        return new PrivacyIndicatorBounds(scaledStaticPos, mRotation);
    }

    private static Rect scaleRect(Rect orig, float scale) {
        if (orig == null) {
            return null;
        }

        Rect newRect = new Rect(orig);
        newRect.scale(scale);
        return newRect;
    }

    public Rect getStaticPrivacyIndicatorBounds() {
        return mStaticBounds[mRotation];
    }

    @Override
    public String toString() {
        return "PrivacyIndicatorBounds {static bounds=" + getStaticPrivacyIndicatorBounds()
                + " rotation=" + mRotation + "}";
    }



    // Code below generated by codegen v1.0.23.
    //
    // DO NOT MODIFY!
    // CHECKSTYLE:OFF Generated code
    //
    // To regenerate run:
    // $ codegen $ANDROID_BUILD_TOP/frameworks/base/core/java/android/view/PrivacyIndicatorBounds.java
    //
    // To exclude the generated code from IntelliJ auto-formatting enable (one-time):
    //   Settings > Editor > Code Style > Formatter Control
    //@formatter:off


    @Override
    @DataClass.Generated.Member
    public boolean equals(@Nullable Object o) {
        // You can override field equality logic by defining either of the methods like:
        // boolean fieldNameEquals(PrivacyIndicatorBounds other) { ... }
        // boolean fieldNameEquals(FieldType otherValue) { ... }

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        @SuppressWarnings("unchecked")
        PrivacyIndicatorBounds that = (PrivacyIndicatorBounds) o;
        //noinspection PointlessBooleanExpression
        return true
                && java.util.Arrays.equals(mStaticBounds, that.mStaticBounds)
                && mRotation == that.mRotation;
    }

    @Override
    @DataClass.Generated.Member
    public int hashCode() {
        // You can override field hashCode logic by defining methods like:
        // int fieldNameHashCode() { ... }

        int _hash = 1;
        _hash = 31 * _hash + java.util.Arrays.hashCode(mStaticBounds);
        _hash = 31 * _hash + mRotation;
        return _hash;
    }

    @Override
    @DataClass.Generated.Member
    public void writeToParcel(@NonNull android.os.Parcel dest, int flags) {
        // You can override field parcelling by defining methods like:
        // void parcelFieldName(Parcel dest, int flags) { ... }

        dest.writeTypedArray(mStaticBounds, flags);
        dest.writeInt(mRotation);
    }

    @Override
    @DataClass.Generated.Member
    public int describeContents() { return 0; }

    /** @hide */
    @SuppressWarnings({"unchecked", "RedundantCast"})
    @DataClass.Generated.Member
    protected PrivacyIndicatorBounds(@NonNull android.os.Parcel in) {
        // You can override field unparcelling by defining methods like:
        // static FieldType unparcelFieldName(Parcel in) { ... }

        Rect[] staticBounds = (Rect[]) in.createTypedArray(Rect.CREATOR);
        int rotation = in.readInt();

        this.mStaticBounds = staticBounds;
        com.android.internal.util.AnnotationValidations.validate(
                NonNull.class, null, mStaticBounds);
        this.mRotation = rotation;

        // onConstructed(); // You can define this method to get a callback
    }

    @DataClass.Generated.Member
    public static final @NonNull Parcelable.Creator<PrivacyIndicatorBounds> CREATOR
            = new Parcelable.Creator<PrivacyIndicatorBounds>() {
        @Override
        public PrivacyIndicatorBounds[] newArray(int size) {
            return new PrivacyIndicatorBounds[size];
        }

        @Override
        public PrivacyIndicatorBounds createFromParcel(@NonNull android.os.Parcel in) {
            return new PrivacyIndicatorBounds(in);
        }
    };

    @DataClass.Generated(
            time = 1621526273838L,
            codegenVersion = "1.0.23",
            sourceFile = "frameworks/base/core/java/android/view/PrivacyIndicatorBounds.java",
            inputSignatures = "private final @android.annotation.NonNull android.graphics.Rect[] mStaticBounds\nprivate final  int mRotation\npublic  android.view.PrivacyIndicatorBounds updateStaticBounds(android.graphics.Rect[])\npublic  android.view.PrivacyIndicatorBounds updateBoundsForRotation(android.graphics.Rect,int)\npublic  android.view.PrivacyIndicatorBounds inset(int,int,int,int)\nprivate static  android.graphics.Rect insetRect(android.graphics.Rect,int,int,int,int)\npublic  android.view.PrivacyIndicatorBounds rotate(int)\npublic  android.view.PrivacyIndicatorBounds scale(float)\nprivate static  android.graphics.Rect scaleRect(android.graphics.Rect,float)\npublic  android.graphics.Rect getStaticPrivacyIndicatorBounds()\npublic @java.lang.Override java.lang.String toString()\nclass PrivacyIndicatorBounds extends java.lang.Object implements [android.os.Parcelable]\n@com.android.internal.util.DataClass(genEqualsHashCode=true, genConstructor=false, genAidl=false, genGetters=false)")
    @Deprecated
    private void __metadata() {}


    //@formatter:on
    // End of generated code

}
