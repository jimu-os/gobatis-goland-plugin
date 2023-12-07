package org.go;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

public class GoUtil {
    /*
     *   GetGoModRoot 根据当前编辑的文件找到与它同级或者最外层的go.mod 文件，以此确定当前的 go mod 项目
     *   @param file 当前被编辑的文件虚拟对象
     *   @param project 提供在递归向上查找过程中超出项目根路径的范围。
     *   @return 与它同级或者最外层的 go.mod 或者 go.work 文件，以此确定当前的 go mod或者 go work 项目根路径
     * */
    public static VirtualFile GetGoModRoot(Project project, VirtualFile file, String goFile) {
        String basePath = project.getBasePath();
        if (basePath == null) return null;
        // 判断项目需要在 根路径指向
        String filePath = file.getPath();
        if (!filePath.startsWith(basePath)) {
            return null;
        }
        VirtualFile parent = file.getParent();
        if (parent == null) {
            return null;
        }
        // 开始检索统同级别 文件
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
