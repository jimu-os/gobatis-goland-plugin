package org.aurora.utils;

import com.intellij.openapi.vfs.VirtualFile;
import org.aurora.key.Key;
import org.jetbrains.yaml.YAMLFileType;

public class AuroraUtil {

    /*
     *   ResourceDir 获取项目根路径下的静态资源虚拟文件信息
     *   @root 一般是项目根路径
     *   @configPath 配置文件中的路径配置信息
     * */
    public static VirtualFile ResourceDir(VirtualFile root, String configPath) {
        if (root != null && root.isDirectory()) {
            String path = root.getPath();
            if (path.endsWith(configPath)) {
                return root;
            }
            VirtualFile[] children = root.getChildren();
            for (VirtualFile virtualFile : children) {
                VirtualFile dir = ResourceDir(virtualFile, configPath);
                if (dir != null) {
                    return dir;
                }
            }
        }
        return null;
    }


    /*
     *   找到项目中的 application 配置文件
     *   @file 项目根虚拟文件
     * */
    public static VirtualFile GetConfigFile(VirtualFile file) {
        if (file.isDirectory()) {
            VirtualFile[] children = file.getChildren();
            for (VirtualFile virtualFile : children) {
                if (virtualFile.isDirectory()) {
                    VirtualFile config = GetConfigFile(virtualFile);
                    if (config != null) {
                        return config;
                    }
                }
                String name = virtualFile.getName();
                if (!virtualFile.getFileType().equals(YAMLFileType.YML)) continue;
                int index = name.lastIndexOf(".");
                if (index != -1) name = name.substring(0, index);
                if (name.equals(Key.application)) {
                    return virtualFile;
                }
            }
        }
        return null;
    }


    /*
     *   从静态资源目录中找到 filePath 结尾的 html 文件
     *   @root 静态资源根虚拟文件
     *   @filePath 是一个以静态资源为根路径的子路径   root:   a/b/c   filePath: aa/cc/bb/test.html  filePath完整路径则是: a/b/c/aa/cc/bb/test.html
     * */
    public static VirtualFile FindHtml(VirtualFile root, String filePath) {
        if (root != null && root.isDirectory()) {
            VirtualFile[] rootChildren = root.getChildren();
            for (VirtualFile virtualFile : rootChildren) {
                if (virtualFile.isDirectory()) {
                    VirtualFile findHtml = FindHtml(virtualFile, filePath);
                    if (findHtml != null) {
                        return findHtml;
                    }
                }
                String path = virtualFile.getPath();
                if (path.endsWith(filePath)) {
                    return virtualFile;
                }
            }
        }
        return null;
    }
}
