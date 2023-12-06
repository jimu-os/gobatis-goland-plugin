package org.aurora.GoModule;

import com.goide.psi.impl.GoFunctionDeclarationImpl;
import com.goide.psi.impl.GoFunctionLitImpl;
import com.goide.psi.impl.GoMethodDeclarationImpl;
import com.goide.vgo.mod.psi.VgoFile;
import com.intellij.openapi.vfs.VirtualFile;
import org.aurora.gobatis.mark.mapper.func.MapperInfo;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
 *   解析并存储当前打开文件项目中的 Go Mod 信息
 * */
public class Module {

    // mod 名称
    public String Name;

    // go module 根目录
    public VirtualFile Root;

    public VgoFile vgoFile;

    public Map<String, String> auroraEngine;


    // GoBatis 的映射函数信息
    public ConcurrentHashMap<String, MapperInfo> Mapper = new ConcurrentHashMap<>();
}
