package com.godaddy.sonar.ruby.metricfu;

import com.godaddy.sonar.ruby.metricfu.rules.RoodiProblem;
import org.sonar.api.BatchExtension;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by akash.v on 27/04/16.
 */
public interface MetricfuRoodiYamlParser extends BatchExtension{

    public List<RoodiProblem> parse(String fileName, File resultsFile) throws FileNotFoundException;
}
