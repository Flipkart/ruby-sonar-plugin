package com.godaddy.sonar.ruby.metricfu.rules;

import com.godaddy.sonar.ruby.core.Ruby;
import org.sonar.api.rule.RuleStatus;
import org.sonar.api.server.rule.RulesDefinition;

import javax.annotation.ParametersAreNonnullByDefault;


/**
 * Created by akash.v on 27/04/16.
 */
public class RubyRuleRepository implements RulesDefinition {

    public static final String REPOSITORY_NAME = "Roodi";
    public static final String REPOSITORY_KEY = REPOSITORY_NAME;
    RoodiRuleParser roodiRuleParser = new RoodiRuleParser();

    @ParametersAreNonnullByDefault
    public void define(Context context) {
        NewRepository repository = context
                .createRepository(REPOSITORY_KEY, Ruby.KEY)
                .setName(REPOSITORY_NAME);

        roodiRuleParser = new RoodiRuleParser();

        for(RoodiRule rule : roodiRuleParser.parse()){
            NewRule newRule = repository.createRule(rule.key)
                    .setName(rule.name)
                    .setHtmlDescription(rule.description)
                    .setStatus(RuleStatus.READY)
                    .setSeverity(rule.severity);
            newRule.setDebtSubCharacteristic(SubCharacteristics.LOGIC_RELIABILITY)
                    .setDebtRemediationFunction(newRule.debtRemediationFunctions().constantPerIssue(rule.debtRemediationFunctionOffset));
        }


        repository.done();
    }
}
