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
 *   MapperPsi ʵ�ֶ� GoBatis mapper ������ �б�Ǽ���
 * */
public class MapperPsi {


    public static MapperInfo isMapperFunction(PsiElement element) {
        if (element instanceof GoFieldDeclaration) {
            PsiElement lastChild = element.getLastChild();
            if (lastChild instanceof GoFunctionType) {
                // �ҵ� �ֶεĽṹ������
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
     *   isModMapper �жϵ�ǰԪ�� ���ڵ� aurora go.mod ��Ŀ application ������ �ܲ����ҵ� aurora.go-batis.mapper ����·���µ� Mapper ӳ�䶨����Ϣ
     * */
    public static MapperInfo isModMapper(PsiElement element, String struct) {
        // ���� psi �ṹ��ȡ�� ��������
        PsiElement firstChild = element.getFirstChild();
        String funcName = firstChild.getText();
        PsiFile psiFile = element.getContainingFile();
        Project project = psiFile.getProject();
        VirtualFile goFile = psiFile.getVirtualFile();

        // �Ż�������Χ ���������ļ��м��� ������Ϣ ֱmod�Ӹ�Ŀ¼����
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
            // ��ʼ���� xml
            Project project = ProjectManager.getInstance().getDefaultProject();
            FileViewProvider viewProvider = PsiManager.getInstance(project).findViewProvider(file);
            if (viewProvider == null) return null;
            PsiFile xmlPsi = viewProvider.getPsi(XMLLanguage.INSTANCE);
            // ��ȡ psi �ļ���һ�����ڵ�
            PsiElement psiElement = xmlPsi.getFirstChild();
            PsiElement lastChild = psiElement.getLastChild();
            if (lastChild instanceof XmlTag) {
                // ��֤ XmlTag �� mapper �ڵ�
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
                        // ��¼�������Ϣ ��ǩ���ͣ�id ��ǰԪ�� �ļ�λ�� ���������ļ�
                        MapperInfo info = new MapperInfo(tagName, id);
                        // ��¼�ļ��е� psi
                        info.setNavigationHandler(new MapperFuncMgmtNavigationHandler(file, xmlTag));
                        // �ļ���Ϣ
                        info.setXml(file);
                        // �洢 �����ռ�
                        info.setNamespace(namespace);
                        return info;
                    }
                }
            }
        }
        return null;
    }
}
