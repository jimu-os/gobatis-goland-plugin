package org.go;

import com.intellij.openapi.vfs.VirtualFile;

import java.util.concurrent.ConcurrentHashMap;

public class GoProject {
    public static ConcurrentHashMap<VirtualFile, GoMod> mods=new ConcurrentHashMap<>();
}
