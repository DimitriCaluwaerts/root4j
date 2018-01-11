package org.dianahep.root4j.refactor;

import org.dianahep.root4j.core.*;
import org.dianahep.root4j.interfaces.*;
import java.io.*;
import java.util.*;

public class SRMap<T> extends SRCollection {
    public static String name;
    public static TBranchElement b;
    public static SRType keyType;
    public static SRType valueType;
    public static boolean split;
    public static boolean isTop;

    public SRMap(String name,TBranchElement b,SRType keyType,SRType valueType,boolean split,boolean isTop){
        super(name,isTop);
        this.b = b;
        this.keyType = keyType;
        this.valueType = valueType;
        this.split = split;
    }

    public SRMap(String name,TBranchElement b,SRComposite<T> types,boolean split,boolean isTop){
        super(name,isTop);
        this.b=b;
        this.keyType = types.members.get(0);
        this.valueType = types.members.get(1);
        this.split=split;
    }

    @Override public void debugMe(String str) {
        logger.debug("SRMap:: "+name+" "+str+" Event="+entry);
    }

    @Override public Map<Object,Object> readArray(RootInput buffer, int size)throws IOException {
        Map<Object,Object> data = new HashMap();
        int nn;
        if (split){
            debugMe("readArray(buffer," +size+") in split mode");
            return null;
        }
        else {
            debugMe("readArray(buffer, "+size+") in non-split mode");
            int byteCount = buffer.readInt();
            short version = buffer.readShort();
            if (version > 0 && kMemberWiseStreaming >0){
                return null;
            }
            else {
                for (int i=0;i<size;i++){
                    nn=buffer.readInt();
                    data.put(keyType.read(buffer),valueType.read(buffer));
                    // toMap ?
                }
                entry+=1;
                return data;
            }
        }
    }

    @Override public Map<Object,Object> readArray(int size)throws IOException {
        debugMe("readArray("+size+") calls readArray(buffer, "+size+")");
        RootInput buffer = b.setPosition((TLeafElement)b.getLeaves().get(0),entry);
        return readArray(buffer,size);
    }

    @Override public Map<Object,Object> read()throws IOException{
        Map<Object,Object> data = new HashMap();
        if (split){
            debugMe("read in split mode");
            TLeaf leaf = (TLeaf)b.getLeaves().get(0);
            RootInput buffer = b.setPosition(leaf,entry);
            int size = buffer.readInt();
            return null;
        }
        else {
            debugMe("read in non-split mode");
            RootInput buffer = b.setPosition((TLeaf)b.getLeaves().get(0),entry);
            int byteCount = buffer.readInt();
            short version = buffer.readShort();
            if (version>0 && kMemberWiseStreaming>0){
                return null;
            }
            else {
                int size = buffer.readInt();
                entry+=1L;
                for (int i=0;i<size;i++){
                    data.put(keyType.read(buffer),valueType.read(buffer));
                    // toMap?
                }
                return data;
            }
        }
    }

    @Override public Map<Object,Object> read(RootInput buffer)throws IOException{
        Map<Object,Object> data = new HashMap();
        if (split){
            debugMe("read(buffer) in split mode");
            return null;
        }
        else {
            debugMe("read(buffer) in non-split mode");
            if (isTop) {
                int byteCount = buffer.readInt();
                short version = buffer.readShort();
                if (version > 0 && kMemberWiseStreaming > 0) {
                    return null;
                } else {
                    int size = buffer.readInt();
                    entry += 1L;
                    for (int i = 0; i < size; i++) {
                        data.put(keyType.read(buffer), valueType.read(buffer));
                        // toMap?
                    }
                    return data;
                }
            }
            else {
                int size=buffer.readInt();
                entry+=1L;
                for (int i=0;i<size;i++){
                    data.put(keyType.read(buffer),valueType.read(buffer));
                    // toMap?
                }
                return data;
            }
        }
    }

    @Override public boolean hasNext(){
        return entry<b.getEntries();
    }

}