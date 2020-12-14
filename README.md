# LambdaNetCLITool
A CLI tool in Java that runs inference using published pre-trained LambdaNet model \w user-defined types. The tool receives a path to the pre-trained model, a path to the folder containing parsingFromFile.js (parsingFromFile.ts, pre-compiled with tsc) and all its dependencies (parsing.ts, etc.), and an input Typescript project folder as named CLI arguments and print the results to stdout. The output includes a specific line of code, a place in that line where the type suggestion should be added, Top5 most probable type suggestions.  

## How to use?    
1. Download the zip file containing all the required JARs from here:  
2. Unzip the zip file at any location (let's say 'lib' folder in the current directory)  
3. Download Driver.java from this repository and compile it using using the following command:  
`javac -cp lib\* Driver.java`  
4. Run the code using the following command:  
`java -cp lib\*;. Driver dir1 dir2 dir3`  
Here,  
dir1 = Path to the model, i.e. ~\models\newParsing-GAT1-fc2-newSim-decay-6 in the folder extracted from models.zip which can be downloaded from:   https://drive.google.com/file/d/1NvEVQ4-5tC3Nc-Mzpu3vYeyEcaM_zEgV/view?usp=sharing (ref: https://github.com/MrVPlusOne/LambdaNet)  
dir2 = Path to the scripts, i.e. a folder containing these: (https://github.com/MrVPlusOne/LambdaNet/tree/master/scripts/ts) files, don't forget to pre-compile parsingFromFile.ts to parsingFromFile.js using tsc command, if not already compiled.  
dir3 = the TypeScript project directory, where all the TypeScript files, on which inference is to be run, are kept.  
5. You'll get output as shown in the following image  

![Imgur](https://imgur.com/mpCcwDU)

<blockquote class="imgur-embed-pub" lang="en" data-id="mpCcwDU"><a href="https://imgur.com/mpCcwDU">View post on imgur.com</a></blockquote><script async src="//s.imgur.com/min/embed.js" charset="utf-8"></script>
