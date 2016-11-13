/*
 * Copyright 2016 Deyan Rizov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.rizov.pgnparse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PGNSource {

	private String source;
	
	public PGNSource(String pgn) {
		if (pgn == null) {
			throw new NullPointerException("PGN data is null");
		}
		
		this.source = pgn;
	}
	
	public PGNSource(File file) throws IOException {
		this(new FileInputStream(file));
	}
	
	public PGNSource(URL url) throws IOException {
		this(url.openStream());
	}
	
	public PGNSource(InputStream inputStream) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		String line;
		StringBuilder buffer = new StringBuilder();
		
		while ((line = br.readLine()) != null) {
			buffer.append(line + "\n");
		}
		
		br.close();
		this.source = buffer.toString();
	}
	
	@Override
	public String toString() {
		return source;
	}
	
	public List<PGNGame> listGames() throws IOException, PGNParseException, MalformedMoveException {
		List<String> pgns = PGNHelper.splitPGN(source);
		ArrayList<PGNGame> games = new ArrayList<PGNGame>();

		for (String pgn : pgns) {
			games.add(PGNHelper.parse(pgn));
		}

		return games;
	}
	
	public List<PGNGame> listGames(boolean force) throws IOException, PGNParseException, MalformedMoveException {

		if (!force) {
			return listGames();
		}

		List<String> pgns = PGNHelper.splitPGN(source);
		ArrayList<PGNGame> games = new ArrayList<PGNGame>();

		for (String pgn : pgns) {
			try {
				games.add(PGNHelper.parse(pgn));
			} catch (PGNParseException e) {
			} catch (MalformedMoveException e) {}
		}

		return games;
	}
	
}
