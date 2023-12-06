package org.aurora.plugin;

import com.intellij.openapi.vfs.VirtualFile;
import org.aurora.GoModule.Module;

import java.util.concurrent.ConcurrentHashMap;

public class GoProject {
    public static ConcurrentHashMap<VirtualFile, Module> mods=new ConcurrentHashMap<>();
}
