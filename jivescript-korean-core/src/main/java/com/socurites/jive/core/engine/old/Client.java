/*
	com.rivescript.RiveScript - The Official Java RiveScript Interpreter

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

import java.lang.String;
import java.util.HashMap;

import com.socurites.jive.core.bot.conts.JiveScriptConstants;

/**
 * An object to represent an individual user's data.
 */

public class Client {
	private String id;
	private HashMap<String, String> data = new HashMap<String, String>(); // User data
	private String[] input = new String [10]; // User's inputs
	private String[] reply = new String [10]; // Bot's replies

	/**
	 * Create a new client object.
	 *
	 * @param id A unique ID for this client.
	 */
	public Client (String id) {
		this.id = id;

		// Set default vars.
		set("topic",JiveScriptConstants.TOPIC_RANDOM);

		// Initialize the user's history.
		for (int i = 0; i < input.length; i++) {
			input[i] = "undefined";
			reply[i] = "undefined";
		}
	}

	/**
	 * Set a variable for the client.
	 *
	 * @param name  The name of the variable.
	 * @param value The value to set in the variable.
	 */
	public void set (String name, String value) {
		data.put(name, value);
	}

	/**
	 * Get a variable from the client. Returns the text "undefined" if it doesn't
	 * exist.
	 *
	 * @param name The name of the variable.
	 */
	public String get (String name) {
		if (data.containsKey(name)) {
			return data.get(name);
		}
		return "undefined";
	}

	/**
	 * Delete a variable for the client.
	 *
	 * @param name The name of the variable.
	 */
	public void delete (String name) {
		if (data.containsKey(name)) {
			data.remove(name);
		}
	}

	/**
	 * Retrieve a hashmap of all the user's vars and values.
	 */
	public HashMap<String, String> getData () {
		return data;
	}

	/**
	 * Replace the internal hashmap with this new data (dangerous!).
	 */
	public boolean setData (HashMap<String, String> newdata) {
		this.data = newdata;
		return true;
	}

	/**
	 * Add a line to the user's input history.
	 */
	public void addInput (String text) {
		// Push this onto the front of the input array.
		input = unshift(input, text);
	}

	/**
	 * Add a line to the user's reply history.
	 */
	public void addReply (String text) {
		// Push this onto the front of the reply array.
		reply = unshift(reply, text);
	}

	/**
	 * Get a specific input value by index.
	 *
	 * @param index The index of the input value to get (1-9).
	 */
	public String getInput (int index) throws java.lang.IndexOutOfBoundsException {
		if (index >= 1 && index <= 9) {
			return this.input[index-1];
		}
		else {
			throw new java.lang.IndexOutOfBoundsException();
		}
	}

	/**
	 * Get a specific reply value by index.
	 *
	 * @param index The index of the reply value to get (1-9).
	 */
	public String getReply (int index) throws java.lang.IndexOutOfBoundsException {
		if (index >= 1 && index <= 9) {
			return this.reply[index-1];
		}
		else {
			throw new java.lang.IndexOutOfBoundsException();
		}
	}

	/**
	 * Shift an item to the beginning of an array and rotate.
	 */
	public String[] unshift (String[] array, String addition) {
		// First rotate all entries from 0 to the end.
		for (int i = array.length - 1; i > 0; i--) {
			array[i] = array[i - 1];
		}

		// Now set the first item.
		array[0] = addition;
		return array;
	}
}
