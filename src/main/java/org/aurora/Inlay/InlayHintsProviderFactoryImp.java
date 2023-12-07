package org.aurora.Inlay;

import com.intellij.codeInsight.hints.declarative.InlayHintsProviderFactory;
import com.intellij.codeInsight.hints.declarative.InlayProviderInfo;
import com.intellij.lang.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public class InlayHintsProviderFactoryImp implements InlayHintsProviderFactory {
    @Nullable
    @Override
    public InlayProviderInfo getProviderInfo(@NotNull Language language, @NotNull String s) {
        return null;
    }

    @NotNull
    @Override
    public List<InlayProviderInfo> getProvidersForLanguage(@NotNull Language language) {
        return null;
    }

    @NotNull
    @Override
    public Set<Language> getSupportedLanguages() {
        return null;
    }
}
