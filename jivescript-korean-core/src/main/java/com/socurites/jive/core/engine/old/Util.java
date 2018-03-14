/*
	RiveScript - The Official Java RiveScript Interpreter

	Copyright (c) 2016 Noah Petherbridge

	Permission is hereby granted, free of charge, to any person obtaining a copy
	of this software and associated documentation files (the "Software"), to deal
	in the Software without restriction, including without limitation the rights
	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
	copies of the Software, and to permit persons to whom the Software is
	furnished to do so, subject to the following conditions:

	The above copyright notice and this permission notice shall be included in all
	copies or substantial portions of the Software.

	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
	SOFTWARE.
*/

package com.socurites.jive.core.engine.old;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility methods.
 */

public class Util {
	/**
	 * Convert a vector to a string array.
	 *
	 * @param vector The vector to convert.
	 */
	public static String[] Sv2s (Vector<String> vector) {
		String[] result = new String [ vector.size() ];
		int i = 0;
		for (Enumeration e = vector.elements(); e.hasMoreElements(); ) {
			result[i] = e.nextElement().toString();
			i++;
		}
		return result;
	}

	/**
	 * Convert an int vector into an int array.
	 *
	 * @param vector The vector to convert.
	 */
	public static int[] Iv2s (Vector<Integer> vector) {
		int[] result = new int [ vector.size() ];
		int i = 0;
		for (Enumeration e = vector.elements(); e.hasMoreElements(); ) {
			result[i] = Integer.parseInt( e.nextElement().toString() );
			i++;
		}
		return result;
	}

	/**
	 * Take the keys in a hash and return them as a string array.
	 *
	 * @param hash The hash to get the keys from.
	 */
	public static String[] SSh2s (HashMap<String, String> hash) {
		Vector<String> vector = new Vector<String>();
		Iterator sit = hash.keySet().iterator();
		while (sit.hasNext()) {
			vector.add((String) sit.next());
		}
		return Sv2s(vector);
	}

	/**
	 * Take the keys in a hash and return them as an int array.
	 *
	 * @param hash The hash to get the keys from.
	 */
	public static int[] IIh2s (HashMap<Integer, Inheritance> hash) {
		Vector<Integer> vector = new Vector<Integer>();
		Iterator sit = hash.keySet().iterator();
		while (sit.hasNext()) {
			vector.add((Integer) sit.next());
		}
		return Iv2s(vector);
	}

	/**
	 * Join a string array into a single string.
	 *
	 * @param parts The string array to join.
	 * @param delim The delimeter to join strings at.
	 */
	public static String join (String[] parts, String delim) {
		StringBuffer buff = new StringBuffer();
		for (int i = 0; i < parts.length; i++) {
			buff.append(parts[i]);
			if (i < parts.length - 1) {
				buff.append(delim);
			}
		}
		return buff.toString();
	}

	/**
	 * Sort the integer keys in a HashMap from highest to lowest.
	 *
	 * @param hash The hashmap of type Integer, Vector<String>.
	 */
	public static int[] sortKeysDesc (HashMap<Integer, Vector<String> > hash) {
		// Make a vector of all the number-keys of the hash.
		Vector<Integer> keys = new Vector<Integer>();

		// Get all the keys.
		Iterator it = hash.keySet().iterator();
		while (it.hasNext()) {
			int wc = Integer.parseInt( it.next().toString() );
			keys.add(wc);
		}

		// Turn the key vector into an int array.
		int[] iKeys = Iv2s (keys);

		// Order the keys in descending order.
		Arrays.sort(iKeys);

		// Reverse and return it!
		int[] reversed = new int [ iKeys.length ];
		int k = 0;
		for (int j = iKeys.length - 1; j >= 0; j--) {
			reversed[k] = iKeys[j];
			k++;
		}

		return reversed;
	}

	/**
	 * Sort strings in a list by length from longest to shortest.
	 *
	 * @param list The list to sort.
	 */
	public static String[] sortByLength (String[] list) {
		Arrays.sort(list, new StringCompare());
		return list;
	}

	/**
	 * Run substitutions on a string.
	 *
	 * @param sorted The sorted list of substitution patterns to process.
	 * @param hash   A hash that pairs the sorted list with the replacement texts.
	 * @param text   The text to apply the substitutions to.
	 */
	public static String substitute (String[] sorted, Map<String, String> hash, String text) {
		for (int i = 0; i < sorted.length; i++) {
			String pattern = sorted[i];
			String result  = hash.get(sorted[i]);
			String rot13   = ROT13(result);

			String quotemeta = Pattern.quote(pattern);
			text = text.replaceAll("^" + quotemeta + "$", "<rot13sub>" + rot13 + "<bus31tor>");
			text = text.replaceAll("^" + quotemeta + "(\\s+)", "<rot13sub>" + rot13 + "<bus31tor>$1");
			text = text.replaceAll("(\\s+)" + quotemeta + "(\\s+)", "$1<rot13sub>" + rot13 + "<bus31tor>$2");
			text = text.replaceAll("(\\s+)" + quotemeta + "$", "$1<rot13sub>" + rot13 + "<bus31tor>");
			
//			text = text.replaceAll("^" + quotemeta + "(\\W+)", "<rot13sub>" + rot13 + "<bus31tor>$1");
//			text = text.replaceAll("(\\W+)" + quotemeta + "(\\W+)", "$1<rot13sub>" + rot13 + "<bus31tor>$2");
//			text = text.replaceAll("(\\W+)" + quotemeta + "$", "$1<rot13sub>" + rot13 + "<bus31tor>");
		}
		if (text.indexOf("<rot13sub>") > -1) {
			Pattern re = Pattern.compile("<rot13sub>(.+?)<bus31tor>");
			Matcher m  = re.matcher(text);
			while (m.find()) {
				String block = m.group(0);
				String data  = ROT13(m.group(1));
				text = text.replace(block, data);
			}
		}
		return text;
	}

	/**
	 * Simple ROT13 encoder.
	 *
	 * @param text Text to encode to ROT13.
	 */
	public static String ROT13 (String text) {
		Vector<String> reply = new Vector<String>();
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if ((c >= 'a' && c <= 'm') || (c >= 'A' && c <= 'M')) {
				c += 13;
			}
			else if ((c >= 'n' && c <= 'z') || (c >= 'N' && c <= 'Z')) {
				c -= 13;
			}
			reply.add(Character.toString(c));
		}
		return join(Sv2s(reply),"");
	}
}
