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
    // Mapper ��Ϣ����
    public static ConcurrentHashMap<String, MapperInfo> Mapper = new ConcurrentHashMap<>();

    // yaml ��ʼ���洢 �����ļ������ʾ��Ϣ
    public static Map<String, List<String>> yaml = new HashMap<>();


    protected GoPlugin() {
        initYamlConfig();
    }


    public static void initYamlConfig() {
        // aurora ��������
        yaml.put("aurora", List.of("server", "database", "resource", "redis", "rabbitmq", "email", "go-batis"));
        // ��������Ϣ������
        yaml.put("server", List.of("name", "host", "port", "file", "tls"));
        // ������Ϣ������
        yaml.put("host", List.of("localhost", "127.0.0.1"));
        // tls �ļ�������
        yaml.put("tls", List.of("certFile", "keyFile"));
        // ���ݿ�������
        yaml.put("database", List.of("mysql", "sqlserver", "postgres", "url"));
        yaml.put("mysql", List.of("url", "username", "password", "host", "database"));
        yaml.put("sqlserver", List.of("url", "username", "password", "host", "database"));
        yaml.put("postgres", List.of("url", "username", "password", "host", "database"));
        // �ʼ�������
        yaml.put("email", List.of("user", "password", "host"));

        yaml.put("rabbitmq", List.of("addr"));

        yaml.put("redis", List.of("addr", "password"));
        // GoBatis ������
        yaml.put("go-batis", List.of("mapper", "driver"));
    }

    public static Map<String, List<String>> YamlConfig() {
        return yaml;
    }

    /*
     * ��ȡ����go.mod��Ŀ¼λ�� �µ� application �����ļ��� ָ�� key ��value ֵ
     * @root go.mod��Ŀ¼λ��
     * @key ��Ӧyml��ȫ·�� ���� xxx.aaa.bbb ���ջ�ȡ bbb�ľ���ֵ��bbb�ľ���ֵ�����Ǹ��ַ�����Ҳ��������ֵ
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
                                        // �ҵ������ݲ��Ǿ��� �ַ���ֵ
                                        return null;
                                    }
                                }
                                // û���ҵ����ڵ����� ����null
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
