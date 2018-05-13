package modular_chat.chat_tools;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.Arrays;

//汉字转换类
public class CNCharTool {
    private static HanyuPinyinOutputFormat format,formatExtend;

    static{
        format= new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);	//输出拼音全部小写
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);	//不带声调
        format.setVCharType(HanyuPinyinVCharType.WITH_V) ;

        formatExtend= new HanyuPinyinOutputFormat();
        formatExtend.setCaseType(HanyuPinyinCaseType.LOWERCASE);	//输出拼音全部小写
        formatExtend.setToneType(HanyuPinyinToneType.WITH_TONE_MARK);//带声调
        formatExtend.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);
    }

    //将一个中文字符转换为拼音
    public static String[] transformation(char ch){
        try{
            if(String.valueOf(ch).matches("[\u4e00-\u9fa5]+")){		//是中文字符
                String[] array=PinyinHelper.toHanyuPinyinStringArray(ch,format);
                return array;
            }else
                return null;
        }catch(BadHanyuPinyinOutputFormatCombination e){
            e.printStackTrace();
        }
        return null;
    }

    //将中文转换为带音调的字符,比如huáng
    public static String[] transformationWithTone(char ch){
        try{
            if(String.valueOf(ch).matches("[\u4e00-\u9fa5]+")){		//是中文字符
                String[] array=PinyinHelper.toHanyuPinyinStringArray(ch,formatExtend);
                return array;
            }else
                return null;
        }catch(BadHanyuPinyinOutputFormatCombination e){
            e.printStackTrace();
        }
        return new String[]{};
    }

    public static boolean isCNChar(char ch){
        if(String.valueOf(ch).matches("[\u4e00-\u9fa5]+"))
            return true;
        return false;
    }

    private static int[][] dp=new int[2][110];
    //通过编辑距离算法，计算两个字符串的差值
    public static int getSentenceSpacing(String str1,String str2){
        if(str1.equals(str2))   return 0;

        StringBuilder sb=new StringBuilder("");
        for(int i=0;i<str1.length();++i){
            if(isCNChar(str1.charAt(i)))
                sb.append(str1.charAt(i));
        }
        str1=' '+sb.toString();

        sb.delete(0,sb.length());
        for(int i=0;i<str2.length();++i){
            if(isCNChar(str2.charAt(i)))
                sb.append(str2.charAt(i));
        }
        str2=' '+sb.toString();

        Arrays.fill(dp[0],0);
        Arrays.fill(dp[1],0);
        for(int i=1;i<=str2.length()-1;++i)
            dp[0][i]=i;

        for(int i=1;i<=str1.length()-1;++i){
            for(int j=1;j<=str2.length()-1;++j){
                if(str1.charAt(i)==str2.charAt(j))
                    dp[1][j]=dp[0][j-1];
                else
                    dp[1][j]=Math.min(dp[0][j]+1,Math.min(dp[1][j-1]+1,dp[0][j-1]+1));
            }
            for(int j=0;j<= str2.length()-1;++j)
                dp[0][j]=dp[1][j];
        }
        return dp[1][str2.length()-1];
    }
}
