package com.github.cdflynn.android.scrollsample.mock;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;

import com.github.cdflynn.android.scrollsample.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Contact {

    private Drawable mProfileImage;
    private String mFirstName;
    private String mLastName;

    private Contact() {
        //
    }

    public Contact(Drawable profileImage, String firstName, String lastName) {
        mProfileImage = profileImage;
        mFirstName = firstName;
        mLastName = lastName;
    }

    public Drawable getProfileImage() {
        return mProfileImage;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public static List<Contact> mocks(Context c) {
        List<Contact> contacts = new ArrayList<>(60);

        contacts.add(fromRes(c, R.drawable.pic_01, R.string.fn_m_01, R.string.ln_01));
        contacts.add(fromRes(c, R.drawable.pic_02, R.string.fn_m_02, R.string.ln_02));
        contacts.add(fromRes(c, R.drawable.pic_03, R.string.fn_m_03, R.string.ln_03));
        contacts.add(fromRes(c, R.drawable.pic_04, R.string.fn_m_04, R.string.ln_04));
        contacts.add(fromRes(c, R.drawable.pic_05, R.string.fn_m_05, R.string.ln_05));
        contacts.add(fromRes(c, R.drawable.pic_06, R.string.fn_m_06, R.string.ln_06));
        contacts.add(fromRes(c, R.drawable.pic_07, R.string.fn_m_07, R.string.ln_07));
        contacts.add(fromRes(c, R.drawable.pic_08, R.string.fn_m_08, R.string.ln_08));
        contacts.add(fromRes(c, R.drawable.pic_09, R.string.fn_m_09, R.string.ln_09));
        contacts.add(fromRes(c, R.drawable.pic_10, R.string.fn_m_10, R.string.ln_10));
        contacts.add(fromRes(c, R.drawable.pic_11, R.string.fn_m_11, R.string.ln_11));
        contacts.add(fromRes(c, R.drawable.pic_12, R.string.fn_m_12, R.string.ln_12));
        contacts.add(fromRes(c, R.drawable.pic_13, R.string.fn_m_13, R.string.ln_13));
        contacts.add(fromRes(c, R.drawable.pic_14, R.string.fn_m_14, R.string.ln_14));
        contacts.add(fromRes(c, R.drawable.pic_15, R.string.fn_m_15, R.string.ln_15));
        contacts.add(fromRes(c, R.drawable.pic_16, R.string.fn_m_16, R.string.ln_16));
        contacts.add(fromRes(c, R.drawable.pic_17, R.string.fn_m_17, R.string.ln_17));
        contacts.add(fromRes(c, R.drawable.pic_18, R.string.fn_m_18, R.string.ln_18));
        contacts.add(fromRes(c, R.drawable.pic_19, R.string.fn_m_19, R.string.ln_19));
        contacts.add(fromRes(c, R.drawable.pic_20, R.string.fn_m_20, R.string.ln_20));
        contacts.add(fromRes(c, R.drawable.pic_21, R.string.fn_m_21, R.string.ln_21));
        contacts.add(fromRes(c, R.drawable.pic_22, R.string.fn_m_22, R.string.ln_22));
        contacts.add(fromRes(c, R.drawable.pic_23, R.string.fn_m_23, R.string.ln_23));
        contacts.add(fromRes(c, R.drawable.pic_24, R.string.fn_m_24, R.string.ln_24));
        contacts.add(fromRes(c, R.drawable.pic_25, R.string.fn_m_25, R.string.ln_25));
        contacts.add(fromRes(c, R.drawable.pic_26, R.string.fn_m_26, R.string.ln_26));
        contacts.add(fromRes(c, R.drawable.pic_27, R.string.fn_m_27, R.string.ln_27));
        contacts.add(fromRes(c, R.drawable.pic_28, R.string.fn_m_28, R.string.ln_28));
        contacts.add(fromRes(c, R.drawable.pic_29, R.string.fn_m_29, R.string.ln_29));
        contacts.add(fromRes(c, R.drawable.pic_30, R.string.fn_m_30, R.string.ln_30));

        contacts.add(fromRes(c, R.drawable.pic_31, R.string.fn_f_01, R.string.ln_31));
        contacts.add(fromRes(c, R.drawable.pic_32, R.string.fn_f_02, R.string.ln_32));
        contacts.add(fromRes(c, R.drawable.pic_33, R.string.fn_f_03, R.string.ln_33));
        contacts.add(fromRes(c, R.drawable.pic_34, R.string.fn_f_04, R.string.ln_34));
        contacts.add(fromRes(c, R.drawable.pic_35, R.string.fn_f_05, R.string.ln_35));
        contacts.add(fromRes(c, R.drawable.pic_36, R.string.fn_f_06, R.string.ln_36));
        contacts.add(fromRes(c, R.drawable.pic_37, R.string.fn_f_07, R.string.ln_37));
        contacts.add(fromRes(c, R.drawable.pic_38, R.string.fn_f_08, R.string.ln_38));
        contacts.add(fromRes(c, R.drawable.pic_39, R.string.fn_f_09, R.string.ln_39));
        contacts.add(fromRes(c, R.drawable.pic_40, R.string.fn_f_10, R.string.ln_40));
        contacts.add(fromRes(c, R.drawable.pic_41, R.string.fn_f_11, R.string.ln_41));
        contacts.add(fromRes(c, R.drawable.pic_42, R.string.fn_f_12, R.string.ln_42));
        contacts.add(fromRes(c, R.drawable.pic_43, R.string.fn_f_13, R.string.ln_43));
        contacts.add(fromRes(c, R.drawable.pic_44, R.string.fn_f_14, R.string.ln_44));
        contacts.add(fromRes(c, R.drawable.pic_45, R.string.fn_f_15, R.string.ln_45));
        contacts.add(fromRes(c, R.drawable.pic_46, R.string.fn_f_16, R.string.ln_46));
        contacts.add(fromRes(c, R.drawable.pic_47, R.string.fn_f_17, R.string.ln_47));
        contacts.add(fromRes(c, R.drawable.pic_48, R.string.fn_f_18, R.string.ln_48));
        contacts.add(fromRes(c, R.drawable.pic_49, R.string.fn_f_19, R.string.ln_49));
        contacts.add(fromRes(c, R.drawable.pic_50, R.string.fn_f_20, R.string.ln_50));
        contacts.add(fromRes(c, R.drawable.pic_51, R.string.fn_f_21, R.string.ln_51));
        contacts.add(fromRes(c, R.drawable.pic_52, R.string.fn_f_22, R.string.ln_52));
        contacts.add(fromRes(c, R.drawable.pic_53, R.string.fn_f_23, R.string.ln_53));
        contacts.add(fromRes(c, R.drawable.pic_54, R.string.fn_f_24, R.string.ln_54));
        contacts.add(fromRes(c, R.drawable.pic_55, R.string.fn_f_25, R.string.ln_55));
        contacts.add(fromRes(c, R.drawable.pic_56, R.string.fn_f_26, R.string.ln_56));
        contacts.add(fromRes(c, R.drawable.pic_57, R.string.fn_f_27, R.string.ln_57));
        contacts.add(fromRes(c, R.drawable.pic_58, R.string.fn_f_28, R.string.ln_58));
        contacts.add(fromRes(c, R.drawable.pic_59, R.string.fn_f_29, R.string.ln_59));
        contacts.add(fromRes(c, R.drawable.pic_60, R.string.fn_f_30, R.string.ln_60));

        Collections.sort(contacts, COMPARATOR);
        return contacts;
    }

    public static final Comparator<Contact> COMPARATOR = new Comparator<Contact>() {
        @Override
        public int compare(Contact o1, Contact o2) {
            return o1.getFirstName().compareTo(o2.getFirstName());
        }
    };

    private static Contact fromRes(Context c, @DrawableRes int img, @StringRes int fn, @StringRes int ln) {
        return new Contact(ContextCompat.getDrawable(c, img), c.getString(fn), c.getString(ln));
    }
}
