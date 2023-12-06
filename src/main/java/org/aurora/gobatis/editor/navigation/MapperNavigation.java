package org.aurora.gobatis.editor.navigation;

import com.intellij.codeInsight.navigation.actions.GotoDeclarationHandler;
import com.intellij.lang.xml.XMLLanguage;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlTag;
import org.aurora.gobatis.mark.mapper.func.MapperInfo;
import org.aurora.key.Key;
import org.go.GoProject;
import org.go.GoUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.go.GoMod;

public class MapperNavigation implements GotoDeclarationHandler {
    @Override
    public PsiElement @Nullable [] getGotoDeclarationTargets(@Nullable PsiElement sourceElement, int offset, Editor editor) {
        if (sourceElement == null) return new PsiElement[0];
        if (!sourceElement.getLanguage().is(XMLLanguage.INSTANCE)) {
            return new PsiElement[0];
        }
        XmlTag parentOfType = PsiTreeUtil.getParentOfType(sourceElement, XmlTag.class);
        if (parentOfType == null) {
            return new PsiElement[0];
        }
        parentOfType = PsiTreeUtil.getParentOfType(parentOfType, XmlTag.class);
        if (parentOfType == null) return new PsiElement[0];
        String name = parentOfType.getName();
        if (!name.equals("mapper")) {
            return new PsiElement[0];
        }
        String namespace = parentOfType.getAttributeValue("namespace");
        if (namespace == null || namespace.equals("")) {
            return new PsiElement[0];
        }
        String text = sourceElement.getText();

        // 找到当前模块的根
        PsiFile psiFile = sourceElement.getContainingFile();
        Project project = psiFile.getProject();
        VirtualFile virtualFile = psiFile.getVirtualFile();
        VirtualFile modRoot = GoUtil.GetGoModRoot(project, virtualFile, Key.go_mod);
        GoMod goMod = GoProject.mods.get(modRoot);
        MapperInfo mapperInfo = goMod.Mapper.get(namespace + "." + text);
        if (mapperInfo == null) {
            return new PsiElement[0];
        }
        return new PsiElement[]{mapperInfo.getElement()};
    }

    @Override
    public @Nullable @Nls(capitalization = Nls.Capitalization.Title) String getActionText(@NotNull DataContext context) {
        return GotoDeclarationHandler.super.getActionText(context);
    }
}
