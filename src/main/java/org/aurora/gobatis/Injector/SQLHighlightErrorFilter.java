package org.aurora.gobatis.Injector;

import com.intellij.codeInsight.highlighting.HighlightErrorFilter;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.PsiFile;

import org.jetbrains.annotations.NotNull;

public class SQLHighlightErrorFilter extends HighlightErrorFilter  {
    @Override
    public boolean shouldHighlightErrorElement(@NotNull PsiErrorElement element) {
        PsiFile psiFile = element.getContainingFile();
        FileViewProvider viewProvider = psiFile.getViewProvider();
        VirtualFile file = viewProvider.getVirtualFile();
        // TODO 通过 虚拟文件查看当前的xml文件来源是否是 application配置中的文件 严格控制错误显示位置
        String fileName = file.getName();
        return !fileName.endsWith(".xml");
    }
}
