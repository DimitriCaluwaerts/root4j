package org.dianahep.root4j.refactor;

import org.dianahep.root4j.interfaces.*;
import org.dianahep.root4j.core.*;
import java.io.*;
import java.util.*;

public class SRString extends SRSimpleType{
    String name;
    TBranch b;
    TLeaf l;

    SRString(String name,TBranch b,TLeaf l){
        super(name,b,l);
    }

    @Override String read(RootInput buffer)throws IOException{
        String r=buffer.readString();
        entry+=1L;
        return r;
    }

    @Override String read()throws IOException{
        RootInput buffer = b.setPosition(l,entry);
        String data = buffer.readString();
        entry+=1L;
        return data;
    }

    @Override List<String> readArray(RootInput buffer,int size)throws IOException{
        List<String> temp = new ArrayList();
        String t;
        for (int i=0;i<size;i++){
            t=buffer.readString();
            temp.add(t);
        }
        entry+=1L;
        return temp;
    }

    @Override List<String> readArray(int size) throws IOException{
        RootInput buffer = b.setPosition(l,entry);
        List<String> temp = new ArrayList();
        String t;
        for (int i=0;i<size;i++){
            t=buffer.readString();
            temp.add(t);
        }
        entry+=1L;
        return temp;
    }

    @Override boolean hasNext(){
        return entry<b.getEntries();
    }

}
