package org.aurora.gobatis.Injector;

import com.intellij.lang.injection.general.Injection;
import com.intellij.lang.injection.general.LanguageInjectionContributor;
import com.intellij.lang.injection.general.SimpleInjection;
import com.intellij.lang.xml.XMLLanguage;
import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlTag;
import com.intellij.psi.xml.XmlText;
import com.intellij.sql.psi.SqlLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*
 *   Mapper.xml SQL Óï·¨×¢Èë
 * */
public class MapperInjector implements LanguageInjectionContributor {

    @Override
    public @Nullable Injection getInjection(@NotNull PsiElement context) {
        PsiElement parent = context.getParent();
        if (isMapperFile(parent)) {
            if (parent instanceof XmlTag) {
                PsiElement nextSibling = parent.getFirstChild().getNextSibling();
                if (
                        nextSibling.getText().equals("select") ||
                                nextSibling.getText().equals("update") ||
                                nextSibling.getText().equals("insert") ||
                                nextSibling.getText().equals("delete") ||
                                nextSibling.getText().equals("for") ||
                                nextSibling.getText().equals("if")) {
                    if (context instanceof XmlText) {
                        return new SimpleInjection(SqlLanguage.INSTANCE, "", "", null);
                    }
                }
            }
        }
        return null;
    }

    public Boolean isMapperFile(PsiElement element) {
        if (!element.getLanguage().equals(XMLLanguage.INSTANCE)) return false;
        PsiElement parent = element.getParent();
        if (parent != null) {
            PsiElement nextSibling = parent.getFirstChild().getNextSibling();
            if (nextSibling != null) {
                String text = nextSibling.getText();
                if (text.equals("mapper")||text.equals("select")||text.equals("update")||text.equals("delete")||text.equals("for")||text.equals("if")) {
                    return true;
                }
            }
        }
        return false;
    }

    public Boolean isSQL() {
        return false;
    }
}
