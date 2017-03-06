package taylor;

public class Taylor {
	public static void main(String... args){
		String phrase = "DONT_WORRY_BE_HAPPY";
		String encryptedPhrase = "KS28NL1VCNNFEGOE44L";
		String encryptedMessage = "HYV9FICJ_G1MNRUHE121VYDPS6_L8BOJTPC5OPQC3J07EEOUD6";
		
		Taylor taylorDecryptor = new Taylor(phrase, encryptedPhrase);

		System.out.println(taylorDecryptor.decrypt(encryptedMessage));
		System.out.println();
	}

	char[] charArr = {
			'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
			'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '_', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
	
	String key;
	
	public Taylor(String key){
		this.key = key;
	}
	
	public Taylor(String phrase, String encryptedPhrase){
		key = "";
		for(int i = 0; i < phrase.length(); i++){
			key += findKeySymbol(phrase.charAt(i), encryptedPhrase.charAt(i), charArr);
		}
		key = getRepeatingPart(key);
	}

	public String decrypt(String message){
		String result = "";
		
		String msgkey = "";
		for(int i = 0; i < message.length(); i++ ){
			msgkey += key.charAt((i) % key.length());
		}

		for(int i = 0; i < message.length(); i++){
			result += findKeySymbol(msgkey.charAt(i), message.charAt(i), charArr);
		}
		return result;
	}	
	
	public static char findKeySymbol(char c1, char c2, char[] dict){
		int n = indexOfChar(c1, dict);
		int i = indexOfChar(c2, dict);
		int index = (i + dict.length - n) % dict.length;
		return dict[index];
	}
	
	public static int indexOfChar(char chr, char[] array){
		int result = -1;
		for(int i = 0; i < array.length; i++){
			if(array[i] == chr)
				return i;
		}
		return result;
	}
	
	public static String getRepeatingPart(String str){
		int len = 1;
		boolean gotIt = true;
		String substr = "";
		
		while(len < str.length()){
			substr = str.substring(0, len);
			gotIt = true;
			
			for(int i = len; i < str.length(); i++){
				if(str.charAt(i) != substr.charAt(i % substr.length())){
					gotIt = false;
					break;
				}
			}
			if(gotIt){
				break;
			}
			len++;
		}
		return (gotIt) ? substr : str;
	}
}
