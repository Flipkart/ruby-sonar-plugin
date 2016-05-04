package com.godaddy.sonar.ruby.metricfu;

import com.godaddy.sonar.ruby.metricfu.rules.RoodiProblem;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * Created by akash.v on 27/04/16.
 */
public class MetricfuRoodiYamlParserImpl implements MetricfuRoodiYamlParser {
    private static final Logger LOG = LoggerFactory
            .getLogger(MetricfuRoodiYamlParser.class);
    private static Map<String, Object> metricfuResult;
    @Override
    public List<RoodiProblem> parse(String fileName, File resultsFile) throws FileNotFoundException {

        InputStream resultsStream = new FileInputStream(resultsFile);
        LOG.debug("MetricfuRoodiYamlParserImpl Start start parse of metrics_fu YAML");
        List<RoodiProblem> roodiProblems = Lists.newArrayList();
        Yaml yaml = new Yaml();

        try {
            if(metricfuResult == null)
            metricfuResult = (Map<String, Object>) yaml.load(resultsStream);
            Map<String, Map<String, Object>> roodiResult = (Map<String, Map<String, Object>>) metricfuResult.get(":roodi");

            for(Map<String, String> problemMap: (List<Map<String, String> >) roodiResult.get(":problems")){
                String file = problemMap.get(":file");
                if(fileName.equals(file)) {
                    roodiProblems.add(getProblem(problemMap));

                }
            }

        } catch (Exception e) {
            LOG.error("Failure parsing YAML results {}", Throwables.getStackTraceAsString(e));
        }

        return roodiProblems;
    }

    private RoodiProblem getProblem(Map<String, String> problem) {
        return new RoodiProblem(problem.get(":file"), Integer.parseInt(problem.get(":line")), problem.get(":problem"));
    }

    public static void main(String[] args) throws FileNotFoundException {
        MetricfuRoodiYamlParser parser= new MetricfuRoodiYamlParserImpl();
        List<RoodiProblem> problems = parser.parse("app/builders/fulfillment_model_builder_factory.rb", new File("/Users/akash.v/Sources/office/symphony/tmp/metric_fu/report.yml"));
    }
}
