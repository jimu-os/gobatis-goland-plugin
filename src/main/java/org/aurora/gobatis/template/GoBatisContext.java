package org.aurora.gobatis.template;

import com.goide.editor.template.GoLiveTemplateContextType;
import com.goide.psi.impl.GoStructTypeImpl;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public class GoBatisContext extends GoLiveTemplateContextType {

    protected GoBatisContext() {
        super("GoBatis");
    }

    @Override
    protected boolean isInContext(@NotNull PsiElement psiElement) {
        if (psiElement instanceof GoStructTypeImpl){
            return true;
        }
        return false;
    }

}