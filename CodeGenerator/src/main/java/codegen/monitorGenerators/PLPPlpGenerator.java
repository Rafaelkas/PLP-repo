package codegen.monitorGenerators;
import codegen.common.PythonWriter;

import java.util.List;
import java.util.Map;

public class PLPPlpGenerator {

    /**
     * Generates code for the Launch file
     * @param plp_names The PLP names will be generated in launch file
     * @return The generated code
     */
    public static String GeneratePlpFile(Map<String, List<String>> plp_names) {
        PythonWriter generator = new PythonWriter();
        for (Map.Entry<String, List<String>> entry : plp_names.entrySet())
        {
            generator.writeLine(entry.getKey());
        }
        return generator.end();
    }
}
