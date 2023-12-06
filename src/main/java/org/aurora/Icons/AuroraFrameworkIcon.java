package org.aurora.Icons;

import com.intellij.ide.IconProvider;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.YAMLFileType;

import javax.swing.*;
/*
*   AuroraFrameworkIcon
*   getIcon 可以对特定的文件提供自定义图标
* */
public class AuroraFrameworkIcon extends IconProvider {
    @Override
    public @Nullable Icon getIcon(@NotNull PsiElement element, int flags) {
        PsiFile psiFile = element.getContainingFile();
        if (psiFile == null) {
            return null;
        }
        // 加载 配置哦文件图标
        if (psiFile.getFileType().equals(YAMLFileType.YML)) {
            VirtualFile virtualFile = psiFile.getViewProvider().getVirtualFile();
            String name = virtualFile.getName();
            if (name.startsWith("application")) {
                return Icons.YmlConfigIcon;
            }
        }

        // todo 通过初始化的扫描 mod 为每个mod 根目录添加图标

        // 加载 静态资源文件夹图标
        return null;
    }
}
