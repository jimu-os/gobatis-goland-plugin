package org.aurora.tool.orm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQL_GO {
    // pk Êı¾İ¿âÖ÷¼ü
    public Map<String, String> pk=new HashMap<>();

    // ×Ö¶Î/ÀàĞÍ Ó³Éä
    public Map<String, String> mapping=new HashMap<>();

    public List<String> fieldSort=new ArrayList<>();

    // ×Ö¶Î/×¢ÊÍ Ó³Éä
    public Map<String, String> comment=new HashMap<>();

    // ×Ö¶Î/Ô¼Êø¼ì²é Ó³Éä
    public Map<String, List<String>> check=new HashMap<>();


    // ×Ö¶Î/Ä¬ÈÏÖµ
    public Map<String, String> defaultValue=new HashMap<>();
}
