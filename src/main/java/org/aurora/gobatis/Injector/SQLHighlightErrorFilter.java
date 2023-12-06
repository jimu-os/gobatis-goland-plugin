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
        // TODO ͨ�� �����ļ��鿴��ǰ��xml�ļ���Դ�Ƿ��� application�����е��ļ� �ϸ���ƴ�����ʾλ��
        String fileName = file.getName();
        return !fileName.endsWith(".xml");
    }
}
