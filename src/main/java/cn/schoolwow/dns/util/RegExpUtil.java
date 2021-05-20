package cn.schoolwow.dns.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**正则表达式工具类*/
public class RegExpUtil {
    private static Logger logger = LoggerFactory.getLogger(RegExpUtil.class);
    private static Map<String,Pattern> patternMap = new HashMap<>();

    /**
     * 普通匹配,提取中括号中的内容
     * @param input 输入字符串
     * @param pattern 普通文本模式(非正则匹配)
     * */
    public static String plainMatch(String input,String pattern){
        int patternStartIndex = -1,patternEndIndex = -1;
        //找到第一个非转义的(
        for(int i=1;i<pattern.length();i++){
            if(pattern.charAt(0)=='('){
                patternStartIndex = 0;
            }
            if((pattern.charAt(i)=='(')&&(pattern.charAt(i-1)!='\\')){
                patternStartIndex = i;
            }
            if(pattern.charAt(0)==')'){
                patternEndIndex = 0;
            }
            if((pattern.charAt(i)==')')&&(pattern.charAt(i-1)!='\\')){
                patternEndIndex = i;
            }
        }
        if(patternEndIndex<=patternStartIndex){
            logger.warn("[匹配失败]模式中不包含中括号");
            return null;
        }
        String prefix = pattern.substring(0,patternStartIndex).replace("\\(","(").replace("\\)",")");
        String suffix = pattern.substring(patternEndIndex+1,pattern.length()).replace("\\(","(").replace("\\)",")");
        int inputStartIndex = input.indexOf(prefix);
        if(inputStartIndex<0){
            return null;
        }
        int inputEndIndex = input.substring(inputStartIndex+prefix.length(),input.length()).indexOf(suffix);
        if(inputEndIndex<0){
            return null;
        }
        return input.substring(inputStartIndex+prefix.length(),inputStartIndex+inputEndIndex+prefix.length());
    }

    /**
     * 正则匹配
     * @param input 输入字符串
     * @param patternString 正则匹配模式
     * @param groupNumber 匹配组序号
     * */
    public static String getGroup(String input,String patternString,int groupNumber){
        Pattern pattern = null;
        if(patternMap.containsKey(patternString)){
            pattern = patternMap.get(patternString);
        }else{
            pattern = Pattern.compile(patternString);
            patternMap.put(patternString,pattern);
        }
        Matcher matcher = pattern.matcher(input);
        if(matcher.find()){
            return matcher.group(groupNumber);
        }else{
            return null;
        }
    }

    /**
     * 正则匹配
     * @param input 输入字符串
     * @param patternString 正则匹配模式
     * @param groupName 匹配组名称
     * */
    public static String getGroup(String input,String patternString,String groupName){
        Pattern pattern = null;
        if(patternMap.containsKey(patternString)){
            pattern = patternMap.get(patternString);
        }else{
            pattern = Pattern.compile(patternString);
            patternMap.put(patternString,pattern);
        }
        Matcher matcher = pattern.matcher(input);
        if(matcher.find()){
            return matcher.group(groupName);
        }else{
            return null;
        }
    }

}
