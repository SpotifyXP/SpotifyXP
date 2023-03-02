package com.spotifyxp.utils;


import com.spotifyxp.custom.DoubleArrayListForEach;
import com.spotifyxp.custom.DoubleArrayListForEachOne;

import java.util.ArrayList;

public class DoubleArrayList {
    Object al1type = null;
    Object al2type = null;
    ArrayList<Object> f = new ArrayList<>();
    ArrayList<Object> s = new ArrayList<>();
    int failure = 0;
    FailureTypes type = FailureTypes.nothing;
    enum FailureTypes {
        first,
        second,
        nothing
    }
    void check() {
        //----
        //When there is a mismatch
        //display an error and try to fix it itself
        //
        //Priority is on => Not losing data
        //----
        if(f.size()>s.size()) {
            System.err.println("[CRITICAL] (DoubleArrayList) mismatch between first and second arraylist size! Try correcting");
            failure = 1;
            type = FailureTypes.first;
        }
        if(s.size()>f.size()) {
            System.err.println("[CRITICAL] (DoubleArrayList) mismatch between first and second arraylist size! Try correcting");
            failure = 1;
            type = FailureTypes.second;
        }
        if(failure==1) {
            switch(type) {
                case first:
                    if(f.size()==s.size()) {
                        return;
                    }
                    s.add(al2type);
                case second:
                    if(f.size()==s.size()) {
                        return;
                    }
                    f.add(al1type);
            }
        }
    }
    public void add(Object first, Object second) {
        check();
        if(al1type==null) {
            al1type = first;
            al2type = second;
        }
        f.add(first);
        s.add(second);
    }
    public Object getFirst(int at) {
        return f.get(at);
    }
    public Object getSecond(int at) {
        return s.get(at);
    }
    public void remove(int at) {
        check();
        f.remove(at);
        s.remove(at);
    }
    public void forEach(DoubleArrayListForEach each) {
        int counter = 0;
        int ccounter = 0;
        for(Object o : f) {
            for(Object b : s) {
                if(ccounter==counter) {
                    each.run(o,b);
                    break;
                }
                ccounter++;
            }
            counter++;
            ccounter = 0;
        }
    }
    public void forEachFirst(DoubleArrayListForEachOne object) {
        for(Object o : f) {
            object.run(o);
        }
    }
    public void forEachSecond(DoubleArrayListForEachOne object) {
        for(Object o : s) {
            object.run(o);
        }
    }
    public int sizeAll() {
        return f.size() + s.size();
    }
    public int sizeFirst() {
        return f.size();
    }
    public int sizeSecond() {
        return s.size();
    }
    @Deprecated
    public ArrayList<Object> getRawFirst() {
        return f;
    }
    @Deprecated
    public ArrayList<Object> getRawSecond() {
        return s;
    }
}
