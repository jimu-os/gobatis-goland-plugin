package org.aurora.gobatis.mark.mapper.func;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import org.aurora.Icons.Icons;


import javax.swing.*;

public class MapperInfo {
    // mapper ��ǩ����
    String type;

    String namespace;

    // mapper ��ǩ id ����
    String id;

    // mapper ������Ϣ
    PsiElement element;

    // mapper���� �����ļ�λ��
    VirtualFile mapper;

    // �б�� ��ʾ
    MapperFuncMarkHint hint;

    // ��ǰmapper �������ڵ��ļ�
    VirtualFile xml;

    // ��ǰ mapper ���ڵ� psi Ԫ��
    MapperFuncMgmtNavigationHandler navigationHandler;


    // ͼ��
    Icon icon;

    public MapperInfo(String type, String id) {
        this.type = type;
        this.id = id;
        this.hint = new MapperFuncMarkHint(this.type + " sql statement");
        switch (this.type) {
            case "select":
                this.icon = Icons.MapperSelectIcon;
                break;
            case "insert":
                this.icon = Icons.MapperInsertIcon;
                break;
            case "update":
                this.icon = Icons.MapperUpdateIcon;
                break;
            case "delete":
                this.icon = Icons.MapperDeleteIcon;
                break;
        }
    }

    public String Id() {
        return this.namespace + "." + this.id;
    }

    public MapperFuncMgmtNavigationHandler getNavigationHandler() {
        return navigationHandler;
    }

    public void setNavigationHandler(MapperFuncMgmtNavigationHandler navigationHandler) {
        this.navigationHandler = navigationHandler;
    }

    public VirtualFile getXml() {
        return xml;
    }

    public void setXml(VirtualFile xml) {
        this.xml = xml;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PsiElement getElement() {
        return element;
    }

    public void setElement(PsiElement element) {
        this.element = element;
    }

    public VirtualFile getMapper() {
        return mapper;
    }

    public void setMapper(VirtualFile mapper) {
        this.mapper = mapper;
    }

    public MapperFuncMarkHint getHint() {
        return hint;
    }

    public void setHint(MapperFuncMarkHint hint) {
        this.hint = hint;
    }

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }
}
