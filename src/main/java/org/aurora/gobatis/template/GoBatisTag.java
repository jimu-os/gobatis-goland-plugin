package org.aurora.gobatis.template;

import com.goide.editor.template.GoLiveTemplateContextType;
import com.goide.psi.impl.GoTagImpl;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public class GoBatisTag  extends GoLiveTemplateContextType {
    protected GoBatisTag() {
        super("GoBatisTag");
    }

    @Override
    protected boolean isInContext(@NotNull PsiElement psiElement) {
        if (psiElement.getParent() instanceof GoTagImpl){
            PsiElement parent = psiElement.getParent();
            String text = parent.getText();
            if (text.contains("column:")){
                return false;
            }
            return true;
        }
        return false;
    }
}
