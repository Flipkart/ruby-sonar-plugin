package com.godaddy.sonar.ruby.metricfu.rules;

/**
 * Created by akash.v on 27/04/16.
 */
public class RoodiProblem {

    public String file;

    public int line;

    public String problem;

    public RoodiProblem(String file, int line, String problem){
        this.file = file;
        this.line = line;
        this.problem = problem;
    }


    public int getLine() {
        return line;
    }

}
