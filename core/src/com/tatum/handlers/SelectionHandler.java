package com.tatum.handlers;

import com.badlogic.gdx.files.FileHandle;

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

    public SelectionHandler(FileHandle parent){

        this.parent=parent;
        current = parent;
        setBasics();
    }
    private void setBasics(){

        children= parent.list();
        names = new String[children.length];
        screenCount = (int) Math.floor(children.length/5);
        remainder = children.length%5;

        for(int i =0;i<children.length;i++){
            names[i] = children[i].name();
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
