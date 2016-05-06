package com.godaddy.sonar.ruby.metricfu.rules;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by akash.v on 02/05/16.
 */
public class RoodiRuleParser {

    private static final String RULES_FILE = "rules.yml";
    private Yaml yaml;
    private List<RoodiRule> roodiRules;
    InMemoryRuleStore inMemoryRuleStore =  new InMemoryRuleStore();

    private static final Logger LOG = LoggerFactory
            .getLogger(RoodiRuleParser.class);

    public RoodiRuleParser(){
        yaml = new Yaml();
        parse();
    }

    public List<RoodiRule> parse(){

        if(roodiRules == null) {
            roodiRules = Lists.newArrayList();

            for(Map p: (List<Map<String, String>>) yaml.load(getClass().getResourceAsStream(RULES_FILE))){
                roodiRules.add(ruleFor(p));
            }
            updateRuleStore();
        }

        return roodiRules;
    }

    private RoodiRule ruleFor(Map p) {
        RoodiRule r =  new RoodiRule();
        r.key = (String) p.get("key");
        r.description = (String) p.get("description");
        r.name = (String) p.get("name");
        r.match = (String) p.get("match");
        r.severity = (String) p.get("severity");
        r.debtRemediationFunctionOffset = (String) p.get("debtRemediationFunctionOffset");

        return r;
    }


    private void updateRuleStore() {
        for(RoodiRule r : roodiRules){
            inMemoryRuleStore.addRule(r.key, r.match);
        }
    }

    public String getKey(String discription){
        return inMemoryRuleStore.findRule(discription);
    }

    public static void main(String[] args) {
        RoodiRuleParser roodiRuleParser = new RoodiRuleParser();
        roodiRuleParser.parse();
    }
}
