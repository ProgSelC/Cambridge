package newton;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Newton {
	public static void main(String... args) {
		String filename = "Ньютон.txt";

		ArrayList<String> personsFamiliar = readInitial(filename);
		Newton n = new Newton(personsFamiliar);
		
		for(String strSymb : readCode(filename)){
			System.out.print(n.decodeSymbol(strSymb));
		}
		System.out.println();
	}

	public Map<String, Person> persons = new HashMap<String, Person>();
	
	public Newton(ArrayList<String> personsStrList) {
		for (String str : personsStrList) {
			String[] items = str.split("\\s*-\\s*");

			if (items.length > 1) {
				Person person1 = tryGetPerson(items[0]);
				Person person2 = tryGetPerson(items[1]);
				person1.addContact(person2);
				person2.addContact(person1);
			}
		}
	}

	public char decodeSymbol(String encodedString){
		String[] items = encodedString.split("\\s*\\t\\s*");

		String code = "";
		for(String pair : items){
			code += getRelation(pair);
		}
		
		return (char)Integer.parseInt(code, 2);
	}
	
	public static ArrayList<String> readCode(String filename){
		ArrayList<String> result = new ArrayList<String>();

		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			String str;
			boolean beginRead = false;
			while ((str = br.readLine()) != null) {
				if (str.trim().matches("^[-]+")) {
					beginRead = true;
					continue;
				}

				if(!beginRead){
					continue;
				}
				else if(str.trim().matches("")){
					break;
				}
				result.add(str.trim());
			}
		} catch (IOException e) {
			System.out.println("Error reading file!");
		}
		return result;
	}
	
	public static ArrayList<String> readInitial(String filename) {
		ArrayList<String> result = new ArrayList<String>();

		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			String str;
			int startReadClearLine = 2;
			int num = 0;
			while ((str = br.readLine()) != null) {
				if (str.trim().matches("")) {
					num++;
					continue;
				}

				if(num < startReadClearLine){
					continue;
				}
				else if(num > startReadClearLine){
					break;
				}
				result.add(str.trim());
			}
		} catch (IOException e) {
			System.out.println("Error reading file!");
		}
		return result;
	}

	
	Person getPerson(String name) {
			return persons.get(name);
	}
	
	Person tryGetPerson(String name) {
		if (persons.containsKey(name)) {
			return persons.get(name);
		}

		Person newPerson = new Person(name);
		persons.put(name, newPerson);

		return newPerson;
	}

	public int getRelation(String contactPair){
		String[] items = contactPair.split("\\s*-\\s*");
		if(items.length < 2)
			return 0;
		
		Person person1 = getPerson(items[0]);
		Person person2 = getPerson(items[1]);
		
		if(person1 == null || person2 == null)
			return 0;
		
		return checkPersonRelation(person1, person2, new HashSet<Person>());
	}
	
	int checkPersonRelation(Person personToFind, Person contact, HashSet<Person> contacts) {
		int length = contacts.size();
		contacts.addAll(contact.getContacts());

		if (contacts.contains(personToFind)) {
			return 1;
		} else if (length == contacts.size()) {
			return 0;
		}

		for (Person friend : contact.getContacts()) {
			if(checkPersonRelation(personToFind, friend, contacts) == 1)
				return 1;
		}
		
		return 0;
	}
	

	public class Person {
		private String name;
		private HashSet<Person> contacts = new HashSet<>();

		public Person(String name){
			this.name = name;
		}
		
		public String getName() {
			return name;
		}

		public HashSet<Person> getContacts() {
			return contacts;
		}

		public void addContact(Person person) {
			contacts.add(person);
		}
	}
}
