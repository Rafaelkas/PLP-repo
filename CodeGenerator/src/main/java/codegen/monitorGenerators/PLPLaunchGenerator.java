package codegen.monitorGenerators;
import codegen.common.PythonWriter;

import java.util.List;
import java.util.Map;

public class PLPLaunchGenerator {

    /**
     * Generates code for the Launch file
     * @param plp_names The PLP names will be generated in launch file
     * @return The generated code
     */
    public static String GenerateLaunchFile(Map<String, List<String>> plp_names) {
        PythonWriter generator = new PythonWriter();
        generator.writeLine("\uFEFF<?xml version=\"1.0\"?>");
        generator.writeLine("<launch>");
        generator.writeLine("<node pkg=\"plp_monitors\" type=\"PLP_plp_service.py\" name=\"PLP_plp_service\" args=\"\" output=\"screen\"/>");
        generator.writeLine("<node pkg=\"plp_monitors\" type=\"PLP_resources_service.py\" name=\"PLP_resources_service\" args=\"\" output=\"screen\"/>");
        for (Map.Entry<String, List<String>> entry : plp_names.entrySet())
        {
            String names = "<node pkg=\"plp_monitors\" type=\"PLP_" + entry.getKey() + "_ros_harness.py\" name=\"PLP_" + entry.getKey() + "_ros_harness\" args=\"\" output=\"screen\"/>";
            generator.writeLine(names);
        }
        generator.writeLine("</launch>");
        return generator.end();
    }
}
