package codegen.monitorGenerators;

import codegen.common.CodeGenerator;
import codegen.common.ParameterGlue;
import codegen.common.PythonWriter;
import modules.PLP;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import plpEtc.FieldType;
import plpFields.Constant;
import plpFields.PLPParameter;
import plpFields.ProgressMeasure;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class PLPHarnessGenerator {

    public static Map<String, ParameterGlue> parameterLocations;
    public static String trigger;

    public static String GeneratePLPHarness(PLP plp, String path) {

        parameterLocations = new HashMap<>();
        trigger = new String();
        PythonWriter generator = new PythonWriter();

        generator.writeLine("#!/usr/bin/env python");
//        generator.writeLine("import rospy");
//        generator.writeLine("import sys");
        generator.writeLine("import logging");
        generator.writeLine("import rosservice");
//        generator.writeLine("from xml.dom import minidom");
        generator.writeLine("from std_msgs.msg import String");

        generator.newLine();
        handleGlueFile(generator, plp, path);

        generator.writeLine(String.format("from %s.msg import PLPMessage", CodeGenerator.packageName));
        generator.writeLine(String.format("from PLP_%s_logic import *",plp.getBaseName()));
        generator.writeLine(String.format("from PLP_%s_classes import *",plp.getBaseName()));
        generator.newLine();
        generator.writeLine(String.format("PLP_TOPIC = \"%s\"",CodeGenerator.outputTopic));
        generator.writeLine(String.format("PLP_TRIGGER = \"%s\"",CodeGenerator.triggerTopic));
        generator.writeLine(String.format("logger = logging.getLogger(\"%s\")",plp.getBaseName()));
        generator.newLine();
        generator.writeLine(String.format("class PLP_%s_ros_harness(object):",plp.getBaseName()));
        generator.indent();
        generator.newLine();
        generateInitFunction(generator,plp);

        if (plp.getRequiredResources().size()>0) {
            generator.writeLine("rospy.wait_for_service('resources_list')");
            generator.writeLine("if \"/resources_list\" in rosservice.get_service_list():");
            generator.indent();
            generator.writeLine(String.format("rospy.loginfo(\"<PLP:%s>: Service resources is up!\")", plp.getBaseName()));
            generator.dendent();
            generator.newLine();
        }
        generator.writeFileContent(PLPHarnessGenerator.class.getResourceAsStream("/HarnessMain.txt"), plp.getBaseName(), plp.getClass().getSimpleName());

        generator.dendent();
        generator.writeLine("def reset_harness_data(self):");
        generator.indent();
        generator.writeLine("self.plp = None");
        generator.writeLine("self.plp_params.callback = None");
        generator.writeLine(String.format("self.plp_params = PLP_%s_parameters()",plp.getBaseName()));
        generator.writeLine("self.triggered = False");
        int counter = 1;
        for (ProgressMeasure pm : plp.getProgressMeasures()) {
            generator.writeLine(String.format("self.timer%d.shutdown()",
                    counter));
            counter++;
        }
        generator.dendent();
        generator.newLine();
        generator.writeLine("def trigger_plp_task(self):");
        generator.indent();
        generator.writeLine("# Creates a PLP and starts the monitoring, if there's no PLP yet.");
        generator.writeLine("# Write to logger that trigger start");
        generator.writeLine(String.format("logger.info('%s: trigger start')",plp.getBaseName()));
        generator.writeLine("# Start timer");
        generator.writeLine("self.plp_params.timer_start = rospy.Time.now().to_sec()");
        generator.writeLine(String.format("rospy.loginfo(\"<PLP:%s> trigger detected, starting \" + \"monitoring\" if self.monitor else \"capturing\")",plp.getBaseName()));
        generator.writeLine(String.format("self.plp = PLP_%s_logic(self.plp_constants, self.plp_params, self)",plp.getBaseName()));
        generator.writeLine("self.plp_params.callback = self.plp");
        if (plp.getRequiredResources().size()>0)
        {
            generator.writeLine("harness.plp.monitor_resources()");
        }
        generator.writeLine("# Progress measures callbacks");
        counter = 1;
        for (ProgressMeasure pm : plp.getProgressMeasures()) {
            generator.writeLine(String.format("self.timer%d = rospy.Timer(rospy.Duration(%s), harness.plp.monitor_progress_%s)",
                    counter,pm.getFrequency(),pm.getCondition().simpleString()));
            counter++;
        }
        generator.writeLine("self.plp.request_estimation()");
        generator.dendent();
        // Capture method
        generator.newLine();
        generator.writeLine("def capture_params(self):");
        generator.indent();
        generator.writeLine("capture_file = open(self.capture_filename, \"w\")");
        for (PLPParameter param : plp.getExecParams()) {
            generateCaptureParameter(generator, param);
        }
        for (PLPParameter param : plp.getInputParams()) {
            generateCaptureParameter(generator, param);
        }
        for (PLPParameter param : plp.getOutputParams()) {
            generateCaptureParameter(generator, param);
        }
        generator.newLine();
        generator.writeLine("capture_file.close()");
        generator.writeLine("rospy.loginfo(\"<PLP:" + plp.getBaseName() +"> captured parameters at trigger time to file '%s'\" % self.capture_filename)");
        generator.dendent();
        generator.newLine();

        generator.writeLine("def trigger_updated(self, msg):");
        generator.indent();
        generator.writeLine("self.plp_params.set_trigger(msg.data)");
        generator.writeLine("self.consider_trigger()");
        generator.dendent();
        generator.newLine();

        for (PLPParameter param : plp.getExecParams()) {
            generateParameterUpdateFunction(generator, param, false, false, true);
        }
        for (PLPParameter param : plp.getInputParams()) {
            generateParameterUpdateFunction(generator, param, false, false, true);
        }
        for (PLPParameter param : plp.getOutputParams()) {
            generateParameterUpdateFunction(generator, param, false, true, true);
        }

        // trigger function
        generator.writeLine("def check_trigger(self):");
        generator.indent();
        generator.writeLine("# The execution parameters are considered the trigger");
        generator.writeLine("# If the trigger includes requirements on other parameters, add them using self.plp_params.<param_name>");
        generator.writeLine("# and uncomment the relevant line in the update functions above");
        generator.writeLine("# You can also use the defined constants using self.plp_constants[<constant_name>]");
        generator.writeLine(String.format("# (All the parameters are defined in PLP_%s_classes.py)",plp.getBaseName()));
        StringBuilder triggerCheck = new StringBuilder();
        triggerCheck.append("return self.plp_params.trigger == \"" + trigger + "\" # or not ");
        if(plp.getExecParams().size()>0) {
            for (PLPParameter param : plp.getExecParams()) {
                triggerCheck.append(String.format("self.plp_params.%s is None or ", param.simpleString()));
            }
            triggerCheck.delete(triggerCheck.length() - 4, triggerCheck.length());
            triggerCheck.append("");
        }
        else
        {
            generator.writeLine("# TODO: decide the trigger for the code module");
        }
        generator.writeLine(triggerCheck.toString());
        generator.dendent();
        generator.newLine();
        generator.newLine();
        generator.dendent();

        // main function
        generator.writeLine("if __name__ == '__main__':");
        generator.indent();
        generator.writeLine("try:");
        generator.indent();
        generator.writeLine(String.format("rospy.loginfo(\"<PLP:%s> node starting\")",plp.getBaseName()));
        generator.writeLine(String.format("harness = PLP_%s_ros_harness()",plp.getBaseName()));
        //generator.writeLine(String.format("rospy.loginfo(\"<PLP:%s> started\")",plp.getBaseName()));
        generator.writeLine("rospy.spin()");
        generator.dendent();
        generator.writeLine("except rospy.ROSInterruptException:");
        generator.indent();
        generator.writeLine("pass");
        generator.dendent();
        generator.dendent();
        return generator.end();
    }

    private static void generateInitFunction(PythonWriter generator, PLP plp) {
        generator.writeLine("def __init__(self):");
        generator.newLine();
        generator.indent();
        generator.writeLine("self.plp_constants = {");
        generator.indent();
        for (Constant constant : plp.getConstants()) {
            String constantLine = String.format("\"%s\": ",constant.getName());
            if (constant.getValue() == null)
                constantLine += "''' TODO: wasn't specified ''',";
            else {
                if (constant.getType().equals(FieldType.String))
                    constantLine += String.format("\"%s\",", constant.getValue());
                else
                    constantLine += constant.getValue() + ",";
            }
            generator.writeLine(constantLine);
        }
        generator.dendent();
        generator.writeLine("}");
        generator.writeLine("# The following method call is for any initialization code you might need");
        generator.writeLine("self.node_setup()");
        generator.newLine();
        generator.writeLine("# Setup internal PLP objects.");
        generator.writeLine("self.plp = None");
        generator.writeLine(String.format("self.plp_params = PLP_%s_parameters()",plp.getBaseName()));
        generator.newLine();
        generator.writeLine("# Defined Logging");
        generator.writeLine(String.format("hdlr = logging.FileHandler('/var/tmp/%s.log')",plp.getBaseName()));
        generator.writeLine("formatter = logging.Formatter('%(asctime)s %(levelname)s %(message)s')");
        generator.writeLine("hdlr.setFormatter(formatter)");
        generator.writeLine("logger.addHandler(hdlr)");
        generator.newLine();
        generator.writeFileContent(PLPHarnessGenerator.class.getResourceAsStream("/HarnessInit.txt"));
        generator.newLine();
        generator.writeLine("self.triggered = False");
        generator.newLine();
        generator.writeLine("# ROS related stuff");
        generator.writeLine(String.format("rospy.init_node(\"plp_%s\", anonymous=False)",plp.getBaseName()));
        generator.writeLine("self.publisher = rospy.Publisher(PLP_TOPIC, PLPMessage, queue_size=5)");
        generator.writeLine("rospy.Subscriber(PLP_TRIGGER, String, self.trigger_updated)");
        generateAllParamTopics(generator, plp, true);
        generator.newLine();
    }

    public static void generateParameterUpdateFunction(PythonWriter generator, PLPParameter param,
                                                        boolean isTriggerParam, boolean isOutputParam,
                                                        boolean isMonitoring) {
        // Get the field of msg from the glue file
        ParameterGlue paramGlue = parameterLocations.get(param.toString());
        if (paramGlue == null) {
            generator.writeLine("# TODO: Implement update function for parameter: "+param.toString()+". No glue mapping found.");
            generator.newLine();
            return;
            //TODO: throw new RuntimeException("parameter "+param.toString()+" wasn't found in glue file");
        }
        generator.writeLine(String.format("def param_%s_updated(self, msg):",param.simpleString()));
        generator.indent();
        String msgField = paramGlue.getField();
        if (!msgField.isEmpty()) msgField = "." + msgField;

        if (isMonitoring) {
            generator.writeLine(String.format("self.plp_params.set_%s(msg%s)",param.simpleString(),msgField));
            if (isTriggerParam)
                generator.writeLine("self.consider_trigger()");
            else {
                generator.writeLine("# If this parameter effects the trigger for the robotic module, uncomment the following line");
                generator.writeLine("# self.consider_trigger()");
            }
        }
        else { // Middleware code
            generator.writeLine(String.format("self.plp_params.%s = msg%s",param.simpleString(),msgField));
            if (isOutputParam) { // Update the output value to mongodb
                generator.writeLine("# Update the message store with the output value for possible module triggers");
                generator.writeLine(String.format(
                        "self.message_store.update_named(\"output_%1$s\",self.plp_params.%1$s)",param.simpleString()));
                generator.newLine();
            }
            generator.writeLine("self.parameters_updated()");
        }
        generator.dendent();
        generator.newLine();
    }

    private static void generateCaptureParameter(PythonWriter generator, PLPParameter param) {
        generator.writeLine(String.format("capture_file.write(\"Parameter: %s, Value: \")",param.toString()));
        generator.writeLine(String.format("capture_file.write(repr(self.plp_params.%s))",param.simpleString()));
    }


    public static void generateAllParamTopics(PythonWriter generator, PLP plp, boolean isMonitor) {
        if (isMonitor) {
            for (PLPParameter param : plp.getExecParams()) {
                generateParamTopic(generator, param, true);
            }
        }
        for (PLPParameter param : plp.getInputParams()) {
            generateParamTopic(generator, param, isMonitor);
        }
        for (PLPParameter param : plp.getOutputParams()) {
            generateParamTopic(generator, param, true);
        }
    }

    /**
     *
     * @param generator
     * @param param
     * @param noComment If TRUE, write the subscriber as a comment.
     */
    private static void generateParamTopic(PythonWriter generator, PLPParameter param, boolean noComment) {
        ParameterGlue paramGlue = parameterLocations.get(param.toString());
        if (paramGlue == null) {
            generator.writeLine("# No glue mapping for parameter: " + param.toString());
            return;
        }
            //TODO: throw new RuntimeException("parameter "+param.toString()+" wasn't found in glue file");

        // In dispatchers, comment out unless needed by the user
        if (!noComment) generator.writeNoIndent("# ");
        generator.writeLine(String.format("rospy.Subscriber(\"%s\", %s, self.param_%s_updated)",
                paramGlue.getRosTopic(),paramGlue.getMessageType(),param.simpleString()));
    }

    /**
     * Write the required imports into the given PythonWriter and saves all the parameter mapping for later code generation
     * @param generator the PythonWriter that will print the imports
     * @param plp the PLP for which the glue file is handled
     * @param gluePath the path to the glue file
     */
    public static void handleGlueFile(PythonWriter generator, PLP plp, String gluePath) {

        // Complete path with with '/' or '\' according to what to OS uses.
        if (!gluePath.endsWith(CodeGenerator.pathBreak)) { gluePath = gluePath.concat(CodeGenerator.pathBreak); }

        gluePath = gluePath.concat(plp.getGlueFile());

        File glueFile = new File(gluePath);
        if (!glueFile.isFile() || !glueFile.getName().substring(glueFile.getName().lastIndexOf(".") + 1).equalsIgnoreCase("xml")) {
            throw new IllegalArgumentException("path for glue file " + gluePath + " is not a legal xml");
        }
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(glueFile);

            doc.getDocumentElement().normalize();

            Element rootElement = doc.getDocumentElement();
            String nodeNameWithoutNS = rootElement.getNodeName().substring(rootElement.getNodeName().indexOf(':')+1);

            trigger = rootElement.getAttribute("trigger");
            if (!nodeNameWithoutNS.equals("code_generator_glue")) {
                throw new IllegalArgumentException("file " + gluePath + " is not a legal glue file");
            }

            NodeList importNodes = rootElement.getElementsByTagName("import");
            for (int i=0; i<importNodes.getLength(); i++) {
                Element importElement = (Element) importNodes.item(i);
                String pack = importElement.getAttribute("from");
                if (!pack.equals("std_msgs.msg")) {
                    if (pack != null) {
                        generator.write("from " + pack + " ");
                        /// Adding it to the list, to later add it to the CMakeLists and package.xml
                        if (!CodeGenerator.importsForPackage.contains(pack))
                            CodeGenerator.importsForPackage.add(pack);
                    }
                    generator.write("import ");
                    NodeList classesNodes = importElement.getElementsByTagName("python_class");
                    for (int j = 0; j < classesNodes.getLength() - 1; j++) {
                        generator.write((classesNodes.item(j).getTextContent() + ", "));
                    }
                    generator.writeLine((classesNodes.item(classesNodes.getLength() - 1)).getTextContent());
                }
           }

            NodeList parameterNodes = rootElement.getElementsByTagName("parameter_location");
            for (int i=0; i<parameterNodes.getLength(); i++) {
                Element paramLocationElement = (Element) parameterNodes.item(i);
                Element paramElement = (Element) paramLocationElement.getElementsByTagName("param").item(0);
                PLPParameter plpParam = new PLPParameter(paramElement.getAttribute("name"));

                NodeList paramFields = paramElement.getElementsByTagName("field");
                for (int j = 0; j < paramFields.getLength(); j++) {
                    plpParam.addParamFieldValue(((Element) paramFields.item(j)).getAttribute("value"));
                }
                Element topicElement = (Element) paramLocationElement.getElementsByTagName("ROS_topic").item(0);
                NodeList fieldNL = topicElement.getElementsByTagName("field_in_message");
                Element fieldElement = null;
                if (fieldNL.getLength() > 0) {
                    fieldElement = (Element) fieldNL.item(0);
                }
                ParameterGlue paramGlue = new ParameterGlue(plpParam.simpleString(),
                        topicElement.getAttribute("name"),
                        topicElement.getAttribute("message_type"),
                        fieldElement == null ? "" : fieldElement.getAttribute("field_name"),
                        fieldElement == null ? "" : fieldElement.getAttribute("field_type"));
                parameterLocations.put(plpParam.toString(), paramGlue);
            }

            /*for (PLPParameter param : plp.getExecParams())
                if (!parameterLocations.containsKey(param.toString()))
                    throw new RuntimeException("glue file " + gluePath + " has some parameters missing");
            for (PLPParameter param : plp.getInputParams())
                if (!parameterLocations.containsKey(param.toString()))
                    throw new RuntimeException("glue file " + gluePath + " has some parameters missing");
            for (PLPParameter param : plp.getOutputParams())
                if (!parameterLocations.containsKey(param.toString()))
                    throw new RuntimeException("glue file " + gluePath + " has some parameters missing");*/


        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
