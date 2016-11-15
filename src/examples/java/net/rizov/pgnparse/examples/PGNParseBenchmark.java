/*
 * This file is part of PGNParse.
 *
 * PGNParse is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PGNParse is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PGNParse.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.rizov.pgnparse.examples;

import net.rizov.pgnparse.PGNGame;
import net.rizov.pgnparse.PGNParseException;
import net.rizov.pgnparse.PGNSource;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

public class PGNParseBenchmark {

    public static void main(String[] args) throws IOException, PGNParseException {
        if (args.length == 0) {
            System.out.println("Usage:");
            System.out.println("\tpgn_directory_path");
            return;
        }

        File file = new File(args[0]);

        if (!file.exists() || !file.isDirectory()) {
            System.out.println("Directory does not exist!");
            return;
        }

        String[] pgnFileNames = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".pgn");
            }
        });

        long startTime = System.currentTimeMillis();
        int filesParsed = 0;
        int gamesParsed = 0;

        for (String pgnFileName : pgnFileNames) {
            File pgnFile = new File(file.getAbsolutePath() + File.separator + pgnFileName);
            PGNSource source = new PGNSource(pgnFile);

            for (PGNGame game : source.listGames()) {
                gamesParsed++;
            }

            filesParsed++;
        }

        long duration = System.currentTimeMillis() - startTime;
        System.out.println("Files parsed: " + filesParsed);
        System.out.println("Games parsed: " + gamesParsed);
        System.out.println("Duration: " + duration);

    }

}
