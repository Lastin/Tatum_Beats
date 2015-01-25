package com.tatum.handlers;

import com.badlogic.gdx.files.FileHandle;

import java.util.ArrayList;

/**
 * Created by Ben on 24/01/2015.
 */
public class SelectionHandler {

    private FileHandle parent;
    private FileHandle current;
    private FileHandle[] children;
    private int screenCount;
    private int remainder;
    private String[] names;
    private ArrayList<FileHandle> prunedChildren;
    private ArrayList<String> prunedChildrenNames;
    private final String[] legalFormats = {".wav", ".mp3", ".ogg", ".m4a", ".mp4"};
    public SelectionHandler(FileHandle parent){

        this.parent=parent;
        current = parent;
        setBasics();
    }
    private void setBasics(){

        children= parent.list();
        names = new String[children.length];
        screenCount = (int) Math.floor(children.length / 5);
        remainder = children.length%5;

        for(int i =0;i<children.length;i++){
            names[i] = children[i].name();
        }
        pruneChildren();
    }
    public void pruneChildren(){
        prunedChildren = new ArrayList<FileHandle>();
        prunedChildrenNames = new ArrayList<String>();
        for(int i =0;i<children.length;i++){
            if(children[i].isDirectory()){
                //System.out.println("Directory: " + children[i].name());
                prunedChildren.add(children[i]);
                prunedChildrenNames.add(children[i].name());

            }else {
                String extention = children[i].name().substring(children[i].name().length()-4); // get last 4 characters
                //System.out.println("file: " + children[i].name());
                //System.out.println(extention);
                for (int j = 0; i < 4; i++) {
                    if(extention.equals(legalFormats[j])){
                        prunedChildren.add(children[i]);
                        prunedChildrenNames.add(children[i].name());

                    }
                }
            }
        }

    }


    private int findChild(String child){
        int position =0;
        for(int i =0;i<names.length;i++){
            if(child.equals(names[i])){
                position=i;
                break;
            }
        }
        return position;
    }
    public FileHandle getChild(String child){
        int position = findChild(child);
        return children[position];
    }
    public int getScreenCount(){ return screenCount; }


    public FileHandle getCurrent(){
        return current;
    }
    public FileHandle getParent(){
        return parent;
    }
    public FileHandle[] getChildren(){
        return children;
    }
    public String[] getNames() {
        return names;
    }
    public int getRemainder(){
        return remainder;
    }
    public void goToParent(){
        if(current.equals(parent)){
            return;
        }
        current = current.parent();
        setBasics();
    }
    public void goToChild(String child){
        int position = findChild(child);
        current=children[position];
        setBasics();
    }
    public boolean isDir(String child){
        int position = findChild(child);
        return children[position].isDirectory();
    }
    public String getChildFullPath(String child){
        int position = findChild(child);
        return children[position].path();
    }
}
