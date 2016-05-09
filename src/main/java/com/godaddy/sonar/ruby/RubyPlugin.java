package com.godaddy.sonar.ruby;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.godaddy.sonar.ruby.metricfu.MetricfuRoodiYamlParserImpl;
import com.godaddy.sonar.ruby.metricfu.rules.RoodiRuleParser;
import com.godaddy.sonar.ruby.metricfu.rules.RoodiSensor;
import com.godaddy.sonar.ruby.metricfu.rules.RubyRuleRepository;
import org.sonar.api.CoreProperties;
import org.sonar.api.Properties;
import org.sonar.api.PropertyType;
import org.sonar.api.SonarPlugin;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.resources.Qualifiers;

import com.godaddy.sonar.ruby.core.Ruby;
import com.godaddy.sonar.ruby.core.RubySourceCodeColorizer;
import com.godaddy.sonar.ruby.core.profiles.SonarWayProfile;
import com.godaddy.sonar.ruby.metricfu.MetricfuComplexitySensor;
import com.godaddy.sonar.ruby.metricfu.MetricfuComplexityYamlParserImpl;
import com.godaddy.sonar.ruby.simplecovrcov.SimpleCovRcovJsonParserImpl;
import com.godaddy.sonar.ruby.simplecovrcov.SimpleCovRcovSensor;

/**
 * This class is the entry point for all extensions
 */
@Properties({})
public final class RubyPlugin extends SonarPlugin
{
  public static final String SIMPLECOVRCOV_REPORT_PATH_PROPERTY  = "sonar.simplecovrcov.reportPath";
  public static final String METRICFU_REPORT_PATH_PROPERTY       = "sonar.metricfu.reportPath";
  public static final String METRICFU_COMPLEXITY_METRIC_PROPERTY = "sonar.metricfu.complexityMetric";

  public List<Object> getExtensions()
  {
    List<Object> extensions = new ArrayList<Object>();

      extensions.add(Ruby.class);
      extensions.add(SimpleCovRcovSensor.class);
      extensions.add(SimpleCovRcovJsonParserImpl.class);
      extensions.add(MetricfuComplexityYamlParserImpl.class);
      extensions.add(RubySourceCodeColorizer.class);
      extensions.add(RubySensor.class);
      extensions.add(MetricfuComplexitySensor.class);

    // Profiles
    extensions.add(SonarWayProfile.class);

    PropertyDefinition metricfuReportPath = PropertyDefinition.builder(METRICFU_REPORT_PATH_PROPERTY)
        .category(CoreProperties.CATEGORY_CODE_COVERAGE)
        .subCategory("Ruby Coverage")
        .name("MetricFu Report path")
        .description("Path (absolute or relative) to MetricFu yml report file.")
        .defaultValue("tmp/metric_fu/report.yml")
        .onQualifiers(Qualifiers.PROJECT)
        .build();
    extensions.add(metricfuReportPath);

    PropertyDefinition simplecovrcovReportPath = PropertyDefinition.builder(SIMPLECOVRCOV_REPORT_PATH_PROPERTY)
        .category(CoreProperties.CATEGORY_CODE_COVERAGE)
        .subCategory("Ruby Coverage")
        .name("SimpleCovRcov Report path")
        .description("Path (absolute or relative) to SimpleCovRcov json report file.")
        .defaultValue("coverage/.resultset.json")
        .onQualifiers(Qualifiers.PROJECT)
        .build();
    extensions.add(simplecovrcovReportPath);

    List<String> options = Arrays.asList("Saikuro", "Cane");

    PropertyDefinition ComplexityMetric = PropertyDefinition.builder(METRICFU_COMPLEXITY_METRIC_PROPERTY)
        .category(CoreProperties.CATEGORY_CODE_COVERAGE)
        .subCategory("Ruby Coverage")
        .name("MetricFu Complexity Metric")
        .description("Type of complexity, Saikuro or Cane")
        .defaultValue("Cane")
        .onQualifiers(Qualifiers.PROJECT)
        .type(PropertyType.SINGLE_SELECT_LIST)
        .options(options)
        .build();
    extensions.add(ComplexityMetric);

      extensions.add(RubyRuleRepository.class);

      extensions.add(MetricfuRoodiYamlParserImpl.class);
      extensions.add(RoodiSensor.class);
//      extensions.add(MetricfuDuplicationSensor.class);
//      extensions.add(MetricfuDuplicationYamlParserImpl.class);

    return extensions;
  }
}
