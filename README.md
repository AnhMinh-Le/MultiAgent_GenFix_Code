# MultiAgent_GenFix_Code

A Java-based multi-agent system designed to assist in code generation, code fixing, project structuring, and interactive chat support based on project descriptions. Built using Eclipse IDE.

## Table of Contents
- [Introduction](#introduction)
- [Components](#components)
  - [GEN CODE](#gen-code)
  - [FIXCODE](#fixcode)
  - [CHATBOT](#chatbot)
  - [STRUCTURE](#structure)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Setup Instructions](#setup-instructions)
- [Usage Examples](#usage-examples)
  - [GEN CODE](#gen-code-usage)
  - [FIXCODE](#fixcode-usage)
  - [CHATBOT](#chatbot-usage)
  - [STRUCTURE](#structure-usage)
- [Contributing](#contributing)
- [License](#license)
- [Contact](#contact)

## Introduction

The MultiAgent_GenFix_Code project is a suite of Java-based tools designed to streamline software development processes. It includes four main components:

- **GEN CODE**: Generates code from UML diagrams or text descriptions.
- **FIXCODE**: Fixes erroneous code provided by the user.
- **CHATBOT**: Provides interactive support by answering questions about a project based on a description text file.
- **STRUCTURE**: Generates a project structure based on a description text file.

## Components

### GEN CODE
- **Purpose**: Generates code from a UML diagram (PNG) or text description.
- **Input**: UML diagram image (PNG) or text file describing the code to be generated.
- **Output**: Generated source code in the specified programming language.

### FIXCODE
- **Purpose**: Fixes erroneous code provided by the user.
- **Input**: Incorrect code snippet or a file containing the faulty code.
- **Output**: Corrected code.

### CHATBOT
- **Purpose**: Provides interactive support by answering questions about a project based on a description text file.
- **Input**: Description text file and user queries.
- **Output**: Answers to user questions regarding the project.

### STRUCTURE
- **Purpose**: Generates a project structure based on a description text file.
- **Input**: Description text file outlining the project requirements.
- **Output**: Directory structure and file organization for the project.

## Getting Started

### Prerequisites
- Java Development Kit (JDK): Version 8 or higher.
- Eclipse IDE: For running and editing the project.

### Setup Instructions

#### Clone the Repository:
```bash
git clone https://github.com/yourusername/MultiAgent_GenFix_Code.git
```

#### Import into Eclipse:
1. Open Eclipse.
2. Go to `File > Import > General > Existing Projects into Workspace`.
3. Select the cloned project directory and click **Finish**.

#### Add Dependencies:
1. Ensure all necessary libraries are included in the build path.
2. Check the `lib` folder for any external JAR files and add them to your project.

## Usage Examples

### GEN CODE Usage

#### From UML Diagram (PNG):
```bash
java GenCode -input uml_diagram.png -output generated_code.java
```

#### From Text Description:
```bash
java GenCode -input description.txt -output generated_code.java
```

### FIXCODE Usage

#### Fixing Code Snippet:
```bash
java FixCode -input faulty_code.java -output fixed_code.java
```

### CHATBOT Usage

#### Interactive Chat Session:
```bash
java ChatBot -input description.txt
```
Interact with the chatbot in the console.

### STRUCTURE Usage

#### Generate Project Structure:
```bash
java Structure -input description.txt -output project_structure
```
This will create a directory named `project_structure` with the project files and folders.

## Contributing
Contributions are welcome! Please read the `CONTRIBUTING.md` file for details on how to contribute to this project.

## License
This project is licensed under the MIT License. See the `LICENSE.md` file for details.

## Additional Information
- **Java Version**: Java 8 or higher.
- **Build Tool**: Eclipse IDE.

Thank you for using MultiAgent_GenFix_Code! We hope this tool enhances your software development workflow. 🚀
