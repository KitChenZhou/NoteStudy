package com.ckt5.test.auto_dail;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by D22395 on 2017/8/18.
 */

public class CaseLab {

    private static CaseLab sCaseLab;
    private List<Case> mCases;

    public static CaseLab get(Context context) {
        if (sCaseLab == null) {
            sCaseLab = new CaseLab(context);
        }
        return sCaseLab;
    }

    private CaseLab(Context context) {
        mCases = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Case c = new Case();
            c.setName("Case#" + i);
            mCases.add(c);
        }
    }

    public List<Case> getCases() {
        return mCases;
    }

    public Case getCase(int id) {
        for (Case c : mCases) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }

}
