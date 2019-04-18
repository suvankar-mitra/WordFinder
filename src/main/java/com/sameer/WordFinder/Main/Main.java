package com.sameer.WordFinder.Main;

import java.util.*;

import com.sameer.WordFinder.Processor.WordProcessor;
import com.sameer.WordFinder.ServiceGateway.OxfordServiceGateway;
import com.sameer.WordFinder.utils.Util;

public class Main {

	public static void main(String args[]) {
		System.out.println("Input a String (MAXIMUM 4 LETTERS PLEASE !!!!!-)");
		System.out.println("Permutation of 5 and above letters makes more than 100 API calls which is not allowed by Oxford APIs");
		System.out.println("for FREE accounts!!");
		System.out.print("Please enter the String: ");

		Scanner sc = new Scanner(System.in);
		String input = sc.next();
		sc.close();

        System.out.println("Checking all available English language words for the given alphabets...");

		WordProcessor processor = new WordProcessor();
        List<String> permuteList = processor.getListOfPermutedString(input);
		OxfordServiceGateway dictionary  = new OxfordServiceGateway();
        Map<String, List<String>> wordDefMap = dictionary.getDefinitionsOfWords(permuteList);

        System.out.println();

		for(Map.Entry<String, List<String>> wordSet :wordDefMap.entrySet()) {
            System.out.println("Found: "+wordSet.getKey());
            List<String> definitions = wordSet.getValue();
            for(int i=0; i< definitions.size(); i++) {
                System.out.println((i+1)+". "+ Util.capitalizeFirstLetter(definitions.get(i)));
            }
            System.out.println();
        }
	}
}
