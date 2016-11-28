package data;

import java.util.ArrayList;

/**
 * Created by stebbun on 11/27/2016.
 */
public class SquareCoordinateGenerator {
    ArrayList<Coordinate> coordinateList = new ArrayList<Coordinate>();
    int range;

    public SquareCoordinateGenerator(int range){
        this.range = range;
        generatePermutations();
    }

    private void generatePermutations(){
        for(int x = 0; x < range; x++)
            for(int y = 0; y < range; y++)
                coordinateList.add(new Coordinate(x,y));
    }

    public ArrayList<Coordinate> getCoordinateList() {
        return coordinateList;
    }

    public void setCoordinateList(ArrayList<Coordinate> coordinateList) {
        this.coordinateList = coordinateList;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }
}
