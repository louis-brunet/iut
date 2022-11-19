# à remplacer par javac #
JCC = '/mnt/d/Program Files/Java/jdk-13.0.1/bin/javac.exe'
JFLAGS = -g -implicit:none
# à remplacer par java #
EXECUTE = '/mnt/d/Program Files/Java/jdk-13.0.1/bin/java.exe' 

### BUT PAR DEFAUT ###
 
but : Main.class

### REGLES ESSENTIELLES ###
 
ButtonPanel.class : ButtonPanel.java \
GameModel.class \
MainFrame.class \
OpenMenuListener.class \
ImportFileListener.class \
MainMenuScreen.java
	${JCC} ${JFLAGS} ButtonPanel.java

Cellule.class : Cellule.java \
ObservableModel.class \
Position.class
	${JCC} ${JFLAGS} Cellule.java

CellulePanel.class : CellulePanel.java \
ObserverView.class \
Cellule.class \
MainMenuScreen.java
	${JCC} ${JFLAGS} CellulePanel.java

CelluleSelectListener.class : CelluleSelectListener.java \
GameModel.class \
Cellule.class \
CellulePanel.class
	${JCC} ${JFLAGS} CelluleSelectListener.java

ColorHoverListener.class : ColorHoverListener.java
	${JCC} ${JFLAGS} ColorHoverListener.java

Construction.class : Construction.java \
GameModel.class \
Grille.class
	${JCC} ${JFLAGS} Construction.java

ConstructionButtonPanel.class : ConstructionButtonPanel.java \
ButtonPanel.class \
ExportFileListener.class \
ConstructionResetListener.class \
Construction.class \
MainFrame.class
	${JCC} ${JFLAGS} ConstructionButtonPanel.java

ConstructionResetListener.class : ConstructionResetListener.java \
Construction.class \
MainFrame.class 
	${JCC} ${JFLAGS} ConstructionResetListener.java

ConstructionScreen.class : ConstructionScreen.java \
GameScreen.class \
Construction.class \
MainFrame.class \
ContentControllerPanel.class \
NumPad.class \
ButtonPanel.class \
ConstructionButtonPanel.class \
SidePanel.class
	${JCC} ${JFLAGS} ConstructionScreen.java

ContentControllerButton.class : ContentControllerButton.java 
	${JCC} ${JFLAGS} ContentControllerButton.java

ContentControllerListener.class : ContentControllerListener.java \
GameModel.class \
ContentControllerPanel.class \
ContentControllerButton.class
	${JCC} ${JFLAGS} ContentControllerListener.java

ContentControllerPanel.class : ContentControllerPanel.java \
GameModel.class \
ContentControllerButton.class \
ContentControllerListener.java
	${JCC} ${JFLAGS} ContentControllerListener.java ContentControllerPanel.java 

CreateConstructionListener.class : CreateConstructionListener.java \
Grille.class \
Construction.class \
ConstructionScreen.class \
MainFrame.class
	${JCC} ${JFLAGS} CreateConstructionListener.java

CreateResolutionListener.class : CreateResolutionListener.java \
Grille.class \
Resolution.class \
ResolutionScreen.class \
MainFrame.class \
GrilleIO.class
	${JCC} ${JFLAGS} CreateResolutionListener.java

ExportFileListener.class : ExportFileListener.java \
GameModel.class \
MainFrame.class \
GrilleIO.class \
Grille.class
	${JCC} ${JFLAGS} ExportFileListener.java

GameModel.class : GameModel.java \
ObservableModel.class \
Grille.class \
Cellule.class \
Position.class
	${JCC} ${JFLAGS} GameModel.java

GameScreen.class : GameScreen.java \
GrillePanel.class \
GameModel.class \
SidePanel.class \
MainFrame.class \
ObserverView.class \
Grille.class \
CelluleSelectListener.class
	${JCC} ${JFLAGS} GameScreen.java

Grille.class : Grille.java \
ObservableModel.class \
Cellule.class \
Position.class
	${JCC} ${JFLAGS} Grille.java

GrilleIO.class : GrilleIO.java \
Grille.class \
Position.class \
Cellule.class
	${JCC} ${JFLAGS} GrilleIO.java

GrillePanel.class : GrillePanel.java \
ObserverView.class \
Grille.class \
CellulePanel.class \
Position.class \
Cellule.class
	${JCC} ${JFLAGS} GrillePanel.java

ImportFileListener.class : ImportFileListener.java \
GameModel.class \
MainFrame.class \
GrilleIO.class \
Grille.class
	${JCC} ${JFLAGS} ImportFileListener.java


IncertitudeButtonListener.class : IncertitudeButtonListener.java \
Resolution.class \
Cellule.class
	${JCC} ${JFLAGS} IncertitudeButtonListener.java


Main.class : Main.java \
MainFrame.class \
MainMenuScreen.class
	${JCC} ${JFLAGS} Main.java


MainFrame.class : MainFrame.java
	${JCC} ${JFLAGS} MainFrame.java

MainMenuScreen.class: MainMenuScreen.java \
MainFrame.class \
ColorHoverListener.class \
CreateConstructionListener.class \
CreateResolutionListener.class
	${JCC} ${JFLAGS} MainMenuScreen.java

NumPad.class : NumPad.java \
ContentControllerPanel.class \
GameModel.class \
NumPadButton.class 
	${JCC} ${JFLAGS} NumPad.java

NumPadButton.class : NumPadButton.java \
ContentControllerButton.class \
CellulePanel.class \
MainMenuScreen.java
	${JCC} ${JFLAGS} NumPadButton.java

ObservableModel.class : ObservableModel.java \
ObserverView.class
	${JCC} ${JFLAGS} ObservableModel.java

ObserverView.class : ObserverView.java
	${JCC} ${JFLAGS} ObserverView.java

OpenMenuListener.class : OpenMenuListener.java \
MainFrame.class \
MainMenuScreen.java # change to .java
	${JCC} ${JFLAGS} OpenMenuListener.java

Position.class : Position.java \
Grille.java # change to .java
	${JCC} ${JFLAGS} Position.java

Resolution.class : Resolution.java \
GameModel.class \
Grille.class \
Cellule.class \
SortByPossibilities.class \
Position.class
	${JCC} ${JFLAGS} Resolution.java

ResolutionButtonPanel.class : ResolutionButtonPanel.java \
Resolution.class \
MainFrame.class \
ResoudreGrilleListener.class \
IncertitudeButtonListener.class \
CellulePanel.class
	${JCC} ${JFLAGS} ResolutionButtonPanel.java

ResolutionScreen.class : ResolutionScreen.java \
Resolution.class \
GameScreen.class \
MainFrame.class \
NumPad.class \
ResolutionButtonPanel.class \
SidePanel.class \
GameModel.class
	${JCC} ${JFLAGS} ResolutionScreen.java

ResoudreGrilleListener.class : ResoudreGrilleListener.java \
Resolution.class \
MainFrame.class
	${JCC} ${JFLAGS} ResoudreGrilleListener.java

SidePanel.class : SidePanel.java \
ContentControllerPanel.class \
ButtonPanel.class \
MainFrame.class # change to .java
	${JCC} ${JFLAGS} SidePanel.java


SortByPossibilities.class : SortByPossibilities.java \
Grille.class \
Cellule.class
	${JCC} ${JFLAGS} SortByPossibilities.java

### REGLES OPTIONNELLES ###

clean :
	rm -f *.class

run : Main.class
	${EXECUTE} Main

### BUTS FACTICES ###

.PHONY : but clean run 
