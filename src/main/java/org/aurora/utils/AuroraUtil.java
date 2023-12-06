package org.aurora.utils;

import com.intellij.openapi.vfs.VirtualFile;
import org.aurora.key.Key;
import org.jetbrains.yaml.YAMLFileType;

public class AuroraUtil {

    /*
     *   ResourceDir ��ȡ��Ŀ��·���µľ�̬��Դ�����ļ���Ϣ
     *   @root һ������Ŀ��·��
     *   @configPath �����ļ��е�·��������Ϣ
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
     *   �ҵ���Ŀ�е� application �����ļ�
     *   @file ��Ŀ�������ļ�
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
     *   �Ӿ�̬��ԴĿ¼���ҵ� filePath ��β�� html �ļ�
     *   @root ��̬��Դ�������ļ�
     *   @filePath ��һ���Ծ�̬��ԴΪ��·������·��   root:   a/b/c   filePath: aa/cc/bb/test.html  filePath����·������: a/b/c/aa/cc/bb/test.html
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
