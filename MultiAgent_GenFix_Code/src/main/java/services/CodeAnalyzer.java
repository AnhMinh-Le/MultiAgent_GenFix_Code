package main.java.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

public class CodeAnalyzer {

    public static Pair<String, String> executeCommand(List<String> command, long timeout) {
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        final AtomicReference<Process> processRef = new AtomicReference<>();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        StringBuilder output = new StringBuilder();
        StringBuilder error = new StringBuilder();

        try {
            Process process = processBuilder.start();
            processRef.set(process);

            Callable<Void> readTask = () -> {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(process.getInputStream()));
                     BufferedReader errorReader = new BufferedReader(
                             new InputStreamReader(process.getErrorStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        output.append(line).append("\n");
                    }
                    while ((line = errorReader.readLine()) != null) {
                        error.append(line).append("\n");
                    }
                }
                return null;
            };

            Future<Void> future = executor.submit(readTask);
            boolean finished = process.waitFor(timeout, TimeUnit.SECONDS);
            if (!finished) {
                process.destroyForcibly();
                return new Pair<>("TIMEOUT", "TIMEOUT");
            }
            future.get();
            return new Pair<>(output.toString().trim(), error.toString().trim());
        } catch (IOException | InterruptedException | ExecutionException e) {
            Process proc = processRef.get();
            if (proc != null) {
                proc.destroyForcibly();
            }
            return new Pair<>("ERROR", e.getMessage());
        } finally {
            executor.shutdownNow();
        }
    }

    public static Pair<String, String> compilePython(String testDir, String name) {
        List<String> command = new ArrayList<>();
        command.add("pylint");
        command.add("D:\\MultiAgent_GenFix_Code\\MultiAgent_GenFix_Code\\src\\main\\resources\\datainputcode.py");

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.directory(new File(testDir));
            Pair<String, String> result = executeCommand(command, 10);
            return result;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return new Pair<>("uncompilable", "uncompilable");
        }
    }

    public static String runPylint(String file) {
        if (file.endsWith(".py")) {
            String testDir = "D:\\MultiAgent_GenFix_Code\\MultiAgent_GenFix_Code\\src\\main\\resources\\data";
            String name = "inputcode";
            Pair<String, String> outErr = compilePython(testDir, name);
            if (outErr.first != null && !outErr.first.equals("uncompilable")) {
                return "PYLINT:\n" + outErr.first;
            }
        }
        return null;
    }

    public static String runFlake8(String file) {
        if (file.endsWith(".py")) {
            List<String> command = new ArrayList<>();
            command.add("flake8");
            command.add(file);
            Pair<String, String> outErr = executeCommand(command, 10);
            if (!outErr.first.isEmpty()) {
                return "FLAKE8:\n" + outErr.first;
            }
        }
        return null;
    }

    public static String runPMD(String file) {
        if (file.endsWith(".java")) {
            List<String> command = new ArrayList<>();
            command.add("pmd.bat");
            command.add("check");
            command.add("-d");
            command.add(file);
            command.add("-R");
            command.add("D:\\MultiAgent_GenFix_Code\\MultiAgent_GenFix_Code\\src\\main\\resources\\checkjava\\rulesets-pmd\\java\\quickstart.xml");
            command.add("-f");
            command.add("text");
            Pair<String, String> outErr = executeCommand(command, 10);
            if (!outErr.first.isEmpty()) {
                return "PMD:\n" + outErr.first + "\n";
            }
        }
        return null;
    }

    public static String runCheckstyle(String file) {
        if (file.endsWith(".java")) {
            List<String> command = new ArrayList<>();
            command.add("java");
            command.add("-jar");
            command.add("D:\\MultiAgent_GenFix_Code\\MultiAgent_GenFix_Code\\src\\main\\resources\\checkjava\\checkstyle\\checkstyle-10.21.1-all.jar");
            command.add("-c");
            command.add("D:\\MultiAgent_GenFix_Code\\MultiAgent_GenFix_Code\\src\\main\\resources\\checkjava\\checkstyle\\sun_checks.xml");
            command.add(file);
            Pair<String, String> outErr = executeCommand(command, 10);
            if (!outErr.first.isEmpty()) {
                return "CHECKSTYLE:\n" + outErr.first + "\n" + outErr.second;
            }
        }
        return null;
    }

    public static String analyzeCode(String codePath) {
        List<String> results = new ArrayList<>();

        String pylintResult = runPylint(codePath);
        if (pylintResult != null) {
            results.add(pylintResult);
        }

        String flake8Result = runFlake8(codePath);
        if (flake8Result != null) {
            results.add(flake8Result);
        }

        String pmdResult = runPMD(codePath);
        if (pmdResult != null) {
            results.add(pmdResult);
        }

        String checkstyleResult = runCheckstyle(codePath);
        if (checkstyleResult != null) {
            results.add(checkstyleResult);
        }

        return String.join("\n", results);
    }

    public static String getCodePath() {
        String dataDir = "D:\\MultiAgent_GenFix_Code\\MultiAgent_GenFix_Code\\src\\main\\resources\\data";
        try {
            File dir = new File(dataDir);
            if (dir.exists() && dir.isDirectory()) {
                File[] files = dir.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (file.getName().equals("inputcode.py")) {
                            return Paths.get(dataDir, "inputcode.py").toString();
                        } else if (file.getName().endsWith(".java")) {
                            return Paths.get(dataDir, file.getName()).toString();
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error accessing code directory: " + e.getMessage());
        }
        return null;
    }

    public static void main(String[] args) {
        String codePath = getCodePath();
        System.out.println(codePath);
        if (codePath == null) {
            System.out.println("No .py or .java file found in the specified directory.");
            return;
        }
        System.out.println(codePath);
        String results = analyzeCode(codePath);
        System.out.println(results);
    }

    public static class Pair<F, S> {
        public final F first;
        public final S second;

        public Pair(F first, S second) {
            this.first = first;
            this.second = second;
        }
    }
}
