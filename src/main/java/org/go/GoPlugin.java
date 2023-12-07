package org.go;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.aurora.gobatis.mark.mapper.func.MapperInfo;
import org.jetbrains.yaml.YAMLFileType;
import org.yaml.snakeyaml.Yaml;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class GoPlugin {
    // Mapper 信息缓存
    public static ConcurrentHashMap<String, MapperInfo> Mapper = new ConcurrentHashMap<>();

    // yaml 初始化存储 配置文件相关提示信息
    public static Map<String, List<String>> yaml = new HashMap<>();


    protected GoPlugin() {
        initYamlConfig();
    }


    public static void initYamlConfig() {
        // aurora 根配置项
        yaml.put("aurora", List.of("server", "database", "resource", "redis", "rabbitmq", "email", "go-batis"));
        // 服务器信息配置项
        yaml.put("server", List.of("name", "host", "port", "file", "tls"));
        // 主机信息配置项
        yaml.put("host", List.of("localhost", "127.0.0.1"));
        // tls 文件配置项
        yaml.put("tls", List.of("certFile", "keyFile"));
        // 数据库配置项
        yaml.put("database", List.of("mysql", "sqlserver", "postgres", "url"));
        yaml.put("mysql", List.of("url", "username", "password", "host", "database"));
        yaml.put("sqlserver", List.of("url", "username", "password", "host", "database"));
        yaml.put("postgres", List.of("url", "username", "password", "host", "database"));
        // 邮件配置项
        yaml.put("email", List.of("user", "password", "host"));

        yaml.put("rabbitmq", List.of("addr"));

        yaml.put("redis", List.of("addr", "password"));
        // GoBatis 配置项
        yaml.put("go-batis", List.of("mapper", "driver"));
    }

    public static Map<String, List<String>> YamlConfig() {
        return yaml;
    }

    /*
     * 获取给定go.mod根目录位置 下的 application 配置文件中 指定 key 的value 值
     * @root go.mod根目录位置
     * @key 对应yml的全路径 例如 xxx.aaa.bbb 最终获取 bbb的具体值，bbb的具体值必须是个字符串，也就是最终值
     * */
    public static String ApplicationConfig(Project project, VirtualFile root, String key) {
        if (root.isDirectory()) {
            VirtualFile[] children = root.getChildren();
            for (VirtualFile virtualFile : children) {
                if (virtualFile.isDirectory()) {
                    String config = ApplicationConfig(project, virtualFile, key);
                    if (config != null) {
                        return config;
                    }
                }
                if (virtualFile.getFileType().equals(YAMLFileType.YML)) {
                    String fileName = virtualFile.getName();
                    if (fileName.startsWith("application")) {
                        Yaml yaml = new Yaml();
                        try {
                            Map<String, Object> config = yaml.load(virtualFile.getInputStream());
                            String value = null;
                            String[] split = key.split("\\.");
                            for (int i = 0; i < split.length; i++) {
                                Object c = config.get(split[i]);
                                if (i == split.length - 1) {
                                    if (c instanceof String) {
                                        value = (String) c;
                                        return value;
                                    } else {
                                        // 找到的数据不是具体 字符串值
                                        return null;
                                    }
                                }
                                // 没有找到对于的数据 返回null
                                if (c == null) {
                                    return null;
                                }
                                config = (Map<String, Object>) c;
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }


            }
        }
        return null;
    }


}
