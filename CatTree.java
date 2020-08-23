import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;


public class CatTree implements Iterable<CatInfo>{
    public CatNode root;

    public CatTree(CatInfo c) {
        this.root = new CatNode(c);
    }

    private CatTree(CatNode c) {
        this.root = c;
    }


    public void addCat(CatInfo c)
    {
        this.root = root.addCat(new CatNode(c));
    }

    public void removeCat(CatInfo c)
    {
        this.root = root.removeCat(c);
    }

    public int mostSenior()
    {
        return root.mostSenior();
    }

    public int fluffiest() {
        return root.fluffiest();
    }

    public CatInfo fluffiestFromMonth(int month) {
        return root.fluffiestFromMonth(month);
    }

    public int hiredFromMonths(int monthMin, int monthMax) {
        return root.hiredFromMonths(monthMin, monthMax);
    }

    public int[] costPlanning(int nbMonths) {
        return root.costPlanning(nbMonths);
    }



    public Iterator<CatInfo> iterator()
    {
        return new CatTreeIterator();
    }


    class CatNode {

        CatInfo data;
        CatNode senior;
        CatNode same;
        CatNode junior;

        public CatNode(CatInfo data) {
            this.data = data;
            this.senior = null;
            this.same = null;
            this.junior = null;
        }

        public String toString() {
            String result = this.data.toString() + "\n";
            if (this.senior != null) {
                result += "more senior " + this.data.toString() + " :\n";
                result += this.senior.toString();
            }
            if (this.same != null) {
                result += "same seniority " + this.data.toString() + " :\n";
                result += this.same.toString();
            }
            if (this.junior != null) {
                result += "more junior " + this.data.toString() + " :\n";
                result += this.junior.toString();
            }
            return result;
        }


        public CatNode addCat(CatNode c) {
            if(c.data.monthHired < this.data.monthHired){//recursive
                if(this.senior != null){
                    this.senior.addCat(c);
                }
                else{
                    this.senior = c;
                }
            }

            else if(c.data.monthHired > this.data.monthHired){//recursive
                if(this.junior != null){
                    this.junior.addCat(c);
                }
                else{
                    this.junior = c;
                }
            }

            else {  //base case
                if(c.data.furThickness > this.data.furThickness){
                    c.same = this.same;
                    this.same = c;
                    CatInfo temp = this.data;
                    this.data = c.data;
                    c.data = temp;
                }
                else{
                    CatNode Temp = this;
                    CatNode Previous = null;
                    while((c.data.furThickness < Temp.data.furThickness) && (Temp.same != null)){
                        Previous = Temp;
                        Temp = Temp.same;
                    }
                    if(Previous == null){
                        c.same = Temp.same;
                        Temp.same = c;
                    }
                    else{
                        c.same = Previous.same;
                        Previous.same = c;
                    }
                }


            }
            return this; // DON'T FORGET TO MODIFY THE RETURN IF NEED BE
        }


        public CatNode removeCat(CatInfo c) {
            if(c.monthHired == this.data.monthHired){
                if (this.data.equals(c)){    //base case
                    if(this.same != null){
                        this.data = this.same.data;
                        this.same = this.same.same;
                        return this;
                    }
                    else if((this.same == null) && (this.senior != null)){
                        CatNode Temp1 = this.senior.junior;
                        CatNode Temp2 = this.senior;
                        while(Temp2.junior != null){
                            Temp2 = Temp2.junior;
                        }
                        this.data = this.senior.data;
                        this.same = this.senior.same;
                        this.senior = this.senior.senior;
                        Temp2.junior = this.junior;
                        this.junior = Temp1;
                        return this;
                    }
                    else{
                        this.data = this.junior.data;
                        this.same = this.junior.same;
                        this.senior = this.junior.senior;
                        this.junior = this.junior.junior;
                        return this;
                    }
                }
                else{
                    CatNode Temp = this.same;
                    CatNode Previous = null;
                    while(Temp != null){
                        if(Temp.data.equals(c)){
                            if(Previous == null){
                                this.same = Temp.same;
                            }
                            else{
                                Previous.same = Temp.same;
                            }
                        }
                        else{
                            Previous = Temp;
                            Temp = Temp.same;
                        }
                    }
                }
            }
            else if(c.monthHired < this.data.monthHired){
                if(this.senior != null){
                    this.senior.removeCat(c);
                }
                else{
                    return this;
                }
            }
            else if(c.monthHired > this.data.monthHired){
                if(this.junior != null){
                    this.junior.removeCat(c);
                }
                else{
                    return this;
                }
            }
            return this;
             // DON'T FORGET TO MODIFY THE RETURN IF NEED BE
        }


        public int mostSenior() {
            if(this.senior != null){             //recursive
                this.senior.mostSenior();
            }
            else{       //base case
                return this.data.monthHired;
            }
            return -1;   //CHANGE THIS
        }

        public int fluffiest() {
            Integer Fluff = 0;
            Fluff = this.fluffiestHelper(Fluff);
            return Fluff; // DON'T FORGET TO MODIFY THE RETURN IF NEED BE
        }

        private int fluffiestHelper(Integer Fluff){

            if(this.senior != null){
                this.senior.fluffiestHelper(Fluff);
            }

            if(this.data.furThickness > Fluff){
                Fluff = this.data.furThickness;
            }

            if(this.junior != null){
                this.junior.fluffiestHelper(Fluff);
            }

            return Fluff;
        }


        public int hiredFromMonths(int monthMin, int monthMax) {
            Integer Count = 0;
            Count = hiredFromMonthsHelper(Count, monthMin, monthMax);
            return Count; // DON'T FORGET TO MODIFY THE RETURN IF NEED BE

        }

        private int hiredFromMonthsHelper(Integer Count, int monthMin, int monthMax) {

            if (this.senior != null) {
                if (this.senior.data.monthHired >= monthMin) {
                    Count = this.senior.hiredFromMonthsHelper(Count, monthMin, monthMax);
                }
            }
            if ((this.data.monthHired <= monthMax) && (this.data.monthHired >= monthMin)) {
                Count++;
                CatNode Temp = this.same;
                while (Temp != null) {
                    Count++;
                    Temp = Temp.same;
                }

            }

            if (this.junior != null) {
                if (this.junior.data.monthHired <= monthMax) {
                    Count = this.junior.hiredFromMonthsHelper(Count, monthMin, monthMax);
                }
            }

            return Count;
        }

        public CatInfo fluffiestFromMonth(int month) {
            if(this.data.monthHired == month){
                return this.data;
            }
            else if(month > this.data.monthHired){
                if(this.junior != null){
                    this.junior.fluffiestFromMonth(month);
                }
            }
            else{
                if(this.senior != null){
                    this.senior.fluffiestFromMonth(month);
                }
            }
            return null; // DON'T FORGET TO MODIFY THE RETURN IF NEED BE
        }

        public int[] costPlanning(int nbMonths) {
            int[] CostPlan = new int[nbMonths];
            CatInfo ThisCat;
            for(int i = 0; i<nbMonths; i++){
                CostPlan[i] = this.CostCalculator(nbMonths);
            }
            return CostPlan; // DON'T FORGET TO MODIFY THE RETURN IF NEED BE
        }

        private int CostCalculator(int Months){
            Integer TotalCost = 0;
            TotalCost = this.TraverseInOrder(TotalCost, Months);
            return TotalCost;
        }

        private int TraverseInOrder(Integer Cost, int Month){
            if(this.senior != null){
                this.senior.TraverseInOrder(Cost, Month);
            }
            if(this.data.nextGroomingAppointment == (243 + Month)){
                Cost = Cost + this.data.expectedGroomingCost;
            }
            if(this.same != null){
                CatNode Temp = this.same;
                while(Temp != null){
                    if(Temp.data.nextGroomingAppointment == (243 + Month)){
                        Cost = Cost + this.data.expectedGroomingCost;
                    }
                    Temp = Temp.same;
                }
            }
            if(this.junior != null){
                this.junior.TraverseInOrder(Cost, Month);
            }
            return Cost;
        }

    }



    private class CatTreeIterator implements Iterator<CatInfo> {
        public ArrayList<CatInfo> TreeList;
        public int Index;

        public CatTreeIterator() {
            this.TreeList = treeListCreator(root);
            this.Index = 0;
        }

        @Override
        public CatInfo next(){
            if(this.hasNext()){
                this.Index--;
                return this.TreeList.get(this.Index + 1)
            }
            else{
                throw new NoSuchElementException();
            }
        }

        @Override
        public boolean hasNext() {
            if(this.Index < 0){
                return false;
            }
            else{
                return true;
            }

        }

        private ArrayList<CatInfo> treeListCreator(CatNode Root){
            ArrayList<CatInfo> TreeList = new ArrayList<CatInfo>();
            if(Root != null){
                traverseInOrder(Root, TreeList);
            }
            return TreeList;
        }

        private void traverseInOrder(CatNode Root, ArrayList<CatInfo> MyList){
            if(Root.senior != null){
                traverseInOrder(Root.senior, MyList);
            }

            CatNode Temp = Root;
            ArrayList<CatInfo> SameList = new ArrayList<CatInfo>();
            while(Temp.same != null){
                SameList.add(Temp.same.data);
                Temp = Temp.same;
            }
            while(!SameList.isEmpty()){
                MyList.add(SameList.remove(SameList.size()-1));
                this.Index++;
            }
            MyList.add(Root.data);
            this.Index++;

            if(Root.junior != null){
                traverseInOrder(Root.junior, MyList);
            }
        }


    }


}

