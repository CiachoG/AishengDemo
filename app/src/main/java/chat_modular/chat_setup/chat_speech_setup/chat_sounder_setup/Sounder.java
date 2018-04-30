package chat_modular.chat_setup.chat_speech_setup.chat_sounder_setup;

//SpeechSounderAdapter的列表子元素
public class Sounder {
    private String name;
    private String language;
    private String attr;
    private String parameter;

    public Sounder(String name, String language, String attr, String parameter) {
        this.name = name;
        this.language = language;
        this.attr = attr;
        this.parameter = parameter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }
}
