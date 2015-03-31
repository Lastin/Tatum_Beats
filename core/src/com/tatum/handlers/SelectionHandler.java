package com.tatum.handlers;

import com.badlogic.gdx.files.FileHandle;

import java.util.ArrayList;

/**
 * Created by Ben on 24/01/2015.
 */
public class SelectionHandler {
    //this class deals with all the files for the select state
    //it abstracts the files into easily dealt with strings
    //which can then be passed to the methods below to return data on the files
    private FileHandle parent;
    private FileHandle current;
    private FileHandle[] children;
    private int screenCount;
    private int remainder;
    private String[] names;
    private ArrayList<FileHandle> prunedChildren;
    private ArrayList<String> prunedChildrenNames;
    private final String[] legalFormats = {".wav", ".mp3", ".ogg", ".m4a", ".mp4"}; // formats that are allowed to be uploaded
    public SelectionHandler(FileHandle parent){

        this.parent=parent;
        current = parent;
        setBasics();
    }
    private void setBasics(){

        children= parent.list();
        names = new String[children.length];

        for(int i =0;i<children.length;i++){
            names[i] = children[i].name();
        }
        pruneChildren();

        screenCount = (int) Math.floor(prunedChildren.size());
        remainder = prunedChildren.size()%5;
    }
    public void pruneChildren(){
        prunedChildren = new ArrayList<FileHandle>();
        prunedChildrenNames = new ArrayList<String>();
        for(int i =0;i<children.length;i++){
            if(children[i].isDirectory()){ //if the child is a file we can add it to the pruned array
                prunedChildren.add(children[i]);
                prunedChildrenNames.add(children[i].name());

            }else { // else we check if the file is one of the accepted extentions
                String extention = children[i].name().substring(children[i].name().length()-4); // get last 4 characters

                for (int j = 0; j < 4; j++) {
                    if(extention.equals(legalFormats[j])){ //if so we can add it to the pruned array
                        prunedChildren.add(children[i]);
                        prunedChildrenNames.add(children[i].name());

                    }
                }
            }
        }

    }


    private int findChild(String child){ //find the child's position within the array
        int position =0;
        for(int i =0;i<prunedChildrenNames.size();i++){
            if(child.equals(prunedChildrenNames.get(i))){
                position=i;
                break;
            }
        }
        return position;
    }
    public FileHandle getChild(String child){ // return the child at the given position
        int position = findChild(child);
        return prunedChildren.get(position);
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
    public ArrayList<FileHandle> getPrunedChilder(){
        return prunedChildren;
    }
    public String[] getNames() {
        return names;
    }
    public ArrayList<String> getPrunedNames(){
        return prunedChildrenNames;
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
        current= prunedChildren.get(position);
        setBasics();
    }
    public boolean isDir(String child){ // check if the given child is a dircectory or not
        int position = findChild(child);
        return prunedChildren.get(position).isDirectory();
    }
    public String getChildFullPath(String child){ //get the full path for a given child
        int position = findChild(child);
        return prunedChildren.get(position).path();
    }
}
