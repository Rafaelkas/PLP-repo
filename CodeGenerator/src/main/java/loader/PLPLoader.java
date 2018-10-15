package loader;

import conditions.BitwiseOperation;
import conditions.Condition;
import conditions.Formula;
import conditions.NotCondition;
import conditions.QuantifiedCondition;
import conditions.BitwiseOperation.Operation;
import conditions.QuantifiedCondition.Quantifier;
import distributions.ConditionalDist;
import distributions.Distribution;
import distributions.ExpDistribution;
import distributions.GammaDistribution;
import distributions.NormalDistribution;
import distributions.UniformDistribution;
import distributions.UnkownDistribution;
import effects.AssignmentEffect;
import effects.ConditionalEffect;
import effects.Effect;
import effects.ForAllEffect;
import effects.NotEffect;
import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import modules.AchievePLP;
import modules.DetectPLP;
import modules.MaintainPLP;
import modules.ObservePLP;
import modules.PLP;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import plpEtc.ConfidenceInterval;
import plpEtc.FieldType;
import plpEtc.Predicate;
import plpEtc.Range;
import plpFields.ConditionalProb;
import plpFields.Constant;
import plpFields.FailureMode;
import plpFields.ModuleRestriction;
import plpFields.ObservationGoal;
import plpFields.PLPParameter;
import plpFields.ProgressMeasure;
import plpFields.RequiredResource;
import plpFields.Variable;
import plpFields.ModuleRestriction.ConcurrencyType;
import plpFields.RequiredResource.RequirementStatus;

public class PLPLoader {
    private static List<AchievePLP> achievePLPs;
    private static List<ObservePLP> observePLPs;
    private static List<DetectPLP> detectPLPs;
    private static List<MaintainPLP> maintainPLPs;

    public PLPLoader() {
    }

    public static List<AchievePLP> getAchievePLPs() {
        return achievePLPs;
    }

    public static List<ObservePLP> getObservePLPs() {
        return observePLPs;
    }

    public static List<DetectPLP> getDetectPLPs() {
        return detectPLPs;
    }

    public static List<MaintainPLP> getMaintainPLPs() {
        return maintainPLPs;
    }

    public static void main(String[] args) {
    }

    public static void loadFromDirectory(String dir) {
        achievePLPs = new LinkedList();
        observePLPs = new LinkedList();
        detectPLPs = new LinkedList();
        maintainPLPs = new LinkedList();
        if (!dir.endsWith("\\") && dir.contains("\\")) {
            dir = dir.concat("\\");
        } else if (!dir.endsWith("/")) {
            dir = dir.concat("/");
        }

        File folder = new File(dir);
        if (!folder.isDirectory()) {
            throw new IllegalArgumentException("Path " + dir + " is not a legal directory");
        } else {
            File[] files = folder.listFiles();

            for(int i = 0; i < files.length; ++i) {
                File file = files[i];
                String tempName = file.getName();
                if (file.isFile() && tempName.substring(tempName.lastIndexOf(".") + 1).equalsIgnoreCase("xml")) {
                    loadFromFile(dir.concat(tempName));
                }
            }

        }
    }

    private static void loadFromFile(String fileName) {
        try {
            File plpFile = new File(fileName);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(plpFile);
            doc.getDocumentElement().normalize();
            Element rootElement = doc.getDocumentElement();
            String PLPname = rootElement.getAttribute("name");
            String nodeNameWithoutNS = rootElement.getNodeName().substring(rootElement.getNodeName().indexOf(58) + 1);
            byte var10 = -1;
            switch(nodeNameWithoutNS.hashCode()) {
                case -1945305512:
                    if (nodeNameWithoutNS.equals("detect_plp")) {
                        var10 = 3;
                    }
                    break;
                case 125420625:
                    if (nodeNameWithoutNS.equals("observe_plp")) {
                        var10 = 2;
                    }
                    break;
                case 1747213062:
                    if (nodeNameWithoutNS.equals("achieve_plp")) {
                        var10 = 0;
                    }
                    break;
                case 1870584128:
                    if (nodeNameWithoutNS.equals("maintain_plp")) {
                        var10 = 1;
                    }
            }

            Object plp;
            switch(var10) {
                case 0:
                    plp = new AchievePLP(PLPname);
                    achievePLPs.add((AchievePLP)plp);
                    LoadAchieveFields(rootElement, (AchievePLP)plp);
                    break;
                case 1:
                    plp = new MaintainPLP(PLPname);
                    maintainPLPs.add((MaintainPLP)plp);
                    LoadMaintainFields(rootElement, (MaintainPLP)plp);
                    break;
                case 2:
                    plp = new ObservePLP(PLPname);
                    observePLPs.add((ObservePLP)plp);
                    LoadObserveFields(rootElement, (ObservePLP)plp);
                    break;
                case 3:
                    plp = new DetectPLP(PLPname);
                    detectPLPs.add((DetectPLP)plp);
                    LoadDetectFields(rootElement, (DetectPLP)plp);
                    break;
                default:
                    return;
            }

            String version = rootElement.getAttribute("version");
            String glueFile = rootElement.getAttribute("glue_file_location");
            if (version != null) {
                ((PLP)plp).setVersion(Double.parseDouble(version));
            }

            if (glueFile != null) {
                ((PLP)plp).setGlueFile(glueFile);
            }

            Node currentNode = rootElement.getElementsByTagName("parameters").item(0);
            if (currentNode != null && currentNode.getNodeType() == 1) {
                LoadParameters((Element)currentNode, (PLP)plp);
            }

            currentNode = rootElement.getElementsByTagName("variables").item(0);
            if (currentNode != null && currentNode.getNodeType() == 1) {
                LoadVariables((Element)currentNode, (PLP)plp);
            }

            currentNode = rootElement.getElementsByTagName("constants").item(0);
            if (currentNode != null && currentNode.getNodeType() == 1) {
                LoadConstants((Element)currentNode, (PLP)plp);
            }

            currentNode = rootElement.getElementsByTagName("required_resources").item(0);
            if (currentNode != null && currentNode.getNodeType() == 1) {
                LoadRequiredResources((Element)currentNode, (PLP)plp);
            }

            currentNode = rootElement.getElementsByTagName("preconditions").item(0);
            if (currentNode != null && currentNode.getNodeType() == 1) {
                LoadPreconditions((Element)currentNode, (PLP)plp);
            }

            currentNode = rootElement.getElementsByTagName("concurrency_conditions").item(0);
            if (currentNode != null && currentNode.getNodeType() == 1) {
                LoadConcurrencyConditions((Element)currentNode, (PLP)plp);
            }

            currentNode = rootElement.getElementsByTagName("concurrent_modules").item(0);
            if (currentNode != null && currentNode.getNodeType() == 1) {
                LoadConcurrentModules((Element)currentNode, (PLP)plp);
            }

            currentNode = rootElement.getElementsByTagName("side_effects").item(0);
            if (currentNode != null && currentNode.getNodeType() == 1) {
                LoadSideEffects((Element)currentNode, (PLP)plp);
            }

            currentNode = rootElement.getElementsByTagName("progress_measures").item(0);
            if (currentNode != null && currentNode.getNodeType() == 1) {
                LoadProgressMeasures((Element)currentNode, (PLP)plp);
            }

            Iterator var12 = getObservePLPs().iterator();

            while(var12.hasNext()) {
                ObservePLP oplp = (ObservePLP)var12.next();
                if (!oplp.isGoalParameter() && oplp.getResultParameter() == null) {
                    throw new RuntimeException("Observe PLP: " + oplp.getBaseName() + " is missing the result parameter from the parameter list");
                }

                if (oplp.isGoalParameter()) {
                    boolean foundParamMatch = false;
                    Iterator var15 = oplp.getOutputParams().iterator();

                    while(var15.hasNext()) {
                        PLPParameter outputParam = (PLPParameter)var15.next();
                        if (outputParam.toString().equals(oplp.getGoal().toString())) {
                            foundParamMatch = true;
                        }
                    }

                    if (!foundParamMatch) {
                        throw new RuntimeException("Observe PLP: " + oplp.getBaseName() + " has a parameter observation goal that isn't listed as an output parameter");
                    }
                }
            }

            var12 = getDetectPLPs().iterator();

            while(var12.hasNext()) {
                DetectPLP dplp = (DetectPLP)var12.next();
                if (dplp.getResultParameter() == null) {
                    throw new RuntimeException("Detect PLP: " + dplp.getBaseName() + " is missing the result parameter from the parameter list");
                }
            }
        } catch (Exception var17) {
            var17.printStackTrace();
        }

    }

    private static void LoadDetectFields(Element rootElement, DetectPLP plp) {
        Node currentNode = rootElement.getElementsByTagName("detection_goal").item(0);
        if (currentNode != null && currentNode.getNodeType() == 1) {
            plp.setGoal((Condition)parseConditions((Element)currentNode).get(0));
            plp.setResultParameterName(((Element)currentNode).getAttribute("result_parameter_name"));
        }

        currentNode = rootElement.getElementsByTagName("success_prob_given_condition").item(0);
        List condList;
        if (currentNode != null && currentNode.getNodeType() == 1) {
            condList = parseProb((Element)currentNode);
            for (int i = 0; i < condList.size(); i++) {
                plp.addSuccessProbGivenCond((ConditionalProb)condList.get(i));
            }
            //condList.forEach(plp::addSuccessProbGivenCond);
        }

        currentNode = rootElement.getElementsByTagName("failure_termination_condition").item(0);
        if (currentNode != null && currentNode.getNodeType() == 1) {
            condList = parseConditions((Element)currentNode);
            plp.setFailTerminationCond((Condition)condList.get(0));
        }

    }

    private static void LoadMaintainFields(Element rootElement, MaintainPLP plp) {
        Node currentNode = rootElement.getElementsByTagName("maintained_condition").item(0);
        if (currentNode != null && currentNode.getNodeType() == 1) {
            plp.setMaintainedCondition((Condition)parseConditions((Element)currentNode).get(0));
            if (((Element)currentNode).getElementsByTagName("initially_true").getLength() > 0) {
                plp.setInitiallyTrue(true);
            } else {
                plp.setInitiallyTrue(false);
                NodeList timeUntilTrueNL = ((Element)currentNode).getElementsByTagName("time_until_true");
                if (timeUntilTrueNL.getLength() > 0) {
                    Node timeUntilTrueNode = timeUntilTrueNL.item(0);
                    plp.setTimeUntilTrue(parseRuntime((Element)timeUntilTrueNode));
                }
            }
        }

        currentNode = rootElement.getElementsByTagName("success_termination_condition").item(0);
        if (currentNode != null && currentNode.getNodeType() == 1) {
            plp.setSuccessTerminationCondition((Condition)parseConditions((Element)currentNode).get(0));
        }

        currentNode = rootElement.getElementsByTagName("failure_termination_conditions").item(0);
        List condDistList;
        if (currentNode != null && currentNode.getNodeType() == 1) {
            condDistList = parseConditions((Element)currentNode);
            for (int i = 0; i < condDistList.size(); i++) {
                plp.addFailureTerminationConditions((Condition)condDistList.get(i));
            }
            //condDistList.forEach(plp::addFailureTerminationConditions);
        }

        currentNode = rootElement.getElementsByTagName("success_probability").item(0);
        if (currentNode != null && currentNode.getNodeType() == 1) {
            condDistList = parseProb((Element)currentNode);
            for (int i = 0; i < condDistList.size(); i++) {
                plp.addSuccessProb((ConditionalProb)condDistList.get(i));
            }
            //condDistList.forEach(plp::addSuccessProb);
        }

        currentNode = rootElement.getElementsByTagName("failure_modes").item(0);
        if (currentNode != null && currentNode.getNodeType() == 1) {
            LoadFailureModes((Element)currentNode, plp);
        }

        currentNode = rootElement.getElementsByTagName("general_failure_probability").item(0);
        if (currentNode != null && currentNode.getNodeType() == 1) {
            condDistList = parseProb((Element)currentNode);
            for (int i = 0; i < condDistList.size(); i++) {
                plp.addGeneralFailureProb((ConditionalProb)condDistList.get(i));
            }

            //condDistList.forEach(plp::addGeneralFailureProb);
        }

        currentNode = rootElement.getElementsByTagName("runtime_given_success").item(0);
        if (currentNode != null && currentNode.getNodeType() == 1) {
            condDistList = parseRuntime((Element)currentNode);
            for (int i = 0; i < condDistList.size(); i++) {
                plp.addSuccessRuntime((ConditionalDist)condDistList.get(i));
            }
            //condDistList.forEach(plp::addSuccessRuntime);
        }

        currentNode = rootElement.getElementsByTagName("runtime_given_failure").item(0);
        if (currentNode != null && currentNode.getNodeType() == 1) {
            condDistList = parseRuntime((Element)currentNode);
            for (int i = 0; i < condDistList.size(); i++) {
                plp.addFailureRuntime((ConditionalDist)condDistList.get(i));
            }
            //condDistList.forEach(plp::addFailureRuntime);
        }

    }

    private static void LoadObserveFields(Element rootElement, ObservePLP plp) {
        boolean goalCondition = rootElement.getElementsByTagName("observation_goal_condition").getLength() > 0;
        Node currentNode;
        List condDistList;
        if (goalCondition) {
            currentNode = rootElement.getElementsByTagName("observation_goal_condition").item(0);
            if (currentNode != null && currentNode.getNodeType() == 1) {
                plp.setGoal((ObservationGoal)parseConditions((Element)currentNode).get(0));
                plp.setResultParameterName(((Element)currentNode).getAttribute("result_parameter_name"));
            }

            currentNode = rootElement.getElementsByTagName("correct_condition_observation_probability").item(0);
            if (currentNode != null && currentNode.getNodeType() == 1) {
                condDistList = parseProb((Element)currentNode);
                for (int i = 0; i < condDistList.size(); i++) {
                    plp.addCorrectObservationProb((ConditionalProb)condDistList.get(i));
                }
                //condDistList.forEach(plp::addCorrectObservationProb);
            }
        } else {
            currentNode = rootElement.getElementsByTagName("observation_goal_parameter").item(0);
            if (currentNode != null && currentNode.getNodeType() == 1) {
                LoadObservationGoalParameter(plp, (Element)currentNode);
            }

            currentNode = rootElement.getElementsByTagName("correct_param_observation_probability").item(0);
            if (currentNode != null && currentNode.getNodeType() == 1) {
                LoadCorrectObservationContinuous(plp, (Element)currentNode);
            }
        }

        currentNode = rootElement.getElementsByTagName("failure_to_observe_probability").item(0);
        if (currentNode != null && currentNode.getNodeType() == 1) {
            condDistList = parseProb((Element)currentNode);
            for (int i = 0; i < condDistList.size(); i++) {
                plp.addFailureToObserveProb((ConditionalProb)condDistList.get(i));
            }
            //condDistList.forEach(plp::addFailureToObserveProb);
        }

        currentNode = rootElement.getElementsByTagName("failure_termination_condition").item(0);
        if (currentNode != null && currentNode.getNodeType() == 1) {
            condDistList = parseConditions((Element)currentNode);
            plp.setFailTerminationCond((Condition)condDistList.get(0));
        }

        currentNode = rootElement.getElementsByTagName("runtime_given_success").item(0);
        if (currentNode != null && currentNode.getNodeType() == 1) {
            condDistList = parseRuntime((Element)currentNode);
            for (int i = 0; i < condDistList.size(); i++) {
                plp.addSuccessRuntime((ConditionalDist) condDistList.get(i));
            }
            //condDistList.forEach(plp::addSuccessRuntime);
        }

        currentNode = rootElement.getElementsByTagName("runtime_given_failure").item(0);
        if (currentNode != null && currentNode.getNodeType() == 1) {
            condDistList = parseRuntime((Element)currentNode);
            for (int i = 0; i < condDistList.size(); i++) {
                plp.addFailureRuntime((ConditionalDist) condDistList.get(i));
            }
            //condDistList.forEach(plp::addFailureRuntime);
        }

    }

    private static void LoadCorrectObservationContinuous(ObservePLP plp, Element currentNode) {
        NodeList probs = currentNode.getElementsByTagName("probability_given_observed_value");
        if (probs.getLength() > 0) {
            List<ConditionalProb> condProbList = parseProb((Element)probs.item(0));
            condProbList.forEach(plp::addCorrectObservationProb);
        }

        NodeList intervals = currentNode.getElementsByTagName("confidence_interval");
        if (intervals.getLength() > 0) {
            Element lowerBoundElement = (Element)((Element)intervals.item(0)).getElementsByTagName("lower_bound").item(0);
            Element upperBoundElement = (Element)((Element)intervals.item(0)).getElementsByTagName("upper_bound").item(0);
            Range range = new Range(lowerBoundElement.getAttribute("value"), Boolean.parseBoolean(lowerBoundElement.getAttribute("inclusive")), upperBoundElement.getAttribute("value"), Boolean.parseBoolean(upperBoundElement.getAttribute("inclusive")));
            Element conLevelElement = (Element)currentNode.getElementsByTagName("confidence_level").item(0);
            plp.setCorrectObservationConfidence(new ConfidenceInterval(range, Double.parseDouble(conLevelElement.getAttribute("value"))));
        }

    }

    private static void LoadObservationGoalParameter(ObservePLP plp, Element currentNode) {
        Element paramElement = (Element)currentNode.getElementsByTagName("param").item(0);
        PLPParameter plpParam = new PLPParameter(paramElement.getAttribute("name"));
        NodeList paramFields = paramElement.getElementsByTagName("field");

        for(int j = 0; j < paramFields.getLength(); ++j) {
            if (paramFields.item(j).getNodeType() == 1) {
                plpParam.addParamFieldValue(((Element)paramFields.item(j)).getAttribute("value"));
            }
        }

        plp.setGoal(plpParam);
    }

    private static void LoadAchieveFields(Element rootElement, AchievePLP plp) {
        Node currentNode = rootElement.getElementsByTagName("achievement_goal").item(0);
        List condDistList;
        if (currentNode != null && currentNode.getNodeType() == 1) {
            condDistList = parseConditions((Element)currentNode);
            plp.setGoal((Condition)condDistList.get(0));
        }

        currentNode = rootElement.getElementsByTagName("success_termination_condition").item(0);
        if (currentNode != null && currentNode.getNodeType() == 1) {
            condDistList = parseConditions((Element)currentNode);
            plp.setSuccessTerminationCond((Condition)condDistList.get(0));
        }

        currentNode = rootElement.getElementsByTagName("success_probability").item(0);
        if (currentNode != null && currentNode.getNodeType() == 1) {
            condDistList = parseProb((Element)currentNode);
            for (int i = 0; i < condDistList.size(); i++) {
                plp.addSuccessProb((ConditionalProb) condDistList.get(i));
            }
           // condDistList.forEach(plp::addSuccessProb);
        }

        currentNode = rootElement.getElementsByTagName("failure_modes").item(0);
        if (currentNode != null && currentNode.getNodeType() == 1) {
            LoadFailureModes((Element)currentNode, plp);
        }

        currentNode = rootElement.getElementsByTagName("general_failure_probability").item(0);
        if (currentNode != null && currentNode.getNodeType() == 1) {
            condDistList = parseProb((Element)currentNode);
            for (int i = 0; i < condDistList.size(); i++) {
                plp.addGeneralFailureProb((ConditionalProb) condDistList.get(i));
            }
            //condDistList.forEach(plp::addGeneralFailureProb);
        }

        currentNode = rootElement.getElementsByTagName("failure_termination_condition").item(0);
        if (currentNode != null && currentNode.getNodeType() == 1) {
            condDistList = parseConditions((Element)currentNode);
            plp.setFailTerminationCond((Condition)condDistList.get(0));
        }

        currentNode = rootElement.getElementsByTagName("runtime_given_success").item(0);
        if (currentNode != null && currentNode.getNodeType() == 1) {
            condDistList = parseRuntime((Element)currentNode);
            for (int i = 0; i < condDistList.size(); i++) {
                plp.addSuccessRuntime((ConditionalDist) condDistList.get(i));
            }
            //condDistList.forEach(plp::addSuccessRuntime);
        }

        currentNode = rootElement.getElementsByTagName("runtime_given_failure").item(0);
        if (currentNode != null && currentNode.getNodeType() == 1) {
            condDistList = parseRuntime((Element)currentNode);
            for (int i = 0; i < condDistList.size(); i++) {
                plp.addFailureRuntime((ConditionalDist) condDistList.get(i));
            }
            //condDistList.forEach(plp::addFailureRuntime);
        }

    }

    private static List<ConditionalDist> parseRuntime(Element rootNode) {
        List<ConditionalDist> result = new LinkedList();
        NodeList childs = rootNode.getChildNodes();

        for(int j = 0; j < childs.getLength(); ++j) {
            if (childs.item(j).getNodeType() == 1) {
                if (childs.item(j).getNodeName().equals("conditional_distribution")) {
                    Element condDistElement = (Element)childs.item(j);
                    List<Condition> innerConditions = parseConditions(condDistElement);
                    Element distElement = (Element)condDistElement.getElementsByTagName("distribution").item(0);
                    Distribution dist = parseDistribution(distElement);
                    result.add(new ConditionalDist(dist, (Condition)innerConditions.get(0)));
                } else if (childs.item(j).getNodeName().equals("distribution")) {
                    Distribution dist = parseDistribution((Element)childs.item(j));
                    result.add(new ConditionalDist(dist, (Condition)null));
                }
            }
        }

        return result;
    }

    private static Distribution parseDistribution(Element distElement) {
        NodeList childNodes = distElement.getElementsByTagName("uniform");
        Element unkownElement;
        String lambda;
        String alpha;
        if (childNodes.getLength() > 0) {
            unkownElement = (Element)childNodes.item(0);
            lambda = ((Element)unkownElement.getElementsByTagName("lower_bound").item(0)).getAttribute("value");
            alpha = ((Element)unkownElement.getElementsByTagName("upper_bound").item(0)).getAttribute("value");
            return new UniformDistribution(lambda, alpha);
        } else {
            childNodes = distElement.getElementsByTagName("normal");
            if (childNodes.getLength() > 0) {
                unkownElement = (Element)childNodes.item(0);
                lambda = ((Element)unkownElement.getElementsByTagName("mean").item(0)).getAttribute("value");
                alpha = ((Element)unkownElement.getElementsByTagName("variance").item(0)).getAttribute("value");
                return new NormalDistribution(lambda, alpha);
            } else {
                childNodes = distElement.getElementsByTagName("gamma");
                if (childNodes.getLength() > 0) {
                    unkownElement = (Element)childNodes.item(0);
                    lambda = ((Element)unkownElement.getElementsByTagName("k-shape").item(0)).getAttribute("value");
                    alpha = ((Element)unkownElement.getElementsByTagName("alpha-shape").item(0)).getAttribute("value");
                    return new GammaDistribution(lambda, alpha);
                } else {
                    childNodes = distElement.getElementsByTagName("exp");
                    if (childNodes.getLength() > 0) {
                        unkownElement = (Element)childNodes.item(0);
                        lambda = ((Element)unkownElement.getElementsByTagName("lambda-rate").item(0)).getAttribute("value");
                        return new ExpDistribution(lambda);
                    } else {
                        childNodes = distElement.getElementsByTagName("other_dist");
                        if (childNodes.getLength() > 0) {
                            unkownElement = (Element)childNodes.item(0);
                            return new UnkownDistribution(unkownElement.getAttribute("description"));
                        } else {
                            throw new UnsupportedOperationException("Can't parse a given distribution " + distElement.toString());
                        }
                    }
                }
            }
        }
    }

    private static void LoadFailureModes(Element failureModesElement, PLP plp) {
        NodeList failureModes = failureModesElement.getElementsByTagName("failure_mode");

        for(int i = 0; i < failureModes.getLength(); ++i) {
            if (failureModes.item(i).getNodeType() == 1) {
                Element failureModeElement = (Element)failureModes.item(i);
                List<Condition> innerCondition = parseConditions(failureModeElement);
                List<ConditionalProb> innerCondProb = parseProb(failureModeElement);
                FailureMode fm = new FailureMode((Condition)innerCondition.get(0));
                Iterator var8 = innerCondProb.iterator();

                while(var8.hasNext()) {
                    ConditionalProb cp = (ConditionalProb)var8.next();
                    fm.addProb(cp);
                }

                if (plp.getClass().isAssignableFrom(AchievePLP.class)) {
                    ((AchievePLP)plp).addFailureMode(fm);
                } else if (plp.getClass().isAssignableFrom(MaintainPLP.class)) {
                    ((MaintainPLP)plp).addFailureMode(fm);
                }
            }
        }

    }

    private static List<ConditionalProb> parseProb(Element rootNode) {
        List<ConditionalProb> result = new LinkedList();
        NodeList childs = rootNode.getChildNodes();

        for(int j = 0; j < childs.getLength(); ++j) {
            if (childs.item(j).getNodeType() == 1) {
                if (childs.item(j).getNodeName().equals("conditional_probability")) {
                    Element condProbElement = (Element)childs.item(j);
                    List<Condition> innerConditions = parseConditions(condProbElement);
                    Element probElement = (Element)condProbElement.getElementsByTagName("probability").item(0);
                    String prob = probElement.getAttribute("value");
                    result.add(new ConditionalProb(prob, (Condition)innerConditions.get(0)));
                } else if (childs.item(j).getNodeName().equals("probability")) {
                    String prob = ((Element)childs.item(j)).getAttribute("value");
                    result.add(new ConditionalProb(prob, (Condition)null));
                }
            }
        }

        return result;
    }

    private static void LoadProgressMeasures(Element measuresElement, PLP plp) {
        NodeList measures = measuresElement.getElementsByTagName("progress_measure");

        for(int i = 0; i < measures.getLength(); ++i) {
            if (measures.item(i).getNodeType() == 1) {
                Element pmElement = (Element)measures.item(i);
                String frequency = pmElement.getAttribute("frequency");
                List<Condition> innerCond = parseConditions(pmElement);
                ProgressMeasure pm = new ProgressMeasure(Double.parseDouble(frequency), (Condition)innerCond.get(0));
                plp.addProgressMeasure(pm);
            }
        }

    }

    private static void LoadSideEffects(Element seNode, PLP plp) {
        List<Effect> effects = parseEffects(seNode);
        effects.forEach(plp::addSideEffect);
    }

    private static List<Effect> parseEffects(Element effectsNode) {
        List<Effect> result = new LinkedList();
        NodeList childNodes = effectsNode.getChildNodes();

        for(int i = 0; i < childNodes.getLength(); ++i) {
            if (childNodes.item(i).getNodeType() == 1) {
                Effect c = null;
                String var5 = childNodes.item(i).getNodeName();
                byte var6 = -1;
                switch(var5.hashCode()) {
                    case -1495676803:
                        if (var5.equals("not_effect")) {
                            var6 = 2;
                        }
                        break;
                    case -1092364950:
                        if (var5.equals("conditional_effect")) {
                            var6 = 0;
                        }
                        break;
                    case -976813799:
                        if (var5.equals("predicate_effect")) {
                            var6 = 3;
                        }
                        break;
                    case -279233672:
                        if (var5.equals("forall_effect")) {
                            var6 = 1;
                        }
                        break;
                    case 215572291:
                        if (var5.equals("assignment_effect")) {
                            var6 = 4;
                        }
                }

                switch(var6) {
                    case 0:
                        c = parseConditionalEffect((Element)childNodes.item(i));
                        break;
                    case 1:
                        c = parseForallEffect((Element)childNodes.item(i));
                        break;
                    case 2:
                        c = parseNotEffect((Element)childNodes.item(i));
                        break;
                    case 3:
                        c = parsePredicate((Element)childNodes.item(i));
                        break;
                    case 4:
                        c = parseAssignmentEffect((Element)childNodes.item(i));
                }

                if (c != null) {
                    result.add(c);
                }
            }
        }

        return result;
    }

    private static Effect parseAssignmentEffect(Element assignmentElement) {
        Element paramElement = (Element)assignmentElement.getElementsByTagName("param").item(0);
        Element expressionElement = (Element)assignmentElement.getElementsByTagName("expression").item(0);
        PLPParameter plpParam = new PLPParameter(paramElement.getAttribute("name"));
        NodeList paramFields = paramElement.getElementsByTagName("field");

        for(int j = 0; j < paramFields.getLength(); ++j) {
            if (paramFields.item(j).getNodeType() == 1) {
                plpParam.addParamFieldValue(((Element)paramFields.item(j)).getAttribute("value"));
            }
        }

        AssignmentEffect ae = new AssignmentEffect(plpParam, expressionElement.getAttribute("value"));
        ae.setDescription(assignmentElement.getAttribute("key_description"));
        return ae;
    }

    private static Effect parseNotEffect(Element nEffectElement) {
        Effect predEff = parsePredicate((Element)nEffectElement.getElementsByTagName("predicate_effect").item(0));
        return new NotEffect((Predicate)predEff);
    }

    private static Effect parseForallEffect(Element feEffectElement) {
        List<Effect> innerEffects = parseEffects(feEffectElement);
        ForAllEffect faEff = new ForAllEffect((Effect)innerEffects.get(0));
        NodeList children = feEffectElement.getChildNodes();

        for(int i = 0; i < children.getLength(); ++i) {
            if (children.item(i).getNodeType() == 1) {
                Element childElement = (Element)children.item(i);
                if (childElement.getNodeName().equals("param")) {
                    faEff.addParam(childElement.getAttribute("name"));
                }
            }
        }

        return faEff;
    }

    private static Effect parseConditionalEffect(Element condEffectElement) {
        List<Effect> innerEffects = parseEffects(condEffectElement);
        List<Condition> innerConditions = parseConditions(condEffectElement);
        return new ConditionalEffect((Condition)innerConditions.get(0), (Effect)innerEffects.get(0));
    }

    private static void LoadConcurrentModules(Element conModulesElement, PLP plp) {
        NodeList childs = conModulesElement.getElementsByTagName("complete_mutex");
        if (childs.getLength() > 0) {
            plp.setCompletelyMutex();
        } else {
            childs = conModulesElement.getElementsByTagName("module");

            for(int i = 0; i < childs.getLength(); ++i) {
                if (childs.item(i).getNodeType() == 1) {
                    String var5 = ((Element)childs.item(i)).getAttribute("concurrency_type");
                    byte var6 = -1;
                    switch(var5.hashCode()) {
                        case 104264063:
                            if (var5.equals("mutex")) {
                                var6 = 0;
                            }
                        default:
                            ConcurrencyType type;
                            switch(var6) {
                                case 0:
                                    type = ConcurrencyType.Mutex;
                                    break;
                                default:
                                    type = ConcurrencyType.Parallel;
                            }

                            ModuleRestriction rest = new ModuleRestriction(((Element)childs.item(i)).getAttribute("name"), type);
                            plp.addModuleRestriction(rest);
                    }
                }
            }
        }

    }

    private static void LoadConcurrencyConditions(Element concurrencyConditionsNode, PLP plp) {
        List<Condition> conditions = parseConditions(concurrencyConditionsNode);
        conditions.forEach(plp::addConcurrencyCondition);
    }

    private static void LoadPreconditions(Element preConditionsNode, PLP plp) {
        List<Condition> conditions = parseConditions(preConditionsNode);
        conditions.forEach(plp::addPreCondition);
    }

    private static List<Condition> parseConditions(Element conditionsNode) {
        List<Condition> result = new LinkedList();
        NodeList childNodes = conditionsNode.getChildNodes();

        for(int i = 0; i < childNodes.getLength(); ++i) {
            if (childNodes.item(i).getNodeType() == 1) {
                Condition c = null;
                String var5 = childNodes.item(i).getNodeName();
                byte var6 = -1;
                System.out.println(var5.hashCode());
                switch(var5.hashCode()) {
                    case -1249011565:
                        if (var5.equals("predicate_condition")) {
                            var6 = 0;
                        }
                        break;
                    case -1184917256:
                        if (var5.equals("exists_condition")) {
                            var6 = 4;
                        }
                        break;
                    case -1109465425:
                        if (var5.equals("not_condition")) {
                            var6 = 2;
                        }
                        break;
                    case 2531:
                        if (var5.equals("OR")) {
                            var6 = 6;
                        }
                        break;
                    case 64951:
                        if (var5.equals("AND")) {
                            var6 = 5;
                        }
                        break;
                    case 1081586050:
                        if (var5.equals("formula_condition")) {
                            var6 = 1;
                        }
                        break;
                    case 1308773844:
                        if (var5.equals("forall_condition")) {
                            var6 = 3;
                        }
                    case 728410161:
                        if (var5.equals("measures_condition")) {
                            var6 = 7;
                        }
                }

                switch(var6) {
                    case 0:
                        c = parsePredicate((Element)childNodes.item(i));
                        break;
                    case 1:
                        c = parseFormula((Element)childNodes.item(i));
                        break;
                    case 2:
                        c = parseNotCondition((Element)childNodes.item(i));
                        break;
                    case 3:
                        c = parseQuantifiedCondition((Element)childNodes.item(i), Quantifier.FORALL);
                        break;
                    case 4:
                        c = parseQuantifiedCondition((Element)childNodes.item(i), Quantifier.EXISTS);
                        break;
                    case 5:
                        c = parseBitwiseCondition((Element)childNodes.item(i), Operation.AND);
                        break;
                    case 6:
                        c = parseBitwiseCondition((Element)childNodes.item(i), Operation.OR);
                        break;
                    case 7:
                        c = parseMeasure((Element)childNodes.item(i));
                }

                if (c != null) {
                    result.add(c);
                }
            }
        }

        return result;
    }

    private static Condition parseBitwiseCondition(Element bwElement, Operation op) {
        List<Condition> innerConditions = parseConditions(bwElement);
        BitwiseOperation bwCond = new BitwiseOperation(op);
        innerConditions.forEach(bwCond::addCondition);
        return bwCond;
    }

    private static Condition parseQuantifiedCondition(Element faElement, Quantifier q) {
        List<Condition> innerConditions = parseConditions(faElement);
        QuantifiedCondition qCond = new QuantifiedCondition((Condition)innerConditions.get(0), q);
        NodeList children = faElement.getChildNodes();

        for(int i = 0; i < children.getLength(); ++i) {
            if (children.item(i).getNodeType() == 1) {
                Element childElement = (Element)children.item(i);
                if (childElement.getNodeName().equals("param")) {
                    qCond.addParam(childElement.getAttribute("name"));
                }
            }
        }

        return qCond;
    }

    private static Condition parseNotCondition(Element notElement) {
        List<Condition> innerConditions = parseConditions(notElement);
        return new NotCondition((Condition)innerConditions.get(0));
    }

    private static Condition parseFormula(Element formulaElement) {
        NodeList expressions = formulaElement.getElementsByTagName("expression");
        NodeList ranges = formulaElement.getElementsByTagName("inside_range");
        String leftExp = ((Element)expressions.item(0)).getAttribute("value");
        Formula f;
        if (ranges.getLength() > 0) {
            Range range = (Range)parseRangesList((Element)ranges.item(0), "range").get(0);
            f = new Formula(leftExp, range);
        } else {
            String rightExp = ((Element)expressions.item(1)).getAttribute("value");
            NodeList operators = formulaElement.getElementsByTagName("operator");
            String operator = ((Element)operators.item(0)).getAttribute("type");
            operator = operator.replace("less", "<");
            operator = operator.replace("greater", ">");
            operator = operator.replace("less_equal", "<=");
            operator = operator.replace("greater_equal", ">=");
            f = new Formula(leftExp, rightExp, operator);
        }

        f.setDescription(formulaElement.getAttribute("key_description"));
        return f;
    }

    private static Condition parseMeasure(Element formulaElement) {
        NodeList expressions = formulaElement.getElementsByTagName("expression");
        NodeList ranges = formulaElement.getElementsByTagName("inside_range");
        String leftExp = ((Element)expressions.item(0)).getAttribute("value");
        Formula f;
        if (ranges.getLength() > 0) {
            Range range = (Range)parseRangesList((Element)ranges.item(0), "range").get(0);
            f = new Formula(leftExp, range);
        } else {
            String rightExp = ((Element)expressions.item(1)).getAttribute("value");
            NodeList operators = formulaElement.getElementsByTagName("operator");
            String operator = ((Element)operators.item(0)).getAttribute("type");
            operator = operator.replace("less", "<");
            operator = operator.replace("greater", ">");
            operator = operator.replace("less_equal", "<=");
            operator = operator.replace("greater_equal", ">=");
            f = new Formula(leftExp, rightExp, operator);
        }

        f.setDescription(formulaElement.getAttribute("key_description"));
        return f;
    }

    private static Predicate parsePredicate(Element predElement) {
        Predicate result = new Predicate(predElement.getAttribute("name"));
        NodeList fields = predElement.getElementsByTagName("field");

        for(int j = 0; j < fields.getLength(); ++j) {
            if (fields.item(j).getNodeType() == 1) {
                result.addValue(((Element)fields.item(j)).getAttribute("value"));
            }
        }

        return result;
    }

    private static void LoadRequiredResources(Element resourcesNode, PLP plp) {
        NodeList resources = resourcesNode.getElementsByTagName("resource");

        for(int i = 0; i < resources.getLength(); ++i) {
            if (resources.item(i).getNodeType() == 1) {
                Element currResource = (Element)resources.item(i);
                Element currStatus = (Element)currResource.getElementsByTagName("status").item(0);
                String var7 = currStatus.getAttribute("type");
                byte var8 = -1;
                switch(var7.hashCode()) {
                    case -70023844:
                        if (var7.equals("frequency")) {
                            var8 = 0;
                        }
                }

                RequirementStatus status;
                switch(var8) {
                    case 0:
                        status = RequirementStatus.Frequency;
                        break;
                    default:
                        status = RequirementStatus.Exclusive;
                }

                RequiredResource plpRR = new RequiredResource(currResource.getAttribute("name"), status);

                try {
                    if (currStatus.hasAttribute("frequency")) {
                        plpRR.setFrequency(Double.parseDouble(currStatus.getAttribute("frequency")));
                    }

                    if (currStatus.hasAttribute("duration")) {
                        plpRR.setDuration(Double.parseDouble(currStatus.getAttribute("duration")));
                    }

                    if (currStatus.hasAttribute("quantity")) {
                        plpRR.setQuantity(Double.parseDouble(currStatus.getAttribute("quantity")));
                    }
                } catch (Exception var9) {
                    throw new NumberFormatException("Required Resource " + plpRR.getName() + " has frequency/duration/quantity that is not a real number");
                }

                plp.addRequiredResource(plpRR);
            }
        }

    }

    private static void LoadConstants(Element constantsNode, PLP plp) {
        NodeList constants = constantsNode.getElementsByTagName("constant");

        for(int i = 0; i < constants.getLength(); ++i) {
            if (constants.item(i).getNodeType() == 1) {
                Element currConst = (Element)constants.item(i);
                Constant plpConstant = new Constant(currConst.getAttribute("name"));
                String stringType = currConst.getAttribute("type");
                FieldType type = getFieldTypeFromString(stringType);
                plpConstant.setType(type);
                String value = currConst.getAttribute("value");
                if (!value.equals("")) {
                    plpConstant.setValue(value);
                }

                plp.addConstant(plpConstant);
            }
        }

    }

    private static void LoadVariables(Element variablesNode, PLP plp) {
        NodeList varList = variablesNode.getElementsByTagName("var");

        for(int i = 0; i < varList.getLength(); ++i) {
            if (varList.item(i).getNodeType() == 1) {
                Element currVar = (Element)varList.item(i);
                String stringType = currVar.getAttribute("type");
                FieldType type = getFieldTypeFromString(stringType);
                Variable plpVar = new Variable(currVar.getAttribute("name"), type);
                List<Range> ranges = parseRangesList(currVar, "possible_range");
                Iterator var9 = ranges.iterator();

                while(var9.hasNext()) {
                    Range range = (Range)var9.next();
                    plpVar.addRange(range);
                }

                NodeList possible_values = currVar.getElementsByTagName("possible_value");

                for(int j = 0; j < possible_values.getLength(); ++j) {
                    if (possible_values.item(j).getNodeType() == 1) {
                        plpVar.addPossibleValue(((Element)possible_values.item(j)).getAttribute("value"));
                    }
                }

                plp.addVariable(plpVar);
            }
        }

    }

    private static List<Range> parseRangesList(Element rootElement, String rangeTagName) {
        List<Range> ranges = new LinkedList();
        NodeList possible_ranges = rootElement.getElementsByTagName(rangeTagName);

        for(int j = 0; j < possible_ranges.getLength(); ++j) {
            if (possible_ranges.item(j).getNodeType() == 1) {
                Element re = (Element)possible_ranges.item(j);
                Range range = new Range(re.getAttribute("min_value"), Boolean.parseBoolean(re.getAttribute("min_inclusive")), re.getAttribute("max_value"), Boolean.parseBoolean(re.getAttribute("max_inclusive")));
                ranges.add(range);
            }
        }

        return ranges;
    }

    private static FieldType getFieldTypeFromString(String stringType) {
        byte var3 = -1;
        switch(stringType.hashCode()) {
            case -891985903:
                if (stringType.equals("string")) {
                    var3 = 3;
                }
                break;
            case 3496350:
                if (stringType.equals("real")) {
                    var3 = 1;
                }
                break;
            case 64711720:
                if (stringType.equals("boolean")) {
                    var3 = 0;
                }
                break;
            case 1958052158:
                if (stringType.equals("integer")) {
                    var3 = 2;
                }
        }

        FieldType type;
        switch(var3) {
            case 0:
                type = FieldType.Boolean;
                break;
            case 1:
                type = FieldType.Real;
                break;
            case 2:
                type = FieldType.Integer;
                break;
            case 3:
                type = FieldType.String;
                break;
            default:
                type = FieldType.OtherType;
        }

        return type;
    }

    private static void LoadParameters(Element parametersNode, PLP plp) {
        String[] parameterTypes = new String[]{"execution_parameters", "input_parameters", "output_parameters", "non_observable"};
        String[] var3 = parameterTypes;
        int var4 = parameterTypes.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            String currType = var3[var5];
            Node currentNode = parametersNode.getElementsByTagName(currType).item(0);
            if (currentNode != null && currentNode.getNodeType() == 1) {
                NodeList paramList = ((Element)currentNode).getElementsByTagName("param");

                for(int i = 0; i < paramList.getLength(); ++i) {
                    if (paramList.item(i).getNodeType() == 1) {
                        Element currParam = (Element)paramList.item(i);
                        PLPParameter plpParam = new PLPParameter(currParam.getAttribute("name"));
                        String error_param = currParam.getAttribute("error_param");
                        String freq = currParam.getAttribute("read_frequency");
                        if (!error_param.equals("")) {
                            plpParam.setErrorParam(error_param);
                        }

                        if (!freq.equals("")) {
                            try {
                                plpParam.setReadFrequency(Double.parseDouble(freq));
                            } catch (Exception var17) {
                                throw new NumberFormatException("Parameter read frequency: " + freq + " is not a real number");
                            }
                        }

                        NodeList paramFields = currParam.getElementsByTagName("field");

                        for(int j = 0; j < paramFields.getLength(); ++j) {
                            if (paramFields.item(j).getNodeType() == 1) {
                                plpParam.addParamFieldValue(((Element)paramFields.item(j)).getAttribute("value"));
                            }
                        }

                        byte var16 = -1;
                        switch(currType.hashCode()) {
                            case -860168833:
                                if (currType.equals("input_parameters")) {
                                    var16 = 1;
                                }
                                break;
                            case 308260209:
                                if (currType.equals("execution_parameters")) {
                                    var16 = 0;
                                }
                                break;
                            case 1952252904:
                                if (currType.equals("output_parameters")) {
                                    var16 = 2;
                                }
                        }

                        switch(var16) {
                            case 0:
                                plp.addExecParam(plpParam);
                                break;
                            case 1:
                                plp.addInputParam(plpParam);
                                break;
                            case 2:
                                plp.addOutputParam(plpParam);
                                break;
                            default:
                                plp.addUnobservableParam(plpParam);
                        }
                    }
                }
            }
        }

    }
}
