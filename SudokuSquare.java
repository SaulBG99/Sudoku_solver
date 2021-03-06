import java.util.Arrays;

  public class SudokuSquare{
    
    public class Cell{
      boolean[] possibilities= new boolean[9];
      byte def=0;
      Line sq;
      Line vline;
      Line hline;
      boolean instanceFromBeginning=false;
    
      public Cell(byte val){
        Arrays.fill(possibilities, true);
        if(val!=0){
          setDefinite(val);
          instanceFromBeginning=true;
        }
      }
      
      public Cell(boolean[] possibilities, byte def, boolean instanceFromBeginning){ //Only recommended for duplicating
        for(byte l=0;l<9;l++){
          this.possibilities[l]=possibilities[l];
        }
        this.def = def;
        this.instanceFromBeginning = instanceFromBeginning;
      }
      
      void setDefinite(byte val){
        def = val;
        Arrays.fill(possibilities, false);
        possibilities[val-1] = true;
      }
      void setDefiniteSol(byte val){
        setDefinite(val);
        alert();
      }
      void setDefiniteSolAlt(byte val){
        for(byte j=1;j<=9;j++){
          if(j!=val){
            eliminatePossibility(j);
          }
        }
      }
      void undoubtedlySetDefinite(byte val){
        def = val;
        alert();
        for(byte l=1;l<=9;l++){
          if(val!=l && possibilities[l-1]){
            possibilities[l-1] = false;
            sq.lowerNumPos(l);
            vline.lowerNumPos(l);
            hline.lowerNumPos(l);
          }
        }
      }
      void alert(){
        sq.alertElements(def);
        vline.alertElements(def);
        hline.alertElements(def);
      }
      void alertWithPrecaution(){
        alert();
        if(instanceFromBeginning){
          for(byte l=1;l<=9;l++){
            if(l!=def){
              sq.lowerNumPos(l);
              vline.lowerNumPos(l);
              hline.lowerNumPos(l);
            }
          }
        }
      }
      boolean isDefinite(){
        return def!=0;
      }
      
      public boolean hasPossibility(byte val){
        return possibilities[val-1];
      }
      
      void eliminatePossibility(byte val){
        if(!isDefinite() && possibilities[val-1]){
          possibilities[val-1] = false;
          byte value=0;
          byte count=0;
          for(byte x=0; x<9;x++){
            if(possibilities[x]){
              count++;
              value = (byte)(x+1);
            }
          }
          if(count == 1){
            setDefiniteSol(value);
          }
            sq.lowerNumPos(val);
            vline.lowerNumPos(val);
            hline.lowerNumPos(val);
        }
      }
      void setHLine(Line hline){
        this.hline = hline;
      }
      void setVLine(Line vline){
        this.vline = vline; 
      }
      void setSquare(Line sq){
        this.sq = sq;
      }
      Line getHLine(){
        return hline;
      }
      Line getVLine(){
        return vline; 
      }
      Line getSquare(){
        return sq;
      }
      boolean isParent(Line l){
        if(hline == l)
          return true;
        else if(vline == l)
          return true;
        else if(sq == l)
          return true;
        else
          return false;
      }
      Line compare(Line l, Cell c){
        if(l!=hline && c.isParent(hline)){
          return hline;
        }else if(l!=vline && c.isParent(vline)){
          return vline;
        }else if(l!=sq && c.isParent(sq)){
          return sq;
        }else
          return null;
      }
      Cell duplicate(){
        return new Cell(possibilities, def, instanceFromBeginning);
      }
      String printCell(){
        return def!=0? " "+String.valueOf(def)+" ":" - ";
      }
      String printCellWP(int stage){
        if( def!=0){
          return stage==1? " "+String.valueOf(def)+" ": "   ";
        }
        return printPossibility(stage*3+1)+printPossibility(stage*3+2)+printPossibility(stage*3+3);
      }
      String printPossibility(int pos){
        return possibilities[pos-1]? String.valueOf(pos):"X";
      }
  }
  public class Line{
    Cell[] cells= new Cell[9];
    byte id;
    byte counter;
    int[] numPos = new int[9];
    boolean errorState;
    
    public Line(byte id){
      this.id = id;
      counter = 0;
      Arrays.fill(numPos, 9);
      errorState = false;
    }
    public Line( byte id, int[] numPos){ // Only recommended for duplicating
      this.id = id;
      for(byte l=0;l<9;l++){
        this.numPos[l]=numPos[l];
      }
      counter = 0;
      errorState = false;
    }
    public byte getId(){
      return id;
    }
    public int getError(){
      int error=0;
      for(int j=0;j<9;j++){
        error +=Math.pow(numPos[j]-1, 2);
      }
      return error;
    }
    public void alertElements(byte val){
      for(int i=0;i<9;i++){
          cells[i].eliminatePossibility(val);
      }
    }
    public void alertElementsWithExceptions(byte val, Cell[] exceptions){
      for(int i=0;i<9;i++){
        if(!cellContainedIn(i, exceptions)) 
          cells[i].eliminatePossibility(val);
      }
    }
    public boolean cellContainedIn(int num, Cell[] exceptions){
      boolean found = false;
      for(int i=0;i<exceptions.length;i++){
        if(exceptions[i]==cells[num])
          found = true;
      }
      return found;
    }
    public void lowerNumPos(byte val){
      int amount = --numPos[val-1];
      if(amount == 0){
        errorState = true;
      }else if(amount == 1){
        //search that son of a bitch
        for(int i=0;i<9;i++){
          if(cells[i].hasPossibility(val) && !cells[i].isDefinite()){
            cells[i].undoubtedlySetDefinite(val);
          }
        } 
      }else if(amount == 2 || amount ==3){
        //Check if they have a common parent different from self, eliminate possibility in else places on that parent
        int curr = 0;
        Cell[] cellPos = new Cell[amount];
        for(int i=0;i<9;i++){
          if(cells[i].hasPossibility(val) && !cells[i].isDefinite()){
            cellPos[curr++] = cells[i];
          }
        }
        if(curr==amount){
          Line newLine = cellPos[0].compare(this, cellPos[1]);
          if(newLine != null){
            if(amount==3){
              Line newLine2 = cellPos[0].compare(this, cellPos[2]);
              if(newLine == newLine2){
                newLine.alertElementsWithExceptions(val, cellPos);
              }
            }else{
              newLine.alertElementsWithExceptions(val, cellPos);
            }
          }
        }
      }
    }
    public boolean errorState(){
      return errorState;
    }
    //    public void checkFreqPos(){
//      int counter=0
//      byte 
//      for(byte l=1;l<=9;l++){
//          for(int i=0;i<9;i++){
//            
//            if(cells[i].getPos(l)){
//              counter++;
//            }
//          }
//          if(counter==1){
//            cells[i].setDefiniteSol(l)
//          }
//      }
//    }
    
    public void setNext(Cell cell){
      cells[counter] = cell;
      counter++;
    }
    public Line duplicate(){
      return new Line(id, numPos);
    }
    public void printLine(){
      System.out.print("| ");
      for(int i=0;i<9;i++){
          System.out.print(cells[i].printCell());
      }
      System.out.print("| \n");
    }
    public void printLineWP(){
      for(int l=0;l<3;l++){
        for(int i=0;i<9;i++){
          if(i%3==0)
            System.out.print(" | ");
          System.out.print(" "+cells[i].printCellWP(l)+" ");
        }
        System.out.print(" | \n");
      }  
    }
    public void printNumPos(){ //For Debugging
      System.out.print("|");
      for(int i=0;i<9;i++){
          System.out.print(" "+String.valueOf(numPos[i])+" ");
      }
      System.out.print("| \n");
    }
  }
    
    Line[] hlines= new Line[9];
    Line[] vlines= new Line[9];
    Line[] square= new Line[9];
    Cell[][] cells= new Cell[9][9];
    
    public SudokuSquare(byte[][] vals){
      
      for(byte i=0;i<9;i++){
          vlines[i] = new Line(i);
      }
      
      for(byte j=0;j<9;j++){
        hlines[j] = new Line(j);
        for(byte i=0;i<9;i++){
          cells[j][i] = new Cell(vals[j][i]);
          hlines[j].setNext(cells[j][i]);
          cells[j][i].setHLine(hlines[j]);
          vlines[i].setNext(cells[j][i]);
          cells[j][i].setVLine(vlines[i]);
        }
      }
      byte vl, hl;
      for(byte k=0;k<9;k++){
        square[k] = new Line(k);
        vl = (byte)(((byte) k/3)*3+1);
        hl = (byte)(((byte) k%3)*3+1);
        for(byte j=(byte)(vl-1);j<=(byte)(vl+1);j++){
          for(byte i=(byte)(hl-1);i<=(byte)(hl+1);i++){
            square[k].setNext(cells[j][i]);
            cells[j][i].setSquare(square[k]);
          }
        }
      }
    }
    public SudokuSquare( Line[] hlines, Line[] vlines, Line[] square, Cell[][] cells){ //Only recommended for duplicating
      this.hlines = hlines;
      this.vlines = vlines;
      this.square = square;
      this.cells = cells;
      
      for(byte j=0;j<9;j++){
        for(byte i=0;i<9;i++){
          hlines[j].setNext(cells[j][i]);
          cells[j][i].setHLine(hlines[j]);
          vlines[i].setNext(cells[j][i]);
          cells[j][i].setVLine(vlines[i]);
        }
      }
      byte vl, hl;
      for(byte k=0;k<9;k++){
        vl = (byte)(((byte) k/3)*3+1);
        hl = (byte)(((byte) k%3)*3+1);
        for(byte j=(byte)(vl-1);j<=(byte)(vl+1);j++){
          for(byte i=(byte)(hl-1);i<=(byte)(hl+1);i++){
            square[k].setNext(cells[j][i]);
            cells[j][i].setSquare(square[k]);
          }
        }
      }
    }
    public void setCellValue(int j, int i, byte val){
      cells[j][i].setDefiniteSolAlt(val);
    }
    public void solveSudoku(){
      printSudokuWP();
      //All definite restrict the possibilities of their horizontal line, vertial line and square.
      //All self posibilities if unique in self become definite
      //All possibilities if unique in horizontal line, vertial line and square become definite.
      //When there are 2 or 3 possibilities with 2 common horizontal line, vertial line and square reduce possibilities in rest of  horizontal line, vertial line or square
      for(byte j=0;j<9;j++){
        for(byte i=0;i<9;i++){
          if(cells[j][i].isDefinite()){
            cells[j][i].alertWithPrecaution();
          }
        }
      }
      
      if(!finished()){
        printSudokuWP();
        //Create alternative universes checking for errors 
        System.out.println("Entering tree of possibilities");
        solveSudokuAlternates("", "");
      }else{
        System.out.println();
        System.out.println("----------------------------");
        printSudoku();
      }

    }
    public void solveSudokuAlternates(String tree, String treeString){
      int j=-1;
      int i=-1;
      boolean ready = false;
      while(j<8 && !ready){
        j++;
        i=-1;
        while(i<8){
          i++;
          if(!cells[j][i].isDefinite()){
            ready = true;
            break;
          }
        }
      }
      SudokuSquare sudsqr;
      for (byte val=1; val<=9;val++){
        if(cells[j][i].hasPossibility(val)){
          sudsqr = duplicate();
          sudsqr.setCellValue(j, i, val);
          if(!sudsqr.errorState()){
            String treeString2 = treeString+tree+"-Value in ["+String.valueOf(j)+", "+String.valueOf(i)+"] was inferred as "+String.valueOf(val)+" resulting in... \n";
            if(sudsqr.finished()){
              System.out.print(treeString2);
              sudsqr.printSudoku();
            }else{
              sudsqr.solveSudokuAlternates(tree+"-", treeString2);
            }
          }
          
        }
      }
      /*
      SudokuSquare sudsqr1 = duplicate();
      System.out.println("-----First iteration-----");
      sudsqr1.printSudokuWP();
      sudsqr1.setCellValue(0, 2, (byte) 2);
      sudsqr1.printSudokuWP();

      SudokuSquare sudsqr2 = duplicate();
      System.out.println("-----Second iteration-----");
      sudsqr2.printSudokuWP();
      sudsqr2.setCellValue(0, 2, (byte) 5);
      sudsqr2.printSudokuWP();
      */
    }
    public boolean finished(){
      boolean finished=true;
      for(byte j=0;j<9;j++){
        for(byte i=0;i<9;i++){
          if(!cells[j][i].isDefinite()){
            finished = false;
          }
        }
      }
      return finished;
    }
    public boolean errorState(){ //Desired a false which means the square has not an error
      boolean errorState=false;
      for(byte j=0;j<9;j++){
        if(hlines[j].errorState() || vlines[j].errorState() || square[j].errorState()){
          errorState = true;
          break;
        }
      }
      return errorState;
    }
    public SudokuSquare duplicate(){
      Line[] hlinesA= new Line[9];
      Line[] vlinesA= new Line[9];
      Line[] squareA= new Line[9];
      Cell[][] cellsA= new Cell[9][9];
      
      for(byte j=0;j<9;j++){
      hlinesA[j]=hlines[j].duplicate();
      vlinesA[j]=vlines[j].duplicate();
      squareA[j]=square[j].duplicate();
        for(byte i=0;i<9;i++){
          cellsA[j][i]=cells[j][i].duplicate();
        }
      }
      return new SudokuSquare( hlinesA, vlinesA, squareA, cellsA);
    }
    public void printSudoku(){
      for(byte j=0;j<9;j++){
        hlines[j].printLine();
      }
    }
    public void printSudokuWP(){ //With Possibilities
      System.out.print("\n \n \n \n ");
      for(byte j=0;j<9;j++){
        if(j%3==0)
            System.out.println(" -------------------------------------------------------");
        hlines[j].printLineWP();
      }
    }
    public void printDebuggingData(){
      int counter=0;
      for(byte j=0;j<9;j++){
        counter+=hlines[j].getError()+vlines[j].getError()+square[j].getError();
      }
      System.out.println("The error is "+String.valueOf(counter)+"/0 (0 is ideal)");
      /*
      System.out.println("Megadebugging");
      System.out.println("Horizontal Num Pos (ideal=1)");
      for(byte j=0;j<9;j++){
        hlines[j].printNumPos();
      }
      System.out.println("Vertical Num Pos (ideal=1)");
      for(byte j=0;j<9;j++){
        vlines[j].printNumPos();
      }
      System.out.println("Square Num Pos (ideal=1)");
      for(byte j=0;j<9;j++){
        square[j].printNumPos();
      }
      */
    }
  }