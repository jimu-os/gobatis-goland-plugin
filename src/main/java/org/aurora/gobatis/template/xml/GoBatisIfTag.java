package org.aurora.gobatis.template.xml;

import com.intellij.codeInsight.template.TemplateActionContext;
import com.intellij.codeInsight.template.TemplateContextType;
import org.jetbrains.annotations.NotNull;

public class GoBatisIfTag extends TemplateContextType {
    protected GoBatisIfTag() {
        super("If");
    }

    public boolean isInContext(@NotNull TemplateActionContext templateActionContext) {
        return templateActionContext.getFile().getName().endsWith(".xml");
    }
}
