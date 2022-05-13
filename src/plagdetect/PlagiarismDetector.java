package plagdetect;


import java.io.File;
import java.util.Scanner;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;



public class PlagiarismDetector implements IPlagiarismDetector {
	
	private Map<String, Set<String>> theNgrams = new HashMap<>();
	private Map<String, Map<String, Integer>> result = new HashMap<>();
	private int ngrams;

	public PlagiarismDetector(int n) {
		// TODO implement this method
		ngrams = n; 
	}
	
	@Override
	public int getN() {
		// TODO Auto-generated method stub
		return ngrams;
	}

	@Override
	public Collection<String> getFilenames() {
		// TODO Auto-generated method stub
		Collection<String> group = new HashSet<String>();
		for(Map.Entry<String,Set<String>> entry : theNgrams.entrySet()) {
			String str = entry.getKey();
			group.add(str);
		}
		return group;
	}

	@Override
	public Collection<String> getNgramsInFile(String filename) {
		// TODO Auto-generated method stub
		return theNgrams.get(filename);
	}

	@Override
	public int getNumNgramsInFile(String filename) {
		// TODO Auto-generated method stub
		return getNgramsInFile(filename).size();
	}

	@Override
	public Map<String, Map<String, Integer>> getResults() {
		// TODO Auto-generated method stub
		return result;
	}

	@Override
	public void readFile(File file) throws IOException {
		// TODO Auto-generated method stub
		// most of your work can happen in this method
		
		Scanner sc = new Scanner(file);
		String [] read;
		Set<String> set1 = new HashSet<>();
		while (sc.hasNextLine()) {
			String temp;
			temp = sc.nextLine();
			read = temp.split(" ");
			if (read.length >= ngrams)
				for(int i = 0; i < read.length-ngrams+1; i++) {
					String sb = "";
					for (int j = i; j < i + ngrams; j++) {
						sb += read[j] + " ";
					}
					sb = sb.trim();
					set1.add(sb);
				}
					
			}
		theNgrams.put(file.getName(), set1);
		}


		
		


	@Override
	public int getNumNGramsInCommon(String file1, String file2) {
		// TODO Auto-generated method stub
		Set<String> a = theNgrams.get(file1);
		Set<String> b = theNgrams.get(file2);
		int count = 0;
		for (String str : a) {
			if (file1.equals(file2)) {
				continue;
			}
			if (b.contains(str)) {
				count++;
			}
		}

		return count;
	}

		



	@Override
	public Collection<String> getSuspiciousPairs(int minNgrams) {
		// TODO Auto-generated method stub
		Set<String> suspicious = new HashSet<>(); 
		for (String str: result.keySet()) {
			for (String st1: result.keySet()) {
				if (result.get(str).get(st1) >= minNgrams); {
					String temp = "";
					if (str.compareTo(st1) > -1) {
						temp += str + " " +st1 + " " + result.get(str).get(st1);
					} else { temp += st1 + " " + str + " " + result.get(str).get(st1);
				}
					suspicious.add(temp);
					
					}
				}

			}
		


	return suspicious;
	
	}
		
		

	
	@Override
	public void readFilesInDirectory(File dir) throws IOException {
		// delegation!
		// just go through each file in the directory, and delegate
		// to the method for reading a file
		for (File f : dir.listFiles()) {
			readFile(f);
		}
		for(Map.Entry<String, Set <String>> f1 : theNgrams.entrySet ()) {
			Map<String, Integer> temp1 = new HashMap<>();
			String file1 = f1.getKey();
			for (Map.Entry<String,Set <String>> f2 : theNgrams.entrySet()) {
				String file2 = f2.getKey();
				int temp = getNumNGramsInCommon (file1, file2);
				temp1.put(file2, temp);
			}
			result.put(file1, temp1);
				
			}
		}
	}

