package org.aurora.gobatis.mark.mapper.func;

import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.aurora.gobatis.MapperPsi;
import org.aurora.key.Key;
import org.go.GoProject;
import org.go.GoUtil;
import org.jetbrains.annotations.NotNull;
import org.go.GoMod;


/*
 *   MapperFuncLineMarker ɨ�� Mapper ӳ�亯��
 * */
public class MapperFuncLineMarker implements LineMarkerProvider {
    @Override
    public LineMarkerInfo<?> getLineMarkerInfo(@NotNull PsiElement element) {
        PsiFile psiFile = element.getContainingFile();
        Project project = psiFile.getProject();
        VirtualFile virtualFile = psiFile.getVirtualFile();
        VirtualFile modRoot = GoUtil.GetGoModRoot(project, virtualFile, Key.go_mod);
        GoMod goMod = GoProject.mods.get(modRoot);
        MapperInfo mapperInfo = MapperPsi.isMapperFunction(element);
        if (mapperInfo != null) {
            goMod.Mapper.put(mapperInfo.Id(), mapperInfo);
            return new LineMarkerInfo<>(
                    element,
                    element.getTextRange(),
                    mapperInfo.icon,
                    mapperInfo.hint,
                    mapperInfo.navigationHandler,
                    GutterIconRenderer.Alignment.CENTER,
                    new MapperAccessibleNameProvider());
        }
        return null;
    }
}
