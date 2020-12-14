import adapter.JavaAdapter;
import lambdanet.TypeInferenceService;
import lambdanet.TypeInferenceService$;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

public class Driver {
    public static void main(String[] args) { //all commented out lines of code can be used to take inputs during the runtime.
        var api = lambdanet.JavaAPI$.MODULE$;
        var typeInfer = TypeInferenceService$.MODULE$;
        var workDir = api.pwd();

//        System.out.println("Enter model path: ");
//        Scanner sc = new Scanner(System.in);
//        String modelPathStr = sc.nextLine();
        String modelPathStr = args[0];
        Path modelPath = Paths.get(modelPathStr);
        ammonite.ops.Path modelDir;

        if(modelPath.isAbsolute()) {
            modelDir = api.absPath(modelPathStr);
        } else {
            modelDir = api.joinPath(workDir, modelPathStr);
        }

//        System.out.println("Enter path to parsingFromFile.js \n(if it is a .ts file, compile it first with tsc command; don't include filename in the path): ");
//        String parsingFromFile = sc.nextLine() + "/parsingFromFile.js";
        String parsingFromFile = args[1] + "/parsingFromFile.js";
        File file = new File(parsingFromFile);
        assert !file.exists() : "Specified path does not exist.";
        Path path = Paths.get(parsingFromFile);
        if(path.isAbsolute()) { // to convert parsingFromFile from absolute to relative
            String base = Paths.get("").toAbsolutePath().toString();
            parsingFromFile = getRelativePath(parsingFromFile, base);
        }

        new JavaAdapter(parsingFromFile);

        var paramPath = api.joinPath(modelDir, "params.serialized");
        var modelCachePath = api.joinPath(modelDir, "model.serialized");
        var modelConfig = api.defaultModelConfig();
        var parsedReposDir = api.joinPath(workDir, "data/parsedRepos");

        var model = typeInfer.loadModel(paramPath, modelCachePath, modelConfig, 8, parsedReposDir);
        var predService = api.predictionService(model, 8, 5);
        System.out.println("\nType Inference Service successfully started.");
        System.out.println("Current working directory: " + workDir);
        long occupiedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.println("Total occupied memory: " + occupiedMemory +" Bytes\n");

//        System.out.println("Enter project path: ");
//        var line = api.readLine();
        String line = args[2];
        try {
            assert !line.strip().isEmpty() : "Specified path should not be empty.";
            Path projectPath = Paths.get(line);
            var sourcePath = projectPath.isAbsolute() ?
                    api.absPath(line) :
                    api.joinPath(workDir, line);
            String[] skipSet = {"node_modules"};
            System.out.println();
            var results =
                    predService.predictOnProject(sourcePath, false, skipSet);
            new TypeInferenceService.PredictionResults(results).prettyPrint();
            System.out.println("DONE");
        } catch (Throwable e) {
            System.out.println("Got exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static String getRelativePath(String path, String base) {

        final String SEP = "/";

        boolean isfile = !path.endsWith(SEP);

        String a = "";
        String b = "";
        try {
            a = new File(base).getCanonicalFile().toURI().getPath();
            b = new File(path).getCanonicalFile().toURI().getPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] basePaths = a.split(SEP);
        String[] otherPaths = b.split(SEP);

        int n = 0;
        for (; n < basePaths.length && n < otherPaths.length; n++) {
            if (basePaths[n].equals(otherPaths[n]) == false)
                break;
        }

        StringBuffer tmp = new StringBuffer("");
        for (int m = n; m < basePaths.length; m++)
            tmp.append(".." + SEP);
        for (int m = n; m < otherPaths.length; m++) {
            tmp.append(otherPaths[m]);
            tmp.append(SEP);
        }

        String result = tmp.toString();

        if (isfile && result.endsWith(SEP)) {
            result = result.substring(0, result.length() - 1);
        }

        return result;
    }
}
