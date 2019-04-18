package com.sameer.WordFinder.Processor;

import java.util.ArrayList;
import java.util.List;

public class WordProcessor {

	private List<String> permuteList;
	
	public List<String> getListOfPermutedString(String word) {
	    permute(word, 0, word.length()-1);
		return permuteList;
	}

	public WordProcessor() {
		permuteList = new ArrayList<>();
	}

	private  void permute(String str, int l, int r){
		if (l == r) 
			permuteList.add(str);
        else
        { 
            for (int i = l; i <= r; i++) 
            { 
                str = swap(str,l,i); 
                permute(str, l+1, r); 
                str = swap(str,l,i); 
            } 
        } 
	}
	
	private String swap(String a, int i, int j)
    { 
        char temp; 
        char[] charArray = a.toCharArray(); 
        temp = charArray[i] ; 
        charArray[i] = charArray[j]; 
        charArray[j] = temp; 
        return String.valueOf(charArray); 
    } 
}
