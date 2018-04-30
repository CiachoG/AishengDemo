package chat_modular.chat_tools;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

//汉字转换类
public class CNCharTransfor {
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


    public static class CNChar{
        private String phonetic;
        private int tone;       //1~4
        public CNChar(){}

        public CNChar(String phonetic, int tone) {
            this.phonetic = phonetic;
            this.tone = tone;
        }

        public String getPhonetic() {
            return phonetic;
        }

        public void setPhonetic(String phonetic) {
            this.phonetic = phonetic;
        }

        public int getTone() {
            return tone;
        }

        public void setTone(int tone) {
            this.tone = tone;
        }
    }
}
