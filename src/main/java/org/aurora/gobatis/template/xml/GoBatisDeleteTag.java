package org.aurora.gobatis.template.xml;

import com.intellij.codeInsight.template.TemplateActionContext;
import com.intellij.codeInsight.template.TemplateContextType;
import org.jetbrains.annotations.NotNull;

public class GoBatisDeleteTag extends TemplateContextType {
    protected GoBatisDeleteTag() {
        super("Delete");
    }

    public boolean isInContext(@NotNull TemplateActionContext templateActionContext) {
        return templateActionContext.getFile().getName().endsWith(".xml");
    }
}
