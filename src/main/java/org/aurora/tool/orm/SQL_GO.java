package org.aurora.tool.orm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQL_GO {
    // pk ���ݿ�����
    public Map<String, String> pk=new HashMap<>();

    // �ֶ�/���� ӳ��
    public Map<String, String> mapping=new HashMap<>();

    public List<String> fieldSort=new ArrayList<>();

    // �ֶ�/ע�� ӳ��
    public Map<String, String> comment=new HashMap<>();

    // �ֶ�/Լ����� ӳ��
    public Map<String, List<String>> check=new HashMap<>();


    // �ֶ�/Ĭ��ֵ
    public Map<String, String> defaultValue=new HashMap<>();
}
