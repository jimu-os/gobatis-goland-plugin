package org.aurora.plugin;


import com.goide.GoFileType;
import com.goide.psi.*;
import com.goide.psi.impl.GoStructTypeImpl;
import com.goide.vgo.mod.VgoFileType;
import com.goide.vgo.mod.psi.VgoFile;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.ProjectActivity;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.util.PsiTreeUtil;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import org.aurora.gobatis.MapperPsi;
import org.aurora.gobatis.mark.mapper.func.MapperInfo;
import org.aurora.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.aurora.GoModule.Module;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
 *   项目启动调用
 * */
public class Startup implements ProjectActivity {

    /*
     *   StartupActivity runActivity
     * */
    public void runActivity(@NotNull Project project) {
        // 初始化 mod 信息
        String basePath = project.getBasePath();
        LocalFileSystem fileSystem = LocalFileSystem.getInstance();
        if (basePath == null) return;
        VirtualFile rootPath = fileSystem.findFileByPath(basePath);
        PsiManager instance = PsiManager.getInstance(project);
        GoProject.mods = ScanGoModule(rootPath, instance);
    }


    @Nullable
    @Override
    public Object execute(@NotNull Project project, @NotNull Continuation<? super Unit> continuation) {
        // 初始化 mod 信息
        String basePath = project.getBasePath();
        LocalFileSystem fileSystem = LocalFileSystem.getInstance();
        if (basePath == null) return false;
        VirtualFile rootPath = fileSystem.findFileByPath(basePath);
        if (rootPath == null) return false;
        PsiManager instance = PsiManager.getInstance(project);
        GoProject.mods = ScanGoModule(rootPath, instance);
        LocalFileSystem.getInstance().refresh(true);
        return true;
    }

    /*
     *   扫描 root下的 mod 模块
     * */
    public static ConcurrentHashMap<VirtualFile, Module> ScanGoModule(VirtualFile root, PsiManager manager) {
        ConcurrentHashMap<VirtualFile, Module> mod = new ConcurrentHashMap<>();
        if (root.isDirectory()) {
            VirtualFile[] files = root.getChildren();
            for (VirtualFile file : files) {
                Map<VirtualFile, Module> mods = ScanGoModule(file, manager);
                mod.putAll(mods);
            }
            return mod;
        }
        if (root.getFileType().equals(VgoFileType.INSTANCE)) {
            VgoFile modFile = (VgoFile) manager.findFile(root);
            if (modFile == null) return mod;
            String name = modFile.getModuleName();
            VirtualFile parent = root.getParent();
            //  初始化模块信息存储数据
            Module module = new Module();
            module.Root = parent;
            module.Name = name;
            module.vgoFile = modFile;
            module.Mapper = ScanMapper(parent, manager);
            mod.put(parent, module);
        }
        return mod;
    }

    /*
     *   ScanMapper 扫描 modFile 下面的 mapper 函数
     *   @param modFile 模块根目录，扫描同一个模块时候 modFile始终不会改变
     *   @param file 模块根目录
     * */
    public static ConcurrentHashMap<String, MapperInfo> ScanMapper(VirtualFile file, PsiManager manager) {
        ConcurrentHashMap<String, MapperInfo> mapper = new ConcurrentHashMap<>();
        if (file.isDirectory()) {
            VirtualFile[] children = file.getChildren();
            for (VirtualFile virtualFile : children) {
                ConcurrentHashMap<String, MapperInfo> map = ScanMapper(virtualFile, manager);
                mapper.putAll(map);
            }
            return mapper;
        }
        FileType fileType = file.getFileType();
        if (!fileType.equals(GoFileType.INSTANCE)) return mapper;
        GoFile goFile = (GoFile) manager.findFile(file);
        if (goFile == null) return mapper;
        Collection<? extends GoTypeSpec> types = goFile.getTypes();
        for (GoTypeSpec spec : types) {
            PsiElement lastChild = spec.getLastChild();
            if (lastChild==null) continue;
            PsiElement firstChild = lastChild.getFirstChild();
            if (firstChild == null) continue;
            GoStructTypeImpl structType = PsiTreeUtil.getNextSiblingOfType(firstChild, GoStructTypeImpl.class);
            if (structType == null) continue;
            List<GoFieldDeclaration> fieldDeclarationList = structType.getFieldDeclarationList();
            for (GoFieldDeclaration fieldDeclaration : fieldDeclarationList) {
                GoType type = fieldDeclaration.getType();
                if (type == null) {
                    continue;
                }
                List<GoFieldDefinition> fieldDefinitionList = fieldDeclaration.getFieldDefinitionList();
                if (fieldDefinitionList.size() != 1) {
                    continue;
                }
                MapperInfo mapperInfo = MapperPsi.isMapperFunction(fieldDeclaration);
                if (mapperInfo != null) {
                    mapper.put(mapperInfo.Id(), mapperInfo);
                }
            }
        }
        return mapper;
    }

}
