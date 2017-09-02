package me.angrypostman.freeforall.util;

import com.google.common.base.Preconditions;

import java.io.File;

public class FileUtils{

    public static String getFileExtension(File file){
        Preconditions.checkNotNull(file, "file cannot be null");
        Preconditions.checkArgument(file.isFile(), "file is not a valid file");

        String absolutePath=file.getAbsolutePath();
        return absolutePath.substring(absolutePath.lastIndexOf('.') + 1);
    }

}
