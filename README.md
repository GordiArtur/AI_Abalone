# AI_Abalone
A project

addPlayHistory Branch
 - Add label displaying last played move.
 - AI will display "NOTHING" for now.
 - HelperMethod that takes in a list of hexes and the dx, dy (direction);

# Development Branch

This is the main development branch. Generally here's how git flow works:
1) Create a new branch based off of the development branch (Name it based off of what you are doing, see: Git Conventions)
2) Code whatever you want on your own personal devevopment branch
3) Make a pull request to merge your changes onto the development branch
4) Ask a peer to review the code before merging. (DO NOT MERGE YOUR OWN CODE)

Making the pull request and asking others to do a peer review is a good way to avoid bugs. 


#Git conventions:

(type of branch)/(what is being done)

bug/ general bugfixing

feat/ adding features

junk/ a throwaway branch for doing whatever you want

wip/ something that won't be done for a long time.

#examples:

bug/fix-timers

feat/push-marbles-off

# Build Instructions using IntelliJ IDEA

In IntelliJ, with AI_Abalone project open, go to:
- File -> Project Structure -> Project Settings -> Artifacts

In "Artifacts" window
- Click green plus sign -> Jar -> From modules with dependencies

Make sure a window labeled "Create JAR from Modules" appeared
- Select Module: AI_Abalone
- Select Main Class: Game
- Make sure the option "extract to the target JAR" is selected
- Press OK

Back in "Artifacts" windows
- Make sure the name is AI_Abalone:jar and type is JAR
- Select the desired output directory (AI_Abalone/out/artifacts/AI_Abalone_jar by default)
- Press OK

The above sets the "skeleton" to where the jar will be saved to. To actually build and save it do the following:
- Build -> Build Artifact -> AI_Abalone:jar -> Build

The built JAR file will be located in AI_Abalone/out/artifacts/AI_Abalone_jar/AI_Abalone.jar

