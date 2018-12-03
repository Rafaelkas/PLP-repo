package codegen.monitorGenerators;
import codegen.common.PythonWriter;

import java.util.List;
import java.util.Map;

public class PLPResourcesGenerator {

    /**
     * Generates code for the Launch file
     * @param resource_names The PLP names will be generated in launch file
     * @return The generated code
     */
    public static String GenerateResourcesFile(Map<String, List<String>> resource_names) {
        PythonWriter generator = new PythonWriter();
        for (Map.Entry<String, List<String>> entry : resource_names.entrySet())
        {
            for (String names : entry.getValue()) {
                generator.writeLine(names);
            }
        }
        return generator.end();
    }
}
