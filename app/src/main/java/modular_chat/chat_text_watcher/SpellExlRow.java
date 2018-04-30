package modular_chat.chat_text_watcher;

public class SpellExlRow implements Comparable<SpellExlRow>{
	String phonetic;
	char[] tone;
	public SpellExlRow(){}
	public SpellExlRow(String phonetic, char flat, char up, char downAndUp, char down) {
		this.phonetic = phonetic;
		tone=new char[4];
		
		this.tone[0]=flat;
		this.tone[1]=up;
		this.tone[2]=downAndUp;
		this.tone[3]=down;
	}
	public String getPhonetic() {
		return phonetic;
	}

	public void setPhonetic(String phonetic) {
		this.phonetic = phonetic;
	}

	public char getCharacter(int index){
		return this.tone[index];
	}
	
	@Override
	public int compareTo(SpellExlRow o) {
		return this.phonetic.compareTo(o.getPhonetic());
	}
}
