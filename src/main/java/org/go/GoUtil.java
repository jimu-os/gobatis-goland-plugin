package org.go;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

public class GoUtil {
    /*
     *   GetGoModRoot ���ݵ�ǰ�༭���ļ��ҵ�����ͬ������������go.mod �ļ����Դ�ȷ����ǰ�� go mod ��Ŀ
     *   @param file ��ǰ���༭���ļ��������
     *   @param project �ṩ�ڵݹ����ϲ��ҹ����г�����Ŀ��·���ķ�Χ��
     *   @return ����ͬ������������ go.mod ���� go.work �ļ����Դ�ȷ����ǰ�� go mod���� go work ��Ŀ��·��
     * */
    public static VirtualFile GetGoModRoot(Project project, VirtualFile file, String goFile) {
        String basePath = project.getBasePath();
        if (basePath == null) return null;
        // �ж���Ŀ��Ҫ�� ��·��ָ��
        String filePath = file.getPath();
        if (!filePath.startsWith(basePath)) {
            return null;
        }
        VirtualFile parent = file.getParent();
        if (parent == null) {
            return null;
        }
        // ��ʼ����ͳͬ���� �ļ�
        VirtualFile[] children = parent.getChildren();
        for (VirtualFile virtualFile : children) {
            if (!virtualFile.isDirectory()) {
                String fileName = virtualFile.getName();
                if (fileName.equals(goFile)) {
                    return parent;
                }
            }
        }
        return GetGoModRoot(project, parent, goFile);
    }
}
