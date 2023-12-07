package org.go;

import com.goide.vgo.mod.psi.VgoFile;
import com.intellij.openapi.vfs.VirtualFile;
import org.aurora.gobatis.mark.mapper.func.MapperInfo;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
 *   �������洢��ǰ���ļ���Ŀ�е� Go Mod ��Ϣ
 * */
public class GoMod {

    // mod ����
    public String Name;

    // go module ��Ŀ¼
    public VirtualFile Root;

    public VgoFile vgoFile;

    public Map<String, String> auroraEngine;


    // GoBatis ��ӳ�亯����Ϣ
    public ConcurrentHashMap<String, MapperInfo> Mapper = new ConcurrentHashMap<>();
}
