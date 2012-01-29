package net.minecraft.src;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;

public final class J_SajParser {
	public J_SajParser() {
	}

	public void parse(Reader reader, J_JsonListener j_jsonlistener)
	throws J_InvalidSyntaxException, IOException {
		J_PositionTrackingPushbackReader j_positiontrackingpushbackreader = new J_PositionTrackingPushbackReader(reader);
		char c = (char)j_positiontrackingpushbackreader.read();
		switch (c) {
			case 123:
				j_positiontrackingpushbackreader.unread(c);
				j_jsonlistener.startDocument();
				objectString(j_positiontrackingpushbackreader, j_jsonlistener);
				break;

			case 91:
				j_positiontrackingpushbackreader.unread(c);
				j_jsonlistener.startDocument();
				arrayString(j_positiontrackingpushbackreader, j_jsonlistener);
				break;

			default:
				throw new J_InvalidSyntaxException((new StringBuilder()).append("Expected either [ or { but got [").append(c).append("].").toString(), j_positiontrackingpushbackreader);
		}
		int i = readNextNonWhitespaceChar(j_positiontrackingpushbackreader);
		if (i != -1) {
			throw new J_InvalidSyntaxException((new StringBuilder()).append("Got unexpected trailing character [").append((char)i).append("].").toString(), j_positiontrackingpushbackreader);
		}
		else {
			j_jsonlistener.endDocument();
			return;
		}
	}

	private void arrayString(J_PositionTrackingPushbackReader j_positiontrackingpushbackreader, J_JsonListener j_jsonlistener)
	throws J_InvalidSyntaxException, IOException {
		char c = (char)readNextNonWhitespaceChar(j_positiontrackingpushbackreader);
		if (c != '[') {
			throw new J_InvalidSyntaxException((new StringBuilder()).append("Expected object to start with [ but got [").append(c).append("].").toString(), j_positiontrackingpushbackreader);
		}
		j_jsonlistener.startArray();
		char c1 = (char)readNextNonWhitespaceChar(j_positiontrackingpushbackreader);
		j_positiontrackingpushbackreader.unread(c1);
		if (c1 != ']') {
			aJsonValue(j_positiontrackingpushbackreader, j_jsonlistener);
		}
		boolean flag = false;
		do {
			if (flag) {
				break;
			}
			char c2 = (char)readNextNonWhitespaceChar(j_positiontrackingpushbackreader);
			switch (c2) {
				case 44:
					aJsonValue(j_positiontrackingpushbackreader, j_jsonlistener);
					break;

				case 93:
					flag = true;
					break;

				default:
					throw new J_InvalidSyntaxException((new StringBuilder()).append("Expected either , or ] but got [").append(c2).append("].").toString(), j_positiontrackingpushbackreader);
			}
		}
		while (true);
		j_jsonlistener.endArray();
	}

	private void objectString(J_PositionTrackingPushbackReader j_positiontrackingpushbackreader, J_JsonListener j_jsonlistener)
	throws J_InvalidSyntaxException, IOException {
		char c = (char)readNextNonWhitespaceChar(j_positiontrackingpushbackreader);
		if (c != '{') {
			throw new J_InvalidSyntaxException((new StringBuilder()).append("Expected object to start with { but got [").append(c).append("].").toString(), j_positiontrackingpushbackreader);
		}
		j_jsonlistener.startObject();
		char c1 = (char)readNextNonWhitespaceChar(j_positiontrackingpushbackreader);
		j_positiontrackingpushbackreader.unread(c1);
		if (c1 != '}') {
			aFieldToken(j_positiontrackingpushbackreader, j_jsonlistener);
		}
		boolean flag = false;
		do {
			if (flag) {
				break;
			}
			char c2 = (char)readNextNonWhitespaceChar(j_positiontrackingpushbackreader);
			switch (c2) {
				case 44:
					aFieldToken(j_positiontrackingpushbackreader, j_jsonlistener);
					break;

				case 125:
					flag = true;
					break;

				default:
					throw new J_InvalidSyntaxException((new StringBuilder()).append("Expected either , or } but got [").append(c2).append("].").toString(), j_positiontrackingpushbackreader);
			}
		}
		while (true);
		j_jsonlistener.endObject();
	}

	private void aFieldToken(J_PositionTrackingPushbackReader j_positiontrackingpushbackreader, J_JsonListener j_jsonlistener)
	throws J_InvalidSyntaxException, IOException {
		char c = (char)readNextNonWhitespaceChar(j_positiontrackingpushbackreader);
		if ('"' != c) {
			throw new J_InvalidSyntaxException((new StringBuilder()).append("Expected object identifier to begin with [\"] but got [").append(c).append("].").toString(), j_positiontrackingpushbackreader);
		}
		j_positiontrackingpushbackreader.unread(c);
		j_jsonlistener.startField(stringToken(j_positiontrackingpushbackreader));
		char c1 = (char)readNextNonWhitespaceChar(j_positiontrackingpushbackreader);
		if (c1 != ':') {
			throw new J_InvalidSyntaxException((new StringBuilder()).append("Expected object identifier to be followed by : but got [").append(c1).append("].").toString(), j_positiontrackingpushbackreader);
		}
		else {
			aJsonValue(j_positiontrackingpushbackreader, j_jsonlistener);
			j_jsonlistener.endField();
			return;
		}
	}

	private void aJsonValue(J_PositionTrackingPushbackReader j_positiontrackingpushbackreader, J_JsonListener j_jsonlistener)
	throws J_InvalidSyntaxException, IOException {
		char c = (char)readNextNonWhitespaceChar(j_positiontrackingpushbackreader);
		switch (c) {
			case 34:
				j_positiontrackingpushbackreader.unread(c);
				j_jsonlistener.stringValue(stringToken(j_positiontrackingpushbackreader));
				break;

			case 116:
				char ac[] = new char[3];
				int i = j_positiontrackingpushbackreader.read(ac);
				if (i != 3 || ac[0] != 'r' || ac[1] != 'u' || ac[2] != 'e') {
					j_positiontrackingpushbackreader.uncount(ac);
					throw new J_InvalidSyntaxException((new StringBuilder()).append("Expected 't' to be followed by [[r, u, e]], but got [").append(Arrays.toString(ac)).append("].").toString(), j_positiontrackingpushbackreader);
				}
				j_jsonlistener.trueValue();
				break;

			case 102:
				char ac1[] = new char[4];
				int j = j_positiontrackingpushbackreader.read(ac1);
				if (j != 4 || ac1[0] != 'a' || ac1[1] != 'l' || ac1[2] != 's' || ac1[3] != 'e') {
					j_positiontrackingpushbackreader.uncount(ac1);
					throw new J_InvalidSyntaxException((new StringBuilder()).append("Expected 'f' to be followed by [[a, l, s, e]], but got [").append(Arrays.toString(ac1)).append("].").toString(), j_positiontrackingpushbackreader);
				}
				j_jsonlistener.falseValue();
				break;

			case 110:
				char ac2[] = new char[3];
				int k = j_positiontrackingpushbackreader.read(ac2);
				if (k != 3 || ac2[0] != 'u' || ac2[1] != 'l' || ac2[2] != 'l') {
					j_positiontrackingpushbackreader.uncount(ac2);
					throw new J_InvalidSyntaxException((new StringBuilder()).append("Expected 'n' to be followed by [[u, l, l]], but got [").append(Arrays.toString(ac2)).append("].").toString(), j_positiontrackingpushbackreader);
				}
				j_jsonlistener.nullValue();
				break;

			case 45:
			case 48:
			case 49:
			case 50:
			case 51:
			case 52:
			case 53:
			case 54:
			case 55:
			case 56:
			case 57:
				j_positiontrackingpushbackreader.unread(c);
				j_jsonlistener.numberValue(numberToken(j_positiontrackingpushbackreader));
				break;

			case 123:
				j_positiontrackingpushbackreader.unread(c);
				objectString(j_positiontrackingpushbackreader, j_jsonlistener);
				break;

			case 91:
				j_positiontrackingpushbackreader.unread(c);
				arrayString(j_positiontrackingpushbackreader, j_jsonlistener);
				break;

			default:
				throw new J_InvalidSyntaxException((new StringBuilder()).append("Invalid character at start of value [").append(c).append("].").toString(), j_positiontrackingpushbackreader);
		}
	}

	private String numberToken(J_PositionTrackingPushbackReader j_positiontrackingpushbackreader)
	throws IOException, J_InvalidSyntaxException {
		StringBuilder stringbuilder = new StringBuilder();
		char c = (char)j_positiontrackingpushbackreader.read();
		if ('-' == c) {
			stringbuilder.append('-');
		}
		else {
			j_positiontrackingpushbackreader.unread(c);
		}
		stringbuilder.append(nonNegativeNumberToken(j_positiontrackingpushbackreader));
		return stringbuilder.toString();
	}

	private String nonNegativeNumberToken(J_PositionTrackingPushbackReader j_positiontrackingpushbackreader)
	throws IOException, J_InvalidSyntaxException {
		StringBuilder stringbuilder = new StringBuilder();
		char c = (char)j_positiontrackingpushbackreader.read();
		if ('0' == c) {
			stringbuilder.append('0');
			stringbuilder.append(possibleFractionalComponent(j_positiontrackingpushbackreader));
			stringbuilder.append(possibleExponent(j_positiontrackingpushbackreader));
		}
		else {
			j_positiontrackingpushbackreader.unread(c);
			stringbuilder.append(nonZeroDigitToken(j_positiontrackingpushbackreader));
			stringbuilder.append(digitString(j_positiontrackingpushbackreader));
			stringbuilder.append(possibleFractionalComponent(j_positiontrackingpushbackreader));
			stringbuilder.append(possibleExponent(j_positiontrackingpushbackreader));
		}
		return stringbuilder.toString();
	}

	private char nonZeroDigitToken(J_PositionTrackingPushbackReader j_positiontrackingpushbackreader)
	throws IOException, J_InvalidSyntaxException {
		char c1 = (char)j_positiontrackingpushbackreader.read();
		char c;
		switch (c1) {
			case 49:
			case 50:
			case 51:
			case 52:
			case 53:
			case 54:
			case 55:
			case 56:
			case 57:
				c = c1;
				break;

			default:
				throw new J_InvalidSyntaxException((new StringBuilder()).append("Expected a digit 1 - 9 but got [").append(c1).append("].").toString(), j_positiontrackingpushbackreader);
		}
		return c;
	}

	private char digitToken(J_PositionTrackingPushbackReader j_positiontrackingpushbackreader)
	throws IOException, J_InvalidSyntaxException {
		char c1 = (char)j_positiontrackingpushbackreader.read();
		char c;
		switch (c1) {
			case 48:
			case 49:
			case 50:
			case 51:
			case 52:
			case 53:
			case 54:
			case 55:
			case 56:
			case 57:
				c = c1;
				break;

			default:
				throw new J_InvalidSyntaxException((new StringBuilder()).append("Expected a digit 1 - 9 but got [").append(c1).append("].").toString(), j_positiontrackingpushbackreader);
		}
		return c;
	}

	private String digitString(J_PositionTrackingPushbackReader j_positiontrackingpushbackreader)
	throws IOException {
		StringBuilder stringbuilder = new StringBuilder();
		boolean flag = false;
		do {
			while (!flag) {
				char c = (char)j_positiontrackingpushbackreader.read();
				switch (c) {
					case 48:
					case 49:
					case 50:
					case 51:
					case 52:
					case 53:
					case 54:
					case 55:
					case 56:
					case 57:
						stringbuilder.append(c);
						break;

					default:
						flag = true;
						j_positiontrackingpushbackreader.unread(c);
						break;
				}
			}
			return stringbuilder.toString();
		}
		while (true);
	}

	private String possibleFractionalComponent(J_PositionTrackingPushbackReader j_positiontrackingpushbackreader)
	throws IOException, J_InvalidSyntaxException {
		StringBuilder stringbuilder = new StringBuilder();
		char c = (char)j_positiontrackingpushbackreader.read();
		if (c == '.') {
			stringbuilder.append('.');
			stringbuilder.append(digitToken(j_positiontrackingpushbackreader));
			stringbuilder.append(digitString(j_positiontrackingpushbackreader));
		}
		else {
			j_positiontrackingpushbackreader.unread(c);
		}
		return stringbuilder.toString();
	}

	private String possibleExponent(J_PositionTrackingPushbackReader j_positiontrackingpushbackreader)
	throws IOException, J_InvalidSyntaxException {
		StringBuilder stringbuilder = new StringBuilder();
		char c = (char)j_positiontrackingpushbackreader.read();
		if (c == '.' || c == 'E') {
			stringbuilder.append('E');
			stringbuilder.append(possibleSign(j_positiontrackingpushbackreader));
			stringbuilder.append(digitToken(j_positiontrackingpushbackreader));
			stringbuilder.append(digitString(j_positiontrackingpushbackreader));
		}
		else {
			j_positiontrackingpushbackreader.unread(c);
		}
		return stringbuilder.toString();
	}

	private String possibleSign(J_PositionTrackingPushbackReader j_positiontrackingpushbackreader)
	throws IOException {
		StringBuilder stringbuilder = new StringBuilder();
		char c = (char)j_positiontrackingpushbackreader.read();
		if (c == '+' || c == '-') {
			stringbuilder.append(c);
		}
		else {
			j_positiontrackingpushbackreader.unread(c);
		}
		return stringbuilder.toString();
	}

	private String stringToken(J_PositionTrackingPushbackReader j_positiontrackingpushbackreader)
	throws J_InvalidSyntaxException, IOException {
		StringBuilder stringbuilder = new StringBuilder();
		char c = (char)j_positiontrackingpushbackreader.read();
		if ('"' != c) {
			throw new J_InvalidSyntaxException((new StringBuilder()).append("Expected [\"] but got [").append(c).append("].").toString(), j_positiontrackingpushbackreader);
		}
		boolean flag = false;
		do {
			if (flag) {
				break;
			}
			char c1 = (char)j_positiontrackingpushbackreader.read();
			switch (c1) {
				case 34:
					flag = true;
					break;

				case 92:
					char c2 = escapedStringChar(j_positiontrackingpushbackreader);
					stringbuilder.append(c2);
					break;

				default:
					stringbuilder.append(c1);
					break;
			}
		}
		while (true);
		return stringbuilder.toString();
	}

	private char escapedStringChar(J_PositionTrackingPushbackReader j_positiontrackingpushbackreader)
	throws IOException, J_InvalidSyntaxException {
		char c1 = (char)j_positiontrackingpushbackreader.read();
		char c;
		switch (c1) {
			case 34:
				c = '"';
				break;

			case 92:
				c = '\\';
				break;

			case 47:
				c = '/';
				break;

			case 98:
				c = '\b';
				break;

			case 102:
				c = '\f';
				break;

			case 110:
				c = '\n';
				break;

			case 114:
				c = '\r';
				break;

			case 116:
				c = '\t';
				break;

			case 117:
				c = (char)hexadecimalNumber(j_positiontrackingpushbackreader);
				break;

			default:
				throw new J_InvalidSyntaxException((new StringBuilder()).append("Unrecognised escape character [").append(c1).append("].").toString(), j_positiontrackingpushbackreader);
		}
		return c;
	}

	private int hexadecimalNumber(J_PositionTrackingPushbackReader j_positiontrackingpushbackreader)
	throws IOException, J_InvalidSyntaxException {
		char ac[] = new char[4];
		int i = j_positiontrackingpushbackreader.read(ac);
		if (i != 4) {
			throw new J_InvalidSyntaxException((new StringBuilder()).append("Expected a 4 digit hexidecimal number but got only [").append(i).append("], namely [").append(String.valueOf(ac, 0, i)).append("].").toString(), j_positiontrackingpushbackreader);
		}
		int j;
		try {
			j = Integer.parseInt(String.valueOf(ac), 16);
		}
		catch (NumberFormatException numberformatexception) {
			j_positiontrackingpushbackreader.uncount(ac);
			throw new J_InvalidSyntaxException((new StringBuilder()).append("Unable to parse [").append(String.valueOf(ac)).append("] as a hexidecimal number.").toString(), numberformatexception, j_positiontrackingpushbackreader);
		}
		return j;
	}

	private int readNextNonWhitespaceChar(J_PositionTrackingPushbackReader j_positiontrackingpushbackreader)
	throws IOException {
		boolean flag = false;
		int i;
		do {
			i = j_positiontrackingpushbackreader.read();
			switch (i) {
				default:
					flag = true;
					break;

				case 9:
				case 10:
				case 13:
				case 32:
					break;
			}
		}
		while (!flag);
		return i;
	}
}
