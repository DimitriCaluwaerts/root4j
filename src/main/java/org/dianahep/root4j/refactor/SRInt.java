package org.dianahep.root4j.refactor;

import org.dianahep.root4j.core.*;
import org.dianahep.root4j.interfaces.*;
import java.io.*;
import java.util.*;

public class SRInt extends SRSimpleType {
    String name;
    TBranch b;
    TLeaf l;

    SRInt(String name,TBranch b,TLeaf l){
        super(name,b,l);
    }

    @Override int read(RootInput buffer)throws IOException{
        int t = buffer.readInt();
        entry+=1L;
        return t;
    }

    @Override int read()throws IOException{
        RootInput buffer = b.setPosition(l,entry);
        int t = buffer.readInt();
        entry+=1L;
        return t;
    }

    @Override List<Integer> readArray(RootInput buffer, int size)throws IOException{
        int t;
        List<Integer> temp = new ArrayList();
        for (int i=0;i<size;i++){
            t=buffer.readInt();
            temp.add(t);
        }
        entry+=1L;
        return temp;
    }

    @Override List<Integer> readArray(int size)throws IOException{
        int t;
        List<Integer> temp = new ArrayList();
        RootInput buffer = b.setPosition(l,entry);
        for (int i=0;i<size;i++){
            t=buffer.readInt();
            temp.add(t);
        }
        entry+=1L;
        return temp;
    }

    @Override boolean hasNext(){
        return entry<b.getEntries();
    }

}
