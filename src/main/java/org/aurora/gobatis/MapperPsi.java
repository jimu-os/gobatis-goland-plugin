package org.aurora.gobatis;

import com.goide.psi.*;
import com.intellij.ide.highlighter.XmlFileType;
import com.intellij.lang.xml.XMLLanguage;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.source.xml.XmlTagImpl;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlTag;
import org.go.GoPlugin;
import org.aurora.gobatis.mark.mapper.func.MapperFuncMgmtNavigationHandler;
import org.aurora.gobatis.mark.mapper.func.MapperInfo;
import org.aurora.key.Key;
import org.go.GoUtil;

import java.util.Objects;

/*
 *   MapperPsi 实现对 GoBatis mapper 函数的 行标记检索
 * */
public class MapperPsi {


    public static MapperInfo isMapperFunction(PsiElement element) {
        if (element instanceof GoFieldDeclaration) {
            PsiElement lastChild = element.getLastChild();
            if (lastChild instanceof GoFunctionType) {
                // 找到 字段的结构体名称
                GoSpecType parentOfType = PsiTreeUtil.getParentOfType(element, GoSpecType.class);
                if (parentOfType == null) return null;
                PsiElement firstChild = parentOfType.getFirstChild();
                String structName = firstChild.getText();
                MapperInfo modMapper = isModMapper(element, structName);
                if (modMapper != null) {
                    GoPlugin.Mapper.put(modMapper.getId(), modMapper);
                }
                return modMapper;
            }
        }
        return null;
    }

    /*
     *   isModMapper 判断当前元素 所在的 aurora go.mod 项目 application 配置中 能不能找到 aurora.go-batis.mapper 配置路径下的 Mapper 映射定义信息
     * */
    public static MapperInfo isModMapper(PsiElement element, String struct) {
        // 根据 psi 结构获取到 函数名称
        PsiElement firstChild = element.getFirstChild();
        String funcName = firstChild.getText();
        PsiFile psiFile = element.getContainingFile();
        Project project = psiFile.getProject();
        VirtualFile goFile = psiFile.getVirtualFile();

        // 优化检索范围 不从配置文件中检索 配置信息 直mod接根目录检索
        VirtualFile modRoot = GoUtil.GetGoModRoot(project, goFile, Key.go_mod);
        if (modRoot == null) return null;
        MapperInfo mapperInfo = GetMapperInfo(modRoot, struct, funcName);
        if (mapperInfo != null) {
            mapperInfo.setMapper(goFile);
            mapperInfo.setElement(element);
            GoPlugin.Mapper.put(mapperInfo.Id(), mapperInfo);
        }
        return mapperInfo;
    }

    public static MapperInfo GetMapperInfo(VirtualFile mapperDir, String struct, String mapperFunc) {
        MapperInfo info = null;
        VirtualFile[] children = mapperDir.getChildren();
        for (VirtualFile file : children) {
            if (!file.isDirectory()) {
                info = findMapperInfo(file, struct, mapperFunc);
                if (info != null) return info;
            }
            info = GetMapperInfo(file, struct, mapperFunc);
            if (info != null) return info;
        }
        return info;
    }

    public static MapperInfo findMapperInfo(VirtualFile file, String struct, String func) {
        if (file.isDirectory()) {
            VirtualFile[] children = file.getChildren();
            for (VirtualFile virtualFile : children) {
                MapperInfo info = findMapperInfo(virtualFile, struct, func);
                if (info != null) return info;
            }
        }
        if (file.getFileType().equals(XmlFileType.INSTANCE)) {
            // 开始解析 xml
            Project project = ProjectManager.getInstance().getDefaultProject();
            FileViewProvider viewProvider = PsiManager.getInstance(project).findViewProvider(file);
            if (viewProvider == null) return null;
            PsiFile xmlPsi = viewProvider.getPsi(XMLLanguage.INSTANCE);
            // 获取 psi 文件第一个根节点
            PsiElement psiElement = xmlPsi.getFirstChild();
            PsiElement lastChild = psiElement.getLastChild();
            if (lastChild instanceof XmlTag) {
                // 验证 XmlTag 是 mapper 节点
                XmlTagImpl tag = (XmlTagImpl) lastChild;
                String name = tag.getName();
                if (!name.equals(Key.mapper)) return null;
                String namespace = tag.getAttributeValue(Key.namespace);
                if (!struct.equals(namespace)) return null;
                XmlTag[] subTags = tag.getSubTags();
                for (XmlTag xmlTag : subTags) {
                    String id = xmlTag.getAttributeValue(Key.id);
                    if (Objects.equals(id, func)) {
                        String tagName = xmlTag.getName();
                        // 记录更多的信息 标签类型，id 当前元素 文件位置 或者虚拟文件
                        MapperInfo info = new MapperInfo(tagName, id);
                        // 记录文件中的 psi
                        info.setNavigationHandler(new MapperFuncMgmtNavigationHandler(file, xmlTag));
                        // 文件信息
                        info.setXml(file);
                        // 存储 命名空间
                        info.setNamespace(namespace);
                        return info;
                    }
                }
            }
        }
        return null;
    }
}
