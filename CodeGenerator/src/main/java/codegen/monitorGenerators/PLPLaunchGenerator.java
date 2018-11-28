package codegen.monitorGenerators;
import codegen.common.PythonWriter;

import java.util.List;

public class PLPLaunchGenerator {

    /**
     * Generates code for the Launch file
     * @param plp_names The PLP names will be generated in launch file
     * @return The generated code
     */
    public static String GenerateLaunchFile(List<String> plp_names) {
        PythonWriter generator = new PythonWriter();
        java.util.Collections.sort(plp_names);
        generator.writeLine("\uFEFF<?xml version=\"1.0\"?>");
        generator.writeLine("<launch>");
        generator.writeLine("<node pkg=\"plp_monitors\" type=\"PLP_resources_server.py\" name=\"PLP_resources_server\" args=\"\" output=\"screen\"/>");
        for (String name : plp_names) {
            String names = "<node pkg=\"plp_monitors\" type=\"PLP_" + name + "_ros_harness.py\" name=\"PLP_" + name + "_ros_harness\" args=\"\" output=\"screen\"/>";
            generator.writeLine(names);
        }
        generator.writeLine("</launch>");
        return generator.end();
    }
}
